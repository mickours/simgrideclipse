package simgrideclipseplugin.wizards;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.List;

import org.eclipse.core.commands.Command;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IAccessRule;
import org.eclipse.jdt.core.IClasspathAttribute;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

import simgrideclipseplugin.Activator;

public class SimgridProjectWizard extends BasicNewProjectResourceWizard {
	
	private SimgridProjectWizardPage page2;

	private IProject newProject;
	private final static String description = "This wizard creates a new SimGrid MSG project";
	private final static String title = "Simgrid project creation";
	private final static String deployementRadical = "deployement";
	private final static String platformRadical = "platform";
	private final static String sufix = ".xml";
	
	private final static String deploymentTemplatePath = "deployment_template.xml";
	private final static String platformTemplatePath = "platform_template.xml";
	private final static String tips = "//TODO add your code here\n";

	private static final String mainCTemplatePath = "c_main_template.c";
	private static final String mainJavaTemplatePath = "TemplateMain.java";
	private static final String functionJavaTemplatePath = "TemplateFunction.java";

	
	
	@Override
	public void addPages() {
		super.addPages();
		page2 = new SimgridProjectWizardPage(title);
		page2.setDescription(description);
		addPage(page2);
	}

	@Override
	public boolean performFinish() {
		if (!super.performFinish()){
			return false;
		}
		newProject = getNewProject();
		
	   
	   

		final List<String> funcL = page2.getFunctionNames();
		final String language = page2.getLanguage();
		//create the new file template
		final String projectName = newProject.getName();
		final String depFileName = projectName+"_"+deployementRadical+sufix;
		final String platFileName = projectName+"_"+platformRadical+sufix;
		final String fileNameC = projectName+"_"+"main.c";
		final String fileNameJava = projectName+"Main.java";
		try {
			IProjectDescription description = newProject.getDescription();
			// TODO do the same for C project
			if (language.equals("Java")) {

				String[] natures = description.getNatureIds();
				String[] newNatures = new String[natures.length + 2];
				System.arraycopy(natures, 0, newNatures, 0, natures.length);
				newNatures[newNatures.length - 2] = org.eclipse.jdt.core.JavaCore.NATURE_ID;
				newNatures[newNatures.length - 1] = "org.eclipse.wst.common.project.facet.core.nature";

				// newNatures[natures.length] = "org.eclipse.cdt.core.cnature";
				description.setNatureIds(newNatures);

				ICommand[] commands = description.getBuildSpec();
				ICommand[] newCommands = new ICommand[commands.length + 2];
				ICommand newCommand = description.newCommand();
				newCommand.setBuilderName("org.eclipse.jdt.core.javabuilder");
				newCommands[newCommands.length - 2] = newCommand;
				newCommand = description.newCommand();
				newCommand
						.setBuilderName("org.eclipse.wst.common.project.facet.core.builder");
				newCommands[newCommands.length - 1] = newCommand;
				System.arraycopy(commands, 0, newCommands, 0, commands.length);
				description.setBuildSpec(newCommands);

			} else {
				String[] newNatures = new String[] { "org.eclipse.cdt.core.cnature" };
				description.setNatureIds(newNatures);
			}
			newProject.setDescription(description, null);
		} catch (CoreException e) {
			MessageDialog.openError(getShell(), "Error", e.getMessage());
		}
		
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					//create xml
					createFile(getDeploymentTemplate(), depFileName, monitor);
					IFile toOpenFile = createFile(getPlatformTemplate(), platFileName, monitor);
					//create code template
					if (language.equals("C")){
						createFile(getMainCTemplate(funcL,projectName), fileNameC, monitor);
					}
					else if(language.equals("Java")){
						String srcPath = createFolder(monitor, "src");
						String pkgPath = createFolder(monitor, "src/"+projectName);
						createFile(getMainJavaTemplate(projectName), fileNameJava, monitor, pkgPath);
						for (String fun : funcL){
							createFile(getFunctionJavaTemplate(fun,projectName), fun+".java", monitor, pkgPath);
						}
						IClasspathEntry src = JavaCore.newSourceEntry(new Path(srcPath));
						IClasspathEntry jre = JavaCore.newContainerEntry(
								new Path(JavaRuntime.JRE_CONTAINER),
								new IAccessRule[0], 
								new IClasspathAttribute[] 
										{ JavaCore.newClasspathAttribute("owner.project.facets", "java")},
								false);
						IClasspathEntry[] entries = new IClasspathEntry[] { src, jre };
						IJavaProject javaProject = JavaCore.create(newProject);
						try {
							javaProject.setRawClasspath(entries, newProject.getFullPath().append("bin"), new NullProgressMonitor());
						} catch (JavaModelException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
//					else if(page2.getLanguage().equals("Java")){
//						String fileName = newProject.getName()+"Main.java";
//						createFile(getMainCTemplate(funcL), fileName, monitor);
//					}
					openFile(toOpenFile, monitor);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		
		
		try {
			getContainer().run(true, false, op);
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error", realException.getMessage());
			return false;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	private InputStream getDeploymentTemplate() {
		return new ByteArrayInputStream(openFileStream(deploymentTemplatePath).getBytes());
	}

	private InputStream getPlatformTemplate() {
		return new ByteArrayInputStream(openFileStream(platformTemplatePath).getBytes());
	}
	
	private InputStream getMainCTemplate(List<String> functionsList, String projectName) {
		String tmp = openFileStream(mainCTemplatePath);
		
		tmp = tmp.replaceAll("<PROJECT_NAME>", projectName);
		
		String functionsString ="";
		for (String fun : functionsList){
			functionsString += "int "+fun+"(int argc, char *argv[])\n{\n\t"+tips+"}\n";
		}
		tmp = tmp.replaceAll("<FUNCTIONS>", functionsString);
		
		functionsString ="";
		for (String fun : functionsList){
			functionsString += "MSG_function_register(\""+fun+"\", "+fun+");\n\t";
		}
		tmp = tmp.replaceAll("<FUNCTION_REGISTER>", functionsString);
		
		return new ByteArrayInputStream(tmp.getBytes());
	}
	
	private InputStream getMainJavaTemplate(String projectName) {
		String tmp = openFileStream(mainJavaTemplatePath);
		tmp = tmp.replaceAll("<PACKAGE_NAME>", projectName);
		return new ByteArrayInputStream(tmp.getBytes());
	}

	
	private InputStream getFunctionJavaTemplate(String functionName, String projectName) {
		String tmp = openFileStream(functionJavaTemplatePath);
		tmp = tmp.replaceAll("<PACKAGE_NAME>", projectName);
		tmp = tmp.replaceAll("<FUNCTION_NAME>", functionName);
		return new ByteArrayInputStream(tmp.getBytes());
	}
	
	private String openFileStream(String path) {
		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader = null;
		try {
			URI uri = FileLocator.toFileURL(
					Platform.getBundle(Activator.PLUGIN_ID).getEntry(
							"/templates/" + path)).toURI();
			File file = new File(uri);

			reader = new BufferedReader(new FileReader(file));

			char[] buf = new char[1024];
			int numRead = 0;
			while ((numRead = reader.read(buf)) != -1) {
				String readData = String.valueOf(buf, 0, numRead);
				fileData.append(readData);
				buf = new char[1024];
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				// do nothing
			}
		}
		return fileData.toString();
	}

//		byte[] buffer = null;
//		try {
//			URI uri = FileLocator.toFileURL(Platform.getBundle(Activator.PLUGIN_ID).getEntry("/templates/"+path)).toURI();
//			File file = new File(uri);
//			FileInputStream in = new FileInputStream(file);
//			buffer = new byte[1024];
//			in.read(buffer);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (URISyntaxException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	
	 private void openFile(final IFile fileToOpen, IProgressMonitor monitor ){
			monitor.setTaskName("Opening file for editing...");
			getShell().getDisplay().asyncExec(new Runnable() {
				public void run() {
					IWorkbenchPage page =
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					try {
						IDE.openEditor(page, fileToOpen, "simgrideclipseplugin.editors.MultiPageSimgridEditor", true);
					} catch (PartInitException e) {
						e.printStackTrace();
					}
				}
			});
			monitor.worked(1);
	    }
		/**
		 * The worker method. It will find the container, create the
		 * file if missing or just replace its contents, and open
		 * the editor on the newly created file.
		 */

		private IFile createFile(InputStream initContentStream,
				String fileName,
				IProgressMonitor monitor,
				String path)
						throws CoreException {
			// create a sample file
			monitor.beginTask("Creating " + fileName, 2);
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IResource resource = root.findMember(path);
			if (!resource.exists() || !(resource instanceof IContainer)) {
				throwCoreException("Container does not exist.");
			}
			IContainer container = (IContainer) resource;
			final IFile file = container.getFile(new Path(fileName));
			try {
				if (file.exists()) {
					file.setContents(initContentStream, true, true, monitor);
				} else {
					file.create(initContentStream, true, monitor);
				}
				initContentStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			monitor.worked(1);
			return file;
		}
		
		private IFile createFile(InputStream initContentStream,
				String fileName,
				IProgressMonitor monitor) throws CoreException{
			return createFile(initContentStream,fileName,monitor,newProject.getFullPath().toString());
			
		}
		
		private String createFolder(IProgressMonitor monitor, String folderName)
						throws CoreException {
			// create a sample file
			monitor.beginTask("Creating folder", 2);
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IResource resource = root.findMember(newProject.getFullPath());
			if (!resource.exists() || !(resource instanceof IContainer)) {
				throwCoreException("Container does not exist.");
			}
			IContainer container = (IContainer) resource;
			IFolder folder = container.getFolder(new Path(folderName));
			folder.create(true, true, monitor);
			monitor.worked(1);
			return folder.getFullPath().toString();
		}
		

		private void throwCoreException(String message) throws CoreException {
			IStatus status =
				new Status(IStatus.ERROR, "simgridEclipsePlugin", IStatus.OK, message, null);
			throw new CoreException(status);
		}
		
//		/*
//		 * Create simple project.
//		 */
//		protected IProject createProject(final String projectName) throws CoreException {
//			final IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
//			IWorkspaceRunnable create = new IWorkspaceRunnable() {
//				public void run(IProgressMonitor monitor) throws CoreException {
//					project.create(null);
//					project.open(null);
//				}
//			};
//			ResourcesPlugin.getWorkspace().run(create, null);
//			return project;
//		}
//		
//		protected IWorkspaceRoot getWorkspaceRoot(){
//			return ResourcesPlugin.getWorkspace().getRoot();
//		}
//		
//		protected IProject getProject(String project) {
//			return getWorkspaceRoot().getProject(project);
//		}
//		
//		
//		protected void addJavaNature(String projectName) throws CoreException {
//			IProject project = getWorkspaceRoot().getProject(projectName);
//			IProjectDescription description = project.getDescription();
//			description.setNatureIds(new String[] {JavaCore.NATURE_ID});
//			project.setDescription(description, null);
//		}
//		
//		public void setUpJCLClasspathVariables(String compliance) throws JavaModelException, IOException {
//			if ("1.5".equals(compliance)) {
//				if (JavaCore.getClasspathVariable("JCL15_LIB") == null) {
//					setupExternalJCL("jclMin1.5");
//					JavaCore.setClasspathVariables(
//						new String[] {"JCL15_LIB", "JCL15_SRC", "JCL_SRCROOT"},
////						new IPath[] {getExternalJCLPath(compliance), getExternalJCLSourcePath(compliance), getExternalJCLRootSourcePath()},
//						null);
//				}
//			} else {
//				if (JavaCore.getClasspathVariable("JCL_LIB") == null) {
//					setupExternalJCL("jclMin");
//					JavaCore.setClasspathVariables(
//						new String[] {"JCL_LIB", "JCL_SRC", "JCL_SRCROOT"},
//						new IPath[] {getExternalJCLPath(), getExternalJCLSourcePath(), getExternalJCLRootSourcePath()},
//						null);
//				}
//			}
//		}
//		
//		/**
//		 * Check locally for the required JCL files, <jclName>.jar and <jclName>src.zip.
//		 * If not available, copy from the project resources.
//		 */
//		public void setupExternalJCL(String jclName) throws IOException {
//			String externalPath = getExternalPath();
//			String separator = java.io.File.separator;
//			String resourceJCLDir = getPluginDirectoryPath() + separator + "JCL";
//			java.io.File jclDir = new java.io.File(externalPath);
//			java.io.File jclMin =
//				new java.io.File(externalPath + jclName + ".jar");
//			java.io.File jclMinsrc = new java.io.File(externalPath + jclName + "src.zip");
//			if (!jclDir.exists()) {
//				if (!jclDir.mkdir()) {
//					//mkdir failed
//					throw new IOException("Could not create the directory " + jclDir);
//				}
//				//copy the two files to the JCL directory
//				java.io.File resourceJCLMin =
//					new java.io.File(resourceJCLDir + separator + jclName + ".jar");
//				copy(resourceJCLMin, jclMin);
//				java.io.File resourceJCLMinsrc =
//					new java.io.File(resourceJCLDir + separator + jclName + "src.zip");
//				copy(resourceJCLMinsrc, jclMinsrc);
//			} else {
//				//check that the two files, jclMin.jar and jclMinsrc.zip are present
//				//copy either file that is missing or less recent than the one in workspace
//				java.io.File resourceJCLMin =
//					new java.io.File(resourceJCLDir + separator + jclName + ".jar");
//
//			}
//		}
//
//		protected IJavaProject createJavaProject(
//				final String projectName,
//				final String[] sourceFolders,
//				final String[] libraries,
//				final String[][] librariesInclusionPatterns,
//				final String[][] librariesExclusionPatterns,
//				final String[] projects,
//				final String[][] projectsInclusionPatterns,
//				final String[][] projectsExclusionPatterns,
//				final boolean combineAccessRestrictions,
//				final boolean[] exportedProjects,
//				final String projectOutput,
//				final String[] sourceOutputs,
//				final String[][] inclusionPatterns,
//				final String[][] exclusionPatterns,
//				final String compliance,
//				final boolean simulateImport) throws CoreException {
//			final IJavaProject[] result = new IJavaProject[1];
//			IWorkspaceRunnable create = new IWorkspaceRunnable() {
//				public void run(IProgressMonitor monitor) throws CoreException {
//					// create project
//					createProject(projectName);
//
//					// set java nature
//					addJavaNature(projectName);
//
//					// create classpath entries
//					IProject project = getWorkspaceRoot().getProject(projectName);
//					IPath projectPath = project.getFullPath();
//					int sourceLength = sourceFolders == null ? 0 : sourceFolders.length;
//					int libLength = libraries == null ? 0 : libraries.length;
//					int projectLength = projects == null ? 0 : projects.length;
//					IClasspathEntry[] entries = new IClasspathEntry[sourceLength+libLength+projectLength];
//					for (int i= 0; i < sourceLength; i++) {
//						IPath sourcePath = new Path(sourceFolders[i]);
//						int segmentCount = sourcePath.segmentCount();
//						if (segmentCount > 0) {
//							// create folder and its parents
//							IContainer container = project;
//							for (int j = 0; j < segmentCount; j++) {
//								IFolder folder = container.getFolder(new Path(sourcePath.segment(j)));
//								if (!folder.exists()) {
//									folder.create(true, true, null);
//								}
//								container = folder;
//							}
//						}
//						IPath outputPath = null;
//						if (sourceOutputs != null) {
//							// create out folder for source entry
//							outputPath = sourceOutputs[i] == null ? null : new Path(sourceOutputs[i]);
//							if (outputPath != null && outputPath.segmentCount() > 0) {
//								IFolder output = project.getFolder(outputPath);
//								if (!output.exists()) {
//									output.create(true, true, null);
//								}
//							}
//						}
//						// inclusion patterns
//						IPath[] inclusionPaths;
//						if (inclusionPatterns == null) {
//							inclusionPaths = new IPath[0];
//						} else {
//							String[] patterns = inclusionPatterns[i];
//							int length = patterns.length;
//							inclusionPaths = new IPath[length];
//							for (int j = 0; j < length; j++) {
//								String inclusionPattern = patterns[j];
//								inclusionPaths[j] = new Path(inclusionPattern);
//							}
//						}
//						// exclusion patterns
//						IPath[] exclusionPaths;
//						if (exclusionPatterns == null) {
//							exclusionPaths = new IPath[0];
//						} else {
//							String[] patterns = exclusionPatterns[i];
//							int length = patterns.length;
//							exclusionPaths = new IPath[length];
//							for (int j = 0; j < length; j++) {
//								String exclusionPattern = patterns[j];
//								exclusionPaths[j] = new Path(exclusionPattern);
//							}
//						}
//						// create source entry
//						entries[i] =
//							JavaCore.newSourceEntry(
//								projectPath.append(sourcePath),
//								inclusionPaths,
//								exclusionPaths,
//								outputPath == null ? null : projectPath.append(outputPath)
//							);
//					}
//					for (int i= 0; i < libLength; i++) {
//						String lib = libraries[i];
//						if (lib.startsWith("JCL")) {
//							try {
//								// ensure JCL variables are set
//								setUpJCLClasspathVariables(compliance);
//							} catch (IOException e) {
//								e.printStackTrace();
//							}
//						}
//
//						// accessible files
//						IPath[] accessibleFiles;
//						if (librariesInclusionPatterns == null) {
//							accessibleFiles = new IPath[0];
//						} else {
//							String[] patterns = librariesInclusionPatterns[i];
//							int length = patterns.length;
//							accessibleFiles = new IPath[length];
//							for (int j = 0; j < length; j++) {
//								String inclusionPattern = patterns[j];
//								accessibleFiles[j] = new Path(inclusionPattern);
//							}
//						}
//						// non accessible files
//						IPath[] nonAccessibleFiles;
//						if (librariesExclusionPatterns == null) {
//							nonAccessibleFiles = new IPath[0];
//						} else {
//							String[] patterns = librariesExclusionPatterns[i];
//							int length = patterns.length;
//							nonAccessibleFiles = new IPath[length];
//							for (int j = 0; j < length; j++) {
//								String exclusionPattern = patterns[j];
//								nonAccessibleFiles[j] = new Path(exclusionPattern);
//							}
//						}
//						if (lib.indexOf(File.separatorChar) == -1 && lib.charAt(0) != '/' && lib.equals(lib.toUpperCase())) { // all upper case is a var
//							char[][] vars = CharOperation.splitOn(',', lib.toCharArray());
//							entries[sourceLength+i] = JavaCore.newVariableEntry(
//								new Path(new String(vars[0])),
//								vars.length > 1 ? new Path(new String(vars[1])) : null,
//								vars.length > 2 ? new Path(new String(vars[2])) : null,
//								ClasspathEntry.getAccessRules(accessibleFiles, nonAccessibleFiles), // ClasspathEntry.NO_ACCESS_RULES,
//								ClasspathEntry.NO_EXTRA_ATTRIBUTES,
//								false);
//						} else if (lib.startsWith("org.eclipse.jdt.core.tests.model.")) { // container
//							entries[sourceLength+i] = JavaCore.newContainerEntry(
//									new Path(lib),
//									ClasspathEntry.getAccessRules(accessibleFiles, nonAccessibleFiles),
//									new IClasspathAttribute[0],
//									false);
//						} else {
//							IPath libPath = new Path(lib);
//							if (!libPath.isAbsolute() && libPath.segmentCount() > 0 && libPath.getFileExtension() == null) {
//								project.getFolder(libPath).create(true, true, null);
//								libPath = projectPath.append(libPath);
//							}
//							entries[sourceLength+i] = JavaCore.newLibraryEntry(
//									libPath,
//									null,
//									null,
//									ClasspathEntry.getAccessRules(accessibleFiles, nonAccessibleFiles),
//									new IClasspathAttribute[0],
//									false);
//						}
//					}
//					for  (int i= 0; i < projectLength; i++) {
//						boolean isExported = exportedProjects != null && exportedProjects.length > i && exportedProjects[i];
//
//						// accessible files
//						IPath[] accessibleFiles;
//						if (projectsInclusionPatterns == null) {
//							accessibleFiles = new IPath[0];
//						} else {
//							String[] patterns = projectsInclusionPatterns[i];
//							int length = patterns.length;
//							accessibleFiles = new IPath[length];
//							for (int j = 0; j < length; j++) {
//								String inclusionPattern = patterns[j];
//								accessibleFiles[j] = new Path(inclusionPattern);
//							}
//						}
//						// non accessible files
//						IPath[] nonAccessibleFiles;
//						if (projectsExclusionPatterns == null) {
//							nonAccessibleFiles = new IPath[0];
//						} else {
//							String[] patterns = projectsExclusionPatterns[i];
//							int length = patterns.length;
//							nonAccessibleFiles = new IPath[length];
//							for (int j = 0; j < length; j++) {
//								String exclusionPattern = patterns[j];
//								nonAccessibleFiles[j] = new Path(exclusionPattern);
//							}
//						}
//
//						entries[sourceLength+libLength+i] =
//							JavaCore.newProjectEntry(
//									new Path(projects[i]),
//									ClasspathEntry.getAccessRules(accessibleFiles, nonAccessibleFiles),
//									combineAccessRestrictions,
//									new IClasspathAttribute[0],
//									isExported);
//					}
//
//					// create project's output folder
//					IPath outputPath = new Path(projectOutput);
//					if (outputPath.segmentCount() > 0) {
//						IFolder output = project.getFolder(outputPath);
//						if (!output.exists()) {
//							output.create(true, true, monitor);
//						}
//					}
//
//					// set classpath and output location
//					JavaProject javaProject = (JavaProject) JavaCore.create(project);
//					if (simulateImport)
//						javaProject.writeFileEntries(entries, projectPath.append(outputPath));
//					else
//						javaProject.setRawClasspath(entries, projectPath.append(outputPath), monitor);
//
//					// set compliance level options
//					if ("1.5".equals(compliance)) {
//						Map options = new HashMap();
//						options.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
//						options.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
//						options.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
//						javaProject.setOptions(options);
//					}
//
//					result[0] = javaProject;
//				}
//			};
//			getWorkspace().run(create, null);
//			return result[0];
//		}
	
}

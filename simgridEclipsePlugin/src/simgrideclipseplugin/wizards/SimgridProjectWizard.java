package simgrideclipseplugin.wizards;

import java.awt.Container;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

import simgrideclipseplugin.Activator;
import simgrideclipseplugin.editors.SimgridGraphicEditor;

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
						String path = createFolder(monitor, projectName);
						createFile(getMainJavaTemplate(projectName), fileNameJava, monitor, path);
						for (String fun : funcL){
							createFile(getFunctionJavaTemplate(fun,projectName), fun+".java", monitor, path);
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
			folder.create(false, true, monitor);
			monitor.worked(1);
			return folder.getFullPath().toString();
		}
		

		private void throwCoreException(String message) throws CoreException {
			IStatus status =
				new Status(IStatus.ERROR, "simgridEclipsePlugin", IStatus.OK, message, null);
			throw new CoreException(status);
		}

	
}

package simgrideclipseplugin.wizards;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IAccessRule;
import org.eclipse.jdt.core.IClasspathAttribute;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.JavaRuntime;

public class SimgridJavaProjectWizard extends SimgridAbstractProjectWizard {
	
	public static final String LIB_LOCATION = "simgrid.lib.location";
	
	private static final String mainJavaTemplatePath = "TemplateMain.java";
	private static final String functionJavaTemplatePath = "TemplateFunction.java";
	

	public SimgridJavaProjectWizard() {
		super(new SimgridJavaProjectWizardPage());
	}

	@Override
	protected IProject initializeNewProject(IProject project,
			List<String> funcL,
			Map<String,Object> args,
			IProgressMonitor monitor)throws CoreException {
		final String projectName = project.getName();
		final String fileNameJava =projectName +"Main.java";
		project = createNewJavaProject(project,(Path) args.get(LIB_LOCATION),monitor);
		String pkgPath = projectUtils.createFolder(monitor, "src/"+projectName);
		projectUtils.createFile(getMainJavaTemplate(projectName), fileNameJava, monitor, pkgPath);
		for (String fun : funcL){
			if (!fun.isEmpty()){
				projectUtils.createFile(getFunctionJavaTemplate(fun,projectName), fun+".java", monitor, pkgPath);
			}
		}
		return project;
	}
	
	private InputStream getMainJavaTemplate(String projectName) {
		String tmp = projectUtils.openFileStream(mainJavaTemplatePath);
		tmp = tmp.replaceAll("<PACKAGE_NAME>", projectName);
		return new ByteArrayInputStream(tmp.getBytes());
	}

	
	private InputStream getFunctionJavaTemplate(String functionName, String projectName) {
		String tmp = projectUtils.openFileStream(functionJavaTemplatePath);
		tmp = tmp.replaceAll("<PACKAGE_NAME>", projectName);
		tmp = tmp.replaceAll("<FUNCTION_NAME>", functionName);
		return new ByteArrayInputStream(tmp.getBytes());
	}
	

	protected IProject createNewJavaProject(IProject newProject, Path lib,IProgressMonitor monitor) throws CoreException {
		try {
			IProjectDescription description = newProject.getDescription();
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
			
			newProject.setDescription(description, monitor);
			
			//create folders
			String srcPath = projectUtils.createFolder(monitor, "src");
			IClasspathEntry src = JavaCore.newSourceEntry(new Path(srcPath));
			//set JRE
			IClasspathEntry jre = JavaCore.newContainerEntry(
					new Path(JavaRuntime.JRE_CONTAINER),
					new IAccessRule[0], 
					new IClasspathAttribute[] 
							{ JavaCore.newClasspathAttribute("owner.project.facets", "java")},
					false);
			IClasspathEntry path = JavaCore.newLibraryEntry(lib, null, null);
			IClasspathEntry[] entries = new IClasspathEntry[] { src, jre, path };
			//create Project
			IJavaProject javaProject = JavaCore.create(newProject);

			javaProject.setRawClasspath(entries, newProject.getFullPath().append("bin"), monitor);
			JavaCore.setClasspathVariable("java.library.path", lib, monitor);
		
		} catch (JavaModelException e1) {
			ProjectWizardsUtils.throwCoreException(e1.toString());
			e1.printStackTrace();
		}
		return newProject;
	}
	
}

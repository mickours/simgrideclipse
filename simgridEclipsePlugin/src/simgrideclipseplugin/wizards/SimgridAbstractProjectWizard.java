package simgrideclipseplugin.wizards;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

public abstract class  SimgridAbstractProjectWizard extends BasicNewProjectResourceWizard {
	

	private final static String deployementRadical = "deployment";
	private final static String platformRadical = "platform";
	private final static String sufix = ".xml";
	
	private final static String deploymentTemplatePath = "deployment_template.xml";
	private final static String platformTemplatePath = "platform_template.xml";

	protected final static String description = "This wizard creates a new SimGrid MSG project";
	protected final static String title = "Simgrid project creation";
	
	protected SimgridAbstractProjectWizardPage projectSpecWizardPage;
	protected ProjectWizardsUtils projectUtils;
	/**
	 * must be set by the sub class if there is an error during initialization
	 */
	public String initErrorMessage;
	
	public SimgridAbstractProjectWizard(SimgridAbstractProjectWizardPage projectSpecWizardPage) {
		super();
		this.projectSpecWizardPage = projectSpecWizardPage;
	}

	@Override
	public void addPages() {
		super.addPages();
		//maybe add an error WizardPage
		projectSpecWizardPage.init();
		if (initErrorMessage != null){
			MessageBox mb = new MessageBox(getShell());
			mb.setMessage(initErrorMessage);
			mb.open();
		}
		else{
			addPage(projectSpecWizardPage);
		}
	}

	@Override
	public boolean performFinish() {
		if (!super.performFinish()){
			return false;
		}

		final List<String> funcL = projectSpecWizardPage.getFunctionNames();
		//create the new file template
		final IProject newProject= getNewProject();
		final String projectName = newProject.getName();
		final String depFileName = projectName+"_"+deployementRadical+sufix;
		final String platFileName = projectName+"_"+platformRadical+sufix;
		final Map<String,Object> args = projectSpecWizardPage.getArgsMap();


		projectUtils = new ProjectWizardsUtils(getShell(), getNewProject());
		
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				
				try {
					IProject project = null;
					//create xml
					projectUtils.createFile(getDeploymentTemplate(), depFileName, monitor);
					IFile toOpenFile = projectUtils.createFile(getPlatformTemplate(), platFileName, monitor);
					//create code template
					project = initializeNewProject(newProject, funcL,args, monitor);
					project.open(IResource.BACKGROUND_REFRESH,monitor);
			        projectUtils.openFile(toOpenFile, monitor);
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
		return new ByteArrayInputStream(projectUtils.openFileStream(deploymentTemplatePath).getBytes());
	}

	private InputStream getPlatformTemplate() {
		return new ByteArrayInputStream(projectUtils.openFileStream(platformTemplatePath).getBytes());
	}
	
	/**
	 * initialize and return the new project already created by the first Wizard page
	 * @param project: the new project already created by the first Wizard page
	 * @param funcL: the user function to add to the templates
	 * @param args: a map of argument depending on the project type
	 */
	protected abstract IProject initializeNewProject(
			IProject project,
			List<String> funcL,
			Map<String,Object> args,
			IProgressMonitor monitor)throws CoreException;
}
		
		
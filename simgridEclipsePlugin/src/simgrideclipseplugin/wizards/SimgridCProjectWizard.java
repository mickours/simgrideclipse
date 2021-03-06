package simgrideclipseplugin.wizards;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.ICDescriptor;
import org.eclipse.cdt.managedbuilder.core.BuildException;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.IManagedBuildInfo;
import org.eclipse.cdt.managedbuilder.core.IManagedProject;
import org.eclipse.cdt.managedbuilder.core.IOption;
import org.eclipse.cdt.managedbuilder.core.IProjectType;
import org.eclipse.cdt.managedbuilder.core.ITool;
import org.eclipse.cdt.managedbuilder.core.IToolChain;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.cdt.managedbuilder.core.ManagedCProjectNature;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.junit.Assert;

import simgrideclipseplugin.Activator;

@SuppressWarnings("deprecation")
public class SimgridCProjectWizard extends SimgridAbstractProjectWizard {

	public static final String TOOL_CHAIN = "simgrideclipse.project.toolchain";
	
	private static final String tips = "//TODO add your code here\n";
	private static final String mainCTemplatePath = "c_main_template.c";
	private static final String oldMainCTemplatePath = "old_c_main_template.c";

	public static final String IS_38_OR_MORE = "simgrid.project.is38orMore";

	public static final String SIMGRID_ROOT = "simgrid.project.simgridRoot";
		
	
	public SimgridCProjectWizard() {
		super(new SimgridCProjectWizardPage());
	}
	
	@Override
	protected IProject initializeNewProject(IProject project,
			List<String> funcL,
			Map<String,Object> args,
			IProgressMonitor monitor)throws CoreException {
		String projectName = project.getName();
		String fileNameC = projectName+"_"+"main.c";
		project = createNewCProject((String) args.get(TOOL_CHAIN),(String) args.get(SIMGRID_ROOT),monitor);
		String template= null;
		if ((Boolean) args.get(IS_38_OR_MORE)){
			template = mainCTemplatePath;
		}
		else{
			template = oldMainCTemplatePath;
		}
		projectUtils.createFile(getMainCTemplate(funcL,projectName,template), fileNameC, monitor);
		return project;
	}
	
	private IProject createNewCProject(final String projectTypeId,final String simgriRoot,IProgressMonitor monitor) throws CoreException {
			IProject newProjectHandle = getNewProject();
			final IPath location = getNewProject().getLocation();
			final String projectId = getNewProject().getName();
			final IWorkspace workspace = ResourcesPlugin.getWorkspace();
			IWorkspaceRoot root = workspace.getRoot();
			final IProject project = newProjectHandle;
			IWorkspaceRunnable runnable = new IWorkspaceRunnable() {
				@Override
				public void run(IProgressMonitor monitor) throws CoreException {
					// Create the base project
					IWorkspaceDescription workspaceDesc = workspace.getDescription();
					workspaceDesc.setAutoBuilding(false);
					workspace.setDescription(workspaceDesc);
					IProjectDescription description = workspace.newProjectDescription(project.getName());
					if (location != null) {
						description.setLocation(location);
					}
					CCorePlugin.getDefault().createCProject(description, project, monitor, projectId);
					// Add the managed build nature and builder
					addManagedBuildNature(project);

					// Find the base project type definition
					IProjectType projType = ManagedBuildManager.getProjectType(projectTypeId);
					if (projType == null)ProjectWizardsUtils.throwCoreException("projType is null");

					// Create the managed-project (.cdtbuild) for our project that builds an executable.
					IManagedProject newProject = null;
					try {
						newProject = ManagedBuildManager.createManagedProject(project, projType);
					} catch (Exception e) {
						ProjectWizardsUtils.throwCoreException("Failed to create managed project for: " + project.getName());
						return;
					}
					Assert.assertEquals(newProject.getName(), projType.getName());
					Assert.assertFalse(newProject.equals(projType));
					ManagedBuildManager.setNewProjectVersion(project);
					// Copy over the configs
					IConfiguration defaultConfig = null;
					IConfiguration[] configs = projType.getConfigurations();
					for (int i = 0; i < configs.length; ++i) {
						// Make the first configuration the default
						if (i == 0) {
							defaultConfig = newProject.createConfiguration(configs[i], projType.getId() + "." + i);
						} else {
							newProject.createConfiguration(configs[i], projType.getId() + "." + i);
						}
					}
					ManagedBuildManager.setDefaultConfiguration(project, defaultConfig);
					IConfiguration cfgs[] = newProject.getConfigurations();
					for(int i = 0; i < cfgs.length; i++){
						cfgs[i].setArtifactName(newProject.getDefaultArtifactName());
						
					}
					 IToolChain toolChain = cfgs[0].getToolChain();
					 ITool[] tool = toolChain.getToolsBySuperClassId("cdt.managedbuild.tool.gnu.c.linker");
					 IOption option = tool[0].getOptionById("gnu.c.link.option.libs");
					 try {
						 option.setValue(new String[]{"simgrid"});
						 option = tool[0].getOptionById("gnu.c.link.option.paths");
						 option.setValue(new String[]{simgriRoot+File.separator+"lib"});
					} catch (BuildException e) {
						throw new CoreException(new Status(Status.ERROR, Activator.PLUGIN_ID,
								"Error while building project:\n"+e.getMessage()));
					}					
					ManagedBuildManager.getBuildInfo(project).setValid(true);
				}
			};
			try {
				workspace.run(runnable, root, IWorkspace.AVOID_UPDATE, monitor);
			} catch (CoreException e2) {
				ProjectWizardsUtils.throwCoreException(e2.getLocalizedMessage());
			}
			// CDT opens the Project with BACKGROUND_REFRESH enabled which causes the
			// refresh manager to refresh the project 200ms later.  This Job interferes
			// with the resource change handler firing see: bug 271264
			try {
				// CDT opens the Project with BACKGROUND_REFRESH enabled which causes the
				// refresh manager to refresh the project 200ms later.  This Job interferes
				// with the resource change handler firing see: bug 271264
				Job.getJobManager().join(ResourcesPlugin.FAMILY_AUTO_REFRESH, null);
			} catch (Exception e) {
				// Ignore
			}

			// Initialize the path entry container
			IStatus initResult = ManagedBuildManager.initBuildInfoContainer(project);
			if (initResult.getCode() != IStatus.OK) {
				ProjectWizardsUtils.throwCoreException("Initializing build information failed for: " + project.getName() + " because: " + initResult.getMessage());
			}
			return project;
		}
	
	private void addManagedBuildNature (IProject project) throws CoreException {
		// Create the buildinformation object for the project
		IManagedBuildInfo info = ManagedBuildManager.createBuildInfo(project);
		if (info == null) ProjectWizardsUtils.throwCoreException("build info = null");

		// Add the managed build nature
		try {
			ManagedCProjectNature.addManagedNature(project, new NullProgressMonitor());
			ManagedCProjectNature.addManagedBuilder(project, new NullProgressMonitor());
		} catch (CoreException e) {
			ProjectWizardsUtils.throwCoreException("Test failed on adding managed build nature or builder: " + e.getLocalizedMessage());
		}

		// Associate the project with the managed builder so the clients can get proper information
		ICDescriptor desc = null;
		try {
			desc = CCorePlugin.getDefault().getCProjectDescription(project, true);
			desc.remove(CCorePlugin.BUILD_SCANNER_INFO_UNIQ_ID);
			desc.create(CCorePlugin.BUILD_SCANNER_INFO_UNIQ_ID, ManagedBuildManager.INTERFACE_IDENTITY);
		} catch (CoreException e) {
			ProjectWizardsUtils.throwCoreException("Test failed on adding managed builder as scanner info provider: " + e.getLocalizedMessage());
			return;
		}
		try {
			desc.saveProjectData();
		} catch (CoreException e) {
			ProjectWizardsUtils.throwCoreException("Test failed on saving the ICDescriptor data: " + e.getLocalizedMessage());		
		}	
	}
	
	private InputStream getMainCTemplate(List<String> functionsList, String projectName, String template) throws CoreException {
		String tmp = projectUtils.openFileStream(template);
		
		tmp = tmp.replaceAll("<PROJECT_NAME>", projectName);
		
		String functionsString ="";
		for (String fun : functionsList){
			if (!fun.isEmpty()){
				functionsString += "int "+fun+"(int argc, char *argv[])\n{\n\t"+tips+"\treturn 0;\n"+"}\n";
		
			}
		}
		tmp = tmp.replaceAll("<FUNCTIONS>", functionsString);
		
		functionsString ="";
		for (String fun : functionsList){
			if (!fun.isEmpty()){
				functionsString += "MSG_function_register(\""+fun+"\", "+fun+");\n\t";
			}
		}
		tmp = tmp.replaceAll("<FUNCTION_REGISTER>", functionsString);
		
		return new ByteArrayInputStream(tmp.getBytes());
	}
	
}

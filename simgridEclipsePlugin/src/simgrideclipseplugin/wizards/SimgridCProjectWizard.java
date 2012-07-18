package simgrideclipseplugin.wizards;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.ICDescriptor;
import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.settings.model.CExternalSetting;
import org.eclipse.cdt.core.settings.model.CIncludePathEntry;
import org.eclipse.cdt.core.settings.model.ICConfigurationDescription;
import org.eclipse.cdt.core.settings.model.ICIncludePathEntry;
import org.eclipse.cdt.core.settings.model.ICProjectDescription;
import org.eclipse.cdt.core.settings.model.ICProjectDescriptionManager;
import org.eclipse.cdt.core.settings.model.ICSettingEntry;
import org.eclipse.cdt.core.settings.model.extension.CExternalSettingProvider;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.IManagedBuildInfo;
import org.eclipse.cdt.managedbuilder.core.IManagedProject;
import org.eclipse.cdt.managedbuilder.core.IProjectType;
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
import org.eclipse.core.runtime.jobs.Job;
import org.junit.Assert;

@SuppressWarnings("deprecation")
public class SimgridCProjectWizard extends SimgridAbstractProjectWizard {

	public static final String TOOL_CHAIN = "simgrideclipse.project.toolchain";
	
	private static final String tips = "//TODO add your code here\n";
	private static final String mainCTemplatePath = "c_main_template.c";
	private static final String oldMainCTemplatePath = "old_c_main_template.c";

	public static final String LIB_PATH = "simgrid.project.C_library_path";

	public static final String IS_38_OR_MORE = "simgrid.project.is38orMore";
	
	private Map<String,Object> args;
	
	
	public SimgridCProjectWizard() {
		super(new SimgridCProjectWizardPage());
	}
	
	@Override
	protected IProject initializeNewProject(IProject project,
			List<String> funcL,
			Map<String,Object> args,
			IProgressMonitor monitor)throws CoreException {
		this.args = args;
		String projectName = project.getName();
		String fileNameC = projectName+"_"+"main.c";
		project = createNewCProject((String) args.get(TOOL_CHAIN),monitor);
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
	
	private IProject createNewCProject(final String projectTypeId,IProgressMonitor monitor) throws CoreException {
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
					//workspaceDesc.setAutoBuilding(false);
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
					Assert.assertNotNull(projType);

					// Create the managed-project (.cdtbuild) for our project that builds an executable.
					IManagedProject newProject = null;
					try {
						newProject = ManagedBuildManager.createManagedProject(project, projType);
					} catch (Exception e) {
						Assert.fail("Failed to create managed project for: " + project.getName());
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
					
					ManagedBuildManager.getBuildInfo(project).setValid(true);
				}
			};
			try {
				workspace.run(runnable, root, IWorkspace.AVOID_UPDATE, monitor);
			} catch (CoreException e2) {
				Assert.fail(e2.getLocalizedMessage());
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
				Assert.fail("Initializing build information failed for: " + project.getName() + " because: " + initResult.getMessage());
			}

			//set additional Simgrid settings
			ICProjectDescriptionManager mngr = CoreModel.getDefault().getProjectDescriptionManager();
			ICProjectDescription desc = mngr.getProjectDescription(project);
			ICConfigurationDescription conf = desc.getConfigurations()[0];
			Assert.assertNotNull(conf);
			LinkedHashSet<String> externalSettingsProviders = new LinkedHashSet<String>(Arrays.asList(conf.getExternalSettingsProviderIds()));
			SimgridCExtenalSettings.lib_path = (String) args.get(LIB_PATH);
			externalSettingsProviders.add(SimgridCExtenalSettings.ID);
			conf.setExternalSettingsProviderIds(externalSettingsProviders.toArray(new String[0]));
			conf.updateExternalSettingsProviders(new String[] {SimgridCExtenalSettings.ID});
			return project;
		}
	
	private void addManagedBuildNature (IProject project) {
		// Create the buildinformation object for the project
		IManagedBuildInfo info = ManagedBuildManager.createBuildInfo(project);
		Assert.assertNotNull(info);

		// Add the managed build nature
		try {
			ManagedCProjectNature.addManagedNature(project, new NullProgressMonitor());
			ManagedCProjectNature.addManagedBuilder(project, new NullProgressMonitor());
		} catch (CoreException e) {
			Assert.fail("Test failed on adding managed build nature or builder: " + e.getLocalizedMessage());
		}

		// Associate the project with the managed builder so the clients can get proper information
		ICDescriptor desc = null;
		try {
			desc = CCorePlugin.getDefault().getCProjectDescription(project, true);
			desc.remove(CCorePlugin.BUILD_SCANNER_INFO_UNIQ_ID);
			desc.create(CCorePlugin.BUILD_SCANNER_INFO_UNIQ_ID, ManagedBuildManager.INTERFACE_IDENTITY);
		} catch (CoreException e) {
			Assert.fail("Test failed on adding managed builder as scanner info provider: " + e.getLocalizedMessage());
			return;
		}
		try {
			desc.saveProjectData();
		} catch (CoreException e) {
			Assert.fail("Test failed on saving the ICDescriptor data: " + e.getLocalizedMessage());		}
		// Associate the project with the managed builder so the clients can get proper information
//		ICConfigurationDescription  conf = null;
//		ICProjectDescription desc = null;
//		try {
//			desc = CoreModel.getDefault().getProjectDescription(project, true);
//			conf = desc.getConfiguration();
//			conf.remove(CCorePlugin.BUILD_SCANNER_INFO_UNIQ_ID);
//			conf.create(CCorePlugin.BUILD_SCANNER_INFO_UNIQ_ID, ManagedBuildManager.INTERFACE_IDENTITY);
//		} catch (CoreException e) {
//			Assert.fail("Test failed on adding managed builder as scanner info provider: " + e.getLocalizedMessage());
//			return;
//		}
//		try {
//			CoreModel.getDefault().setProjectDescription(project, desc);
//		} catch (CoreException e) {
//			Assert.fail("Test failed on saving the ICDescriptor data: " + e.getLocalizedMessage());		}
	
	}
	
	private InputStream getMainCTemplate(List<String> functionsList, String projectName, String template) {
		String tmp = projectUtils.openFileStream(template);
		
		tmp = tmp.replaceAll("<PROJECT_NAME>", projectName);
		
		String functionsString ="";
		for (String fun : functionsList){
			functionsString += "int "+fun+"(int argc, char *argv[])\n{\n\t"+tips+"\treturn 0;\n"+"}\n";
		}
		tmp = tmp.replaceAll("<FUNCTIONS>", functionsString);
		
		functionsString ="";
		for (String fun : functionsList){
			functionsString += "MSG_function_register(\""+fun+"\", "+fun+");\n\t";
		}
		tmp = tmp.replaceAll("<FUNCTION_REGISTER>", functionsString);
		
		return new ByteArrayInputStream(tmp.getBytes());
	}
	
}

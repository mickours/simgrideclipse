package simgrideclipseplugin.wizards;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
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

public class SimgridProjectWizard extends BasicNewProjectResourceWizard {
	
	private SimgridProjectWizardPage page2;

	private IProject newProject;
	private final static String description = "This wizard creates a new SimGrid MSG project";
	private final static String title = "Simgrid project creation";
	private final static String deployementRadical = "deployement";
	private final static String platformRadical = "platform";
	private final static String sufix = ".xml";

	
	
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
		//create the new file template
		final String depFileName = newProject.getName()+"_"+deployementRadical+sufix;
		final String platFileName = newProject.getName()+"_"+platformRadical+sufix;
		
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					createFile(getDeploymentTemplate(), depFileName, monitor);
					IFile toOpenFile = createFile(getPlatformTemplate(), platFileName, monitor);
					openFile(toOpenFile, monitor);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}

			private InputStream getDeploymentTemplate() {
				return openFileStream("deployment_template.xml");
			}

			private InputStream getPlatformTemplate() {
				return openFileStream("platform_template.xml");
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
	
	private InputStream openFileStream(String path){
		byte[] buffer = null;
		try {
			URI uri = FileLocator.toFileURL(Platform.getBundle(Activator.PLUGIN_ID).getEntry("/templates/"+path)).toURI();
			File file = new File(uri);
			FileInputStream in = new FileInputStream(file);
			buffer = new byte[1024];
			in.read(buffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ByteArrayInputStream(buffer);
	}
	
	 private void openFile(final IFile fileToOpen, IProgressMonitor monitor ){
			monitor.setTaskName("Opening file for editing...");
			getShell().getDisplay().asyncExec(new Runnable() {
				public void run() {
					IWorkbenchPage page =
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					try {
						IDE.openEditor(page, fileToOpen, true);
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
				IProgressMonitor monitor)
						throws CoreException {
			// create a sample file
			monitor.beginTask("Creating " + fileName, 2);
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IResource resource = root.findMember(newProject.getFullPath());
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

		private void throwCoreException(String message) throws CoreException {
			IStatus status =
				new Status(IStatus.ERROR, "simgridEclipsePlugin", IStatus.OK, message, null);
			throw new CoreException(status);
		}

	
}

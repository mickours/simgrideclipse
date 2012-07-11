package simgrideclipseplugin.wizards;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.ide.IDE;

public class SimgridProjectWizard extends Wizard implements INewWizard{

	private final String description = "This wizard creates a new SimGrid MSG project";
	private final String title = "Simgrid project creation";
	private WizardNewProjectCreationPage page1;
	private SimgridProjectWizardPage page2;
	
	public SimgridProjectWizard() {
		super();
		setNeedsProgressMonitor(true);
	}
	
	public void addPages() {
		page1 = new WizardNewProjectCreationPage(title);
		page1.setDescription(description);
		addPage(page1);
		page2 = new SimgridProjectWizardPage(title);
		page2.setDescription(description);
		addPage(page2);
	}

	@Override
	public boolean performFinish() {
//		final String containerName = page1.getContainerName();
//		final String fileName = page.getFileName();
//		IRunnableWithProgress op = new IRunnableWithProgress() {
//			public void run(IProgressMonitor monitor) throws InvocationTargetException {
//				try {
//					doFinish(containerName, fileName, monitor);
//				} catch (CoreException e) {
//					throw new InvocationTargetException(e);
//				} finally {
//					monitor.done();
//				}
//			}
//		};
//		try {
//			getContainer().run(true, false, op);
//		} catch (InterruptedException e) {
//			return false;
//		} catch (InvocationTargetException e) {
//			Throwable realException = e.getTargetException();
//			MessageDialog.openError(getShell(), "Error", realException.getMessage());
//			return false;
//		}
		return true;
	}
	
//	private void doFinish(
//			String containerName,
//			String fileName,
//			IProgressMonitor monitor)
//			throws CoreException {
//			// create a sample file
//			monitor.beginTask("Creating " + fileName, 2);
//			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
//			IResource resource = root.findMember(new Path(containerName));
//			if (!resource.exists() || !(resource instanceof IContainer)) {
//				throwCoreException("Container \"" + containerName + "\" does not exist.");
//			}
//			
//			monitor.worked(1);
//			monitor.setTaskName("Opening file for editing...");
//			getShell().getDisplay().asyncExec(new Runnable() {
//				public void run() {
//					IWorkbenchPage page =
//						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
//					try {
//						IDE.openEditor(page, file, true);
//					} catch (PartInitException e) {
//					}
//				}
//			});
//			monitor.worked(1);
//		}
//
//	private void throwCoreException(String message) throws CoreException {
//		IStatus status =
//			new Status(IStatus.ERROR, "simgridEclipsePlugin", IStatus.OK, message, null);
//		throw new CoreException(status);
//	}
//	
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub
		
	}
}

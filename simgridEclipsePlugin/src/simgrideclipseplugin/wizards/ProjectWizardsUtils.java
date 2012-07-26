package simgrideclipseplugin.wizards;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import simgrideclipseplugin.Activator;

public class ProjectWizardsUtils {
	
	private Shell shell;
	private IProject newProject;
	
	
	public ProjectWizardsUtils(Shell shell, IProject newProject) {
		this.shell = shell;
		this.newProject = newProject;
	}

	public Shell getShell() {
		return shell;
	}

	public IProject getNewProject() {
		return newProject;
	}

	public String openFileStream(String path) throws CoreException{
		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader = null;
		try {
			URI uri = FileLocator.toFileURL(
					Platform.getBundle(Activator.PLUGIN_ID).getEntry(
							"/templates/"+ path)).toURI();
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
			e.printStackTrace();
			throwCoreException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throwCoreException(e.getMessage());
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				// do nothing
				throwCoreException(e.getMessage());
			}
		}
		return fileData.toString();
	}

	 public void openFile(final IFile fileToOpen, IProgressMonitor monitor ){
			monitor.setTaskName("Opening file for editing...");
			getShell().getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					IWorkbenchPage page =
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					try {
						IDE.openEditor(page, fileToOpen, "simgrideclipseplugin.editors.MultiPageSimgridEditor", true);
					} catch (PartInitException e) {
						e.printStackTrace();
						MessageDialog.openError(getShell(), "Error", e.getMessage());
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

		public IFile createFile(InputStream initContentStream,
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
				throwCoreException(e.getMessage());			}
			monitor.worked(1);
			return file;
		}
		
		public IFile createFile(InputStream initContentStream,
				String fileName,
				IProgressMonitor monitor) throws CoreException{
			return createFile(initContentStream,fileName,monitor,getNewProject().getFullPath().toString());
			
		}
		
		public String createFolder(IProgressMonitor monitor, String folderName)
						throws CoreException {
			// create a sample file
			monitor.beginTask("Creating folder", 2);
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IResource resource = root.findMember(getNewProject().getFullPath());
			if (!resource.exists() || !(resource instanceof IContainer)) {
				throwCoreException("Container does not exist.");
			}
			IContainer container = (IContainer) resource;
			IFolder folder = container.getFolder(new Path(folderName));
			folder.create(true, true, monitor);
			monitor.worked(1);
			return folder.getFullPath().toString();
		}
		

		public static void throwCoreException(String message) throws CoreException {
			IStatus status =
				new Status(IStatus.ERROR, "simgridEclipsePlugin", IStatus.OK, message, null);
			throw new CoreException(status);
		}
}

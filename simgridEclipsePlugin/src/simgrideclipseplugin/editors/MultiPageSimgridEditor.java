package simgrideclipseplugin.editors;

import org.eclipse.core.commands.util.Tracing;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorActionBarContributor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.misc.Policy;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.part.MultiPageEditorSite;
import org.eclipse.ui.part.MultiPageSelectionProvider;
import org.eclipse.ui.views.contentoutline.ContentOutline;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.xml.core.internal.provisional.contenttype.ContentTypeIdForXML;

import simgrideclipseplugin.editors.outline.SimgridOutlinePage;

/**
 * An example showing how to create a multi-page editor. This example has 3
 * pages:
 * <ul>
 * <li>page 0 contains a nested text editor.
 * <li>page 1 allows you to change the font used in page 2
 * <li>page 2 shows the words in page 0 in sorted order
 * </ul>
 */
@SuppressWarnings("restriction")
public class MultiPageSimgridEditor extends MultiPageEditorPart implements
		IResourceChangeListener {

	/** The text editor used in page 0. */
	public StructuredTextEditor editor;

	/** The graphic editor used in page 1. */
	private SimgridGraphicEditor graphEditor;

	/** holder for graphicEditor index */
	private int graphicEditorIndex = -1;

	/** holder for graphicEditor index */
	private int textEditorIndex = -1;

	/** the specific outline for this editor */
//	private SimgridOutlinePage myOutlinePage;
	
	/** part Listener to handle part activation changes **/
//	protected IPartListener partListener = new IPartListener() {
//		public void partActivated(IWorkbenchPart p) {
////				handleActivate();
//		}
//
//		public void partBroughtToTop(IWorkbenchPart p) {
//			// Ignore.
//		}
//
//		public void partClosed(IWorkbenchPart p) {
//			// Ignore.
//		}
//
//		public void partDeactivated(IWorkbenchPart p) {
//			// Ignore.
//		}
//
//		public void partOpened(IWorkbenchPart p) {
//			// Ignore.
//		}
//	};
	

	/**
	 * Creates a multi-page editor example.
	 */
	public MultiPageSimgridEditor() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}

	/**
	 * The <code>MultiPageEditorExample</code> implementation of this method
	 * checks that the input is an instance of <code>IFileEditorInput</code>.
	 */
	public void init(IEditorSite site, IEditorInput editorInput)
			throws PartInitException {
		if (!(editorInput instanceof IFileEditorInput)){
			throw new PartInitException(
					"Invalid Input: Must be IFileEditorInput");
		}
		super.init(site, editorInput);
//		site.getPage().addPartListener(partListener);
	}

	/**
	 * @see org.eclipse.ui.part.MultiPageEditorPart#createSite(org.eclipse.ui.IEditorPart)
	 */
	protected IEditorSite createSite(IEditorPart page) {
		IEditorSite site = null;
		if (page == editor) {
			site = new MultiPageEditorSite(this, page) {
				public String getId() {
					// Sets this ID so nested editor is configured for XML
					// source
					return ContentTypeIdForXML.ContentTypeID_XML + ".source"; //$NON-NLS-1$;
				}
			};
		} else {
			site = super.createSite(page);
		}
		return site;
	}

	/**
	 * Creates page of the multi-page editor, which contains a text editor.
	 */
	void createStructuredTextEditorPage() {
		try {
			editor = new StructuredTextEditor();
			editor.setEditorPart(this);
			textEditorIndex = addPage(editor, getEditorInput());
			setPageText(textEditorIndex, "Text Editor");
		} catch (PartInitException e) {
			ErrorDialog.openError(getSite().getShell(),
					"Error creating nested text editor", null, e.getStatus());
		}
		// Extra : changing main title (should not be here ...
		this.setPartName(editor.getTitle());
	}

	/**
	 * Creates page 1 of the multi-page editor, which should contain the graphic
	 * view.
	 */
	void createGraphicEditorPage() {
		try {
			graphEditor = new SimgridGraphicEditor(this);
			graphicEditorIndex = addPage(graphEditor, getEditorInput());
			setPageText(graphicEditorIndex, "Visual editor");
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates and connect the pages of the multi-page editor.
	 */
	protected void createPages() {
		createStructuredTextEditorPage();
		createGraphicEditorPage();
	}
	
	

//	public void adaptiveSetPageText(int index, String name) {
//		setPageText(index, name);
//	}
//	
//	protected void handleActivate() {
//		// Refresh any actions that may become enabled or disabled.
//		//getSite().getSelectionProvider().setSelection(); 
//	}

	/**
	 * The <code>MultiPageEditorPart</code> implementation of this
	 * <code>IWorkbenchPart</code> method disposes all nested editors.
	 * Subclasses may extend.
	 */
	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		super.dispose();
	}

	/**
	 * Saves the multi-page editor's document.
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		editor.doSave(monitor);
	}

	/**
	 * Saves the multi-page editor's document as another file. Also updates the
	 * text for page 0's tab, and updates this multi-page editor's input to
	 * correspond to the nested editor's.
	 */
	@Override
	public void doSaveAs() {
		editor.doSaveAs();
		setPageText(0, editor.getTitle());
		setInput(editor.getEditorInput());
	}

	/*
	 * (non-Javadoc) Method declared on IEditorPart
	 */
	public void gotoMarker(IMarker marker) {
		setActivePage(0);
		IDE.gotoMarker(getEditor(0), marker);
	}

	/*
	 * (non-Javadoc) Method declared on IEditorPart.
	 */
	public boolean isSaveAsAllowed() {
		return editor.isSaveAsAllowed();
	}

	/**
	 * Closes all project files on project close.
	 */
	public void resourceChanged(final IResourceChangeEvent event) {
		if (event.getType() == IResourceChangeEvent.PRE_CLOSE) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					IWorkbenchPage[] pages = getSite().getWorkbenchWindow()
							.getPages();
					for (int i = 0; i < pages.length; i++) {
						if (((FileEditorInput) editor.getEditorInput())
								.getFile().getProject()
								.equals(event.getResource())) {
							IEditorPart editorPart = pages[i].findEditor(editor
									.getEditorInput());
							pages[i].closeEditor(editorPart, true);
						}
					}
				}
			});
		}
	}
	

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class required) {
		// if (IContentOutlinePage.class.equals(required)) {
		// if (myOutlinePage == null) {
		// myOutlinePage = new SimgridOutlinePage(graphEditor);
		// myOutlinePage.setInput(getEditorInput());
		// }
		// return myOutlinePage;
		// }
		if (required == ZoomManager.class)
			return graphEditor.getAdapter(ZoomManager.class);
		return super.getAdapter(required);
	}

	public IEditorPart getActivePageEditor() {
		return super.getActiveEditor();
	}
}

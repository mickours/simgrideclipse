package simgrideclipseplugin.editors;



import javax.swing.JComponent;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.*;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.part.MultiPageEditorSite;
import org.eclipse.ui.ide.IDE;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.xml.core.internal.provisional.contenttype.ContentTypeIdForXML;

import simgrideclipseplugin.utils.embeddedSwing.EmbeddedSwingComposite;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.swingViewer.View;
import org.graphstream.ui.swingViewer.Viewer;
/**
 * An example showing how to create a multi-page editor.
 * This example has 3 pages:
 * <ul>
 * <li>page 0 contains a nested text editor.
 * <li>page 1 allows you to change the font used in page 2
 * <li>page 2 shows the words in page 0 in sorted order
 * </ul>
 */
public class MultiPageXMLEditor extends MultiPageEditorPart implements IResourceChangeListener{

	/** The text editor used in page 0. */
	private StructuredTextEditor editor;

	/** The graphic editor used in page 1. */
	private GraphicEditorPage  graphEditor;
	
	/** holder for graphicEditor index */
	private int graphicEditorIndex = -1; 
	
	/** holder for graphicEditor index */
	private int indexSSE = -1; 
	
	/**
	 * Creates a multi-page editor example.
	 */
	public MultiPageXMLEditor() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}
	

/**
 * @see org.eclipse.ui.part.MultiPageEditorPart#createSite(org.eclipse.ui.IEditorPart)
 */
protected IEditorSite createSite(IEditorPart page) {
	IEditorSite site = null;
	if (page == editor) {
		site = new MultiPageEditorSite(this, page) {
			public String getId() {
				// Sets this ID so nested editor is configured for XML source
				return ContentTypeIdForXML.ContentTypeID_XML + ".source"; //$NON-NLS-1$;
			}
		};
	}
	else {
		site = super.createSite(page);
	}
	return site;
}
	/**
	 * Creates page 0 of the multi-page editor,
	 * which contains a text editor.
	 */
	void createStructuredTextEditorPage() {
		try {
			editor = new StructuredTextEditor();
			indexSSE = addPage(editor, getEditorInput());
			setPageText(indexSSE, "Text Editor");
		} catch (PartInitException e) {
			ErrorDialog.openError(
				getSite().getShell(),
				"Error creating nested text editor",
				null,
				e.getStatus());
		}
		// Extra : changing main title (should not be here ...
		this.setPartName(editor.getTitle());
	}
	
	
	
	/**
	 * Creates page 1 of the multi-page editor,
	 * which should contain the graphstream view.
	 */
	void createGraphstreamPage() {
		// composite, graphstream on the left, buttons on the right
		Composite composite = new Composite(getContainer(), SWT.NONE);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);
		layout.numColumns = 5;
		
		// setting correct layout data
				GridData gridData = new GridData();
				gridData.verticalAlignment = GridData.FILL;
				gridData.verticalSpan = 4;
				gridData.grabExcessVerticalSpace = true;
				gridData.horizontalAlignment = GridData.FILL;
				gridData.grabExcessHorizontalSpace = true;
				
		// graphstream Swing graph Editor		
		graphEditor = new GraphicEditorPage(composite, SWT.PUSH); 		       
		graphEditor.populate();
		
		
		// Setting layout 
		graphEditor.setLayoutData(gridData);
		
		// Adding the list of possible stuff to add while editing		
		addPalette(composite);
				
		graphicEditorIndex = addPage(composite);	
		setPageText(graphicEditorIndex, "Visual editor");
	}
	private void addPalette(Composite composite) {
		 // dummy placeholder button		
		new Button(composite, SWT.PUSH).setText("Dummy button");		
		
	}


	/**
	 * Creates and connect the pages of the multi-page editor.
	 */
	protected void createPages() {
		createStructuredTextEditorPage();		
		createGraphstreamPage();
		//connects the graphStream page to the document, the selection provider
		// and so on.
		connectPages();
	}
	
	private void connectPages() {
		// Setting doc for graph editor
		if (graphEditor!=null)
		{
		//	this.getEditorInput()
			//graphEditor.seD
		}
	}


	public void adaptiveSetPageText(int index, String name)
	{
		setPageText(index, name);
	}
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
	public void doSave(IProgressMonitor monitor) {
		getEditor(0).doSave(monitor);
	}
	/**
	 * Saves the multi-page editor's document as another file.
	 * Also updates the text for page 0's tab, and updates this multi-page editor's input
	 * to correspond to the nested editor's.
	 */
	public void doSaveAs() {
		IEditorPart editor = getEditor(0);
		editor.doSaveAs();
		setPageText(0, editor.getTitle());
		setInput(editor.getEditorInput());
	}
	/* (non-Javadoc)
	 * Method declared on IEditorPart
	 */
	public void gotoMarker(IMarker marker) {
		setActivePage(0);
		IDE.gotoMarker(getEditor(0), marker);
	}
	/**
	 * The <code>MultiPageEditorExample</code> implementation of this method
	 * checks that the input is an instance of <code>IFileEditorInput</code>.
	 */
	public void init(IEditorSite site, IEditorInput editorInput)
		throws PartInitException {
		if (!(editorInput instanceof IFileEditorInput))
			throw new PartInitException("Invalid Input: Must be IFileEditorInput");
		super.init(site, editorInput);
	}
	/* (non-Javadoc)
	 * Method declared on IEditorPart.
	 */
	public boolean isSaveAsAllowed() {
		return true;
	}
	
	/**
	 * Closes all project files on project close.
	 */
	public void resourceChanged(final IResourceChangeEvent event){
		if(event.getType() == IResourceChangeEvent.PRE_CLOSE){
			Display.getDefault().asyncExec(new Runnable(){
				public void run(){
					IWorkbenchPage[] pages = getSite().getWorkbenchWindow().getPages();
					for (int i = 0; i<pages.length; i++){
						if(((FileEditorInput)editor.getEditorInput()).getFile().getProject().equals(event.getResource())){
							IEditorPart editorPart = pages[i].findEditor(editor.getEditorInput());
							pages[i].closeEditor(editorPart,true);
						}
					}
				}            
			});
		}
	}
	
}

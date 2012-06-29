package simgrideclipseplugin.editors.outline;


import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.w3c.dom.Element;

import simgrideclipseplugin.editors.SimgridGraphicEditor;
import simgrideclipseplugin.graphical.ElementLabelProvider;
import simgrideclipseplugin.model.ElementList;
import simgrideclipseplugin.model.ModelHelper;
import simgrideclipseplugin.model.SimgridModelListener;

@SuppressWarnings("restriction")
public class SimgridOutlinePage extends ContentOutlinePage //implements IPropertyListener
{

	private IEditorInput input;
	private OutlineContentProvider outlineContentProvider;
	private LabelProvider outlineLabelProvider;
	private StructuredTextEditor editor;
	private SimgridGraphicEditor graphEditor;

	public SimgridOutlinePage(StructuredTextEditor editor, SimgridGraphicEditor graphEditor)
	{
		super();
		this.editor = editor;
		outlineContentProvider = new OutlineContentProvider(editor.getDocumentProvider());
		//handle model changes
		ModelHelper.addModelListener(editor.getEditorInput(),
				new SimgridModelListener(){
					String oldModel;
					@Override
					public void modelChanged(IStructuredModel model) {
						if (!model.getStructuredDocument().get().equals(oldModel)){
							oldModel = model.getStructuredDocument().get();
							updateInput();
						}
					}
		});
		updateInput();
	}

	public void createControl(Composite parent)
	{
		super.createControl(parent);
		TreeViewer viewer = getTreeViewer();
		viewer.setContentProvider(outlineContentProvider);
		outlineLabelProvider = new ElementLabelProvider();
		viewer.setLabelProvider(outlineLabelProvider);
		if (input != null){
			viewer.setInput(input);
		}
//		//add double click action
//		getTreeViewer().addDoubleClickListener(new IDoubleClickListener() {
//			@Override
//			public void doubleClick(DoubleClickEvent event) {
//				SimgridGraphicEditor ge =SimgridOutlinePage.this.graphEditor;
//				IStructuredSelection sel = (IStructuredSelection) event.getSelection();
//				Element toOpen = (Element) sel.getFirstElement();
//				if (toOpen.getTagName().equals(ElementList.AS)){
//					ge.getGraphicalViewer().setContents(sel);
//				}
//			}
//		});
	}

	/**
	 * Sets the input of the outline page
	 */
	public void updateInput()
	{
		this.input = (IEditorInput)editor.getEditorInput();
		update();
	}

	/**
	 * The editor is saved, so we should refresh representation
	 * 
	 * @param tableNamePositions
	 */
	public void update()
	{
		//set the input so that the outlines parse can be called
		//update the tree viewer state
		TreeViewer viewer = getTreeViewer();

		if (viewer != null)
		{
			Control control = viewer.getControl();
			if (control != null && !control.isDisposed())
			{
				control.setRedraw(false);
				viewer.setInput(input);
				viewer.expandAll();
				control.setRedraw(true);
			}
		}
	}
	
	

	@Override
	public void dispose() {
		super.dispose();
	}
}
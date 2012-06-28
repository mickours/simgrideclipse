package simgrideclipseplugin.editors.outline;


import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;
import org.eclipse.wst.sse.core.internal.provisional.IModelStateListener;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;

import simgrideclipseplugin.graphical.ElementLabelProvider;
import simgrideclipseplugin.model.ModelHelper;
import simgrideclipseplugin.model.SimgridModelListener;

@SuppressWarnings("restriction")
public class SimgridOutlinePage extends ContentOutlinePage //implements IPropertyListener
{

	private IEditorInput input;
	private OutlineContentProvider outlineContentProvider;
	private LabelProvider outlineLabelProvider;
	private StructuredTextEditor editor;
//	private ISelectionChangedListener listener = new ISelectionChangedListener() {
//		@Override
//		public void selectionChanged(SelectionChangedEvent event) {
//			ISelection selection = event.getSelection();
//			if (!selection.isEmpty() && selection instanceof IStructuredSelection) {
//				setSelection(editor.partToModelSelection((IStructuredSelection) selection));
//			}
//		}
//	};

	public SimgridOutlinePage(StructuredTextEditor editor)
	{
		super();
		this.editor = editor;
		outlineContentProvider = new OutlineContentProvider(editor.getDocumentProvider());
		ModelHelper.addModelListener(editor.getEditorInput(),
				new SimgridModelListener(){
					@Override
					public void modelChanged(IStructuredModel arg0) {
						updateInput();
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
		//viewer.addSelectionChangedListener(this);
//		editor.getSite().getSelectionProvider().addSelectionChangedListener(listener);

		//control is created after input is set
		if (input != null)
			viewer.setInput(input);
	}

	/**
	 * Sets the input of the outline page
	 */
	public void updateInput()
	{
		this.input = (IEditorInput)editor.getEditorInput();
		update();
	}

	/*
	 * Change in selection
	 */

//	public void selectionChanged(SelectionChangedEvent event)
//	{
//		super.selectionChanged(event);
//		//find out which item in tree viewer we have selected, and set highlight range accordingly
//
//		ISelection selection = event.getSelection();
//		if (!selection.isEmpty() && selection instanceof IStructuredSelection) {
//			List<?> selectedElements = ((IStructuredSelection) selection)
//					.toList();
//			editor.selectionChanged(getSite().getPage().getActivePart(),
//					new StructuredSelection(selectedElements));
//		}
//	}

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

//	@Override
//	public void propertyChanged(Object source, int propId) {
//		//TODO: can be optimized
//		updateInput();
//	}

}
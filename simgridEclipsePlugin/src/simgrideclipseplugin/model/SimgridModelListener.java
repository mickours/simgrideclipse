package simgrideclipseplugin.model;

import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.wst.sse.core.internal.provisional.IModelStateListener;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;

import simgrideclipseplugin.graphical.SimgridGraphicEditor;

/**
 * Provide default implementation of the IModelStateListener interface 
 * @author mmercier
 * 
 */
@SuppressWarnings("restriction")
//TODO Remove this class if it's not used after adding commands
public class SimgridModelListener implements IModelStateListener {

	SimgridGraphicEditor editor;

	public SimgridModelListener(SimgridGraphicEditor editor) {
		this.editor = editor;
	}

	@Override
	public void modelAboutToBeChanged(IStructuredModel arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void modelAboutToBeReinitialized(IStructuredModel arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void modelChanged(IStructuredModel arg0) {
		// UPdate UI From DOM Model which have changed
		//editor.updateUIFromDOMModel();

	}

	@Override
	public void modelDirtyStateChanged(IStructuredModel model, boolean isDirty) {
		 // dirty from DOM Model has changed (the XML content was changed 
	      // with anothher editor), fire the dirty property change to 
	      // indicate to the editor that dirty has changed.
		//editor.setDirty();
		
	}

	@Override
	public void modelReinitialized(IStructuredModel arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void modelResourceDeleted(IStructuredModel arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void modelResourceMoved(IStructuredModel arg0, IStructuredModel arg1) {
		// TODO Auto-generated method stub

	}

}

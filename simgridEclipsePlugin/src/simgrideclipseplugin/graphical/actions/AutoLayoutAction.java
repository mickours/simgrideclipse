package simgrideclipseplugin.graphical.actions;

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.actions.*;
import org.eclipse.ui.IEditorPart;

import simgrideclipseplugin.editors.SimgridGraphicEditor;
import simgrideclipseplugin.graphical.AutomaticGraphLayoutHelper;
import simgrideclipseplugin.graphical.SimgridIconProvider;

public class AutoLayoutAction extends EditorPartAction{
	
	public static final String ID = "simgrideclipseplugin.AutoLayout";
	
	public AutoLayoutAction(IEditorPart editor) {
		super(editor);
	}

	@Override
	protected void init() {
		setId(ID);
		setText("Auto Layout");
		setToolTipText("Perform Auto Layout");
		setImageDescriptor(SimgridIconProvider.getIconImageDescriptor(ID));
	}

	public void run() {
		GraphicalViewer viewer = ((SimgridGraphicEditor)getEditorPart()).getGraphicalViewer();
		AutomaticGraphLayoutHelper.INSTANCE.init(viewer.getContents());
		AutomaticGraphLayoutHelper.INSTANCE.computeLayout();
	}
	
	
	@Override
	protected boolean calculateEnabled() {
		//FIXME activate only if necessary
		return true;
	}
}

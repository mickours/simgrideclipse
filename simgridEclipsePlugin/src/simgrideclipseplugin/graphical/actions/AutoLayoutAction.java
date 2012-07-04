package simgrideclipseplugin.graphical.actions;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.actions.EditorPartAction;
import org.eclipse.ui.IEditorPart;

import simgrideclipseplugin.editors.SimgridGraphicEditor;
import simgrideclipseplugin.graphical.AutomaticGraphLayoutHelper;
import simgrideclipseplugin.graphical.providers.SimgridIconProvider;

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
		EditPart content = ((SimgridGraphicEditor)getEditorPart()).getGraphicalContents();
		AutomaticGraphLayoutHelper.INSTANCE.init(content);
		AutomaticGraphLayoutHelper.INSTANCE.computeLayout();
	}
	
	
	@Override
	protected boolean calculateEnabled() {
		//FIXME activate only if necessary
		return true;
	}
}

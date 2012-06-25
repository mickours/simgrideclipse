package simgrideclipseplugin.graphical.actions;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.ui.actions.*;
import org.eclipse.ui.IEditorPart;

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
		AutomaticGraphLayoutHelper.INSTANCE.computeLayout();
	}
	
	

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return super.isEnabled();
	}

	@Override
	protected boolean calculateEnabled() {
		//FIXME activate only if necessary
		return true;
	}
}

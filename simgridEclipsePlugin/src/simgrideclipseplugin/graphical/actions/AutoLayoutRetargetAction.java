package simgrideclipseplugin.graphical.actions;

import org.eclipse.ui.actions.RetargetAction;

import simgrideclipseplugin.graphical.SimgridIconProvider;

public class AutoLayoutRetargetAction extends RetargetAction {
	
	public AutoLayoutRetargetAction(){
		super(null,null);
		setId(AutoLayoutAction.ID);
		setText("Auto Layout");
		setToolTipText("Perform Auto Layout");
		setImageDescriptor(SimgridIconProvider.getIconImageDescriptor(AutoLayoutAction.ID));
	}
}

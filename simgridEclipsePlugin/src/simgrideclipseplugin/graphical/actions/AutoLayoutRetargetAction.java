package simgrideclipseplugin.graphical.actions;

import org.eclipse.ui.actions.RetargetAction;

import simgrideclipseplugin.graphical.providers.SimgridIconProvider;

public class AutoLayoutRetargetAction extends RetargetAction {
	
	public AutoLayoutRetargetAction(){
		super(AutoLayoutAction.ID,AutoLayoutAction.TEXT);
		setToolTipText(AutoLayoutAction.TOOL_TIP_TEXT);
		setImageDescriptor(SimgridIconProvider.getIconImageDescriptor(AutoLayoutAction.ID));
	}	
}

package simgrideclipseplugin.graphical.actions;

import org.eclipse.ui.actions.RetargetAction;

import simgrideclipseplugin.graphical.actions.GoOutAction;
import simgrideclipseplugin.graphical.providers.SimgridIconProvider;

public class GoOutRetargetAction extends RetargetAction {

	public GoOutRetargetAction(){
		super(GoOutAction.ID,GoOutAction.TEXT);
		setToolTipText(GoOutAction.TOOL_TIP);
		setImageDescriptor(SimgridIconProvider.getIconImageDescriptor(GoOutAction.ID));
	}
}
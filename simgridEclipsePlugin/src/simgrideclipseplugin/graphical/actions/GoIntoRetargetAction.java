package simgrideclipseplugin.graphical.actions;

import org.eclipse.ui.actions.RetargetAction;

import simgrideclipseplugin.graphical.SimgridIconProvider;
import simgrideclipseplugin.graphical.actions.GoIntoAction;

public class GoIntoRetargetAction extends RetargetAction {

	public GoIntoRetargetAction(){
		super(GoIntoAction.ID,GoIntoAction.TEXT);
		setToolTipText(GoIntoAction.TOOL_TIP);
		setImageDescriptor(SimgridIconProvider.getIconImageDescriptor(GoIntoAction.ID));
	}
}

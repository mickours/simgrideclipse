package simgrideclipseplugin.graphical.actions;

import org.eclipse.ui.actions.RetargetAction;

import simgrideclipseplugin.graphical.actions.GoIntoAction;
import simgrideclipseplugin.graphical.providers.SimgridIconProvider;

public class GoIntoRetargetAction extends RetargetAction {

	public GoIntoRetargetAction(){
		super(GoIntoAction.ID,GoIntoAction.MESSAGE);
		setToolTipText(GoIntoAction.TOOL_TIP);
		setImageDescriptor(SimgridIconProvider.getIconImageDescriptor(GoIntoAction.ID));
	}
}

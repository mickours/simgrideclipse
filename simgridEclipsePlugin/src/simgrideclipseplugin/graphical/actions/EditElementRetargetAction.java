package simgrideclipseplugin.graphical.actions;

import org.eclipse.ui.actions.RetargetAction;

import simgrideclipseplugin.graphical.providers.SimgridIconProvider;

public class EditElementRetargetAction extends RetargetAction {

	public EditElementRetargetAction(){
		super(EditElementAction.ID,EditElementAction.TEXT);
		setToolTipText(EditElementAction.TOOL_TIP);
		setImageDescriptor(SimgridIconProvider.getIconImageDescriptor(EditElementAction.ID));
	}
}

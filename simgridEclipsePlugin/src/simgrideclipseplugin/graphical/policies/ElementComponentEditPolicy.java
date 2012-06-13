package simgrideclipseplugin.graphical.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;
import org.w3c.dom.Element;

import simgrideclipseplugin.graphical.commands.DeleteElementCommand;

public class ElementComponentEditPolicy extends ComponentEditPolicy {
	@Override
	protected Command createDeleteCommand(GroupRequest deleteRequest) {
		DeleteElementCommand cmd = new DeleteElementCommand();
		cmd.setModel((Element) getHost().getModel());
		return cmd;
	}
	
}

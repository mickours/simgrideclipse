package simgrideclipseplugin.graphical.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.swt.widgets.Shell;
import org.w3c.dom.Element;

import simgrideclipseplugin.graphical.commands.DeleteElementCommand;

public class ElementComponentEditPolicy extends ComponentEditPolicy {
	@Override
	protected Command createDeleteCommand(GroupRequest deleteRequest) {
		DeleteElementCommand cmd = new DeleteElementCommand();
		Shell shell = getHost().getViewer().getControl().getShell();
		cmd.setShell(shell);
		cmd.setModel((Element) getHost().getModel());
		return cmd;
	}
}

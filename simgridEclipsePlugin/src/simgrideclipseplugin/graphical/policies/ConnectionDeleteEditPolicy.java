package simgrideclipseplugin.graphical.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ConnectionEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import simgrideclipseplugin.graphical.commands.DeleteConnectionCommand;

public class ConnectionDeleteEditPolicy extends ConnectionEditPolicy {

	@Override
	protected Command getDeleteCommand(GroupRequest request) {
		DeleteConnectionCommand command = new DeleteConnectionCommand();
        command.setRoute(getHost().getModel());
        return command;
	}

}

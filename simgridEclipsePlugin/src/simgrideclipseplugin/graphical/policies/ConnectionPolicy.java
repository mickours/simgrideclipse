package simgrideclipseplugin.graphical.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;
import org.w3c.dom.Element;

import simgrideclipseplugin.graphical.commands.ConnectionCreateCommand;
import simgrideclipseplugin.graphical.commands.ConnectionReconnectCommand;

public class ConnectionPolicy extends GraphicalNodeEditPolicy {

	@Override
	protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
		ConnectionCreateCommand cmd = (ConnectionCreateCommand)request.getStartCommand();
        Element targetNode = (Element)getHost().getModel();
        cmd.setTargetNode(targetNode);
        //TODO display a dialog that allows to add links and gateways
        return cmd;
	}

	@Override
	protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
		ConnectionCreateCommand cmd = new ConnectionCreateCommand();
        Element sourceNode = (Element)getHost().getModel();
        cmd.setConnectionType(request.getNewObjectType().toString());
        cmd.setSourceNode(sourceNode);
        request.setStartCommand(cmd);
        return cmd;
	}

	@Override
	protected Command getReconnectTargetCommand(ReconnectRequest request) {
		Element route = (Element)request.getConnectionEditPart().getModel();
        Element targetNode = (Element)getHost().getModel();
        ConnectionReconnectCommand cmd = new ConnectionReconnectCommand(route);
        cmd.setNewTargetNode(targetNode);       
        return cmd;
	}

	@Override
	protected Command getReconnectSourceCommand(ReconnectRequest request) {
		Element route = (Element)request.getConnectionEditPart().getModel();
        Element sourceNode = (Element)getHost().getModel();
        ConnectionReconnectCommand cmd = new ConnectionReconnectCommand(route);
        cmd.setNewSourceNode(sourceNode);       
        return cmd;
	}

}

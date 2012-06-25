package simgrideclipseplugin.graphical.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.internal.UIPlugin;
import org.w3c.dom.Element;

import simgrideclipseplugin.graphical.commands.ConnectionCreateCommand;
import simgrideclipseplugin.graphical.commands.ConnectionReconnectCommand;
import simgrideclipseplugin.model.ElementList;
import simgrideclipseplugin.model.ModelHelper;
import simgrideclipseplugin.model.SimgridRules;

public class ConnectionPolicy extends GraphicalNodeEditPolicy {

	@Override
	protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
		ConnectionCreateCommand cmd = (ConnectionCreateCommand)request.getStartCommand();
        Element targetNode = (Element)getHost().getModel();
        String type = cmd.getConnectionType();
        if (SimgridRules.isAllowedConnection(targetNode,type)){
	        cmd.setTargetNode(targetNode);
	        //TODO display a dialog that allows to add links and gateways
	        ElementListSelectionDialog dialog = new ElementListSelectionDialog(getHost().getViewer().getControl().getShell(), new LabelProvider());
	        
//		        dialog.setElements(new String[] { "Linux", "Mac", "Windows" });
//		        dialog.setTitle("Which operating system are you using");
//		        // User pressed cancel
//		        if (dialog.open() != Window.OK) {
//		        		return false;
//		        }
//		        Object[] result = dialog.getResult();
	    }
        else{
        	cmd = null;
        }
        return cmd;
	}

	@Override
	protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
		ConnectionCreateCommand cmd = null;
		Element sourceNode = (Element)getHost().getModel();
		String type = request.getNewObjectType().toString();
		if (SimgridRules.isAllowedConnection(sourceNode,type)){
			cmd = new ConnectionCreateCommand();
	        cmd.setConnectionType(type);
	        cmd.setSourceNode(sourceNode);
	        request.setStartCommand(cmd);
		}
        return cmd;
	}

	@Override
	protected Command getReconnectTargetCommand(ReconnectRequest request) {
		Element route = (Element)request.getConnectionEditPart().getModel();
        Element targetNode = (Element)getHost().getModel();
        ConnectionReconnectCommand cmd = null;
        if (SimgridRules.isAllowedConnection(targetNode,route.getTagName())){
	       cmd = new ConnectionReconnectCommand(route);
	       cmd.setNewTargetNode(targetNode);
        }
        return cmd;
	}

	@Override
	protected Command getReconnectSourceCommand(ReconnectRequest request) {
		Element route = (Element)request.getConnectionEditPart().getModel();
        Element sourceNode = (Element)getHost().getModel();
        ConnectionReconnectCommand cmd = null;
        if (SimgridRules.isAllowedConnection(sourceNode,route.getTagName())){
	        cmd = new ConnectionReconnectCommand(route);
	        cmd.setNewSourceNode(sourceNode);    
        }
        return cmd;
	}

}

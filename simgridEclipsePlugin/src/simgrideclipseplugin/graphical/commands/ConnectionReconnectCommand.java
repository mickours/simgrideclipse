package simgrideclipseplugin.graphical.commands;

import org.eclipse.gef.commands.Command;
import org.w3c.dom.Element;

import simgrideclipseplugin.model.ModelHelper;

public class ConnectionReconnectCommand extends Command {

	private Element route;
	private Element oldSourceNode;
	private Element oldTargetNode;
	private Element newSourceNode;
    private Element newTargetNode;

	public ConnectionReconnectCommand(Element route) {
		if (route == null) {
            throw new IllegalArgumentException();
        }
        this.route = route;
        this.oldSourceNode = ModelHelper.getSourceNode(route);
        this.oldTargetNode = ModelHelper.getTargetNode(route);
	}
   
    private boolean checkSourceReconnection() {
    	if (newSourceNode.equals(oldTargetNode))
            return false;
        else if (!newSourceNode.getClass().equals(oldTargetNode.getClass()))
            return false;
        return true;
    }
   
    private boolean checkTargetReconnection() {
        if (oldSourceNode.equals(newTargetNode))
            return false;
        else if (!oldSourceNode.getClass().equals(newTargetNode.getClass()))
            return false;
        return true;
    }

	public void setNewTargetNode(Element targetNode) {
		if (targetNode == null) {
            throw new IllegalArgumentException();
        }
        this.newSourceNode = null;
        this.newTargetNode = targetNode;
		
	}

	public void setNewSourceNode(Element sourceNode) {
		if (sourceNode == null) {
            throw new IllegalArgumentException();
        }
        this.newSourceNode = sourceNode;
        this.newTargetNode = null;
	}
	
	@Override
	public boolean canExecute() {
        if (newSourceNode != null) {
            return checkSourceReconnection();
        } else if (newTargetNode != null) {
            return checkTargetReconnection();
        }
        return false;
    }
	
	@Override
	public void execute() {
        if (newSourceNode != null) {
            ModelHelper.reconnect(route, newSourceNode, oldTargetNode);
        } else if (newTargetNode != null) {
        	ModelHelper.reconnect(route, oldSourceNode, newTargetNode);
        } else {
            throw new IllegalStateException("Should not happen");
        }
    }
   
	@Override
    public void undo() {
		ModelHelper.reconnect(route, oldSourceNode, oldTargetNode);
    }

}


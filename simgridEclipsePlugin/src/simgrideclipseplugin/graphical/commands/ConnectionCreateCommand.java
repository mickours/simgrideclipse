package simgrideclipseplugin.graphical.commands;

import org.eclipse.gef.commands.Command;
import org.w3c.dom.Element;

import simgrideclipseplugin.model.ModelHelper;

public class ConnectionCreateCommand extends Command {
	private Element sourceNode, targetNode;
    private String connectionType;
    private Element route = null;
   
    public void setSourceNode(Element sourceNode) {
        this.sourceNode = sourceNode;
    }
   
    public void setTargetNode(Element targetNode) {
        this.targetNode = targetNode;
    }
   
    @Override
    public boolean canExecute() {
        if (sourceNode == null || targetNode == null)
            return false;
        else if (sourceNode.equals(targetNode))
            return false;
        return true;
    }
   
    @Override
    public void execute() {
        route = ModelHelper.createRoute(sourceNode, targetNode, connectionType);
    }
   
    @Override
    public boolean canUndo() {
        if (sourceNode == null || targetNode == null || route == null)
            return false;
        return true;         
    }
   
    @Override
    public void undo() {
    	ModelHelper.removeElement(route);
    }

    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    public String getConnectionType() {
        return connectionType;
    }
}
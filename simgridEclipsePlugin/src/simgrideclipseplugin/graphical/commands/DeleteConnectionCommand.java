package simgrideclipseplugin.graphical.commands;

import org.eclipse.gef.commands.Command;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import simgrideclipseplugin.model.ModelHelper;

public class DeleteConnectionCommand extends Command {

	private Element route;
	private Node parent;
	   
    public void setRoute(Object route) {
        this.route = (Element)route;
        this.parent = this.route.getParentNode();
    }
   
    @Override
    public void execute() {
        ModelHelper.removeElement(route);
    }
   
    @Override
    public void undo() {
    	ModelHelper.addRoute(route, parent);
    }
}

package simgrideclipseplugin.graphical.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.commands.Command;
import org.w3c.dom.Element;

import simgrideclipseplugin.model.ModelHelper;

public class DeleteElementCommand extends Command {
	private Element model;
	private Element parent;
	private List<Element> connections;
	private Map<Element,List<Element>> deletedLinks;
	
	@Override
	public void execute() {
		connections = ModelHelper.getConnections(model);
		deletedLinks = new HashMap<Element,List<Element>>(connections.size());
		for (Element e : connections){
			deletedLinks.put(e,(ModelHelper.removeRoute(e)));
		}
		ModelHelper.removeElement(model);
	}
	
	@Override
	public void undo() {
		ModelHelper.addElementChild(parent, model);
		for (Element route :connections){
			ModelHelper.restoreRoute(parent, route, deletedLinks.get(route));
		}
		
	}

	public void setModel(Element model) {
		this.model = model;
		parent = (Element)model.getParentNode();
	}
}

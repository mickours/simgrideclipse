package simgrideclipseplugin.graphical.commands;

import org.eclipse.gef.commands.Command;
import org.w3c.dom.Element;

import simgrideclipseplugin.model.ModelHelper;

public class DeleteElementCommand extends Command {
	private Element model;
	private Element parent;
	
	@Override
	public void execute() {
		ModelHelper.removeElement(model);
		for (Element e : ModelHelper.getConnections(model)){
			ModelHelper.removeRoute(e);
		}
	}
	
	@Override
	public void undo() {
		ModelHelper.addElementChild(parent, model);
	}

	public void setModel(Element model) {
		this.model = model;
		parent = (Element)model.getParentNode();
	}
}

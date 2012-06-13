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
	}
	
	@Override
	public void undo() {
		ModelHelper.addChild(parent, model);
	}

	public void setModel(Element model) {
		this.model = model;
		parent = (Element)model.getParentNode();
	}
}

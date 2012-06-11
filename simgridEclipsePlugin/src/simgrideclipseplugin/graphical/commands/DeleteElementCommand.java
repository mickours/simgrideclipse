package simgrideclipseplugin.graphical.commands;

import org.eclipse.gef.commands.Command;
import org.w3c.dom.Element;

import simgrideclipseplugin.model.ModelHelper;

public class DeleteElementCommand extends Command {
	private Element model;
	
	@Override
	public void execute() {
		ModelHelper.removeElement(model);
	}

	public void setModel(Element model) {
		this.model = model;
	}
}

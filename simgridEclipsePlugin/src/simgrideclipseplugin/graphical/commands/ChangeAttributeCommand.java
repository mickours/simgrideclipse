package simgrideclipseplugin.graphical.commands;

import org.eclipse.gef.commands.Command;
import org.w3c.dom.Element;

public class ChangeAttributeCommand extends Command {

	private String oldValue, newValue, attributeName;
	private Element model;

	@Override
	public void execute() {
		oldValue = model.getAttribute(attributeName);
		model.setAttribute(attributeName, newValue);
	}

	@Override
	public void undo() {
		model.setAttribute(attributeName, oldValue);
	}
	
	@Override
	public void redo() {
		model.setAttribute(attributeName, newValue);
	}

	public void setNewValue(String newName) {
		this.newValue = newName;
	}
	public void setattributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	
	public void setModel(Element model) {
		this.model = model;
	}
}

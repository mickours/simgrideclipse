package simgrideclipseplugin.graphical.commands;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;

import simgrideclipseplugin.graphical.ElementPositionMap;
import simgrideclipseplugin.graphical.parts.ElementAbstractEditPart;

public class ChangeLayoutConstraintCommand extends Command {
	private Rectangle layout;
	private ElementAbstractEditPart part;

	@Override
	public void execute() {
		part.setLocation(layout.getLocation());
	}

	public void setLayout(Rectangle layout) {
		this.layout = layout;
	}

	public void setPart(ElementAbstractEditPart part) {
		this.part = part;
	}	
	
}

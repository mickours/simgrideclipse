package simgrideclipseplugin.graphical.commands;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import simgrideclipseplugin.graphical.parts.AbstractElementEditPart;

public class ChangeLayoutConstraintCommand extends Command {
	//private Rectangle oldLayout;
	private Point oldLocation;
	private Rectangle layout;
	private AbstractElementEditPart part;

	@Override
	public void execute() {
		if(oldLocation == null) {
			 oldLocation = part.getLocation(); 
		}
		part.setLocation(layout.getLocation());
	}
	
	@Override public void undo() {
	    part.setLocation(oldLocation);
	}
	
	@Override
	public boolean canUndo() {
		return part != null && part.isActive();
	}

	public void setLayout(Rectangle layout) {
		this.layout = layout;
	}

	public void setPart(AbstractElementEditPart part) {
		this.part = part;
	}	
	
}

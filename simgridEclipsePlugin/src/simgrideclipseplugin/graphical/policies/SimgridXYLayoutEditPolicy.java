package simgrideclipseplugin.graphical.policies;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.w3c.dom.Element;

import simgrideclipseplugin.graphical.commands.AddElementCommand;
import simgrideclipseplugin.graphical.commands.ChangeLayoutConstraintCommand;
import simgrideclipseplugin.graphical.parts.AbstractElementEditPart;
import simgrideclipseplugin.model.ElementList;

public class SimgridXYLayoutEditPolicy extends XYLayoutEditPolicy implements
		EditPolicy {

	@Override
	protected Command getCreateCommand(CreateRequest request) {
		Command cmd = null;
		String type = request.getNewObjectType().toString();
		Element parent = ((Element)getHost().getModel());
		Point position = ((Rectangle)getConstraintFor(request)).getLocation();
		cmd = new AddElementCommand(parent,type,position);
		return cmd;
	}



	@Override
	protected Command createChangeConstraintCommand(
			ChangeBoundsRequest request, EditPart child, Object constraint) 
	{
		ChangeLayoutConstraintCommand cmd = null;
		if (child instanceof AbstractElementEditPart)
		{
			cmd = new ChangeLayoutConstraintCommand();
			cmd.setLayout((Rectangle) constraint);
			cmd.setPart((AbstractElementEditPart) child);
		}
		return cmd;
	}

}

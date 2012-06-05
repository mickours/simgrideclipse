package simgrideclipseplugin.graphical.policies;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.w3c.dom.Element;

import simgrideclipseplugin.graphical.addElementCommand;
import simgrideclipseplugin.graphical.parts.SimgridAbstractEditPart;

public class SimgridXYLayoutEditPolicy extends XYLayoutEditPolicy implements
		EditPolicy {

	@Override
	protected Command getCreateCommand(CreateRequest request) {
		String type = ((Element)request.getNewObject()).getTagName();
		Element parent = ((Element)getHost().getModel());
		Command cmd = new addElementCommand(parent,type);
		((SimgridAbstractEditPart)getHost()).setPosition(request.getLocation());
		return cmd;
	}
	
	@Override 
	  protected EditPolicy createChildEditPolicy(EditPart child) { 
	    return null; 
	  } 
	 
	 
	  @Override 
	  protected Command getMoveChildrenCommand(Request request) { 
	    return null; 
	  } 

}

package simgrideclipseplugin.graphical.policies;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.w3c.dom.Element;

import simgrideclipseplugin.graphical.commands.addElementCommand;

public class SimgridXYLayoutEditPolicy extends XYLayoutEditPolicy implements
		EditPolicy {

	@Override
	protected Command getCreateCommand(CreateRequest request) {
		String type = request.getNewObject().toString();
		Element parent = ((Element)getHost().getModel());
		Point position = request.getLocation();
		Command cmd = new addElementCommand(parent,type,position);
		return cmd;
	}
	
	
	
	@Override
	protected IFigure getSizeOnDropFeedback() {
		IFigure f = new Figure();
		f.setBorder(new LineBorder(ColorConstants.red));
		return f;
	}



	@Override 
	  protected EditPolicy createChildEditPolicy(EditPart child) { 
	    return null; 
	  } 
	 
	 
//	  @Override 
//	  protected Command getMoveChildrenCommand(Request request) { 
//	    return null; 
//	  }

}

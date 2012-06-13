package simgrideclipseplugin.graphical.parts;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.w3c.dom.Element;

import simgrideclipseplugin.graphical.ElementPositionMap;
import simgrideclipseplugin.graphical.policies.ElementComponentEditPolicy;

public abstract class ElementAbstractEditPart extends SimgridAbstractEditPart {

	private final Point size = new Point(100,100);
	private Rectangle bounds;

	@Override
	protected void refreshVisuals() {
		
		// assign object size depending on zoom/space
		if (bounds == null){
			bounds = new Rectangle();
		}
		bounds.width = size.x;
		bounds.height = size.y;
		bounds.x = getLocation().x;
		bounds.y = getLocation().y;
		GraphicalEditPart parent = (GraphicalEditPart) getParent();
		if (parent != null){
			parent.setLayoutConstraint(this,getFigure(), bounds);
		}
		if (getSelected() != SELECTED_NONE){
			//little trick to update selection UI
			setSelected(SELECTED_NONE);
			setSelected(SELECTED_PRIMARY);
		}
	}
	
	public Point getLocation() {
		Point location = ElementPositionMap.getPosition((Element) getModel());
		if (location == null){
			location = new Point(0, 0);
		}
		return location;
	}

	public void setLocation(Point location) {
		 ElementPositionMap.setPositionAndRefresh(this, location);
	}
	
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE,new ElementComponentEditPolicy());
		//installEditPolicy(EditPolicy.LAYOUT_ROLE, new SimgridXYLayoutEditPolicy());
	}
}

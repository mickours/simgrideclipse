package simgrideclipseplugin.graphical.parts;

import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.ScalableFreeformLayeredPane;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.FreeformGraphicalRootEditPart;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.editpolicies.SelectionEditPolicy;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.gef.requests.SelectionRequest;
import org.eclipse.gef.tools.DragEditPartsTracker;
import org.w3c.dom.Element;

import simgrideclipseplugin.graphical.ElementPositionMap;
import simgrideclipseplugin.graphical.commands.DeleteElementCommand;
import simgrideclipseplugin.graphical.policies.SimgridXYLayoutEditPolicy;
import simgrideclipseplugin.model.ModelHelper;

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
		((GraphicalEditPart) getParent()).setLayoutConstraint(this,
				getFigure(), bounds);
		
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
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ComponentEditPolicy() {
			
			@Override
			protected Command createDeleteCommand(GroupRequest deleteRequest) {
				DeleteElementCommand cmd = new DeleteElementCommand();
				cmd.setModel((Element) getModel());
				return cmd;
			}
			
		});
		
		//installEditPolicy(EditPolicy.LAYOUT_ROLE, new SimgridXYLayoutEditPolicy());
	}
}

package simgrideclipseplugin.graphical.parts;

import java.util.List;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ShortestPathConnectionRouter;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.swt.SWT;
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;
import org.w3c.dom.Element;

import simgrideclipseplugin.graphical.ElementPositionMap;
import simgrideclipseplugin.graphical.figures.AbstractElementFigure;
import simgrideclipseplugin.graphical.policies.ConnectionPolicy;
import simgrideclipseplugin.graphical.policies.ElementComponentEditPolicy;
import simgrideclipseplugin.model.ModelHelper;

public abstract class AbstractElementEditPart extends SimgridAbstractEditPart
			implements NodeEditPart{

	private final Point size = new Point(100,100);
	private Rectangle bounds;

	@Override
	protected void refreshVisuals() {
		super.refreshVisuals();
		// assign object size and location
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
//		if (getSelected() != SELECTED_NONE){
//			//little trick to update selection UI
//			setSelected(SELECTED_NONE);
//			setSelected(SELECTED_PRIMARY);
//		}

		//get the Figure and the model to refresh the view
		IFigure f = getFigure();
		Element node = (Element) getModel();
		if (node.hasAttribute("id") && f instanceof AbstractElementFigure){
			AbstractElementFigure elementFigure = (AbstractElementFigure) f;
			elementFigure.setId(node.getAttribute("id"));
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
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new ConnectionPolicy());
		//installEditPolicy(EditPolicy.LAYOUT_ROLE, new SimgridXYLayoutEditPolicy());
	}
	
	@SuppressWarnings("restriction")
	public void addConnection(Object model){
		((INodeNotifier) model).addAdapter(this);
	}
	
	@SuppressWarnings("restriction")
	public void removeConnection(Object model){
		((INodeNotifier) model).removeAdapter(this);
	}
	
	@Override
	protected IFigure createFigure() {
		// TODO Auto-generated method stub
		return null;
	}

	protected List<?> getModelSourceConnections() { 
		  return ModelHelper.getSouceConnections((Element)getModel());
	}
	
	protected List<?> getModelTargetConnections() { 
		  return ModelHelper.getTargetConnections((Element)getModel());
	}
	
	@Override
    public ConnectionAnchor getSourceConnectionAnchor(
            ConnectionEditPart connection) {
       return new ChopboxAnchor(getFigure());
    }

    @Override
    public ConnectionAnchor getSourceConnectionAnchor(Request request) {
        return new ChopboxAnchor(getFigure());
    }

    @Override
    public ConnectionAnchor getTargetConnectionAnchor(
            ConnectionEditPart connection) {
        return new ChopboxAnchor(getFigure());
    }

    @Override
    public ConnectionAnchor getTargetConnectionAnchor(Request request) {
        return new ChopboxAnchor(getFigure());
    }
}

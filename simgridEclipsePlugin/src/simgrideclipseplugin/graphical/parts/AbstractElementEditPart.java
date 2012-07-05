package simgrideclipseplugin.graphical.parts;

import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;
import org.w3c.dom.Element;

import simgrideclipseplugin.graphical.ElementPositionMap;
import simgrideclipseplugin.graphical.figures.ElementFigure;
import simgrideclipseplugin.graphical.policies.ConnectionPolicy;
import simgrideclipseplugin.graphical.policies.ElementComponentEditPolicy;
import simgrideclipseplugin.model.ModelHelper;
import simgrideclipseplugin.model.SimgridRules;

@SuppressWarnings("restriction")
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

		//get the Figure and the model to refresh the view
		IFigure f = getFigure();
		Element node = (Element) getModel();
		if (node.hasAttribute("id") && f instanceof ElementFigure){
			ElementFigure elementFigure = (ElementFigure) f;
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
	}
	
	public void addConnection(Object model){
		((INodeNotifier) model).addAdapter(this);
	}
	
	public void removeConnection(Object model){
		((INodeNotifier) model).removeAdapter(this);
	}

	protected List<?> getModelSourceConnections() {
		if(SimgridRules.parentDontAcceptRoute((Element) getModel())){
			return Collections.emptyList();
		}
		return ModelHelper.getSouceConnections((Element)getModel());
	}
	
	protected List<?> getModelTargetConnections() { 
		if(SimgridRules.parentDontAcceptRoute((Element) getModel())){
			return Collections.emptyList();
		}
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

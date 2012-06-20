package simgrideclipseplugin.graphical.parts;

import java.util.Collections;
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
import org.w3c.dom.Element;

import simgrideclipseplugin.graphical.ElementPositionMap;
import simgrideclipseplugin.graphical.policies.ConnectionPolicy;
import simgrideclipseplugin.graphical.policies.ElementComponentEditPolicy;
import simgrideclipseplugin.model.ModelHelper;

public abstract class AbstractElementEditPart extends SimgridAbstractEditPart
			implements NodeEditPart{

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
	
	
	
	protected Figure initConnection(Figure figure){
		ConnectionLayer connLayer = (ConnectionLayer)getLayer(LayerConstants.CONNECTION_LAYER);
        connLayer.setAntialias(SWT.ON);
        connLayer.setConnectionRouter(new ShortestPathConnectionRouter(figure));
        return figure;
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE,new ElementComponentEditPolicy());
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new ConnectionPolicy());
		//installEditPolicy(EditPolicy.LAYOUT_ROLE, new SimgridXYLayoutEditPolicy());
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

package simgrideclipseplugin.graphical.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.w3c.dom.Element;

import simgrideclipseplugin.graphical.policies.ConnectionDeleteEditPolicy;
import simgrideclipseplugin.model.ModelHelper;

public class ConnectionEditPart extends AbstractConnectionEditPart {
	
	

	@Override
	protected void activateFigure() {
		super.activateFigure();
		Element src = ModelHelper.getSourceNode((Element) getModel());
		Element dst = ModelHelper.getTargetNode((Element) getModel());
		setSource((EditPart)getViewer().getEditPartRegistry().get(src));
		setTarget((EditPart)getViewer().getEditPartRegistry().get(dst));
	}



	@Override
	protected IFigure createFigure() {
        PolylineConnection connection = (PolylineConnection) super.createFigure();
        connection.setLineWidth(2);
        PolygonDecoration decoration = new PolygonDecoration();
        decoration.setTemplate(PolygonDecoration.TRIANGLE_TIP);
        connection.setTargetDecoration(decoration);
        return connection;
    }
	
	@Override
    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.CONNECTION_ROLE, new ConnectionDeleteEditPolicy());
        installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new ConnectionEndpointEditPolicy());
    }
}

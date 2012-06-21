package simgrideclipseplugin.graphical.parts;

import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ShortestPathConnectionRouter;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editpolicies.RootComponentEditPolicy;
import org.eclipse.swt.SWT;

import simgrideclipseplugin.graphical.figures.ContentPaneFigure;
import simgrideclipseplugin.graphical.policies.SimgridXYLayoutEditPolicy;

public class PlatformEditPart extends SimgridAbstractEditPart {

	@Override
	protected IFigure createFigure() {
		IFigure figure = new ContentPaneFigure("Platform");
		ConnectionLayer connLayer = (ConnectionLayer)getLayer(LayerConstants.CONNECTION_LAYER);
        connLayer.setAntialias(SWT.ON);
        connLayer.setConnectionRouter(new ShortestPathConnectionRouter(figure));
		return figure;
	}
	
	

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE,new RootComponentEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new SimgridXYLayoutEditPolicy());
	}
}

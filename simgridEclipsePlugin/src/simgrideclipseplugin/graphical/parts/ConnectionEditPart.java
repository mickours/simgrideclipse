package simgrideclipseplugin.graphical.parts;

import java.util.List;

import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.ShortestPathConnectionRouter;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.swt.SWT;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapter;
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;
import org.w3c.dom.Element;

import simgrideclipseplugin.graphical.policies.ConnectionDeleteEditPolicy;
import simgrideclipseplugin.model.ModelHelper;

@SuppressWarnings("restriction")
public class ConnectionEditPart extends AbstractConnectionEditPart implements INodeAdapter{
	
	@Override
    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.CONNECTION_ROLE, new ConnectionDeleteEditPolicy());
        installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new ConnectionEndpointEditPolicy());
    }

	public void updateLinks(){
		//set end points and make them refresh
		Element src = ModelHelper.getSourceNode((Element) getModel());
		GraphicalEditPart srcEP = (GraphicalEditPart) getViewer().getEditPartRegistry().get(src);
		setSource(srcEP);
		Element dst = ModelHelper.getTargetNode((Element) getModel());
		GraphicalEditPart dstEP = (GraphicalEditPart) getViewer().getEditPartRegistry().get(dst);
		setTarget(dstEP);
		this.getFigure().invalidate();
	}

	
	//Comming from SimgridAbstractEditPart (no multiple heritage)
	@Override
	public List<?> getModelChildren() {
		return ModelHelper.getNoConnectionChildren((Element) getModel());
	}

	@Override
	public void activate() {
		updateLinks();
		((INodeNotifier) getModel()).addAdapter(this);
		super.activate();
	}
	
	@Override
	public void deactivate() {
		((INodeNotifier) getModel()).removeAdapter(this);
		super.deactivate();
	}

	@Override
	public void notifyChanged(INodeNotifier notifier, int eventType,
			Object changedFeature, Object oldValue, Object newValue, int pos) {
		// TODO: update UI can be optimized
		// update only the current if it's an attribute change OR the children
		//if (eventType == INodeNotifier.CHANGE) {
			updateLinks();
			refreshVisuals();
		//} else {
			refreshChildren();
		//}
	}

	@Override
	public boolean isAdapterForType(Object type) {
		return type.equals(Element.class);
	}
}

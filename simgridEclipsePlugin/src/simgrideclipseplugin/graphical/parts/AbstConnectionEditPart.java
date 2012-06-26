package simgrideclipseplugin.graphical.parts;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapter;
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;
import org.w3c.dom.Element;

import simgrideclipseplugin.graphical.figures.AbstractConnectionFigure;
import simgrideclipseplugin.graphical.policies.ConnectionDeleteEditPolicy;
import simgrideclipseplugin.model.ModelHelper;

@SuppressWarnings("restriction")
public abstract class AbstConnectionEditPart extends AbstractConnectionEditPart implements INodeAdapter{

	@Override
	protected void refreshVisuals() {
		AbstractConnectionFigure connection = (AbstractConnectionFigure)getFigure();
		String sim = ((Element)getModel()).getAttribute("symmetrical");
		if ( sim.isEmpty() || sim.equals("YES")){
	        connection.setSourceDecoration();
		}else{
			connection.resetSourceDecoration();
		}
		connection.invalidate();
		connection.repaint();
		super.refreshVisuals();
	}
	
	@Override
    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.CONNECTION_ROLE, new ConnectionDeleteEditPolicy());
        installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new ConnectionEndpointEditPolicy());
    }
	
	@Override
	public void setSource(EditPart editPart) {
		if (getSource() != editPart){
			if (getSource() != null){
				((AbstractElementEditPart)getSource()).removeConnection(getModel());
			}
			if (editPart != null){
				((AbstractElementEditPart)editPart).addConnection(getModel());
			}
		}
		super.setSource(editPart);
	}

	@Override
	public void setTarget(EditPart editPart) {
		if (getTarget() != editPart){
			if (getTarget() != null){
				((AbstractElementEditPart)getTarget()).removeConnection(getModel());
			}
			if (editPart != null){
				((AbstractElementEditPart)editPart).addConnection(getModel());
			}
		}
		super.setTarget(editPart);
	}

	//Coming from SimgridAbstractEditPart (no multiple heritage)
	@Override
	public List<?> getModelChildren() {
		return ModelHelper.getNoConnectionChildren((Element) getModel());
	}

	@Override
	public void activate() {
		((INodeNotifier) getModel()).addAdapter(this);
		super.activate();
	}
	
	@Override
	public void deactivate() {
		((INodeNotifier) getModel()).removeAdapter(this);
		if (getSource() != null){
			((AbstractElementEditPart)getSource()).removeConnection(getModel());
		}
		if (getTarget() != null){
			((AbstractElementEditPart)getTarget()).removeConnection(getModel());
		}
		super.deactivate();
	}

	@Override
	public void notifyChanged(INodeNotifier notifier, int eventType,
			Object changedFeature, Object oldValue, Object newValue, int pos) {
		// TODO: update UI can be optimized
		EditPartCommons.updateLinks((Element) getModel(),this);
		refresh();
	}

	@Override
	public boolean isAdapterForType(Object type) {
		return type.equals(Element.class);
	}
}

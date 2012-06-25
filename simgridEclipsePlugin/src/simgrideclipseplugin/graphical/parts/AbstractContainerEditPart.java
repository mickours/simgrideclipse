package simgrideclipseplugin.graphical.parts;

import java.util.List;

import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ShortestPathConnectionRouter;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editpolicies.RootComponentEditPolicy;
import org.eclipse.swt.SWT;
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;
import org.w3c.dom.Element;

import simgrideclipseplugin.graphical.figures.ContentPaneFigure;
import simgrideclipseplugin.graphical.policies.SimgridXYLayoutEditPolicy;
import simgrideclipseplugin.model.ElementList;
import simgrideclipseplugin.model.ModelHelper;

public abstract class AbstractContainerEditPart extends SimgridAbstractEditPart {

	@Override
	protected IFigure createFigure() {
		IFigure figure = new ContentPaneFigure();
		//Initialize connection layer
		ConnectionLayer connLayer = (ConnectionLayer)getLayer(LayerConstants.CONNECTION_LAYER);
        connLayer.setAntialias(SWT.ON);
        connLayer.setConnectionRouter(new ShortestPathConnectionRouter(figure));
		return figure;
	}
	
	@SuppressWarnings("restriction")
	@Override
	public void notifyChanged(INodeNotifier notifier, int eventType,
			Object changedFeature, Object oldValue, Object newValue, int pos) {
		// TODO: update UI can be optimized
		// update links if necessary
		Element e = null;
		if (newValue instanceof Element && ElementList.isConnection(((Element) newValue).getTagName())) {
			e = (Element) newValue;
		}
		else if(oldValue instanceof Element && ElementList.isConnection(((Element) oldValue).getTagName())){
			e = (Element) oldValue;
		}
		if (e != null){
			EditPartCommons.updateLinks(e, this);
		}
		super.notifyChanged(notifier, eventType, changedFeature, oldValue, newValue, pos);
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE,new RootComponentEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new SimgridXYLayoutEditPolicy());
	}
	
	@Override
	public List<?> getModelChildren() {
		return ModelHelper.getNoConnectionChildren((Element) getModel());
	}
}

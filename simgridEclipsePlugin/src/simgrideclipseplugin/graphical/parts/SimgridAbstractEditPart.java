package simgrideclipseplugin.graphical.parts;

import java.util.List;

import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapter;
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;
import org.w3c.dom.Element;

import simgrideclipseplugin.model.ModelHelper;

@SuppressWarnings("restriction")
public abstract class SimgridAbstractEditPart extends AbstractGraphicalEditPart
		implements INodeAdapter {

	@SuppressWarnings("rawtypes")
	@Override
	public List getModelChildren() {
		return ModelHelper.getChildren((Element) getModel());
	}

	@Override
	public void activate() {
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
		if (eventType == 1) {
			refreshVisuals();
		} else {
			refreshChildren();
		}
	}

	@Override
	public boolean isAdapterForType(Object type) {
		return type.equals(Element.class);
	}
}

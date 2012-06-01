package simgrideclipseplugin.graphical.parts;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapter;
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.eclipse.gef.GraphicalEditPart;

@SuppressWarnings("restriction")
public abstract class SimgridAbstractEditPart extends AbstractGraphicalEditPart
		implements INodeAdapter {

//	@Override
//	protected void refreshVisuals() {
//		super.refreshVisuals();
//
//		int x = new Double(Math.random() * 400).intValue();
//		int y = new Double(Math.random() * 400).intValue();
//
//		Rectangle bounds = new Rectangle(x, y, 100, 100);
//		((GraphicalEditPart) getParent()).setLayoutConstraint(this,
//				getFigure(), bounds);
//	}

	@SuppressWarnings("rawtypes")
	@Override
	public List getModelChildren() {
		// return the model children
		List<Element> elemList = new LinkedList<Element>();
		NodeList l = ((Element) getModel()).getChildNodes();
		for (int i = 0; i < l.getLength(); i++) {
			if (l.item(i) instanceof Element) {
				elemList.add((Element) l.item(i));
			}
		}
		return elemList;
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
		// TODO remove model listener on this
	}

	/**
	 * Sent to adapter when notifier changes. Each notifier is responsible for
	 * defining specific eventTypes, feature changed, etc.
	 */
	@Override
	public void notifyChanged(INodeNotifier notifier, int eventType,
			Object changedFeature, Object oldValue, Object newValue, int pos) {
		// update UI
		// TODO update only the current OR the children
		refreshVisuals();
		refreshChildren();
	}

	/**
	 * The infrastructure calls this method to determine if the adapter is
	 * appropriate for 'type'. Typically, adapters return true based on identity
	 * comparison to 'type', but this is not required, that is, the decision can
	 * be based on complex logic.
	 */
	@Override
	public boolean isAdapterForType(Object type) {
		return type.equals(Element.class);
	}
}

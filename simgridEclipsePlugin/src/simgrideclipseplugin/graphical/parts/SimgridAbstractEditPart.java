package simgrideclipseplugin.graphical.parts;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;
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
	
	private Point position = new Point();
	
	public void setPosition(Point position) {
		this.position = position;
		refreshVisuals();
		getFigure().revalidate();
	}
	
	private Point getPosition(){
		return position;
	}

	@Override
	protected void refreshVisuals() {
		super.refreshVisuals();
		int x = getPosition().x;
		int y = getPosition().y;
		
		Rectangle bounds = new Rectangle(x, y, 100, 100);
		((GraphicalEditPart) getParent())
			.setLayoutConstraint(this,getFigure(), bounds);
	}

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
	}


	@Override
	public void notifyChanged(INodeNotifier notifier, int eventType,
			Object changedFeature, Object oldValue, Object newValue, int pos) {
		//TODO: update UI can be optimized
		//update only the current if it's an attribute change OR the children
		if (eventType == 1){
			refreshVisuals();
		}
		else{
			refreshChildren();
		}
	}

	@Override
	public boolean isAdapterForType(Object type) {
		return type.equals(Element.class);
	}
}

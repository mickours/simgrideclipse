package simgrideclipseplugin.graphical.parts;

import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.ScalableFreeformLayeredPane;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editparts.FreeformGraphicalRootEditPart;
import org.eclipse.gef.editpolicies.SelectionEditPolicy;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapter;
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;
import org.w3c.dom.Element;

import simgrideclipseplugin.graphical.ElementPositionMap;
import simgrideclipseplugin.graphical.figures.ASfigure;
import simgrideclipseplugin.model.ModelHelper;

@SuppressWarnings("restriction")
public abstract class SimgridAbstractEditPart extends AbstractGraphicalEditPart
		implements INodeAdapter {

	private Point location;

	@Override
	protected void refreshVisuals() {
		location = ElementPositionMap.getPosition((Element) getModel());
		if (location == null)
			location = new Point(0, 0);

		// assign object size depending on zoom/space
		Rectangle bounds = new Rectangle(location.x, location.y, 100, 100);
		((GraphicalEditPart) getParent()).setLayoutConstraint(this,
				getFigure(), bounds);
	}

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

	private IFigure getScaledFeedbackLayer() {
		FreeformGraphicalRootEditPart dep = (FreeformGraphicalRootEditPart) getRoot();
		if (dep != null) {
			IFigure layer = dep.getLayer(LayerConstants.SCALABLE_LAYERS);
			if (layer instanceof ScalableFreeformLayeredPane) {
				Layer feedbackLayer = ((ScalableFreeformLayeredPane) layer)
						.getLayer(LayerConstants.SCALED_FEEDBACK_LAYER);
				if (feedbackLayer == null) {
					feedbackLayer = new FreeformLayer();
					feedbackLayer.setEnabled(false);
					layer.add(feedbackLayer,
							LayerConstants.SCALED_FEEDBACK_LAYER);
				}
				return feedbackLayer;
			}
		}
		return null;
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE,
				new SelectionEditPolicy() {
					private Figure f;

					@Override
					protected void showSelection() {
						IFigure feedBackLayer = getScaledFeedbackLayer();
						if (feedBackLayer != null){
							f = new Figure();
							f.setBorder(new LineBorder(ColorConstants.red));
							Rectangle bounds = new Rectangle(location.x,
									location.y, 100, 100);
							LayoutManager layoutManager = feedBackLayer.getLayoutManager();
							if (layoutManager == null){
								feedBackLayer.setLayoutManager(new FreeformLayout());
							}
							feedBackLayer.getLayoutManager().setConstraint(f,
									bounds);
							feedBackLayer.add(f);
						}
					}

					@Override
					protected void hideSelection() {
						IFigure feedBackLayer = getScaledFeedbackLayer();
						if (feedBackLayer != null && f != null) {
							feedBackLayer.remove(f);
						}
					}
				});
	}

	@Override
	public DragTracker getDragTracker(Request request) {
		// TODO manage gragging object
		return super.getDragTracker(request);
	}
}

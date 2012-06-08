package simgrideclipseplugin.graphical.parts;

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
import org.eclipse.gef.editparts.FreeformGraphicalRootEditPart;
import org.eclipse.gef.editpolicies.SelectionEditPolicy;
import org.eclipse.gef.tools.DragEditPartsTracker;
import org.w3c.dom.Element;

import simgrideclipseplugin.graphical.ElementPositionMap;

public abstract class ElementAbstractEditPart extends SimgridAbstractEditPart {

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
		((GraphicalEditPart) getParent()).setLayoutConstraint(this,
				getFigure(), bounds);
		if (getSelected() != SELECTED_NONE){
			//little trick to update selection UI
			setSelected(SELECTED_NONE);
			setSelected(SELECTED_PRIMARY);
		}
	}
	
	private Point getLocation() {
		Point location = ElementPositionMap.getPosition((Element) getModel());
		if (location == null){
			location = new Point(0, 0);
		}
		return location;
	}

	private void setLocation(Point location) {
		 ElementPositionMap.setPositionAndRefresh(this, location);
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
							LayoutManager layoutManager = feedBackLayer.getLayoutManager();
							if (layoutManager == null){
								feedBackLayer.setLayoutManager(new FreeformLayout());
							}
							if (f == null){
								f = new Figure();
							}
							
							f.setBorder(new LineBorder(ColorConstants.red));
							feedBackLayer.getLayoutManager().setConstraint(f,bounds);
							if (! feedBackLayer.getChildren().contains(f)){
								feedBackLayer.add(f);
								
							}
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
		return new DragEditPartsTracker(this){

			@Override
			protected void performDrag() {
				Point p = this.getLocation();
				//get the center
				p.x -= size.x/2;
				p.y -= size.y/2;
				setLocation(p);
				super.performDrag();
			}
			
		};
	}

}

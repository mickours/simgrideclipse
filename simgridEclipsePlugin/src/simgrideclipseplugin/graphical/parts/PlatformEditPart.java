package simgrideclipseplugin.graphical.parts;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.ToolbarLayout;

public class PlatformEditPart extends SimgridAbstractEditPart {

	
	@Override
	protected void refreshVisuals() {
		super.refreshVisuals();
	}

	@Override
	protected IFigure createFigure() {
		Figure f =new Figure();
		f.setLayoutManager(new ToolbarLayout());
		f.setBorder(new LineBorder(ColorConstants.blue, 3));
		return f;
	}

	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub

	}
}

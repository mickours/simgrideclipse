package simgrideclipseplugin.graphical.parts;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformFigure;
import org.eclipse.draw2d.FreeformLayeredPane;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.XYLayout;

import simgrideclipseplugin.graphical.AutomaticGraphLayoutRenderer;

public class PlatformEditPart extends SimgridAbstractEditPart {

	
	@Override
	protected void refreshVisuals() {
		AutomaticGraphLayoutRenderer.INSTANCE.computeLayout();
	}

	@Override
	protected IFigure createFigure() {
		FreeformFigure f = new FreeformLayeredPane(); 
		f.setLayoutManager(new XYLayout());
		f.setBorder(new LineBorder(ColorConstants.blue, 3));
		return f;
	}

	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub

	}
}

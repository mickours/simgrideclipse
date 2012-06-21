package simgrideclipseplugin.graphical.parts;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MidpointLocator;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.swt.SWT;
import org.w3c.dom.Element;

import simgrideclipseplugin.graphical.figures.ASrouteFigure;

public class ASrouteEditPart extends ConnectionEditPart {
	
	private PolygonDecoration decoration;

	@Override
	protected IFigure createFigure() {
		return new ASrouteFigure();
	}

	@Override
	protected void refreshVisuals() {
		PolylineConnection connection = (PolylineConnection)getFigure();
		if ( ! ((Element)getModel()).getAttribute("symmetrical").equals("YES")){
			if (decoration == null){
				decoration = new PolygonDecoration();
			}
	        decoration.setTemplate(PolygonDecoration.TRIANGLE_TIP);
	        decoration.setLineWidth(2);
	        connection.setTargetDecoration(decoration);
		}else{
			if (decoration != null){
				connection.remove(decoration);
			}
		}
		connection.invalidate();
		connection.repaint();
		super.refreshVisuals();
	}
	
}

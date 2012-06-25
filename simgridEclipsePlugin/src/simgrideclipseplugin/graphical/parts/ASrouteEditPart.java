package simgrideclipseplugin.graphical.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.w3c.dom.Element;

import simgrideclipseplugin.graphical.figures.ASrouteFigure;

public class ASrouteEditPart extends AbstConnectionEditPart {
	


	@Override
	protected IFigure createFigure() {
		return new ASrouteFigure();
	}

	@Override
	protected void refreshVisuals() {
		ASrouteFigure connection = (ASrouteFigure)getFigure();
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
	
}

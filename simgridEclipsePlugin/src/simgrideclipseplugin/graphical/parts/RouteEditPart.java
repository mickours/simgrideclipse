package simgrideclipseplugin.graphical.parts;

import org.eclipse.draw2d.IFigure;
import simgrideclipseplugin.graphical.figures.RouteFigure;

public class RouteEditPart extends AbstConnectionEditPart {

	@Override
	protected IFigure createFigure() {
		return new RouteFigure();
	}
}

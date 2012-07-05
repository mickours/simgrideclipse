package simgrideclipseplugin.graphical.parts;

import org.eclipse.draw2d.IFigure;

import simgrideclipseplugin.graphical.figures.BypassConnectionFigure;

public class BypassRouteEditPart extends AbstConnectionEditPart{

	@Override
		protected IFigure createFigure() {
			return new BypassConnectionFigure();
		}
	
}

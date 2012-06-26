package simgrideclipseplugin.graphical.parts;

import org.eclipse.draw2d.IFigure;

import simgrideclipseplugin.graphical.figures.ASrouteFigure;

public class ASrouteEditPart extends AbstConnectionEditPart {
	
	@Override
	protected IFigure createFigure() {
		return new ASrouteFigure();
	}

}

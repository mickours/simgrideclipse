package simgrideclipseplugin.graphical.parts;

import org.eclipse.draw2d.IFigure;

import simgrideclipseplugin.graphical.figures.HostFigure;

public class HostEditPart extends AbstractElementEditPart {

	@Override
	protected IFigure createFigure() {
		return new HostFigure();
	}

	
}

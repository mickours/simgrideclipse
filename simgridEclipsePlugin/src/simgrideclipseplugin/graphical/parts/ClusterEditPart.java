package simgrideclipseplugin.graphical.parts;

import org.eclipse.draw2d.IFigure;

import simgrideclipseplugin.graphical.figures.ClusterFigure;

public class ClusterEditPart extends AbstractElementEditPart {

	@Override
	protected IFigure createFigure() {
		return new ClusterFigure();
	}

}

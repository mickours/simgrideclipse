package simgrideclipseplugin.graphical.parts;

import org.eclipse.draw2d.IFigure;

import simgrideclipseplugin.graphical.figures.ElementFigure;
import simgrideclipseplugin.model.ElementList;

public class BackboneEditPart extends AbstractElementEditPart {

	@Override
	protected IFigure createFigure() {
		return new ElementFigure(ElementList.BACKBONE);
	}

}

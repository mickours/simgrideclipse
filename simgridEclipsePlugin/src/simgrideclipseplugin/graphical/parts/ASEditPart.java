package simgrideclipseplugin.graphical.parts;

import org.eclipse.draw2d.IFigure;

import simgrideclipseplugin.graphical.figures.ASfigure;

public class ASEditPart extends AbstractElementEditPart {

	@Override
	protected IFigure createFigure() {
		return new ASfigure();
	}
	
//	public IFigure getContentPane(){
//		return ((ASfigure)getFigure()).getContentPane();
//	}
	
	
}

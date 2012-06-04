package simgrideclipseplugin.graphical.parts;

import org.eclipse.draw2d.IFigure;

import simgrideclipseplugin.graphical.figures.ContentPaneFigure;

public class PlatformEditPart extends SimgridAbstractEditPart {

	@Override
	protected IFigure createFigure() {
		return new ContentPaneFigure("Platform");
	}

	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub

	}
	
//	public IFigure getContentPane(){
//		return ((ContentPaneFigure)getFigure()).getContentPane();
//	}
}

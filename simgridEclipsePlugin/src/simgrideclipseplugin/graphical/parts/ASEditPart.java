package simgrideclipseplugin.graphical.parts;

import org.eclipse.draw2d.IFigure;
import org.w3c.dom.Element;

import simgrideclipseplugin.graphical.figures.ASfigure;

public class ASEditPart extends AbstractElementEditPart {

	@Override
	protected IFigure createFigure() {
		return new ASfigure();
	}
	
	@Override
	public void refreshVisuals(){
		super.refreshVisuals();
		//get the Figure and the model to refresh the view
		ASfigure f = (ASfigure) getFigure();
		Element node = (Element) getModel();
		f.setId(node.getAttribute("id"));
		f.setRouting(node.getAttribute("routing"));
	}
	
	public IFigure getContentPane(){
		return ((ASfigure)getFigure()).getContentPane();
	}
	
	
}

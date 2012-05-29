package simgrideclipseplugin.graphical.parts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import simgrideclipseplugin.graphical.figures.ASfigure;

public class ASPart extends AbstractGraphicalEditPart {

	@Override
	protected IFigure createFigure() {
		return new ASfigure();
	}
	
	@Override
	public void refreshVisuals(){
		//TODO get the Figure and the model to refresh the view
		//http://wiki.eclipse.org/GEF_Description#Model
	}
	
	@Override
	public List getModelChildren(){
		//TODO return the model children
		return new ArrayList();
		
	}

	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub

	}

}

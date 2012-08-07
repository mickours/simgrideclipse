package simgrideclipseplugin.graphical.parts;

import java.util.Collection;
import java.util.List;
import java.util.Vector;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;

import simgrideclipseplugin.graphical.figures.NonEditableASrouteFigure;

public class NonEditableASrouteEditPart extends AbstractConnectionEditPart {
	
	@Override
	protected IFigure createFigure() {
		return new NonEditableASrouteFigure();
	}

	@Override
	protected void createEditPolicies() {
		// 
		
	}	
	
//	@Override
//	public void setSource(EditPart editPart) {	
//		superSetSource(editPart);
//	}
//	
//	@Override
//	public void setTarget(EditPart editPart) {	
//		super.superSetTarget(editPart);
//	}
//
//	@Override
//	public List<?> getModelChildren() {
//		return new Vector<Object>() ;
//	}
//
//	@Override
//	public void performRequest(Request req) {		
//	}
//	
//	
	public void unregister()
	{
		super.unregisterVisuals();
	}
	public void register()
	{
		super.register();
	}

}

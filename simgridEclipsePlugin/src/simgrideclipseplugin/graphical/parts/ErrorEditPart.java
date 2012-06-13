package simgrideclipseplugin.graphical.parts;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

/**
 * Dummy EditPart that is made for an unknown Element
 * @author mmercier
 *
 */
public class ErrorEditPart extends AbstractGraphicalEditPart{

	@Override
	protected IFigure createFigure() {
		return new Figure();
	}

	@Override
	protected void createEditPolicies() {
		
	}
	
}
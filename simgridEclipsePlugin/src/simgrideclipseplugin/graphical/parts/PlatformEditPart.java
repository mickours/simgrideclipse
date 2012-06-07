package simgrideclipseplugin.graphical.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editpolicies.RootComponentEditPolicy;

import simgrideclipseplugin.graphical.figures.ContentPaneFigure;
import simgrideclipseplugin.graphical.policies.SimgridXYLayoutEditPolicy;

public class PlatformEditPart extends SimgridAbstractEditPart {

	@Override
	protected IFigure createFigure() {
		return new ContentPaneFigure("Platform");
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE,new RootComponentEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new SimgridXYLayoutEditPolicy());
	}
}

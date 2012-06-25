package simgrideclipseplugin.graphical.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.Request;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.PlatformUI;
import org.w3c.dom.Element;

import simgrideclipseplugin.graphical.actions.GoIntoAction;
import simgrideclipseplugin.graphical.figures.ASfigure;

public class ASEditPart extends AbstractElementEditPart {

	@Override
	protected IFigure createFigure() {
		return new ASfigure();
	}

	@Override
	protected void refreshVisuals() {
		super.refreshVisuals();
		ASfigure f = (ASfigure)getFigure();
		Element node = (Element)getModel();
		f.setRouting(node.getAttribute("routing"));
	}

	@Override
	public void performRequest(Request req) {
		if (req.getType().equals(REQ_OPEN)){
			IAction a = new GoIntoAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage().getActivePart());
			a.run();
		}
	}
	
//	public IFigure getContentPane(){
//		return ((ASfigure)getFigure()).getContentPane();
//	}
	
	
}

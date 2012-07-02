package simgrideclipseplugin.graphical.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.ui.IWorkbenchPart;
import org.w3c.dom.Element;

import simgrideclipseplugin.graphical.actions.GoIntoAction;
import simgrideclipseplugin.graphical.figures.ASfigure;

public class ASEditPart extends AbstractElementEditPart {

	private GoIntoAction goIntoAction;
	private IWorkbenchPart editor;

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
	
	protected void setEditor(){
		EditDomain ed = getViewer().getEditDomain();
		if (ed instanceof DefaultEditDomain)
			editor = ((DefaultEditDomain)getViewer().getEditDomain()).getEditorPart();
	}

	@Override
	protected void addChild(EditPart child, int index) {
		//do nothing
	}

	
	@Override
	public void activate() {
		setEditor();
		goIntoAction = new GoIntoAction(editor);
		super.activate();
	}
	
	@Override
	public void deactivate() {
		goIntoAction.dispose();
		super.deactivate();
	}

	@Override
	public void performRequest(Request req) {
		if (req.getType().equals(REQ_OPEN)){
			goIntoAction.doGoInto(this);
		}
	}
	
//	public IFigure getContentPane(){
//		return ((ASfigure)getFigure()).getContentPane();
//	}
	
	
	
}

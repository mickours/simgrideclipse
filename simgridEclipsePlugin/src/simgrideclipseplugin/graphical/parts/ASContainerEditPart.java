package simgrideclipseplugin.graphical.parts;

import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.Request;
import org.eclipse.ui.IWorkbenchPart;
import org.w3c.dom.Element;

import simgrideclipseplugin.graphical.actions.GoOutAction;
import simgrideclipseplugin.graphical.figures.ContentPaneFigure;
import simgrideclipseplugin.model.ElementList;

public class ASContainerEditPart extends AbstractContainerEditPart {

	private IWorkbenchPart editor;
	private GoOutAction goOutAction;

	@Override
	protected void refreshVisuals() {
		ContentPaneFigure f = (ContentPaneFigure) getFigure();
		Element node = (Element)getModel();
		f.setTitle(ElementList.AS+" id=\""+node.getAttribute("id")+"\"");
		super.refreshVisuals();
	}
	
	@Override
	public void performRequest(Request req) {
		if (req.getType().equals(REQ_OPEN)){
			goOutAction.run();
		}
	}
	
	protected void setEditor(){
		EditDomain ed = getViewer().getEditDomain();
		if (ed instanceof DefaultEditDomain)
			editor = ((DefaultEditDomain)getViewer().getEditDomain()).getEditorPart();
	}
	
	@Override
	public void activate() {
		setEditor();
		goOutAction = new GoOutAction(editor);
		super.activate();
	}
	
	@Override
	public void deactivate() {
		goOutAction.dispose();
		super.deactivate();
	}
	
}

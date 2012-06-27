package simgrideclipseplugin.graphical.actions;

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.actions.WorkbenchPartAction;
import org.eclipse.ui.IWorkbenchPart;
import org.w3c.dom.Element;

import simgrideclipseplugin.editors.SimgridGraphicEditor;
import simgrideclipseplugin.graphical.SimgridIconProvider;

public class GoUpAction extends WorkbenchPartAction {

	public GoUpAction(IWorkbenchPart part) {
		super(part);
	}

	public static final String ID = "simgrideclipseplugin.GoUp";

	@Override
	protected void init() {
		setId(ID);
		setText("Go up");
		setToolTipText("Go up the selected AS");
		setImageDescriptor(SimgridIconProvider.getIconImageDescriptor(ID));
	}

	public void run() {
		GraphicalViewer viewer = ((SimgridGraphicEditor)getWorkbenchPart()).getGraphicalViewer();
		Element current = (Element) viewer.getContents().getModel();
		viewer.setContents(current.getParentNode());
	}

	@Override
	protected boolean calculateEnabled() {
		GraphicalViewer viewer = ((SimgridGraphicEditor)getWorkbenchPart()).getGraphicalViewer();
		Element current = (Element) viewer.getContents().getModel();
		return (current.getParentNode() instanceof Element);
	}

}

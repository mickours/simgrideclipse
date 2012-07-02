package simgrideclipseplugin.graphical.actions;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.actions.WorkbenchPartAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.w3c.dom.Element;

import simgrideclipseplugin.editors.SimgridGraphicEditor;
import simgrideclipseplugin.graphical.SimgridIconProvider;

public class GoOutAction extends WorkbenchPartAction {

	public GoOutAction(IWorkbenchPart part) {
		super(part);
	}

	public static final String ID = "simgrideclipseplugin.GoOut";
	public static final String TEXT = "Go out";
	public static final String TOOL_TIP = "Go out the current AS";

	@Override
	protected void init() {
		setId(ID);
		setText(TEXT);
		setToolTipText(TOOL_TIP);
		setImageDescriptor(SimgridIconProvider.getIconImageDescriptor(ID));
	}

	public void run() {
		SimgridGraphicEditor graphEditor = (SimgridGraphicEditor)getWorkbenchPart();
		EditPart content = graphEditor.getGraphicalContents();
		Element current = (Element) content.getModel();
		graphEditor.setGraphicalContents(current.getParentNode());
		//update selection
		StructuredSelection sel =new StructuredSelection(current.getParentNode());
		graphEditor.externalSelectionChanged(sel);
	}

	@Override
	protected boolean calculateEnabled() {
		SimgridGraphicEditor graphEditor = (SimgridGraphicEditor)getWorkbenchPart();
		EditPart content = graphEditor.getGraphicalContents();
		Element current = (Element) content.getModel();
		return (current.getParentNode() instanceof Element);
	}

}

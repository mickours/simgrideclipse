package simgrideclipseplugin.graphical.actions;

import java.util.List;

import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;

import simgrideclipseplugin.editors.SimgridGraphicEditor;
import simgrideclipseplugin.graphical.AutomaticGraphLayoutHelper;
import simgrideclipseplugin.graphical.SimgridIconProvider;

public class GoIntoAction extends SelectionAction{

	public GoIntoAction(IWorkbenchPart part) {
		super(part);
	}

	private static final String ID = "simgrideclipseplugin.GoInto";

	@Override
	protected void init() {
		setId(ID);
		setText("Go into");
		setToolTipText("Go into the selected AS");
		setImageDescriptor(SimgridIconProvider.getIconImageDescriptor(ID));
	}

	public void run() {
		//FIXME : make the selection work properly to make this work!!!
		List sel = getSelectedObjects();
		if (getSelectedObjects().size() == 1){
			SimgridGraphicEditor wb = (SimgridGraphicEditor) getWorkbenchPart();
			wb.setViewerContents(getSelectedObjects().get(0));
		}
	}

	@Override
	protected boolean calculateEnabled() {
		// TODO Auto-generated method stub
		return false;
	}
}

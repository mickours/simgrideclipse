package simgrideclipseplugin.graphical.actions;

import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;

import simgrideclipseplugin.graphical.SimgridIconProvider;
import simgrideclipseplugin.graphical.parts.ASEditPart;

public class GoIntoAction extends SelectionAction{

	public GoIntoAction(IWorkbenchPart part) {
		super(part);
	}

	public static final String ID = "simgrideclipseplugin.GoInto";

	@Override
	protected void init() {
		setId(ID);
		setText("Go into");
		setToolTipText("Go into the selected AS");
		setImageDescriptor(SimgridIconProvider.getIconImageDescriptor(ID));
	}

	public void run() {
		ASEditPart asEP  = (ASEditPart)getSelectedObjects().get(0);
		asEP.getViewer().setContents(asEP.getModel());
}

	@Override
	protected boolean calculateEnabled() {
		// we only want enabled if is single ASEditPart selection
	    if(getSelectedObjects().size() !=1
	        || (!(getSelectedObjects().get(0) instanceof ASEditPart))){
	        return false;
	    }
	    return true;
	}
}

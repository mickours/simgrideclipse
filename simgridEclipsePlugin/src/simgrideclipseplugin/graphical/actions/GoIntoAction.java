package simgrideclipseplugin.graphical.actions;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchPart;

import simgrideclipseplugin.graphical.parts.ASEditPart;
import simgrideclipseplugin.graphical.providers.SimgridIconProvider;

public class GoIntoAction extends SelectionAction{

	public GoIntoAction(IWorkbenchPart part) {
		super(part);
	}

	public static final String ID = "simgrideclipseplugin.GoInto";
	public static final String TEXT = "Go into";
	public static final String TOOL_TIP = "Go into the selected AS";

	@Override
	protected void init() {
		setId(ID);
		setText(TEXT);
		setToolTipText(TOOL_TIP);
		setImageDescriptor(SimgridIconProvider.getIconImageDescriptor(ID));
		setSelectionProvider(getWorkbenchPart().getSite().getSelectionProvider());
	}

	public void run() {
		ASEditPart asEP  = (ASEditPart)getSelectedObjects().get(0);
		doGoInto(asEP);
	}
	
	public void doGoInto(ASEditPart asEP){
		EditPartViewer viewer = asEP.getViewer();
		viewer.setContents(asEP.getModel());
		//update selection
		ISelection sel =new StructuredSelection(viewer.getRootEditPart().getContents());
		getWorkbenchPart().getSite().getSelectionProvider().setSelection(sel);
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

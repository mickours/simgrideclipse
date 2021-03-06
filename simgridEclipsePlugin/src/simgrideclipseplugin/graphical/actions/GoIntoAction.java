package simgrideclipseplugin.graphical.actions;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.w3c.dom.Element;

import simgrideclipseplugin.editors.SimgridGraphicEditor;
import simgrideclipseplugin.graphical.parts.ASEditPart;
import simgrideclipseplugin.graphical.providers.SimgridIconProvider;

public class GoIntoAction extends SelectionAction{

	public GoIntoAction(IWorkbenchPart part) {
		super(part);
	}

	public static final String ID = "simgrideclipseplugin.GoInto";
	public static final String MESSAGE = "Go into";
	public static final String TOOL_TIP = "Go into the selected AS";

	@Override
	protected void init() {
		setId(ID);
		setText(MESSAGE);
		setToolTipText(TOOL_TIP);
		setImageDescriptor(SimgridIconProvider.getIconImageDescriptor(ID));
		setSelectionProvider(getWorkbenchPart().getSite().getSelectionProvider());
	}

	@Override
	public void run() {
		ASEditPart asEP  = (ASEditPart)getSelectedObjects().get(0);
		doGoInto(asEP);
	}
	
	public void doGoInto(ASEditPart asEP){
		EditPartViewer viewer = asEP.getViewer();
		//viewer.setContents(asEP.getModel());
		((SimgridGraphicEditor) getWorkbenchPart()).setGraphicalContents((Element) asEP.getModel());
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

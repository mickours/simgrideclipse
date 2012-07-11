package simgrideclipseplugin.graphical.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IWorkbenchPart;
import org.w3c.dom.Element;

import simgrideclipseplugin.graphical.parts.ASEditPart;
import simgrideclipseplugin.graphical.providers.SimgridIconProvider;
import simgrideclipseplugin.model.ElementList;

public class EditASRouting extends SelectionAction {

	public EditASRouting(IWorkbenchPart part) {
		super(part);
		// TODO Auto-generated constructor stub
	}
	public static final String ID = "simgrideclipseplugin.EditASRouting";
	public static final String TEXT = "Edit \"routing\"";
	public static final String TOOL_TIP = "Edit the selected AS routing";

	@Override
	protected void init() {
		setId(ID);
		setText(TEXT);
		setToolTipText(TOOL_TIP);
		setImageDescriptor(SimgridIconProvider.getIconImageDescriptor(ID));
		setSelectionProvider(getWorkbenchPart().getSite().getSelectionProvider());
	}

//	public void run() {
//		EditPart asEP = (EditPart)getSelectedObjects().get(0);
//		
//	}
	
	@Override
	protected boolean calculateEnabled() {
		// we only want enabled if is single ASEditPart selection
	    if(getSelectedObjects().size() !=1
	        || (!(getSelectedObjects().get(0) instanceof ASEditPart))){
	        return false;
	    }
	    return true;
	}

	public List<IAction> getSubActions() {
		Element model = (Element) ((EditPart)getSelectedObjects().get(0)).getModel();
		List<String> valList = ElementList.getValueList(ElementList.AS, "routing");
		List<IAction> actionList = new ArrayList<IAction>(valList.size());
		for (String val : valList){
			actionList.add(new SetAttributeAction(model,"routing",val));
		}
		return actionList;
	}
}

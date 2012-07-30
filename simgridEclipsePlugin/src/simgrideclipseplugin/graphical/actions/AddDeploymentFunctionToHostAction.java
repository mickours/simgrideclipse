package simgrideclipseplugin.graphical.actions;

import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;
import org.w3c.dom.Element;

import simgrideclipseplugin.graphical.parts.HostEditPart;
import simgrideclipseplugin.graphical.providers.SimgridIconProvider;

public class AddDeploymentFunctionToHostAction extends SelectionAction {

	public AddDeploymentFunctionToHostAction(IWorkbenchPart part) {
		super(part);
	}
	public static final String ID = "simgrideclipseplugin.actions.AddDeploymentFunctionToHost";
	public static final String MESSAGE = "Add deployment function";
	public static final String TOOL_TIP = "add a deployment function to this host";

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
		HostEditPart host  = (HostEditPart)getSelectedObjects().get(0);
		doAddDeplymentFunction(host);
	}
	
	public void doAddDeplymentFunction(HostEditPart host){
		String id = ((Element)host.getModel()).getAttribute("id");
		//TODO show a wizard or show deployment file
		
	}

	@Override
	protected boolean calculateEnabled() {
		// we only want enabled if is single HostEditPart selection
	    if(getSelectedObjects().size() !=1
	        || (!(getSelectedObjects().get(0) instanceof HostEditPart))){
	        return false;
	    }
	    return true;
	}
}

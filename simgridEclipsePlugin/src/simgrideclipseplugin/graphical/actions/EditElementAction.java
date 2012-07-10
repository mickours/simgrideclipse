package simgrideclipseplugin.graphical.actions;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;
import org.w3c.dom.Element;

import simgrideclipseplugin.graphical.providers.SimgridIconProvider;
import simgrideclipseplugin.wizards.EditElementWizard;

public class EditElementAction extends SelectionAction {

	public EditElementAction(IWorkbenchPart part) {
		super(part);
		// TODO Auto-generated constructor stub
	}

	public static final String ID = "simgrideclipseplugin.EditASAction";
	public static final String TEXT = "Edit...";
	public static final String TOOL_TIP = "Edit the selected AS";

	@Override
	protected void init() {
		setId(ID);
		setText(TEXT);
		setToolTipText(TOOL_TIP);
		setImageDescriptor(SimgridIconProvider.getIconImageDescriptor(ID));
		setSelectionProvider(getWorkbenchPart().getSite().getSelectionProvider());
	}

	public void run() {
		EditPart asEP = (EditPart)getSelectedObjects().get(0);
		Shell shell = asEP.getViewer().getControl().getShell();
		EditElementWizard wizard = new EditElementWizard((Element)asEP.getModel());
		WizardDialog dialog = new WizardDialog(shell, wizard);
        dialog.create();
    	dialog.open();
	}
	
	@Override
	protected boolean calculateEnabled() {
		// we only want enabled if is single ASEditPart selection
	    if(getSelectedObjects().size() !=1
	        || (!(getSelectedObjects().get(0) instanceof EditPart))){
	        return false;
	    }
	    return true;
	}

}

package simgrideclipseplugin.graphical.commands;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.w3c.dom.Element;

import simgrideclipseplugin.model.ModelHelper;
import simgrideclipseplugin.wizards.EditElementWizard;

public class ConnectionReconnectCommand extends Command {

	private Element route;
	private Element oldSourceNode;
	private Element oldTargetNode;
	private Element newSourceNode;
    private Element newTargetNode;

	public ConnectionReconnectCommand(Element route) {
		if (route == null) {
            throw new IllegalArgumentException();
        }
        this.route = route;
        this.oldSourceNode = ModelHelper.getSourceNode(route);
        this.oldTargetNode = ModelHelper.getTargetNode(route);
	}
   
    private boolean checkSourceReconnection() {
    	if (newSourceNode.equals(oldTargetNode))
            return false;
        else if (!newSourceNode.getClass().equals(oldTargetNode.getClass()))
            return false;
        return true;
    }
   
    private boolean checkTargetReconnection() {
        if (oldSourceNode.equals(newTargetNode))
            return false;
        else if (!oldSourceNode.getClass().equals(newTargetNode.getClass()))
            return false;
        return true;
    }

	public void setNewTargetNode(Element targetNode) {
		if (targetNode == null) {
            throw new IllegalArgumentException();
        }
        this.newSourceNode = null;
        this.newTargetNode = targetNode;
		
	}

	public void setNewSourceNode(Element sourceNode) {
		if (sourceNode == null) {
            throw new IllegalArgumentException();
        }
        this.newSourceNode = sourceNode;
        this.newTargetNode = null;
	}
	
	@Override
	public boolean canExecute() {
        if (newSourceNode != null) {
            return checkSourceReconnection();
        } else if (newTargetNode != null) {
            return checkTargetReconnection();
        }
        return false;
    }
	
	@Override
	public void execute() {
        if (newSourceNode != null) {
            ModelHelper.editRoute(route, newSourceNode, newTargetNode);
        } else if (newTargetNode != null) {
        	ModelHelper.editRoute(route, oldSourceNode, newTargetNode);
        } else {
            throw new IllegalStateException("Should not happen");
        }
		EditElementWizard wizard = new EditElementWizard(route);
		WizardDialog dialog = new WizardDialog(null, wizard);
        dialog.create();
    	if (dialog.open() == Window.CANCEL){
    		ModelHelper.editRoute(route, oldSourceNode, oldTargetNode);
    	}
    }
   
	@Override
    public void undo() {
		
    }

}


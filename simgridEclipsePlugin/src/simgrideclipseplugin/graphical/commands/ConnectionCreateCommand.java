package simgrideclipseplugin.graphical.commands;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.w3c.dom.Element;

import simgrideclipseplugin.model.ModelHelper;
import simgrideclipseplugin.model.SimgridRules;
import simgrideclipseplugin.wizards.CreateElementWizard;

public class ConnectionCreateCommand extends Command {
	private Element sourceNode, targetNode, parent;
	private String connectionType;
    private Element route = null;
	private Shell shell;
   
    @Override
    public boolean canExecute() {
        if (sourceNode == null || targetNode == null 
        		|| sourceNode.equals(targetNode)
        		|| !SimgridRules.isAllowedConnection(sourceNode,connectionType)
        		|| !SimgridRules.isAllowedConnection(targetNode,connectionType)){
            return false;
        }
        return true;
    }
   
    @Override
    public void execute() {
    	boolean isOk;
    	//create the route and set links
		CreateElementWizard wizard = new CreateElementWizard(connectionType);
		wizard.setSourceNode(sourceNode);
		wizard.setTargetNode(targetNode);
		wizard.setFullRouting(parent.getAttribute("routing").equals("Full"));
		WizardDialog dialog = new WizardDialog(shell, wizard);
        dialog.create();
    	dialog.open();
    	isOk = dialog.getReturnCode()== Window.OK;
    	if (isOk){
    		route = wizard.createdElement;
    	}
    }

	@Override
    public boolean canUndo() {
        if (sourceNode == null || targetNode == null || route == null)
            return false;
        return true;         
    }
   
    @Override
    public void undo() {
    	ModelHelper.removeElement(route);
    }

    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    public String getConnectionType() {
        return connectionType;
    }
    
    public void setSourceNode(Element sourceNode) {
        this.sourceNode = sourceNode;
    }
    
    public Element getSourceNode() {
		return sourceNode;
	}
   
    public void setTargetNode(Element targetNode) {
        this.targetNode = targetNode;
    }

	public void setParent(Element parent) {
		this.parent = parent;
		
	}

	public void setShell(Shell shell) {
		this.shell = shell;
	}
}
package simgrideclipseplugin.graphical.commands;

import java.util.List;
import java.util.Map;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.w3c.dom.Element;

import simgrideclipseplugin.graphical.ListSelectionDialog;
import simgrideclipseplugin.model.ElementList;
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
        		|| parent.getAttribute("routing").equals("None")
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
    	
    	//set gateway
    	if (isOk && connectionType.equals(ElementList.AS_ROUTE)){
    		setSourceGateway();
    		setTargetGateway();
    	}
    }
   
    private void setTargetGateway() {
    	setGateway(targetNode,"gw_dst");
		
	}

	private void setSourceGateway() {
		setGateway(sourceNode,"gw_src"); 
		
	}

	private void setGateway(Element node, String attr) {
		String gw = getGateway(node);
		if (gw != null){
			route.setAttribute(attr, gw);
		}
		else{
			List<Element> routerList =  ModelHelper.getRouters(node);
			ElementListSelectionDialog ld = new ListSelectionDialog(shell);
			ld.setElements(routerList.toArray());
    		ld.open();
    		if (ld.getReturnCode() == Window.OK){
    			gw = getGateway((Element)ld.getFirstResult());
    		}
		}
		
	}

	private String getGateway(Element node) {
		String gw = null;
		if (node.getTagName().equals(ElementList.CLUSTER)){
			String prefix = node.getAttribute("prefix");
			String suffix = node.getAttribute("suffix");
			gw = prefix + "router_" + suffix;
		}else if (node.getTagName().equals(ElementList.PEER)){
			//TODO add the peer gateway
		}
		else if (node.getTagName().equals(ElementList.AS)){
			List<Element> routers = ModelHelper.getRouters(node);
			if (routers.size() == 1){
				gw = routers.get(0).getAttribute("id");
			}
		}
		else if (node.getTagName().equals(ElementList.ROUTER)){
			gw = node.getAttribute("id");
		}
		return gw;
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
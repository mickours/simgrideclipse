package simgrideclipseplugin.graphical.commands;

import java.util.HashMap;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.w3c.dom.Element;

import simgrideclipseplugin.graphical.CreateElementFormDialog;
import simgrideclipseplugin.graphical.LinkSelectionDialog;
import simgrideclipseplugin.model.ModelHelper;

public class ConnectionCreateCommand extends Command {
	private Element sourceNode, targetNode;
	private String connectionType;
    private Element route = null;
	private boolean parentFull;
	private Shell shell;
   
    @Override
    public boolean canExecute() {
        if (sourceNode == null || targetNode == null)
            return false;
        else if (sourceNode.equals(targetNode))
            return false;
        return true;
    }
   
    @Override
    public void execute() {
    	if (!parentFull){
    		//the link is unique
    		HashMap<String, String> map = new HashMap<String, String>();
    		map.put("id",null);
    		map.put("bandwidth", null);
	        CreateElementFormDialog dialog = new CreateElementFormDialog(shell,map);
	        dialog.create();
	    	dialog.open();
	    	if (dialog.getReturnCode() == Window.OK){
	    		route = ModelHelper.createRoute(sourceNode, targetNode, connectionType);
	    		String id = map.get("id");
	    		String bw = map.get("bandwidth");
	    		ModelHelper.createAndAddLink(route,id,bw);
	    	}
    	}else{
    		//show a dialog to select existing links
    		ElementListSelectionDialog ld = new LinkSelectionDialog(shell, new LabelProvider());
    		List<Element> links = ModelHelper.getLinks(sourceNode);
    		ld.setElements(links.toArray());
    		ld.open();
    		if (ld.getReturnCode() == Window.OK){
    			route = ModelHelper.createRoute(sourceNode, targetNode, connectionType);
    			for (Object link : ld.getResult()){
    				ModelHelper.addLink(route, (Element) link);
    			}
    		}
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

	public void isParentFull(boolean isFull) {
		parentFull = isFull;
		
	}

	public void setShell(Shell shell) {
		this.shell = shell;
	}
}
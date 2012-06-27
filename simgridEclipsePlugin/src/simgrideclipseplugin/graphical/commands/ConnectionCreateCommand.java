package simgrideclipseplugin.graphical.commands;

import java.util.HashMap;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.w3c.dom.Element;

import simgrideclipseplugin.graphical.CreateElementFormDialog;
import simgrideclipseplugin.graphical.ListSelectionDialog;
import simgrideclipseplugin.model.ElementList;
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
    	boolean isOk;
    	//create the route and set links
    	if (!parentFull){
    		//the link is unique
    		HashMap<String, String> map = new HashMap<String, String>();
    		map.put("id",null);
    		map.put("bandwidth", null);
    		map.put("latency", null);
	        CreateElementFormDialog dialog = new CreateElementFormDialog(shell,map);
	        dialog.create();
	    	dialog.open();
	    	isOk = dialog.getReturnCode()== Window.OK;
	    	if ( isOk){
	    		route = ModelHelper.createRoute(sourceNode, targetNode, connectionType);
	    		String id = map.get("id");
	    		String bw = map.get("bandwidth");
	    		String lat = map.get("latency");
	    		ModelHelper.createAndAddLink(route,id,bw,lat);
	    	}
    	}else{
    		//TODO show a dialog to select and order links
    		ElementListSelectionDialog ld = new ListSelectionDialog(shell);
    		List<Element> links = ModelHelper.getLinks(sourceNode);
    		if (links.isEmpty()){
    			MessageBox msgB = new MessageBox(shell);
    			msgB.setMessage("You must have at least one link available in this AS");
    			msgB.open();
    			isOk = false;
    		}
    		else{
	    		ld.setElements(links.toArray());
	    		ld.open();
	    		isOk = ld.getReturnCode()== Window.OK;
		    	if ( isOk){
	    			route = ModelHelper.createRoute(sourceNode, targetNode, connectionType);
	    			for (Object link : ld.getResult()){
	    				ModelHelper.addLink(route, (Element) link);
	    			}
	    		}
    		}
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

	public void isParentFull(boolean isFull) {
		parentFull = isFull;
		
	}

	public void setShell(Shell shell) {
		this.shell = shell;
	}
}
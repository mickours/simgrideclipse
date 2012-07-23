package simgrideclipseplugin.graphical.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.w3c.dom.Element;

import simgrideclipseplugin.model.ModelHelper;

public class DeleteElementCommand extends Command {
	private Element model;
	private Element parent;
	private List<Element> connections;
	private Map<Element,List<Element>> deletedLinks;
	private Shell shell;
	
	@Override
	public void execute() {
		//check if the model or one of his children is used as gateway
		Map<Element,Element> gwMap = ModelHelper.isUsedOrContainsGateway(model);
		if (!gwMap.isEmpty()){
			//TODO add a "remove anyway" button
			MessageBox mb = new MessageBox(shell,SWT.ICON_WARNING);
			String msg = "";
			for (Entry<Element,Element> entry: gwMap.entrySet()){
				Element route = entry.getKey();
				Element elem = entry.getValue();
				msg += "the element \""+elem.getAttribute("id")+
					"\" is used as a gateway by an ASroute between \""+
					ModelHelper.getSourceNode(route).getAttribute("id")+"\" and \""+
					ModelHelper.getTargetNode(route).getAttribute("id")+"\"\n";
			}
			msg += "\nYou can't remove \""+model.getAttribute("id")+"\", you must remove the route(s) first.";
			mb.setMessage(msg);
			mb.open();
			return;
		}
		connections = ModelHelper.getConnections(model);
		deletedLinks = new HashMap<Element,List<Element>>(connections.size());
		for (Element e : connections){
			deletedLinks.put(e,(ModelHelper.removeRoute(e)));
		}
		ModelHelper.removeElement(model);
	}
	
	@Override
	public boolean canExecute() {
		return (model != null && parent != null);
	}


	@Override
	public boolean canUndo() {
		return !ModelHelper.getChildren(parent).contains(model);
	}



	@Override
	public void undo() {
		ModelHelper.addElementChild(parent, model);
		for (Element route :connections){
			ModelHelper.restoreRoute(parent, route, deletedLinks.get(route));
		}
		
	}

	public void setModel(Element model) {
		this.model = model;
		parent = (Element)model.getParentNode();
	}

	public void setShell(Shell shell) {
		this.shell = shell;
	}
}

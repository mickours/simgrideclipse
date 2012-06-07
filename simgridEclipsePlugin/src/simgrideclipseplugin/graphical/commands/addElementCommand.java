package simgrideclipseplugin.graphical.commands;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;

import simgrideclipseplugin.graphical.ElementPositionMap;

public class addElementCommand extends Command {
	private Element parent;
	private String type;
	private Point position;

	public addElementCommand(Element parent, String type,Point position) {
		this.parent = parent;
		this.type = type;
		this.position = position;
	}

	@Override
	public void execute() {
		Element e = parent.getOwnerDocument().createElement(type);
		//TODO find a clever way to define the id and routing defaults values
//		NodeList ndl = parent.getOwnerDocument().getElementsByTagName(type);
//		for(Node n : ndl){
//			//find pattern
//		}
		e.setAttribute("id", "ASnew");
		e.setAttribute("routing", "Full");
		ElementPositionMap.setPosition(e, position);
		try{
			parent.appendChild(e);
		}catch (DOMException e2) {
			e2.printStackTrace();
		}
		
	}
}

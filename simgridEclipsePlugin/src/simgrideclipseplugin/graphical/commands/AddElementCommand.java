package simgrideclipseplugin.graphical.commands;

import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.w3c.dom.Element;

import simgrideclipseplugin.graphical.ElementPositionMap;
import simgrideclipseplugin.model.ModelHelper;

public class AddElementCommand extends Command {
	private Element parent;
	private String type;
	private Point position;
	private static int numID = 0;

	public AddElementCommand(Element parent, String type,Point position) {
		this.parent = parent;
		this.type = type;
		this.position = position;
	}

	@Override
	public void execute() {
		Element e = parent.getOwnerDocument().createElement(type);
		//TODO find a clever way to define the id default value
		String newId = e.getTagName()+numID++;
		List<Element> ndl = ModelHelper
				.nodeListToElementList(parent.getOwnerDocument().getElementsByTagName(type));
		for(Element n : ndl){
			if (n.getAttribute("id").equals(newId)){
				newId = e.getTagName()+numID++;
			}
		}
		//FIXME get attribute according to the element type
		e.setAttribute("id",newId);
		
		ElementPositionMap.setPosition(e, position);
		ModelHelper.addChild(parent, e);
	}
}

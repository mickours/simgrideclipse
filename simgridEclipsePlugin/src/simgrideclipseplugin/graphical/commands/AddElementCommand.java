package simgrideclipseplugin.graphical.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.w3c.dom.Element;

import simgrideclipseplugin.graphical.ElementPositionMap;
import simgrideclipseplugin.model.ElementList;
import simgrideclipseplugin.model.ModelHelper;
import simgrideclipseplugin.model.SimgridRules;

public class AddElementCommand extends Command {
	private Element newElem;
	private Element parent;
	private String type;
	private Point position;
	private static Map<String,Integer> numIDMap = new HashMap<String,Integer>();

	public AddElementCommand(Element parent, String type,Point position) {
		this.parent = parent;
		this.type = type;
		this.position = position;
	}

	@Override
	public void execute() {
		newElem = parent.getOwnerDocument().createElement(type);
		String newId;
		if (numIDMap.get(type) == null){
			numIDMap.put(type, 0);
		}
		int idNum = numIDMap.get(type);
		newId = type+idNum;
		//increment for next one
		numIDMap.put(type,++idNum);
		
		//verify uniqueness of id for this type
		String saveId;
		List<Element> ndl = ModelHelper
				.nodeListToElementList(parent.getOwnerDocument().getElementsByTagName(type));
		do{
			saveId = newId;
			for(Element n : ndl){
				if (n.getAttribute("id").equals(newId)){
					idNum = numIDMap.get(type);
					newId = type+idNum;
					//increment for next one
					numIDMap.put(type,++idNum);
				}
			}
		}while (!saveId.equals(newId));
		
		//FIXME get attribute according to the element type
		newElem.setAttribute("id",newId);
		if (type.equals(ElementList.HOST)){
			//TODO handle power
		}
		if (type.equals(ElementList.CLUSTER)){
			//TODO handle prefix and suffix
		}
		
		ElementPositionMap.setPosition(newElem, position);
		ModelHelper.addElementChild(parent, newElem);
	}

	@Override
	public boolean canExecute() {
		return SimgridRules.isAllowedElementAdd(parent,type);
	}

	@Override
	public boolean canUndo() {
		return (newElem != null);
	}
	
	@Override
	public void undo() {
		ModelHelper.removeElement(newElem);
		ElementPositionMap.removeElement(newElem);
	}

	@Override
	public void redo() {
		ElementPositionMap.setPosition(newElem, position);
		ModelHelper.addElementChild(parent, newElem);
	}
}
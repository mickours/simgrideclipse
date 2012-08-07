package simgrideclipseplugin.graphical.commands;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.w3c.dom.Element;

import simgrideclipseplugin.graphical.ElementPositionMap;
import simgrideclipseplugin.model.ElementList;
import simgrideclipseplugin.model.ModelHelper;
import simgrideclipseplugin.model.SimgridRules;
import simgrideclipseplugin.wizards.AbstractElementWizard;
import simgrideclipseplugin.wizards.CreateElementWizard;
import simgrideclipseplugin.wizards.RuleBasedASRouteWizard;

public class AddElementCommand extends Command {
	private Element newElem;
	private Element parent;
	private String type;
	private Point position;
	private Shell shell;

	public AddElementCommand(Element parent, String type,Point position, Shell shell) {
		this.parent = parent;
		this.type = type;
		this.position = position;
		this.shell = shell;
	}

	@Override
	public void execute() {
		if (SimgridRules.needEdition(type)){
			//TODO: add here a ruleBased creator.
			AbstractElementWizard wizard ;
//			if (type.equals(ElementList.RULE_BASED_ROUTE)) {
//				wizard = new RuleBasedASRouteWizard(); 
//				//TODO: move regexp related functions from editPart to
//				// ModelHelper in order to get rid of editPart refs. 
//			}
			wizard = new CreateElementWizard(type);
			WizardDialog dialog = new WizardDialog(shell, wizard);
	        dialog.create();
	    	dialog.open();
	    	if (dialog.getReturnCode()== Window.OK){
	    		newElem = wizard.newElement;
	    	}
		}
		else{
			newElem = parent.getOwnerDocument().createElement(type);
			//set default value
			for (String attr:ElementList.getAttributesList(type)){
				if (attr.equals("id")){
					String newId = ModelHelper.createId(type);
					newElem.setAttribute("id",newId);
				}
				else{
					String defVal =  ElementList.getDefaultValue(type, attr);
					if (defVal != null){
						newElem.setAttribute(attr,defVal);
					}
				}
			}
		}
		if (newElem != null){
			ElementPositionMap.setPosition(newElem, position);
			ModelHelper.addElementChild(parent, newElem);
		}
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
		if (newElem != null){
			ElementPositionMap.setPosition(newElem, position);
			ModelHelper.addElementChild(parent, newElem);
		}
	}
}
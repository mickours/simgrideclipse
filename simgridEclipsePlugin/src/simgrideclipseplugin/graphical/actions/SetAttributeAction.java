package simgrideclipseplugin.graphical.actions;

import org.eclipse.jface.action.Action;
import org.w3c.dom.Element;

import simgrideclipseplugin.model.ModelHelper;

public class SetAttributeAction extends Action {
	
	private String attrValue;
	private String attrName;
	private Element toEditElement;
	
	public SetAttributeAction(Element toEditElement,String attrName,String attrValue) {
		this.attrName = attrName;
		this.attrValue = attrValue;
		this.toEditElement = toEditElement;
		this.setText(attrValue);
		if (toEditElement.getAttribute(attrName).equals(attrValue)){
			setEnabled(false);
		}
	}

	@Override
	public void run() {
		ModelHelper.editAttribute(toEditElement, attrName, attrValue);
	}
}

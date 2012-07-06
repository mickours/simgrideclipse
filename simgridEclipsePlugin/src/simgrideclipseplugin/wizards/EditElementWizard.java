package simgrideclipseplugin.wizards;

import org.w3c.dom.Element;

import simgrideclipseplugin.model.ElementList;
import simgrideclipseplugin.model.ModelHelper;
import simgrideclipseplugin.model.SimgridRules;


public class EditElementWizard extends CreateElementWizard {

	private Element toEditElement;

	public EditElementWizard(Element toEditElement) {
		super(toEditElement.getTagName());
		this.toEditElement = toEditElement;
		for (String attr : ElementList.getAttributesList(tagName)){
			if (toEditElement.hasAttribute(attr)){
				attrMap.put(attr, toEditElement.getAttribute(attr));
			}
		}
		if (ElementList.isConnection(tagName)){
			sourceNode = ModelHelper.getSourceNode(toEditElement);
			targetNode = ModelHelper.getTargetNode(toEditElement);
			multiLink = true;
			//multiLink = SimgridRules.((Element) toEditElement.getParentNode()).getAttribute("r")
		}
	}

	@Override
	public boolean performFinish() {
		if (ElementList.isConnection(tagName)){
			//assert  sourceNode != null && targetNode != null
			ModelHelper.editRoute(toEditElement, sourceNode, targetNode);
			//create links
    		ModelHelper.editRouteLinks(toEditElement, linkList);
			//create gateway 
			if (SimgridRules.isASLikeConnection(tagName)){
				ModelHelper.editRouteGateways(toEditElement, selectedSrcGw, selectedDstGw);
			}
		}else{
			ModelHelper.editElementAttributes(toEditElement, attrMap);
		}
		return true;
	}

}

package simgrideclipseplugin.wizards;

import org.w3c.dom.Element;

import simgrideclipseplugin.model.ElementList;
import simgrideclipseplugin.model.ModelHelper;
import simgrideclipseplugin.model.SimgridRules;


public class EditElementWizard extends CreateElementWizard {

	public Element toEditElement;

	public EditElementWizard(Element toEditElement) {
		super(toEditElement.getTagName());
		this.toEditElement = toEditElement;
		ModelHelper.setAttributeMap(toEditElement, attrMap);
		if (ElementList.isConnection(tagName)){
			route = toEditElement;
			sourceNode = ModelHelper.getSourceNode(toEditElement);
			targetNode = ModelHelper.getTargetNode(toEditElement);
			multiLink = true;
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

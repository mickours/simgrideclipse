package simgrideclipseplugin.wizards;

import org.w3c.dom.Element;

import simgrideclipseplugin.model.ModelHelper;
import simgrideclipseplugin.model.SimgridRules;


public class EditElementWizard extends AbstractElementWizard {

	public EditElementWizard(Element toEditElement) {
		super(toEditElement.getTagName());
		this.newElement = toEditElement;
		ModelHelper.setAttributeMap(toEditElement, attrMap);
		if (SimgridRules.isConnection(tagName)){
			route = toEditElement;
			sourceNode = ModelHelper.getSourceNode(toEditElement);
			targetNode = ModelHelper.getTargetNode(toEditElement);
			multiLink = true;
		}
	}

	@Override
	public boolean performFinish() {
		if (SimgridRules.isConnection(tagName)){
			//assert  sourceNode != null && targetNode != null
			ModelHelper.editRoute(newElement, sourceNode, targetNode);
			//create links
    		ModelHelper.editRouteLinks(newElement, linkCtnList, linkCtnDirectionList);
			//create gateway 
			if (SimgridRules.isASLikeConnection(tagName)){
				ModelHelper.editRouteGateways(newElement, selectedSrcGw, selectedDstGw);
			}
		}else{
			ModelHelper.editElementAttributes(newElement, attrMap);
		}
		return true;
	}

}

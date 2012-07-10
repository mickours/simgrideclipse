package simgrideclipseplugin.wizards;

import org.w3c.dom.Element;

import simgrideclipseplugin.model.ElementList;
import simgrideclipseplugin.model.ModelHelper;
import simgrideclipseplugin.model.SimgridRules;

public class CreateElementWizard extends AbstractElementWizard {

	public CreateElementWizard(String tagName) {
		super(tagName);
	}

	@Override
	public boolean performFinish() {
		if (ElementList.isConnection(tagName)){
			assert  sourceNode != null && targetNode != null;
			newElement = ModelHelper.createAndAddRoute(sourceNode, targetNode, tagName);
			//create links
			for (Object link : linkList){
				ModelHelper.addLinkToRoute(newElement, (Element) link);
			}
			//create gateway 
			if (SimgridRules.isASLikeConnection(tagName)){
				ModelHelper.editRouteGateways(newElement,selectedSrcGw, selectedDstGw);
			}
		}else{
			newElement = ModelHelper.createElement(tagName, attrMap);
		}
		return true;
	}
}

package simgrideclipseplugin.wizards;

import simgrideclipseplugin.model.ElementList;
import simgrideclipseplugin.model.ModelHelper;
import simgrideclipseplugin.model.SimgridRules;

public class CreateElementWizard extends AbstractElementWizard {

	public CreateElementWizard(String tagName) {
		super(tagName);
	}

	@Override
	public boolean performFinish() {
		if (SimgridRules.isConnection(tagName)){
			assert  sourceNode != null && targetNode != null;
			newElement = ModelHelper.createAndAddRoute(sourceNode, targetNode, tagName);
			//create links
			for (int i=0; i< linkCtnList.size();i++){
				ModelHelper.addLinkToRoute(newElement, linkCtnList.get(i), linkCtnDirectionList.get(i));
			}
			//create gateway 
			if (SimgridRules.isASLikeConnection(tagName)){
				ModelHelper.editRouteGateways(newElement,selectedSrcGw, selectedDstGw);
			}
		}else if(tagName.equals(ElementList.CLUSTER) && clusterContent != null){
			newElement = ModelHelper.createCluster(clusterContent);
		}else{
			newElement = ModelHelper.createElement(tagName, attrMap);
		}
		return true;
	}
}

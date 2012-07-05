package simgrideclipseplugin.wizards;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.wizard.Wizard;
import org.w3c.dom.Element;

import simgrideclipseplugin.model.ElementList;
import simgrideclipseplugin.model.ModelHelper;
import simgrideclipseplugin.model.SimgridRules;

public class CreateElementWizard extends Wizard {

	private String tagName;
	private AttributeFieldFormPage fieldPage;
	private LinkSelectionPage linkPage;
	private GatewaySelectionPage gwPage;
	
	public Map<String, String> attrMap;
	public Element createdElement;
	public List<Element> linkList;
	public Element selectedSrcGw, selectedDstGw;
	
	private Element sourceNode;
	private Element targetNode;
	private boolean fullRouting;
	private Element parent;
	
	public CreateElementWizard(String tagName) {
		super();
		this.tagName = tagName;
		this.attrMap = new HashMap<String, String>(); 
	}

	@Override
	public void addPages() {
		
		if (ElementList.isConnection(tagName)){
			//set links
			if (fullRouting){
				linkPage = new LinkSelectionPage(ModelHelper.getLinks(sourceNode));
				addPage(linkPage);
			}
			else{
				fieldPage = new AttributeFieldFormPage(ElementList.LINK);
				addPage(fieldPage);
			}
			//set gateway 
			if (SimgridRules.isASLikeConnection(tagName)){
				List<Element> srcRouter = ModelHelper.getRouters(sourceNode);
				List<Element> dstRouter = ModelHelper.getRouters(targetNode);
				gwPage = new GatewaySelectionPage(srcRouter,dstRouter);
				addPage(gwPage);
			}
		}
		else{
			fieldPage = new AttributeFieldFormPage(tagName);
			addPage(fieldPage);
		}
	}
	

	@Override
	public boolean performFinish() {
		if (ElementList.isConnection(tagName)){
			//assert  sourceNode != null && targetNode != null
			createdElement = ModelHelper.createRoute(sourceNode, targetNode, tagName);
			//create links
			if (fullRouting){
				for (Object link : linkList){
    				ModelHelper.addLink(createdElement, (Element) link);
    			}
			}
			else{
				ModelHelper.createAndAddLink(createdElement,attrMap);
			}
			//create gateway 
			if (SimgridRules.isASLikeConnection(tagName)){
				String id = ModelHelper.getGatewayId(selectedSrcGw);
				createdElement.setAttribute("gw_src", id);
				id = ModelHelper.getGatewayId(selectedDstGw);
				createdElement.setAttribute("gw_dst", id);
			}
		}else{
			createdElement = ModelHelper.createElement(tagName, attrMap);
		}
		return true;
	}
	
	public void setSourceNode(Element sourceNode) {
		this.sourceNode = sourceNode;
	}

	public void setTargetNode(Element targetNode) {
		this.targetNode = targetNode;
	}

	public void setFullRouting(boolean fullRouting) {
		this.fullRouting= fullRouting; 
	}
	
	public void setParent(Element parent) {
		this.parent = parent;
	}
	
}

package simgrideclipseplugin.wizards;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.wizard.Wizard;
import org.w3c.dom.Element;

import simgrideclipseplugin.model.ElementList;
import simgrideclipseplugin.model.ModelHelper;

public class CreateElementWizard extends Wizard {

	private String tagName;
	private AttributeFieldFormPage fieldPage;
	private LinkSelectionPage linkPage;
	private GatewaySelectionPage gwPage;
	
	public Map<String, String> attrMap;
	public Element createdElement;
	public List<Element> linkList;
	
	private Element sourceNode;
	private Element targetNode;
	private boolean fullRouting;
	
	
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
//			if (tagName.equals(ElementList.AS_ROUTE)){
//				gwPage = new GatewaySelectionPage();
//				addPage(gwPage);
//			}
		}
		else{
			fieldPage = new AttributeFieldFormPage(tagName);
			addPage(fieldPage);
		}
	}
	

	@Override
	public boolean performFinish() {
		//assert  sourceNode != null && targetNode != null
		if (ElementList.isConnection(tagName)){
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
			if (tagName.equals(ElementList.AS_ROUTE)){
				gwPage = new GatewaySelectionPage();
				addPage(gwPage);
			}
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
	
	
}

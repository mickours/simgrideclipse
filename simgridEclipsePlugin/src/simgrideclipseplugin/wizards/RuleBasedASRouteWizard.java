package simgrideclipseplugin.wizards;

import java.util.List;

import org.w3c.dom.Element;

import simgrideclipseplugin.graphical.parts.RuleBasedASRouteEditPart;
import simgrideclipseplugin.model.ModelHelper;
import simgrideclipseplugin.model.SimgridRules;

public class RuleBasedASRouteWizard extends AbstractElementWizard {	
	private RuleBasedASRouteEditPart editPart;
	
//	public RuleBasedASRouteWizard(RuleBasedASRouteEditPart editPart, String tagName) {
//		super(tagName);		
//		this.setEditPart(editPart);
//	}

	public RuleBasedASRouteWizard(Element toEditElement, RuleBasedASRouteEditPart editPart) {
		super(toEditElement.getTagName());
		this.setEditPart(editPart);
		this.newElement = toEditElement;
		ModelHelper.setAttributeMap(toEditElement, attrMap);
		if (SimgridRules.isConnection(tagName)){
			route = toEditElement;
//			sourceNode = ModelHelper.getSourceNode(toEditElement);
//			targetNode = ModelHelper.getTargetNode(toEditElement);
			multiLink = true;
		}
	}
	@Override
	public void addPages() {			
		// default field page (must modify it to ensure linkkList is not corrupted.
		fieldPage = new RuleBasedASRouteAttributesWizardPage(getEditPart());
		addPage(fieldPage);
		String src = attrMap.get("src");
		String dst = attrMap.get("dst");
		String gw_src = attrMap.get("gw_src");
		String gw_dst = attrMap.get("gw_dst");
		linkPage = new RuleBasedLinkSelectionPage(newElement, getEditPart(), 
				src, dst, gw_src, gw_dst);
		addPage(linkPage);
//			fieldPage = new AttributeFieldFormPage(tagName);			
//			linkCtnList = ModelHelper.getLinks(newElement);			
//			addPage(fieldPage);
//			LinkSelectionPage linkSelectionPage  = new LinkSelectionPage(linkCtnList, newElement, multiLink);  
//			addPage(linkSelectionPage);
	}

	@Override
	public boolean performFinish() {
		
    		ModelHelper.editRouteLinks(newElement, linkCtnList);			
			ModelHelper.editElementAttributes(newElement, attrMap);				
		editPart.updateConnections();
		return true;
	}
	

	public RuleBasedASRouteEditPart getEditPart() {		
		return editPart;
	}

	public void setEditPart(RuleBasedASRouteEditPart editPart) {
		this.editPart = editPart;
	}


}

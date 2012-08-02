package simgrideclipseplugin.wizards;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.wizard.Wizard;
import org.w3c.dom.Element;

import simgrideclipseplugin.model.ModelHelper;
import simgrideclipseplugin.model.SimgridRules;

public abstract class AbstractElementWizard extends Wizard {
	protected String tagName;
	
//	protected AttributeFieldFormPage fieldPage;
//	protected LinkSelectionPage linkPage;
//	protected GatewaySelectionPage gwPage;
	
	public Map<String, String> attrMap;
	public Element newElement;
	
	public List<Element> linkCtnList;
	public List<String> linkCtnDirectionList;
	public Element selectedSrcGw, selectedDstGw;
	
	public List<Element>clusterContent;
	public Element backbone;
	public String clusterId;

	
	protected Element sourceNode;
	protected Element targetNode;
	protected Element route;

	protected boolean multiLink;
	
	public AbstractElementWizard(String tagName) {
		super();
		this.tagName = tagName;
		this.attrMap = new HashMap<String, String>(); 
	}

	@Override
	public void addPages() {
		
		if (SimgridRules.isConnection(tagName)){
			//set gateway 
			if (SimgridRules.isASLikeConnection(tagName)){
				List<Element> srcRouter = ModelHelper.getRouters(sourceNode);
				List<Element> dstRouter = ModelHelper.getRouters(targetNode);
				GatewaySelectionPage gwPage = new GatewaySelectionPage(srcRouter,dstRouter);
				addPage(gwPage);
			}
			//set links
			Element refNode = (route != null) ?route : sourceNode;
			LinkSelectionPage linkPage = new LinkSelectionPage(ModelHelper.getLinks(sourceNode), refNode, multiLink);
			addPage(linkPage);
		}
		else{
			AttributeFieldFormPage fieldPage = new AttributeFieldFormPage(tagName);
			addPage(fieldPage);
		}
	}
	
	
	public void setSourceNode(Element sourceNode) {
		this.sourceNode = sourceNode;
	}

	public void setTargetNode(Element targetNode) {
		this.targetNode = targetNode;
	}

	public void setMultiLink(boolean multiLink) {
		this.multiLink= multiLink; 
	}
}

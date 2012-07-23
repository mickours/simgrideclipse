package simgrideclipseplugin.model;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import simgrideclipseplugin.wizards.CreateElementWizard;

/**
 * define the internal rules of Simgrid
 * @author mmercier
 *
 */
public final class SimgridRules {

	/**
	 * return true if this type of route is allowed to be connected to
	 * this element
	 * @param element
	 * @param connType the tag name of the connection 
	 * @see ElementList ElementList to find the connection type
	 */
	public static boolean isAllowedConnection(Element element, String connType) {
		if (parentDontAcceptRoute(element)){
			return false;
		}
		String node = element.getTagName();
		if ( isASLikeConnection(connType) && isASLike(node)){
			return true;
		}
		if ( isHostLikeConnection(connType) && isHostLike(node)){
			return true;
		}
		return false;
	}
	
	/**
	 * return true if the element of tag name </code>childType</code> can be
	 * added to this parent element
	 * @param childType the tag name of the child @see "ElementList"
	 * @return
	 */
	public static boolean isAllowedElementAdd(Element parent, String childType) {
		if (parent.getTagName().equals(ElementList.PLATFORM)){
			if (!ModelHelper.getChildren(parent).isEmpty() || !childType.equals(ElementList.AS)){
				return false;
			}
		}
		else if (isASLike(childType)){
			NodeList nl = parent.getChildNodes();
			for (int i=0; i< nl.getLength(); i++){
				Object n = nl.item(i);
				if (n instanceof Element && isHostLike(((Element)n).getTagName())){
					return false;
				}
			}
		}
		else if (isHostLike(childType)){
			NodeList nl = parent.getChildNodes();
			for (int i=0; i< nl.getLength(); i++){
				Object n = nl.item(i);
				if (n instanceof Element && isASLike(((Element)n).getTagName())){
					return false;
				}
			}
		}
		return true;
	}
	
	public static boolean isASLike(String tag){
		return (tag.equals(ElementList.AS) ||
				tag.equals(ElementList.CLUSTER) ||
				tag.equals(ElementList.PEER));
	}
	
	public static boolean isHostLike(String tag){
		return (tag.equals(ElementList.HOST) ||
				tag.equals(ElementList.ROUTER));
	}
	
	public static boolean isASLikeConnection(String tag){
		return (tag.equals(ElementList.AS_ROUTE) ||
				tag.equals(ElementList.BYPASS_AS_ROUTE));
	}
	
	public static boolean isHostLikeConnection(String tag){
		return (tag.equals(ElementList.ROUTE) ||
				tag.equals(ElementList.BYPASS_ROUTE));
	}
	
	/**
	 * return true if the tag named an Element that needs a wizard editor to be created 
	 * @see CreateElementWizard the creation wizard
	 * @see ElementList ElementList: contains the list of tag constant
	 */
	public static boolean needEdition(String tag) {
		return (tag.equals(ElementList.HOST) ||
				tag.equals(ElementList.CLUSTER) ||
				tag.equals(ElementList.PEER));
	}
	
	public static boolean parentDontAcceptRoute(Node element){
		Element parent = (Element) element.getParentNode();
		String routing = parent.getAttribute("routing");
		return isNotAllowingRoute(routing);
	}

	public static boolean isNotAllowingRoute(String routing){
		if (routing.equals("None")
	    		||routing.equals("Vivaldi")
	    		||routing.equals("Cluster")
	    		||routing.equals("RuleBased")){
			return true;
		}
		return false;
	}
	
	public static boolean canBeAGateway(String tagName){
		if (tagName.equals(ElementList.ROUTER)
	    		||tagName.equals(ElementList.CLUSTER)
	    		||tagName.equals(ElementList.PEER)){
			return true;
		}
		return false;
	}
}

package simgrideclipseplugin.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	
	private static List<String> connectionList = createConnectionList();
	private static List<String> notDrawableList = createNotDrawableList();
	
	/**
	 * these function create the list of the connection elements.
	 * 
	 * this list must be updated if a new connection elements is created.
	 */
	private static List<String> createConnectionList() {
		String[] tags = {
				ElementList.ROUTE,ElementList.BYPASS_ROUTE,
				ElementList.AS_ROUTE,ElementList.BYPASS_AS_ROUTE,
		};
		return Arrays.asList(tags);
	}
	
	/**
	 * these function create the list of the not drawable elements
	 * the not drawable elements are the elements that can be present in 
	 * the outline view or in any list using the SimgridIconProvder Class
	 * but that are not in the palette.
	 * 
	 * this list must be updated if a new not drawable element is created.
	 * @see simgrideclipseplugin.graphical.providers.SimgridIconProvder SimgridIconProvder
	 */
	private static List<String> createNotDrawableList() {
		String[] tags = {
				ElementList.LINK,ElementList.LINK_CTN,ElementList.BACKBONE,
		};
		return Arrays.asList(tags);
	}
	
	/**
	 * return true if the element that has this tagName is a graphical connection.
	 */
	public static boolean isConnection(String tagName){
		return connectionList.contains(tagName);
	}
	
	/**
	 * return the graphical connection list.
	 */
	public static List<String> getConnectionList(){
		return new ArrayList<String>(connectionList);
	}
	
	/**
	 * return true if the element that has this tagName is a drawable elements
	 * the not drawable elements are the elements that can be present in 
	 * the outline view or in any list using the SimgridIconProvder Class
	 * but that are not in the palette.
	 * @param tagName the tag name from the {@link ElementList}
	 */
	public static boolean isDrawable(String tagName){
		return !notDrawableList.contains(tagName);
	}
	
	/**
	 * return true if the element that has this tagName is a bypass.
	 * @param tag the tag name from the {@link ElementList}
	 */
	public static boolean isBypass(String tagName){
		return tagName.equals(ElementList.BYPASS_ROUTE) 
				|| tagName.equals(ElementList.BYPASS_AS_ROUTE);
	}
	

	
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
	 * @param childType the tag name of the child from the {@link ElementList}
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
		else if (childType.equals(ElementList.PEER) && !(parent.getAttribute("routing").equals("Vivaldi"))){
			return false;
		}
		return true;
	}
	
	/**
	 * return true if the element that have this <code>tag</code> name is like an AS:
	 * 	that means that it can be connected with an ASLikeConnection and can be 
	 * 	present in the same container with other AS like elements
	 * @param tag the tag name from the {@link ElementList}
	 * @see SimgridRules#isASLikeConnection(String)
	 */
	public static boolean isASLike(String tag){
		return (tag.equals(ElementList.AS) ||
				tag.equals(ElementList.CLUSTER));
	}
	
	/**
	 * return true if the element that have this <code>tag</code> name is like an host:
	 * 	that means that it can be connected with a normal route and can be 
	 * 	present in the same container with other host like elements
	 * @param tag the tag name from the {@link ElementList}
	 */
	public static boolean isHostLike(String tag){
		return (tag.equals(ElementList.HOST) ||
				tag.equals(ElementList.ROUTER));
	}
	
	/**
	 * return true if the element that have this <code>tag</code> name is like an host:
	 * 	that means that it can be connected with a normal route and can be 
	 * 	present in the same container with other host like elements
 	 * @param tag the tag name from the {@link ElementList}
	 */
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
	
	/**
	 * return true if the parent of this <code>element</code> is NOT allowing
	 * routes regarding to his routing attribute.
	 */
	public static boolean parentDontAcceptRoute(Node element){
		Element parent = (Element) element.getParentNode();
		String routing = parent.getAttribute("routing");
		return isNotAllowingRoute(routing);
	}

	/**
	 * return true if this <code>routing</code> type is NOT allowing routes.
	 */
	public static boolean isNotAllowingRoute(String routing){
		if (routing.equals("None")
	    		||routing.equals("Vivaldi")
	    		||routing.equals("Cluster")
	    		||routing.equals("RuleBased")){
			return true;
		}
		return false;
	}
	
	/**
	 * return true if the element that has this <code>tagName</code>
	 * can be a gateway for an ASLikeConnection 
	 * @param tagName the tag name from the {@link ElementList}
	 * @see SimgridRules#isASLikeConnection(String)
	 */
	public static boolean canBeAGateway(String tagName){
		if (tagName.equals(ElementList.ROUTER)
	    		||tagName.equals(ElementList.CLUSTER)){
			return true;
		}
		return false;
	}
}

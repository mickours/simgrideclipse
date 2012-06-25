package simgrideclipseplugin.model;

import org.w3c.dom.Element;

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
	 * @param type
	 * @return
	 */
	public static boolean isAllowedConnection(Element element, String type) {
		String node = element.getTagName();
		if (type.equals(ElementList.AS_ROUTE) && isASLike(node)){
			return true;
		}
		if (type.equals(ElementList.ROUTE) && isHostLike(node)){
			return true;
		}
		return false;
	}
	
	private static boolean isASLike(String node){
		return (node.equals(ElementList.AS) ||
				node.equals(ElementList.CLUSTER) ||
				node.equals(ElementList.PEER));
	}
	
	private static boolean isHostLike(String node){
		return (node.equals(ElementList.HOST) ||
				node.equals(ElementList.ROUTER));
	}
}

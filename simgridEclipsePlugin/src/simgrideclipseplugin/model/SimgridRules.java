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
	
	public static boolean isASLike(String tag){
		return (tag.equals(ElementList.AS) ||
				tag.equals(ElementList.CLUSTER) ||
				tag.equals(ElementList.PEER));
	}
	
	public static boolean isHostLike(String tag){
		return (tag.equals(ElementList.HOST) ||
				tag.equals(ElementList.ROUTER));
	}
}

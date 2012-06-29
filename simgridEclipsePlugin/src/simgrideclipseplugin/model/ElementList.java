package simgrideclipseplugin.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ElementList {
	private static List<String> tagNameList = createTagList();
	private static List<String> connectionList = createConnectionList();
	private static List<String> notDrawableList = createNotDrawableList();
//	private static Map<String,List<String>> attributMap = createAttributMap();
	public static final String PLATFORM = "platform";
	public static final String AS = "AS";
	public static final String AS_ROUTE = "ASroute";
	public static final String HOST = "host";
	public static final String CLUSTER = "cluster";
	public static final String ROUTE = "route";
	public static final String PEER = "peer";
	public static final String ROUTER = "router";
	public static final String LINK = "link";
	public static final String LINK_CTN = "link_ctn";
	
	public static List<String> getElementTagNameList() {
		return new ArrayList<String>(tagNameList);
	}
	
	public static boolean isConnection(String tagName){
		return connectionList.contains(tagName);
	}
	
	public static boolean isDrawable(String tagName){
		return !notDrawableList.contains(tagName);
	}
	
//	public static void setAttributesList(String tagName, List<String> attributes){
//		attributMap.put(tagName, attributes);
//	}
//	
//	public static List<String> getAttributesList(String tagName){
//		return attributMap.get(tagName);
//	}
	
	/** PRIVATE **/
	
//	private static Map<String, List<String>> createAttributMap() {
//		//FIXME: this list should come from the dtd
//		Map<String,List<String>> map = new HashMap<String,List<String>>();
////		map.put("AS", Arrays.asList("id","routing"));
////		map.put("ASroute", Arrays.asList("src","dst","gw_src","gw_dst","symmetrical"));
//		return map;
//	}

	

	private static List<String> createConnectionList() {
		String[] tags = {
				ROUTE,
				AS_ROUTE
		};
		return Arrays.asList(tags);
	}
	
	private static List<String> createNotDrawableList() {
		String[] tags = {
				LINK,LINK_CTN,
		};
		return Arrays.asList(tags);
	}

	private static List<String> createTagList() {
		//FIXME: this list should come from the dtd
		String[] tags = {
				AS,CLUSTER,PEER,
				AS_ROUTE,
				HOST,ROUTER,
				ROUTE,LINK,LINK_CTN,
				
				//TODO to complete
		};
		return Arrays.asList(tags);
	}
	
	
}

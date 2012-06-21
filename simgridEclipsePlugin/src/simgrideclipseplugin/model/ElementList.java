package simgrideclipseplugin.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;

public class ElementList {
	private static List<String> tagNameList = createTagList();
	private static List<String> connectionList = createConnectionList();
//	private static Map<String,List<String>> attributMap = createAttributMap();
	
	public static List<String> getElementTagNameList() {
		return new ArrayList<String>(tagNameList);
	}
	
	public static boolean isConnection(String tagName){
		return connectionList.contains(tagName);
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
				"Route",
				"ASroute"
		};
		return Arrays.asList(tags);
	}

	private static List<String> createTagList() {
		//FIXME: this list should come from the dtd
		String[] tags = {
				"AS",
				"host",
				"ASroute"
				//TODO to complete
		};
		return Arrays.asList(tags);
	}
	
	
}

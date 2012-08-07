package simgrideclipseplugin.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contains the Element static constant names,
 * and some access methods for the Elements and their Attributes
 * @author Michael Mercier 
 *
 */
public class ElementList {
	private static List<String> tagNameList = createTagList();
	private static Map<String,String> defaultValueMap = new HashMap<String, String>();
	
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
	public static final String BYPASS_AS_ROUTE = "bypassASroute";
	public static final String BYPASS_ROUTE = "bypassRoute";
	public static final String CABINET = "cabinet";
	public static final String BACKBONE = "backbone";

	// handling rule-based routes 
	public static final String RULE_BASED_ROUTE = "regexpRoute";
	public static final String NON_EDITABLE_AS_ROUTE = "(regexp)";	
	
	
	public static List<String> getElementTagNameList() {
		return new ArrayList<String>(tagNameList);
	}
	
	public static List<String> getAttributesList(String tagName){
		return DtdParser.INSTANCE.getAttributesList(tagName);
	}
	
	public static boolean isRequiredField(String tagName, String fieldName){
		return DtdParser.INSTANCE.isRequieredField(tagName, fieldName);
	}
	
	public static List<String> getValueList(String tagName, String fieldName){
		return DtdParser.INSTANCE.getValueList(tagName, fieldName);
	}
	
	
	public static String getDefaultValue(String tagName, String fieldName) {
		String val = defaultValueMap.get(tagName+"."+fieldName);
		if (val == null){
			val = DtdParser.INSTANCE.getDefaultValue(tagName,fieldName);
		}
		if (val == null && fieldName.equals("id")){
			val = ModelHelper.createId(tagName);
		}
		return val;
	}
	
	public static void setDefaultValue(String tagName, String fieldName, String value) {
		defaultValueMap.put(tagName+"."+fieldName,value);
	}
	
	/* PRIVATE */

	/**
	 * This function create a list of all the tags that the plug-in understand.
	 * If you want to add new elements you must add it to this list. 
	 */
	private static List<String> createTagList() {
		String[] tags = {
				AS,CLUSTER,PEER,
				AS_ROUTE,BYPASS_AS_ROUTE,
				HOST,ROUTER,
				ROUTE,BYPASS_ROUTE,
				LINK,LINK_CTN,				
				RULE_BASED_ROUTE,
				CABINET,
				BACKBONE
				//TODO to complete with the new elements
		};
		return Arrays.asList(tags);
	}
}

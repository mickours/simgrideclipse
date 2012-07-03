package simgrideclipseplugin.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.XMLReader;

import com.wutka.dtd.DTD;
import com.wutka.dtd.DTDAttribute;


public class ElementList {
	private static List<String> tagNameList = createTagList();
	private static List<String> connectionList = createConnectionList();
	private static List<String> notDrawableList = createNotDrawableList();
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
	
	public static List<String> getElementTagNameList() {
		return new ArrayList<String>(tagNameList);
	}
	
	public static boolean isConnection(String tagName){
		return connectionList.contains(tagName);
	}
	
	public static boolean isDrawable(String tagName){
		return !notDrawableList.contains(tagName);
	}
	
	public static List<String> getAttributesList(String tagName){
		return DtdParser.INSTANCE.getAttributesList(tagName);
	}
	
	public static boolean isRequieredField(String tagName, String fieldName){
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
		return val;
	}
	
	public static void setDefaultValue(String tagName, String fieldName, String value) {
		defaultValueMap.put(tagName+"."+fieldName,value);
	}
	
	/** PRIVATE **/

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

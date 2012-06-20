package simgrideclipseplugin.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ElementList {
	private static List<String> tagNameList;
	private static List<String> connectionList;
	
	public static List<String> getElementTagNameList() {
		if  (tagNameList == null){
			createTagList();
		}
		return new ArrayList<String>(tagNameList);
	}
	
	public static boolean isConnection(String tagName){
		if (connectionList == null){
			createConnectionList();
		}
		return connectionList.contains(tagName);
	}

	private static void createConnectionList() {
		String[] tags = {
				"Route",
				"ASroute"
		};
		connectionList = Arrays.asList(tags);
	}

	private static void createTagList() {
		//FIXME: this list should come from the dtd
		String[] tags = {
				"AS",
				"Host",
				"ASroute"
				//TODO to complete
		};
		tagNameList = Arrays.asList(tags);
	}
	
	
}

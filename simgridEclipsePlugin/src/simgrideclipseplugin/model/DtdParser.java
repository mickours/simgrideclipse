package simgrideclipseplugin.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.xerces.parsers.SAXParser;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.DeclHandler;

public class DtdParser {

	
	
	public static final DtdParser INSTANCE = new DtdParser();
	
	private static final String dtdURL = "http://simgrid.gforge.inria.fr/simgrid.dtd";
	private SAXParser parser;
	private Map<String, Map<String,Boolean>> elementMap;
	
	private DtdParser(){
		try {
			parser = new SAXParser();
			DeclHandler handler = new MyDeclHandler();
			parser.setProperty("http://xml.org/sax/properties/declaration-handler",handler);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Map<String,Map<String,Boolean>> parse(){
		elementMap = new HashMap<String, Map<String,Boolean>>();
		try {
			parser.parse(dtdURL);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return elementMap;
	}
	
	private class MyDeclHandler implements DeclHandler {

		@Override
		public void attributeDecl(String eName, String aName, String type,
				String mode, String value) throws SAXException {
			Map<String,Boolean> attrMap = elementMap.get(eName);
			if (attrMap == null){
				attrMap = new HashMap<String, Boolean>();
				elementMap.put(eName,attrMap);
			}
			attrMap.put(aName, mode.equals("#REQUIRED"));
		}

		@Override
		public void elementDecl(String name, String model) throws SAXException {
			elementMap.put(name, null);
		}

		@Override
		public void externalEntityDecl(String name, String publicId,
				String systemId) throws SAXException {
			// TODO Auto-generated method stub

		}

		@Override
		public void internalEntityDecl(String name, String value)
				throws SAXException {
			// TODO Auto-generated method stub

		}

	}
	
		
}

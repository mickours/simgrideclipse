package simgrideclipseplugin.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.eclipse.wst.xml.core.internal.provisional.format.FormatProcessorXML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

//import simgrideclipseplugin.editors.properties.ElementPropertySource;

/*
 * Help to access WST internal model and hide it to avoid restriction warnings
 */
@SuppressWarnings("restriction")
public final class ModelHelper {
	
	private final static FormatProcessorXML formatProcessor = new FormatProcessorXML();
	
/*******************************************/	
/*****   CREATION/DELETION FUNCTIONS  ******/
/*******************************************/
	public static void addChild(Node parent, Element child){
		try{
			//FIXME must add the child after the link and before the route
			parent.appendChild(child);
			//format sources
			formatProcessor.formatNode(parent);
		}catch (Exception e2) {
			e2.printStackTrace();
		}
	}
	
	public static Element createRoute(Element sourceNode, Element targetNode,
			String routeType) {
		//create a route
		Element route = sourceNode.getOwnerDocument().createElement(routeType);
		route.setAttribute("src",getId(sourceNode));
		route.setAttribute("dst",getId(targetNode));
		//include the route in sourceNode location
		insertAtLast(sourceNode.getParentNode(),route);
		return route;
	}
	
	public static void createAndAddLink(Element route,String id, String bandwidth){
		Document doc = route.getOwnerDocument();
		//create the link
		Element link = doc.createElement(ElementList.LINK);
		link.setAttribute("id", id);
		link.setAttribute("bandwidth", bandwidth);
		insertAtFirst(route.getParentNode(),link);
		//add it to the route
		addLink(route,link);
	}
	
	/**
	 * add the link to the route by creating a link_ctn node into the route
	 * @param route
	 * @param link
	 */
	public static void addLink(Element route, Element link){
		Element linkCtn = route.getOwnerDocument().createElement(ElementList.LINK_CTN);
		linkCtn.setAttribute("id", link.getAttribute("id"));
		addChild(route,linkCtn);
	}
	
	
	public static void reconnect(Element route, Element sourceNode,
			Element targetNode) {
		String srcId = getId(sourceNode);
		if ( ! route.getAttribute("src").equals(srcId)){
			route.setAttribute("src", srcId);
		}
		String dstId = getId(targetNode);
		if ( ! route.getAttribute("dst").equals(dstId)){
			route.setAttribute("dst", dstId);
		}		
	}
	
	public static void removeElement(Element e) {
		Node parent = e.getParentNode();
		parent.removeChild(e);
		//formatProcessor.formatNode(parent);
	}
	
	/**
	 * restore the route with the related links
	 * @param parent
	 * @param route
	 * @param links
	 */
	public static void restoreRoute(Node parent,Element route,List<Element> links) {
		//add links
		for (Element link : links){
			insertAtFirst(parent,link);
		}
		//add a route
		insertAtLast(parent,route);
	}
	
	/**
	 * remove the route and handle the related link deletion
	 * @param route
	 */
	public static List<Element> removeRoute(Element route) {
		List<Element> deletedLink = new LinkedList<Element>(); 
		NodeList nl = route.getChildNodes();
		Element parent = (Element) route.getParentNode();
		for (int i = 0; i< nl.getLength(); i++){
			String id = ((Element)nl.item(i)).getAttribute("id");
			NodeList linkCtnList = parent.getElementsByTagName(ElementList.LINK_CTN);
			//check if this link is not used elsewhere
			int j = 0;
			while(j< linkCtnList.getLength() 
					&& !((Element)linkCtnList.item(j)).getAttribute("id").equals(id)){
				j++;
			}
			if (j < linkCtnList.getLength()){
				//remove the link
				Element link = getSubElementbyId(parent,id);
				if (link != null){
					removeElement(link);
					deletedLink.add(link);
				}
			}
		}
		return deletedLink;
	}

/*******************************************/	
/*****       ACCES FUNCTIONS          ******/
/*******************************************/
	public static List<Element> getChildren(Element root) {
		if (root != null){
			return nodeListToElementList(root.getChildNodes());
		}
		return Collections.emptyList();
	}

	public static List<Element> getNoConnectionChildren(Element root) {
		// return the model children without connections
		List<Element> l = getChildren(root);
		List<Element> tmp = new ArrayList<Element>(l);
		for (Element e : tmp){
			if (ElementList.isConnection(e.getTagName())){
				l.remove(e);
			}
		}
		return l;
	}

	public static IDOMModel getDOMModel(IEditorInput input) throws Exception {
		IFile file = ((IFileEditorInput)input).getFile();
		IModelManager manager = StructuredModelManager.getModelManager();
		IStructuredModel model = manager.getExistingModelForEdit(file);
		if (model == null) {
			model = manager.getModelForEdit(file);
		}
		if (model == null) {
			throw new Exception(
					"DOM Model is null, check the content type of your file (it seems that it's not *.xml file)");
		}
		if (!(model instanceof IDOMModel)) {
			throw new Exception("Model getted is not DOM Model!!!");
		}
		return (IDOMModel) model;
	}
	
	/**
	 * return the links contained in the same AS as aNode and in
	 * the descendants
	 * @param aNode
	 * @return
	 */
	public static List<Element> getLinks(Element aNode){
		Element parent = (Element)aNode.getParentNode();
        NodeList l = parent.getElementsByTagName(ElementList.LINK);
        return nodeListToElementList(l);
	}
	
//	public static IPropertySource getPropertySource(Object node){
//		if (node instanceof IDOMNode) {
//			INodeNotifier source = (INodeNotifier) node;
//			IPropertySource propertySource = (IPropertySource) source
//					.getAdapterFor(IPropertySource.class);
//			if (propertySource == null) {
//				propertySource = new XMLPropertySource(
//						(INodeNotifier) source);
//				return new ElementPropertySource(propertySource);
//			}
//		}
//		return null;
//	}
	
	
	
	public static Element getRootElement(IEditorInput input){
		Element e = null;
		try {
			e = getDOMModel(input).getDocument().getDocumentElement();
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		return e;
	}
	
//	public static int getStartOffset(Element e){
//		return ((ElementImpl)e).getStartOffset();
//	}
//	
//	public static int getLength(Element e){
//		return ((ElementImpl)e).getLength();
//	}
	
	public static List<Element> nodeListToElementList(NodeList toConvert){
		List<Element> elemList = new LinkedList<Element>();
		for (int i = 0; i < toConvert.getLength(); i++) {
			if (toConvert.item(i) instanceof Element) {
				elemList.add((Element) toConvert.item(i));
			}
		}
		return elemList;
	}

	public static Element getSourceNode(Element route) {
		String srcId = route.getAttribute("src");
		return getElementbyId(route, srcId);
	}

	public static Element getTargetNode(Element route) {
		String dstId = route.getAttribute("dst");
		return getElementbyId(route, dstId);
	}
	
	public static List<?> getSouceConnections(Element model) {
		return getConnections(model,"src");
	}

	public static List<?> getTargetConnections(Element model) {
		return getConnections(model,"dst");
	}
	
//	public static List<String> getAttributeList(Element element) {
//		List<String> l = new LinkedList<String>();
//		if (element == null){
//			return l;
//		}
//		for (int i=0; i<element.getAttributes().getLength(); i++){
//			l.add(element.getAttributes().item(i).getNodeName());
//		}
//		return l;
//	}
	
	/*******************************************/	
	/*****       PRIVATE UTILS            ******/
	/*******************************************/
	
	private static void insertAtLast(Node parent, Element newChild){
		try{
			parent.insertBefore(newChild, null);
			//format sources
			formatProcessor.formatNode(parent);
		}catch (Exception e2) {
			e2.printStackTrace();
		}
	}
	
	private static void insertAtFirst(Node parent, Element child) {
		try{
			parent.insertBefore(child, parent.getFirstChild());
			//format sources
			formatProcessor.formatNode(parent);
		}catch (Exception e2) {
			e2.printStackTrace();
		}
	}
	
	private static String getId(Element e){
		if (e.hasAttribute("id"))
			return e.getAttribute("id");
		return "";
	}
	

	
	private static Element getElementbyId(Node anyNode, String id){
		Element root = anyNode.getOwnerDocument().getDocumentElement();
		return getSubElementbyId(root,id);
	}
	
	private static Element getSubElementbyId(Element current, String id){
		if (getId(current).equals(id)){
			return current;
		}
		for (Element e : nodeListToElementList(current.getChildNodes())){
			Element res = getSubElementbyId(e,id);
			if (res != null){
				return res;
			}
		}
		return null;
	}
	
	private static List<Element> getConnections(Element model, String type){
		//TODO look for route or ASroute according to the model type
		NodeList nlAS = model.getOwnerDocument().getElementsByTagName(ElementList.AS_ROUTE);
		NodeList nl = model.getOwnerDocument().getElementsByTagName(ElementList.ROUTE);
		
		List<Element> routeList = new ArrayList<Element>();
		routeList.addAll(nodeListToElementList(nl));
		routeList.addAll(nodeListToElementList(nlAS));
		
		String id = getId(model);
		List<Element> connected = new LinkedList<Element>();
		for (Element e : routeList){
			if (e.getAttribute(type).equals(id)){
				connected.add(e);
			}
		}
		return connected;
	}
}

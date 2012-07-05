package simgrideclipseplugin.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.document.TextImpl;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.eclipse.wst.xml.core.internal.provisional.format.FormatProcessorXML;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import simgrideclipseplugin.editors.SimgridGraphicEditor;

//import simgrideclipseplugin.editors.properties.ElementPropertySource;

/**
 * Help to access WST internal model and hide it to avoid restriction warnings
 */
@SuppressWarnings("restriction")
public final class ModelHelper {
	
	private final static FormatProcessorXML formatProcessor = new FormatProcessorXML();
	private static IDOMModel model;
	private static Map<String,Integer> numIDMap = new HashMap<String,Integer>();
	
/*******************************************/	
/*****   CREATION/DELETION FUNCTIONS  ******/
/*******************************************/
	
	/**
	 * add the child in the write place
	 * 
	 */
	public static void addElementChild(Node parent, Element newChild){
		try{
			model.aboutToChangeModel();
			//find the block
			Element firstChild = getFirstElementByTag(parent, newChild.getTagName());
			if (firstChild != null){
				parent.insertBefore(newChild, firstChild);
			}
			//fist insertion of this type
			else{
				//go after the links
				Node child = parent.getFirstChild();
				while(child != null
						&& (! (child instanceof Element)
						|| !((Element) child).getTagName().equals(ElementList.LINK))){
					child = child.getNextSibling();
				}
				parent.insertBefore(newChild,child);
			}
			formatProcessor.formatNode(parent);
			model.changedModel();
		}catch (Exception e2) {
			e2.printStackTrace();
		}
	}
	
	
	public static Element createElement(String tagName, Map<String,String> attrMap){
		Element newElem = model.getDocument().createElement(tagName);
		for (String attr : attrMap.keySet()){
			if (!attrMap.get(attr).isEmpty()){
				newElem.setAttribute(attr, attrMap.get(attr));
			}
		}
		return newElem;
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
	
	public static void createAndAddLink(Element route,Map<String,String> attrMap){
		Element link = createElement(ElementList.LINK,attrMap);
		insertAtFirst(route.getParentNode(),link);
		//add it to the route
		addLink(route,link);
	}
	
	/**
	 * add the link to the route by creating a link_ctn node into the route
	 */
	public static void addLink(Element route, Element link){
		Element linkCtn = route.getOwnerDocument().createElement(ElementList.LINK_CTN);
		linkCtn.setAttribute("id", link.getAttribute("id"));
		addElementChild(route,linkCtn);
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
	
	public static String createId(String type){
		String newId;
		if (numIDMap.get(type) == null){
			numIDMap.put(type, 0);
		}
		int idNum = numIDMap.get(type);
		newId = type+idNum;
		//increment for next one
		numIDMap.put(type,++idNum);
		
		//verify uniqueness of id for this type
		String saveId;
		List<Element> ndl = nodeListToElementList(model.getDocument().getElementsByTagName(type));
		do{
			saveId = newId;
			for(Element n : ndl){
				if (n.getAttribute("id").equals(newId)){
					idNum = numIDMap.get(type);
					newId = type+idNum;
					//increment for next one
					numIDMap.put(type,++idNum);
				}
			}
		}while (!saveId.equals(newId));
		return newId;
	}
	
	/**
	 * remove the element from the document
	 * @param e
	 */
	public static void removeElement(Element e) {
		model.aboutToChangeModel();
		Node parent = e.getParentNode();
		parent.removeChild(e);
		//FIXME : find node that doesn't contains anything and remove them
		Node child = parent.getFirstChild();
		Node next;
		while(child != null){
			next = child.getNextSibling();
			if (child instanceof TextImpl && 
					//TODO find a good regexp
					(child.getTextContent().startsWith("\n\t\n")
							|| child.getTextContent().startsWith("\n\n"))){
				parent.removeChild(child);
			}
			child = next;
		}
		model.changedModel();
	}
	
	/**
	 * restore the route with the related links
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
	 * @return a list of the deleted links during the deletion
	 */
	public static List<Element> removeRoute(Element route) {
		//remove the links
		List<Element> deletedLink = new LinkedList<Element>(); 
		NodeList nl = route.getChildNodes();
		Element parent = (Element) route.getParentNode();
		for (int i = 0; i< nl.getLength(); i++){
			if (nl.item(i) instanceof Element){
				Element myLinkCtn = (Element)nl.item(i);
				String id = myLinkCtn.getAttribute("id");
				NodeList linkCtnList = parent.getElementsByTagName(ElementList.LINK_CTN);
				//check if this link is not used elsewhere
				int j = 0;
				while(j< linkCtnList.getLength()
						&& (linkCtnList.item(j) == myLinkCtn
							|| !((Element)linkCtnList.item(j)).getAttribute("id").equals(id))){
					j++;
				}
				if (j == linkCtnList.getLength()){
					//remove the link
					Element link = getSubElementbyId(parent,id);
					if (link != null){
						removeElement(link);
						deletedLink.add(link);
					}
				}
			}
		}
		//remove the route
		removeElement(route);
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
		ModelHelper.model = (IDOMModel) model;
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
	
	/**
	 * return the routers contained in the ASNode and in
	 * the descendants
	 */
	public static List<Element> getRouters(Element ASNode) {
        NodeList rl = ASNode.getElementsByTagName(ElementList.ROUTER);
        NodeList cl = ASNode.getElementsByTagName(ElementList.CLUSTER);
        NodeList pl = ASNode.getElementsByTagName(ElementList.PEER);
        List<Element> l = nodeListToElementList(rl);
        l.addAll(nodeListToElementList(cl));
        l.addAll(nodeListToElementList(pl));
        return l;
	}
	
	/**
	 * return the gateway id according to his node type or null
	 * @param node contains the router. Works for different node type :
	 *  <ul>
	 *  <li>CLUSTER</li>
	 *  <li>PEER</li>
	 *  <li>AS (if it contains only one router)</li>
	 *  <li>ROUTER</li>
	 *  </ul>
	 *  @see ElementList ElementList to find the node types
	 * @return
	 */
	public static String getGatewayId(Element node) {
		String gw = null;
		if (node.getTagName().equals(ElementList.CLUSTER)){
			String prefix = node.getAttribute("prefix");
			String suffix = node.getAttribute("suffix");
			gw = prefix + "router_" + suffix;
		}else if (node.getTagName().equals(ElementList.PEER)){
			//TODO add the peer gateway
		}
		else if (node.getTagName().equals(ElementList.AS)){
			List<Element> routers = ModelHelper.getRouters(node);
			if (routers.size() == 1){
				gw = routers.get(0).getAttribute("id");
			}
		}
		else if (node.getTagName().equals(ElementList.ROUTER)){
			gw = node.getAttribute("id");
		}
		return gw;
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
	
	public static List<Element> nodeListToElementList(NodeList toConvert){
		List<Element> elemList = new LinkedList<Element>();
		for (int i = 0; i < toConvert.getLength(); i++) {
			if (toConvert.item(i) instanceof Element) {
				elemList.add((Element) toConvert.item(i));
			}
		}
		return elemList;
	}

	/**
	 * return the node related to the route as a source
	 */
	public static Element getSourceNode(Element route) {
		String srcId = route.getAttribute("src");
		return getElementbyId(route, srcId);
	}

	/**
	 * return the node related to the route as a target
	 */
	public static Element getTargetNode(Element route) {
		String dstId = route.getAttribute("dst");
		return getElementbyId(route, dstId);
	}
	
	public static List<Element> getConnections(Element node) {
		List<Element> ret = getSouceConnections(node);
		ret.addAll(getTargetConnections(node));
		return ret;
	}
	
	/**
	 * return the route related to the node as a source
	 */
	public static List<Element> getSouceConnections(Element node) {
		return getConnections(node,"src");
	}

	/**
	 * return the route related to the node as a target
	 */
	public static List<Element> getTargetConnections(Element node) {
		return getConnections(node,"dst");
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
	
	@SuppressWarnings("unchecked")
	public static StructuredSelection partToModelSelection(IStructuredSelection partSelection){
		 List<?> l = partSelection.toList();
		 if (!l.isEmpty()){
			 List<Object> modelList = new ArrayList<Object>(l.size());
			 for (EditPart e : (List<EditPart>) l) {
				 modelList.add(e.getModel());
			 }
			 return new StructuredSelection(modelList);
		 }
		 return new StructuredSelection();
	 }

	public static StructuredSelection modelToPartSelection(IStructuredSelection modelSelection, SimgridGraphicEditor graphEditor){
		 List<?> selectedList = modelSelection.toList();
		 if (!selectedList.isEmpty()){
			 List<EditPart> selectedPartList = new LinkedList<EditPart>();
			 for (Object e : selectedList){
				 if (e instanceof Element){
					 EditPart ep =(EditPart) graphEditor.getEditPartRegistry().get(e);
					 if (ep != null && ep.isActive()){
						 selectedPartList.add(ep);
					 }
				 }
			 }
			 return new StructuredSelection(selectedPartList);
		 }
		 return new StructuredSelection();
	 }
	/*******************************************/	
	/*****       PRIVATE UTILS            ******/
	/*******************************************/
	
	/**
	 * return the first element whose got this tag name in this container
	 * or null
	 */
	private static Element getFirstElementByTag(Node parent, String tag){
		NodeList nl = parent.getChildNodes();
		Element foundElem = null;
		int i=0;
		while ( i< nl.getLength() && foundElem == null){
			if (nl.item(i) instanceof Element && ((Element)nl.item(i)).getTagName().equals(tag)){
				foundElem = (Element)nl.item(i);
			}
			i++;
		}
		return foundElem;
	}
	
	private static void insertAtLast(Node parent, Element newChild){
		try{
			model.aboutToChangeModel();
			parent.insertBefore(newChild, null);
			//format sources
			formatProcessor.formatNode(parent);
			model.changedModel();
		}catch (Exception e2) {
			e2.printStackTrace();
		}
	}
	
	private static void insertAtFirst(Node parent, Element child) {
		try{
			model.aboutToChangeModel();
			parent.insertBefore(child, parent.getFirstChild());
			//format sources
			formatProcessor.formatNode(parent);
			model.changedModel();
		}catch (Exception e2) {
			e2.printStackTrace();
		}
	}
	
	private static String getId(Element e){
		if (e.hasAttribute("id"))
			return e.getAttribute("id");
		return "";
	}
	

	/**
	 * return the element corresponding to this id or null in the entire document
	 */
	private static Element getElementbyId(Node anyNode, String id){
		Element root = anyNode.getOwnerDocument().getDocumentElement();
		return getSubElementbyId(root,id);
	}
	
	/**
	 * return the element corresponding to this id or null in the sub tree
	 * determine by the current node
	 */
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
	
	/**
	 * return the list of the connection related to the model element 
	 * as a source or a destination
	 * @param model: a connectable node
	 * @param type : the type must be "src" or "dst"
	 * @return
	 */
	private static List<Element> getConnections(Element model, String type){
		//look for route or ASroute according to the model type
		List<Element> routeList = new ArrayList<Element>();
		NodeList nlRoute;
		NodeList nlBPRoute;
		if (SimgridRules.isASLike(model.getTagName())){
			nlRoute = model.getOwnerDocument().getElementsByTagName(ElementList.AS_ROUTE);
			nlBPRoute = model.getOwnerDocument().getElementsByTagName(ElementList.BYPASS_AS_ROUTE);
			
		}
		else{
			nlRoute = model.getOwnerDocument().getElementsByTagName(ElementList.ROUTE);
			nlBPRoute = model.getOwnerDocument().getElementsByTagName(ElementList.BYPASS_ROUTE);
			
		}
		routeList.addAll(nodeListToElementList(nlRoute));
		routeList.addAll(nodeListToElementList(nlBPRoute));
		
		String id = getId(model);
		List<Element> connected = new LinkedList<Element>();
		for (Element e : routeList){
			if (e.getAttribute(type).equals(id)){
				connected.add(e);
			}
		}
		return connected;
	}

	public static void addModelListener(IEditorInput input,
			SimgridModelListener simgridModelListener) {
		try{
			IDOMModel model = ModelHelper.getDOMModel(input);
			model.addModelStateListener(simgridModelListener);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

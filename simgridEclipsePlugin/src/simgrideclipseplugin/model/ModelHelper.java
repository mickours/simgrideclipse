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
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IModelManager;
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.document.TextImpl;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.core.internal.provisional.format.FormatProcessorXML;
import org.eclipse.wst.xml.ui.internal.properties.XMLPropertySource;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import simgrideclipseplugin.editors.SimgridGraphicEditor;
import simgrideclipseplugin.editors.properties.ElementPropertySource;
import simgrideclipseplugin.wizards.LinkSelectionPage.LinkCtnList;

//import simgrideclipseplugin.editors.properties.ElementPropertySource;

/**
 * Help to access WST internal model and hide it to avoid restriction warnings
 */
@SuppressWarnings("restriction")
public final class ModelHelper {

	private final static FormatProcessorXML formatProcessor = new FormatProcessorXML();
	private static IDOMModel model;
	private static Map<String, Integer> numIDMap = new HashMap<String, Integer>();

	/*******************************************/
	/***** CREATION/DELETION FUNCTIONS ******/
	/*******************************************/

	/**
	 * add the child in the parent element at the right place
	 */
	public static void addElementChild(Node parent, Element newChild) {
		model.aboutToChangeModel();
		try {
			// find the block
			Element firstChild = getFirstElementByTag(parent,
					newChild.getTagName());
			if (firstChild != null) {
				parent.insertBefore(newChild, firstChild);
			}
			// fist insertion of this type
			else {
				// go after the links
				Node child = parent.getFirstChild();
				while (child != null
						&& (!(child instanceof Element) || !((Element) child)
								.getTagName().equals(ElementList.LINK))) {
					child = child.getNextSibling();
				}
				parent.insertBefore(newChild, child);
			}
			formatProcessor.formatNode(parent);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		model.changedModel();
	}

	public static Element createElement(String tagName,
			Map<String, String> attrMap) {
		Element newElem = model.getDocument().createElement(tagName);
		for (String attr : attrMap.keySet()) {
			if (!attrMap.get(attr).isEmpty()) {
				newElem.setAttribute(attr, attrMap.get(attr));
			}
		}
		return newElem;
	}

	public static Element createAndAddRoute(Element sourceNode,
			Element targetNode, String routeType) {
		model.aboutToChangeModel();
		// create a route
		Element route = sourceNode.getOwnerDocument().createElement(routeType);
		route.setAttribute("src", getId(sourceNode));
		route.setAttribute("dst", getId(targetNode));
		model.changedModel();
		// include the route in sourceNode location
		insertAtLast(sourceNode.getParentNode(), route);
		return route;
	}

	/**
	 * create a Link, set the attributes using the attrMap and insert it in the
	 * refNode's parent
	 */
	public static Element createAndAddLink(Element refNode,
			Map<String, String> attrMap) {
		Element link = createElement(ElementList.LINK, attrMap);
		insertAtFirst(refNode.getParentNode(), link);
		return link;
	}

	/**
	 * add the link to the route by creating a link_ctn node into the route
	 */
	public static void addLinkToRoute(Element route, Element link,
			String direction) {
		Element linkCtn = route.getOwnerDocument().createElement(
				ElementList.LINK_CTN);
		linkCtn.setAttribute("id", link.getAttribute("id"));
		linkCtn.setAttribute("direction", direction);
		insertAtLast(route, linkCtn);

	}
	

	public static String createId(String tagName) {
		String newId;
		if (numIDMap.get(tagName) == null) {
			numIDMap.put(tagName, 0);
		}
		int idNum = numIDMap.get(tagName);
		newId = tagName + idNum;

		// verify uniqueness of id for this type
		String saveId;
		List<Element> ndl = nodeListToElementList(model.getDocument()
				.getElementsByTagName(tagName));
		do {
			saveId = newId;
			for (Element n : ndl) {
				if (n.getAttribute("id").equals(newId)) {
					// increment
					numIDMap.put(tagName, ++idNum);
					newId = tagName + idNum;
				}
			}
		} while (!saveId.equals(newId));
		return newId;
	}

	/**
	 * return true if the newId is unique in the file or equals with the oldId
	 */
	public static boolean isUniqueId(String newId, String oldId, String tagName) {
		if (newId.equals(oldId)) {
			return true;
		}
		List<Element> ndl = nodeListToElementList(model.getDocument()
				.getElementsByTagName(tagName));
		for (Element n : ndl) {
			if (n.getAttribute("id").equals(newId)) {
				return false;
			}
		}
		return true;
	}

	public static boolean isUniqueId(String newId, String tagName) {
		return isUniqueId(newId, null, tagName);
	}

	/**
	 * this function his used to check if an element <code>toCheckElem</code>
	 * contains or is used as a gateway by routes
	 * 
	 * @return a map of couple <route,gateway> that contains the toChekElement
	 *         including himself
	 */
	public static Map<Element, Element> isUsedOrContainsGateway(
			Element toCheckElem) {
		model.aboutToChangeModel();
		Map<Element, Element> gwMap = new HashMap<Element, Element>();
		NodeList nl = model.getDocument().getElementsByTagName(
				ElementList.AS_ROUTE);
		NodeList nlbp = model.getDocument().getElementsByTagName(
				ElementList.BYPASS_AS_ROUTE);
		List<Element> ASRouteList = nodeListToElementList(nl);
		ASRouteList.addAll(nodeListToElementList(nlbp));
		List<Element> l = ModelHelper.getAllNoConnectionChildren(toCheckElem);
		l.add(toCheckElem);
		for (Element elem : l) {
			if (SimgridRules.canBeAGateway(elem.getTagName())) {
				for (Element route : ASRouteList) {
					Element src = ModelHelper.getSourceGateway(route);
					Element trg = ModelHelper.getDestinationGateway(route);
					if (elem.equals(src) || elem.equals(trg)) {
						gwMap.put(route, elem);
					}
				}
			}
		}
		model.changedModel();
		return gwMap;
	}

	// public static boolean isUniqueId(Element element){
	// List<Element> ndl =
	// nodeListToElementList(model.getDocument().getElementsByTagName(element.getTagName()));
	// for(Element n : ndl){
	// if (n.getAttribute("id").equals(getId(element)) && element != n){
	// return false;
	// }
	// }
	// return true;
	// }

	/**
	 * remove the element from the document
	 * 
	 * @param e
	 */
	public static void removeElement(Element e) {
		model.aboutToChangeModel();
		Node parent = e.getParentNode();
		parent.removeChild(e);
		// find node that doesn't contain anything and remove them
		Node child = parent.getFirstChild();
		Node next;
		while (child != null) {
			next = child.getNextSibling();
			if (child instanceof TextImpl && next instanceof TextImpl
					&& (child.getTextContent().matches("\n+\t*"))) {
				parent.removeChild(child);
			}
			child = next;
		}
		model.changedModel();
	}

	/**
	 * restore the route with the related links
	 */
	public static void restoreRoute(Node parent, Element route,
			List<Element> links) {
		// add links
		for (Element link : links) {
			insertAtFirst(parent, link);
		}
		// add a route
		insertAtLast(parent, route);
	}

	/**
	 * remove the route and handle the related link deletion
	 * 
	 * @return a list of the deleted links during the deletion
	 */
	public static List<Element> removeRoute(Element route) {
		model.aboutToChangeModel();
		// remove the links
		List<Element> deletedLink = new LinkedList<Element>();
		NodeList nl = route.getChildNodes();
		Element parent = (Element) route.getParentNode();
		for (int i = 0; i < nl.getLength(); i++) {
			if (nl.item(i) instanceof Element) {
				Element myLinkCtn = (Element) nl.item(i);
				String id = myLinkCtn.getAttribute("id");
				NodeList linkCtnList = parent
						.getElementsByTagName(ElementList.LINK_CTN);
				// check if this link is not used elsewhere
				int j = 0;
				while (j < linkCtnList.getLength()
						&& (linkCtnList.item(j) == myLinkCtn || !((Element) linkCtnList
								.item(j)).getAttribute("id").equals(id))) {
					j++;
				}
				if (j == linkCtnList.getLength()) {
					// remove the link
					Element link = getSubElementbyId(parent, id);
					if (link != null) {
						removeElement(link);
						deletedLink.add(link);
					}
				}
			}
		}
		// remove the route
		removeElement(route);
		model.changedModel();
		return deletedLink;
	}

	/*******************************************/
	/***** EDITION FUNCTIONS ******/
	/*******************************************/

	public static void editRoute(Element toEditElement, Element sourceNode,
			Element targetNode) {
		editAttribute(toEditElement, "src", getId(sourceNode));
		editAttribute(toEditElement, "dst", getId(targetNode));
	}

	public static void editRouteLinks(Element toEditElement,
			List<Element> linkList, List<String> dirList) {
		// remove old nodes
		for (Element e : getChildren(toEditElement)) {
			assert (!linkList.contains(e));
			removeElement(e);
		}
		// add new nodes
		for (int j = 0; j < linkList.size(); j++) {
			addLinkToRoute(toEditElement, linkList.get(j), dirList.get(j));
		}
	}

	public static void editRouteLinks(Element toEditElement,
			List<Element> linkCtnList) {
		// remove old nodes
		for (Element e : getChildren(toEditElement)) {
			assert (!linkCtnList.contains(e));
			removeElement(e);
		}
		// add new nodes
		for (int j = 0; j < linkCtnList.size(); j++) {
			Element linkCtn =linkCtnList.get(j);
			insertAtLast(toEditElement, linkCtn);
		}
	}

//	public static void editRuleBasedRoute(Element toEditElement,
//			String src, String dst, String gw_src, String gw_dst) {		
//		editAttribute(toEditElement, "gw_src", gw_src);		
//		editAttribute(toEditElement, "gw_dst", gw_dst);
//	}
	public static void editRouteGateways(Element toEditElement,
			Element selectedSrcGw, Element selectedDstGw) {
		String id = ModelHelper.getGatewayRouterId(selectedSrcGw);
		editAttribute(toEditElement, "gw_src", id);
		id = ModelHelper.getGatewayRouterId(selectedDstGw);
		editAttribute(toEditElement, "gw_dst", id);
	}

	public static void editElementAttributes(Element toEditElement,
			Map<String, String> attrMap) {
		for (String attr : attrMap.keySet()) {
			editAttribute(toEditElement, attr, attrMap.get(attr));
		}

	}

	public static void editAttribute(Element toEditElement, String attrName,
			String value) {
		if (toEditElement.getAttribute(attrName) != value) {
			if (toEditElement != null) {
				toEditElement.setAttribute(attrName, value);
			}
		}
	}

	/*******************************************/
	/***** ACCES FUNCTIONS ******/
	/*******************************************/
	/**
	 * return the children in direct sub level of <code>root</code>
	 */
	public static List<Element> getChildren(Element root) {
		if (root != null) {
			return nodeListToElementList(root.getChildNodes());
		}
		return Collections.emptyList();
	}

	/**
	 * return all the children in the sub tree of <code>root</code>
	 */
	public static List<Element> getAllNoConnectionChildren(Element root) {
		List<Element> ret = getNoConnectionChildren(root);
		List<Element> tmp = new LinkedList<Element>();
		for (Element e : ret) {
			tmp.addAll(getAllNoConnectionChildren(e));
		}
		ret.addAll(tmp);
		ret.add(root);
		return ret;
	}

	public static List<Element> getNoConnectionChildren(Element root) {
		// return the model children without connections
		List<Element> l = getChildren(root);
		List<Element> tmp = new ArrayList<Element>(l);
		for (Element e : tmp) {
			if (SimgridRules.isConnection(e.getTagName())) {
				if (((Element) root).getNodeName().equals(ElementList.AS)
						&& ((Element) root).getAttributes()
								.getNamedItem("routing").getNodeValue()
								.equals("RuleBased")) {
					// do nothing
				} else {
					l.remove(e);
				}
			}
		}
		return l;
	}

	public static void setAttributeMap(Element toEditElement,
			Map<String, String> attrMap) {
		model.aboutToChangeModel();
		for (String attr : ElementList.getAttributesList(toEditElement
				.getTagName())) {
			if (toEditElement.hasAttribute(attr)) {
				attrMap.put(attr, toEditElement.getAttribute(attr));
			}
		}
		model.changedModel();
	}

	/**
	 * return the element corresponding to this id or null in the entire
	 * document
	 */
	public static Element getElementbyId(String id) {
		Element root = model.getDocument().getDocumentElement();
		return getSubElementbyId(root, id);
	}

	public static IDOMModel getDOMModel(IEditorInput input) throws Exception {
		IFile file = ((IFileEditorInput) input).getFile();
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
		return ModelHelper.model;
	}

	/**
	 * return the links contained in the same AS as aNode and in the descendants
	 */
	public static List<Element> getLinks(Element aNode) {
		Element parent = (Element) aNode.getParentNode();
		NodeList l = parent.getElementsByTagName(ElementList.LINK);
		return nodeListToElementList(l);
	}

	/**
	 * return the source gateway element for this route
	 */
	public static Element getSourceGateway(Element route) {
		return getGateway(route, "gw_src");
	}

	/**
	 * return the destination gateway element for this route
	 */
	public static Element getDestinationGateway(Element route) {
		return getGateway(route, "gw_dst");
	}

	/**
	 * return the routers contained in the ASNode and in the descendants
	 */
	public static List<Element> getRouters(Element ASNode) {
		List<Element> l = new LinkedList<Element>();
		for (String tag : ElementList.getElementTagNameList()) {
			if (SimgridRules.canBeAGateway(tag)) {
				NodeList nl = ASNode.getElementsByTagName(tag);
				l.addAll(nodeListToElementList(nl));
			}
		}
		// add himself as a possible gateway
		if (SimgridRules.canBeAGateway(ASNode.getTagName())) {
			l.add(ASNode);
		}
		return l;
	}

	/**
	 * return the gateway router id according to the node type or null
	 * 
	 * @param node
	 *            contains the router. Works for different node type :
	 *            <ul>
	 *            <li>CLUSTER</li>
	 *            <li>PEER</li>
	 *            <li>AS (if it contains only one router)</li>
	 *            <li>ROUTER</li>
	 *            </ul>
	 * @see ElementList ElementList to find the node types
	 */
	public static String getGatewayRouterId(Element node) {
		String gw = null;
		if (node.getTagName().equals(ElementList.CLUSTER)) {
			gw = getClusterRouterId(node);
		} else if (node.getTagName().equals(ElementList.AS)) {
			List<Element> routers = ModelHelper.getRouters(node);
			if (routers.size() == 1) {
				gw = routers.get(0).getAttribute("id");
			}
		} else if (node.getTagName().equals(ElementList.ROUTER)) {
			gw = node.getAttribute("id");
		}
		return gw;
	}

	/**
	 * find and return the router corresponding to routerId in the
	 * toSearchInList
	 */
	public static Element getGatewayFromRouterId(String routerId,
			List<Element> toSearchInList) {
		String gw = null;
		for (Element node : toSearchInList) {
			if (node.getTagName().equals(ElementList.CLUSTER)) {
				gw = getClusterRouterId(node);
			} else if (node.getTagName().equals(ElementList.ROUTER)) {
				gw = node.getAttribute("id");
			}
			if (gw != null && gw.equals(routerId)) {
				return node;
			}
		}
		return null;
	}

	private static String getClusterRouterId(Element node) {
		String routerId = node.getAttribute("router_id");
		if (routerId.isEmpty()) {
			String prefix = node.getAttribute("prefix");
			String suffix = node.getAttribute("suffix");
			String id = node.getAttribute("id");
			routerId = prefix + id + "_router" + suffix;
		}
		return routerId;
	}

	public static IPropertySource getPropertySource(Object node) {
		if (node instanceof IDOMNode) {
			INodeNotifier source = (INodeNotifier) node;
			IPropertySource propertySource = (IPropertySource) source
					.getAdapterFor(IPropertySource.class);
			if (propertySource == null) {
				propertySource = new XMLPropertySource(source);
				return new ElementPropertySource(propertySource);
			}
		}
		return null;
	}

	/**
	 * fill the routeLinks with the ordered links contains in this route
	 */
	public static void fillRouteLinksList(Element route, LinkCtnList routeLinks) {
		for (int i = 0; i < route.getChildNodes().getLength(); i++) {
			if (route.getChildNodes().item(i) instanceof Element) {
				Element linkCtn = (Element) route.getChildNodes().item(i);
				routeLinks.addLast(getSubElementbyId(
						(Element) route.getParentNode(), getId(linkCtn)));
				routeLinks.getLast().setDir(linkCtn.getAttribute("direction"));
			}
		}
	}

	public static Element getRootElement(IEditorInput input) {
		Element e = null;
		try {
			e = getDOMModel(input).getDocument().getDocumentElement();
		} catch (Exception ex) {
			throw new RuntimeException("Error while loading document\n"
					+ ex.getMessage());
		}
		return e;
	}

	/**
	 * return the node related to the route as a source
	 */
	public static Element getSourceNode(Element route) {
		String srcId = route.getAttribute("src");
		return getElementbyId(srcId);
	}

	/**
	 * return the node related to the route as a target
	 */
	public static Element getTargetNode(Element route) {
		String dstId = route.getAttribute("dst");
		return getElementbyId(dstId);
	}

	/**
	 * return the list of source and destination connections (route) associate
	 * to this node
	 */
	public static List<Element> getConnections(Element node) {
		List<Element> ret = getSouceConnections(node);
		ret.addAll(getTargetConnections(node));
		return ret;
	}

	/**
	 * return the route related to the node as a source
	 */
	public static List<Element> getSouceConnections(Element node) {
		return getConnections(node, "src");
	}

	/**
	 * return the route related to the node as a target
	 */
	public static List<Element> getTargetConnections(Element node) {
		return getConnections(node, "dst");
	}

	/*******************************************/
	/***** UTILS FUNCTIONS ******/
	/*******************************************/

	public static List<Element> nodeListToElementList(NodeList toConvert) {
		List<Element> elemList = new LinkedList<Element>();
		for (int i = 0; i < toConvert.getLength(); i++) {
			if (toConvert.item(i) instanceof Element) {
				elemList.add((Element) toConvert.item(i));
			}
		}
		return elemList;
	}

	@SuppressWarnings("unchecked")
	public static StructuredSelection partToModelSelection(
			IStructuredSelection partSelection) {
		List<?> l = partSelection.toList();
		if (!l.isEmpty()) {
			List<Object> modelList = new ArrayList<Object>(l.size());
			for (EditPart e : (List<EditPart>) l) {
				modelList.add(e.getModel());
			}
			return new StructuredSelection(modelList);
		}
		return new StructuredSelection();
	}

	public static StructuredSelection modelToPartSelection(
			IStructuredSelection modelSelection,
			SimgridGraphicEditor graphEditor) {
		List<?> selectedList = modelSelection.toList();
		if (!selectedList.isEmpty()) {
			List<EditPart> selectedPartList = new LinkedList<EditPart>();
			for (Object e : selectedList) {
				//if (e instanceof Element) {
					EditPart ep = (EditPart) graphEditor.getEditPartRegistry()
							.get(e);
					if (ep != null && ep.isActive()) {
						selectedPartList.add(ep);
					}
				//}
			}
			return new StructuredSelection(selectedPartList);
		}
		return new StructuredSelection();
	}

	public static void addModelListener(IEditorInput input,
			SimgridModelListener simgridModelListener) {
		try {
			IDOMModel model = ModelHelper.getDOMModel(input);
			model.addModelStateListener(simgridModelListener);
		} catch (Exception e) {
			throw new RuntimeException("Error while loading document model\n"
					+ e.getMessage());
		}
	}

	/*******************************************/
	/***** PRIVATE UTILS ******/
	/*******************************************/

	/**
	 * return the first element whose got this tag name in this container or
	 * null
	 */
	private static Element getFirstElementByTag(Node parent, String tag) {
		NodeList nl = parent.getChildNodes();
		Element foundElem = null;
		int i = 0;
		while (i < nl.getLength() && foundElem == null) {
			if (nl.item(i) instanceof Element
					&& ((Element) nl.item(i)).getTagName().equals(tag)) {
				foundElem = (Element) nl.item(i);
			}
			i++;
		}
		return foundElem;
	}

	public static void insertAtLast(Node parent, Element newChild) {
		model.aboutToChangeModel();
		try {
			parent.insertBefore(newChild, null);
			// format sources
			formatProcessor.formatNode(parent);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		model.changedModel();
	}

	private static void insertAtFirst(Node parent, Element child) {
		model.aboutToChangeModel();
		try {
			parent.insertBefore(child, parent.getFirstChild());
			// format sources
			formatProcessor.formatNode(parent);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		model.changedModel();
	}

	private static String getId(Element e) {
		if (e != null && e.hasAttribute("id"))
			return e.getAttribute("id");
		return "";
	}

	/**
	 * return the element corresponding to this id or null in the sub tree
	 * determine by the current node
	 */
	private static Element getSubElementbyId(Element current, String id) {
		if (getId(current).equals(id)) {
			return current;
		}
		for (Element e : nodeListToElementList(current.getChildNodes())) {
			Element res = getSubElementbyId(e, id);
			if (res != null) {
				return res;
			}
		}
		return null;
	}

	/**
	 * return the list of the connection related to the model element as a
	 * source or a destination
	 * 
	 * @param model
	 *            : a connectable node
	 * @param type
	 *            : the type must be "src" or "dst"
	 * @return
	 */
	private static List<Element> getConnections(Element model, String type) {
		// look for route or ASroute according to the model type
		List<Element> routeList = new ArrayList<Element>();
		NodeList nlRoute;
		NodeList nlBPRoute;
		if (SimgridRules.isASLike(model.getTagName())) {
			nlRoute = model.getOwnerDocument().getElementsByTagName(
					ElementList.AS_ROUTE);
			nlBPRoute = model.getOwnerDocument().getElementsByTagName(
					ElementList.BYPASS_AS_ROUTE);

		} else {
			nlRoute = model.getOwnerDocument().getElementsByTagName(
					ElementList.ROUTE);
			nlBPRoute = model.getOwnerDocument().getElementsByTagName(
					ElementList.BYPASS_ROUTE);

		}
		routeList.addAll(nodeListToElementList(nlRoute));
		routeList.addAll(nodeListToElementList(nlBPRoute));

		String id = getId(model);
		List<Element> connected = new LinkedList<Element>();
		for (Element e : routeList) {
			if (e.getAttribute(type).equals(id)) {
				connected.add(e);
			}
		}
		return connected;
	}

	private static Element getGateway(Element route, String type) {
		List<Element> toSearchIn = new LinkedList<Element>();
		Element node = null;
		if (type.endsWith("src")) {
			node = getSourceNode(route);
			if (node != null) {
				toSearchIn.addAll(ModelHelper.getAllNoConnectionChildren(node));
			}
		} else if (type.endsWith("dst")) {
			node = getTargetNode(route);
			if (node != null) {
				toSearchIn.addAll(ModelHelper.getAllNoConnectionChildren(node));
			}
		}

		return getGatewayFromRouterId(route.getAttribute(type), toSearchIn);
	}

	public static Element getCommonAncestor(Element upper, Element element) {
		Element parentElement = element;
		Element toReach = (Element) upper.getParentNode();
		Element toReturn = element;
		while (!(toReach.equals(parentElement))) 
		{			
			toReturn = parentElement;
			parentElement = (Element) toReturn.getParentNode();
		} 
		return toReturn;
	}

}

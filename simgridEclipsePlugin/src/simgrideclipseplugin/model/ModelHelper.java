package simgrideclipseplugin.model;

import java.util.ArrayList;
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
	
	public static void addChild(Node parent, Element child){
		try{
			parent.appendChild(child);
			//format sources
			formatProcessor.formatNode(parent);
		}catch (Exception e2) {
			e2.printStackTrace();
		}
	}
	
	public static void removeElement(Element e) {
		//TODO handle the link deletion if it's a route
		Node parent = e.getParentNode();
		parent.removeChild(e);
		//formatProcessor.formatNode(parent);
	}

	public static List<Element> getChildren(Element root) {
		// return the model children
		return nodeListToElementList(root.getChildNodes());
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

	public static void addRoute(Element route, Node parent) {
		//add a route
		addChild(parent,route);
	}

	public static Element createRoute(Element sourceNode, Element targetNode,
			String routeType) {
		//create a route
		Element route = sourceNode.getOwnerDocument().createElement(routeType);
		route.setAttribute("src",getId(sourceNode));
		route.setAttribute("dst",getId(targetNode));
		//include the route in sourceNode location
		addChild(sourceNode.getParentNode(),route);
		return route;
	}

	public static Element getSourceNode(Element route) {
		String srcId = route.getAttribute("src");
		return getElementbyId(route, srcId);
	}

	public static Element getTargetNode(Element route) {
		String dstId = route.getAttribute("dst");
		return getElementbyId(route, dstId);
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

	public static List<?> getSouceConnections(Element model) {
		return getConnections(model,"src");
	}

	public static List<?> getTargetConnections(Element model) {
		return getConnections(model,"dst");
	}
	
	private static String getId(Element e){
		if (e.hasAttribute("id"))
			return e.getAttribute("id");
		return "";
	}
	
	private static List<Element> nodeListToElementList(NodeList toConvert){
		List<Element> elemList = new LinkedList<Element>();
		for (int i = 0; i < toConvert.getLength(); i++) {
			if (toConvert.item(i) instanceof Element) {
				elemList.add((Element) toConvert.item(i));
			}
		}
		return elemList;
	}
	
	private static Element getElementbyId(Node anyNode, String id){
		Element root = anyNode.getOwnerDocument().getDocumentElement();
		return getSubElementbyId(root,id);
	}
	
	private static Element getSubElementbyId(Element current, String id){
		if (getId(current).equals(id)){
			return current;
		}
		for (Element e : getChildren(current)){
			Element res = getSubElementbyId(e,id);
			if (res != null){
				return e;
			}
		}
		return null;
	}
	
	private static List<Element> getConnections(Element model, String type){
		//TODO look for route or ASroute according to the model type
				NodeList nlAS = model.getOwnerDocument().getElementsByTagName("ASroute");
				NodeList nl = model.getOwnerDocument().getElementsByTagName("route");
				
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

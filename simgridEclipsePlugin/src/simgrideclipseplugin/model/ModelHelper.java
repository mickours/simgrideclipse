package simgrideclipseplugin.model;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IModelManager;
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.document.ElementImpl;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.core.internal.provisional.format.FormatProcessorXML;
import org.eclipse.wst.xml.ui.internal.properties.XMLPropertySource;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import simgrideclipseplugin.editors.properties.ElementPropertySource;

/*
 * Help to access WST internal model and hide it to avoid restriction warnings
 */
@SuppressWarnings("restriction")
public final class ModelHelper {
	
	public static void addChild(Element parent, Element child){
		try{
			parent.appendChild(child);
			//format sources
			FormatProcessorXML formatProcessor = new FormatProcessorXML();
			formatProcessor.formatNode(parent);
		}catch (Exception e2) {
			e2.printStackTrace();
		}
	}
	
	public static void removeElement(Element e) {
		e.getParentNode().removeChild(e);
	}

	public static List<Element> getChildren(Element root) {
		// return the model children
		List<Element> elemList = new LinkedList<Element>();
		NodeList l = root.getChildNodes();
		for (int i = 0; i < l.getLength(); i++) {
			if (l.item(i) instanceof Element) {
				elemList.add((Element) l.item(i));
			}
		}
		return elemList;
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
	
	public static IPropertySource getPropertySource(Object node){
		if (node instanceof IDOMNode) {
			INodeNotifier source = (INodeNotifier) node;
			IPropertySource propertySource = (IPropertySource) source
					.getAdapterFor(IPropertySource.class);
			if (propertySource == null) {
				propertySource = new XMLPropertySource(
						(INodeNotifier) source);
				return new ElementPropertySource(propertySource);
			}
		}
		return null;
	}
	
	
	
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
	
	public static int getStartOffset(Element e){
		return ((ElementImpl)e).getStartOffset();
	}
	
	public static int getLength(Element e){
		return ((ElementImpl)e).getLength();
	}	
}

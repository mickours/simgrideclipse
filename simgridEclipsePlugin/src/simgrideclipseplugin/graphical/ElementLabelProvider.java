package simgrideclipseplugin.graphical;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class ElementLabelProvider extends LabelProvider {

	@Override
	public Image getImage(Object element) {
		Element elem = (Element)element;
		return SimgridIconProvider.getIcon(elem.getTagName());
	}

	@Override
	public String getText(Object element) {
		Element elem = (Element)element;
		String desc = elem.getTagName();
		NamedNodeMap nm = elem.getAttributes();
		for (int i=0; i< nm.getLength(); i++){
			Node node = nm.item(i);
			desc += " "+node.getNodeName()+"=\""+node.getNodeValue()+"\"";
		}
		return desc;
	}
	
}

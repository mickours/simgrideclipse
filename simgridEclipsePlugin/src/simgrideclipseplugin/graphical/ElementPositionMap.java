package simgrideclipseplugin.graphical;

import java.util.HashMap;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.GraphicalEditPart;
import org.w3c.dom.Element;

/**
 * store the map between model element and graphical element position
 * @author mmercier
 *
 */
public class ElementPositionMap {
	private static HashMap<Element, Point> map = new HashMap<Element, Point>();
	
	public static void setPosition(Element elem, Point p){
		map.put(elem,p);
	}
	
	public static void setPositionAndRefresh(GraphicalEditPart part, Point p){
		map.put((Element) part.getModel(),p);
		part.refresh();
		part.getFigure().revalidate();
	}
	
	public static Point getPosition(Element elem){
		Point p = map.get(elem);
		return p;
	}
	
	public static Point getPosition(GraphicalEditPart part){
		Point p = map.get(part.getModel());
		return p;
	}

	public static void removeElement(Element newElem) {
		map.remove(newElem);		
	}
}

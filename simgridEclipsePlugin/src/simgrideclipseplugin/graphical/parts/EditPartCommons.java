package simgrideclipseplugin.graphical.parts;

import java.util.Map;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.w3c.dom.Element;

import simgrideclipseplugin.model.ModelHelper;

public class EditPartCommons {

	/**
	 * inform end points that a connection links them
	 * @param connection
	 * @param anyEditPart
	 */
	public static void updateLinks(Element connection, EditPart anyEditPart){
		//inform end points that a link concerns them
		Map<?, ?> reg = anyEditPart.getViewer().getEditPartRegistry();
		Element src = ModelHelper.getSourceNode(connection);
		AbstractElementEditPart srcEP = (AbstractElementEditPart) reg.get(src);
		srcEP.refresh();
		Element dst = ModelHelper.getTargetNode(connection);
		GraphicalEditPart dstEP = (GraphicalEditPart) reg.get(dst);
		dstEP.refresh();
	}
}

package simgrideclipseplugin.graphical.parts;

import org.w3c.dom.Element;

import simgrideclipseplugin.graphical.figures.ContentPaneFigure;
import simgrideclipseplugin.model.ElementList;

public class ASContainerEditPart extends AbstractContainerEditPart {

	@Override
	protected void refreshVisuals() {
		ContentPaneFigure f = (ContentPaneFigure) getFigure();
		Element node = (Element)getModel();
		f.setTitle(ElementList.AS+" id=\""+node.getAttribute("id")+"\"");
		super.refreshVisuals();
	}
	
}

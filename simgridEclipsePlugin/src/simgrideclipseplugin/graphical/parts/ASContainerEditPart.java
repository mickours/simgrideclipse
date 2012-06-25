package simgrideclipseplugin.graphical.parts;

import org.w3c.dom.Element;

import simgrideclipseplugin.graphical.figures.ContentPaneFigure;

public class ASContainerEditPart extends AbstractContainerEditPart {

	@Override
	protected void refreshVisuals() {
		ContentPaneFigure f = (ContentPaneFigure) getFigure();
		Element node = (Element)getModel();
		f.setTitle(node.getAttribute("id"));
		super.refreshVisuals();
	}
	
}

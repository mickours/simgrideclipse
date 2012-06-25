package simgrideclipseplugin.graphical.parts;

import org.eclipse.draw2d.IFigure;

import simgrideclipseplugin.graphical.figures.ContentPaneFigure;
import simgrideclipseplugin.model.ElementList;

public class PlatformEditPart extends AbstractContainerEditPart {

	@Override
	protected IFigure createFigure() {
		ContentPaneFigure f = (ContentPaneFigure) super.createFigure();
		f.setTitle(ElementList.PLATFORM);
		return f;
	}
}

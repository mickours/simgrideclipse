package simgrideclipseplugin.graphical.figures;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;

public class ASfigure extends Figure {

	public ASfigure() {
		super();
		add((IFigure) new Label());
	}
}

package simgrideclipseplugin.graphical.figures;

import org.eclipse.draw2d.Figure;

public abstract class AbstractElementFigure extends Figure {
	protected Figure selected = null;

	public void setSelected(Figure selected) {
		this.selected = selected;
	}

	public Figure getSelected() {
		return selected;
	}
	

}

package simgrideclipseplugin.graphical.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;

import simgrideclipseplugin.model.ElementList;


public class RouteFigure extends AbstractConnectionFigure{

	public RouteFigure(){
		label.setText(ElementList.ROUTE);
	    setLineStyle(SWT.LINE_DOT);
	    label.setBackgroundColor(ColorConstants.darkBlue);
	    label.setForegroundColor(ColorConstants.white);
	}
}

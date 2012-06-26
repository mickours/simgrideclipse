package simgrideclipseplugin.graphical.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;

import simgrideclipseplugin.model.ElementList;

public class ASrouteFigure extends AbstractConnectionFigure {
	
	public ASrouteFigure(){
		label.setText(ElementList.AS_ROUTE);
	    setLineStyle(SWT.LINE_DASH);
	    label.setBackgroundColor(ColorConstants.darkGreen);
	    label.setForegroundColor(ColorConstants.white);
	}
	
}

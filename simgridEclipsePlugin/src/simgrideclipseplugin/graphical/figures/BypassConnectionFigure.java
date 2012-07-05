package simgrideclipseplugin.graphical.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;

public class BypassConnectionFigure extends AbstractConnectionFigure {
	public BypassConnectionFigure(){
		label.setText("bypass");
	    setLineStyle(SWT.LINE_SOLID);
	    label.setBackgroundColor(ColorConstants.red);
	    label.setForegroundColor(ColorConstants.white);
	}
}

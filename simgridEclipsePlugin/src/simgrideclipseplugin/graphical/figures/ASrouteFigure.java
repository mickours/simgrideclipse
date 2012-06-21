package simgrideclipseplugin.graphical.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MidpointLocator;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.swt.SWT;

public class ASrouteFigure extends PolylineConnection {
	public ASrouteFigure(){
		setLineWidth(2);
	    
		Label label = new Label();
	    label.setText("ASroute");
	    setLineStyle(SWT.LINE_DASH);
	    label.setBackgroundColor(ColorConstants.darkGreen);
	    label.setForegroundColor(ColorConstants.white);
	    label.setOpaque( true );
	    add(label, new MidpointLocator(this, 0));
	}
}

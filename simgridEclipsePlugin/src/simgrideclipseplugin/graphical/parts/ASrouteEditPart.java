package simgrideclipseplugin.graphical.parts;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MidpointLocator;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.swt.SWT;

public class ASrouteEditPart extends ConnectionEditPart {

	@Override
	protected IFigure createFigure() {
		PolylineConnection connection = (PolylineConnection) super.createFigure();
		Label label = new Label();
        label.setText("ASroute");
        connection.setLineStyle(SWT.LINE_DASH);
        label.setBackgroundColor(ColorConstants.green);
        label.setOpaque( true );
        connection.add(label, new MidpointLocator(connection, 0));
        return connection;
	}
	

}

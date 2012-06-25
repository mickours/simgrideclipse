package simgrideclipseplugin.graphical.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MidpointLocator;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.swt.SWT;

import simgrideclipseplugin.model.ElementList;

public class ASrouteFigure extends PolylineConnection {
	
	private PolygonDecoration decoTrg = getArrow();
	private PolygonDecoration decoSrc = getArrow();
	
	public ASrouteFigure(){
		setLineWidth(2);
	    
		Label label = new Label();
	    label.setText(ElementList.AS_ROUTE);
	    setLineStyle(SWT.LINE_DASH);
	    label.setBackgroundColor(ColorConstants.darkGreen);
	    label.setForegroundColor(ColorConstants.white);
	    label.setOpaque( true );
	    add(label, new MidpointLocator(this, 0));
	    //symmetrical by default
	    setTargetDecoration();
	    setSourceDecoration();
	}
	
	public void setTargetDecoration(){
        setTargetDecoration(decoTrg);
	}
	
	public void setSourceDecoration(){
        setSourceDecoration(decoSrc);
	}
	
	public void resetSourceDecoration() {
		setSourceDecoration(null);
		
	}
	
	private PolygonDecoration getArrow(){
		PolygonDecoration deco = new PolygonDecoration();
		deco.setTemplate(PolygonDecoration.TRIANGLE_TIP);
	    deco.setLineWidth(2);
	    return deco;
	}

	
}

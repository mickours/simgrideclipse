package simgrideclipseplugin.graphical.figures;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MidpointLocator;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;


public abstract class AbstractConnectionFigure extends PolylineConnection{
	private PolygonDecoration decoTrg = getArrow();
	private PolygonDecoration decoSrc = getArrow();
	protected Label label = new Label();
	
	public AbstractConnectionFigure(){
		setLineWidth(2);
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

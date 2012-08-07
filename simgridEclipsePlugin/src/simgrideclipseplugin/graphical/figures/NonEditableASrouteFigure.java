package simgrideclipseplugin.graphical.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;

import simgrideclipseplugin.model.ElementList;

public class NonEditableASrouteFigure extends AbstractConnectionFigure {
	
	public NonEditableASrouteFigure(){
		label.setText(ElementList.NON_EDITABLE_AS_ROUTE);
	    setLineStyle(SWT.LINE_DASH);
	    label.setBackgroundColor(ColorConstants.orange);
	    label.setForegroundColor(ColorConstants.white);
	}

//	@Override
//	protected boolean isMouseEventTarget() {		
//		return false;
//	}
	
}

package simgrideclipseplugin.graphical.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.TitleBarBorder;

public class ContentPaneFigure extends SimgridAbstractFigure {

	public ContentPaneFigure(String title){
		super(); 
		setBorder(new TitleBarBorder(title));
		setLayoutManager(new FreeformLayout());
	}
}

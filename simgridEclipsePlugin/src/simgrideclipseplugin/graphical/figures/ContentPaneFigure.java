package simgrideclipseplugin.graphical.figures;

import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.TitleBarBorder;

public class ContentPaneFigure extends FreeformLayer {

	public ContentPaneFigure(String title){
		super(); 
		setBorder(new TitleBarBorder(title));
		setLayoutManager(new FreeformLayout());
	}
}

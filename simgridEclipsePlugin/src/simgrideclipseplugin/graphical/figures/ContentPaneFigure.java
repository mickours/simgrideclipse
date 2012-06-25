package simgrideclipseplugin.graphical.figures;

import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.TitleBarBorder;

public class ContentPaneFigure extends FreeformLayer {
	
	public ContentPaneFigure(){
		super(); 
		setBorder(new TitleBarBorder());
		setLayoutManager(new FreeformLayout());
	}

	public void setTitle(String title) {
		((TitleBarBorder)getBorder()).setLabel(title);
	}
}

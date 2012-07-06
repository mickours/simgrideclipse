package simgrideclipseplugin.graphical.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.TitleBarBorder;

public class ContentPaneFigure extends FreeformLayer {
	
//	protected Label label;
	
	public ContentPaneFigure(){
		super(); 
		setBorder(new TitleBarBorder());
		setLayoutManager(new FreeformLayout());
//		label = new Label();
//		label.setBackgroundColor(ColorConstants.titleBackground);
//		add(label);
	}

	public void setTitle(String title) {
		((TitleBarBorder)getBorder()).setLabel(title);
//		label.setText(title);
	}
}

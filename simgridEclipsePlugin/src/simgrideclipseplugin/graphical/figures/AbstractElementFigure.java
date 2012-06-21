package simgrideclipseplugin.graphical.figures;

import javax.swing.ImageIcon;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.TitleBarBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import simgrideclipseplugin.graphical.SimgridIconProvider;

public abstract class AbstractElementFigure extends Figure {
	
	protected final Label idLabel = new Label();
	protected final ImageFigure icon = new ImageFigure();
	protected final TitleBarBorder titleBorder = new TitleBarBorder();
	
	
	public AbstractElementFigure(String name) {
		super.setLayoutManager(new ToolbarLayout());
		titleBorder.setLabel(name);
		setBorder(titleBorder);
		add(idLabel);
		add(icon);
		icon.setImage(SimgridIconProvider.getIcon(name));
		setBackgroundColor(ColorConstants.menuBackground);
		setOpaque(true);
	}

	public String getId() {
		return idLabel.getText();
	}

	public void setId(String id) {
		//TODO verify if there is no other who has this id
		this.idLabel.setText("id: "+id);
	}
}

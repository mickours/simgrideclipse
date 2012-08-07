package simgrideclipseplugin.graphical.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.TitleBarBorder;
import org.eclipse.draw2d.ToolbarLayout;

import simgrideclipseplugin.graphical.providers.SimgridIconProvider;

public class ElementFigure extends Figure {
	
	protected final Label idLabel = new Label();
	protected final ImageFigure icon = new ImageFigure();
	protected final TitleBarBorder titleBorder = new TitleBarBorder();
	
	
	public ElementFigure(String name) {
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
		this.idLabel.setText("id: "+id);
	}
	public void setIcon(String name)
	{
		icon.setImage(SimgridIconProvider.getIcon(name));
	}
}

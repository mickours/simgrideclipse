package simgrideclipseplugin.graphical.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.TitleBarBorder;
import org.eclipse.draw2d.ToolbarLayout;

import simgrideclipseplugin.graphical.SimgridIconProvider;

public class ASfigure extends AbstractElementFigure {
	
	private final Label idLabel;
	private final Label routingLabel;
	private final Label iconLabel;
	private final IFigure contentPane;
	
	public ASfigure() {
		super.setLayoutManager(new ToolbarLayout());
		idLabel = new Label();
		idLabel.setForegroundColor(ColorConstants.gray);
		iconLabel = new Label();
		iconLabel.setIcon(SimgridIconProvider.getIcon("AS"));
		routingLabel = new Label();
		contentPane = new Figure();
		contentPane.setLayoutManager(new ToolbarLayout());
		TitleBarBorder b = new TitleBarBorder("AS");
		b.setBackgroundColor(ColorConstants.darkGreen);
		setBorder(b);
		add(idLabel);
		add(iconLabel);
		add(routingLabel);
		add(contentPane);
		setBackgroundColor(ColorConstants.menuBackground);
		setOpaque(true);
	}
	

	public ASfigure(ASfigure figure) {
		this();
		setId(figure.getId());
		setRouting(figure.getRouting());
	}


	public IFigure getContentPane() {
		return contentPane;
	}

	public String getId() {
		return idLabel.getText();
	}

	public String getRouting() {
		return routingLabel.getText();
	}

	public void setId(String id) {
		this.idLabel.setText("id: "+id);
	}

	public void setRouting(String routing) {
		this.routingLabel.setText("routing: "+routing);
	}
}

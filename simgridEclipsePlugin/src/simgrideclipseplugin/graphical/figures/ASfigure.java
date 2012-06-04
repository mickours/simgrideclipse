package simgrideclipseplugin.graphical.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.TitleBarBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;

public class ASfigure extends SimgridAbstractFigure {
	
	private final Label idLabel;
	private final Label routingLabel;
	private final IFigure contentPane;

	public ASfigure() {
		super.setLayoutManager(new ToolbarLayout());
		idLabel = new Label();
		idLabel.setForegroundColor(ColorConstants.gray);
		routingLabel = new Label();
		contentPane = new Figure();
		contentPane.setLayoutManager(new ToolbarLayout());
		contentPane.setBorder(new TitleBarBorder("AS"));
		add(idLabel);
		add(routingLabel);
		add(contentPane);
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

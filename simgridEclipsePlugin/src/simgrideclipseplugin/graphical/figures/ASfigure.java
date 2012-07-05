package simgrideclipseplugin.graphical.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Label;

import simgrideclipseplugin.model.ElementList;

public class ASfigure extends ElementFigure {
	
	private final Label routingLabel;
	
	public ASfigure() {
		super(ElementList.AS);
		titleBorder.setBackgroundColor(ColorConstants.darkGreen);
		routingLabel = new Label();
		add(routingLabel);
		
	}

	public String getRouting() {
		return routingLabel.getText();
	}

	public void setRouting(String routing) {
		this.routingLabel.setText("routing: "+routing);
	}
}

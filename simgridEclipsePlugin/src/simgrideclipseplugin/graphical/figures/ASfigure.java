package simgrideclipseplugin.graphical.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.TitleBarBorder;
import org.eclipse.draw2d.ToolbarLayout;

import simgrideclipseplugin.graphical.providers.SimgridIconProvider;
import simgrideclipseplugin.model.ElementList;

public class ASfigure extends ElementFigure {
	
	private final Label routingLabel;
//	private final IFigure contentPane;
	
	public ASfigure() {
		super(ElementList.AS);
		
//		contentPane = new Figure();
//		contentPane.setLayoutManager(new ToolbarLayout());
//		add(contentPane);
		titleBorder.setBackgroundColor(ColorConstants.darkGreen);
		routingLabel = new Label();
		add(routingLabel);
		
	}
	

//	public ASfigure(ASfigure figure) {
//		this();
//		setId(figure.getId());
//		setRouting(figure.getRouting());
//	}


//	public IFigure getContentPane() {
//		return contentPane;
//	}

	public String getRouting() {
		return routingLabel.getText();
	}

	public void setRouting(String routing) {
		this.routingLabel.setText("routing: "+routing);
	}
}

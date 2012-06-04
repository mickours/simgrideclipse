package simgrideclipseplugin.graphical.figures;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.TitleBarBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

public class ASfigure extends SimgridAbstractFigure {
	
	private final Label idLabel;
	private final Label routingLabel;
	private final Label iconLabel;
	private final IFigure contentPane;
	
	private static Image getIcon(){
		URL url = null;
		try {
			url = new URL("platform:/plugin/simgridEclipsePlugin/icons/AS.png");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ImageDescriptor.createFromURL(url).createImage();
	}
	
	public ASfigure() {
		super.setLayoutManager(new ToolbarLayout());
		idLabel = new Label();
		idLabel.setForegroundColor(ColorConstants.gray);
		iconLabel = new Label();
		iconLabel.setIcon(getIcon());
		routingLabel = new Label();
		contentPane = new Figure();
		contentPane.setLayoutManager(new ToolbarLayout());
		TitleBarBorder b = new TitleBarBorder("AS");
		b.setBackgroundColor(ColorConstants.darkGreen);
		contentPane.setBorder(b);
		add(idLabel);
		add(iconLabel);
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

package simgrideclipseplugin.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import simgrideclipseplugin.graphical.providers.SimgridIconProvider;
import simgrideclipseplugin.model.ElementList;
import simgrideclipseplugin.wizards.composites.ElementSelectionList;

public class GatewaySelectionPage extends WizardPage {

	protected GatewaySelectionPage() {
		super("Gateway selection","Gateway selection",
				SimgridIconProvider.getIconImageDescriptor(ElementList.ROUTER) );
		
	}

	@Override
	public void createControl(Composite parent) {
		// TODO to finish
		// create the composite to hold the widgets 
//				Composite composite = new Composite(parent, SWT.NONE);
//			    // create the desired layout for this wizard page
//				GridLayout gl = new GridLayout();
//			    int ncol = 4;
//			    gl.numColumns = ncol;
//			    composite.setLayout(gl);
//			    
//			    //top labels
//			    Label l = new Label (composite, SWT.NONE);
//			    l.setText("the new route");
//			    GridData gd = new GridData(GridData.FILL_HORIZONTAL);
//				gd.horizontalSpan = (ncol/2)+1;
//			    l.setLayoutData(gd);
//			    
//			    l = new Label (composite, SWT.NONE);
//			    l.setText("available links");
//			    gd = new GridData(GridData.FILL_HORIZONTAL);
//				gd.horizontalSpan = (ncol/2)-1;
//			    l.setLayoutData(gd);
//			    
//			    //left list 
//			    routeViewer = new ElementSelectionList(composite);
//			    routeViewer.setInput(routeList);
//			    GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
//				int listHeight = (routeViewer.getItemHeight() * 6) +routeViewer.getItemHeight()/2;
//				int listWidth = 300;
//				routeViewer.getTable().setSize(listWidth, listHeight);
//				gridData.heightHint = listHeight;
//				gridData.widthHint = listWidth;
//				routeViewer.getTable().setLayoutData(gridData);
//			    
//				//middle buttons
//			    toLeft = new Button(composite, SWT.ARROW | SWT.LEFT);
//			    toLeft.addListener(SWT.Selection, this);
//			    toRight = new Button(composite, SWT.ARROW | SWT.RIGHT);
//			    toRight.addListener(SWT.Selection, this);
//			    
//			    //right list
//			    availableLinkViewer = new ElementSelectionList(composite);
//			    availableLinkViewer.setInput(availableLinks);
//			    gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
//				availableLinkViewer.getTable().setSize(listWidth, listHeight);
//				gridData.heightHint = listHeight;
//				gridData.widthHint = listWidth;
//				availableLinkViewer.getTable().setLayoutData(gridData);

	}

}

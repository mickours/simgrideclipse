package simgrideclipseplugin.wizards;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.w3c.dom.Element;

import simgrideclipseplugin.graphical.providers.SimgridIconProvider;
import simgrideclipseplugin.model.ElementList;
import simgrideclipseplugin.wizards.composites.ElementSelectionList;

public class LinkSelectionPage extends WizardPage implements Listener {

	private ElementSelectionList routeViewer;
	private ElementSelectionList availableLinkViewer;
	private List<Element> availableLinks;
	private LinkedList<Element> routeList;
	private Button toLeft;
	private Button toRight;
	private Button up;
	private Button down;
//	private Button plus;

	public LinkSelectionPage(java.util.List<Element> list) {
		super("Route editing", "Route editing", SimgridIconProvider.getIconImageDescriptor(ElementList.LINK_CTN));
		availableLinks = list;
		routeList = new LinkedList<Element>();
	}

	@Override
	public void createControl(Composite parent) {
		// create the composite to hold the widgets 
		Composite composite = new Composite(parent, SWT.NONE);
	    // create the desired layout for this wizard page
		GridLayout gl = new GridLayout();
	    int ncol = 4;
	    gl.numColumns = ncol;
	    composite.setLayout(gl);
	    
	    //top labels
	    Label l = new Label (composite, SWT.NONE);
	    l.setText("the new route");
	    GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = (ncol/2)+1;
	    l.setLayoutData(gd);
	    
	    l = new Label (composite, SWT.NONE);
	    l.setText("available links");
	    gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = (ncol/2)-1;
	    l.setLayoutData(gd);
	    
	    //left list 
	    routeViewer = new ElementSelectionList(composite);
	    routeViewer.setInput(routeList);
	    GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
		int listHeight = (routeViewer.getItemHeight() * 6) +routeViewer.getItemHeight()/2;
		int listWidth = 300;
		routeViewer.getTable().setSize(listWidth, listHeight);
		gridData.heightHint = listHeight;
		gridData.widthHint = listWidth;
		routeViewer.getTable().setLayoutData(gridData);
	    
		//middle buttons
	    toLeft = new Button(composite, SWT.ARROW | SWT.LEFT);
	    toLeft.addListener(SWT.Selection, this);
	    toRight = new Button(composite, SWT.ARROW | SWT.RIGHT);
	    toRight.addListener(SWT.Selection, this);
	    
	    //right list
	    availableLinkViewer = new ElementSelectionList(composite);
	    availableLinkViewer.setInput(availableLinks);
	    gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
		availableLinkViewer.getTable().setSize(listWidth, listHeight);
		gridData.heightHint = listHeight;
		gridData.widthHint = listWidth;
		availableLinkViewer.getTable().setLayoutData(gridData);
	    
		//down buttons
		Composite c = new Composite(composite, SWT.NONE);
		c.setLayout(new GridLayout(2,true));
		up =  new Button(c, SWT.ARROW | SWT.UP);
		up.addListener(SWT.Selection, this);
	    down = new Button(c, SWT.ARROW | SWT.DOWN);
	    down.addListener(SWT.Selection, this);
	    gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = (ncol/2)+1;
	    c.setLayoutData(gd);
	    
	    //TODO add a new link button
//	    c = new Composite(composite, SWT.NONE);
//		c.setLayout(new GridLayout(2,true));
//		plus =  new Button(c, SWT.PUSH);
//		plus.setImage(PlatformUI.getWorkbench().getSharedImages().
//				getImageDescriptor(ISharedImages.IMG_OBJ_ADD).createImage());
//		plus.addListener(SWT.Selection, this);
//	    gd = new GridData(GridData.FILL_HORIZONTAL);
//		gd.horizontalSpan = (ncol/2)-1;
//	    l.setLayoutData(gd);
	    
	    parent.pack();
	    setControl(composite);
	    setPageComplete(false);
	    if (availableLinks.isEmpty()){
	    	setErrorMessage("you must have at least one link in this container or in his descendants");
	    }
	}

	@Override
	public void handleEvent(Event event) {
//		if (event.widget == plus){
//			
//		}
		if (event.widget == toLeft) {
			IStructuredSelection sel = (StructuredSelection) availableLinkViewer
					.getSelection();
			if (!sel.isEmpty()) {
				Element e = (Element) sel.getFirstElement();
				routeList.addLast(e);
				availableLinks.remove(e);
				updateUI();
				routeViewer.setSelection(new StructuredSelection(e));
			}
		} else if (!routeViewer.getSelection().isEmpty()) {
			IStructuredSelection sel = (StructuredSelection) routeViewer
					.getSelection();
			Element e = (Element) sel.getFirstElement();
			if (event.widget == toRight) {
				routeList.remove(e);
				availableLinks.add(e);
				updateUI();
				availableLinkViewer.setSelection(new StructuredSelection(e));
			} else if (event.widget == up && routeList.indexOf(e) > 0) {
				int ind = routeList.indexOf(e);
				routeList.remove(ind);
				routeList.add(ind-1, e);
				updateUI();
				
			} else if (event.widget == down && routeList.indexOf(e) < routeList.size()-1) {
				int ind = routeList.indexOf(e);
				routeList.remove(ind);
				routeList.add(ind+1, e);
				updateUI();
			}
		}
		((CreateElementWizard)getWizard()).linkList = routeList;
		
		boolean isComplete = routeList.size() > 0;
		if(isComplete){
			setErrorMessage(null);
		}
		else{
			setErrorMessage("Your route must contains at least one link");
		}
		setPageComplete(isComplete);
	}
	
	private void updateUI(){
		//update UI
		routeViewer.setInput(routeList);
		availableLinkViewer.setInput(availableLinks);
	}

}

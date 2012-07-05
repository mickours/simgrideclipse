package simgrideclipseplugin.wizards;

import java.util.List;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.w3c.dom.Element;

import simgrideclipseplugin.graphical.providers.SimgridIconProvider;
import simgrideclipseplugin.model.ElementList;
import simgrideclipseplugin.wizards.composites.ElementSelectionList;

public class GatewaySelectionPage extends WizardPage implements ISelectionChangedListener {

	private ElementSelectionList srcViewer;
	private List<Element> srcList;
	private ElementSelectionList dstViewer;
	private List<Element> dstList;
	

	protected GatewaySelectionPage(List<Element> srcList, List<Element> dstList) {
		super("Gateway selection","Gateway selection",
				SimgridIconProvider.getIconImageDescriptor(ElementList.ROUTER) );
		this.srcList = srcList;
		this.dstList = dstList;
	}

	@Override
	public void createControl(Composite parent) {
		// create the composite to hold the widgets 
		Composite composite = new Composite(parent, SWT.NONE);
	    // create the desired layout for this wizard page
		GridLayout gl = new GridLayout();
	    int ncol = 2;
	    gl.numColumns = ncol;
	    composite.setLayout(gl);
	    
	    //top labels
	    Label l = new Label (composite, SWT.NONE);
	    l.setText("Select the gateway for the source");
	    GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = (ncol/2);
	    l.setLayoutData(gd);
	    
	    l = new Label (composite, SWT.NONE);
	    l.setText("Select the gateway for the destination");
	    gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = (ncol/2);
	    l.setLayoutData(gd);
	    
	    //left list 
	    srcViewer = new ElementSelectionList(composite);
	    srcViewer.setInput(srcList);
	    GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
		int listHeight = (srcViewer.getItemHeight() * 6) +srcViewer.getItemHeight()/2;
		int listWidth = 300;
		srcViewer.getTable().setSize(listWidth, listHeight);
		gridData.heightHint = listHeight;
		gridData.widthHint = listWidth;
		srcViewer.getTable().setLayoutData(gridData);
		srcViewer.addPostSelectionChangedListener(this);
	    
	    //right list
	    dstViewer = new ElementSelectionList(composite);
	    dstViewer.setInput(dstList);
	    gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
		dstViewer.getTable().setSize(listWidth, listHeight);
		gridData.heightHint = listHeight;
		gridData.widthHint = listWidth;
		dstViewer.getTable().setLayoutData(gridData);
		dstViewer.addPostSelectionChangedListener(this);

		setControl(composite);
		
		if(srcList.size() >= 1){
			srcViewer.setSelection(new StructuredSelection((srcList).get(0)));
		}
		if (dstList.size() >= 1){
			dstViewer.setSelection(new StructuredSelection((dstList).get(0)));
		}
		updateUI();
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		IStructuredSelection sel = (StructuredSelection) event.getSelection();
		CreateElementWizard wizard = (CreateElementWizard)getWizard();
		if (event.getSource() == srcViewer){
			wizard.selectedSrcGw = (Element) sel.getFirstElement();
		}
		else if (event.getSource() == dstViewer){
			wizard.selectedDstGw = (Element) sel.getFirstElement();
		}
		updateUI();
	}
	
	private void updateUI(){
		CreateElementWizard wizard = (CreateElementWizard)getWizard();
		boolean isComplete = wizard.selectedSrcGw != null && wizard.selectedDstGw != null;

		if(isComplete){
			setErrorMessage(null);
		}
		else{
			setErrorMessage("You must select a router for the source and the destination gateways");
		}
		setPageComplete(isComplete);
	}

}

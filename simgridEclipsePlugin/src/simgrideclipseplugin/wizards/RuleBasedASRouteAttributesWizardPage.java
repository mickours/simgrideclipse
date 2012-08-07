package simgrideclipseplugin.wizards;

import java.util.List;
import java.util.Map.Entry;
import java.util.Vector;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.w3c.dom.Element;

import simgrideclipseplugin.graphical.parts.RuleBasedASRouteEditPart;
import simgrideclipseplugin.model.ElementList;
import simgrideclipseplugin.wizards.composites.ElementSelectionList;

public class RuleBasedASRouteAttributesWizardPage extends RuleBasedAttributeFieldFormPage {
 
	ElementSelectionList listViewer;
	Text logs;
	private RuleBasedASRouteEditPart editPart;
	protected RuleBasedASRouteAttributesWizardPage(RuleBasedASRouteEditPart editPart) {
		super(ElementList.AS_ROUTE);
		this.editPart = editPart;
		
		
	}

	@Override
	public void createControl(Composite parent) {		
		// create the composite to hold the widgets 
				Composite wholePage = new Composite(parent, SWT.NULL);
				// create the desired layout for this wizard page
				GridLayout gl = new GridLayout();
			    int ncol = 2;
			    gl.numColumns = ncol;
			    wholePage.setLayout(gl);
			    wholePage.setSize(SWT.DEFAULT, 600);
			    			    
			    // Fields 
				Composite composite = new Composite(wholePage, SWT.NULL);				
			    // create the desired layout for this wizard page
				gl = new GridLayout();
			    ncol = 2;
			    gl.numColumns = ncol;
			    composite.setLayout(gl);
			    
//			    Label l = new Label (composite, SWT.RIGHT);
//			    l.setText("set as default");
//			    GridData gd = new GridData(GridData.FILL_HORIZONTAL);
//				gd.horizontalSpan = ncol;
//			    l.setLayoutData(gd);

				//create FIELDs
				List<String> attrList = ElementList.getAttributesList(tagName);
				
				//add others
				for (String field: attrList){
						addField(field, composite);					
				}				
				
				Composite clist = new Composite(wholePage, SWT.NONE);
				
				Label l = new Label (clist, SWT.NONE);
			    l.setText("Matching resources");
			    GridData gd = new GridData(GridData.FILL_HORIZONTAL);
				//gd.horizontalSpan = 2;
			    l.setLayoutData(gd);
			   // The list on the right to show matching resources
				listViewer = new ElementSelectionList(clist);	
				int listHeight = (listViewer.getItemHeight() * 6) +listViewer.getItemHeight()/2;
				int listWidth = 300;
				
				GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
				listViewer.getTable().setSize(listWidth, listHeight);
				gridData.heightHint = listHeight;
				gridData.widthHint = listWidth;
				listViewer.getTable().setLayoutData(gridData);
				// showing logs.
				 //top label
			    l = new Label (wholePage, SWT.NONE);
			    l.setText("Logs");
			    gd = new GridData(GridData.FILL_HORIZONTAL);
				gd.horizontalSpan = 2;
			    l.setLayoutData(gd);
				
				logs = new Text(wholePage, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
				logs.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));			
				gd = new GridData(GridData.FILL_HORIZONTAL);
				//ugly setting here
				gd.heightHint = 200;
				gd.horizontalSpan = 2;
				logs.setLayoutData(gd);
				
				logs.setEditable(false);
				editPart = ((RuleBasedASRouteWizard) getWizard()).getEditPart();
				
				logs.setText(editPart.logText());

				// Setting control
			    setControl(wholePage);

				//setPageComplete(computeErrors() == null);
				parent.pack();
	}	
	
	
	@Override
	public boolean canFlipToNextPage() {
		// TODO Auto-generated method stub
		return true;
	}

	protected void addField(String fieldName, Composite container){
		
		new Label (container, SWT.NONE).setText(fieldName+":");
		List<String> valList = ElementList.getValueList(tagName, fieldName);
		
		AbstractElementWizard wizard = (AbstractElementWizard) getWizard();
		String defVal = wizard.attrMap.get(fieldName);
		
		Control ctr;
		if (valList.isEmpty()){
			//it's a text field
			Text text = new Text(container,SWT.BORDER);
			text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			//set default value
			if (defVal != null){
				text.setText(defVal);
			}
			text.addListener(SWT.FocusIn, this);
			ctr = text;
			text.addListener(SWT.Modify, this);

			

		}
		else{
			//it's a combo box
			Combo cmb = new Combo(container, SWT.BORDER | SWT.READ_ONLY);
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalAlignment = GridData.BEGINNING;
			cmb.setLayoutData(gd);
			cmb.setItems(valList.toArray(new String[0]));
			//set default value
			if (defVal != null){
				cmb.setText(defVal);
			}
			cmb.addListener(SWT.Selection, this);
			ctr = cmb;
		}
		fieldMap.put(fieldName,ctr);			
	}

	@Override
	public void handleEvent(Event event) {		
		super.handleEvent(event);
		List<Element> eltList = null;
		AbstractElementWizard wizard = (AbstractElementWizard) getWizard();
		String src = wizard.attrMap.get("src");
		String dst = wizard.attrMap.get("dst");
		String gw_src = wizard.attrMap.get("gw_src");
		String gw_dst = wizard.attrMap.get("gw_dst");
		for (Entry<String, Control> entry : fieldMap.entrySet()){
			Control ctl = entry.getValue();
			if (event.widget.equals(ctl)){	
				
				if (entry.getKey().equals("src") || entry.getKey().equals("dst")) {					
					String regexp = wizard.attrMap.get(entry.getKey());
					eltList = editPart.getMatchingASList(regexp);
					} else 
					if (entry.getKey().equals("gw_src"))						
					{
					eltList = editPart.getMatchingGateways(src, dst, gw_src, gw_dst, RuleBasedASRouteEditPart.SRC_ONLY);					
					} else if (entry.getKey().equals("gw_dst"))
					{
						eltList = editPart.getMatchingGateways(src, dst, gw_src, gw_dst, RuleBasedASRouteEditPart.DST_ONLY);
					}
			}
		}
		logs.setText(editPart.logText());
		listViewer.setInput(eltList);
		
	}
  
}

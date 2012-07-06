package simgrideclipseplugin.wizards;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import simgrideclipseplugin.graphical.providers.SimgridIconProvider;
import simgrideclipseplugin.model.ElementList;
import simgrideclipseplugin.model.ModelHelper;

public class AttributeFieldFormPage extends WizardPage implements Listener {
	private String tagName;
	private Map<String, Control> fieldMap;
	private Map<String, Button> defaultMap;
	
	public AttributeFieldFormPage(String tagName) {
		super(tagName, tagName + " creation", SimgridIconProvider.getIconImageDescriptor(tagName));
		this.tagName = tagName;
		fieldMap = new HashMap<String, Control>();
		defaultMap = new HashMap<String, Button>();
	}

	@Override
	public void createControl(Composite parent) {
		// create the composite to hold the widgets 
		ScrolledComposite sc = new ScrolledComposite(parent, SWT.V_SCROLL | SWT.BORDER);
		sc.setExpandHorizontal(true);
	    sc.setExpandVertical(true);
		Composite composite = new Composite(sc, SWT.FILL);
		sc.setContent(composite);
	    // create the desired layout for this wizard page
		GridLayout gl = new GridLayout();
	    int ncol = 3;
	    gl.numColumns = ncol;
	    composite.setLayout(gl);
	    
//	    Label l = new Label (composite, SWT.RIGHT);
//	    l.setText("set as default");
//	    GridData gd = new GridData(GridData.FILL_HORIZONTAL);
//		gd.horizontalSpan = ncol;
//	    l.setLayoutData(gd);

		//create FIELDs
		List<String> attrList = ElementList.getAttributesList(tagName);
		//add required fields
		for (String field: attrList){
			if (ElementList.isRequieredField(tagName, field)){
				addField(field, composite);
			}
		}
		createLine(composite, ncol);
		//add others
		for (String field: attrList){
			if (!ElementList.isRequieredField(tagName, field)){
				addField(field, composite);
			}
		}
		sc.setMinSize(composite.computeSize(500, SWT.DEFAULT));
		setControl(sc);
		updateData();
		setPageComplete(false);
	}

	private void updateData(){
		//save data
		CreateElementWizard wizard = (CreateElementWizard) getWizard();
		for (String field : fieldMap.keySet()){
			String value = getField(field);
			wizard.attrMap.put(field,value);
			if (isDefault(field)){
				ElementList.setDefaultValue(tagName, field, value);
			}
		}
	}

	private void addField(String fieldName, Composite container){
		
		new Label (container, SWT.NONE).setText(fieldName+":");
		List<String> valList = ElementList.getValueList(tagName, fieldName);
		
		CreateElementWizard wizard = (CreateElementWizard) getWizard();
		String defVal = wizard.attrMap.get(fieldName);
		if (defVal == null){
			defVal =  ElementList.getDefaultValue(tagName, fieldName);
		}
		Control ctr;
		if (valList.isEmpty()){
			//it's a text field
			Text text = new Text(container,SWT.BORDER);
			text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			//set default value
			if (defVal != null){
				text.setText(defVal);
			}
			text.addListener(SWT.KeyUp, this);
			ctr = text;

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
		Button button = new Button(container, SWT.CHECK);
		button.setText("set as default");
		button.addListener(SWT.Selection, this);
		defaultMap.put(fieldName, button);		
	}
	
	private void createLine(Composite parent, int ncol) 
	{
		Label line = new Label(parent, SWT.SEPARATOR|SWT.HORIZONTAL|SWT.BOLD);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = ncol;
		line.setLayoutData(gridData);
	}	
	
	public boolean isDefault(String tag){
		return defaultMap.get(tag).getSelection();
	}
	
	/**
	 * @see Listener#handleEvent(Event)
	 */
	public void handleEvent(Event event) {
	    String error = null;
		for (String field : fieldMap.keySet()){
			//show error if required fields are not set
			if (getField(field).isEmpty() && ElementList.isRequieredField(tagName, field)){
				if (error == null){
					error = "";
				}
				error += ("The fields \""+field+"\" must be set\n");
			}
			//show error if the id is not unique
			if (field.equals("id") && !ModelHelper.isUniqueId(getField(field),tagName)){
				if (error == null){
					error = "";
				}
				error += ("The id is not unique\n");
			}
		}
		setErrorMessage(error);
		setPageComplete(error == null);
		updateData();
	}
	
	private String getField(String fieldName){
		Control c = fieldMap.get(fieldName);
		if (c instanceof Combo){
			Combo combo = (Combo)c;
			return combo.getItem(combo.getSelectionIndex());
		}
		else{ //(c instanceof Text){
			return ((Text)c).getText();
		}
	}

}

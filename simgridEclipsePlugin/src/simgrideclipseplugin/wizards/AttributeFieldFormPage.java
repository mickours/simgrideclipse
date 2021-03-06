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
	protected String tagName;
	protected Map<String, Control> fieldMap;
	private Map<String, Button> defaultMap;
	private String oldId;
	
	public AttributeFieldFormPage(String tagName) {
		super(tagName, tagName + " creation", SimgridIconProvider.getIconImageDescriptor(tagName));
		this.tagName = tagName;
		fieldMap = new HashMap<String, Control>();
		defaultMap = new HashMap<String, Button>();
	}
	
	@Override
	public boolean canFlipToNextPage() {
		//little trick to make the cluster wizard wors
		return false;
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
			if (ElementList.isRequiredField(tagName, field)){
				addField(field, composite);
			}
		}
		createLine(composite, ncol);
		//add others
		for (String field: attrList){
			if (!ElementList.isRequiredField(tagName, field)){
				addField(field, composite);
			}
		}
		sc.setMinSize(composite.computeSize(500, SWT.DEFAULT));
		setControl(sc);
		AbstractElementWizard wizard = (AbstractElementWizard) getWizard();
		oldId = wizard.attrMap.get("id");
		updateData();
		setPageComplete(computeErrors() == null);
	}

	protected void updateData(){
		//save data
		AbstractElementWizard wizard = (AbstractElementWizard) getWizard();
		for (String field : fieldMap.keySet()){
			String value = getField(field);
			wizard.attrMap.put(field,value);
			if (isDefault(field)){
				ElementList.setDefaultValue(tagName, field, value);
			}
		}
	}

	protected void addField(String fieldName, Composite container){
		
		new Label (container, SWT.NONE).setText(fieldName+":");
		List<String> valList = ElementList.getValueList(tagName, fieldName);
		
		AbstractElementWizard wizard = (AbstractElementWizard) getWizard();
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
		if (defaultMap.get(tag) != null)			
		{
		return defaultMap.get(tag).getSelection();
		}
		return false;
	}
	
	/**
	 * @see Listener#handleEvent(Event)
	 */
	@Override
	public void handleEvent(Event event) {
	    String error = computeErrors();
		setErrorMessage(error);
		setPageComplete(error == null);
		updateData();
	}
	
	protected String computeErrors(){
		String error = "";
		for (String field : fieldMap.keySet()){
			//show error if required fields are not set
			if (getField(field).isEmpty() && ElementList.isRequiredField(tagName, field)){
				error += ("The fields \""+field+"\" must be set\n");
			}
			//show error if the id is not unique

			if (field.equals("id") && !ModelHelper.isUniqueId(getField(field),oldId,tagName)){
				error += ("This id already exists\n");
			}
		}
		return (error.isEmpty()) ? null: error;
	}
	
	protected String getField(String fieldName){
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

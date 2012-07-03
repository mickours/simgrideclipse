package simgrideclipseplugin.wizards;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import simgrideclipseplugin.graphical.SimgridIconProvider;
import simgrideclipseplugin.model.ElementList;

public class AttributeFieldFormPage extends WizardPage implements Listener {
	private String tagName;
	private Map<String, Text> fieldMap;
	private Map<String, Button> defaultMap;
	
	public AttributeFieldFormPage(String tagName) {
		super(tagName, tagName + " creation", SimgridIconProvider.getIconImageDescriptor(tagName));
		this.tagName = tagName;
		fieldMap = new HashMap<String, Text>();
		defaultMap = new HashMap<String, Button>();
	}
	
	public void init(){
		
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
			String value = fieldMap.get(field).getText();
			wizard.attrMap.put(field,value);
			if (isDefault(field)){
				ElementList.setDefaultValue(tagName, field, value);
			}
		}
	}

	private void addField(String fieldName, Composite container){
		
		new Label (container, SWT.NONE).setText(fieldName+":");
		Text text = new Text(container,SWT.BORDER);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		//set default value
		String val = ElementList.getDefaultValue(tagName, fieldName);
		if (val != null){
			text.setText(val);
		}
		text.addListener(SWT.KeyUp, this);
		fieldMap.put(fieldName,text);
		
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
	
	public Map<String, Text> getFieldMap(){
		return fieldMap;
	}
	
	public boolean isDefault(String tag){
		return defaultMap.get(tag).getSelection();
	}
	
	/**
	 * @see Listener#handleEvent(Event)
	 */
	public void handleEvent(Event event) {
	    // Initialize a variable with the no error status
//	    Status status = new Status(IStatus.OK, "not_used", 0, "", null);
	    String error = null;
		for (String field : fieldMap.keySet()){
			if (fieldMap.get(field).getText().isEmpty() && ElementList.isRequieredField(tagName, field)){
				if (error == null){
					error = "";
				}
				error += ("The fields \""+field+"\" must be set\n");
			}
		}
		setErrorMessage(error);
		setPageComplete(error == null);
		updateData();
	}

}

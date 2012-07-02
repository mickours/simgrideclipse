package simgrideclipseplugin.wizards;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.ManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import simgrideclipseplugin.graphical.SimgridIconProvider;
import simgrideclipseplugin.model.ElementList;

public class AttributeFieldFormPage extends WizardPage {
	private String tagName;
	private Map<String, Text> fieldMap;
	private FormToolkit toolkit;
	
	public AttributeFieldFormPage(String tagName) {
		super(tagName, tagName + "creation", SimgridIconProvider.getIconImageDescriptor(tagName));
		this.tagName = tagName;
		fieldMap = new HashMap<String, Text>();
	}

	@Override
	public void createControl(Composite parent) {
		//init form
		toolkit = new FormToolkit(parent.getDisplay());
		ScrolledForm sform = toolkit.createScrolledForm(parent);
		sform.setLayoutData(new GridData(GridData.FILL_BOTH));
		ManagedForm mform = new ManagedForm(toolkit, sform);
		ScrolledForm form = mform.getForm();
		
		GridLayout layout = new GridLayout();
		form.getBody().setLayout(layout);

		//FIELDs
		Map<String, Boolean> attrMap = ElementList.getAttributesList(tagName);
		//add required fields
		for (String field: attrMap.keySet()){
			if (attrMap.get(field)){
				addField(field, form.getBody());
			}
		}		

	}
	
	private void addField(String fieldName, Composite container){
		
		toolkit.createLabel(container, fieldName+":");
		Text text = toolkit.createText(container, ElementList.getDefaultValue(tagName,fieldName));
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Button button = toolkit.createButton(container, "set as default", SWT.CHECK);
		GridData gd = new GridData();
		gd.verticalSpan = 2;
		button.setLayoutData(gd);
		fieldMap.put(fieldName,text);
	}
	
	public Map<String, Text> getFieldMap(){
		return fieldMap;
	}

}

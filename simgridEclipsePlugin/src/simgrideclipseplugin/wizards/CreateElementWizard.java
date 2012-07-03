package simgrideclipseplugin.wizards;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;

import simgrideclipseplugin.model.ElementList;

public class CreateElementWizard extends Wizard {

	private String tagName;
	private AttributeFieldFormPage page;
	public Map<String, String> attrMap;
	
	public CreateElementWizard(String tagName ) {
		super();
		this.tagName = tagName;
		this.attrMap = new HashMap<String, String>(); 
	}

	@Override
	public void addPages() {
		page = new AttributeFieldFormPage(tagName);
		addPage(page);
	}
	

	@Override
	public boolean performFinish() {
//		boolean isOK = true;
//		for (String field : page.getFieldMap().keySet()){
//			String value = page.getFieldMap().get(field).getText();
//			attrMap.put(field,value);
//			if (page.isDefault(field)){
//				ElementList.setDefaultValue(tagName, field, value);
//			}
//		}
//		return isOK;
		return true;
	}
	
	
}

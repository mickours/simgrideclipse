package simgrideclipseplugin.wizards;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.Page;

public class CreateElementWizard extends Wizard {

	private String tagName;
	private AttributeFieldFormPage page;
	private Map<String, Text> attrMap;
	
	public CreateElementWizard(String tagName) {
		super();
		this.tagName = tagName;
		attrMap = new HashMap<String, Text>();
	}

	@Override
	public void addPages() {
		AttributeFieldFormPage page = new AttributeFieldFormPage(tagName);
		addPage(page);
	}

	@Override
	public boolean performFinish() {
		boolean isOK = true;
		for (String field : page.getFieldMap().keySet()){
			attrMap.put(field,page.getFieldMap().get(field));
		}
		return isOK;
	}

}

package simgrideclipseplugin.wizards;

import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.w3c.dom.Element;

import simgrideclipseplugin.model.ModelHelper;
import simgrideclipseplugin.model.SimgridRules;

public class RuleBasedEditElementWizard extends EditElementWizard {

	public RuleBasedEditElementWizard(Element toEditElement) {
		super(toEditElement);

	}

	@Override
	public void addPages() {
		fieldPage = new RuleBasedAttributeFieldFormPage(tagName);
		addPage(fieldPage);
	}

}

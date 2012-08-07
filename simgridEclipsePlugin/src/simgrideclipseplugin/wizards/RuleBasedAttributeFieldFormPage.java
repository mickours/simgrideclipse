package simgrideclipseplugin.wizards;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.swt.widgets.Control;

public class RuleBasedAttributeFieldFormPage extends AttributeFieldFormPage {

	public RuleBasedAttributeFieldFormPage(String tagName) {
		super(tagName);
		// TODO Auto-generated constructor stub
	}
			@Override
			protected String computeErrors() {				
				for (String field : fieldMap.keySet()){				
				try {
					Pattern.compile(this.getField(field));
				} catch (PatternSyntaxException e) {
					return "Syntax error in regexp " + e.getMessage();
				}
				}
				return null;
			}
	

}

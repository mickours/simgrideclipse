package simgrideclipseplugin.graphical;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.FormDialog;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ScrolledForm;

public class CreateElementFormDialog extends FormDialog {
	
	private Map<String, Text> fieldMap;
	private Label errorLabel;
	private Map<String,String> attrMap;

	/**
	 * create a form dialog to get link attribute values.
	 * the attributes values are stored in the attrList that must be instantiated
	 * before this Form
	 * @param shell
	 * @param attrList
	 */
	public CreateElementFormDialog(Shell shell,Map<String, String> attrMap) {
		super(shell);
		this.attrMap = attrMap;
		fieldMap = new HashMap<String, Text>(attrMap.size());
	}
	
	private void addField(String fieldName, Composite container){
		Label label = new Label(container, SWT.NULL);
		label.setText(fieldName+":");
		Text text = new Text(container, SWT.BORDER);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		fieldMap.put(fieldName,text);
	}
	
	private String getField(String fieldName){
		return fieldMap.get(fieldName).getText();
	}
	
	@Override
	protected void createFormContent(IManagedForm mform) {
		super.createFormContent(mform);
		ScrolledForm form = mform.getForm();
		form.setText("Create a link");
		GridLayout layout = new GridLayout();
		form.getBody().setLayout(layout);
		errorLabel = new Label(form.getBody(), SWT.NULL);
		errorLabel.setForeground(ColorConstants.red);
		//FIELDs
		for (String field: attrMap.keySet()){
			addField(field, form.getBody());
		}		
	}

	@Override
	public boolean isHelpAvailable() {
		//TODO maybe add help content
		return false;
	}

	@Override
	protected void okPressed() {
		boolean isOK = true;
		for (String field : fieldMap.keySet()){
			if (getField(field).isEmpty()){
				errorLabel.setText("The fields \"id\" and \"bandwidth\" must be set");
				errorLabel.redraw();
				isOK = false;
			}
			else{
				attrMap.put(field,getField(field) );
			}
		}
		if (isOK){
			super.close();
		}
	}
}

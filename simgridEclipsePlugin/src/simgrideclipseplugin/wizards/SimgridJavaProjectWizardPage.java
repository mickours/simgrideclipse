package simgrideclipseplugin.wizards;

import java.io.File;

import org.eclipse.core.runtime.Path;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class SimgridJavaProjectWizardPage extends SimgridAbstractProjectWizardPage {
	
	private static final String title = "Simgrid MSG Java project";
	private static final String LIB_NAME = "simgrid.jar";
	
	private Text locationText;
	
	public SimgridJavaProjectWizardPage() {
		super(title);
	}
	
	@Override
	protected void addProjectSpecificComposite(Composite container) {
		Label label = new Label(container, SWT.NULL);
		label.setText("&Your SimGrid Java libraries location:");
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		label.setLayoutData(gd);
		
		locationText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		locationText.setLayoutData(gd);
		locationText.addListener(SWT.Modify, this);
		Button button = new Button(container, SWT.PUSH);
		button.setText("Browse...");
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleBrowse();
			}
		});	
	}

	private void handleBrowse() {
		DirectoryDialog dlg = new DirectoryDialog(getShell());

        // Set the initial filter path according
        // to anything they've selected or typed in
        dlg.setFilterPath(locationText.getText());

        // Change the title bar text
        dlg.setText("Select your SimGrid librairies install location");

        // Customizable message displayed in the dialog
        dlg.setMessage("the directory must contains \""+LIB_NAME+"\"");

        // Calling open() will open and run the dialog.
        // It will return the selected directory, or
        // null if user cancels
        String dir = dlg.open();
        if (dir != null) {
          // Set the text box to the new selection
        	locationText.setText(dir);
        	
        }
	}
	
	@Override
	public void handleEvent(Event event) {
		super.handleEvent(event);
		String message = errorMessage;
		if (event.widget == locationText){
			String loc = locationText.getText();
			Path path = new Path(loc+File.separator+LIB_NAME);
			if (loc.isEmpty() || !new File(path.toOSString()).exists()){
				message += "You must select a valid path containing the \""+LIB_NAME+"\" file";
			}
			else{
				argsMap.put(SimgridJavaProjectWizard.LIB_LOCATION, path);
			}
		}
		updateStatus(message);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
	
	
	
}

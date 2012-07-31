package simgrideclipseplugin.wizards;

import java.io.File;
import org.eclipse.cdt.managedbuilder.core.IProjectType;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
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

public class SimgridCProjectWizardPage extends SimgridAbstractProjectWizardPage {

	
	private static final String title = "Simgrid MSG C project";
	private Text locationText;
	private Button is38orMore;
	private static final String LIB_NAME = File.separator+"lib"+File.separator+"libsimgrid";

	
	IProjectType[] projectTypes;

	
	public SimgridCProjectWizardPage() {
		super(title);
	}



	@Override
	public void init(SimgridAbstractProjectWizard wizard){
		try{
			projectTypes = ManagedBuildManager.getDefinedProjectTypes();
			if (projectTypes == null || projectTypes.length == 0){
				wizard.initErrorMessage = "You don't have any tool chain install for C compilation";
			}
			for (int i = 0; i< projectTypes.length; i++){
				if (projectTypes[i].isSupported() && projectTypes[i].getId().equals("cdt.managedbuild.target.gnu.exe")){
					argsMap.put(SimgridCProjectWizard.TOOL_CHAIN,  projectTypes[i].getId());
					return;
				}

			}
		}catch(Exception e){
			wizard.initErrorMessage ="You need to install the C/C++ Developement Tools to create a SimGrid C project";
		}
	}



	@Override
	protected void addProjectSpecificComposite(Composite container) {
		Label label = new Label(container, SWT.NULL);
		label.setText("&Select your SimGrid root install location");
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		label.setLayoutData(gd);
		
		locationText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		locationText.setLayoutData(gd);
		locationText.addListener(SWT.Modify, this);
		Button button = new Button(container, SWT.PUSH);
		button.setText("Browse...");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				handleBrowse();
			}
		});	
		
		is38orMore = new Button(container, SWT.CHECK);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		is38orMore.setLayoutData(gd);
		is38orMore.setText("&Your SimGrid version is 3.8 or later");
		is38orMore.addListener(SWT.Selection, this);
		

	}
	
	@Override
	protected void initializeComposite() {
		argsMap.put(SimgridCProjectWizard.IS_38_OR_MORE,false);
		super.initializeComposite();
		String s = System.getenv("SIMGRID_ROOT");
		if (s != null){
			locationText.setText(s);
			setPageComplete(true);
		}
	}
	
	private void handleBrowse() {
		DirectoryDialog dlg = new DirectoryDialog(getShell());

        // Set the initial filter path according
        // to anything they've selected or typed in
        dlg.setFilterPath(locationText.getText());

        // Change the title bar text
        dlg.setText("Select your SimGrid librairies install location");

        // Customizable message displayed in the dialog
        dlg.setMessage("the directory must contains \""+LIB_NAME+"\" library file");

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
			argsMap.put(SimgridCProjectWizard.SIMGRID_ROOT, loc);
		}
		else if (event.widget == is38orMore){
			argsMap.put(SimgridCProjectWizard.IS_38_OR_MORE,is38orMore.getSelection());
		}
		updateStatus(message);
	}
	
	

}

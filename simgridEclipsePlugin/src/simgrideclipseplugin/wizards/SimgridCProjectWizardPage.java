package simgrideclipseplugin.wizards;

import java.io.File;
import java.util.Arrays;

import org.eclipse.core.runtime.Path;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class SimgridCProjectWizardPage extends SimgridAbstractProjectWizardPage {

	
	private static final String title = "Simgrid MSG C project";
	private Combo cmbToolChain;
	private Text locationText;
	private Button is38orMore;
	private static final String LIB_NAME = "libsimgrid.so";
	
	/**
	 * there is 1:1 correspondence between the to arrays
	 */
	private final static String[] projectIDList = {
		"cdt.managedbuild.target.gnu.cygwin.exe",
		"cdt.managedbuild.target.gnu.mingw.exe", 
		"cdt.managedbuild.target.gnu.solaris.exe", 
		"cdt.managedbuild.target.macosx.exe",
		"cdt.managedbuild.target.gnu.exe", 
	};
	private final static String[] toolChainList = {
		"GNU cygwin",
		"GNU mingw",
		"GNU solaris",
		"macosx",
		"GNU Unix",
	};

	
	public SimgridCProjectWizardPage() {
		super(title);
	}
	
	@Override
	protected void addProjectSpecificComposite(Composite container) {
		Label label = new Label(container, SWT.NULL);
		label.setText("&Select your project toolchain:");
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		label.setLayoutData(gd);
		
		String[] valList = getToolChainList();
		cmbToolChain = new Combo(container, SWT.BORDER | SWT.READ_ONLY);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalAlignment = GridData.BEGINNING;
		cmbToolChain.setLayoutData(gd);
		cmbToolChain.setItems(valList);
		cmbToolChain.addListener(SWT.Selection, this);
		
//		label = new Label(container, SWT.NULL);
//		label.setText("&Select your SimGrid librairies install location");
//		gd = new GridData(GridData.FILL_HORIZONTAL);
//		gd.horizontalSpan = 3;
//		label.setLayoutData(gd);
//		
//		locationText = new Text(container, SWT.BORDER | SWT.SINGLE);
//		gd = new GridData(GridData.FILL_HORIZONTAL);
//		locationText.setLayoutData(gd);
//		locationText.addListener(SWT.Modify, this);
//		Button button = new Button(container, SWT.PUSH);
//		button.setText("Browse...");
//		button.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(SelectionEvent e) {
//				handleBrowse();
//			}
//		});	
		
		is38orMore = new Button(container, SWT.CHECK);
		is38orMore.setText("&Your SimGrid version is 3.8 or later");
		is38orMore.addListener(SWT.Selection, this);
		

	}
	
	

	@Override
	protected void initialize() {
		argsMap.put(SimgridCProjectWizard.IS_38_OR_MORE,false);
		super.initialize();
	}

	private String[] getToolChainList() {
		String os = System.getProperty("os.name");
		if (os.startsWith("windows")){
			String[] ret = {toolChainList[0],toolChainList[1] };
			return ret;
		}
		if (os.startsWith("solaris")){
			String[] ret = {toolChainList[2]};
			return ret;
		}
		if (os.startsWith("mac")){
			String[] ret = {toolChainList[3]};
			return ret;
		}
		//guess it's an UNIX like
		String[] ret = {toolChainList[4]};
		return ret;
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
		if (event.widget == cmbToolChain){
			String tc = cmbToolChain.getText();
			if (tc.isEmpty()){
				message += "You must select a tool chain";
			}
			else{
				int index = Arrays.asList(toolChainList).indexOf(tc);
				argsMap.put(SimgridCProjectWizard.TOOL_CHAIN, projectIDList[index]);
			}
		}
		else if (event.widget == locationText){
			String loc = locationText.getText();
			Path path = new Path(loc+File.separator+LIB_NAME);
			if (loc.isEmpty() || !new File(path.toOSString()).exists()){
				message += "You must select a valid path containing the \""+LIB_NAME+"\" file";
			}
			else{
				argsMap.put(SimgridCProjectWizard.LIB_PATH, loc);
			}
		}
		else if (event.widget == is38orMore){
			argsMap.put(SimgridCProjectWizard.IS_38_OR_MORE,is38orMore.getSelection());
		}
		updateStatus(message);
	}
	
	

}

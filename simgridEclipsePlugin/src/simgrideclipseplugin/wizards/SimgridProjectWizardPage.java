package simgrideclipseplugin.wizards;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public class SimgridProjectWizardPage extends WizardPage implements Listener {

	private LinkedList<Text> funcTextList;
	private Composite funcContainer;
	private Combo cmbLanguage;
	private Button plus;
	private Button moins;
	private final static String[] valList = {"C","Java"};

	protected SimgridProjectWizardPage(String title) {
		super(title);
		setTitle(title);
		funcTextList = new LinkedList<Text>();
	}

	@Override
	public void createControl(Composite parent) {
		//initialize
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 3;
		setControl(container);
		//language
		Label label = new Label(container, SWT.NULL);
		label.setText("&Language:");

		cmbLanguage = new Combo(container, SWT.BORDER | SWT.READ_ONLY);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalAlignment = GridData.BEGINNING;
		cmbLanguage.setLayoutData(gd);
		cmbLanguage.setItems(valList);
		cmbLanguage.addListener(SWT.Selection, this);
		
		//functions
		label = new Label(container, SWT.NULL);
		label.setText("&Add functions:");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		label.setLayoutData(gd);
		
		funcContainer = new Composite(container, SWT.NONE);		
		layout = new GridLayout();
		funcContainer.setLayout(layout);
		
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		funcContainer.setLayoutData(gd);
		
		createFunctionText();
		
		plus =  new Button(container, SWT.PUSH );
		plus.setImage(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_OBJ_ADD).createImage());
		plus.addListener(SWT.Selection, this);
		
		moins =  new Button(container, SWT.PUSH );
		moins.setImage(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_ELCL_REMOVE).createImage());
		moins.addListener(SWT.Selection, this);
		
		initialize();
	}
	
	private void initialize() {
		funcTextList.get(0).setText("defaultFunction");
	}

	private void createFunctionText(){
		Text funcText = new Text(funcContainer, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = getControl().getParent().getSize().x - 40;
		funcText.setLayoutData(gd);
		funcText.addListener(SWT.KeyUp, this);
		funcTextList.add(funcText);
	}
	
	
	private void updateStatus(String message) {
		if (getLanguage().isEmpty()){
			message += "you need to specify a language";
		}
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public List<String> getFunctionNames() {
		List<String> l = new ArrayList<String>(funcTextList.size());
		for (Text func : funcTextList){
			l.add(func.getText());
		}
		return l;
	}
	
	public String getLanguage() {
		return cmbLanguage.getText();
	}

	@Override
	public void handleEvent(Event event) {
		String message = null;
		//TODO handle errors
		if (event.widget == plus){
			createFunctionText();
			int size = funcContainer.getChildren().length;
			if (size > 4){
				getShell().pack();
			}
		}
		else if (event.widget == moins){
			int size = funcContainer.getChildren().length;
			if (size > 1){
				funcTextList.getLast().dispose();
				funcTextList.removeLast();
			}
		}
		getControl().pack();
		updateStatus(message);
	}

}

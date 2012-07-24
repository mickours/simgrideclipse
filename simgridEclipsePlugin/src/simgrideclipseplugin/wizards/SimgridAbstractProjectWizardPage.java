package simgrideclipseplugin.wizards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public abstract class SimgridAbstractProjectWizardPage extends WizardPage implements Listener {

	private LinkedList<Text> funcTextList;
	private Composite funcContainer;
	private Button plus;
	private Map<Button,Text> delButtonMap;
	
	/**
	 * this map is used by the class that extends this to give
	 * arguments to the <code>initializeNewProject</code> methods
	 * in the parent wizard that must be a SimgridProjectWizard
	 * @see SimgridAbstractProjectWizard SimgridProjectWizard
	 */
	protected Map<String, Object> argsMap;
	protected String errorMessage;
	
	public SimgridAbstractProjectWizardPage(String title) {
		super(title);
		setTitle(title);
		funcTextList = new LinkedList<Text>();
		argsMap = new HashMap<String, Object>();
		delButtonMap = new HashMap<Button, Text>();
	}

	@Override
	public void createControl(Composite parent) {
		//initialize
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 3;
		setControl(container);

		addProjectSpecificComposite(container);
		
		//functions
		Label label = new Label(container, SWT.NULL);
		label.setText("&Add functions:");
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		label.setLayoutData(gd);
		
		funcContainer = new Composite(container, SWT.NONE);		
		layout = new GridLayout();
		layout.numColumns = 2;
		funcContainer.setLayout(layout);
		
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		funcContainer.setLayoutData(gd);
		
		createFunctionText();
		
		plus =  new Button(container, SWT.PUSH );
		plus.setImage(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_OBJ_ADD).createImage());
		plus.addListener(SWT.Selection, this);
		
		setPageComplete(false);
		initializeComposite();
	}
	/**
	 * should be override to compute specific data before adding page
	 * errorMessage should be set if an error occurred
	 */
	public abstract void init(SimgridAbstractProjectWizard wizard);
	/**
	 * should be override to add project specific visual settings
	 */
	protected abstract void addProjectSpecificComposite(Composite container);

	/**
	 * should be override to initialize project specific visual settings
	 */
	protected void initializeComposite() {
		funcTextList.get(0).setText("defaultFunction");
	}

	private void createFunctionText(){
		Text funcText = new Text(funcContainer, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL,GridData.FILL,true,true);
//		gd.widthHint = getControl().getParent().getSize().x - 40;
		funcText.setLayoutData(gd);
		funcText.addListener(SWT.KeyUp, this);
		funcTextList.add(funcText);
		
		Button moins =  new Button(funcContainer, SWT.PUSH );
		moins.setImage(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_ELCL_REMOVE).createImage());
		moins.addListener(SWT.Selection, this);
		delButtonMap.put(moins, funcText);
	}
	
	/**
	 * set error message with the given message and update page complete status
	 * if the message is empty or null reset error message and set page complete
	 * @param message
	 */
	protected void updateStatus(String message) {
		message = (message == null || message.isEmpty()) ? null: message;
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

	@Override
	public void handleEvent(Event event) {
		errorMessage = "";
		if (event.widget == plus){
			createFunctionText();
			int size = funcContainer.getChildren().length;
			if (size > 4){
				getShell().pack();
			}
		}
		else if (delButtonMap.containsKey(event.widget)){
			int size = funcContainer.getChildren().length;
			if (size > 1){
				Button b = (Button) event.widget;
				Text t = delButtonMap.get(b);
				funcTextList.remove(t);
				delButtonMap.remove(b);
				b.dispose();
				t.dispose();
			}
		}
		//handle errors
		else if (funcTextList.contains(event.widget)){
			Text text = (Text) event.widget;
			if (text.getText().isEmpty() || !text.getText().matches("[A-Za-z0-9_]+")){
				errorMessage += "the function name must use only letters and numbers";
			}
		}
		getControl().pack();
		updateStatus(errorMessage);
	}

	public Map<String, Object> getArgsMap() {
		return argsMap;
	}

}

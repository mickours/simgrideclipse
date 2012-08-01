package simgrideclipseplugin.wizards;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import simgrideclipseplugin.model.ElementList;

public class ClusterWizardPage extends WizardPage {
	
	private Button simpleCluster;
	private Button advancedCluster;
	private WizardPage wizardPage;
	
	protected ClusterWizardPage() {
		super("Cluster wizard");
	}
	

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout gl = new GridLayout();
		//gl.numColumns = 1;
		container.setLayout(gl);
		
		simpleCluster = new Button(container,SWT.RADIO);
		simpleCluster.setSelection(true);
		simpleCluster.setText("Create a simple cluster");
		
		advancedCluster = new Button(container,SWT.RADIO);
		advancedCluster.setText("Create an advanced cluster");
		setControl(container);
	}
	
	public boolean canFlipToNextPage(){
		return true;
	}

	@Override
	public IWizardPage getNextPage() {
		WizardPage wizardPage;
		if (simpleCluster.getSelection()){
			wizardPage = ((AbstractElementWizard)getWizard()).simplePage;
		}
		else{
			//TODO remove the page if necessary
			wizardPage = ((AbstractElementWizard)getWizard()).advancedPage;
		}
		return wizardPage;
	}
	
	

}

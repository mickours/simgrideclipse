package simgrideclipseplugin.wizards;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import simgrideclipseplugin.graphical.providers.SimgridIconProvider;
import simgrideclipseplugin.model.ElementList;

public class ClusterCreationWizardPage extends WizardPage {
	
	private Button simpleCluster;
	private Button advancedCluster;
	
	protected ClusterCreationWizardPage() {
		super("Cluster wizard");
		setTitle("Cluster creation");
		setDescription("The advanced Cluster allows you to have severals host sets and routers");
		setImageDescriptor(SimgridIconProvider.getIconImageDescriptor(ElementList.CLUSTER));
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
		
		//initialize
		setPageComplete(true);
	}

	@Override
	public IWizardPage getNextPage() {
		WizardPage wizardPage = null;
		if (simpleCluster.getSelection()){
			wizardPage = ((CreateElementWizard)getWizard()).simplePage;
			((CreateElementWizard)getWizard()).advancedPage.setPageComplete(true);
		}
		if (advancedCluster.getSelection()){
			//TODO remove the page if necessary
			wizardPage = ((CreateElementWizard)getWizard()).advancedPage;
			((CreateElementWizard)getWizard()).simplePage.setPageComplete(true);
		}
		if (wizardPage != null){
			((Wizard) getWizard()).addPage(wizardPage);
			wizardPage.setPageComplete(false);
			return wizardPage;
		}
		return null;
	}
}

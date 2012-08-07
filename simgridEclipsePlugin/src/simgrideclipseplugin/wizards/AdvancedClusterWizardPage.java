package simgrideclipseplugin.wizards;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.w3c.dom.Element;

import simgrideclipseplugin.graphical.providers.SimgridIconProvider;
import simgrideclipseplugin.model.ElementList;
import simgrideclipseplugin.model.ModelHelper;
import simgrideclipseplugin.wizards.composites.ElementSelectionList;

public class AdvancedClusterWizardPage extends WizardPage implements Listener {
	
	private ElementSelectionList clusterContentViewer;
	private LinkedList<Element> clusterContentList;
	private String oldId;
	private Text idText;
	private Button addCabinet;
	private Button addRouter;
	private Button addBackbone;
	private Element backbone;
	
	protected AdvancedClusterWizardPage() {
		super("Advanced Cluster wizard");
		setTitle("Advanced Cluster creation");
		setDescription("A Cabinet is a host set.");
		setImageDescriptor(SimgridIconProvider.getIconImageDescriptor(ElementList.AS));
	}
	
	@Override
	public boolean canFlipToNextPage() {
		return false;
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout gl = new GridLayout();
		//gl.numColumns = 2;
		container.setLayout(gl);
		
		Label idLabel = new Label(container, SWT.NULL);
		idLabel.setText("Cluster id:");
		
		
		idText = new Text(container, SWT.BORDER);
		idText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		idText.addListener(SWT.Modify, this );
		
		clusterContentViewer = new ElementSelectionList(container);
		clusterContentViewer.setInput(clusterContentList);
		GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
		clusterContentViewer.getTable().setLayoutData(gridData);
		
		Composite buttonContainer = new Composite(container,SWT.NULL);
		buttonContainer.setLayout(new RowLayout());
		
		addCabinet = new Button(buttonContainer, SWT.PUSH);
		addCabinet.setImage(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_OBJ_ADD).createImage());
		addCabinet.setText("Cabinet");
		addCabinet.addListener(SWT.Selection, this);
		
		addRouter = new Button(buttonContainer, SWT.PUSH);
		addRouter.setImage(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_OBJ_ADD).createImage());
		addRouter.setText("Router");
		addRouter.addListener(SWT.Selection, this);
		
		addBackbone = new Button(buttonContainer, SWT.PUSH);
		addBackbone.setImage(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_OBJ_ADD).createImage());
		addBackbone.setText("Backbone");
		addBackbone.addListener(SWT.Selection, this);
		
		//initialize
		List<Element> l = ((AbstractElementWizard) getWizard()).clusterContent;
		if (l != null && !l.isEmpty()){
			clusterContentList = new LinkedList<Element>(l);
		}
		else{
			clusterContentList = new LinkedList<Element>();
		}
		oldId = ModelHelper.createId(ElementList.AS)+"-cluster";//.replaceFirst(ElementList.AS,"advance-"+ElementList.CLUSTER);
		idText.setText(oldId);
		setControl(container);
		update();
	}
	
	private String computeErrors(){
		String error = "";
		if (!ModelHelper.isUniqueId(idText.getText(),oldId,ElementList.AS)){
			error += ("This id alredy exists\n");
		}
		
		int i=0;
		while (i <clusterContentList.size() &&  !clusterContentList.get(i).getTagName().equals(ElementList.CABINET)){
			i++;
		}
		if (i == clusterContentList.size()){
			error += ("You must have a least one Cabinet in your cluster");
		}
		return (error.isEmpty()) ? null: error;
	}
	
	public void handleEvent(Event event) {
		String elemType = null;
		if (event.widget == addCabinet){	
			elemType = ElementList.CABINET;
		}else if(event.widget == addRouter){
			elemType = ElementList.ROUTER;
		}else if(event.widget == addBackbone){
			if (backbone != null){
				setMessage("You can have only one backbone in a Cluster",WARNING);
			}
			else{
				elemType = ElementList.BACKBONE;
			}
		}
		if (elemType != null){
			CreateElementWizard wizard = new CreateElementWizard(elemType);
			WizardDialog dialog = new WizardDialog(getShell(), wizard);
	        dialog.create();
	    	dialog.open();
	    	if (dialog.getReturnCode()== Window.OK){
	    		Element newElem = ModelHelper.createElement(elemType, wizard.attrMap);
	    		if (elemType.equals(ElementList.BACKBONE)){
	    			clusterContentList.addLast(newElem);
	    			backbone = newElem;
	    		}
	    		else{
	    			clusterContentList.addFirst(newElem);
	    		}
	    	}
		}
		update();
	}

	private void update(){
		//handle completion
		String error = computeErrors();
		setErrorMessage(error);
		setPageComplete(error == null);
		//udate data and ui
		clusterContentViewer.setInput(clusterContentList);
		((AbstractElementWizard) getWizard()).clusterContent = clusterContentList;
		((AbstractElementWizard) getWizard()).clusterId = idText.getText();
	}
		

}

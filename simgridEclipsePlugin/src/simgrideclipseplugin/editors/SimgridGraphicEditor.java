package simgrideclipseplugin.editors;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.MouseWheelHandler;
import org.eclipse.gef.MouseWheelZoomHandler;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite.FlyoutPreferences;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.commands.ActionHandler;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import simgrideclipseplugin.graphical.AutomaticGraphLayoutHelper;
import simgrideclipseplugin.graphical.SimgridPaletteFactory;
import simgrideclipseplugin.graphical.actions.AutoLayoutAction;
import simgrideclipseplugin.graphical.actions.EditASRouting;
import simgrideclipseplugin.graphical.actions.EditElementAction;
import simgrideclipseplugin.graphical.actions.GoIntoAction;
import simgrideclipseplugin.graphical.actions.GoOutAction;
import simgrideclipseplugin.graphical.parts.SimgridEditPartFactory;
import simgrideclipseplugin.model.ElementList;
import simgrideclipseplugin.model.ModelHelper;

@SuppressWarnings("restriction")
public class SimgridGraphicEditor extends GraphicalEditorWithFlyoutPalette {

	// The DOM Model initialized with IFile Input Source
	private IDOMModel model;
	private MultiPageSimgridEditor parent;
	private static PaletteRoot palette;
	private KeyHandler sharedKeyHandler;
	private IPropertySource propertiesSource;
	private GoOutAction goOutAction;
	
	public SimgridGraphicEditor(final MultiPageSimgridEditor parent) {
		super.setEditDomain(new DefaultEditDomain(this));
		this.parent = parent;
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		if (!(input instanceof IFileEditorInput)) {
			throw new PartInitException(
					"Invalid Input: Must be IFileEditorInput");
		}
		super.init(site, input);
		setPartName(input.getName());
	}
	
	

	@Override
	protected void setInput(IEditorInput input) {
		super.setInput(input);
		// Force the load of IDOMMOdel on Editor load
		getDOMModel();
	}

	private IDOMModel getDOMModel() {
		if (model == null) {
			try {
				model = ModelHelper.getDOMModel(getEditorInput());
			} catch (Exception e) {
				throw new RuntimeException("Invalid Input: Must be DOM", e);
			}
		}
		return model;
	}

	@Override
	protected void initializeGraphicalViewer() {
		super.initializeGraphicalViewer();
		initContents();
		
	}
	
	public EditPart getGraphicalContents() {
		return getGraphicalViewer().getContents();
	}
	
	public void setGraphicalContents(Node parentNode) {
		getGraphicalViewer().setContents(parentNode);
	}
	
	public Map<?,?> getEditPartRegistry(){
		return getGraphicalViewer().getEditPartRegistry();
	}
	

	/**
	 * Initialize the view to display the inside of the root AS
	 * @param viewer
	 */
	public void initContents() {
		GraphicalViewer viewer = getGraphicalViewer();
		Element platform = model.getDocument().getDocumentElement();
		if (null != platform){
			int i = 0;
			List<Element> childrens = ModelHelper.getNoConnectionChildren(platform);
			while (i < childrens.size() 
					&& !childrens.get(i).getTagName().equals(ElementList.AS)){
				i++;
			}
			//if an AS was found select it as a container
			if (i < childrens.size()){
				Element rootAs = childrens.get(i);
				viewer.setContents(rootAs);
				//update action state
				goOutAction.update();
			}
			else{
				MessageBox mb = new MessageBox(parent.getSite().getShell());
				mb.setMessage("This file does not seem to be a valid platform description (no root AS)");
				mb.open();
				viewer.setContents(platform);
			}
		}
		else{
			MessageBox mb = new MessageBox(parent.getSite().getShell());
			mb.setMessage("This file does not seem to be a valid platform description (no root platform)");
			mb.open();
			viewer.setContents(null);
		}
		
	}

	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		GraphicalViewer viewer = getGraphicalViewer();
		
		viewer.setEditPartFactory(SimgridEditPartFactory.INSTANCE);
		
		ScalableFreeformRootEditPart rootEditPart = new ScalableFreeformRootEditPart();
		viewer.setRootEditPart(rootEditPart);
		
		//add actions...
		IHandlerService service = (IHandlerService) getSite().getService(
				IHandlerService.class);
		// add zooming support
		List<String> zoomContributions = Arrays.asList(new String[] {
				ZoomManager.FIT_ALL, ZoomManager.FIT_HEIGHT,
				ZoomManager.FIT_WIDTH });
		rootEditPart.getZoomManager().setZoomLevelContributions(
				zoomContributions);
		IAction zoomIn = new ZoomInAction(rootEditPart.getZoomManager());
		IAction zoomOut = new ZoomOutAction(rootEditPart.getZoomManager());
		getActionRegistry().registerAction(zoomIn);
		getActionRegistry().registerAction(zoomOut);
		service.activateHandler(GEFActionConstants.ZOOM_IN, new ActionHandler(zoomIn));
		service.activateHandler(GEFActionConstants.ZOOM_OUT, new ActionHandler(zoomOut));
		// mouse support
		viewer.setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.MOD1),
				MouseWheelZoomHandler.SINGLETON);
		//key support
		GraphicalViewerKeyHandler handler = new GraphicalViewerKeyHandler(viewer);
		handler.setParent(getCommonKeyHandler());
		viewer.setKeyHandler(handler);
		//context menu
		ContextMenuProvider provider = new SimgridContextMenuProvider(viewer, getActionRegistry());
		viewer.setContextMenu(provider);
		getSite().registerContextMenu( 
			      "simgrideclipse.graphical.contextmenu", //$NON-NLS-1$ 
			      provider, getGraphicalViewer());
	}
	
	protected KeyHandler getCommonKeyHandler() { 
		  if (sharedKeyHandler == null) { 
		    sharedKeyHandler = new KeyHandler(); 
		    sharedKeyHandler 
		        .put(KeyStroke.getPressed(SWT.DEL, 127, 0), 
		            getActionRegistry().getAction(ActionFactory.DELETE.getId())); 
		  } 
		  return sharedKeyHandler; 
	}


	public void doAutoLayout() {
		final double zoomMax = 1.20;
		AutomaticGraphLayoutHelper.INSTANCE.init(getGraphicalContents());
		AutomaticGraphLayoutHelper.INSTANCE.computeLayout();
		ScalableFreeformRootEditPart root =(ScalableFreeformRootEditPart)getGraphicalViewer().getRootEditPart();
		root.getZoomManager().setZoomAsText(ZoomManager.FIT_ALL);
		if (root.getZoomManager().getZoom() > zoomMax){
			root.getZoomManager().setZoom(zoomMax);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void createActions() {
		super.createActions();
		
		ActionRegistry ar = getActionRegistry();
		IAction action;
		
		action = new AutoLayoutAction(this);
		ar.registerAction(action);
		
		action = new EditElementAction(this);
		ar.registerAction(action);
		getSelectionActions().add(action.getId());
		
		action = new EditASRouting(this);
		ar.registerAction(action);
		getSelectionActions().add(action.getId());
		
		action = new GoIntoAction(this);
		ar.registerAction(action);
		getSelectionActions().add(action.getId());
		
		goOutAction = new GoOutAction(this);
		ar.registerAction(goOutAction);
		getSelectionActions().add(GoOutAction.ID);
		
	}

	@Override
	public boolean isDirty() {
		return model.isDirty();
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) {
		try {
			model.save();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	 @Override
	 public void dispose() {
	 	super.dispose();
	 }
	
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// If not the active editor, ignore selection changed.
		if (part == parent && parent.getActivePageEditor() == this){			
			//update available action 
			super.updateActions(super.getSelectionActions());
		} 
	}
	
	@Override
	protected PaletteRoot getPaletteRoot() {
		//set palette preference
		FlyoutPreferences pref = getPalettePreferences();
		pref.setPaletteState(FlyoutPaletteComposite.STATE_PINNED_OPEN);
		//initPalette content
		if (palette == null) {
			palette = SimgridPaletteFactory.createPalette();
		}
		return palette;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class type) {
		if (type == ZoomManager.class){
			return getGraphicalViewer().getProperty(
					ZoomManager.class.toString());
		}
		if (type == ActionRegistry.class) {
			return getActionRegistry();
		}
		if (type == IPropertySource.class) {
			return propertiesSource;
			//return ModelHelper.getPropertySource(getSite().getSelectionProvider().getSelection());
		}
		return super.getAdapter(type);
	}

	/**
	 * handle external selection change
	 * @param modelSelection
	 */
	public void externalSelectionChanged(IStructuredSelection modelSelection) {
		 //show the appropriate AS container
		 List<?> selectedList = modelSelection.toList();
		 if (selectedList.isEmpty()){
			 initContents();
		 }
		 else if (selectedList.size() == 1){
			
			 Element selected = (Element) selectedList.get(0);
			 //update properties
			 propertiesSource = ModelHelper.getPropertySource(selected);
			 Element toOpen = selected;
			 while (!(toOpen.getTagName().equals(ElementList.AS) || toOpen.getTagName().equals(ElementList.AS)) && toOpen.getParentNode() instanceof Element){
				 //|| ModelHelper.getChildren(toOpen).isEmpty()){
				 toOpen = (Element) toOpen.getParentNode();
			 }
			 if (getGraphicalContents() == null || !getGraphicalContents().getModel().equals(toOpen)){
				 //avoid selection listener while changing content
				 getSite().setSelectionProvider(null);
				 getGraphicalViewer().setContents(toOpen);
				 getSite().setSelectionProvider(getGraphicalViewer());
				 //update action state
				 goOutAction.update();
			 }
			 //update graphic viewer selection
			 if (ElementList.isDrawable(selected.getTagName())){
				 getSite().getSelectionProvider()
			 		.setSelection(ModelHelper.modelToPartSelection(modelSelection, this));
			 }
		 }
		 
	}


}

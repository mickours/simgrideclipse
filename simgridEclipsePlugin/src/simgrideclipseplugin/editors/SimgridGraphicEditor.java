package simgrideclipseplugin.editors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.commands.ActionHandler;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.w3c.dom.Element;

import simgrideclipseplugin.graphical.AutomaticGraphLayoutHelper;
import simgrideclipseplugin.graphical.SimgridPaletteFactory;
import simgrideclipseplugin.graphical.actions.AutoLayoutAction;
import simgrideclipseplugin.graphical.parts.SimgridEditPartFactory;
import simgrideclipseplugin.model.ModelHelper;

@SuppressWarnings("restriction")
public class SimgridGraphicEditor extends GraphicalEditorWithFlyoutPalette {
	// private SimgridModelListener listener;
	// The DOM Model initialized with IFile Input Source
	private IDOMModel model;
	private MultiPageXMLEditor parent;
	private static PaletteRoot palette;
	private KeyHandler sharedKeyHandler;
	private ISelectionChangedListener listener = new ISelectionChangedListener() {
		
		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			
			ISelection selection = event.getSelection();
			System.out.println(selection);
			IStructuredSelection sel = (IStructuredSelection) selection;

			if (!sel.isEmpty()) {
				//update selection in other view
				parent.editor.getSelectionProvider().setSelection(convertedSelection(sel));
				// update EditPart Selection from UI and from outline
				List<?> selectedList = sel.toList();
				@SuppressWarnings("unchecked")
				List<EditPart> partList = getGraphicalViewer()
						.getContents().getChildren();
				for (EditPart e : partList) {
					if  ((sel.getFirstElement() instanceof Element && selectedList.contains(e.getModel()))
						|| (sel.getFirstElement() instanceof EditPart && selectedList.contains(e))){
						e.setSelected(EditPart.SELECTED_PRIMARY);
					} else{
						e.setSelected(EditPart.SELECTED_NONE);
					}
				}
			}
		}
	};
	
	
	public SimgridGraphicEditor(){
		super.setEditDomain(new DefaultEditDomain(this));
	}
	
	public SimgridGraphicEditor(final MultiPageXMLEditor parent) {
		super.setEditDomain(new DefaultEditDomain(this));
		this.parent = parent;
		//parent.getSite().getPage().addSelectionListener((ISelectionListener) this);
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		setInput(input);
		getCommandStack().addCommandStackListener(this);
		initializeActionRegistry();
		if (!(input instanceof IFileEditorInput)) {
			throw new PartInitException(
					"Invalid Input: Must be IFileEditorInput");
		}
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
		GraphicalViewer viewer = getGraphicalViewer();
		AutomaticGraphLayoutHelper.INSTANCE.init(viewer.getRootEditPart());
		viewer.setContents(model.getDocument().getDocumentElement());
		AutomaticGraphLayoutHelper.INSTANCE.computeLayout();
	}

	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		GraphicalViewer viewer = getGraphicalViewer();
		
		viewer.setEditPartFactory(SimgridEditPartFactory.INSTANCE);
		
		ScalableFreeformRootEditPart rootEditPart = new ScalableFreeformRootEditPart();
		viewer.setRootEditPart(rootEditPart);
		
		//synch selection
		//getSite().setSelectionProvider(viewer);
		//getSite().getWorkbenchWindow().getSelectionService().addPostSelectionListener(this);
		parent.editor.getSelectionProvider().addSelectionChangedListener(listener );
		viewer.addSelectionChangedListener(listener);
		
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
	
	

	// public void setDirty() {
	// firePropertyChange(IEditorPart.PROP_DIRTY);
	// }

	@Override
	protected void createActions() {
		super.createActions();
		
		ActionRegistry ar = getActionRegistry();
		IAction action;
		
		action = new AutoLayoutAction(this);
		ar.registerAction(action);		
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
		 // Remove listener
		 //getSite().getWorkbenchWindow().getSelectionService().removeSelectionListener(this);
	
	 	super.dispose();
	 }
	 
	 @SuppressWarnings("unchecked")
	private ISelection convertedSelection(IStructuredSelection partSelection){
		 List<?> l = partSelection.toList();
		 if ((!l.isEmpty() && l.get(0) instanceof EditPart)){
			 List<Object> modelList = new ArrayList<Object>(l.size());
			 for (EditPart e : (List<EditPart>) l) {
				 modelList.add(e.getModel());
			 }
			 return new StructuredSelection(modelList);
		 }
		 return new StructuredSelection();
	 }
	 
	 private ISelection oldSelection;

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
//		System.out.println(selection);
//		if (part == SimgridGraphicEditor.this || selection.equals(oldSelection)){
//			return;
//		}
//		oldSelection = selection;
//		IStructuredSelection sel = (IStructuredSelection) selection;
//		
		// If not the active editor, ignore selection changed.
		IWorkbenchPartSite site = getSite();
		if (parent.equals(site.getPage().getActiveEditor()) 
				&& this.equals(parent.getActivePageEditor())) {
			//update available action 
			super.updateActions(super.getSelectionActions());
			listener.selectionChanged(new SelectionChangedEvent(getSite().getSelectionProvider(), selection));
		}
////		if (part instanceof PropertySheet){
////			
////		}
//		if (!sel.isEmpty()) {
//			//update selection in other view
//			parent.editor.getSelectionProvider().setSelection(convertedSelection(sel));
//			// update EditPart Selection from UI and from outline
//			List<?> selectedList = sel.toList();
//			@SuppressWarnings("unchecked")
//			List<EditPart> partList = getGraphicalViewer()
//					.getContents().getChildren();
//			for (EditPart e : partList) {
//				if  ((sel.getFirstElement() instanceof Element && selectedList.contains(e.getModel()))
//					|| (sel.getFirstElement() instanceof EditPart && selectedList.contains(e))){
//					e.setSelected(EditPart.SELECTED_PRIMARY);
//				} else{
//					e.setSelected(EditPart.SELECTED_NONE);
//				}
//			}
//		}
	}
	
	

	@Override
	protected PaletteRoot getPaletteRoot() {
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
		return super.getAdapter(type);
	}

}

package simgrideclipseplugin.editors;

import java.util.Arrays;
import java.util.List;

import javax.management.RuntimeErrorException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.MouseWheelHandler;
import org.eclipse.gef.MouseWheelZoomHandler;
import org.eclipse.gef.editparts.FreeformGraphicalRootEditPart;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.commands.ActionHandler;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IKeyBindingService;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.internal.ISelectionConversionService;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.part.MultiPageSelectionProvider;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.w3c.dom.Element;

import simgrideclipseplugin.editors.outline.SimgridOutlinePage;
import simgrideclipseplugin.graphical.AutomaticGraphLayoutHelper;
import simgrideclipseplugin.graphical.SimgridPaletteFactory;
import simgrideclipseplugin.graphical.parts.SimgridEditPartFactory;
import simgrideclipseplugin.model.ModelHelper;

@SuppressWarnings("restriction")
public class SimgridGraphicEditor extends GraphicalEditorWithFlyoutPalette {
	// private SimgridModelListener listener;
	// The DOM Model initialized with IFile Input Source
	private IDOMModel model;
	private MultiPageXMLEditor parent;
	private static PaletteRoot palette;

	public SimgridGraphicEditor(final MultiPageXMLEditor parent) {
		super.setEditDomain(new DefaultEditDomain(this));
		this.parent = parent;
		// listener = new SimgridModelListener(this);
		parent.getSite().getSelectionProvider()
				.addSelectionChangedListener(new ISelectionChangedListener() {

					@SuppressWarnings("unchecked")
					@Override
					public void selectionChanged(SelectionChangedEvent event) {
						//System.out.println(event.getSelection());
						IStructuredSelection sel = (IStructuredSelection) event.getSelection();
						if (!sel.isEmpty()) {
							List selectedList = sel.toList();
							// update EditPart Selection from UI and from outline
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
				});
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		if (!(input instanceof IFileEditorInput)) {
			throw new PartInitException(
					"Invalid Input: Must be IFileEditorInput");
		}
		setSite(site);
		setInput(input);
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
				// TODO maybe show it in UI
				throw new RuntimeException("Invalid Input: Must be DOM", e);
			}
		}
		// model.getModelManager().getExistingModelForEdit(model).
		return model;
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
		// add zooming support
		List zoomContributions = Arrays.asList(new String[] {
				ZoomManager.FIT_ALL, ZoomManager.FIT_HEIGHT,
				ZoomManager.FIT_WIDTH });
		rootEditPart.getZoomManager().setZoomLevelContributions(
				zoomContributions);
		IAction zoomIn = new ZoomInAction(rootEditPart.getZoomManager());
		IAction zoomOut = new ZoomOutAction(rootEditPart.getZoomManager());
		getActionRegistry().registerAction(zoomIn);
		getActionRegistry().registerAction(zoomOut);
		IHandlerService service = (IHandlerService) getSite().getService(
				IHandlerService.class);
		service.activateHandler(GEFActionConstants.ZOOM_IN, new ActionHandler(zoomIn));
		service.activateHandler(GEFActionConstants.ZOOM_OUT, new ActionHandler(zoomOut));
		// mouse support
		viewer.setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.MOD1),
				MouseWheelZoomHandler.SINGLETON);
	}

	// public void setDirty() {
	// firePropertyChange(IEditorPart.PROP_DIRTY);
	// }

	@Override
	public boolean isDirty() {
		return model.isDirty();
	}

	// public void updateUIFromDOMModel() {
	// Document document = model.getDocument();
	// Element root = document.getDocumentElement();
	// if (root != null) {
	// //do update for add/remove nodes
	// //this.getGraphicalViewer().getRootEditPart().refresh();
	// }
	// }

	// @Override
	// public void dispose() {
	// IgetDOMModel();
	// // Remove listener
	// //model.removeModelStateListener(listener);
	//
	// super.dispose();
	// }

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
		if (type == ZoomManager.class)
			return getGraphicalViewer().getProperty(
					ZoomManager.class.toString());
		return super.getAdapter(type);
	}

}

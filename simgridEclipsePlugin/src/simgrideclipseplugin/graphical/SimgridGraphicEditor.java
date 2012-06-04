package simgrideclipseplugin.graphical;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.editparts.FreeformGraphicalRootEditPart;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;

import simgrideclipseplugin.graphical.parts.SimgridEditPartFactory;

@SuppressWarnings("restriction")
public class SimgridGraphicEditor extends GraphicalEditorWithFlyoutPalette {
	//private SimgridModelListener listener;
	// The DOM Model initialized with IFile Input Source
	private IDOMModel model;

	public SimgridGraphicEditor(IEditorPart parent) {
		super.setEditDomain(new DefaultEditDomain(this));
		//listener = new SimgridModelListener(this);
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
			IFile file = ((IFileEditorInput) super.getEditorInput()).getFile();
			try {
				model = getDOMModel(file);
			} catch (Exception e) {
				throw new RuntimeException("Invalid Input: Must be DOM", e);
			}
		}
		return model;
	}

	private IDOMModel getDOMModel(IFile file) throws Exception {
		IModelManager manager = StructuredModelManager.getModelManager();
		IStructuredModel model = manager.getExistingModelForEdit(file);
		if (model == null) {
			model = manager.getModelForEdit(file);
		}
		if (model == null) {
			throw new Exception(
					"DOM Model is null, check the content type of your file (it seems that it's not *.xml file)");
		}
		if (!(model instanceof IDOMModel)) {
			throw new Exception("Model getted is not DOM Model!!!");
		}

		// Add listener to observe change of DOM (change is done with another
		// editor).
		//model.addModelStateListener(listener);
		return (IDOMModel) model;
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
		AutomaticGraphLayoutRenderer.INSTANCE.init(viewer.getRootEditPart());
		viewer.setContents(model.getDocument().getDocumentElement());
		AutomaticGraphLayoutRenderer.INSTANCE.computeLayout();
	}

	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		GraphicalViewer viewer = getGraphicalViewer();
		viewer.setEditPartFactory(SimgridEditPartFactory.INSTANCE);
		viewer.setRootEditPart(new FreeformGraphicalRootEditPart());
	}

//	public void setDirty() {
//		firePropertyChange(IEditorPart.PROP_DIRTY);
//	}

	@Override
	public boolean isDirty() {
		return model.isDirty();
	}

//	public void updateUIFromDOMModel() {
//		Document document = model.getDocument();
//		Element root = document.getDocumentElement();
//		if (root != null) {
//			//do update for add/remove nodes
//			//this.getGraphicalViewer().getRootEditPart().refresh();
//		}
//	}

//	@Override
//	public void dispose() {
//		IgetDOMModel();
//		// Remove listener
//		//model.removeModelStateListener(listener);
//		
//		super.dispose();
//	}

	@Override
	protected PaletteRoot getPaletteRoot() {
		return new PaletteRoot();
	}

}

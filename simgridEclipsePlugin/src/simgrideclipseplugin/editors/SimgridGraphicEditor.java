package simgrideclipseplugin.editors;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.editparts.FreeformGraphicalRootEditPart;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.part.MultiPageSelectionProvider;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;

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
//		parent.getSite().getSelectionProvider()
//				.addSelectionChangedListener(new ISelectionChangedListener() {
//					@Override
//					public void selectionChanged(SelectionChangedEvent event) {
//						//getGraphicalViewer().getSelectedEditParts().get(0);
//						System.out.println(event.getSelection());
//						parent.editor.getSelectionProvider().
//							setSelection(
//									new ISelection() {
//										
//										@Override
//										public boolean isEmpty() {
//											// TODO Auto-generated method stub
//											return false;
//										}
//									});
//					}
//				});

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
				//TODO maybe show it in UI
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
		viewer.setRootEditPart(new ScalableFreeformRootEditPart());
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
}

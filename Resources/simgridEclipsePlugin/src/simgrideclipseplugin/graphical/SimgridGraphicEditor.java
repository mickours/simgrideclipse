package simgrideclipseplugin.graphical;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorPart;

import simgrideclipseplugin.graphical.parts.ASEditPartFactory;

public class SimgridGraphicEditor extends GraphicalEditorWithFlyoutPalette {
	private IEditorPart parent; 
	private IDocument doc;
	
	public SimgridGraphicEditor(IEditorPart parent, IDocument doc){
		super.setEditDomain(new DefaultEditDomain(this));
		this.parent = parent;
		this.doc = doc;
	}
	
	@Override
	protected PaletteRoot getPaletteRoot() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void initializeGraphicalViewer() {
		super.initializeGraphicalViewer();
		GraphicalViewer viewer = getGraphicalViewer();
		viewer.setContents(getModel());
	}

	protected EditPart getModel() {
		// TODO Return data from model
		return null;
	}

	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		GraphicalViewer viewer = getGraphicalViewer(); 
		viewer.setEditPartFactory(ASEditPartFactory.INSTANCE); 
	}
}

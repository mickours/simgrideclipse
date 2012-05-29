package simgrideclipseplugin.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;

public class GraphicEditor extends GraphicalEditorWithFlyoutPalette {
	public GraphicEditor(){
		super.setEditDomain(new DefaultEditDomain(this));
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

}

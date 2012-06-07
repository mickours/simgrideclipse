package simgrideclipseplugin.graphical;

import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.requests.SimpleFactory;
import org.w3c.dom.Document;

public class SimgridPaletteFactory {

	private static PaletteContainer createComponentDrawer(String title) {
		PaletteDrawer componentsDrawer = new PaletteDrawer(title);

		// Creation tool of AS
		CreationToolEntry ASComponent = 
			new CreationToolEntry(
				"AS",
				"Create an AS",
				//not necessary: the object is made by the command
				new CreationFactory() {
					
					@Override
					public Object getObjectType() {
						return null;
					}
					
					@Override
					public Object getNewObject() {
						return "AS";
					}
				}
				,SimgridIconProvider.getIconImageDescriptor("AS.small")
				,SimgridIconProvider.getIconImageDescriptor("AS.large"));
		componentsDrawer.add(ASComponent);
		
		//TODO: do this for all components
		
		return componentsDrawer;
	}

	public static PaletteRoot createPalette() {
		PaletteRoot palette = new PaletteRoot();
		palette.add(createComponentDrawer("Platform"));
		return palette;
	}
}

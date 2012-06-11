package simgrideclipseplugin.graphical;

import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteSeparator;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.w3c.dom.Document;

import simgrideclipseplugin.Activator;

public class SimgridPaletteFactory {

	private static PaletteContainer createComponentDrawer(String title) {
		PaletteDrawer componentsDrawer = new PaletteDrawer(title);

		// Creation tool of AS
		CreationToolEntry ASComponent = 
			new CreationToolEntry(
				"AS",
				"Create an AS",
				//the real object is made by the command
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
		
		//add default tools
		PaletteGroup tools = new PaletteGroup("Tools");
		palette.add(tools);
		tools.add(new SelectionToolEntry());
		tools.add(new PaletteSeparator());
		//componentsDrawer.add(new MarqueeToolEntry());
		
		//add creation Tools for platform
		palette.add(createComponentDrawer("Platform"));
		
		return palette;
	}
}

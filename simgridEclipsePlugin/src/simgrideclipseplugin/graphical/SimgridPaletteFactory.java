package simgrideclipseplugin.graphical;

import java.util.List;

import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteSeparator;
import org.eclipse.gef.palette.SelectionToolEntry;
import simgrideclipseplugin.graphical.ElementCreationToolEntryProvider;

import simgrideclipseplugin.model.ElementList;

public class SimgridPaletteFactory {

	private static PaletteContainer createComponentDrawer(String title) {
		PaletteDrawer componentsDrawer = new PaletteDrawer(title);

		// Creation tool for all Element
		List<String> l = ElementList.getElementTagNameList();
		for (String tagName: l){
			CreationToolEntry component = 
					ElementCreationToolEntryProvider.newCreationToolEntry(tagName);
			componentsDrawer.add(component);
		}
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

package simgrideclipseplugin.graphical;

import java.util.List;

import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteSeparator;
import org.eclipse.gef.palette.SelectionToolEntry;

import simgrideclipseplugin.graphical.providers.ElementCreationToolEntryProvider;

import simgrideclipseplugin.model.ElementList;
import simgrideclipseplugin.model.SimgridRules;

public class SimgridPaletteFactory {

	private static PaletteContainer createComponentDrawer(String title) {
		PaletteGroup grp = new PaletteGroup(title);
		
		// Creation tool for ASLike Element
		PaletteDrawer asDrawer = new PaletteDrawer("Meta element");
		List<String> l = ElementList.getElementTagNameList();
		for (String tagName: l){
			if (SimgridRules.isDrawable(tagName) && SimgridRules.isASLike(tagName)){
				addComponent(asDrawer,tagName);
			}
		}
		asDrawer.add(new PaletteSeparator());
		addComponent(asDrawer,ElementList.AS_ROUTE);
		addComponent(asDrawer,ElementList.BYPASS_AS_ROUTE);
		asDrawer.add(new PaletteSeparator());
		addComponent(asDrawer,ElementList.RULE_BASED_ROUTE);		
		asDrawer.add(new PaletteSeparator());
		addComponent(asDrawer,ElementList.PEER);
		grp.add(asDrawer);

		// Creation tool for HostLike Element
		PaletteDrawer hostDrawer = new PaletteDrawer("Concrete element");
		hostDrawer.add(new PaletteSeparator());
		for (String tagName: l){
			if (SimgridRules.isDrawable(tagName) && SimgridRules.isHostLike(tagName)){
				addComponent(hostDrawer,tagName);
			}
		}
		hostDrawer.add(new PaletteSeparator());
		addComponent(hostDrawer,ElementList.ROUTE);
		addComponent(hostDrawer,ElementList.BYPASS_ROUTE);
		grp.add(hostDrawer);

		return grp;
	}
	
	private static void addComponent(PaletteDrawer cd, String tag){
		CreationToolEntry component = 
				ElementCreationToolEntryProvider.newCreationToolEntry(tag);
		cd.add(component);
	}

	public static PaletteRoot createPalette() {
		PaletteRoot palette = new PaletteRoot();
		
		//add default tools
		PaletteGroup tools = new PaletteGroup("Tools");
		palette.add(tools);
		tools.add(new SelectionToolEntry());
		//componentsDrawer.add(new MarqueeToolEntry());
		
		//add creation Tools for platform
		palette.add(createComponentDrawer("Platform"));
		return palette;
	}
}

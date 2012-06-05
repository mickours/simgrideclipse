package simgrideclipseplugin.graphical;

import javax.imageio.metadata.IIOMetadataNode;

import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.requests.CreationFactory;

public class SimgridPaletteFactory {

	private static PaletteContainer createComponentDrawer(String title) {
		PaletteDrawer componentsDrawer = new PaletteDrawer(title);

		// Creation tool of AS
		CreationToolEntry stateComponent = new CreationToolEntry("AS",
				"Create an AS", new CreationFactory() {
					public Object getNewObject() {
						return new IIOMetadataNode("AS");
					}

					public Object getObjectType() {
						return null;
					}
				},SimgridIconProvider.getIconImageDescriptor("AS.small"), 
				SimgridIconProvider.getIconImageDescriptor("AS.large"));
		componentsDrawer.add(stateComponent);
		return componentsDrawer;
	}

	public static PaletteRoot createPalette() {
		PaletteRoot palette = new PaletteRoot();
		palette.add(createComponentDrawer("Platform"));
		return palette;
	}
}

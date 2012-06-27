package simgrideclipseplugin.graphical;

import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.jface.resource.ImageDescriptor;

import simgrideclipseplugin.model.ElementCreationFactory;
import simgrideclipseplugin.model.ElementList;

public class ElementCreationToolEntryProvider {

	public static CreationToolEntry newCreationToolEntry(String label) {
		String shortDesc = "Create a "+label;
		ElementCreationFactory factory = new ElementCreationFactory(label);
		ImageDescriptor iconSmall = SimgridIconProvider.getIconImageDescriptor(label+"_small");
		ImageDescriptor iconLarge = SimgridIconProvider.getIconImageDescriptor(label);
		if (ElementList.isConnection(label)){
			return new ConnectionCreationToolEntry(label, shortDesc, factory, iconSmall, iconLarge);
		}
		return new CreationToolEntry(label, shortDesc, factory, iconSmall, iconLarge);
	}

}

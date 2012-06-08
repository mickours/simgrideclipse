package simgrideclipseplugin.graphical.parts;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.w3c.dom.Element;

public class SimgridEditPartFactory implements EditPartFactory {

	public static final EditPartFactory INSTANCE = new SimgridEditPartFactory();

	@Override
	public EditPart createEditPart(EditPart context, Object modelElement) {
		// get EditPart for model element
		EditPart part = getPartForElement(modelElement);
		// store model element in EditPart
		part.setModel(modelElement);
		return part;
	}

	/**
	 * Maps an object to an EditPart.
	 * 
	 * @throws RuntimeException
	 *             if no match was found (programming error)
	 */
	private EditPart getPartForElement(Object modelElement) {
		//TODO modify this to fit with the model
		String name = ((Element)modelElement).getTagName();
		if (name.equals("AS")) {
			return new ASEditPart();
		}
		if (name.equals("platform")) {
			return new PlatformEditPart();
		}
		throw new RuntimeException("Can't create part for model element: "
				+ ((modelElement != null) ? name
						: "null"));
		}
}

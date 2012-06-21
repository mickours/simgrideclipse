package simgrideclipseplugin.graphical.parts;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.w3c.dom.Element;

import simgrideclipseplugin.model.ElementList;
import simgrideclipseplugin.model.ModelHelper;

public class SimgridEditPartFactory implements EditPartFactory {

	public static final EditPartFactory INSTANCE = new SimgridEditPartFactory();

	@Override
	public EditPart createEditPart(EditPart context, Object modelElement) {
		Element element = null;
		String name = "";
		if (modelElement != null){
			element = (Element)modelElement;
			name = element.getTagName();
		}
		// get EditPart for model element
		EditPart part = getPartForElement(name);
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
	private EditPart getPartForElement(String name) {
		//format the string to get the appropriate editPart from tag name
		String className = this.getClass().getPackage().getName()+"."+name.substring(0,1).toUpperCase() + name.substring(1) + "EditPart";
		try {
			return (EditPart) Class.forName(className).newInstance();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//throw new RuntimeException("Can't create part for model element: "+ name);
		return (EditPart) new ErrorEditPart();
	}
	
}

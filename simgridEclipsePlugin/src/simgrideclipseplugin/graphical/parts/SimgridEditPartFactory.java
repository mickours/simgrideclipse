package simgrideclipseplugin.graphical.parts;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.w3c.dom.Element;

import simgrideclipseplugin.model.ElementList;

public class SimgridEditPartFactory implements EditPartFactory {

	public static final EditPartFactory INSTANCE = new SimgridEditPartFactory();

	@Override
	public EditPart createEditPart(EditPart context, Object modelElement) {
		EditPart part;
		//check if it's the container
		if (context == null && ((Element)modelElement).getTagName().equals(ElementList.AS)){
			part = new ASContainerEditPart();
		}
		else{
			// get EditPart for model element
			part = getPartForElement(modelElement);
		}
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
		if (modelElement != null && modelElement instanceof Element){
			Element element = (Element)modelElement;
			String name = element.getTagName();
			//format the string to get the appropriate editPart from tag name
			String className = this.getClass().getPackage().getName()+"."+name.substring(0,1).toUpperCase() + name.substring(1) + "EditPart";
			try {
				return (EditPart) Class.forName(className).newInstance();
			} catch (Exception e) {
				//nothing created
				//System.err.println("EditPart of type : "+className+" not created");
			}
		}
		//throw new RuntimeException("Can't create part for model element: "+ name);
		return new ErrorEditPart();
	}
	
}

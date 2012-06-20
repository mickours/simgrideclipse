package simgrideclipseplugin.model;

import org.eclipse.gef.requests.CreationFactory;

public class ElementCreationFactory implements CreationFactory {
	
	private String elementTagName;
	
	public ElementCreationFactory(String elementTagName) {
		this.elementTagName = elementTagName;
	}

	@Override
	public Object getNewObject() {
		return null;
	}

	@Override
	public Object getObjectType() {
		return elementTagName;
	}

}

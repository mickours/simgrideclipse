package simgrideclipseplugin.graphical;

import org.eclipse.gef.commands.Command;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;

public class addElementCommand extends Command {
	private Element parent;
	private String type;

	public addElementCommand(Element parent, String type) {
		this.parent = parent;
		this.type = type;
	}

	@Override
	public void execute() {
		Element e = parent.getOwnerDocument().createElement(type);
		//TODO find a clever way to define the id and routing defaults values
//		NodeList ndl = parent.getOwnerDocument().getElementsByTagName(type);
//		for(Node n : ndl){
//			
//		}
		e.setAttribute("id", "ASnew");
		e.setAttribute("routing", "Full");
		try{
			parent.appendChild(e);
		}catch (DOMException e2) {
			e2.printStackTrace();
		}
	}
}

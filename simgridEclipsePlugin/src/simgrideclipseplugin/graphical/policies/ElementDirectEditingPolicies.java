package simgrideclipseplugin.graphical.policies;

import org.eclipse.draw2d.Figure;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;
import org.w3c.dom.Element;

import simgrideclipseplugin.graphical.commands.ChangeAttributeCommand;

public class ElementDirectEditingPolicies extends DirectEditPolicy {

	@Override
	protected Command getDirectEditCommand(DirectEditRequest request) {
		ChangeAttributeCommand command = new ChangeAttributeCommand();
	    command.setModel((Element) getHost().getModel());
	    String s = (String) request.getCellEditor().getValue();
	    String val = s.substring(s.indexOf(":"));
	    command.setNewValue(val.trim());
	    command.setattributeName(s.substring(0, s.indexOf(":")));
	    return command;
	}

	@Override
	protected void showCurrentEditValue(DirectEditRequest request) {
//		String value = (String) request.getCellEditor()..getValue();
//	    ((Figure)getHostFigure()).getNameLabel().setText(value);
	}

}

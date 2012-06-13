package simgrideclipseplugin.graphical.actions;

import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.ui.actions.DeleteAction;
import org.eclipse.gef.ui.actions.EditorPartAction;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

public class DeleteElementAction extends DeleteAction {

	public static final String ID = "Delete element id";
	
	public DeleteElementAction(IWorkbenchPart part) {
		super(part);
	}

	@Override
	protected boolean calculateEnabled() {
		return true;
	}
	
}

package simgrideclipseplugin.graphical.actions;

import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.RetargetAction;

import simgrideclipseplugin.graphical.providers.SimgridIconProvider;

public class AutoLayoutRetargetAction extends RetargetAction {
	
	public AutoLayoutRetargetAction(){
		super(AutoLayoutAction.ID,"Auto Layout");
		setToolTipText("Perform Auto Layout");
		setImageDescriptor(SimgridIconProvider.getIconImageDescriptor(AutoLayoutAction.ID));
	}

//	@Override
//	public void partActivated(IWorkbenchPart part) {
//		// TODO Auto-generated method stub
//		super.partActivated(part);
//	}
//
//	@Override
//	public void partClosed(IWorkbenchPart part) {
//		// TODO Auto-generated method stub
//		super.partClosed(part);
//	}
//
//	@Override
//	public void partDeactivated(IWorkbenchPart part) {
//		// TODO Auto-generated method stub
//		super.partDeactivated(part);
//	}
	
	
	
}

package simgrideclipseplugin.model;

import org.eclipse.wst.sse.core.internal.provisional.IModelStateListener;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;

@SuppressWarnings("restriction")
public abstract class SimgridModelListener implements IModelStateListener {

	@Override
	public void modelAboutToBeChanged(IStructuredModel arg0) {
		//do nothing

	}

	@Override
	public void modelAboutToBeReinitialized(IStructuredModel arg0) {
		//do nothing

	}

	@Override
	public void modelDirtyStateChanged(IStructuredModel arg0, boolean arg1) {
		//do nothing

	}

	@Override
	public void modelReinitialized(IStructuredModel arg0) {
		//do nothing

	}

	@Override
	public void modelResourceDeleted(IStructuredModel arg0) {
		//do nothing

	}

	@Override
	public void modelResourceMoved(IStructuredModel arg0, IStructuredModel arg1) {
		//do nothing

	}

}

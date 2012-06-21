package simgrideclipseplugin.graphical.parts;

import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapter;
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;
import org.w3c.dom.Element;

import simgrideclipseplugin.model.ModelHelper;

@SuppressWarnings("restriction")
public abstract class SimgridAbstractEditPart extends AbstractGraphicalEditPart
		implements INodeAdapter, IAdaptable {
	
	//private IPropertySource elementPropertySource;
	@Override
	public List<?> getModelChildren() {
		return ModelHelper.getNoConnectionChildren((Element) getModel());
	}

	@Override
	public void activate() {
		((INodeNotifier) getModel()).addAdapter(this);
		super.activate();

	}

	@Override
	public void deactivate() {
		((INodeNotifier) getModel()).removeAdapter(this);
		super.deactivate();
	}

	@Override
	public void notifyChanged(INodeNotifier notifier, int eventType,
			Object changedFeature, Object oldValue, Object newValue, int pos) {
		// TODO: update UI can be optimized
		// update only the current if it's an attribute change OR the children
		if (eventType == INodeNotifier.CHANGE) {
			refreshVisuals();
		} else if (eventType == INodeNotifier.STRUCTURE_CHANGED){//(notifier == this || getChildren().contains(notifier)) {
			refreshChildren();
		}
	}

	@Override
	public boolean isAdapterForType(Object type) {
		return type.equals(Element.class);
	}

	
//	@Override
//    public void performRequest(Request req) {
//            if (req.getType().equals(RequestConstants.REQ_OPEN)) {
//                    try {
//                            MultiPageXMLEditor mpe = (MultiPageXMLEditor)
//                            mpe.editor.getSite().getPage().showView(IPageLayout.ID_PROP_SHEET);
//                    }
//                    catch (PartInitException e) {
//                            e.printStackTrace();
//                    }
//            }
//    }
	
//	/**
//     * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
//     */
//    @SuppressWarnings("rawtypes")
//	public Object getAdapter(Class type) {
//    	if (IPropertySource.class == type) {
//    		ModelHelper.getPropertySource(getModel());
//    	}
//    	return super.getAdapter(type);
//    }

}

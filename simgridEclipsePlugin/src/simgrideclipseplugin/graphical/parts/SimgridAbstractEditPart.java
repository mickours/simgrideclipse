package simgrideclipseplugin.graphical.parts;

import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapter;
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.w3c.dom.Element;
//import org.eclipse.wst.sse.ui.views.properties.XMLPropertySheetConfiguration;

import simgrideclipseplugin.editors.properties.ElementPropertySource;
import simgrideclipseplugin.model.ModelHelper;

@SuppressWarnings("restriction")
public abstract class SimgridAbstractEditPart extends AbstractGraphicalEditPart
		implements INodeAdapter, IAdaptable {
	
	private IPropertySource elementPropertySource;

	@SuppressWarnings("rawtypes")
	@Override
	public List getModelChildren() {
		return ModelHelper.getChildren((Element) getModel());
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
		if (eventType == 1) {
			refreshVisuals();
		} else {
			refreshChildren();
		}
	}

	@Override
	public boolean isAdapterForType(Object type) {
		return type.equals(Element.class);
	}
//	
//	@Override
//    public void performRequest(Request req) {
//            if (req.getType().equals(RequestConstants.REQ_OPEN)) {
//                    try {
//                            IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
//                            page.showView(IPageLayout.ID_PROP_SHEET);
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

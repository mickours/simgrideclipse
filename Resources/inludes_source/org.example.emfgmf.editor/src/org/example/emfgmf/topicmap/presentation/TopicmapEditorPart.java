package org.example.emfgmf.topicmap.presentation;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.edit.ui.dnd.EditingDomainViewerDropAdapter;
import org.eclipse.emf.edit.ui.dnd.LocalTransfer;
import org.eclipse.emf.edit.ui.dnd.ViewerDragAdapter;
import org.eclipse.emf.edit.ui.provider.UnwrappingSelectionProvider;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.document.FileEditorInputProxy;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

/**
 * Abstract base class for the editor parts that make up the pages of the Topicmap Editor. 
 *
 */
public abstract class TopicmapEditorPart extends EditorPart implements IMenuListener, IEditingDomainProvider {

	/**
	 * The "parent" editor that this editor part is embedded into. 
	 */
	protected TopicmapEditor parentEditor;
	
	/**
	 * Default constructor.  
	 * @param parent
	 */
	public TopicmapEditorPart(TopicmapEditor parent) {
		super();
		this.parentEditor = parent;
	}
	
    /* (non-Javadoc)
     * @see org.eclipse.emf.common.EMFPlugin.getString(java.lang.String)
     */
    protected static String getString(String key) {
        return TopicMapEditorPlugin.INSTANCE.getString(key);
    }

    /**
     * @return the common editing domain provided by the parent editor 
     */
    public EditingDomain getEditingDomain() {
    	return parentEditor.getEditingDomain();
    }

    /**
     * @return the common command stack provided by the parent editor
     */
    protected BasicCommandStack getCommandStack() {
        return ((BasicCommandStack)getEditingDomain().getCommandStack());
    }
    
    /**
     * @return the common adapter factory used by the parent editor
     */
    protected AdapterFactory getAdapterFactory() {
        return ((AdapterFactoryEditingDomain) ((FileEditorInputProxy)getEditorInput()).getEditingDomain()).getAdapterFactory();
    }
    
	/**
	 * This creates a context menu for the viewer and adds a listener as well registering the menu for extension.
	 */
	protected void createContextMenuFor(StructuredViewer viewer) {
		MenuManager contextMenu = new MenuManager("#PopUp");
		contextMenu.add(new Separator("additions"));
		contextMenu.setRemoveAllWhenShown(true);
		contextMenu.addMenuListener(this);
		Menu menu= contextMenu.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(contextMenu, new UnwrappingSelectionProvider(viewer));

		int dndOperations = DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK;
		Transfer[] transfers = new Transfer[] { LocalTransfer.getInstance() };
		viewer.addDragSupport(dndOperations, transfers, new ViewerDragAdapter(viewer));
		viewer.addDropSupport(dndOperations, transfers, new EditingDomainViewerDropAdapter(getEditingDomain(), viewer));
	}
    
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		// nothing to do here - this is handled by the parent editor
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#doSaveAs()
	 */
	@Override
	public void doSaveAs() {
		// nothing to do here - this is handled by the parent editor
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
	 */
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#isDirty()
	 */
	@Override
	public boolean isDirty() {
		return getCommandStack().isSaveNeeded();        
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
	 */
	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IMenuListener#menuAboutToShow(org.eclipse.jface.action.IMenuManager)
	 */
	public void menuAboutToShow(IMenuManager manager) {
		// pass the request to show the context menu on to the parent editor
		((TopicmapActionBarContributor) ((TopicmapMultipageActionBarContributor) parentEditor
				.getEditorSite().getActionBarContributor())
				.getTreeSubActionBars().getContributor())
				.menuAboutToShow(manager);
	}

	/**
	 * This is used by the parent to pass information on the input to be used to the editor part
	 * @param selection
	 */
	public abstract void setInput(Object input);

}

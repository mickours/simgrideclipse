package org.example.emfgmf.topicmap.presentation;

import org.eclipse.emf.edit.ui.celleditor.AdapterFactoryTreeEditor;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * This is the editor part that shadows the selection in the content outline.
 * The parent relation must be correctly defined for this to work.
 *
 */
public class SelectionEditorPart extends TopicmapEditorPart {

	/**
	 * The tree viewer used by this editor part.
	 */
	protected TreeViewer viewer;
	
	/**
	 * Default constructor.
	 * @param parent
	 */
	public SelectionEditorPart(TopicmapEditor parent) {
		super(parent);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI);
		viewer.setContentProvider(new AdapterFactoryContentProvider(getAdapterFactory()));
		viewer.setLabelProvider(new AdapterFactoryLabelProvider(getAdapterFactory()));
		viewer.setSelection(new StructuredSelection(getEditingDomain().getResourceSet().getResources().get(0)), true);
		getEditorSite().setSelectionProvider(viewer);
		new AdapterFactoryTreeEditor(viewer.getTree(), getAdapterFactory());
		createContextMenuFor(viewer);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		viewer.getTree().setFocus();
	}

	/* (non-Javadoc)
	 * @see org.example.emfgmf.topicmap.presentation.TopicmapEditorPart#setInput(java.lang.Object)
	 */
	@Override
	public void setInput(Object input) {
		viewer.setInput(input);
	}

}
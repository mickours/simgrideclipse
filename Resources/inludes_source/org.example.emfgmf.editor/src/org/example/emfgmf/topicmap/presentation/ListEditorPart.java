package org.example.emfgmf.topicmap.presentation;

import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * This editor part shows how a list view works.
 * A list viewer doesn't support icons.
 *
 */
public class ListEditorPart extends TopicmapEditorPart {

	/**
	 * The list viewer used by this editor part.
	 */
	protected ListViewer viewer;
	
	/**
	 * Default constructor.
	 * @param parent
	 */
	public ListEditorPart(TopicmapEditor parent) {
		super(parent);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		viewer = new ListViewer(parent, SWT.NONE);
		viewer.setContentProvider(new AdapterFactoryContentProvider(getAdapterFactory()));
		viewer.setLabelProvider(new AdapterFactoryLabelProvider(getAdapterFactory()));
		createContextMenuFor(viewer);
		getEditorSite().setSelectionProvider(viewer);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		viewer.getList().setFocus();
	}

	/* (non-Javadoc)
	 * @see org.example.emfgmf.topicmap.presentation.TopicmapEditorPart#setInput(java.lang.Object)
	 */
	@Override
	public void setInput(Object input) {
		viewer.setInput(input);
	}
}

package org.example.emfgmf.topicmap.presentation;

import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * This editor part inverts the role of parent and child in the content provider 
 * and show parents as a tree.
 *
 */
public class ParentEditorPart extends TopicmapEditorPart {

	/**
	 * The tree viewer used by this editor part.
	 */
	protected TreeViewer viewer;
	
	/**
	 * Default constructor.
	 * @param parent
	 */
	public ParentEditorPart(TopicmapEditor parent) {
		super(parent);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI);
		viewer.setAutoExpandLevel(30);
		viewer.setContentProvider(new ReverseAdapterFactoryContentProvider(getAdapterFactory()));
		viewer.setLabelProvider(new AdapterFactoryLabelProvider(getAdapterFactory()));		
		createContextMenuFor(viewer);
		getEditorSite().setSelectionProvider(viewer);
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

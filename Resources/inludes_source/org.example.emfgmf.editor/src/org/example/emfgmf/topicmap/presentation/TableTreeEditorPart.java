package org.example.emfgmf.topicmap.presentation;

import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

/**
 * This shows how a tree view with columns works.
 *
 */
public class TableTreeEditorPart extends TopicmapEditorPart {

	/**
	 * The tree viewer used by this editor part.
	 */
	protected TreeViewer viewer;
	
	/**
	 * Default constructor.
	 * @param parent
	 */
	public TableTreeEditorPart(TopicmapEditor parent) {
		super(parent);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.NONE);
		Tree tree = viewer.getTree();
		tree.setLayoutData(new FillLayout());
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);

		TreeColumn objectColumn = new TreeColumn(tree, SWT.NONE);
		objectColumn.setText(getString("_UI_ObjectColumn_label"));
		objectColumn.setResizable(true);
		objectColumn.setWidth(250);

		TreeColumn selfColumn = new TreeColumn(tree, SWT.NONE);
		selfColumn.setText(getString("_UI_SelfColumn_label"));
		selfColumn.setResizable(true);
		selfColumn.setWidth(200);

		viewer.setColumnProperties(new String [] {"a", "b"});
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

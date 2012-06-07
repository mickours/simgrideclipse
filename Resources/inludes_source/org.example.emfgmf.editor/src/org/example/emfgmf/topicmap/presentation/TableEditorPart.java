/**
 * MISSDOC Add file header to file TableEditorPart.java
 */
package org.example.emfgmf.topicmap.presentation;

import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * This shows how a table view works.
 * A table can be used as a list with icons.
 *
 */
public class TableEditorPart extends TopicmapEditorPart {

	/**
	 * The table viewer used by this editor part.
	 */
	protected TableViewer viewer;
	
	/**
	 * Default constructor.
	 * @param parent
	 */
	public TableEditorPart(TopicmapEditor parent) {
		super(parent);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.NONE);
		
		Table table = viewer.getTable();
		TableLayout layout = new TableLayout();
		table.setLayout(layout);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableColumn objectColumn = new TableColumn(table, SWT.NONE);
		layout.addColumnData(new ColumnWeightData(3, 100, true));
		objectColumn.setText(getString("_UI_ObjectColumn_label"));
		objectColumn.setResizable(true);

		TableColumn selfColumn = new TableColumn(table, SWT.NONE);
		layout.addColumnData(new ColumnWeightData(2, 100, true));
		selfColumn.setText(getString("_UI_SelfColumn_label"));
		selfColumn.setResizable(true);

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
		viewer.getTable().setFocus();
	}

	/* (non-Javadoc)
	 * @see org.example.emfgmf.topicmap.presentation.TopicmapEditorPart#setInput(java.lang.Object)
	 */
	@Override
	public void setInput(Object input) {
		viewer.setInput(input);
	}
}

package simgrideclipseplugin.wizards.composites;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import simgrideclipseplugin.graphical.providers.ElementLabelProvider;

public class ElementSelectionList extends TableViewer {
	public ElementSelectionList (Composite parent){
		super(parent,SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		setLabelProvider(new ElementLabelProvider());
//		addFilter(new ViewerFilter() {
//	      public boolean select(Viewer viewer, Object parent, Object element) {
//	        return ((Element) element).value % 2 == 0;
//	      }
//	    });

//	    setSorter(new ViewerSorter() {
//	      public int compare(Viewer viewer, Object obj1, Object obj2) {
//	        return (((Element) obj2).getTextContent().compareToIgnoreCase(((Element) obj1).getTextContent()));
//	      }
//	    });

	    setContentProvider(new IStructuredContentProvider() {

	    	
	      public Object[] getElements(Object inputElement) {
	        return ((List<?>) inputElement).toArray();
	      }

	      public void dispose() {
	      }

	      public void inputChanged(Viewer viewer, Object oldInput,
	          Object newInput) {
	    	  
	      }
	    });
	  }

	public int getItemHeight() {
		return getTable().getItemHeight();
	}
}

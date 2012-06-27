package simgrideclipseplugin.graphical;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

public class ListSelectionDialog extends ElementListSelectionDialog {

	public ListSelectionDialog(Shell parent) {
		super(parent,new ElementLabelProvider());
		setMultipleSelection(false);
	}
}

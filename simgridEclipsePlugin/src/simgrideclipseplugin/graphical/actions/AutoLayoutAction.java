package simgrideclipseplugin.graphical.actions;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.actions.EditorPartAction;
import org.eclipse.ui.IEditorPart;

import simgrideclipseplugin.editors.SimgridGraphicEditor;
import simgrideclipseplugin.graphical.AutomaticGraphLayoutHelper;
import simgrideclipseplugin.graphical.providers.SimgridIconProvider;

public class AutoLayoutAction extends EditorPartAction{
	
	public static final String ID = "simgrideclipseplugin.AutoLayout";
	public static final String TEXT = "Auto Layout";
	public static final String TOOL_TIP_TEXT = "Perform Auto Layout";
	
	public AutoLayoutAction(IEditorPart editor) {
		super(editor);
	}

	@Override
	protected void init() {
		setId(ID);
		setText(TEXT);
		setToolTipText(TOOL_TIP_TEXT);
		setImageDescriptor(SimgridIconProvider.getIconImageDescriptor(ID));
	}

	public void run() {
		EditPart content = ((SimgridGraphicEditor)getEditorPart()).getGraphicalContents();
		AutomaticGraphLayoutHelper.INSTANCE.init(content);
		AutomaticGraphLayoutHelper.INSTANCE.computeLayout();
	}
	
	
	@Override
	protected boolean calculateEnabled() {
		//FIXME activate only if necessary
		return true;
	}
}

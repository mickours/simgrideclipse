package simgrideclipseplugin.editors;

import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IActionBars2;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.ide.IDEActionFactory;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;

import simgrideclipseplugin.graphical.actions.AutoLayoutAction;
import simgrideclipseplugin.graphical.actions.GoIntoAction;
import simgrideclipseplugin.graphical.actions.GoOutAction;

/**
 * Manages the installation/deinstallation of global actions for multi-page editors.
 * Responsible for the redirection of global actions to the active editor.
 * Multi-page contributor replaces the contributors for the individual editors in the multi-page editor.
 */
public class MultiPageSimgridEditorContributor extends MultiPageEditorActionBarContributor {

	private SubActionBarsExt myGraphicSubActionBars;
	
	private SubActionBarsExt activeEditorActionBars;
	private IEditorPart activeEditorPart;
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorActionBarContributor#init(org.eclipse.ui.IActionBars)
	 */
	@Override
	public void init(IActionBars bars) {
		super.init(bars);
		assert bars instanceof IActionBars2;
		myGraphicSubActionBars = new SubActionBarsExt(getPage(),
				(IActionBars2) getActionBars(),
				new GraphicalEditorActionBarContributor(),
				"simgrideclipseplugin.editors.GraphicalActionBarContributor");
		getActionBars().clearGlobalActionHandlers();
		getActionBars().updateActionBars();
		myGraphicSubActionBars.deactivate(true);
	}
	
	/**
	 * Returns the action registed with the given text editor.
	 * @return IAction or null if editor is null.
	 */
	protected IAction getAction(ITextEditor editor, String actionID) {
		return (editor == null ? null : editor.getAction(actionID));
	}
	

	@Override
	public void setActivePage(IEditorPart part) {
		activeEditorPart = part;
		if (activeEditorPart instanceof SimgridGraphicEditor) {
			setActiveActionBars(myGraphicSubActionBars);
		}else {
			setActiveActionBars(null);
		}
		updateTextEditorActions();
		updateGraphicalEditorActions();
		getActionBars().updateActionBars();
	}
	
	private void updateGraphicalEditorActions(){
		IActionBars actionBars = getActionBars();
		if (actionBars != null) {
			//manage action bars to works with graphical editor
			if (activeEditorPart instanceof SimgridGraphicEditor){
				ActionFactory[] actionList = 
					{ActionFactory.UNDO,
					 ActionFactory.DELETE,
					 ActionFactory.REDO,
					 ActionFactory.SELECT_ALL,
					 ActionFactory.COPY,
					 ActionFactory.PASTE,
					 ActionFactory.CUT,
					};
				ActionRegistry actionRegistry = (ActionRegistry) activeEditorPart.getAdapter(ActionRegistry.class);
				for (ActionFactory af : actionList){
					IAction action = actionRegistry.getAction(af.getId());
					if (action != null){
						actionBars.setGlobalActionHandler(action.getId(),action);
					}
				}
				String[] simgridActions = {
						AutoLayoutAction.ID,
						GoIntoAction.ID,
						GoOutAction.ID,
				};
				for (String id : simgridActions){
					IAction action = actionRegistry.getAction(id);
					if (action != null){
						actionBars.setGlobalActionHandler(id,action);
					}
				}
			}
		}
	}
	
	private void updateTextEditorActions(){
		IActionBars actionBars = getActionBars();
		if (actionBars != null) {
			ITextEditor editor = (activeEditorPart instanceof ITextEditor) 
					? (ITextEditor) activeEditorPart : null;
			actionBars.setGlobalActionHandler(
				ActionFactory.DELETE.getId(),
				getAction(editor, ITextEditorActionConstants.DELETE));
			actionBars.setGlobalActionHandler(
				ActionFactory.UNDO.getId(),
				getAction(editor, ITextEditorActionConstants.UNDO));
			actionBars.setGlobalActionHandler(
				ActionFactory.REDO.getId(),
				getAction(editor, ITextEditorActionConstants.REDO));
			actionBars.setGlobalActionHandler(
				ActionFactory.CUT.getId(),
				getAction(editor, ITextEditorActionConstants.CUT));
			actionBars.setGlobalActionHandler(
				ActionFactory.COPY.getId(),
				getAction(editor, ITextEditorActionConstants.COPY));
			actionBars.setGlobalActionHandler(
				ActionFactory.PASTE.getId(),
				getAction(editor, ITextEditorActionConstants.PASTE));
			actionBars.setGlobalActionHandler(
				ActionFactory.SELECT_ALL.getId(),
				getAction(editor, ITextEditorActionConstants.SELECT_ALL));
			actionBars.setGlobalActionHandler(
				ActionFactory.FIND.getId(),
				getAction(editor, ITextEditorActionConstants.FIND));
			actionBars.setGlobalActionHandler(
				IDEActionFactory.BOOKMARK.getId(),
				getAction(editor, IDEActionFactory.BOOKMARK.getId()));
		}
	}
	
	
	/**
	 * Switches the active action bars.
	 */
	private void setActiveActionBars(SubActionBarsExt actionBars) {
		if (activeEditorActionBars != null
				&& activeEditorActionBars != actionBars) {
			activeEditorActionBars.deactivate();
		}
		activeEditorActionBars = actionBars;
		if (activeEditorActionBars != null) {
			activeEditorActionBars.setEditorPart(activeEditorPart);
			activeEditorActionBars.activate();
		}
	}

	@Override
	public void dispose() {
		if (null != myGraphicSubActionBars)
			myGraphicSubActionBars.dispose();
		super.dispose();
	}
	
	
}

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

/**
 * Manages the installation/deinstallation of global actions for multi-page editors.
 * Responsible for the redirection of global actions to the active editor.
 * Multi-page contributor replaces the contributors for the individual editors in the multi-page editor.
 */
public class MultiPageEditorXMLContributor extends MultiPageEditorActionBarContributor {
	
	private IActionBars2 myActionBars2;

	//private SubActionBarsExt myTextSubActionBars;

	private SubActionBarsExt myGraphicSubActionBars;
	
	private SubActionBarsExt activeEditorActionBars;
	private IEditorPart activeEditorPart;
	
//	private IPropertyListener myEditorPropertyChangeListener = new IPropertyListener() {
//
//		public void propertyChanged(Object source, int propId) {
//			if (activeEditorActionBars != null) {
//				if (activeEditorActionBars.getContributor() instanceof GraphicalEditorActionBarContributor) {
//					((GraphicalEditorActionBarContributor) activeEditorActionBars.getContributor()).update();
//				}
//			}
//		}
//	};
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorActionBarContributor#init(org.eclipse.ui.IActionBars)
	 */
	@Override
	public void init(IActionBars bars) {
		super.init(bars);
		assert bars instanceof IActionBars2;
		myActionBars2 = (IActionBars2) bars;
	}
	
	/**
	 * Returns the action registed with the given text editor.
	 * @return IAction or null if editor is null.
	 */
	protected IAction getAction(ITextEditor editor, String actionID) {
		return (editor == null ? null : editor.getAction(actionID));
	}
	
	/* (non-JavaDoc)
	 * Method declared in AbstractMultiPageEditorActionBarContributor.
	 */
	public void setActivePage(IEditorPart part) {
		if (activeEditorPart == part)
			return;
		activeEditorPart = part;
		if (activeEditorPart instanceof SimgridGraphicEditor) {
			setActiveActionBars(getGraphicSubActionBars());
		}else {
			setActiveActionBars(null);
		}
		updateTextEditorActions();
		updateGraphicalEditorActions();
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
					IAction action = (IAction) actionRegistry.getAction(af.getId());
					if (action != null){
						actionBars.setGlobalActionHandler(action.getId(),action);
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
			actionBars.updateActionBars();
		}
	}
	
	/**
	 * @return the sub cool bar manager for the diagram editor.
	 */
	public SubActionBarsExt getGraphicSubActionBars() {
		if (myGraphicSubActionBars == null) {
			myGraphicSubActionBars = new SubActionBarsExt(getPage(),
					myActionBars2,
					new GraphicalEditorActionBarContributor(),
					"simgrideclipseplugin.editors.GraphicalActionBarContributor");
		}
		return myGraphicSubActionBars;
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
		myGraphicSubActionBars.dispose();
		super.dispose();
	}
	
	
}

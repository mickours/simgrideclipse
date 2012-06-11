package simgrideclipseplugin.editors;

import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.DeleteAction;
import org.eclipse.gef.ui.actions.ZoomComboContributionItem;
import org.eclipse.gef.ui.actions.ZoomInRetargetAction;
import org.eclipse.gef.ui.actions.ZoomOutRetargetAction;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.ide.IDEActionFactory;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.eclipse.wst.sse.ui.internal.IStructuredTextEditorActionConstants;

import simgrideclipseplugin.graphical.AutomaticGraphLayoutHelper;
import simgrideclipseplugin.graphical.SimgridIconProvider;
import simgrideclipseplugin.graphical.actions.AutoLayoutAction;

/**
 * Manages the installation/deinstallation of global actions for multi-page editors.
 * Responsible for the redirection of global actions to the active editor.
 * Multi-page contributor replaces the contributors for the individual editors in the multi-page editor.
 */
public class MultiPageEditorXMLContributor extends MultiPageEditorActionBarContributor {
	private IEditorPart activeEditorPart;
	/**
	 * Creates a multi-page contributor.
	 */
	public MultiPageEditorXMLContributor() {
		super();
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
		

		IActionBars actionBars = getActionBars();
		if (actionBars != null) {
				if (part instanceof ITextEditor){
				ITextEditor editor =  (ITextEditor) part;
	
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
		//TODO manage action bars to works with graphical editor
		if (part instanceof SimgridGraphicEditor){
			ActionRegistry actionRegistry = (ActionRegistry) activeEditorPart.getAdapter(ActionRegistry.class);
			actionBars.setGlobalActionHandler(
					ActionFactory.DELETE.getId(),actionRegistry.getAction(ActionFactory.DELETE.getId()));
			
		}
		actionBars.updateActionBars();
	}
	
	private ActionRegistry getActionRegistery(){
		ActionRegistry actionRegistry = null;
		if (activeEditorPart instanceof SimgridGraphicEditor){
			actionRegistry = (ActionRegistry) activeEditorPart.getAdapter(ActionRegistry.class);
		}
		return actionRegistry;
	}
	
	public void contributeToMenu(IMenuManager manager) {
		IMenuManager editorMenu = new MenuManager("Editor &Menu");
		manager.prependToGroup(IWorkbenchActionConstants.MB_ADDITIONS, editorMenu);
		editorMenu.add(new AutoLayoutAction(null));
		IMenuManager viewMenu = new MenuManager("View");
		manager.prependToGroup(IWorkbenchActionConstants.MB_ADDITIONS,viewMenu);
		viewMenu.add(new ZoomInRetargetAction());
		viewMenu.add(new ZoomOutRetargetAction());
	}
	public void contributeToToolBar(IToolBarManager manager) {
			manager.add(new Separator());
			manager.add(new AutoLayoutAction(null));
			manager.add(new ZoomComboContributionItem(getPage()));
	}
}

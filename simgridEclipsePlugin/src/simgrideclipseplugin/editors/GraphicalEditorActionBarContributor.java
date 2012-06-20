package simgrideclipseplugin.editors;

import org.eclipse.gef.ui.actions.ActionBarContributor;
import org.eclipse.gef.ui.actions.DeleteRetargetAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.RedoRetargetAction;
import org.eclipse.gef.ui.actions.UndoRetargetAction;
import org.eclipse.gef.ui.actions.ZoomComboContributionItem;
import org.eclipse.gef.ui.actions.ZoomInRetargetAction;
import org.eclipse.gef.ui.actions.ZoomOutRetargetAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.actions.ActionFactory;

import simgrideclipseplugin.graphical.actions.AutoLayoutAction;

public class GraphicalEditorActionBarContributor extends ActionBarContributor {

	@Override
	protected void buildActions() {
		addRetargetAction(new UndoRetargetAction());
	    addRetargetAction(new RedoRetargetAction());
	    addRetargetAction(new DeleteRetargetAction());
	    
	    addRetargetAction(new ZoomInRetargetAction());
	    addRetargetAction(new ZoomOutRetargetAction());
	    addAction(new AutoLayoutAction(getPage().getActiveEditor()));
	}
	
	@Override
	  public void contributeToToolBar(IToolBarManager toolBarManager) {
	    super.contributeToToolBar(toolBarManager);
	    toolBarManager.add(getAction(ActionFactory.UNDO.getId()));
	    toolBarManager.add(getAction(ActionFactory.REDO.getId()));
	    toolBarManager.add(getAction(ActionFactory.DELETE.getId()));
	    toolBarManager.add(new Separator());
	    toolBarManager.add(getAction(AutoLayoutAction.ID));
	    toolBarManager.add(new ZoomComboContributionItem(getPage()));
	 }

	@Override
	protected void declareGlobalActionKeys() {
		// TODO Auto-generated method stub

	}
	
	public void contributeToMenu(IMenuManager manager) {
		IMenuManager editorMenu = new MenuManager("Editor &Menu");
		manager.prependToGroup(IWorkbenchActionConstants.MB_ADDITIONS, editorMenu);
		editorMenu.add(getAction(AutoLayoutAction.ID));
		IMenuManager viewMenu = new MenuManager("View");
		manager.prependToGroup(IWorkbenchActionConstants.MB_ADDITIONS,viewMenu);
		viewMenu.add(getAction(GEFActionConstants.ZOOM_IN));
		viewMenu.add(getAction(GEFActionConstants.ZOOM_OUT));
	}
}
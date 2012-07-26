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
import simgrideclipseplugin.graphical.actions.AutoLayoutRetargetAction;
import simgrideclipseplugin.graphical.actions.EditElementAction;
import simgrideclipseplugin.graphical.actions.EditElementRetargetAction;
import simgrideclipseplugin.graphical.actions.GoIntoAction;
import simgrideclipseplugin.graphical.actions.GoIntoRetargetAction;
import simgrideclipseplugin.graphical.actions.GoOutAction;
import simgrideclipseplugin.graphical.actions.GoOutRetargetAction;

public class GraphicalEditorActionBarContributor extends ActionBarContributor {

	@Override
	protected void buildActions() {
		addRetargetAction(new UndoRetargetAction());
	    addRetargetAction(new RedoRetargetAction());
	    addRetargetAction(new DeleteRetargetAction());
	    
	    addRetargetAction(new ZoomInRetargetAction());
	    addRetargetAction(new ZoomOutRetargetAction());
	    addRetargetAction(new AutoLayoutRetargetAction());
	    addRetargetAction(new GoIntoRetargetAction());
	    addRetargetAction(new GoOutRetargetAction());
	    addRetargetAction(new EditElementRetargetAction());
	}
	
	@Override
	  public void contributeToToolBar(IToolBarManager toolBarManager) {
	    super.contributeToToolBar(toolBarManager);
	    toolBarManager.add(getAction(GoOutAction.ID));
	    toolBarManager.add(getAction(GoIntoAction.ID));
	    toolBarManager.add(new Separator());
	    toolBarManager.add(getAction(ActionFactory.UNDO.getId()));
	    toolBarManager.add(getAction(ActionFactory.REDO.getId()));
	    toolBarManager.add(new Separator());
	    toolBarManager.add(getAction(ActionFactory.DELETE.getId()));
	    toolBarManager.add(new Separator());
	    toolBarManager.add(getAction(AutoLayoutAction.ID));
	    toolBarManager.add(new ZoomComboContributionItem(getPage()));
	    
	 }

	@Override
	protected void declareGlobalActionKeys() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void contributeToMenu(IMenuManager manager) {
		IMenuManager editorMenu = new MenuManager("Simgrid &Menu");
		manager.prependToGroup(IWorkbenchActionConstants.MB_ADDITIONS, editorMenu);
		editorMenu.add(getAction(AutoLayoutAction.ID));
		editorMenu.add(new Separator());
		editorMenu.add(getAction(EditElementAction.ID));
		editorMenu.add(new Separator());
		editorMenu.add(getAction(GoIntoAction.ID));
		editorMenu.add(getAction(GoOutAction.ID));
		IMenuManager viewMenu = new MenuManager("Simgrid &View");
		manager.prependToGroup(IWorkbenchActionConstants.MB_ADDITIONS,viewMenu);
		viewMenu.add(getAction(GEFActionConstants.ZOOM_IN));
		viewMenu.add(getAction(GEFActionConstants.ZOOM_OUT));
	}
}

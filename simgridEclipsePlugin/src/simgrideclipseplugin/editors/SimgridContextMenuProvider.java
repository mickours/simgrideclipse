package simgrideclipseplugin.editors;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.actions.ActionFactory;

import simgrideclipseplugin.graphical.actions.EditElementAction;
import simgrideclipseplugin.graphical.actions.GoIntoAction;
import simgrideclipseplugin.graphical.actions.GoOutAction;

public class SimgridContextMenuProvider extends ContextMenuProvider {

	  private ActionRegistry actionRegistry; 
	  
	  public SimgridContextMenuProvider(EditPartViewer viewer, 
	      ActionRegistry registry) { 
	    super(viewer); 
	    setActionRegistry(registry); 
	  } 
	 
	  public void buildContextMenu(IMenuManager menu) { 
		  menu.removeAll();
		  menu.add(new Separator(GEFActionConstants.GROUP_UNDO));
			menu.add(new Separator(GEFActionConstants.GROUP_COPY));
			menu.add(new Separator(GEFActionConstants.GROUP_EDIT));
			menu.add(new Separator(GEFActionConstants.GROUP_VIEW));
			menu.add(new Separator("Others"));
		  ActionFactory[] actionList = 
				{ActionFactory.UNDO,
				 ActionFactory.DELETE,
				 ActionFactory.REDO,
				 ActionFactory.SELECT_ALL,
				 ActionFactory.COPY,
				 ActionFactory.PASTE,
				 ActionFactory.CUT,
				};
			for (ActionFactory af : actionList){
				IAction action = (IAction) actionRegistry.getAction(af.getId());
				if (action != null){
					if (action.getId().equals(ActionFactory.UNDO.getId()) || action.getId().equals(ActionFactory.REDO.getId())){
						menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action);
					}
					else if (action.isEnabled()) {
						menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);
					}
				}
			}
			IAction action = actionRegistry.getAction(EditElementAction.ID);
			menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);
			action = actionRegistry.getAction(GoIntoAction.ID);
			menu.appendToGroup(GEFActionConstants.GROUP_VIEW, action);
			action = actionRegistry.getAction(GoOutAction.ID);
			menu.appendToGroup(GEFActionConstants.GROUP_VIEW, action);
			
	  }
	 
	  public void setActionRegistry(ActionRegistry registry) { 
	    actionRegistry = registry; 
	  }
	  
	  public ActionRegistry getActionRegistry( ) { 
		    return actionRegistry; 
		 } 

}

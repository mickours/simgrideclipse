package simgrideclipseplugin.editors;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.actions.ActionFactory;

public class SimgridContextMenuProvider extends ContextMenuProvider {

	  private ActionRegistry actionRegistry; 
	  
	  public SimgridContextMenuProvider(EditPartViewer viewer, 
	      ActionRegistry registry) { 
	    super(viewer); 
	    setActionRegistry(registry); 
	  } 
	 
	  public void buildContextMenu(IMenuManager menu) { 
	    GEFActionConstants.addStandardActionGroups(menu); 
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
	  }
	 
	  public void setActionRegistry(ActionRegistry registry) { 
	    actionRegistry = registry; 
	  } 

}

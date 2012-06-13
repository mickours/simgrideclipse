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
	 
	    IAction action = getActionRegistry().getAction( 
	        ActionFactory.DELETE.getId()); 
	    if (action.isEnabled()) 
	      menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action); 
	  } 
	 
	  private ActionRegistry getActionRegistry() { 
	    return actionRegistry; 
	  } 
	 
	  public void setActionRegistry(ActionRegistry registry) { 
	    actionRegistry = registry; 
	  } 

}

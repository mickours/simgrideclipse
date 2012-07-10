package simgrideclipseplugin.graphical.parts;

import java.util.List;

import org.w3c.dom.Element;

import simgrideclipseplugin.graphical.AutomaticGraphLayoutHelper;
import simgrideclipseplugin.graphical.figures.ContentPaneFigure;
import simgrideclipseplugin.model.ElementList;

public class ASContainerEditPart extends AbstractContainerEditPart {

	@Override
	protected void refreshVisuals() {
		ContentPaneFigure f = (ContentPaneFigure) getFigure();
		Element node = (Element)getModel();
		f.setTitle(ElementList.AS+" id=\""+node.getAttribute("id")+"\"");
		super.refreshVisuals();
	}

	@Override
	public void refresh() {
		super.refresh();
		//auto layout if all element are at (0,0)
		List<?> children = getChildren();
		int i = 0;
		int nbInZero = 0;
		while(i < children.size() && nbInZero < 2){
			if (children.get(i) instanceof AbstractElementEditPart){
				AbstractElementEditPart elem = (AbstractElementEditPart) children.get(i);
				if (elem.getLocation().equals(0, 0)){
					nbInZero++;
				}
			}
			i++;
		}
		if (nbInZero > 1){
			AutomaticGraphLayoutHelper.INSTANCE.init(this);
			AutomaticGraphLayoutHelper.INSTANCE.computeLayout();
		}
	}
	
	
	
	
}

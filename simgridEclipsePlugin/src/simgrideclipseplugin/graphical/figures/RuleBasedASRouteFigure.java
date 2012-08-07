package simgrideclipseplugin.graphical.figures;

import org.eclipse.draw2d.ColorConstants;

import simgrideclipseplugin.model.ElementList;


public class RuleBasedASRouteFigure extends ElementFigure{
	
	public RuleBasedASRouteFigure(){
		super(ElementList.RULE_BASED_ROUTE);				
	    titleBorder.setBackgroundColor(ColorConstants.gray);
		setBackgroundColor(ColorConstants.orange);		
	}	
}

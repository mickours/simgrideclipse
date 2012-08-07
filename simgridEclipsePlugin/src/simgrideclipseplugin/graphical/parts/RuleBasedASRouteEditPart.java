/**
 * 
 */
package simgrideclipseplugin.graphical.parts;

import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import simgrideclipseplugin.graphical.figures.RuleBasedASRouteFigure;
import simgrideclipseplugin.model.ElementList;
import simgrideclipseplugin.model.ModelHelper;
import simgrideclipseplugin.wizards.RuleBasedASRouteWizard;
import simgrideclipseplugin.wizards.EditElementWizard;

/**
 * the class used to represent RuleBasedASRoute. Should be almost similar to the
 * routes ones.
 * 
 * @author lbobelin
 * 
 */
public class RuleBasedASRouteEditPart extends AbstractElementEditPart {
	//Warning and errors
	private String errorLog = "";
	private String warningLog = "";
	private Boolean warning = false;
	private Boolean error = false;
	
	// ugly hack. I should definitely clean up this class when it'll work.
	private String oldErrorLog = "";
	private String oldWarningLog = "";
	private Boolean oldWarning = false;
	private Boolean oldError = false;
	
	//Lists to maintain regexp state for direct use
	private List<Element> asSrcList = new Vector<Element>();
	private List<Element> asDstList = new Vector<Element>();
	private List<Matcher> asSrcMatcher = new Vector<Matcher>();
	private List<Matcher> asDstMatcher = new Vector<Matcher>();
	public static final int SRC_ONLY = 0; 
	public static final int DST_ONLY = 1;
	public static final int BOTH_SRC_AND_DST = 2;
	// 
//    private String gw_src=null;
//    private String gw_dst=null;
    private String old_value = null;    
    
    private List<NonEditableASrouteEditPart> connectionList = new Vector<NonEditableASrouteEditPart>();
	@Override
	protected IFigure createFigure() {
		
		RuleBasedASRouteFigure toReturn = new RuleBasedASRouteFigure();
		// update connection calculates what matches something.
		//Unused lists usefull for external calls
		List<Element> routersList = new Vector<Element>();
		List<Element> linksList = new Vector<Element>();
		
		// real parameters
		String src = (((Element) getModel()).getAttribute("src"));
		String dst = ((Element) getModel()).getAttribute("dst");		
		String gw_dst = ((Element) getModel()).getAttribute("gw_dst");
		String gw_src = ((Element) getModel()).getAttribute("gw_src");
		List<Element> toCheckLinks = ModelHelper.nodeListToElementList(((Element) getModel()).getElementsByTagName(ElementList.LINK_CTN));

		calculateRegexpConnections(src, dst, gw_src, gw_dst, toCheckLinks, asSrcList, asDstList, asSrcMatcher, asDstMatcher, routersList, linksList, 
				BOTH_SRC_AND_DST);
		if (getWarning() && !getError()) {
			toReturn.setIcon("warning");
			System.out.println(warningLog);
		}
		if (getError()) {
			toReturn.setIcon("error");
			System.out.println(errorLog);
		}
		return toReturn;
	}
	
	private void updateVisuals()
	{
		if (getWarning() && !getError()) {
			((RuleBasedASRouteFigure) this.getFigure()).setIcon("warning");
			System.out.println(warningLog);
		}
		if (getError()) {
			((RuleBasedASRouteFigure) this.getFigure()).setIcon("error");
			System.out.println(errorLog);
		}
	}
	@Override
	public void performRequest(Request req) {
		if (req.getType().equals(REQ_OPEN)){
			Shell shell = getViewer().getControl().getShell();
			RuleBasedASRouteWizard wizard = new RuleBasedASRouteWizard((Element) getModel(), this);
			WizardDialog dialog = new WizardDialog(shell, wizard);
	        dialog.create();
	    	dialog.open();
	    	refreshVisuals();

		}
		}

	@Override
	protected void refreshVisuals() {
		if (!((Element) getModel()).getTextContent().equals(old_value))
		{
				updateConnections();
				updateVisuals();
				old_value = ((Element) getModel()).getTextContent();
		}
		super.refreshVisuals();
	}

	public void reset() {
		setWarning(false);
		setError(false);
		warningLog = "";
		errorLog = "";
		asSrcList.clear();
		asDstList.clear();
		asSrcMatcher.clear();
		asDstMatcher.clear();

		for (NonEditableASrouteEditPart e : connectionList) {
			e.setSource(null);
			e.setTarget(null);
			e.refresh();
			// ((Figure) e.getFigure()).erase();
			// e.unregister();
			// e.deactivate();
		}
		connectionList = new Vector<NonEditableASrouteEditPart>();
	}
	

	
	public void calculateRegexpConnections(
			String src, 
			String dst, 
			String gw_src, 
			String gw_dst,
			List<Element> toCheckLinks, 
			List<Element> asSrcList, 
			List<Element> asDstList, List<Matcher> asSrcMatcher,  
			List<Matcher> asDstMatcher, List<Element> routersList, List<Element> linksList,
			 int whichRouters) {

		// TODO : LBO add ref to Pattern for differences with the original Perl
		// 5 regexps
		// http://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html
		// Retrieving patterns

		// Cleaning error and warning logs and so on
		reset();

		Pattern ASsrc = Pattern.compile(src);
		Pattern ASdst = Pattern.compile(dst);

		// Constructing AS id list and cluster id list.
		NodeList AsList = ((Element) ((Element) getModel()).getParentNode())
				.getElementsByTagName(ElementList.AS);
		NodeList clusterList = ((Element) ((Element) getModel())
				.getParentNode()).getElementsByTagName(ElementList.CLUSTER);
		// Then try to find out if there is any matching AS and gateway

		String matchingASsrc = "";
		String matchingASdst = "";
		String matchingClsrc = "";
		String matchingCldst = "";
		// src
		boolean foundASsrc = lookingForAS(AsList, matchingASsrc, ASsrc,
				asSrcList, asSrcMatcher);
		boolean foundClsrc = lookingForAS(clusterList, matchingClsrc, ASsrc,
				asSrcList, asSrcMatcher);
		if (!foundASsrc && !foundClsrc) {// no matching link, error
			setError(true);
			errorLog += "No AS matching ASsrc " + ASsrc.toString() + "\n";
		}

		// dst
		boolean foundASdst = lookingForAS(AsList, matchingASdst, ASdst,
				asDstList, asDstMatcher);
		boolean foundCldst = lookingForAS(clusterList, matchingCldst, ASdst,
				asDstList, asDstMatcher);
		if (!foundASdst && !foundCldst) {// no matching link, error
			setError(true);
			errorLog += "No AS matching ASdst " + ASsrc.toString() + "\n";
		} else {
			errorLog += "found\n";
		}
		// Now I have a list of src and dest for connections. I have to check if
		// relevant links and
		// gateways exists
		for (int i = 0; i < asSrcList.size(); i++) {
			Element currASsrc = asSrcList.get(i);
			Matcher currMatcherSrc = asSrcMatcher.get(i);
			for (int j = 0; j < asDstMatcher.size(); j++) {
				Element currASdst = asDstList.get(j);
				Matcher currMatcherDst = asDstMatcher.get(j);
				
				checkLinks(currMatcherSrc, currMatcherDst, toCheckLinks, linksList);
				String currRexgexp = generateRegexp(gw_src, currMatcherSrc,
						currMatcherDst);
				Pattern gw_src_pattern = Pattern.compile(currRexgexp);
				if (whichRouters == DST_ONLY)
				{
					saveErrorAndWarningState();
					List<Element> throwAwayList = new Vector<Element>();
					checkRouters(currASsrc,
							currASsrc.getAttribute("id"), gw_src_pattern, throwAwayList);
					loadErrorAndWarningState();
					 
				}
				else {
					checkRouters(currASsrc,
							currASsrc.getAttribute("id"), gw_src_pattern, routersList);
				
				}

				currRexgexp = generateRegexp(gw_dst, currMatcherSrc,
						currMatcherDst);
				Pattern gw_dst_pattern = Pattern.compile(currRexgexp);
				if (whichRouters == SRC_ONLY)
				{
					saveErrorAndWarningState();
					List<Element> throwAwayList = new Vector<Element>();
					
					checkRouters(currASdst,
							currASdst.getAttribute("id"), gw_dst_pattern, throwAwayList);
					loadErrorAndWarningState();
				}
				else {
				checkRouters(currASdst,
						currASdst.getAttribute("id"), gw_dst_pattern, routersList);
				}
			}
		}
	}
	
	private void loadErrorAndWarningState() {
		errorLog = oldErrorLog;
		warningLog = oldWarningLog;
		error = oldError;
		warning = oldWarning;
				
		
	}

	private void saveErrorAndWarningState() {
		oldErrorLog = errorLog;
		oldWarningLog = warningLog;
		oldError = error;
		oldWarning = warning;
		
	}

	public List<Element> getMatchingASList(String toMatch)
	{
		Pattern ASsrc = Pattern.compile(toMatch);
		
		// Constructing AS id list and cluster id list.
		NodeList AsList = ((Element) ((Element) getModel()).getParentNode())
				.getElementsByTagName(ElementList.AS);
		NodeList clusterList = ((Element) ((Element) getModel())
				.getParentNode()).getElementsByTagName(ElementList.CLUSTER);
		// Then try to find out if there is any matching AS in it.
		String matchingASsrc = "";
		String matchingClsrc = "";
	
		List<Element> asSrcList = new Vector<Element>();
		List<Matcher> asSrcMatcher = new Vector<Matcher>();
		boolean foundAS = lookingForAS(AsList, matchingASsrc, ASsrc,
				asSrcList, asSrcMatcher);
		boolean foundCluster = lookingForAS(clusterList, matchingClsrc, ASsrc,
				asSrcList, asSrcMatcher);
		if (!foundAS && !foundCluster) {// no matching link, error
			setError(true);
			errorLog += "No AS matching ASdst " + ASsrc.toString() + "\n";
		} else {
			errorLog += "found\n";
		}
		return asSrcList;
	}
	
	public boolean lookingForAS(NodeList AsList, String matchingAS,
			Pattern pattern, List<Element> asList, List<Matcher> matcherList) {
		boolean foundAS = false;
		for (int i = 0; i < AsList.getLength(); i++) {
			Node currNode = AsList.item(i);
			String currASName = currNode.getAttributes().getNamedItem("id")
					.getNodeValue();
			Matcher matcherASpattern = pattern.matcher(currASName);
			if (matcherASpattern.matches()) {
				asList.add((Element) currNode);
				matcherList.add(matcherASpattern);

				foundAS = true;
				matchingAS += " " + currASName;
			}

		}
		return foundAS;
	}

	public boolean checkRouters(Node currNode, String currASName,
			Pattern pattern, List<Element> routersList) {
		
		// looking for a correct gateway
		boolean foundASgw = false;
		String matchingRouterFound = "";
		if (currNode.getNodeName().equals(ElementList.AS)) {
			List<Element> routers = ModelHelper.getRouters((Element) currNode);
			for (int j = 0; j < routers.size(); j++) {
				String currRouterName = routers.get(j).getAttribute("id");
				Matcher matcherASgw = pattern.matcher(currRouterName);
				if (matcherASgw.matches()) {
					if (foundASgw) {// Multiple routers matching, warning
						setWarning(true);
						warningLog += "Multiple routers matching "
								+ pattern.toString() + ": " + currRouterName
								+ " and " + matchingRouterFound + "\n";
					}
					foundASgw = true;
					matchingRouterFound = currRouterName;
					if (!routersList.contains(routers.get(j))) {
						routersList.add(routers.get(j));
					}
				}
			}
		} else {// it's a cluster captain.

			String routerId = ModelHelper
					.getGatewayRouterId((Element) currNode);
			Matcher matcherASgw = pattern.matcher(routerId);
			if (matcherASgw.matches()) {				
				foundASgw = true;
				matchingRouterFound = routerId;				
				if (!routersList.contains((Element) currNode)) {
					routersList.add((Element) currNode);
				}
			}
		}

		if (!foundASgw) {// no matching link, error
			setError(true);
			errorLog += "AS "
					+ currASName
					+ " matches regexp but no router found in it that matches regexp "
					+ pattern.toString() + "\n";
			return false;
		}
		return true;
	}

    public List<Element> getMatchingLink(Matcher src, Matcher dst, Element linkCtn)
    {
    	List<Element> toReturn = new Vector<Element>();
    	List<Element> possibleLinks = ModelHelper
				.getLinks((Element) getModel());
    	String currRegexp = ((Node) linkCtn).getAttributes().getNamedItem("id").getNodeValue();
		// replacing $src and $dst with correct values
		currRegexp = generateRegexp(currRegexp, src, dst);
		Pattern pattern = Pattern.compile(currRegexp);
		boolean found = false;
			for (int j = 0; j < possibleLinks.size(); j++) {
				String currLink = possibleLinks.get(j).getAttribute("id");
				Matcher matcher = pattern.matcher(currLink);
				if (matcher.matches()) {
					if (found) {// spitting a warning as multiple links
								// matches the regexp
						setWarning(true);
						warningLog += "Multiple links matching "
								+ pattern.toString() + ". \n";
					}
					found = true;
					toReturn.add(possibleLinks.get(j));
				}
			}
			if (!found) {// no matching link, error
				setError(true);
				errorLog += "No link matching " + pattern.toString()
						+ " thus the route is undefined\n";				
			}

    	return toReturn; 
    	
    }
	public boolean checkLinks(Matcher src, Matcher dst, 
			List<Element> toCheckLinks, 
			List<Element> toReturnLinkList) {
		if (toCheckLinks== null)
		{
			return false;
		}
		if (toReturnLinkList == null)
		{
			toReturnLinkList = new Vector<Element>();
		}
		// getting group in matcher

		// retrieving link_ctn
		boolean linksOk = true;
		for (int i = 0; i < toCheckLinks.size(); i++) {
			toReturnLinkList.addAll(getMatchingLink(src, dst,toCheckLinks.get(i)));			
		}
		return linksOk;
	}

	public String generateRegexp(String currRegexp, Matcher src, Matcher dst) {
		for (int i = 1; i <= src.groupCount(); i++) {
			currRegexp = currRegexp.replaceAll("\\$" + i + "src", src.group(i));
		}
		for (int i = 1; i <= dst.groupCount(); i++) {
			currRegexp = currRegexp.replaceAll("\\$" + i + "dst", dst.group(i));
		}
		return currRegexp;
	}

	public String getErrorLog() {
		return errorLog;
	}

	public String getWarningLog() {
		return warningLog;
	}

	public boolean foundError() {
		return getError();
	}

	public boolean foundWarning() {
		return getWarning();
	}

	public void updateConnections() {
		
		List<Element> routersList = new Vector<Element>();
		List<Element> linksList = new Vector<Element>();
		String src = (((Element) getModel()).getAttribute("src"));
		String dst = ((Element) getModel()).getAttribute("dst");		
		String gw_dst = ((Element) getModel()).getAttribute("gw_dst");
		String gw_src = ((Element) getModel()).getAttribute("gw_src");
		List<Element> toCheckLinks = ModelHelper.nodeListToElementList(((Element) getModel())
		.getElementsByTagName(ElementList.LINK_CTN));

		calculateRegexpConnections(src, dst, gw_src, gw_dst, toCheckLinks, asSrcList, asDstList, asSrcMatcher, asDstMatcher, routersList, linksList, BOTH_SRC_AND_DST);

		List<NonEditableASrouteEditPart> toReturn = new Vector<NonEditableASrouteEditPart>();
		Map<?, ?> reg = this.getViewer().getEditPartRegistry();
		for (int i = 0; i < asSrcList.size(); i++) {
			if (reg.get(asSrcList.get(i)) != null) {				
				String key = (((Element) this.getModel()).getAttribute("dst")
						 + ((Element) this.getModel()).getAttribute("src")+
						 (asSrcList.get(i)).getAttribute("id"));
				NonEditableASrouteEditPart currRoute = (NonEditableASrouteEditPart) SimgridEditPartFactory.INSTANCE
						.createEditPart(this, ElementList.NON_EDITABLE_AS_ROUTE+key);
				AbstractElementEditPart currsrc = (AbstractElementEditPart) reg
						.get(asSrcList.get(i));
				if (currsrc==null) {// I have to find my parent which is into the same AS
					Element eltSrc = ModelHelper.getCommonAncestor((Element) this.getModel(), asSrcList.get(i));
					currsrc = (AbstractElementEditPart) reg
							.get(eltSrc);
				}
				currRoute.setTarget(this);
				currRoute.setSource(currsrc);
				 
				currRoute.register();
				toReturn.add(currRoute);
				if (asDstList.contains(asSrcList.get(i))) {
					// TODO: add arrows
					// currRoute.getConnectionFigure().
				}
			}

		}
		for (int i = 0; i < asDstList.size(); i++) {
			
			if (!asSrcList.contains(asDstList.get(i))) {
				String key = (
						((Element) this.getModel()).getAttribute("src") + ((Element) this.getModel()).getAttribute("dst") + 
						(asDstList.get(i)).getAttribute("id"));

				NonEditableASrouteEditPart currRoute = (NonEditableASrouteEditPart) SimgridEditPartFactory.INSTANCE
						.createEditPart(this, ElementList.NON_EDITABLE_AS_ROUTE+key);
				AbstractElementEditPart currdst = (AbstractElementEditPart) reg
						.get(asDstList.get(i));	
				if (currdst==null) {// I have to find my parent which is into the same AS
					Element eltDst = ModelHelper.getCommonAncestor((Element) this.getModel(), asDstList.get(i));
					currdst = (AbstractElementEditPart) reg
							.get(eltDst);
				}
				currRoute.setTarget(this);
				currRoute.setSource(currdst);
				currRoute.register();
				toReturn.add(currRoute);

			}

		}
		connectionList = toReturn;
	}
    public List<Element> getMatchingGateways(String src, String dst, String gw_src, String gw_dst, int whichRouters)
    {
    	List<Element> asSrcList = new Vector<Element>();
    	List<Element> asDstList = new Vector<Element>();
    	List<Matcher> asSrcMatcher = new Vector<Matcher>();
    	List<Matcher> asDstMatcher = new Vector<Matcher>();
		List<Element> routersList = new Vector<Element>();
		List<Element> linksList = new Vector<Element>();
		List<Element> toCheckLinks = null; 
		calculateRegexpConnections(src, dst, gw_src, gw_dst, toCheckLinks, asSrcList, asDstList, asSrcMatcher, asDstMatcher, routersList, linksList, whichRouters);
		
    	return routersList;
    }
    public List<Element> getMatchingLinks(List<Element> toCheckLinks, String src, String dst, String gw_src, String gw_dst)
    {
    	List<Element> asSrcList = new Vector<Element>();
    	List<Element> asDstList = new Vector<Element>();
    	List<Matcher> asSrcMatcher = new Vector<Matcher>();
    	List<Matcher> asDstMatcher = new Vector<Matcher>();
		List<Element> routersList = new Vector<Element>();
		List<Element> linksList = new Vector<Element>();
		 
		calculateRegexpConnections(src, dst, gw_src, gw_dst, toCheckLinks, asSrcList, asDstList, asSrcMatcher, asDstMatcher, routersList, linksList, BOTH_SRC_AND_DST);
		
    	return linksList;
    }
	public List getConnectionList() {
		return this.connectionList;
	}

	public void setConnectionList(
			List<NonEditableASrouteEditPart> connectionList) {
		this.connectionList = connectionList;
	}

	public Boolean getWarning() {
		return warning;
	}

	public void setWarning(Boolean warning) {
		this.warning = warning;
	}

	public Boolean getError() {
		return error;
	}

	public void setError(Boolean error) {
		this.error = error;
	}
	public String logText() {
		if (foundError() || foundWarning()) { 
		return "Warnings \n"  + getWarningLog() + "Errors \n " + getErrorLog();
		}
		else {return "No problem found";}
	}

}

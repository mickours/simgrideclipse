package simgrideclipseplugin.wizards;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import simgrideclipseplugin.graphical.providers.SimgridIconProvider;
import simgrideclipseplugin.model.ElementList;
import simgrideclipseplugin.model.ModelHelper;
import simgrideclipseplugin.model.SimgridRules;
import simgrideclipseplugin.wizards.composites.ElementSelectionList;

public class LinkSelectionPage extends WizardPage implements Listener {

	protected ElementSelectionList routeViewer;
	protected ElementSelectionList availableLinkViewer;
	protected List<Element> availableLinks;
	protected LinkCtnList routeList;
	protected Element refNode;
	protected boolean isMultilink;
	
	private Button toLeft;
	private Button toRight;
	protected Button up;
	protected Button down;
	protected Button plus;
	protected Button edit;


	/**
	 * Construct the Link selection page with the available Links list and a reference node
	 * that can can be a node in the current AS or the route to edit
	 * @param availableLinks
	 * @param refNode
	 * @param isMultilink
	 */
	public LinkSelectionPage(List<Element> availableLinks, Element refNode, boolean isMultilink) {
		super("Route editing", "Route editing", SimgridIconProvider.getIconImageDescriptor(ElementList.LINK_CTN));
		this.availableLinks = availableLinks;
		this.isMultilink = isMultilink;
		routeList = new LinkCtnList();
		this.refNode = refNode;
	}

	@Override
	public void createControl(Composite parent) {
		// create the composite to hold the widgets 
		Composite composite = new Composite(parent, SWT.NONE);
	    // create the desired layout for this wizard page
		GridLayout gl = new GridLayout();
	    int ncol = 4;
	    gl.numColumns = ncol;
	    composite.setLayout(gl);
	    
	    //top labels
	    Label l = new Label (composite, SWT.NONE);
	    l.setText("the new route");
	    GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = (ncol/2)+1;
	    l.setLayoutData(gd);
	    
	    l = new Label (composite, SWT.NONE);
	    l.setText("available links");
	    gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = (ncol/2)-1;
	    l.setLayoutData(gd);
	    
	    //left list 
	    routeViewer = new ElementSelectionList(composite);
	    final Table table = routeViewer.getTable();
	    table.setHeaderVisible(true);
	    // First column is for link_ctn property
	    TableViewerColumn col = new TableViewerColumn(routeViewer, SWT.NONE);
	    col.getColumn().setWidth(70);
	    col.getColumn().setText("direction");
	    col.setEditingSupport(new EditingSupport(routeViewer) {
			private String[] valList = ElementList.getValueList(ElementList.LINK_CTN, "direction")
					.toArray(new String[0]);
	    	
			@Override
			protected void setValue(Object element, Object value) {
				((LinkCtn)element).setDir(valList[(Integer) value]);
				getViewer().refresh();
				update();
			}
			
			@Override
			protected Object getValue(Object element) {
				String dir = ((LinkCtn)element).getDir();
				for (int i=0;i< valList.length; i++){
					if (valList[i].equals(dir)) return i;
				}
				return 0;
			}
			
			@Override
			protected CellEditor getCellEditor(Object element) {
				return new ComboBoxCellEditor(routeViewer.getTable(),valList,SWT.READ_ONLY);
			}
			
			@Override
			protected boolean canEdit(Object element) {
				return true;
			}
		});
	    col.setLabelProvider(new ColumnLabelProvider(){
	    	@Override
	    	public String getText(Object element) {
	    		LinkCtn l = (LinkCtn) element;
	    		return l.getDir();
	    	}
	    });
	    
	    //second column for the links
	    col = new TableViewerColumn(routeViewer, SWT.NONE);
	    col.getColumn().setWidth(500);
	    col.getColumn().setText("link");
	    col.setLabelProvider(
		    new ColumnLabelProvider(){
		    	@Override
		    	public Image getImage(Object element) {
		    		Element elem = ((LinkCtn) element).getLink();
		    		return SimgridIconProvider.getIcon(elem.getTagName()+"_small");
		    	}
	
		    	@Override
		    	public String getText(Object element) {
		    		Element elem = ((LinkCtn) element).getLink();
		    		String desc = elem.getTagName();
		    		NamedNodeMap nm = elem.getAttributes();
		    		for (int i=0; i< nm.getLength(); i++){
		    			Node node = nm.item(i);
		    			desc += " "+node.getNodeName()+"=\""+node.getNodeValue()+"\"";
		    		}
		    		return desc;
		    	}
	
				@Override
				public String getToolTipText(Object element) {
					return getText(element);
				}
	    	
	    });
	    routeViewer.setInput(routeList);
	    //set routeViewer layout
	    GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
		int listHeight = (routeViewer.getItemHeight() * 6) +routeViewer.getItemHeight()/2;
		int listWidth = 300;
		routeViewer.getTable().setSize(listWidth, listHeight);
		gridData.heightHint = listHeight;
		gridData.widthHint = listWidth;
		routeViewer.getTable().setLayoutData(gridData);
	    
		//middle buttons
	    toLeft = new Button(composite, SWT.ARROW | SWT.LEFT);
	    toLeft.addListener(SWT.Selection, this);
	    toRight = new Button(composite, SWT.ARROW | SWT.RIGHT);
	    toRight.addListener(SWT.Selection, this);
	    
	    //right list available links
	    Composite rightContainer = new Composite(composite,SWT.NONE);
	    gl = new GridLayout();
	    rightContainer.setLayout(gl);
	    gd = new GridData(GridData.FILL, GridData.FILL, true, true);
	    rightContainer.setLayoutData(gd);
//	    
//	    Label searchLabel = new Label(rightContainer, SWT.NONE);
//		searchLabel.setText("Search: ");
		
		final Text searchText = new Text(rightContainer, SWT.BORDER | SWT.SEARCH);
		searchText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
				| GridData.HORIZONTAL_ALIGN_FILL));
		
	    availableLinkViewer = new ElementSelectionList(rightContainer);
	    availableLinkViewer.getTable().setLinesVisible(true);
	    //table layout
	    gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
		availableLinkViewer.getTable().setSize(listWidth, listHeight);
		gridData.heightHint = listHeight;
		gridData.widthHint = listWidth;
		availableLinkViewer.getTable().setLayoutData(gridData);
		
		//table filter
	    final ElementIdFilter filter = new ElementIdFilter();
 		searchText.addKeyListener(new KeyAdapter() {
 			@Override
			public void keyReleased(KeyEvent ke) {
 				filter.setSearchText(searchText.getText());
 				availableLinkViewer.refresh();
 			}

 		});
 		availableLinkViewer.addFilter(filter);
 		
	    availableLinkViewer.setInput(availableLinks);
	    
		//up down buttons
		Composite c = new Composite(composite, SWT.NONE);
		c.setLayout(new GridLayout(3,false));
		up =  new Button(c, SWT.ARROW | SWT.UP);
		up.addListener(SWT.Selection, this);
	    down = new Button(c, SWT.ARROW | SWT.DOWN);
	    down.addListener(SWT.Selection, this);
	    edit = new Button(c,SWT.PUSH);
	    edit.setText("edit");
	    edit.addListener(SWT.Selection, this);
	    gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = (ncol/2)+1;
	    c.setLayoutData(gd);
	    
	    //add a new link button
	    c = new Composite(composite, SWT.NONE);
		c.setLayout(new GridLayout(2,true));
		plus =  new Button(c, SWT.PUSH);
		plus.setImage(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_OBJ_ADD).createImage());
		plus.addListener(SWT.Selection, this);
	    gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = (ncol/2)-1;
	    l.setLayoutData(gd);
	    
	    parent.pack();
	    setControl(composite);
	    
	    if (availableLinks.isEmpty()){
	    	setErrorMessage("you must have at least one link in this container or in his descendants");
	    }
	    
	    //initialize route List
	    if (SimgridRules.isConnection(refNode.getTagName())){
	    	ModelHelper.fillRouteLinksList(refNode,routeList);
	    	availableLinks.removeAll(routeList.getLinkList());
	    }
    	update();
	}

	@Override
	public void handleEvent(Event event) {
		if (event.widget == plus){
			CreateElementWizard wizard = new CreateElementWizard(ElementList.LINK);
			WizardDialog dialog = new WizardDialog(getShell(), wizard);
	        dialog.create();
	    	dialog.open();
	    	if (dialog.getReturnCode()== Window.OK){
	    		Element link = ModelHelper.createAndAddLink( refNode, wizard.attrMap);
	    		availableLinks.add(link);
	    	}
		}
		else if (event.widget == toLeft) {
			if(!isMultilink && routeList.size() == 1){
				String routing = ((Element)refNode.getParentNode()).getAttribute("routing");
				MessageBox mb = new MessageBox(getShell(),SWT.ICON_WARNING);
				mb.setMessage("The route with multiple links is not allowed " +
						"with the actual routing \""+routing+"\"");
				mb.open();
			}
			else{
				IStructuredSelection sel = (StructuredSelection) availableLinkViewer
						.getSelection();
				if (!sel.isEmpty()) {
					Element e = (Element)sel.getFirstElement();
					LinkCtn l = routeList.addLast(e);
					availableLinks.remove(e);
					update();
					routeViewer.setSelection(new StructuredSelection(l));
				}
			}
		} else if (!routeViewer.getSelection().isEmpty()) {
			IStructuredSelection sel = (StructuredSelection) routeViewer
					.getSelection();
			LinkCtn l = (LinkCtn)sel.getFirstElement();
			Element e = l.getLink();
			if (event.widget == edit){
				EditElementWizard wizard = new EditElementWizard(e);
				WizardDialog dialog = new WizardDialog(getShell(), wizard);
		        dialog.create();
		    	dialog.open();
			} else if (event.widget == toRight) {
				routeList.remove(l);
				availableLinks.add(e);
				update();
				availableLinkViewer.setSelection(new StructuredSelection(e));
			} else if (event.widget == up && routeList.indexOf(l) > 0) {
				routeList.moveUp(l);
				
			} else if (event.widget == down && routeList.indexOf(l) < routeList.size()-1) {
				routeList.moveDown(l);
			}
		}
		update();
	}
	
	protected void update(){
		//update data
		((AbstractElementWizard)getWizard()).linkCtnList = routeList.getLinkList();
		((AbstractElementWizard)getWizard()).linkCtnDirectionList = routeList.getDirListList();
		//update UI
		boolean isComplete = routeList.size() > 0;
		if(isComplete){
			setErrorMessage(null);
		}
		else{
			setErrorMessage("Your route must contains at least one link");
		}
		setPageComplete(isComplete);
		
		routeViewer.setInput(routeList);
		availableLinkViewer.setInput(availableLinks);
	}
	
	protected class ElementIdFilter extends ViewerFilter{
    	private String searchString;

    	public void setSearchText(String s) {
    		// Search must be a substring of the existing value
    		this.searchString = ".*" + s + ".*";
    	}
		
		@Override
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			if (searchString == null || searchString.length() == 0) {
				return true;
			}
			Element elem = (Element) element;
			String id = elem.getAttribute("id");
			if (id.matches(searchString) || id.toLowerCase().matches(searchString)){
				return true;
			}
			return false;
		}
	}
	
	public class LinkCtnList extends java.util.LinkedList<LinkCtn>{

		private static final long serialVersionUID = 1L;

		public void setRoute(List<Element> l){
			for (Element e : l){
				addLast(new LinkCtn(e));
			}
		}

		public List<String> getDirListList() {
			LinkedList<String> l = new LinkedList<String>();
			for (LinkCtn link : this){
				l.addLast(link.getDir());
			}
			return l;
		}

		public List<Element> getLinkList() {
			LinkedList<Element> l = new LinkedList<Element>();
			for (LinkCtn link : this){
				l.addLast(link.getLink());
			}
			return l;
		}

		public LinkCtn addLast(Element e) {
			LinkCtn l = new LinkCtn(e);
			addLast(l);
			return l;
		}
		
		public void moveUp(LinkCtn link){
			int index = indexOf(link);
			remove(link);
			add(index-1, link);
		}
		
		public void moveDown(LinkCtn link){
			int index = indexOf(link);
			remove(link);
			add(index+1, link);
		}
	}
	
	public class LinkCtn{
		private Element link;
		private String dir = ElementList.getDefaultValue(ElementList.LINK_CTN, "direction");
		
		public LinkCtn(Element link) {
			super();
			this.link = link;
		}
		public String getDir(){
			return dir;
		}
		public void setDir(String dir){
			this.dir = dir;
		}
		
		public Element getLink(){
			return link;
		}
	}

}

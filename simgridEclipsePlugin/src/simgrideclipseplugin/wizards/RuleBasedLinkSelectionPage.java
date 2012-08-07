package simgrideclipseplugin.wizards;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;

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

import simgrideclipseplugin.graphical.parts.RuleBasedASRouteEditPart;
import simgrideclipseplugin.graphical.providers.SimgridIconProvider;
import simgrideclipseplugin.model.ElementList;
import simgrideclipseplugin.model.ModelHelper;
import simgrideclipseplugin.model.SimgridRules;
import simgrideclipseplugin.wizards.LinkSelectionPage.LinkCtnList;
import simgrideclipseplugin.wizards.composites.ElementSelectionList;

public class RuleBasedLinkSelectionPage extends LinkSelectionPage implements
		Listener {
	private RuleBasedASRouteEditPart editPart;
	private String src, dst;
	private String gw_src, gw_dst;

	// private ElementSelectionList routeViewer;
	// private ElementSelectionList availableLinkViewer;
	// private List<Element> availableLinks;
	// private LinkCtnList routeList;
	// private Element refNode;
	// private boolean isMultilink;
	//
	// private Button toLeft;
	// private Button toRight;
	// private Button up;
	// private Button down;
	private Button plusRegexp;
	private Button minusRegexp;

	Text logs;
	// private Button edit;

	private List<Element> linkRegexpList = new Vector<Element>();

	/**
	 * Construct the Link selection page with the available Links list and a
	 * reference node that can can be a node in the current AS or the route to
	 * edit
	 * 
	 * @param availableLinks
	 * @param refNode
	 * @param isMultilink
	 * @param the
	 *            calling editPart.
	 */
	public RuleBasedLinkSelectionPage(Element refNode,
			RuleBasedASRouteEditPart editPart, String src, String dst,
			String gw_src, String gw_dst) {
		super(new Vector<Element>(), refNode, true);
		this.src = src;
		this.dst = dst;
		this.gw_src = gw_src;
		this.gw_dst = gw_dst;
		this.editPart = editPart;

	}

	@Override
	public void createControl(Composite parent) {
		// create the composite to hold the widgets
		Composite composite = new Composite(parent, SWT.NONE);
		// create the desired layout for this wizard page
		GridLayout gl = new GridLayout();
		int ncol = 2;
		gl.numColumns = ncol;
		composite.setLayout(gl);

		// top labels
		Label l = new Label(composite, SWT.NONE);
		l.setText("Route");
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 1;
		l.setLayoutData(gd);

		l = new Label(composite, SWT.NONE);
		l.setText("Matching links (for any matching AS)");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 1;
		l.setLayoutData(gd);

		// left list
		routeViewer = new ElementSelectionList(composite);
		final Table table = routeViewer.getTable();
		table.addListener(SWT.Selection, this);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		// First column is for link_ctn property
		TableViewerColumn col = new TableViewerColumn(routeViewer, SWT.NONE);
		col.getColumn().setWidth(70);
		col.getColumn().setText("direction");
		col.setEditingSupport(new EditingSupport(routeViewer) {
			private String[] valList = ElementList.getValueList(
					ElementList.LINK_CTN, "direction").toArray(new String[0]);

			@Override
			protected void setValue(Object element, Object value) {
				((LinkCtn) element).setDir(valList[(Integer) value]);
				getViewer().refresh();
				update();
			}

			@Override
			protected Object getValue(Object element) {
				String dir = ((LinkCtn) element).getDir();
				for (int i = 0; i < valList.length; i++) {
					if (valList[i].equals(dir))
						return i;
				}
				return 0;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				return new ComboBoxCellEditor(routeViewer.getTable(), valList,
						SWT.READ_ONLY);
			}

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}
		});
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((Element) element).getAttribute("direction");
			}
		});

		// second column for the links
		col = new TableViewerColumn(routeViewer, SWT.NONE);
		col.getColumn().setWidth(500);
		col.getColumn().setText("link");
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Image getImage(Object element) {
				return SimgridIconProvider.getIcon(ElementList.LINK_CTN
						+ "_small");
			}

			@Override
			public String getText(Object element) {
				Element elem = (Element) element;
				String desc = elem.getTagName();
				NamedNodeMap nm = elem.getAttributes();
				for (int i = 0; i < nm.getLength(); i++) {
					Node node = nm.item(i);
					desc += " " + node.getNodeName() + "=\""
							+ node.getNodeValue() + "\"";
				}
				return desc;
			}

			@Override
			public String getToolTipText(Object element) {
				return getText(element);
			}

		});
		linkRegexpList = ModelHelper.nodeListToElementList(refNode
				.getElementsByTagName(ElementList.LINK_CTN));
		routeViewer.setInput(linkRegexpList);
		// set routeViewer layout
		GridData gridData = new GridData(GridData.FILL, GridData.FILL, true,
				true);
		int listHeight = (routeViewer.getItemHeight() * 6)
				+ routeViewer.getItemHeight() / 2;
		int listWidth = 300;
		routeViewer.getTable().setSize(listWidth, listHeight);
		gridData.heightHint = listHeight;
		gridData.widthHint = listWidth;
		routeViewer.getTable().setLayoutData(gridData);

		// middle buttons
		// toLeft = new Button(composite, SWT.ARROW | SWT.LEFT);
		// toLeft.addListener(SWT.Selection, this);
		// toRight = new Button(composite, SWT.ARROW | SWT.RIGHT);
		// toRight.addListener(SWT.Selection, this);
		//
		// right list available links
		Composite rightContainer = new Composite(composite, SWT.NONE);
		gl = new GridLayout();
		rightContainer.setLayout(gl);
		gd = new GridData(GridData.FILL, GridData.FILL, true, true);
		rightContainer.setLayoutData(gd);
		//
		// Label searchLabel = new Label(rightContainer, SWT.NONE);
		// searchLabel.setText("Search: ");private

		// final Text searchText = new Text(rightContainer, SWT.BORDER |
		// SWT.SEARCH);
		// searchText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
		// | GridData.HORIZONTAL_ALIGN_FILL));

		availableLinkViewer = new ElementSelectionList(rightContainer);
		availableLinkViewer.getTable().setLinesVisible(true);
		// table layout
		gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
		availableLinkViewer.getTable().setSize(listWidth, listHeight);
		gridData.heightHint = listHeight;
		gridData.widthHint = listWidth;
		availableLinkViewer.getTable().setLayoutData(gridData);

		// table filter
		// final ElementIdFilter filter = new ElementIdFilter();
		// searchText.addKeyListener(new KeyAdapter() {
		// @Override
		// public void keyReleased(KeyEvent ke) {
		// //TODO: switch to the regexp elements
		// filter.setSearchText(searchText.getText());
		// availableLinkViewer.refresh();
		// }

		// });
		// availableLinkViewer.addFilter(filter);
		// String toMatch = routeViewer.getSelection().toString();

		availableLinkViewer.setInput(availableLinks);

		// up down buttons
		Composite c = new Composite(composite, SWT.NONE);
		c.setLayout(new GridLayout(5, false));
		up = new Button(c, SWT.ARROW | SWT.UP);
		up.addListener(SWT.Selection, this);
		down = new Button(c, SWT.ARROW | SWT.DOWN);
		down.addListener(SWT.Selection, this);

		edit = new Button(c, SWT.PUSH);
		edit.setText("edit");
		edit.addListener(SWT.Selection, this);
		// gd = new GridData(GridData.FILL_HORIZONTAL);
		// gd.horizontalSpan = (ncol/2)+1;
		// c.setLayoutData(gd);

		// add a new regexp button
		// c = new Composite(composite, SWT.NONE);
		// c.setLayout(new GridLayout(2,true));
		plusRegexp = new Button(c, SWT.PUSH);
		plusRegexp.setImage(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_OBJ_ADD).createImage());
		plusRegexp.addListener(SWT.Selection, this);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = (ncol / 2) - 1;
		l.setLayoutData(gd);

		// add a new remove regexp button
		minusRegexp = new Button(c, SWT.PUSH);
		minusRegexp.setImage(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_ELCL_REMOVE)
				.createImage());
		minusRegexp.addListener(SWT.Selection, this);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = (ncol / 2) - 1;
		l.setLayoutData(gd);

		// add a new link button
		c = new Composite(composite, SWT.NONE);
		c.setLayout(new GridLayout(2, true));
		plus = new Button(c, SWT.PUSH);
		plus.setImage(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_OBJ_ADD).createImage());
		plus.addListener(SWT.Selection, this);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = (ncol / 2) - 1;
		l.setLayoutData(gd);

		// showing logs.
		// top label
		l = new Label(composite, SWT.NONE);
		l.setText("Logs");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		l.setLayoutData(gd);

		logs = new Text(composite, SWT.MULTI | SWT.BORDER | SWT.WRAP
				| SWT.V_SCROLL);
		logs.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true,
				true));
		gd = new GridData(GridData.FILL_HORIZONTAL);
		// ugly setting here
		gd.heightHint = 200;
		gd.horizontalSpan = 2;
		logs.setLayoutData(gd);

		logs.setEditable(false);
		editPart = ((RuleBasedASRouteWizard) getWizard()).getEditPart();

		logs.setText(editPart.logText());

		parent.pack();
		setControl(composite);

		if (availableLinks.isEmpty()) {
			setErrorMessage("you must have at least one link possibly matching in this container or in his descendants");
		}
		update();
	}

	@Override
	public void handleEvent(Event event) {
		if (event.widget == plus) {
			CreateElementWizard wizard = new CreateElementWizard(
					ElementList.LINK);
			WizardDialog dialog = new WizardDialog(getShell(), wizard);
			dialog.create();
			dialog.open();
			if (dialog.getReturnCode() == Window.OK) {
				Element link = ModelHelper.createAndAddLink(refNode,
						wizard.attrMap);
				// availableLinks = editPart.getMatchingLinks(currElt, src, dst,
				// gw_src, gw_dst);

			}

		} else if (event.widget == plusRegexp) {
			RuleBasedCreateElementWizard wizard = new RuleBasedCreateElementWizard(
					ElementList.LINK_CTN);
			WizardDialog dialog = new WizardDialog(getShell(), wizard);
			dialog.create();
			dialog.open();
			if (dialog.getReturnCode() == Window.OK) {
				Element linkCtn = ModelHelper.createElement(
						ElementList.LINK_CTN, wizard.attrMap);
				ModelHelper.insertAtLast(refNode, linkCtn);
				linkRegexpList.add(linkCtn);
				editPart.getMatchingLinks(linkRegexpList, src, dst, gw_src,
						gw_dst);
				logs.setText(editPart.logText());
			}

		}
		else if (event.widget == minusRegexp) {
			if (!routeViewer.getSelection().isEmpty()) {
				IStructuredSelection sel = (StructuredSelection) routeViewer
						.getSelection();
				Element e = (Element) sel.getFirstElement();
				ModelHelper.removeElement(e);
				linkRegexpList.remove(e);
				editPart.getMatchingLinks(linkRegexpList, src, dst, gw_src,
						gw_dst);
				logs.setText(editPart.logText());
			}

		} 
		
		else if (!routeViewer.getSelection().isEmpty()) {
			IStructuredSelection sel = (StructuredSelection) routeViewer
					.getSelection();
			Element e = (Element) sel.getFirstElement();
			List<Element> currElt = new Vector<Element>();
			currElt.add(e);
			availableLinks = editPart.getMatchingLinks(currElt, src, dst,
					gw_src, gw_dst);
			availableLinkViewer.setInput(availableLinks);
			if (event.widget == edit) {
				RuleBasedEditElementWizard wizard = new RuleBasedEditElementWizard(
						e);
				WizardDialog dialog = new WizardDialog(getShell(), wizard);
				dialog.create();
				dialog.open();
				availableLinks = editPart.getMatchingLinks(currElt, src, dst,
						gw_src, gw_dst);
				availableLinkViewer.setInput(availableLinks);

			} else
			// if (event.widget == toRight) {
			// routeList.remove(l);
			// availableLinks.add(e);
			// update();
			// availableLinkViewer.setSelection(new StructuredSelection(e));
			// } else
			if (event.widget == up && linkRegexpList.indexOf(e) > 0) {
				int indexOfe = linkRegexpList.indexOf(e);
				Element temp = linkRegexpList.get(indexOfe - 1);
				linkRegexpList.set(indexOfe, temp);
				linkRegexpList.set(indexOfe - 1, e);

			} else if (event.widget == down
					&& linkRegexpList.indexOf(e) < linkRegexpList.size() - 1) {
				int indexOfe = linkRegexpList.indexOf(e);
				Element temp = linkRegexpList.get(indexOfe + 1);
				linkRegexpList.set(indexOfe, temp);
				linkRegexpList.set(indexOfe + 1, e);
			}
		}
		logs.setText(editPart.logText());
		update();
	}

	@Override
	protected void update() {
		// update data
		// TODO: check if it's necessary
		((AbstractElementWizard) getWizard()).linkCtnList = linkRegexpList;
		((AbstractElementWizard) getWizard()).linkCtnDirectionList = routeList
				.getDirListList();
		// update UI
		boolean isComplete = linkRegexpList.size() > 0
				&& !editPart.foundError();
		if (isComplete) {
			setErrorMessage(null);
		} else {
			setErrorMessage("Your route must contains at least one link");
		}
		setPageComplete(isComplete);

		routeViewer.setInput(linkRegexpList);
		availableLinkViewer.setInput(availableLinks);
	}

}

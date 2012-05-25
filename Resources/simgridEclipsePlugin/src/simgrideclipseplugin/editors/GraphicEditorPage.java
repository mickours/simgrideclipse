package simgrideclipseplugin.editors;


import javax.swing.JComponent;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PartInitException;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.swingViewer.View;
import org.graphstream.ui.swingViewer.Viewer;
import org.graphstream.graph.Node;


import simgrideclipseplugin.utils.embeddedSwing.EmbeddedSwingComposite;

public class GraphicEditorPage extends EmbeddedSwingComposite   {

	// Holding GraphStream objects.
	private Graph graph;
	private Viewer viewer;
	private View view;
//	private ISelectionProvider structuredSelectionProvider;
//	private int index = -1;
//	private MultiPageXMLEditor parent;

	public GraphicEditorPage(Composite parent, int style) {
		super(parent, style);		
	}

	@Override
	protected JComponent createSwingComponent() {	    
      	   graph = new MultiGraph("embedded");      	
      	  viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
      	  
      	  view = viewer.addDefaultView(false);   // false indicates "no JFrame".			            	  			            	
      	  viewer.enableAutoLayout();
      	  // testing ... Default should be nothing, as there are no selected AS.
      	  
      	  feedingGraphForTestingPurposes();
      	  return view;
        }
	private void feedingGraphForTestingPurposes() {
   	  // Adding dummy values
	  graph.addNode("A" );
	  graph.addNode("B" );
	  graph.addNode("C" );
	  graph.addEdge("AB", "A", "B");
	  graph.addEdge("BC", "B", "C");
	  graph.addEdge("CA", "C", "A");
	  
	   //Testing some stupid stuff
	  for (Node node : graph) {
		    node.addAttribute("ui.label", node.getId());
		}
	}
	
	/**
	 * display the actual selection that should be displayed.
	 */
	public void display()
	{
		// Retrieving the current selection		
//		IStructuredSelection currSelection = (IStructuredSelection) structuredSelectionProvider.getSelection();
//		Iterator it = currSelection.iterator();

		// if (selection!=null && selection != platform)
				// if (selection==AS)
				//setPageText(index, "current AS name");
				//else
				//setPageText(index, "current element AS");
				//else {
		
	}
	/**
	 * @param provider the provider provided by the SSE
	 * The provider will be used for getting selection and 
	 * eventually changing it when needed.
	 */


	
//	public void setSelectionProvider(ISelectionProvider selectionProvider)
//	{
//		this.structuredSelectionProvider = selectionProvider;
//	}
//	/**
//	 * When creating a new upper level AS, other editors must be warned that 
//	 * selection changed.
//	 */
//	
//	public void setParentEditor(MultiPageXMLEditor mpe)
//	{
//		this.parent = mpe;
//	}
//	public void setIndex(int index)
//	{
//		this.index = index;
//	}
//	private void changeSelectedAS()
//	{
//		structuredSelectionProvider.setSelection(null);
//	}
	}


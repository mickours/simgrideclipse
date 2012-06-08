package simgrideclipseplugin.graphical;

import java.util.HashMap;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.RootEditPart;
import org.graphstream.algorithm.Toolkit;
import org.graphstream.graph.Node;
import org.graphstream.ui.graphicGraph.GraphicGraph;
import org.graphstream.ui.layout.springbox.SpringBox;

import simgrideclipseplugin.graphical.parts.PlatformEditPart;
import simgrideclipseplugin.graphical.parts.SimgridAbstractEditPart;

public class AutomaticGraphLayoutHelper {
	//private HashMap<String, Point> positionMap;
	private HashMap<String, SimgridAbstractEditPart> editPartMap;

	private SpringBox layoutManager;
	private GraphicGraph graph;
	private RootEditPart root;

	// define as a singleton
	public static final AutomaticGraphLayoutHelper INSTANCE = new AutomaticGraphLayoutHelper();

	public void init(RootEditPart root) {
		this.root = root;
		//positionMap = new HashMap<String, Point>();
		editPartMap = new HashMap<String, SimgridAbstractEditPart>();
		layoutManager = new SpringBox();
		layoutManager.setStabilizationLimit(0.5);
		graph = new GraphicGraph("id");
		layoutManager.addSink(graph);
		graph.addSink(layoutManager);
	}

	public void computeLayout() {
		//clear old data
		graph.clear();
		//positionMap.clear();
		editPartMap.clear();
		// populate the graph
		SimgridAbstractEditPart visualRoot = (SimgridAbstractEditPart) root
				.getContents();
		addNode(visualRoot);
		// compute layout
		int i = 0;
		do  {
			layoutManager.compute();
			i++;
			//TODO the i limit must depends on the number of nodes
		}while(layoutManager.getNodeMoved() != 0 && i < 300 );
		double xmin = 0, ymin = 0;
		//get the x and y min to translate
		for (Node n : graph.getEachNode()) {
			double pos[] = Toolkit.nodePosition(graph, n.getId());
			xmin = Math.min(pos[0], xmin);
			ymin = Math.min(pos[1], ymin);
		}
		
		// update position in the map
		for (Node n : graph.getEachNode()) {
			// get (position,id) from graph
			double pos[] = Toolkit.nodePosition(graph, n.getId());
			//TODO assign position depending on object size and zoom
			int x =  new Double((pos[0]+Math.abs(xmin))*50).intValue();
			int y =  new Double((pos[1]+Math.abs(ymin))*50).intValue();
			Point p = new Point(x, y);
			// set position
			ElementPositionMap.setPositionAndRefresh(editPartMap.get(n.getId()),p);
		}	
		((PlatformEditPart)root.getContents()).refresh();		
	}

	@SuppressWarnings("unchecked")
	private void addNode(SimgridAbstractEditPart node) {
		if (!(node instanceof PlatformEditPart) ) {
			String id = Integer.toString(node.hashCode());
			editPartMap.put(id, node);
			// FIXME I think it's not really a good idea...
			graph.addNode(id);
		}
		List<SimgridAbstractEditPart> l = node.getChildren();
		for (SimgridAbstractEditPart elem : l) {
			addNode(elem);
		}
	}
}

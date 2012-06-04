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

public class AutomaticGraphLayoutRenderer {
	//private HashMap<String, Point> positionMap;
	private HashMap<String, SimgridAbstractEditPart> editPartMap;

	private SpringBox layoutManager;
	private GraphicGraph graph;
	private RootEditPart root;

	// define as a singleton
	public static final AutomaticGraphLayoutRenderer INSTANCE = new AutomaticGraphLayoutRenderer();

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
		}while(layoutManager.getNodeMoved() != 0 && i < 100 );
		// update position in the map
		for (Node n : graph.getEachNode()) {
			// get (position,id) from graph
			double pos[] = Toolkit.nodePosition(graph, n.getId());
			int x =  new Double(pos[0]*100).intValue();
			int y =  new Double(pos[1]*100).intValue();
			Point p = new Point(x, y);
			//positionMap.put(n.getId(), new Point(x, y));
			// set position
			editPartMap.get(n.getId()).setPosition(p);
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

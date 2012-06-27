package simgrideclipseplugin.graphical;

import java.util.HashMap;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.graphstream.algorithm.Toolkit;
import org.graphstream.graph.Node;
import org.graphstream.ui.graphicGraph.GraphicGraph;
import org.graphstream.ui.layout.springbox.SpringBox;

import simgrideclipseplugin.graphical.parts.AbstractElementEditPart;
import simgrideclipseplugin.graphical.parts.ErrorEditPart;
import simgrideclipseplugin.graphical.parts.AbstConnectionEditPart;
import simgrideclipseplugin.graphical.parts.SimgridAbstractEditPart;

public class AutomaticGraphLayoutHelper {
	//private HashMap<String, Point> positionMap;
	private HashMap<String, SimgridAbstractEditPart> editPartMap;

	private SpringBox layoutManager;
	private GraphicGraph graph;
	private EditPart root;

	// define as a singleton
	public static final AutomaticGraphLayoutHelper INSTANCE = new AutomaticGraphLayoutHelper();

	public void init(EditPart root) {
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
		if (root instanceof ErrorEditPart){
			//there is an Error in the model
			return;
		}
		addNode((SimgridAbstractEditPart) root);
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
	}

	private void addNode(EditPart node) {
		String id = computeId(node);
		if (node instanceof AbstractElementEditPart) {
			editPartMap.put(id, (SimgridAbstractEditPart) node);
			graph.addNode(id);
		}
		else if (node instanceof AbstConnectionEditPart) {
			AbstConnectionEditPart connection = (AbstConnectionEditPart) node;
			graph.addEdge(id, computeId(connection.getSource()), computeId(connection.getTarget()));
		}
		List<?> l = node.getChildren();
		for (Object elem : l) {
			if (!(elem instanceof ErrorEditPart)){
				addNode((SimgridAbstractEditPart)elem);
			}
		}
	}
	
	private String computeId(EditPart node){
		return Integer.toString(node.hashCode());
	}
}

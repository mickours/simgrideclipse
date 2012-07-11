package simgrideclipseplugin.graphical;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.graphstream.algorithm.Toolkit;
import org.graphstream.graph.Edge;
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
		editPartMap = new HashMap<String, SimgridAbstractEditPart>();
		layoutManager = new SpringBox();
		graph = new GraphicGraph("id");
		layoutManager.addSink(graph);
		graph.addSink(layoutManager);
	}

	public void computeLayout() {
		//clear old data
		graph.clear();
		editPartMap.clear();
		// populate the graph
		if (root instanceof ErrorEditPart){
			//there is an Error in the model
			return;
		}
		addNode((SimgridAbstractEditPart) root);
		int nbEdge = 0;
		for (Node node : graph.getEachNode()){
			for (Edge edge : graph.getEachEdge()){
				if (edge.getNode0().equals(node)){
					for (Node otherNode : graph.getEachNode()){
						if (edge.getNode1().equals(otherNode)){
							nbEdge++;
						}
					}
				}
			}
			if (nbEdge == 0){
				node.setAttribute("layout.weight",0.05);
			}
		}
		// compute layout
		int i = 0;
		do  {
			layoutManager.compute();
			i++;
			//TODO the i limit must depends on the number of nodes
		}while(layoutManager.getNodeMoved() != 0 && i < 200 );
		double xmin = 0, ymin = 0;
		//get the x and y min to translate
		for (Node n : graph.getEachNode()) {
			double pos[] = Toolkit.nodePosition(graph, n.getId());
			xmin = Math.min(pos[0], xmin);
			ymin = Math.min(pos[1], ymin);
		}
		
		// update position in the map
		for (Node n : graph.getEachNode()) {
			double pos[] = Toolkit.nodePosition(graph, n.getId());
			//TODO assign position depending on object size
			int x =  new Double((pos[0]+Math.abs(xmin))*200).intValue();
			int y =  new Double((pos[1]+Math.abs(ymin))*200).intValue();
			Point p = new Point(x, y);
			// set position
			ElementPositionMap.setPositionAndRefresh(editPartMap.get(n.getId()),p);
		}		
	}

	@SuppressWarnings("unchecked")
	private void addNode(EditPart node) {
		if (node instanceof AbstractElementEditPart){
			String id = computeId(node);
			//add the node if it is necessary
			if (!editPartMap.containsKey(id)){
				editPartMap.put(id, (SimgridAbstractEditPart) node);
				graph.addNode(id);
			}
			//get connections in source
			AbstractElementEditPart editPart = (AbstractElementEditPart) node;
			int numberOfConnection = editPart.getSourceConnections().size();
			graph.getNode(id).setAttribute("layout.weight",0.5);
			if (numberOfConnection != 0) {
				//add edges in the graph
				List<?> l = new ArrayList<Object>();
				l.addAll(editPart.getSourceConnections());
				for (Object e :l){
					if (e instanceof AbstConnectionEditPart){
						AbstConnectionEditPart conn = (AbstConnectionEditPart) e;
						AbstractElementEditPart newElem = (AbstractElementEditPart) conn.getTarget();
						String id2 = computeId(newElem);
						if (graph.getNode(id2) == null){
							editPartMap.put(id2, (SimgridAbstractEditPart) newElem);
							graph.addNode(id2);
						}
						graph.addEdge(computeId(conn),id,id2);
					}
				}
			}
		}
		List<?> l = node.getChildren();
		for (Object elem : l) {
			addNode((EditPart)elem);
		}
	}
	
	private String computeId(EditPart node){
		return node.getModel().toString();
	}
}

package simgrideclipseplugin.graphical;

import java.util.HashMap;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.RootEditPart;
import org.graphstream.graph.Node;
import org.graphstream.ui.graphicGraph.GraphicGraph;
import org.graphstream.ui.layout.springbox.SpringBox;

import simgrideclipseplugin.graphical.parts.SimgridAbstractEditPart;

public class AutomaticGraphLayoutRenderer {
	private HashMap<SimgridAbstractEditPart,Point> map;
	private SpringBox layoutManager;
	private GraphicGraph graph;
	private RootEditPart root;
	
	//define as a singleton
	public static final AutomaticGraphLayoutRenderer INSTANCE = new AutomaticGraphLayoutRenderer();
	
	public void init(RootEditPart root){
		this.root = root;
		map = new HashMap<SimgridAbstractEditPart, Point>();
		layoutManager = new SpringBox();
		graph = new GraphicGraph("id");
		layoutManager.addSink(graph);
	}
	
	public void computeLayout() {
		//populate the graph
		SimgridAbstractEditPart visualRoot = (SimgridAbstractEditPart) root.getContents();
		addNode(visualRoot);
		//compute layout
		while(layoutManager.getNodeMoved() != 0){
			layoutManager.compute();
		}
		//update position in the map
		for(Node n : graph.getEachNode()){
			//get (position,id) from graph
			//get Editpart from id
			//set position
			//n.setPosition(p);
		}
	}

	@SuppressWarnings("unchecked")
	private void addNode(SimgridAbstractEditPart node){
		String id = Integer.toString(node.hashCode());
		map.put(node,new Point());
		//FIXME I think it's not really a good idea...
		graph.addNode(id);
		List<SimgridAbstractEditPart> l = node.getChildren();
		for (SimgridAbstractEditPart elem: l){
			addNode(elem);
		}
	}
	
//	/**
//	 * @param elem
//	 * @return the calculated position for this element.
//	 */
//	public Point getPosition(EditPart elem) {
//		return map.get(elem);
//	}
}

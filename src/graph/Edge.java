package graph;

public class Edge {
	private Node from;
	private Node to;
	
	Edge (Node sNode, Node eNode){
		from = sNode;
		to = eNode;
	}
	
	public Node getEndNode() {
		return to;
	}
	
	public Node getStartNode() {
		return from;
	}
	
	
}

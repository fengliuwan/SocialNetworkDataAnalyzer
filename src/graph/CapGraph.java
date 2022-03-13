/**
 * 
 */
package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;

import util.GraphLoader;

/**
 * @author Fengliu Wan
 * 
 */
public class CapGraph implements Graph {
	private HashMap<Integer, Node> gNodes;
	private HashSet<Edge> gEdges;

	public CapGraph(){
		gNodes = new HashMap<Integer, Node>();
		gEdges = new HashSet<>();
	}
	
	public CapGraph(CapGraph g){
		this.gNodes = new HashMap<>(g.gNodes);
		this.gEdges = new HashSet<>(g.gEdges);
	}
	
	private HashMap<Integer, Node> getMap(){
		return gNodes;
	}
	
	private Set<Integer> getNodesValues(){
		return gNodes.keySet();
	}
	
	private Set<Node> getNodes(){
		Set<Node> output = new HashSet<>();
		for(int n : gNodes.keySet()) {
			output.add(gNodes.get(n));
		}
		return output;
	}
	
	private Set<Edge> getEdges(){
		return gEdges;
	}
	
	public Set<Node> getInNeighbors(Node curr){
		HashSet<Node> output = new HashSet<>();
		for(Edge ed : gEdges) {
			if(ed.getEndNode() == curr) {
				output.add(ed.getStartNode());
			}
		}
		return output;
	}
	/* (non-Javadoc)
	 * @see graph.Graph#addVertex(int)
	 */
	@Override
	public void addVertex(int num) {
		Node n1 = gNodes.get(num);
		if(n1 != null) throw new IllegalArgumentException ("trying to add duplicate node to graph");
		// creates a new node and add to graph
		Node newNode = new Node(num);
		gNodes.put(num, newNode);
	}

	/* (non-Javadoc)
	 * @see graph.Graph#addEdge(int, int)
	 */
	@Override
	public void addEdge(int from, int to) {
		// TODO Auto-generated method stub
		Node n1 = gNodes.get(from);
		Node n2 = gNodes.get(to);
		if(n1 == null || n2 == null) {
			throw new NullPointerException("Trying to add edge between nodes non in graph");
		}
		Edge ne = new Edge(n1,n2);
		gEdges.add(ne);
		n1.addEdge(ne);
	}

	/* (non-Javadoc)
	 * @see graph.Graph#getEgonet(int)
	 */
	@Override
	public Graph getEgonet(int center) {
		// check if center exist in current graph
		Node cNode = gNodes.get(center);
		if(cNode == null) throw new NullPointerException("center not in graph");
		
		CapGraph output = new CapGraph();
		// creates a new vertex with @param center and insert in graph
		output.addVertex(center);
		
		for(Node nb: cNode.getNeighbors()) {
			// creates a new node for each of center's neighbor, edges between its neighbor
			// and add to graph
			output.addVertex(nb.getNum());
		}
		
		
		HashMap<Integer, Node> map = this.getMap();
		buildEdges(output, map);
		
		return output;
	}

	/**
	 * added edges to the output file in place, if nodes in the output graph have edge in originalgraph
	 * @param output
	 * @param originalMap
	 */
	public void buildEdges(CapGraph output, HashMap<Integer, Node> originalMap) {
		Set<Node> all = output.getNodes();
		for(Node n1 : all) {
			// find corresponding node in original graph
			Node n1Original = originalMap.get(n1.getNum());
			for(Node n2 : all) {
				Node n2Original = originalMap.get(n2.getNum());
				// check if there is edge between any two vertex in original graph
				if(n1Original.getNeighbors().contains(n2Original)) {
					//System.out.println("adding edge");
					output.addEdge(n1.getNum(), n2.getNum());
				}
			}
		}
	}
	
	public Stack<Node> getGraphNodesAsStack(){
		Stack<Node> output = new Stack<>();
		for(Integer n : gNodes.keySet()) {
			output.add(gNodes.get(n));
		}
		return output;
	}
	/* (non-Javadoc)
	 * @see graph.Graph#getSCCs()
	 */
	@Override
	public List<Graph> getSCCs() {
		ArrayList<Graph> output = new ArrayList<>();
		
		//make a copy of original graph
		CapGraph copyG = new CapGraph(this);
		Stack<Node> vertices = copyG.getGraphNodesAsStack();
		
		Stack<Node> finished = DFS(copyG, vertices, null);
		copyG.reverseGraphEdges();
		DFS(copyG, finished, output);
		for(Graph g : output) {
			buildEdges((CapGraph)g, this.gNodes);
		}
		return output;
	}

	/**
	 * create a copy of the graph with edge direction reversed
	 * @return a new graph with edge directions reversed from original graph
	 */
	public void reverseGraphEdges() {
		// create a set of reversed edges to replace original edges
		HashSet<Edge> reversedEdges = new HashSet<>();
		for(Edge ed : this.gEdges) {
			Node currS = ed.getStartNode();
			Node currE = ed.getEndNode();
			// remove existing edge from current start node
			// cannot remove edges in gEdges set due to concurrent modification
			currS.removeEdge(ed);
			// add reversed edge to current end node and to edge set
			Edge reversedEd = new Edge(currE,currS);
			currE.addEdge(reversedEd);
			reversedEdges.add(reversedEd);
		}
		// replace edge set with reversed edge set
		this.gEdges = reversedEdges;
	}
	
	public Stack<Node> DFS(CapGraph g, Stack<Node> vertices, ArrayList<Graph> graphList) {
		Stack<Node> finished = new Stack<>();
		HashSet<Node> visited = new HashSet<>();
		Node curr = null;
		while(!vertices.empty()) {
			curr = vertices.pop();
			if(!visited.contains(curr)) {
				CapGraph sCC = new CapGraph();
				DFS_Visit(g, curr, visited, finished, sCC);
				if(graphList != null) graphList.add(sCC);
			}
		}
		return finished;
	}
	
	public void DFS_Visit(CapGraph g, Node curr,
		HashSet<Node> visited, Stack<Node> finished, CapGraph sCC) {
		visited.add(curr);
		for(Node nb : curr.getNeighbors()) {
			if(!visited.contains(nb)) {
				DFS_Visit(g, nb, visited, finished, sCC);
			}
		}
		sCC.addVertex(curr.getNum());
		finished.add(curr);
	}
	/* (non-Javadoc)
	 * @see graph.Graph#exportGraph()
	 */
	@Override
	public HashMap<Integer, HashSet<Integer>> exportGraph() {

		HashMap<Integer, HashSet<Integer>> output = new HashMap<>();
		for(Node n : this.getNodes()) {
			HashSet<Integer> currNB = new HashSet<>();
			for(Node nb : n.getNeighbors()) {
				currNB.add(nb.getNum());
			}
			output.put(n.getNum(),currNB);
		}
		return output;
	}
	
	/**
	 * find center user's friends who are not friends with each other and return a HashMap mapping friend to suggested friends
	 * @param center, the center user whose friends will get friends suggestions
	 * @return a HashMap mapping center's each friends to every other of center's friends who are not yet friends yet
	 */
	public HashMap<Node, HashSet<Node>> suggestFriendsofFriends(Integer center){
		HashMap<Node, HashSet<Node>> output = new HashMap<>();
		Node cVertex = this.gNodes.get(center);
		if(cVertex == null) throw new NullPointerException("center user is not in graph");
		for(Node nb : cVertex.getNeighbors()) {
			HashSet<Node> currSet = new HashSet<>();
			for(Node otherNB : cVertex.getNeighbors()) {
				if(nb != otherNB && !nb.getNeighbors().contains(otherNB)) {
					currSet.add(otherNB);
				}
			}
			output.put(nb, currSet);
		}
		return output;
	}
	
	public HashSet<Node> findDominatingSet(){
		HashSet<Node> DS = new HashSet<>();
		HashSet<Node> uncovered = new HashSet<>();
		// load all nodes into uncovered set
		for(Integer n : gNodes.keySet()) {
			uncovered.add(gNodes.get(n));
		}
		
		while(!uncovered.isEmpty()) {
			int max = -1;
			Node toAdd = null;
			// count the number of uncovered neighbors of each node in uncovered set
			for(int n : gNodes.keySet()) {
				Node curr = gNodes.get(n);
				if(uncovered.contains(curr)) {
					int numNeighbor = curr.getNeighbors().size();
					for (Node currNB : curr.getNeighbors()) {
						if(!uncovered.contains(currNB)){
							numNeighbor--;
						}
					}
					if (numNeighbor > max) {
						max = numNeighbor;
						toAdd = curr;
					}
				}
			}
			// end of one loop, got the vertex with most uncovered vertices, add the vertex to dominating set
			System.out.println("\nadding vertex " + toAdd + " to set ");
			DS.add(toAdd);
			// remove the vertex and its neighbors from uncovered set
			uncovered.remove(toAdd);
			uncovered.removeAll(toAdd.getNeighbors());
			if(uncovered.size() == 0) break;
		}
		return DS;
	}	
	
	public void updateNeighborUncoveredEdgeNum(Node curr, HashSet<Node> found) {
		for(Node nb : getInNeighbors(curr)) {
			if(!found.contains(nb)) {
				int updateEdgeNum = nb.getUncoveredEdgesNum() - 1;
				System.out.println("updating uncoveredEdges of node " + nb + "from " + nb.getUncoveredEdgesNum() + " to " + updateEdgeNum);
				nb.setUncoveredEdgesNum(updateEdgeNum);
			}
		}
	}
	
	public static void main (String[] arg){
		CapGraph g1 = new CapGraph();
		GraphLoader.loadGraph(g1, "data/small_test_graph.txt");
		System.out.println(g1.gNodes);
		
		//test getInNeighbors
		Iterator hmIte = g1.gNodes.keySet().iterator();
		Node curr = g1.gNodes.get(hmIte.next());
		System.out.println("in neighbor of " + curr + " are " + g1.getInNeighbors(curr));
		
		CapGraph ego1 = (CapGraph)g1.getEgonet(1);
		System.out.println(ego1.gNodes);
		
		g1.getSCCs();
		
		System.out.println("dominating set of g1: \n");
		System.out.println(g1.findDominatingSet());
	}

}
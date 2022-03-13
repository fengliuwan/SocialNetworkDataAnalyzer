package graph;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class Node implements Comparable <Node> {
	private int num;
	private int uncoveredEdgesNum;
	private HashSet<Edge> edges;
	
	Node (int number){
		num = number;
		edges = new HashSet<>();
		uncoveredEdgesNum = edges.size();
	}
	
	public int getUncoveredEdgesNum() {
		return uncoveredEdgesNum;
	}
	
	public void setUncoveredEdgesNum(int n) {
		uncoveredEdgesNum = n;;
	}
	
	public int getNum() {
		return num;
	}
	
	public Set<Edge> getEdges(){
		return edges;
	}
	
	public void removeEdge(Edge ed) {
		if(!edges.contains(ed)) throw new IllegalArgumentException("trying to remove edge not in graph");
		edges.remove(ed);
		uncoveredEdgesNum--;
	}
	
	public void addEdge(Edge ed) {
		edges.add(ed);
		uncoveredEdgesNum++;
	}
	
	public Set<Node> getNeighbors(){
		HashSet<Node> output = new HashSet<>();
		for(Edge currEd : edges) {
			output.add(currEd.getEndNode());
		}
		return output;
	}
	
	@Override
	public int compareTo(Node other) {
		if(this.uncoveredEdgesNum > other.uncoveredEdgesNum) return -1;
		else if (this.uncoveredEdgesNum < other.uncoveredEdgesNum) return 1;
		else return 0;
	}
	
	@Override
	public String toString() {
		String output = "";
		output += "node number: " + num + " neighbor:";
		for(Node n : this.getNeighbors()) {
			output += " " + n.num;
		}
		output += "\n";
		return output;
	}
	
}

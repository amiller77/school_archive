/**
* EDGE
* contains a weight, and to and from nodes
*/

public class Edge {
	public int weight;
	public String to;
	public String from;
	public boolean solutionEdge;
	
	public Edge (String from, String to, int weight) {
		this.from = from;
		this.to= to;
		this.weight = weight;
		this.solutionEdge = false; // allows us to draw the solution graph more easily
	}
	
}
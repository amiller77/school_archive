import java.util.LinkedList;

/**
 * Node
 * contains name and shortest distance found
 *
 */
public class Node {
	public String name;	//name of node
	public Integer distance; // records current distance from start node
	public Integer index; // records their position in the heap
	public boolean visited;	// records if node has been visited
	public String solutionPath;	// string representation of solution path
	public LinkedList<Edge> edges;	// list of edges for the node
	public boolean checkedForDuplicates;	// helps us remove non-distinct graph edges for dot output
	public Edge previousEdge;	//records the path that discovered us when our distance was updated
	
	public Node(String name) {
		distance = null;
		index = null;
		this.name = name;
		visited = false;
		solutionPath = null;
		edges = new LinkedList<Edge>();
		previousEdge = null;
	}
	
	public void addEdge(Edge e) {
		this.edges.add(e);
	}
	
}

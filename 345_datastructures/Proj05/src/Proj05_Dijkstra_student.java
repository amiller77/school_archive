import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;


/**
 * class that solves graphs using Dijkstra's algorithm
 * @author Alexander Miller
 */

public class Proj05_Dijkstra_student implements Proj05_Dijkstra {

	// *******************************************************
	// **************** INSTANCE VARIABLES *******************
	
	// STORING AND BUILDING THE GRAPH:
	// are we operating on a digraph or graph?
	private boolean isDigraph;
	
	// allows instant access for any node (assigning edges when building graph)
	private HashMap<String,Node> nodeMap;
	// allows iteration over all nodes (generate dot, solution)
	private LinkedList<String> nodeNames;
	
	// PERFORMING DIJKSTRA'S
	// stores nodes, ordered by priority [yet to be processed]
	private PriorityQueue nodeQueue;
	
	// STORING THE SOLUTION:
	// stores our solution edges and nodes (dot file)
	private LinkedList<Edge> allEdges;
	
	// WRITING OUTPUT
	private int dotPrefix;
	
	
	// ***************************************************
	// **************** PUBLIC INTERFACE *****************
	/**
	 * @param isDigraph true if digraph, else false
	 */
	public Proj05_Dijkstra_student (boolean isDigraph) {
		this.isDigraph = isDigraph;
		nodeMap = new HashMap<String,Node>();
		nodeNames = new LinkedList<String>();
		nodeQueue = new PriorityQueue();
		allEdges = new LinkedList<Edge>();
		dotPrefix = 0;
	}
	
	/**
	 * ADD NODE
	 * @param s name of the node
	 */
	public void addNode(String s) {
		Node n = new Node(s);
		// store the name
		nodeNames.add(s);
		// store the node in hashmap
		nodeMap.put(s,n);
	}
	
	
	/**
	 * ADD EDGE
	 * pre-conditions: will not be a self-loop, addNode() as already declared any nodes,
	 * weight is non-negative, and the edge has not previously been declared
	 * @param from name of node
	 * @param to name of node
	 * @param weight edge weight
	 */
	public void addEdge(String from, String to, int weight) {
		if (isDigraph) {
			Edge e = new Edge(from,to,weight);
			Node n = nodeMap.get(from);
			n.addEdge(e);
			allEdges.add(e);
		} // need to add a double edge for an undirected graph
		else {
			Edge e1 = new Edge(from,to,weight);
			Edge e2 = new Edge(to,from,weight);
			Node n1 = nodeMap.get(from);
			Node n2 = nodeMap.get(to);
			n1.addEdge(e1);
			n2.addEdge(e2);
			allEdges.add(e1);
			allEdges.add(e2);
		}
	}
	
	
	/**
	 * RUN DIJKSTRA
	 * @param startNodeName starting node of dijkstra
	 */
	public void runDijkstra(String startNodeName) {
		Node n = nodeMap.get(startNodeName);
		n.distance = 0;
		nodeQueue.insert(n);
		dijkstraHelper();
	}
	
	/**
	 * HELPER FOR RUN DIJKSTRA
	 * recursive method that executes the algorithm
	 * pre-conditions: thisNode has an accurate and initialized distance, or is null
	 * @param lastNode
	 */
	private void dijkstraHelper() {
		// VISIT NODE
		// pop the node
		Node thisNode = nodeQueue.pop();
		// END CASE:
		// if queue is done, return
		if (thisNode == null) {
			return;
		}
		//register that it has been visited:
		thisNode.visited = true;
		
		//	FIND PREVIOUS NODE ; ADD TO SOLUTION (EDGE + PATH)
		// find the edge that brought us here:
		Edge previousEdge = thisNode.previousEdge;
		// find the node that pointed to us:
		if (previousEdge != null) {
			Node lastNode = nodeMap.get(previousEdge.from);
			// add the edge to the solution
			previousEdge.solutionEdge = true;
			// add this node to the solution path for predecessor to get its own solution path
			thisNode.solutionPath = lastNode.solutionPath+" "+thisNode.name;	
		} // if lastNode == null, then solutionPath = " "+thisNode.name
		else {
			thisNode.solutionPath = " "+thisNode.name;
		}
		
		// UPDATE TARGET NODES' DISTANCES ^ ADD TO QUEUE
		// iterate over thisNode's edges:
		for (Edge e : thisNode.edges) {
			// look at the to-node
			Node toNode = nodeMap.get(e.to);
			// if distance hasn't been initialized, initialize it and add to queue:
			if (toNode.distance == null) {
				// register that we updated the node at this edge
				toNode.previousEdge = e;
				updateDistance(thisNode,toNode,e);
			} // otherwise, if we have found a shorter distance, update it in queue:
			else if (thisNode.distance + e.weight < toNode.distance) {
				// register that we updated the node at this edge
				toNode.previousEdge = e;
				updateDistance(thisNode,toNode,e);
			} // otherwise, if node hasn't been visited, add to queue
			else if (! toNode.visited) {
				nodeQueue.insert(toNode);
			}	
		}
		// RECURSE
		dijkstraHelper();	
	}
	
	/**
	 * HELPER FOR DIJKSTRA HELPER
	 * UPDATE DISTANCE
	 * updates distance of target node based on distance of the predecessor
	 * @param thisNode
	 * @param toNode
	 * @param e
	 */
	private void updateDistance(Node thisNode, Node toNode, Edge e) {
		// assign it the new distance;
		toNode.distance = thisNode.distance + e.weight;
		// update its position in queue:
		nodeQueue.insert(toNode);
	}
	
	
	/**
	 * PRINT DIJKSTRA RESULTS
	 * prints out the best path to every node s.t. each line prints out the list of nodes in the given path
	 * @param startNodeName
	 */
	public void printDijkstraResults(String startNodeName) {
		for (String nodeName : nodeNames) {
			Node n = nodeMap.get(nodeName);
			String printString;
			// note: solution path starts with leading space
			if (n.solutionPath == null) {
				printString = startNodeName+" -> "+nodeName+": NO PATH";
			} else {
				printString = startNodeName+" -> "+nodeName+": best "+n.distance+":"+n.solutionPath;
			}
			System.out.println(printString);
		}
		
	}
	
	
	/**
	 * WRITE SOLUTION DOT FILE
	 * writes dot file that shows solution and supports both graphs and digraphs
	 * includes edge weights, nodes, and edges
	 * nodes labeled with name and best distance found by Dijkstra's; if unreachable, only include node name
	 * selected edges which form paths are indicated as such in some fashion
	 */
	public void writeSolutionDotFile() {		
		// set up:
		FileWriter f = createFileWriter();
		PrintWriter p = new PrintWriter(f);
		// write to file:
		if (isDigraph) {
			p.println("digraph");
		} else {
			p.println("graph");
		}
		p.println("{");
		// write all nodes, with their keys and vals
		writeNodes(p);
		// write all connections
		if (isDigraph) {
			String connection= " -> ";
			writeConnections(p,allEdges, connection);
		} else {
			String connection = " -- ";
			// eliminate "duplicate" edges
			LinkedList<Edge> distinctEdges = new LinkedList<Edge>();
			distinctEdges = findDistinctEdges(distinctEdges);
			writeConnections(p,distinctEdges, connection);
		}
		// wrapping up:
		p.println("}");
		p.close();
	}
	
	// ********************** WRITE SOLUTION DOT FILE HELPERS **********************
	// CREATE FILE WRITER
	// gets the try-catch out of a way in an encapsulated way
	private FileWriter createFileWriter() {
		try {
			FileWriter f = new FileWriter(dotPrefix+"dijkstra.dot");
			dotPrefix++;
			return f;
			
		} catch (IOException io) {
			System.out.println("IO issue: cannot write a new file .");
		}
		return null;
	}
	
	// WRITE NODES
	private void writeNodes(PrintWriter p) {
		for (String nodeName : nodeNames) {
			p.println("\t"+getNodeDotName(nodeName)+";");
		}
	}
	
	/**
	 * WRITE CONNECTIONS
	 * @param p
	 */
	private void writeConnections(PrintWriter p, LinkedList<Edge> graphEdges, String connection) {
		// iterate over all graphEdges:
		for (Edge e : graphEdges) {
			String printString ="";
			String fromNode = getNodeDotName(e.from);
			String toNode = getNodeDotName(e.to);
			// if solution edge, color it
			if (e.solutionEdge) {
				printString = "\t"+fromNode+connection+toNode+" [label="+e.weight+" color=red];";
			} // otherwise, don't
			else {
				printString = "\t"+fromNode+connection+toNode+" [label="+e.weight+"];";
			}
			p.println(printString);
		}
	}
	
	/**
	 * getNodeDotName
	 * @param nodeName
	 * @return
	 */
	private String getNodeDotName(String nodeName) {
		Node n = nodeMap.get(nodeName);
		String printString = "";
		if (n.distance == null) {
			printString = nodeName;
		} else {
			printString =nodeName+"_"+n.distance;
		}
		return printString;
	}
	
	/**
	 * FIND DISTINCT EDGES
	 * finds our distinct edges (to form graph from a digraph internal representation)
	 */
	private LinkedList<Edge> findDistinctEdges(LinkedList<Edge> distinctEdges) {
		// iterate over edges:
		for (Edge e: allEdges) {
			boolean edgeDistinct = true;
			// only add if inverse edge doesn't exist in distinct edges
			for (Edge distinctEdge : distinctEdges) {
				if (distinctEdge.to.equals(e.from) && distinctEdge.from.equals(e.to) ) {
					edgeDistinct = false;
					break;
				}
			}
			if (edgeDistinct) {
				distinctEdges.add(e);
			}
		}
		return distinctEdges;
	}
	
	
	
}

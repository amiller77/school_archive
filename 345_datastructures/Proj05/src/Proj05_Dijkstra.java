public interface Proj05_Dijkstra {
	
	/**
	 * @param s name of the node
	 */
	void addNode(String s);
	
	
	/**
	 * pre-conditions: will not be a self-loop, addNode() as already declared any nodes,
	 * weight is non-negative, and the edge has not previously been declared
	 * @param from name of node
	 * @param to name of node
	 * @param weight edge weight
	 */
	void addEdge(String from, String to, int weight);

	/**
	 * 
	 * @param startNodeName starting node of dijkstra
	 */
	void runDijkstra(String startNodeName);
	
	/**
	 * prints out the best path to every node s.t. each line prints out the list of nodes in the given path
	 * @param startNodeName
	 */
	void printDijkstraResults(String startNodeName);
	
	/**
	 * writes dot file that shows solution and supports both graphs and digraphs
	 * includes edge weights, nodes, and edges
	 * nodes labeled with name and best distance found by Dijkstra's; if unreachable, only include node name
	 * selected edges which form paths are indicated as such in some fashion
	 */
	void writeSolutionDotFile();
	
}

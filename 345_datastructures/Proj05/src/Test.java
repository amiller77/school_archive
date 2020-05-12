
public class Test {

	public static void main(String[] args) {
		PriorityQueue queue = new PriorityQueue();
		Node a = new Node("a");
		Node b = new Node("b");
		Node c = new Node("c");
		Node d = new Node("d");
		Node e = new Node("e");
		Node f = new Node("f");
		Node g = new Node("g");
		Node h = new Node("h");
		Node i = new Node("i");
		
		a.distance = 0;
		b.distance = 5;
		c.distance = 2;
		d.distance = 1;
		e.distance = 3;
		f.distance = 6;
		g.distance = 4;
		h.distance = 7;
		i.distance = 1;
		queue.insert(a);
		queue.insert(b);
		queue.insert(c);
		queue.insert(d);
		queue.insert(e);
		queue.insert(f);
		queue.insert(g);
		queue.insert(h);
		queue.insert(i);
		
		queue.pop();
		queue.pop();
		queue.pop();
		queue.pop();
		queue.pop();
		queue.pop();
		queue.pop();
		queue.pop();

		Proj05_Dijkstra_student ds = new Proj05_Dijkstra_student(true);
		ds.addNode("a");
		ds.addNode("b");
		ds.addNode("c");
		ds.addNode("d");
		ds.addNode("e");
		ds.addNode("f");
		ds.addNode("g");
		ds.addNode("h");
		ds.addEdge("a", "b", 5);
		ds.addEdge("a", "c", 1);
		ds.addEdge("b", "d", 5);
		ds.addEdge("a", "e", 4);
		ds.addEdge("e", "f", 3);
		ds.addEdge("c", "g", 5);
		ds.addEdge("d", "h", 7);
		ds.runDijkstra("a");
		ds.printDijkstraResults("a");
		
		
		

		
	}

}

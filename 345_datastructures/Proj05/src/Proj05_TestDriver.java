import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;


public class Proj05_TestDriver
{
	public static void bmain(String[] args)
	{
		boolean isExample = false;
		String  startNode = null;

		Proj05_Dijkstra graph = null;
		boolean isDigraph     = true;


		if (args.length == 2 && args[0].equals("example"))
		{
			isExample = true;
			startNode = args[1];
		}
		else if (args.length == 1)
		{
			startNode = args[0];
		}
		else
		{
			System.err.printf("SYNTAX: java Proj05_TestDriver example? <startNode>\n");
			System.err.printf("    <give the .dot file input as System.in>\n");
			System.exit(1);
		}

		// set up the BufferedReader to read the .dot file
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));


		try {
			// sanity-check the first line.  Is this a digraph
			// or a graph?
			String str = in.readLine().trim();
			if (str.equals("digraph {"))
			{
				// NOP, the 'isDigraph flag is already set
			}
			else if (str.equals("graph {"))
			{
				isDigraph = false;
			}
			else
				inputErr("The leading line is not as expected");


			if (isExample)
				graph = new Proj05_Dijkstra_example(isDigraph);
			else
				graph = new Proj05_Dijkstra_student(isDigraph);


			// now, a big loop that proceeds until we hit the
			// trailing curly-brace.
			for (str = in.readLine().trim();
			     str.equals("}") == false;
			     str = in.readLine().trim())
			{
				if (str.equals(""))
					continue;

				// remove the trailing semicolon
				if (str.charAt(str.length()-1) != ';')
					inputErr("A line is not terminated with a semicolon");
				str = str.substring(0, str.length()-1);


				// is this a node, or an edge?
				String[] words = str.split(" ");

				if (words.length == 1)
				{
					graph.addNode(words[0]);
					continue;
				}


				// this is an edge!  interpret the pieces...
				String from = words[0];
				String to   = words[2];

				if (words[1].equals("->") && isDigraph == false)
					inputErr("The input file has a directed edge, but the graph is an undirected graph.");
				if (words[1].equals("--") && isDigraph == true)
					inputErr("The input file has an undirected edge, but the graph is a digraph.");

				if (words[1].equals("->") == false && words[1].equals("--") == false)
					inputErr("A line in the graph file - which appears to be an edge (because it has multiple words) has the wrong 2nd word.");


				// the last word should be a 'label' statement.
				if (words[3].substring(0,7).equals("[label=") == false)
					inputErr("A line in the graph file - which appears to be an edge (because it has multiple words) has the wrong leading text in the last word.");

				if (words[3].substring(words[3].length()-1).equals("]") == false)
					inputErr("A line in the graph file - which appears to be an edge (because it has multiple words) has the wrong trailing text in the last word.");

				String label  = words[3].substring(7,words[3].length()-1);
				int    weight = Integer.parseInt(label);

				if (weight < 0)
					inputErr("an edge has a negative weight");


				graph.addEdge(from,to, weight);
			}


			// we don't worry about reading the rest of the input; we
			// assume that, after finding the curly brace, we've found
			// everything that is important in the file.


			graph.runDijkstra         (startNode);
			graph.printDijkstraResults(startNode);
			graph.writeSolutionDotFile();
		}
		catch (IOException e)
		{
			inputErr("An IOException happened somewhere while we were reading the input graph.  It appears to have an invalid format of some sort.");
		}
	}


	public static void inputErr(String str)
	{
		System.err.printf("INPUT ERROR: %s\n", str);
		System.exit(1);
	}
}

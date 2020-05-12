import java.io.*;



public class Proj06_TestDriver
{
	public static void main(String[] args)
	{
		String  startTape = null;

		Proj06_TuringMachine tm = null;


		if (args.length == 2 && args[0].equals("example"))
		{
			startTape = args[1];
			tm = new Proj06_TuringMachine_example();
		}
		else if (args.length == 1)
		{
			startTape = args[0];
			tm = new Proj06_TuringMachine_student();
		}
		else
		{
			System.err.printf("SYNTAX: java Proj06_TestDriver example? <startTape>\n");
			System.err.printf("    <give the .dot file input as System.in>\n");
			System.exit(1);
		}


		// sanity-check the input.
		if (startTape.equals(""))
		{
			System.err.printf("ERROR: The start-state command-line argument is the empty string.  This is invalid!\n");
			System.exit(2);
		}

		for (char c: startTape.toCharArray())
			if ((c >= 'a' && c <= 'z' || c >= '0' && c <= '9') == false)
			{
				System.err.printf("ERROR: The start-state command-line argument has an invalid format.  It must be made up entirely of digits or lowercase letters, like this:  aaa01aa7890zvy\n");
				System.exit(2);
			}


		// read the input file
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));


		try {
			// sanity-check the first line.
			String str = in.readLine().trim();
			if (str.equals("digraph {") == false)
				inputErr("The leading line is not as expected");


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


				// is this a node, or an edge?  (Add support
				// for accept states).
				String[] words = str.split(" ");

				if (words.length == 2 &&
				    words[1].equals("[peripheries=2]"))
				{
					// accept state
					tm.addState(words[0], true);
					continue;
				}
				else if (words.length == 1)
				{
					// ordinary state
					tm.addState(words[0], false);
					continue;
				}


				// this is an edge!  interpret the pieces...
				String from = words[0];
				String to   = words[2];

				if (words[1].equals("->") == false)
					inputErr("A line in the graph file - which appears to be an edge has the wrong 2nd word.");


				// the last word should be a 'label'
				// statement, with three comma-separated
				// fields
				if (words[3].substring(0,8).equals("[label=\"") == false)
					inputErr("A line in the graph file - which appears to be an edge (because it has multiple words) has the wrong leading text in the last word.");

				if (words[3].substring(words[3].length()-2).equals("\"]") == false)
					inputErr("A line in the graph file - which appears to be an edge (because it has multiple words) has the wrong trailing text in the last word.");


				String label = words[3].substring(8,words[3].length()-2);
				String[] labelWords = label.split(",");

				if (labelWords.length != 3)
					inputErr("An edge in the input file does not have exactly 3 comma-separated fields");

				if (labelWords[0].length() != 1 ||
				    labelWords[1].length() != 1)
				{
					inputErr("The first and second parameters of an edge must each be exactly a single character");
				}

				if (labelWords[2].equals("R") == false &&
				    labelWords[2].equals("L") == false)
				{
					inputErr("The third parameter of an edge must be R or L");
				}


				tm.addTransition(from, labelWords[0].charAt(0),
				                 labelWords[1].charAt(0),
				                 labelWords[2].equals("R"),
				                 to);
			}
		}
		catch (IOException e)
		{
			inputErr("An IOException happened somewhere while we were reading the input graph.  It appears to have an invalid format of some sort.");
		}



		// if we get here, then we've completely read the input file.
		// We're ready to run.

		System.out.printf("--------------------\n");
		System.out.printf("Running the first time, without debug.\n");
		System.out.printf("--------------------\n");

		tm.run(startTape, false);

		System.out.printf("\n");
		System.out.printf("--------------------\n");
		System.out.printf("Running again, with debug turned on.\n");
		System.out.printf("--------------------\n");

		tm.run(startTape, true);
	}


	public static void inputErr(String str)
	{
		System.err.printf("INPUT ERROR: %s\n", str);
		System.exit(1);
	}
}
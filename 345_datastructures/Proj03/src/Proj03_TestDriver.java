/* Proj03_TestDriver
 *
 * Class to exercise the BST class.  It reads a command file from stdin, and
 * performs operations on the BSTs.
 *
 * It supports 4 trees: int-1, int-2, string, and student (which all take
 * different types).  Each takes a string as the value.
 */

import java.util.*;

public class Proj03_TestDriver
{
	/* main
	 *
	 * Test driver for the BST_student class (and, for comparison,
	 * the BST_example class).
	 *
	 * Reads commands from stdin; passes these commands as operations
	 * on the tree.
	 */
	public static void main(String[] args)
	{
		/* there are 3 trees, each with different key/value types */
		Proj03_BST<Integer,String>  is = null;
		Proj03_BST<Integer,Integer> ii = null;
		Proj03_BST<String, Integer> si = null;


		if (args.length == 1 && args[0].equals("example"))
		{
			is = new Proj03_SimpleBST_example<Integer,String> ("debug_example_is");
			ii = new Proj03_SimpleBST_example<Integer,Integer>("debug_example_ii");
			si = new Proj03_SimpleBST_example<String ,Integer>("debug_example_si");
		}
		else if (args.length == 0)
		{
			is = new Proj03_SimpleBST_student<Integer,String> ("debug_student_is");
			ii = new Proj03_SimpleBST_student<Integer,Integer>("debug_student_ii");
			si = new Proj03_SimpleBST_student<String ,Integer>("debug_student_si");
		}
		else
		{
			System.out.printf("SYNTAX: example?\n");
			System.exit(1);
		}



		/* we'll read the commands from stdin.  Note that our input
		 * parser is *VERY* forgiving; we'll always assume that the
		 * input has valid format.
		 */
		Scanner in = new Scanner(System.in);

		while (in.hasNext())
		{
			/* read the tree-name string first. */
			String tree = in.next();

			/* validate the tree type */
			switch (tree)
			{
			case "is": case "ii": case "si":
				break;

			default:
				System.err.printf("TESTCASE ERROR: The first word on the line is not a valid tree name: is/ii/si\n");
				System.exit(1);
			}


			/* the tree type must be followed by another word,
			 * which is the command
			 */
			if (in.hasNext() == false)
			{
				System.out.printf("TESTCASE ERROR: Invalid command.  A tree name was not followed by anything else in the file\n");
				System.exit(1);
			}
			String cmd = in.next();


			Comparable result = null;
			switch (cmd)
			{
			default:
				System.out.printf("TESTCASE ERROR: Unrecognized command '%s'\n", cmd);
				System.exit(1);

			case "set":
				switch (tree)
				{
				case "is": is.set(readInt(in), readStr(in)); break;
				case "ii": ii.set(readInt(in), readInt(in)); break;
				case "si": si.set(readStr(in), readInt(in)); break;
				}
				break;

			case "get":
			{
				Integer i = null;
				String  s = null;
				Comparable key = null;

				switch (tree)
				{
				case "is":
				case "ii":
					key = i = readInt(in);
					break;
				case "si":
					key = s = readStr(in);
					break;
				}

				switch (tree)
				{
				case "is": result = is.get(i); break;
				case "ii": result = ii.get(i); break;
				case "si": result = si.get(s); break;
				}

				System.out.printf("tree %s: get(%s) returned %s\n",
				                  tree, key, result);
			}
			break;

			case "remove":
				switch (tree)
				{
				case "is": is.remove(readInt(in)); break;
				case "ii": ii.remove(readInt(in)); break;
				case "si": si.remove(readStr(in)); break;
				}
				break;

			case "rotateLeft":
				switch (tree)
				{
				case "is": is.rotateLeft(readInt(in)); break;
				case "ii": ii.rotateLeft(readInt(in)); break;
				case "si": si.rotateLeft(readStr(in)); break;
				}
				break;

			case "rotateRight":
				switch (tree)
				{
				case "is": is.rotateRight(readInt(in)); break;
				case "ii": ii.rotateRight(readInt(in)); break;
				case "si": si.rotateRight(readStr(in)); break;
				}
				break;

			case "getSize":
			{
				int size = 0;
				switch(tree)
				{
				case "is": System.out.printf("is.getSize()=%d\n", is.getSize()); break;
				case "ii": System.out.printf("ii.getSize()=%d\n", ii.getSize()); break;
				case "si": System.out.printf("si.getSize()=%d\n", si.getSize()); break;
				}
			}
			break;

			case "inOrder":
			{
				if (tree.equals("is"))
				{
					int       size = is.getSize();
					Integer[] keys = new Integer[size];
					String [] vals = new String [size];
					int    [] counts = new int[size];
					is.inOrder(keys,vals,counts);
					printTraversal("inOrder",tree, keys,vals,counts);
				}
				if (tree.equals("ii"))
				{
					int       size = ii.getSize();
					Integer[] keys = new Integer[size];
					Integer[] vals = new Integer[size];
					int    [] counts = new int[size];
					ii.inOrder(keys,vals,counts);
					printTraversal("inOrder",tree, keys,vals,counts);
				}
				if (tree.equals("si"))
				{
					int       size = si.getSize();
					String [] keys = new String [size];
					Integer[] vals = new Integer[size];
					int    [] counts = new int[size];
					si.inOrder(keys,vals,counts);
					printTraversal("inOrder",tree, keys,vals,counts);
				}
			}
			break;

			case "postOrder":
			{
				if (tree.equals("is"))
				{
					int       size = is.getSize();
					Integer[] keys = new Integer[size];
					String [] vals = new String [size];
					int    [] counts = new int[size];
					is.postOrder(keys,vals,counts);
					printTraversal("postOrder",tree, keys,vals,counts);
				}
				if (tree.equals("ii"))
				{
					int       size = ii.getSize();
					Integer[] keys = new Integer[size];
					Integer[] vals = new Integer[size];
					int    [] counts = new int[size];
					ii.postOrder(keys,vals,counts);
					printTraversal("postOrder",tree, keys,vals,counts);
				}
				if (tree.equals("si"))
				{
					int       size = si.getSize();
					String [] keys = new String [size];
					Integer[] vals = new Integer[size];
					int    [] counts = new int[size];
					si.postOrder(keys,vals,counts);
					printTraversal("postOrder",tree, keys,vals,counts);
				}
			}
			break;

			case "dotFile":
				switch (tree)
				{
				case "is": is.genDebugDot(); break;
				case "ii": ii.genDebugDot(); break;
				case "si": si.genDebugDot(); break;
				}
				break;

			}
		}

		// we never get here!
	}


	public static int readInt(Scanner in)
	{
		if (in.hasNext() == false)
		{
			System.out.printf("TESTCASE ERROR: The file ended, but the last command required another parameter.\n");
			System.exit(1);
		}

		if (in.hasNextInt() == false)
		{
			System.out.printf("TESTCASE ERROR: The command did not have the type of parameters that it expected.\n");
			System.exit(1);
		}

		return in.nextInt();
	}


	public static String readStr(Scanner in)
	{
		if (in.hasNext() == false)
		{
			System.out.printf("TESTCASE ERROR: The file ended, but the last command required another parameter.\n");
			System.exit(1);
		}

		return in.next();
	}


	public static <K,V> void printTraversal(String traversal, String tree,
	                                        K[] keys, V[] vals, int[] counts)
	{
		System.out.printf("%s traversal of the tree '%s':", traversal, tree);
		for (int i=0; i<keys.length; i++)
			System.out.printf(" %s->%s (count %d)", keys[i],vals[i],counts[i]);
		System.out.printf("\n");
	}
}

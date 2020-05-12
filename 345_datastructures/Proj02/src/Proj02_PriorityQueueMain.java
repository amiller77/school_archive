/* PriorityQueueMain
 *
 * Adapted from SortMain, to test dynamic in/out operations.
 *
 * Author: Russell Lewis
 */



import java.util.Random;
import java.util.Arrays;

public class Proj02_PriorityQueueMain
{
	public static void main(String[] args)
	{
		// is this debug statement?
		boolean debug = false;
		if (args.length > 0 && args[args.length-1].equals("debug"))
		{
			debug = true;

			// this chops off the last argument from the array
			args = Arrays.copyOf(args, args.length-1);
		}


		// is this the special "string" mode?
		boolean string = false;
		if (args.length > 0 && args[args.length-1].equals("String"))
		{
			string = true;

			// this chops off the last argument from the array
			args = Arrays.copyOf(args, args.length-1);
		}


		// very simple sanity checking of the arguments.  We'll
		// do more details down below - but this is the *ONLY*
		// case where we print the syntax help-text.  Lazy?  Yes.
		// Survivable?  On a one week project?  Yep.
		if (args.length > 1)
		{
			System.err.printf("SYNTAX: Proj02_PriorityQueueMain [<randomSeed>] [String] [debug]\n");
			System.err.printf("  [<randomSeed>] - selects exactly what dataset to use (instead of a randomly-chosen one)\n");
			System.err.printf("\n");
			System.err.printf("  String  - the input data is Strings, instead of integers\n");
			System.err.printf("  debug   - if set, the sort class will be created with debug=true\n");
			return;
		}


		// we determine the random seed either by (a) reading
		// it from the second command-line argument, or
		// (b) auto-generating it as a random number.
		//
		// Why generate a "random" number as a seed for your
		// own generator?  Certainly, it doesn't make things
		// any *more* random than they had been.  Answer: simply
		// so that the code below (which prints, and then uses,
		// the seed) is common in both code paths.
		int seed;
		if (args.length == 1)
		{
			try {
				seed = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				System.err.printf("ERROR: Could not convert the 2nd command-line argument, '%s', into an integer.\n", args[0]);
				return;
			}
		}
		else
		{
			seed = (int)(1000*1000*Math.random());
		}


		// now that we have the seed, we'll print it out, so that
		// people attempting to re-create their broken testcases
		// can know what seed to re-use.
		//
		// Then we'll use it.  See above to understand why we re-init
		// the random system, even if we've used it to generate the
		// very seed we plan to use.
		System.out.printf("SEED:   %d\n", seed);
		Random rand = new Random(seed);


		// ------------- DIVERGENCE -------------
		// This is where this program begins to deviate wildly from
		// the SortMain class.  Instead of pre-generating an entire
		// list of data all at once and sorting it, we instead create
		// an empty dataset.  We then will randomly either (a) insert;
		// (b) delete; or (c) terminate (after flushing).
		//
		// I don't want to give away the code for resizing the array
		// (though in truth it's not hard!), so I pre-allocate a fixed
		// array size.  It's quite large, but it is a (by design)
		// fixed upper limit on the working set size.  But to make
		// this something that you can't hard-code, I will randomly
		// decide what that length is!

		Comparable[] data = new Comparable[rand.nextInt(1500)];
		int dataLen = 0;

		Proj02_MaxHeap heap = new Proj02_MaxHeap(debug);


		// main loop.  If we ever hit the 'die' state (even once),
		// we'll force ourselves into flush-until-empty mode.  But we
		// do this using the same 'remove' code as we use when we're
		// still live, so that we get debug potential.
		boolean die = false;
		while (!die || dataLen > 0)
		{
			boolean add;
			if (dataLen == 0)
				add = true;
			else if (die || dataLen == data.length)
				add = false;
			else
			{
				// if we get here, then the various oddball
				// cases are *NOT* happening.  So what do
				// we do?  We'll generate a random number
				// between 0 and 100: if the number is 0, we
				// die; if it's 1-60, we add; otherwise, we
				// remove.
				int r = rand.nextInt(101);
				if (r == 0)
				{
					die = true;
					add = false;
				}
				else
					add = (r <= 60);
			}


			// now, we either add or remove.
			if (add)
			{
				Comparable newVal;
				if (string == false)
					newVal = rand.nextInt(data.length*10);
				else
					newVal = genRandomString(rand);

				System.out.printf("TESTCASE: Inserting %s\n", newVal);

				// add the value to the heap
				heap.insert(newVal);

				// add the value to our private copy
				data[dataLen] = newVal;
				dataLen++;

				// re-sort our private copy.  This is a
				// ludicrously wasteful way to keep the array
				// sorted, but it's trivial to write, and not
				// performance sensitive since it's testing code.
				Arrays.sort(data, 0,dataLen);
			}
			else
			{
				System.out.printf("TESTCASE: Removing the maximum.\n");

				// remove one value from the heap.  We confirm
				// that it matches the maximum that we believe
				// should exist - and then shorten our private
				// copy of the data.
				Comparable max = heap.removeMax();

				if (max.compareTo(data[dataLen-1]) != 0)
				{
					System.err.printf("TESTCASE ERROR: removeMax() miscompare!  expected: %s actual %s\n", data[dataLen-1],max);
					System.exit(1);
				}

				// "discard" this value from our private copy
				dataLen--;
			}


			/* dump the state of the array */
			System.out.printf("TESTCASE: Cur data:");
			for (int i=0; i<dataLen; i++)
				System.out.printf(" %s", data[i]);
			System.out.printf("\n");
		}

		// when we get here, the data array is now empty, and we're
		// ready to terminate the program.


		// this line is printed to allow the grading script to
		// detect, easily, whether all of the tests completed.  Do
		// *NOT* attempt to falsify this line if your tests are
		// broken - that is a violation of the Code of Academic
		// Integrity!

		System.out.printf("--- TESTCASE TERMINATED, NO ERRORS FOUND ---\n");
	}


	public static String genRandomString(Random rand)
	{
		int len = 8+rand.nextInt(4);

		String retval = "";
		for (int i=0; i<len; i++)
			retval += (char)('a'+rand.nextInt(26));

		return retval;
	}
}


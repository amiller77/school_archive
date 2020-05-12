/* Test_xx.java
 *
 * Specific testcases, to see if students perform the required
 * operations as designed.  In *NO CASE* will we have duplicates in
 * the heap at any given time, so that we don't have any ambiguity
 * about going left or right when bubbling down.  (Bubbling up
 * doesn't have this problem.)
 *
 * Author: Russell Lewis
 */



import java.io.PrintWriter;

public class Test_03
{
	public static void main(String[] args)
	{
		// this is the heap we'll test
		Proj02_MaxHeap heap = new Proj02_MaxHeap(false);

		// wrap System.out with a PrintWriter
		PrintWriter out = new PrintWriter(System.out);

		// any -99 values are "remove" operations.  The rest are
		// insert operations.
		int[] inputs = {30, -99, 16,79,63,-46, -99,-99, -35,89,
		                -99, -7, -99, 60, -99,-99, -34, -99,
		                -14,-48,74,54, -99, -20,75};

		for (int val: inputs)
		{
			if (val == -99)
				out.printf("removed %s\n", heap.removeMax());
			else
			{
				heap.insert(val);
				out.printf("inserted %s\n", val);
			}

			out.printf("Current heap: ");
			heap.dump(out);
		}


		// this line is printed to allow the grading script to
		// detect, easily, whether all of the tests completed.  Do
		// *NOT* attempt to falsify this line if your tests are
		// broken - that is a violation of the Code of Academic
		// Integrity!

		out.printf("--- TESTCASE TERMINATED ---\n");
		out.close();
	}
}


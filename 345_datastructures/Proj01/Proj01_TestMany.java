/* TestMany
 *
 * A useful class for testing all of your sort algorithms.  A single set of
 * data is randomly generated, and then delivered to *all* of the different
 * sort algorithsm to compare how they work.
 *
 * Author: Russell Lewis
 */



import java.util.Arrays;

public class Proj01_TestMany
{
	public static void main(String[] args)
	{
		// Since we have an interface which is the base class for
		// all of the sort classes, we can create objects and
		// store them into an array of those base class
		// references.
		Proj01_Sort sorts[] = {new Proj01_BubbleSort(true),
		                       new Proj01_InsertionSort(true),
		                       new Proj01_SelectionSort(true),
		                       new Proj01_MergeSort(true, 4),
		                       new Proj01_MergeSort(true, 8),
		                       new Proj01_MergeSort(true, 13),
		                       new Proj01_QuickSort(true, 0, 4),
		                       new Proj01_QuickSort(true, 1, 8),
		                       new Proj01_QuickSort(true, 2, 3),
		                       new Proj01_QuickSort(true, 2, 6) };

		// generate input data; we fix the length, but the contents
		// are random.
		Integer[] arr = new Integer[32];
		for (int i=0; i<arr.length; i++)
			arr[i] = (int)(100*Math.random());

		// this duplicate (sorted) is used for comparison - checking
		// to see if the sort worked.
		Integer[] sorted = Arrays.copyOf(arr, arr.length);
		Arrays.sort(sorted);

		System.out.printf("DATA:  ");
		for (int i=0; i<arr.length; i++)
			System.out.printf(" %s", arr[i]);
		System.out.printf("\n");

		System.out.printf("SORTED:");
		for (int i=0; i<arr.length; i++)
			System.out.printf(" %s", sorted[i]);
		System.out.printf("\n");

		System.out.printf("\n");

		// iterate over all of the ALGORITHMS
		for (int s=0; s<sorts.length; s++)
		{
			System.out.printf("---- RUNNING %s ----\n", sorts[s]);

			Integer[] dup = Arrays.copyOf(arr, arr.length);

			sorts[s].sort(dup);
			System.out.printf("\n");

			// for the data used by this algorithm, iterate over
			// the VALUES, to see if you sorted correctly.
			for (int i=0; i<dup.length; i++)
			if (dup[i] != sorted[i])
			{
				System.out.printf("  *** MISCOMPARE ***    first error at index %d\n", i);

				// an error was found; now iterate over
				// all of the values again, but this time add
				// a marker that shows the user (visually)
				// where the error occurred.
				System.out.printf("  CORRECT:");
				for (int j=0; j<sorted.length; j++)
				{
					if (j == i)
						System.out.printf(" .");
					System.out.printf(" %s", sorted[j]);
				}
				System.out.printf("\n");

				System.out.printf("  ERROR:  ");
				for (int j=0; j<sorted.length; j++)
				{
					if (j == i)
						System.out.printf(" .");
					System.out.printf(" %s", dup[j]);
				}
				System.out.printf("\n");

				System.out.printf("\n");
				break;
			}
		}
	}
}


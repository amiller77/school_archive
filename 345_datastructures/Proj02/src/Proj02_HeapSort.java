import java.io.PrintWriter;

public class Proj02_HeapSort implements Proj01_Sort{

	// PRIVATE VARIABLES
	private boolean debug;
	
	//CONSTRUCTOR
	public Proj02_HeapSort (boolean debug) {
		this.debug=debug;
	}
	
	/* 
	 * SORT (IMPLEMENTS PROJ01_SORT)
	 * note: can't use remove max while sorting, because that readjusts the array into a heap
	 * each time, and we are trying to turn it into a sorted array, not a heap
	 */
	public void sort(Comparable[] arr) {
		Proj02_MaxHeap heap = new Proj02_MaxHeap(this.debug,arr);
		Integer j = heap.findHeapEnd();
		// edge case: empty heap
		if (j==null) {
			System.out.println("empty heap!");
			return;
		}
		// iterate over all the nodes
		for (int i =j;i>0;i--) {
			// pull the max, swap and and bubble down with "conceptual heap end"
			Comparable max = heap.removeMax(i);
			heap.get()[i]=max;
			// DEBUG
			if (this.debug) {
				heap.generateDot("sortingStep"+(Math.abs(j-i)+1));
			}
		}
		// now overwrite arr with our sorted heap
		Proj02_MaxHeap.deepCopyPaste(heap.get(), arr);
	}
}

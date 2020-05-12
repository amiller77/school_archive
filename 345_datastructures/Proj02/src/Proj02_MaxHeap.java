import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Proj02_MaxHeap {
	
	// PRIVATE VARIABLES
	private Comparable[] heap;
	private boolean debug;
	private int dotIterator;
	
	
	//********** PRINCIPLE PUBLIC INTERFACE ************
	
	// CONSTRUCTOR_A
	// instantiate empty heap in array of length 4
	public Proj02_MaxHeap (boolean debug) {
		this.dotIterator=0;
		this.debug=debug;
		this.heap = new Comparable[4];
		// DEBUG
		if (this.debug) {
			generateDot("emptyArrayBuilt");
		}
	}
	
	// CONSTRUCTOR_B
	// pull a deep copy of the parameter array, convert to heap
	public Proj02_MaxHeap(boolean debug, Comparable[] arr) {
		this.dotIterator = 0;
		this.debug=debug;
		// pull a deep copy of the array and assign to heap:
		this.heap = new Comparable[arr.length];
		deepCopyPaste(arr,this.heap);
		// build the heap in place:
		buildHeap();
	}
	
	// INSERT
	// append to the end, then bubble up
	public void insert(Comparable node) {
		int bubbleUpIndex;
		Integer lastNodeIndex = findHeapEnd();
		// edge case: heap is empty -> assign to root and return
		if (lastNodeIndex==null) {
			this.heap[0]=node;
			// DEBUG
			if (this.debug) {
				generateDot("insertionComplete");
			}
			return;
		}
		// edge case: heap is full -> grow the array before appending
		if (lastNodeIndex==this.heap.length-1) {
			growArray();
		}
		// general case operations:
		this.heap[lastNodeIndex+1]=node;
		bubbleUp(lastNodeIndex+1);
		// DEBUG
		if (this.debug) {
			generateDot("insertionComplete");
		}
	}
	
	// REMOVE MAX
	// swap max with last element, chop n' save, bubble new root down, return max
	public Comparable removeMax() {
		Integer lastNodeIndex = findHeapEnd();
		return removeMaxCodeCore(lastNodeIndex);

	}
	/*
	 *  REMOVE MAX OVERLOAD
	 *  for HeapSort use: allow for different conceptual end to heap as parameter
	 *  rather than the true last element of the array
	 */
	public Comparable removeMax(Integer heapEnd) {
		return removeMaxCodeCore(heapEnd);
	}
	// REMOVE MAX CODE CORE:
	private Comparable removeMaxCodeCore(Integer lastNodeIndex) {
		// edge case: empty heap
		if (lastNodeIndex==null) {
			return null;
		}
		// swap the first and last elements, chop, bubble
		Comparable buffer = this.heap[0];
		this.heap[0]=this.heap[lastNodeIndex];
		this.heap[lastNodeIndex]=null;
		bubbleDown(0,lastNodeIndex);
		// DEBUG
		if (this.debug) {
			generateDot("removeMaxComplete");
		}
		return buffer;
	}
	
	// DUMP
	// print elements, space delineated, to PrintWriter
	public void dump(PrintWriter p) {
		String printString = "";
		// just in case there's some weird edge case
		if (this.heap.length == 0) {
			p.println("");
			return;
		}
		// general case:
		for (int i = 0; i< this.heap.length;i++) {
			if (this.heap[i] != null) {
				printString += this.heap[i]+" ";
			} 
		}
		p.println(printString.trim());
		return;
	}
	
	// FIND HEAP END
	// return index of the last non-null element, null if empty
	public Integer findHeapEnd() {
		// iterate backwards through the heap until you find non null
		for (int i = this.heap.length-1;i>=0;i--) {
			if (this.heap[i] != null) {
				return i;
			}
		}
		return null;
	}
	
	// GET
	// returns heap
	public Comparable[] get() {
		return this.heap;
	}
	
	// ********* INTERNAL HEAP MECHANICS ************
	
	/*
	 * GET CHILDREN (INDICES!)
	 * takes parent index as arg, returns array of length 2
	 * where left child index is at array[0], and right at array[1]
	 * 
	 * for heapSort: also takes heapEnd, to allow for configurable conceptual end to
	 * heap
	 * 
	 * for heap: just pass null as heapEnd
	 * 
	 * this method also validates the children, so if they are g.t.o.e.t. heapEnd
	 * or are null, then their value in the returned array is also null
	 * 
	 * this allows us to easily check where the children are and if they are valid
	 * or not in one encapsulated step
	 */
	private Integer[] getChildren(int parentIndex, Integer heapEnd) {
		// normal heap case:
		if (heapEnd==null) {
			heapEnd = this.heap.length;
		}
		// find children indices, create an array to store them
		int leftChildIndex = ((2*parentIndex)+1);
		int rightChildIndex = ((2*parentIndex)+2);
		Integer[] children = {leftChildIndex,rightChildIndex};
		// validate the children & make null if invalid
		if ((leftChildIndex>= heapEnd) || (this.heap[leftChildIndex]==null)) {
			children[0] = null;
		}
		if ((rightChildIndex>= heapEnd) || (this.heap[rightChildIndex]==null)) {
			children[1] = null;
		}
		return children;
	}
	
	/*
	 * FIND MAX CHILD
	 * takes a children array as argument (see GET CHILDREN)
	 * returns the index of the larger, or null if both are null
	 */
	private Integer findMaxChild(Integer[] children) {
		// if left is null, then right is null, so return null
		if (children[0]==null) {
			return null;
		}
		// if left not null, but right null, return left
		if (children[1]==null) {
			return children[0];
		}
		// else, neither is null, so perform compareTo, return larger
		// to maintain stability, return left if left >= right
		else {
			if (this.heap[children[0]].compareTo(this.heap[children[1]])>=0) {
				return children[0];
			}
			else {
				return children[1];
			}
		}
	}
	
	// GET PARENT (INDEX!)
	// finds parent index from a child index, or returns null if there isn't one
	private static Integer getParent(int childIndex) {
		Integer parentIndex;
		// where parent is relatively depends on left or right child
		// and left children are odd
		if (childIndex % 2 != 0) {
			parentIndex = ((childIndex-1)/2);
		} else {
			parentIndex = ((childIndex-2)/2);
		}
		if (parentIndex < 0) {
			parentIndex = null;
		}
		return parentIndex;
	}
	
	// GROW ARRAY
	// create a buffer of double length, deep copy the heap over,
	// reassign the heap to point to the buffer
	private void growArray() {
		Comparable[] buffer = new Comparable[this.heap.length*2];
		deepCopyPaste(this.heap,buffer);
		this.heap=buffer;
	}
	
	/*
	 *  BUBBLE DOWN
	 *  for heap: just call findHeapEnd() and pass as heapEnd
	 *  for HeapSort use: allow for different conceptual end to heap as parameter
	 *  rather than the true last element of the array
	 *  	^ this is just passed along to getChildren, with similar functionality
	 *  	^ IF YOU DO NOT WANT TO VARY HEAP LENGTH, GIVE NULL FOR HEAPEND
	 */
	public void bubbleDown(int nodeIndex, Integer heapEnd) {
		// find children: 
		Integer[] children = getChildren(nodeIndex, heapEnd);
		// use GET MAX CHILD to get the larger child, or null if both are null
		Integer largerChildIndex = findMaxChild(children);
		if (largerChildIndex==null) {
			return;
		}
		// if parent is smaller: swap, update parent index, bubble again
		if (this.heap[nodeIndex].compareTo(this.heap[largerChildIndex])<0) {
			Comparable buffer = this.heap[largerChildIndex];
			this.heap[largerChildIndex]=this.heap[nodeIndex];
			this.heap[nodeIndex]=buffer;
			nodeIndex=largerChildIndex;
			bubbleDown(nodeIndex,heapEnd);
		}
	}
		
	// BUBBLE UP
	// recursively bubbles up from the original node
	private void bubbleUp(int nodeIndex) {
		// find parent:
		Integer parentIndex = getParent(nodeIndex);
		// edge case: parent is invalid
		if (parentIndex==null) {
			return;
		}
		// if child is larger: swap, update child index, bubble again
		if (this.heap[nodeIndex].compareTo(this.heap[parentIndex])>0) {
			Comparable buffer = this.heap[parentIndex];
			this.heap[parentIndex]=this.heap[nodeIndex];
			this.heap[nodeIndex]=buffer;
			nodeIndex=parentIndex;
			bubbleUp(nodeIndex);
		}
	}
	
	// BUILD HEAP
	// builds the heap in place
	private void buildHeap() {
		Integer lastNodeIndex = findHeapEnd();
		// edge case: heap is empty
		if (lastNodeIndex == null) {
			return;
		}
		// find last parent:
		Integer lastParentIndex = getParent(lastNodeIndex);
		// if no parent, return
		if (lastParentIndex == null) {
			return;
		}
		// iterate back over the parents, and bubble them down
		for (int i = lastParentIndex;i>=0;i--) {
			bubbleDown(i,null);
		}
		// DEBUG
		if (this.debug) {
			generateDot("builtHeapFromArr");
		}
	}
	
	
	// ********** EXTERNAL LOGISTICS ***********
	
	// GENERATE DOT
	public void generateDot(String context) {
		// set up:
		FileWriter f = createFileWriter(context);
		PrintWriter p = new PrintWriter(f);
		// write to file:
		p.println("digraph");
		p.println("{");
		// write all the nodes & their indices, if not null
		for (int i = 0; i < this.heap.length; i++) {
			if (heap[i]!=null) {
				p.println("\t"+i+"."+heap[i]+";");
			}
		}
		// write all the connections: 
		// for each parent, at least 1 child connection:
		for (int i=0; i<this.heap.length; i++) {
			Integer[] children = getChildren(i,null);
			if (children[0]!=null) {
				p.println("\t"+i+"."+heap[i]+" -> "+children[0]+"."+heap[children[0]]+";");
			} 
			// if there are no more left children, there are no more children:
			else {
				break;
			}
			if (children[1] != null) {
				p.println("\t"+i+"."+heap[i]+" -> "+children[1]+"."+heap[children[1]]+";");
			}
		}
		// wrapping up:
		p.println("}");
		p.close();
		// iterate our file marker
		this.dotIterator++;
	}

	// DEEP COPY PASTE
	// takes first array and pastes it over onto the second array
	public static void deepCopyPaste(Comparable[] copyFrom, Comparable[] pasteTo) {
		// if second array is smaller, we have a problem
		if (pasteTo.length < copyFrom.length) {
			System.out.println("ERROR: You are attempting to paste to a shorter array than the original. ");
			return;
		}
		for (int i = 0; i<copyFrom.length; i++) {
			pasteTo[i]=copyFrom[i];
		}
	}
	
	// ********* INTERNAL LOGISTICS ***********
	
	// CREATE FILE WRITER
	// gets the try-catch out of a way in an encapsulated way
	private FileWriter createFileWriter(String context) {
		try {
			FileWriter f = new FileWriter(this.dotIterator+context+".dot");
			return f;
			
		} catch (IOException io) {
			System.out.println("IO issue: cannot write a new file .");
		}
		return null;
	}

	
	/*
	 * 
	 * 
	 * note: since there's no way to deep copy a comparable in general
	 * the swap & copy/paste operations only work for comparables where pass by value is used
	 * 
	 * 
	 * note: FIND HEAP END doesn't work with arrays of length 0
	 * 
	 * debug "Debug"/generateDot
	 * 
	 * remove print(debug) from sort fn
	 */
	
	
}


/**
* PRIORITY QUEUE
* stores edges in a min heap
*/
public class PriorityQueue {
	
	// **** INSTANCE VARIABLES ****
	// store our edges:
	private Node[] nodes;
	// tracks location of last entry; -1 -> empty queue
	private int lastEntryIndex;
	
	// **** CONSTRUCTOR **** 
	// initializes array to size 8
	public PriorityQueue () {
		this.nodes = new Node[8];
		this.lastEntryIndex = -1;
	}
	
	
	// *******************************************************
	// **************** PUBLIC INTERFACE *********************
	
	/**
	 * INSERT
	 * takes a Node and adds it to the priority queue
	 * add to end, bubble it up
	 * @param e
	 */
	public void insert(Node n) {
		// if already in heap, move it to the proper location (has a new distance) by bubbling it down and up
		if (n.index != null) {
			bubbleDown(n);
			bubbleUp(n);
			return;
		}
		// grow array if necessary
		if (lastEntryIndex >= nodes.length - 1) {
			resizeArray();
		}
		// add to end
		lastEntryIndex++;
		nodes[lastEntryIndex] = n;
		n.index = lastEntryIndex;
		// bubble up
		bubbleUp(n);
	}
	
	/**
	 * POP
	 * @return Node object that has been popped from the queue, or null if empty
	 */
	public Node pop() {
		// if empty -> return null
		if (lastEntryIndex == -1) {
			return null;
		}
		// if single item -> return ,decrement last entry index
		else if (lastEntryIndex == 0) {
			Node returnNode = nodes[0];
			nodes[0] = null;
			lastEntryIndex = -1;
			return returnNode;
		}
		// swap last element with first element, ax the (former) first, bubble (former) last down
		Node firstElement = nodes[0];
		// move last element to first position; update index
		nodes[0] = nodes[lastEntryIndex];
		nodes[0].index = 0;
		// remove last, change last index counter
		nodes[lastEntryIndex] = null;
		lastEntryIndex--;
		bubbleDown(nodes[0]);
		// nullify index and return
		firstElement.index = null;
		return firstElement;
	}
	
	// *********************************************************
	// **************** INTERNAL MECHANICS *********************
	
	/**
	 * RESIZE ARRAY
	 * resizes the internal array
	 */
	private void resizeArray() {
		// double the size:
		Node[] newArray = new Node[nodes.length*2];
		// copy over the vals:
		for (int i = 0; i< nodes.length; i++) {
			newArray[i] = nodes[i];
		}
		// overwrite the instance variable:
		nodes = newArray;
	}
	
	/**
	 * BUBBLE UP
	 * bubble up from index
	 */
	private void bubbleUp(Node n) {
		int nStartingIndex = n.index;
		Integer parentIndex = getParentIndex(nStartingIndex);
		// if parent d.n.e., return
		if (parentIndex == null) {
			return;
		}
		Node parent = nodes[parentIndex];
		// if parent is smaller, return
		if (parent.distance < n.distance) {
			return;
		} 
		// otherwise swap 'em
		nodes[parentIndex] = n;
		nodes[nStartingIndex] = parent;
		n.index = parentIndex;
		parent.index = nStartingIndex;
		
		// call bubble up again
		bubbleUp(n);
	}
	
	/**
	 * BUBBLE DOWN
	 * bubble down from index
	 */
	private void bubbleDown(Node n) {
		int nStartingIndex = n.index;
		// find children:
		Integer leftChildIndex = getLeftChildIndex(nStartingIndex);
		Integer rightChildIndex = getRightChildIndex(nStartingIndex);
		// if neither exists, return
		if (leftChildIndex == null && rightChildIndex == null) {
			return;
		}
		// compare the children, see which is smaller (or non-null):
		Integer smallerChildIndex;
		if (rightChildIndex == null) {
			smallerChildIndex = leftChildIndex;
		} else if (nodes[leftChildIndex].distance <= nodes[rightChildIndex].distance ) {
			smallerChildIndex = leftChildIndex;
		} else {
			smallerChildIndex = rightChildIndex;
		}
		// if smaller than the larger child, return
		if (n.distance < nodes[smallerChildIndex].distance ) {
			//System.out.println(n.name+" bubbled down to "+nStartingIndex);
			return;
		}
		// otherwise, swap 'em
		nodes[nStartingIndex] = nodes[smallerChildIndex];
		nodes[smallerChildIndex] = n;
		nodes[nStartingIndex].index = nStartingIndex;
		n.index = smallerChildIndex;
		
		// call bubble down again
		bubbleDown(n);
	}

	
	/**
	 * GET LEFT CHILD
	 * returns left child index, or null if none exists, or if request invalid
	 * @param index of parent
	 */
	private Integer getLeftChildIndex(int parentIndex) {
		int leftChildIndex = 2*parentIndex + 1;
		// validate:
		if (leftChildIndex > lastEntryIndex) {
			return null;
		} else {
			return leftChildIndex;
		}
	}
	
	/**
	 * GET RIGHT CHILD
	 * returns right child edge, or null if none exists, or if request invalid
	 * @param index of parent
	 */
	private Integer getRightChildIndex(int parentIndex) {
		int rightChildIndex = 2*parentIndex + 2;
		// validate:
		if (rightChildIndex > lastEntryIndex) {
			return null;
		} else {
			return rightChildIndex;
		}
	}
	
	/**
	 * GET PARENT
	 * returns parent edge, or null if none exists, or if request invalid
	 * @param index of child
	 */
	private Integer getParentIndex(int childIndex) {
		int parentIndex;
		// determine if left or right child first, to find parent index:
		if (childIndex % 2 == 0) {
			// even -> right child
			parentIndex = (childIndex / 2) - 1;
		} else {
			parentIndex = (childIndex - 1) / 2;
		}
		// validate:
		if (parentIndex < 0 || parentIndex > lastEntryIndex) {
			return null;
		} else {
			return parentIndex;
		}
	}
	
	
}
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * This tree designed to work for String/Integer and Integer/String pairs
 * @author alexander miller
 *
 * @param <K>
 * @param <V>
 */
public class Proj04_AVL_student<K extends Comparable<K>,V> implements Proj04_Dictionary<K, V> {

	// INSTANCE VARIABLES
	private AVLNode root;
	private int debugSuite;
	private String debugPrefix;
	
	// CONSTRUCTOR
	public Proj04_AVL_student(String debug) {
		this.root=null;
		this.debugSuite=0;
		this.debugPrefix = debug;
	}
	
	// **************************************************************
	// **************** EXTERNAL INTERFACE **************************
	
	/** 
	 * SET
	 * 
	 * Inserts a given (key,value) pair into the tree.
	 * 
	 * If the key already exists, then update the stored value to the new
	 * value; do *NOT* store multiple values for the same key!
	 * 
	 * This call may assume that both the key and value are non-null.
	 *
	 */
	public void set(K key, V value) {
		this.root = set_helper(this.root, key,value);
	}

	// **** HELPER FUNCTION FOR SET ****
	// SET HELPER
	// recursive function for use with insertion
	private AVLNode<K,V> set_helper(AVLNode<K,V> oldRoot,K key, V value){
		if (oldRoot == null) {
			oldRoot = new AVLNode(key,value);
		} else if (oldRoot.key.equals(key)) {
			oldRoot.value=value;
		} else if (key.compareTo(oldRoot.key)>0) {
			oldRoot.right = set_helper(oldRoot.right,key,value);
			// rebalance tree with updated height & count
			oldRoot = balanceSubtree(oldRoot);
		} else {
			oldRoot.left=set_helper(oldRoot.left,key,value);
			// rebalance tree with updated height & count
			oldRoot = balanceSubtree(oldRoot);
		}
		return oldRoot;
	}
	
	/**
	 * REMOVE 
	 * 
	 * Removes a given node from the tree
	 *
	 * This is a NOP if the key does not exist.
	 * 
	 * delete case 3: pull up predecessor
	 */
	public void remove(K key) {
		this.root = removeHelper(this.root,key);
	}
	
	// **** REMOVE HELPER ****
	public AVLNode<K,V> removeHelper(AVLNode<K,V> oldroot, K key) {
		// base case:
		if (oldroot==null) {
			return null;
		} else if (key.equals(oldroot.key)) {
			// CASE 1:
			// return null node
			if (oldroot.left == null && oldroot.right==null) {
				return null;
			}
			// CASE 2:
			// return the child as the node
			else if (oldroot.left==null && oldroot.right !=null) {
				return oldroot.right;
			}
			// CASE 2:
			else if (oldroot.right==null && oldroot.left !=null) {
				return oldroot.left;
			}
			// CASE 3:
			// swap key,vals with predecessor, then call delete again on key
			else {
				AVLNode<K,V> predecessor = getPredecessor(oldroot);
				K bufferKey = oldroot.key;
				V bufferValue = oldroot.value;
				oldroot.key=predecessor.key;
				oldroot.value=predecessor.value;
				predecessor.key=bufferKey;
				predecessor.value=bufferValue;
				/*
				 * now to delete: if the predecessor is a child of the node, the order of the BST
				 * is not preserved, and so we cannot find it to delete it under normal algorithm
				 * so, we need to update from the left child to avoid this
				 */
				oldroot.left = removeHelper(oldroot.left,key);
			}
			// CONTINUE SEARCHING TREE:
		} else if (key.compareTo(oldroot.key)>0) {
			// it isn't in tree, just return the root
			if (oldroot.right==null) {
				return oldroot;
			}
			// recurse right, update counts of node and right child, return
			oldroot.right=removeHelper(oldroot.right,key);
		} else {
			// it isn't in tree, just return the root
			if (oldroot.left==null) {
				return oldroot;
			}
			// recurse left, update counts of node and left child, return
			oldroot.left=removeHelper(oldroot.left,key);
			//System.out.println("oldroot: "+oldroot.key+" has left child: "+oldroot.left+" after recursion");
		}
		// update count/height for root ; balance for root and return
		oldroot = balanceSubtree(oldroot);
		return oldroot;
	}
	
	
	
	// BALANCE SUBTREE
	// rebalances subtree and updates all heights/counts
	private AVLNode<K,V> balanceSubtree(AVLNode<K,V> oldRoot) {
		// get heights of children
		int leftHeight = getHeight(oldRoot.left);
		int rightHeight = getHeight(oldRoot.right);
		// see which child is larger:
		boolean leftIsLarger;
		if (leftHeight >= rightHeight) {
			leftIsLarger = true;
		} else {
			leftIsLarger = false;
		}
		
		// base case: already balanced -> update height/count
		if (Math.abs(rightHeight-leftHeight)<2) {
			//System.out.println("subtree balanced at "+oldRoot.key+". update height.");
			updateCount(oldRoot);
			updateHeight(oldRoot);
			return oldRoot;
		}
		
		// if consists of 2 valid, non-null subtrees, but an imbalance occurs
		if (oldRoot.left != null && oldRoot.right != null) {
			oldRoot = complexCaseRebalance(leftIsLarger, oldRoot);
			
		}
		// primitive class imbalance A : LL
		else if (oldRoot.left != null && oldRoot.left.left != null) {
			// move root down to right, pull left child up
			oldRoot = rotateRightHelper(oldRoot,oldRoot.key);
			
		} // primitive class imbalance B : RR
		else if (oldRoot.right!=null && oldRoot.right.right!=null) {
			// move root down to left, pull right child up
			oldRoot = rotateLeftHelper(oldRoot,oldRoot.key);
			
		} // primitive class imbalance C : LR
		else if (oldRoot.left!=null && oldRoot.left.right!=null ) {
			// move left child down to left, pulling up LR grandchild
			oldRoot.left = rotateLeftHelper(oldRoot.left,oldRoot.left.key);
			// move root down to right, pulling up what was originally LR grandchild
			oldRoot = rotateRightHelper(oldRoot, oldRoot.key);
			
		} // primitive class imbalance C : RL
		else if (oldRoot.right!=null && oldRoot.right.left!=null) {
			// move right child down to left, pulling up RL grandchild
			oldRoot.right = rotateRightHelper(oldRoot.right,oldRoot.right.key);
			// move root down to left, pulling up what was originally RL grandchild
			oldRoot = rotateLeftHelper(oldRoot,oldRoot.key);
		}
		
		return oldRoot;
	}
	
	// **** HELPER FOR BALANCE SUBTREE ****
	// COMPLEX CASE REBALANCE
	// pre-condition : both left and right children exist; both are valid AVL trees
	private AVLNode<K,V> complexCaseRebalance(boolean leftIsLarger, AVLNode<K,V> oldRoot) {
		// case A: left is larger:
		if (leftIsLarger) {
			// shift weight to left in subtree if necessary
			if ( getHeight(oldRoot.left.right)>getHeight(oldRoot.left.left) ) {
				// move root of the subtree down to the left 
				oldRoot.left = rotateLeftHelper(oldRoot.left,oldRoot.left.key);
			}
			// move right at root (throw weight right in the larger tree)
			oldRoot = rotateRightHelper(oldRoot, oldRoot.key);
			return oldRoot;
		}
		// case b: right is larger:
		if (!leftIsLarger) {
			// shift weight to right in subtree if necessary
			if (getHeight(oldRoot.right.left)>getHeight(oldRoot.right.right)) {
				// move root of the subtree down to the right 
				oldRoot.right = rotateRightHelper(oldRoot.right, oldRoot.right.key);
			}
			// move left at root (throw weight left in the larger tree)
			oldRoot = rotateLeftHelper(oldRoot,oldRoot.key);
		}
		return oldRoot;
	}

	
	/** 
	 * GET
	 * 
	 * returns the value or null if it is not found.
	 */
	public V get(K key) {
		AVLNode<K,V> node = getNode(key);
		if (node==null) {
			return null;
		} else {
			return node.value;
		}
	}

	/**
	 * GET SIZE
	 * 
	 * Returns the number of keys stored in the tree.  
	 * This *MUST* run in O(1) time.
	 * 
	 */
	public int getSize() {
		return getCount(this.root);
	}


	/**
	 * IN ORDER
	 *
	 * all three param arrays are non-null, and have enough space to hold entries for all of the
	 * nodes in the tree
	 * 
	 * arrays may not be *exactly* that length; might be longer
	 * 
	 * heights in auxOut: h=1, etc.
	 * 
	 */
	public void inOrder(K[] keysOut, V[] valuesOut, String[] auxOut) {
		// make sure they're good n' clean first:
		wipeArray(keysOut);
		wipeArray(valuesOut);
		wipeArray(auxOut);
		// fill 'er up
		inOrderHelper(this.root,keysOut,valuesOut,auxOut);
	}
	
	// **** IN ORDER HELPER ****
	public void inOrderHelper(AVLNode<K,V> oldroot, K[] keysOut, V[] valuesOut, String[] auxOut) {
		// edge case:
		if (oldroot == null) {
			return;
			// leaf case:
		} else if (oldroot.left == null && oldroot.right==null){
			append(keysOut,oldroot.key);
			Integer index = append(valuesOut,oldroot.value);
			auxOut[index]="h="+oldroot.height;
			return;
			// node case: recurse left, print, then right
		} else {
			inOrderHelper(oldroot.left,keysOut,valuesOut,auxOut);
			append(keysOut,oldroot.key);
			Integer index = append(valuesOut,oldroot.value);
			auxOut[index]="h="+oldroot.height;
			inOrderHelper(oldroot.right,keysOut,valuesOut,auxOut);
		}
	}
	

	/**
	 * POST ORDER 
	 * 
	 * Same as inOrder(), except that it performs a post-order traversal.
	 */
	 public void postOrder(K[] keysOut, V[] valuesOut, String[] auxOut) {
		// make sure they're good n' clean first:
		wipeArray(keysOut);
		wipeArray(valuesOut);
		wipeArray(auxOut);
		// fill 'er up
		postOrderHelper(this.root,keysOut,valuesOut,auxOut);
	 }
		
	// **** POST ORDER HELPER ****
	public void postOrderHelper(AVLNode<K,V> oldroot, K[] keysOut, V[] valuesOut, String[] auxOut) {
		// edge case:
		if (oldroot == null) {
			return;
			// leaf case:
		} else if (oldroot.left == null && oldroot.right==null){
			append(keysOut,oldroot.key);
			Integer index = append(valuesOut,oldroot.value);
			auxOut[index]="h="+oldroot.height;
			return;
			// node case: recurse left, then right, then print
		} else {
			postOrderHelper(oldroot.left,keysOut,valuesOut,auxOut);
			postOrderHelper(oldroot.right,keysOut,valuesOut,auxOut);
			append(keysOut,oldroot.key);
			Integer index = append(valuesOut,oldroot.value);
			auxOut[index]="h="+oldroot.height;
		}
	}
	 

	/**
	 * GEN DEBUG DOT
	 * 
	 * if called multiple times, must all have different filenames.
	 *
	 * must not include any whitespace in the name.
	 */
	public void genDebugDot() {
		// set up:
		FileWriter f = createFileWriter();
		PrintWriter p = new PrintWriter(f);
		// write to file:
		p.println("digraph");
		p.println("{");
		// write all nodes, with their keys and vals
		writeNodes(this.root,p);
		// write all connections
		writeConnections(this.root,p);
		// wrapping up:
		p.println("}");
		p.close();
		// iterate our file marker
		this.debugSuite++;
	}
	
	// **** GEN DEBUG DOT HELPERS ****
	// CREATE FILE WRITER
	// gets the try-catch out of a way in an encapsulated way
	private FileWriter createFileWriter() {
		try {
			FileWriter f = new FileWriter(this.debugPrefix+this.debugSuite+".dot");
			return f;
			
		} catch (IOException io) {
			System.out.println("IO issue: cannot write a new file .");
		}
		return null;
	}
	
	// WRITE NODES
	private void writeNodes(AVLNode<K,V> oldroot, PrintWriter p) {
		if (oldroot==null) {
			return;
		}
		p.println("\t"+"\""+oldroot.key+"="+oldroot.value+" height="+oldroot.height+"\""+";");
		writeNodes(oldroot.left,p);
		writeNodes(oldroot.right,p);
	}
	
	// WRITE CONNECTIONS
	private void writeConnections(AVLNode<K,V> oldroot, PrintWriter p) {
		if (oldroot==null) {
			return;
		}
		if (oldroot.left != null) {
			p.println("\t"+"\""+oldroot.key+"="+oldroot.value+" height="+oldroot.height+"\""+" -> "+"\""+oldroot.left.key+"="+oldroot.left.value+" height="+oldroot.left.height+"\""+";");
		}
		if (oldroot.right !=null) {
			p.println("\t"+"\""+oldroot.key+"="+oldroot.value+" height="+oldroot.height+"\""+" -> "+"\""+oldroot.right.key+"="+oldroot.right.value+" height="+oldroot.right.height+"\""+";");
		}
		writeConnections(oldroot.left,p);
		writeConnections(oldroot.right,p);
	}
	
	
	// **************************************************************
	// ******************** INSIDE CLASSES **************************
	private class AVLNode<K extends Comparable,V> {
		
		// INSTANCE VARIABLES
		public K key;
		public V value;
		public int count;
		public AVLNode<K,V> left,right;
		
		public int height;
		
		// CONSTRUCTOR
		public AVLNode(K key, V value) {
			this.key = key;
			this.value = value;
			this.count = 1;
			this.height = 0;
		}
		
	}
	
	// **************************************************************
	// **************** INTERNAL MECHANICS **************************
	
	// **** RELATING TO NODES:
	
	// GET COUNT
	// only benefit of this is to qualify for null nodes systematically 
	private int getCount(AVLNode<K,V> node){
		if (node == null)
			return 0;
		return node.count;
	}
	
	// UPDATE COUNT
	private void updateCount(AVLNode<K,V> node) {
		node.count = getCount(node.left)+getCount(node.right)+1;
	}
	
	// GET HEIGHT
	private int getHeight(AVLNode<K,V> node) {
		if (node==null) {
			return -1;
		}
		return node.height;
	}
	
	// UPDATE HEIGHT
	private void updateHeight(AVLNode<K,V> node) {
		// height = maxChildHeight+1;
		if (getHeight(node.left)>= getHeight(node.right)) {
			node.height = getHeight(node.left)+1;
		} else {
			node.height = getHeight(node.right)+1;
		}
	}
	
	// **** TRAVERSING THE TREE:
	
	// GET PREDECESSOR
	// step one to the left, then go all the way right
	private AVLNode<K,V> getPredecessor(AVLNode<K,V> oldroot) {
		return recurseRight(oldroot.left);
	}
	
	// RECURSE RIGHT
	private AVLNode<K,V> recurseRight(AVLNode<K,V> oldroot) {
		if (oldroot.right==null) {
			return oldroot;
		} else {
			return recurseRight(oldroot.right);
		}
	}
	
	// GET NODE
	private AVLNode<K,V> getNode(K key) {
		return getNodeHelper(this.root,key);
	}
	
	// **** HELPER FOR GET NODE ****
	private AVLNode<K,V> getNodeHelper(AVLNode<K,V> oldroot, K key) {
		if (oldroot == null) {
			return null;
		} else if (oldroot.key.equals(key)) {
			return oldroot;
		} else if (key.compareTo(oldroot.key)>0) {
			return getNodeHelper(oldroot.right,key);
		} else {
			return getNodeHelper(oldroot.left,key);
		}
	}
	
	/* 
	 * ROTATE LEFT
	 * find node, rotate it left
	 * "rotate left" -> selected node moves down and right child moves up to be new root
	 *
	 * If key DNE or doesn't have right child, then do nothing
	 * 
	 * updates heights up to the new root of subtree
	 */
	public void rotateLeft(K at) {
		this.root = rotateLeftHelper(this.root,at);
	}
	
	// **** ROTATE LEFT HELPER ****
	// returns new root of subtree
	private AVLNode<K,V> rotateLeftHelper(AVLNode<K,V> oldroot, K key) {
		// edge case: null node
		if (oldroot==null) {
			return oldroot;
		}
		// base case: rotate the root
		if (oldroot.key.equals(key)) {
			// if right child null, return without change
			if (oldroot.right==null) {
				return oldroot;
			} else {
				// make the right child the new root
				AVLNode newRoot = oldroot.right;
				// move the left subtree of right child to new right child of oldroot
				oldroot.right=newRoot.left;
				// attach oldroot to left of the new root
				newRoot.left=oldroot;
				// update counters
				updateCount(oldroot);
				updateCount(newRoot);
				// update heights
				updateHeight(oldroot);
				updateHeight(newRoot);
				// return new root back to parent
				return newRoot;
			}
		} // otherwise, search for key:
		else if (key.compareTo(oldroot.key)>0) {
			oldroot.right = rotateLeftHelper(oldroot.right,key);	
		} else {
			oldroot.left = rotateLeftHelper(oldroot.left,key);
		}
		// update count/height and return
		updateCount(oldroot);
		updateHeight(oldroot);
		return oldroot;
	}
	
	/* ROTATE RIGHT
	 *
	 * Same as rotateLeft, except mirrored.
	 * 
	 * updates heights up to the new root of subtree
	 */
	public void rotateRight(K at) {
		this.root = rotateRightHelper(this.root,at);
	}

	// **** ROTATE RIGHT HELPER ****
	// returns new root of subtree
	private AVLNode<K,V> rotateRightHelper(AVLNode<K,V> oldroot, K key) {
		// edge case: null node
		if (oldroot==null) {
			return oldroot;
		}
		// base case: rotate the root
		if (oldroot.key.equals(key)) {
			// if left child null, return without change
			if (oldroot.left==null) {
				return oldroot;
			} else {
				// make the left child the new root
				AVLNode newRoot = oldroot.left;
				// move the right subtree of left child to new left child of oldroot
				oldroot.left=newRoot.right;
				// attach oldroot to right of the new root
				newRoot.right=oldroot;
				// update counters
				updateCount(oldroot);
				updateCount(newRoot);
				// update heights
				updateHeight(oldroot);
				updateHeight(newRoot);
				// return new root back to parent
				return newRoot;
			}
		} // otherwise, search for key:
		else if (key.compareTo(oldroot.key)>0) {
			oldroot.right = rotateRightHelper(oldroot.right,key);
		} else {
			oldroot.left = rotateRightHelper(oldroot.left,key);
		}
		// update count/height and return
		updateCount(oldroot);
		updateHeight(oldroot);
		return oldroot;
	}
	
	
	// **** WORKING WITH ARRAYS
	
	// APPEND METHODS
	// adds to end, returns the current index, or null if full
	private Integer append(K[] array, K item) {
		for (int i = 0; i<array.length; i++) {
			if (array[i]==null) {
				array[i]=item;
				return i;
			} 
		}
		return null;
	}
	private Integer append(V[] array, V item) {
		for (int i = 0; i<array.length; i++) {
			if (array[i]==null) {
				array[i]=item;
				return i;
			} 
		}
		return null;
	}
			
	// WIPE ARRAY
	private void wipeArray(K[] array) {
		for (int i = 0; i < array.length; i++) {
			array[i]=null;
		}
	}
	private void wipeArray(V[] array) {
		for (int i = 0; i < array.length; i++) {
			array[i]=null;
		}
	}
	private void wipeArray(String[] array) {
		for (int i = 0; i<array.length;i++) {
			array[i]=null;
		}
	}
		
	
}

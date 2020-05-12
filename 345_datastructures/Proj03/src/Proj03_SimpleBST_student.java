/* Proj03_SimpleBST_student
 *
 * Author: Alexander Miller
 */

import java.io.*;

public class Proj03_SimpleBST_student<K extends Comparable<K>, V> implements Proj03_BST<K,V> {

	// INSTANCE VARIABLES
	private Proj03_BSTNode root;
	private int debugSuite;
	private String debugPrefix;
	
	// CONSTRUCTOR
	// debugStr is prefix for dot file names
	public Proj03_SimpleBST_student(String debugStr){
		this.root=null;
		this.debugSuite=0;
		this.debugPrefix = debugStr;
	}

	// ****** EXTERNAL INTERFACE ******

	/* 
	 * SET
	 * Inserts a given (key,value) pair into the tree.
	 * This call may assume that both the key and value are non-null.
	 * If the key already exists, then update the stored value to the new value
	 */
	public void set(K key, V value){
		/* we use a recursive implementation, in the x=change(x)
		 * style, for most methods in this class.
		 */
		this.root = set_helper(this.root, key,value);
	}

	// **** HELPER FUNCTION FOR SET ****
	// SET HELPER
	// recursive function for use with insertion
	private Proj03_BSTNode<K,V> set_helper(Proj03_BSTNode<K,V> oldRoot,K key, V value){
		if (oldRoot == null) {
			oldRoot = new Proj03_BSTNode(key,value);
		} else if (oldRoot.key.equals(key)) {
			oldRoot.value=value;
		} else if (key.compareTo(oldRoot.key)>0) {
			oldRoot.right = set_helper(oldRoot.right,key,value);
			updateCount(oldRoot);
		} else {
			oldRoot.left=set_helper(oldRoot.left,key,value);
			updateCount(oldRoot);
		}
		return oldRoot;
	}
	
	/* 
	 * GET
	 * return value at key, or null if key absent
	 */
	public V get(K key) {
		Proj03_BSTNode<K,V> node = getNode(key);
		if (node==null) {
			return null;
		} else {
			return node.value;
		}
	}
	
	/* 
	 * REMOVE
	 * removes node at key if key present
	 */
	public void remove(K key) {
		this.root = removeHelper(this.root,key);
	}
	
	// **** REMOVE HELPER ****
	public Proj03_BSTNode<K,V> removeHelper(Proj03_BSTNode<K,V> oldroot, K key) {
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
				Proj03_BSTNode<K,V> predecessor = getPredecessor(oldroot);
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
		}
		// update count for root and return
		updateCount(oldroot);
		return oldroot;
	}
	
	/* 
	 * ROTATE LEFT
	 * find node, rotate it left
	 * "rotate left" -> selected node moves down and right child moves up to be new root
	 *
	 * If key DNE or doesn't have right child, then do nothing
	 */
	public void rotateLeft(K at) {
		this.root = rotateLeftHelper(this.root,at);
	}
	
	// **** ROTATE LEFT HELPER ****
	private Proj03_BSTNode<K,V> rotateLeftHelper(Proj03_BSTNode<K,V> oldroot, K key) {
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
				Proj03_BSTNode newRoot = oldroot.right;
				// move the left subtree of right child to new right child of oldroot
				oldroot.right=newRoot.left;
				// attach oldroot to left of the new root
				newRoot.left=oldroot;
				// update counters
				updateCount(oldroot);
				updateCount(newRoot);
				// return new root back to parent
				return newRoot;
			}
		} // otherwise, search for key:
		else if (key.compareTo(oldroot.key)>0) {
			oldroot.right = rotateLeftHelper(oldroot.right,key);	
		} else {
			oldroot.left = rotateLeftHelper(oldroot.left,key);
		}
		// update count and return
		updateCount(oldroot);
		return oldroot;
	}
	
	/* ROTATE RIGHT
	 *
	 * Same as rotateLeft, except mirrored.
	 */
	public void rotateRight(K at) {
		this.root = rotateRightHelper(this.root,at);
	}

	// **** ROTATE RIGHT HELPER ****
	private Proj03_BSTNode<K,V> rotateRightHelper(Proj03_BSTNode<K,V> oldroot, K key) {
		// edge case: null node
		if (oldroot==null) {
			return oldroot;
		}
		// base case: rotate the root
		if (oldroot.key.equals(key)) {
			// if right child null, return without change
			if (oldroot.left==null) {
				return oldroot;
			} else {
				// make the left child the new root
				Proj03_BSTNode newRoot = oldroot.left;
				// move the right subtree of left child to new left child of oldroot
				oldroot.left=newRoot.right;
				// attach oldroot to right of the new root
				newRoot.right=oldroot;
				// update counters
				updateCount(oldroot);
				updateCount(newRoot);
				// return new root back to parent
				return newRoot;
			}
		} // otherwise, search for key:
		else if (key.compareTo(oldroot.key)>0) {
			oldroot.right = rotateRightHelper(oldroot.right,key);
		} else {
			oldroot.left = rotateRightHelper(oldroot.left,key);
		}
		// update count and return
		updateCount(oldroot);
		return oldroot;
	}
	
	/* 
	 * GET SIZE
	 * Returns the number of keys stored in the tree.
	 * Runs in 0(1) time if node counts update during recursion
	 */
	public int getSize(){
		return getCount(root);
	}
	
	/* 
	 * IN ORDER
	 * caller provide arrays to be populated - one for keys, one for values,
	 * and one for the count fields of each node.
	 *
	 * these arrays will be long enough to accommodate values, but may be longer than strictly necessary
	 */
	public void inOrder(K[] keysOut, V[] valuesOut, int[] countsOut) {
		// make sure they're good n' clean first:
		wipeArray(keysOut);
		wipeArray(valuesOut);
		wipeArray(countsOut);
		// fill 'er up
		inOrderHelper(this.root,keysOut,valuesOut,countsOut);
	}
	
	// **** IN ORDER HELPER ****
	public void inOrderHelper(Proj03_BSTNode<K,V> oldroot, K[] keysOut, V[] valuesOut, int[] countsOut) {
		// edge case:
		if (oldroot == null) {
			return;
			// leaf case:
		} else if (oldroot.left == null && oldroot.right==null){
			append(keysOut,oldroot.key);
			Integer index = append(valuesOut,oldroot.value);
			countsOut[index]=oldroot.count;
			return;
			// node case: recurse left, print, then right
		} else {
			inOrderHelper(oldroot.left,keysOut,valuesOut,countsOut);
			append(keysOut,oldroot.key);
			Integer index = append(valuesOut,oldroot.value);
			countsOut[index]=oldroot.count;
			inOrderHelper(oldroot.right,keysOut,valuesOut,countsOut);
		}
	}
	
	/*
	 * POST ORDER
	 * Same as inOrder(), except that it performs a post-order traversal.
	 */
	public void postOrder(K[] keysOut, V[] valuesOut, int[] countsOut) {
		// make sure they're good n' clean first:
		wipeArray(keysOut);
		wipeArray(valuesOut);
		wipeArray(countsOut);
		// fill 'er up
		postOrderHelper(this.root,keysOut,valuesOut,countsOut);
	}
	
	// **** POST ORDER HELPER ****
	public void postOrderHelper(Proj03_BSTNode<K,V> oldroot, K[] keysOut, V[] valuesOut, int[] countsOut) {
		// edge case:
		if (oldroot == null) {
			return;
			// leaf case:
		} else if (oldroot.left == null && oldroot.right==null){
			append(keysOut,oldroot.key);
			Integer index = append(valuesOut,oldroot.value);
			countsOut[index]=oldroot.count;
			return;
			// node case: recurse left, then right, then print
		} else {
			postOrderHelper(oldroot.left,keysOut,valuesOut,countsOut);
			postOrderHelper(oldroot.right,keysOut,valuesOut,countsOut);
			append(keysOut,oldroot.key);
			Integer index = append(valuesOut,oldroot.value);
			countsOut[index]=oldroot.count;
		}
	}
	
	/* 
	 * GEN DEBUG DOT
	 * Generates a dot file which represents the tree; if this
	 * is called multiple times, then they must all have different
	 * filenames.
	 *
	 * names must not include any whitespace in the name.
	 */
	public void genDebugDot(){
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
	private void writeNodes(Proj03_BSTNode<K,V> oldroot, PrintWriter p) {
		if (oldroot==null) {
			return;
		}
		p.println("\t"+"\""+oldroot.key+"="+oldroot.value+" count="+oldroot.count+"\""+";");
		writeNodes(oldroot.left,p);
		writeNodes(oldroot.right,p);
	}
	
	// WRITE CONNECTIONS
	private void writeConnections(Proj03_BSTNode<K,V> oldroot, PrintWriter p) {
		if (oldroot==null) {
			return;
		}
		if (oldroot.left != null) {
			p.println("\t"+"\""+oldroot.key+"="+oldroot.value+" count="+oldroot.count+"\""+" -> "+"\""+oldroot.left.key+"="+oldroot.left.value+" count="+oldroot.left.count+"\""+";");
		}
		if (oldroot.right !=null) {
			p.println("\t"+"\""+oldroot.key+"="+oldroot.value+" count="+oldroot.count+"\""+" -> "+"\""+oldroot.right.key+"="+oldroot.right.value+" count="+oldroot.right.count+"\""+";");
		}
		writeConnections(oldroot.left,p);
		writeConnections(oldroot.right,p);
	}
	
	
	// ****** INTERNAL MECHANICS ******
	
	// GET COUNT
	// only benefit of this is to qualify for null nodes systematically 
	private int getCount(Proj03_BSTNode<K,V> node){
		if (node == null)
			return 0;
		return node.count;
	}
	
	// UPDATE COUNT
	private void updateCount(Proj03_BSTNode<K,V> node) {
		node.count = getCount(node.left)+getCount(node.right)+1;
	}

	// GET NODE
	private Proj03_BSTNode<K,V> getNode(K key) {
		return getNodeHelper(this.root,key);
	}
	
	// **** HELPER FOR GET NODE ****
	private Proj03_BSTNode<K,V> getNodeHelper(Proj03_BSTNode<K,V> oldroot, K key) {
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
	
	// GET PREDECESSOR
	// step one to the left, then go all the way right
	private Proj03_BSTNode<K,V> getPredecessor(Proj03_BSTNode<K,V> oldroot) {
		return recurseRight(oldroot.left);
	}
	
	// RECURSE RIGHT
	private Proj03_BSTNode<K,V> recurseRight(Proj03_BSTNode<K,V> oldroot) {
		if (oldroot.right==null) {
			return oldroot;
		} else {
			return recurseRight(oldroot.right);
		}
	}
	
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
	private void wipeArray(int[] array) {
		for (int i = 0; i<array.length;i++) {
			array[i]=0;
		}
	}

}

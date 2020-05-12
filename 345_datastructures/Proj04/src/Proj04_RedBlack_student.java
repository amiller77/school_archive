import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

/**
 * This tree designed to work for String/Integer and Integer/String pairs
 * @author alexander miller
 *
 * @param <K>
 * @param <V>
 */
public class Proj04_RedBlack_student<K extends Comparable<K>,V> implements Proj04_Dictionary<K, V> {

	// INSTANCE VARIABLES
	private RedBlackNode root;
	private int debugSuite;
	private String debugPrefix;
	
	// CONSTRUCTOR
	public Proj04_RedBlack_student(String debug) {
		this.root=null;
		this.debugSuite=0;
		this.debugPrefix = debug;
	}
	
	// CONSTRUCTOR_2 -> takes 234Node (root of 234 tree) param and builds a RBTree
	// if 2 vals in widget -> latter is the widget root (black)
	// insert by the widget root first, then lesser val, then greater val
	// then insert child1, child2... then child1.1, child 1.2 ...
	public Proj04_RedBlack_student(String debug, Proj04_234Node root) {
		// call constructor_1
		this(debug);
		// create queue to set order for insertion
		LinkedList<Proj04_234Node> processQueue = new LinkedList<Proj04_234Node>();
		// use recursive constructor helper to build RBTree
		constructorHelper(root,processQueue);
	}
	
	// *************** HELPER METHODS FOR CONSTRUCTOR_2 *****************
	
	private void constructorHelper(Proj04_234Node<K,V> root234, LinkedList<Proj04_234Node> processQueue) {
		// base case: root is null
		if (root234 == null) {
			return;
		}
		// otherwise: find size of widget
		int widgetSize = root234.numKeys;
		
		// case 1: widget size 1
		if (widgetSize == 1) {
			// find widget root, insert into tree as black
			BSTinsertion(root234.key1,root234.val1,"b");
			// no children values to insert...
			// add children 234nodes left to right to queue (2 potential children)
			processQueue.add(root234.child1);
			processQueue.add(root234.child2);
		}
		// case 2: widget size 2
		else if (widgetSize == 2) {
			// find widget root, insert into tree as black
			BSTinsertion(root234.key2,root234.val2,"b");
			//insert children values as RBNodes left to right as red (1 child val)
			BSTinsertion(root234.key1,root234.val1,"r");
			// add children 234nodes left to right to queue
			processQueue.add(root234.child1);
			processQueue.add(root234.child2);
			processQueue.add(root234.child3);
		}
		// case 3: widget size 3
		else {
			// find widget root, insert into tree as black
			BSTinsertion(root234.key2,root234.val2,"b");
			//insert children values as RBNodes left to right as red
			BSTinsertion(root234.key1,root234.val1,"r");
			BSTinsertion(root234.key3,root234.val3,"r");
			// add children 234nodes left to right to queue
			processQueue.add(root234.child1);
			processQueue.add(root234.child2);
			processQueue.add(root234.child3);
			processQueue.add(root234.child4);
		}
		// common ops:
		// pop the next 234node off queue and pass into constructorHelper
		constructorHelper(processQueue.poll(),processQueue);
	}
	
	/* 
	 * BST INSERTION
	 * Inserts a given (key,value) pair into the tree.
	 * This call may assume that both the key and value are non-null.
	 * If the key already exists, then update the stored value to the new value
	 */
	private void BSTinsertion(K key, V value, String color){
		this.root = BSTinsertionHelper(this.root, key,value,color);
	}

	// **** HELPER FUNCTION FOR SET ****
	// BST INSERTION HELPER
	// recursive function for use with insertion
	private RedBlackNode<K,V> BSTinsertionHelper(RedBlackNode<K,V> oldRoot,K key, V value, String color){
		
		if (oldRoot == null) {
			oldRoot = new RedBlackNode(key,value,color);
		}
		else if (key.compareTo(oldRoot.key)>0) {
			oldRoot.right = BSTinsertionHelper(oldRoot.right,key,value,color);
			updateCount(oldRoot);
		} else {
			oldRoot.left=BSTinsertionHelper(oldRoot.left,key,value,color);
			updateCount(oldRoot);
		}
		return oldRoot;
	}
	
	// **************************************************************
	// **************** EXTERNAL INTERFACE **************************
	
	/** 
	 * SET
	 * 
	 * Inserts a given (key,value) pair into the tree.
	 * 
	 * SET FOR EXISTING KEYS NOT SUPPORTED [and won't be tested -ignore this case]
	 * 
	 * This call may assume that both the key and value are non-null.
	 * 
	 * PREEMPTIVE SPLITTING approach used
	 *
	 */
	public void set(K key, V value) {
		this.root = set_helper(this.root, key,value);
		if (this.root!=null) {
			this.root.color="b";
		}
	}

	// **** HELPER FUNCTION FOR SET ****
	// SET HELPER
	// recursive function for use with insertion
	private RedBlackNode<K,V> set_helper(RedBlackNode<K,V> oldRoot,K key, V value){
		if (oldRoot == null) {
			oldRoot = new RedBlackNode(key,value);
			return oldRoot;
		} // haven't found it here, split the widget if it's black and full
		else {
			if (getColor(oldRoot).equals("b") && getColor(oldRoot.left).equals("r") 
					&& getColor(oldRoot.right).equals("r")) {
				oldRoot.color="r";
				oldRoot.left.color="b";
				oldRoot.right.color="b";
			}
		}
		// if searching right side of tree:
		if (key.compareTo(oldRoot.key)>0) {
			oldRoot.right = set_helper(oldRoot.right,key,value);
			updateCount(oldRoot);
			oldRoot = rebalanceWidget(oldRoot);
		} // if searching left side of tree: 
		else {
			oldRoot.left=set_helper(oldRoot.left,key,value);
			updateCount(oldRoot);
			oldRoot = rebalanceWidget(oldRoot);
		}
		return oldRoot;
	}
	
	// REBALANCE WIDGET
	private RedBlackNode<K,V> rebalanceWidget (RedBlackNode<K,V> oldRoot) {
		// base case: already balanced
		// balanced if kids null or black:
		if ( getColor(oldRoot.left).equals("b") && getColor(oldRoot.right).equals("b")) {
			return oldRoot;
		}
		
		// if left child red:
		if (getColor(oldRoot.left).equals("r")) {
			// check LL
			if (getColor(oldRoot.left.left).equals("r")) {
				// color root red
				oldRoot.color = "r";
				// color root.left black
				oldRoot.left.color = "b";
				// rotate right at root
				oldRoot = rotateRightHelper(oldRoot,oldRoot.key);
				return oldRoot;
			}
			// check LR
			else if (getColor(oldRoot.left.right).equals("r")) {
				// color root red
				oldRoot.color = "r";
				// color root.left.right black
				oldRoot.left.right.color="b";
				// rotate left at root.left
				oldRoot.left = rotateLeftHelper(oldRoot.left,oldRoot.left.key);
				// rotate right at root
				oldRoot = rotateRightHelper(oldRoot,oldRoot.key);
			}
			
		}
		// if right child red:
		if (getColor(oldRoot.right).equals("r")) {
			// check RR
			if (getColor(oldRoot.right.right).equals("r")) {
				// color root red
				oldRoot.color = "r";
				// color root.right black
				oldRoot.right.color= "b";
				// rotate left at root
				oldRoot = rotateLeftHelper(oldRoot,oldRoot.key);
			}
			// check RL
			else if (getColor(oldRoot.right.left).equals("r")) {
				// color root red
				oldRoot.color = "r";
				// color root.right.left black
				oldRoot.right.left.color = "b";
				// rotate right at root.left
				oldRoot.right = rotateRightHelper(oldRoot.right,oldRoot.right.key);
				// rotate left at root
				oldRoot = rotateLeftHelper(oldRoot,oldRoot.key);
			}
		}
		return oldRoot;
	}

	/** 
	 * GET
	 * 
	 * returns the value or null if it is not found.
	 */
	public V get(K key) {
		RedBlackNode<K,V> node = getNode(key);
		if (node==null) {
			return null;
		} else {
			return node.value;
		}
	}

	/**
	 * REMOVE 
	 * 
	 * OPERATION NOT SUPPORTED
	 */
	public void remove(K key) throws RuntimeException {
		throw new RuntimeException("Remove operation not supported for RBTree");
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
	 * colors in auxOut: b,r
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
	public void inOrderHelper(RedBlackNode<K,V> oldroot, K[] keysOut, V[] valuesOut, String[] auxOut) {
		// edge case:
		if (oldroot == null) {
			return;
			// leaf case:
		} else if (oldroot.left == null && oldroot.right==null){
			append(keysOut,oldroot.key);
			Integer index = append(valuesOut,oldroot.value);
			auxOut[index]=oldroot.color;
			return;
			// node case: recurse left, print, then right
		} else {
			inOrderHelper(oldroot.left,keysOut,valuesOut,auxOut);
			append(keysOut,oldroot.key);
			Integer index = append(valuesOut,oldroot.value);
			auxOut[index]=oldroot.color;
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
	public void postOrderHelper(RedBlackNode<K,V> oldroot, K[] keysOut, V[] valuesOut, String[] auxOut) {
		// edge case:
		if (oldroot == null) {
			return;
			// leaf case:
		} else if (oldroot.left == null && oldroot.right==null){
			append(keysOut,oldroot.key);
			Integer index = append(valuesOut,oldroot.value);
			auxOut[index]=oldroot.color;
			return;
			// node case: recurse left, then right, then print
		} else {
			postOrderHelper(oldroot.left,keysOut,valuesOut,auxOut);
			postOrderHelper(oldroot.right,keysOut,valuesOut,auxOut);
			append(keysOut,oldroot.key);
			Integer index = append(valuesOut,oldroot.value);
			auxOut[index]=oldroot.color;
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
	private void writeNodes(RedBlackNode<K,V> oldroot, PrintWriter p) {
		if (oldroot==null) {
			return;
		}
		p.println("\t"+"\""+oldroot.key+"="+oldroot.value+" color="+oldroot.color+"\""+";");
		writeNodes(oldroot.left,p);
		writeNodes(oldroot.right,p);
	}
	
	// WRITE CONNECTIONS
	private void writeConnections(RedBlackNode<K,V> oldroot, PrintWriter p) {
		if (oldroot==null) {
			return;
		}
		if (oldroot.left != null) {
			p.println("\t"+"\""+oldroot.key+"="+oldroot.value+" color="+oldroot.color+"\""+" -> "+"\""+oldroot.left.key+"="+oldroot.left.value+" color="+oldroot.left.color+"\""+";");
		}
		if (oldroot.right !=null) {
			p.println("\t"+"\""+oldroot.key+"="+oldroot.value+" color="+oldroot.color+"\""+" -> "+"\""+oldroot.right.key+"="+oldroot.right.value+" color="+oldroot.right.color+"\""+";");
		}
		writeConnections(oldroot.left,p);
		writeConnections(oldroot.right,p);
	}
	
	
	
	// **************************************************************
	// ******************** INSIDE CLASSES **************************
	private class RedBlackNode<K extends Comparable,V> {
		
		// INSTANCE VARIABLES
		public K key;
		public V value;
		public int count;
		public RedBlackNode<K,V> left,right;

		public String color; //b,r
		
		
		// CONSTRUCTOR
		public RedBlackNode(K key, V value) {
			this.key = key;
			this.value = value;
			this.count = 1;
			this.color = "r";
		}
		
		// CONSTRUCTOR_2
		// allows parameterization of color
		public RedBlackNode(K key, V value, String color) {
			this(key,value);
			this.color=color;
		}
		
	}
	
	// **************************************************************
	// **************** INTERNAL MECHANICS **************************
	
	// **** RELATING TO NODES:
	
	// GET COUNT
	// only benefit of this is to qualify for null nodes systematically 
	private int getCount(RedBlackNode<K,V> node){
		if (node == null)
			return 0;
		return node.count;
	}
	
	// GET COLOR
	private String getColor(RedBlackNode<K,V> node) {
		if (node==null) {
			return "b";
		} else {
			return node.color;
		}
	}
	
	// UPDATE COUNT
	private void updateCount(RedBlackNode<K,V> node) {
		node.count = getCount(node.left)+getCount(node.right)+1;
	}
	
	// **** TRAVERSING THE TREE:
	
	// GET PREDECESSOR
	// step one to the left, then go all the way right
	private RedBlackNode<K,V> getPredecessor(RedBlackNode<K,V> oldroot) {
		return recurseRight(oldroot.left);
	}
	
	// RECURSE RIGHT
	private RedBlackNode<K,V> recurseRight(RedBlackNode<K,V> oldroot) {
		if (oldroot.right==null) {
			return oldroot;
		} else {
			return recurseRight(oldroot.right);
		}
	}
	
	// GET NODE
	private RedBlackNode<K,V> getNode(K key) {
		return getNodeHelper(this.root,key);
	}
	
	// **** HELPER FOR GET NODE ****
	private RedBlackNode<K,V> getNodeHelper(RedBlackNode<K,V> oldroot, K key) {
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
	 */
	public void rotateLeft(K at) {
		this.root = rotateLeftHelper(this.root,at);
	}
	
	// **** ROTATE LEFT HELPER ****
	private RedBlackNode<K,V> rotateLeftHelper(RedBlackNode<K,V> oldroot, K key) {
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
				RedBlackNode newRoot = oldroot.right;
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
	private RedBlackNode<K,V> rotateRightHelper(RedBlackNode<K,V> oldroot, K key) {
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
				RedBlackNode newRoot = oldroot.left;
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

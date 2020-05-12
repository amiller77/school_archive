/* Proj03_BST
 *
 * Author: Russ Lewis
 *
 * Generic interface for a BST which maps one key type to another (that is,
 * it implements a dictionary-type BST).
 *
 * GENERIC PARAMTERS
 *   K (extends Comparable) - key type.  BSTs will use compareTo() to decide
 *       how to arrange the data.
 *   V - value type.  Can be absolutely *anything*, since we won't call any
 *       methods inside it - however, null is *never* valid, as null is used
 *       in this interface as "not found"
 */

public interface Proj03_BST<K extends Comparable, V>
{
	/* void set(K,V)
	 *
	 * Inserts a given (key,value) pair into the tree.
	 *
	 * This call may assume that both the key and value are non-null.
	 *
	 * If the key already exists, then update the stored value to the new
	 * value; do *NOT* store multiple values for the same key!
	 */
	void set(K key, V value);


	/* V get(K)
	 *
	 * Searches the tree for a given key; returns the associated value
	 * if the key is found, or null if it is not found.
	 */
	V get(K key);


	/* void remove(K)
	 *
	 * Removes a given node from the tree, found by the key.
	 *
	 * This is a NOP if the key does not exist.
	 */
	void remove(K key);


	/* void rotateLeft(K)
	 *
	 * Searches the tree for a given key, and then performs a rotate left
	 * at that location.  A "rotate left" means that the selected node
	 * moves down, and the right child of that node moves up to be the new
	 * root.
	 *
	 * If the key does not exist, or if it doesn't have a right child,
	 * then this is a NOP.
	 */
	void rotateLeft(K at);


	/* void rotateRight(K)
	 *
	 * Same as rotateLeft, except mirrored.
	 */
	void rotateRight(K at);


	/* int getSize()
	 *
	 * Returns the number of keys stored in the tree.  This *MUST* run in
	 * O(1) time.
	 */
	int getSize();


	/* void inOrder(K[], V[], int[])
	 *
	 * Performs an in-order traversal of the tree.  The caller must
	 * provide arrays to be populated - one for keys, one for values,
	 * and one for the count fields of each node.
	 *
	 * The caller is responsible for ensuring that all three arrays are
	 * non-null, and have enough space to hold entries for all of the
	 * nodes in the tree.  However, you should *not* assume that the
	 * arrays are *exactly* that length; they might be longer than
	 * required.
	 */
	void inOrder(K[] keysOut, V[] valuesOut, int[] countsOut);


	/* void postOrder(K[], V[])
	 *
	 * Same as inOrder(), except that it performs a post-order traversal.
	 */
	void postOrder(K[] keysOut, V[] valuesOut, int[] countsOut);


	/* void genDebugDot()
	 *
	 * Generates a \texttt{.dot} file which represents the tree; if this
	 * is called multiple times, then they must all have different
	 * filenames.
	 *
	 * The generated files must be placed in the *current* directory, and
	 * must not include any whitespace in the name.
	 */
	void genDebugDot();
}

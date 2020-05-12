/* Proj03_BSTNode
 *
 * Author: Russ Lewis
 *
 * Represents a single node in a BST.  Carries both a key and satellite data.
 * Also includes one metadata field.
 */

public class Proj03_BSTNode<K extends Comparable, V>
{
	public  K  key;
	public  V  value;
	public int count;

	public Proj03_BSTNode<K,V> left,right;


	/* constructor
	 *
	 * Parameters: K,V
	 *
	 * Generates a single BSTNode (with no children), with the key/value
	 * pair given.
	 */
	public Proj03_BSTNode(K key, V value)
	{
		if (value == null)
			throw new IllegalArgumentException("BSTNode: The 'value' parameter was null");

		this.key   = key;
		this.value = value;

		this.count = 1;

		// the left,right nodes default to 'null' (Java defaults)
	}
}

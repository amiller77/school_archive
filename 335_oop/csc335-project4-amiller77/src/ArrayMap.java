import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * ARRAY MAP
 * stores dictionary information in an array information structure
 * @author ale
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public class ArrayMap<K,V> extends AbstractMap<K,V> {
	
	// INSTANCE VARIABLES
	private Object[] keys;
	private Object[] values;
	private ArrayMapSet entrySet;
	
	/**
	 * CONSTRUCTOR
	 * initializes default values for instance variables
	 */
	public ArrayMap() {
		this.keys = new Object[8];
		this.values = new Object[8];
		this.entrySet = new ArrayMapSet<Entry<K,V>>();
	}
	
	// *** OVERRIDDEN METHODS ***
	/**
	 * PUT
	 * puts a key and value pair into the dictionary
	 */
	@Override
	public V put(K key, V value) {
		// iterate over keys to find key
		for (int k = 0; k<this.keys.length; k++) {
			// if no key found, add to end
			if (this.keys[k]==null) {
				this.keys[k]=key;
				this.values[k]=value;
				this.entrySet.add(new SimpleEntry(key,value));
				return value;
			} else if (key.equals((K)this.keys[k])){
				this.values[k]=value;
				this.entrySet.add(new SimpleEntry(key,value));
				return value;
			}
		}
		// if array saturated, grow array first, then add
		int nextIndex = this.keys.length;
		growArrays();
		this.keys[nextIndex]=key;
		this.values[nextIndex]=value;
		this.entrySet.add(new SimpleEntry(key,value));
		return value;
	}
	
	/**
	 * GET
	 * @return val, value for the key
	 */
	@Override
	public V get(Object key) {
		for (int i = 0; i<this.keys.length;i++) {
			if (this.keys[i].equals(key)) {
				return (V)this.values[i];
			}
		}
		return null;
	}
	
	/**
	 * SIZE
	 * @return int, size of map
	 */
	@Override
	public int size() {
		return this.entrySet.size();
	}
	
	/**
	 * ENTRY SET
	 * @return the entry set for the map
	 */
	@Override
	public Set<Entry<K,V>> entrySet() {
		return this.entrySet.entrySet();
	}
	
	// **** INTERNAL MECHANISM ****
	/**
	 * GROW ARRAYS
	 * doubles size of the key and value arrays
	 */
	private void growArrays() {
		this.keys = doubleArraySize(this.keys);
		this.values= doubleArraySize(this.values);
	}
	
	/**
	 * DOUBLE ARRAY SIZE 
	 * doubles the size of arrays; used a static version, so the set could make use of it as well
	 * @param array , array to double
	 * @return array , the doubled array
	 */
	private static Object[] doubleArraySize(Object[] array) {
		Object[] newArray = new Object[2*array.length];
		for (int i = 0; i<array.length; i++) {
			newArray[i]=array[i];
		}
		return newArray;
	}
	
	
	// ****** BEGIN: INSIDE CLASSES ******
	/**
	 * ARRAY MAP SET
	 * contains entry set for the array map
	 * @author ale
	 *
	 * @param <T> the type of data to be held by array map set
	 */
	private class ArrayMapSet<T> extends AbstractSet<Entry<K,V>> {
		
		// INSTANCE VARIABLES
		private Iterator<Entry<K,V>> iterator;
		private Object[] data;
		
		/**
		 * CONSTRUCTOR
		 * initializes instance variables
		 */
		public ArrayMapSet() {
			this.iterator= new ArrayMapIterator<Entry<K,V>>(this);
			this.data= new Object[8];
		}
		
		// *** OVERRIDDEN METHODS ***
		/**
		 * SIZE
		 * @return int , size of the map
		 */
		@Override
		public int size() {
			int k = 0;
			for (int i=0;i<this.data.length;i++) {
				if (this.data[i]==null) {
					return k;
				} else {
					k++;
				}
			}
			return this.data.length;
		}
		
		/**
		 * CONTAINS
		 * sees if set contains the object in question
		 * @param Object o, the item to check against
		 * @return true if contains, else false
		 */
		@Override
		public boolean contains(Object o) {
			for (int i = 0; i<this.data.length;i++) {
				if (this.data[i].equals(o)) {
					return true;
				}
			}
			return false;
		}
		
		/**
		 * ITERATOR
		 * @return returns the iterator for the set
		 */
		@Override
		public Iterator<Entry<K,V>> iterator() {
			return this.iterator;
		}
		
		/**
		 * ADD
		 * @param Entry<K,V> entry, the thing to add
		 * @return true if add successful, else false
		 */
		@Override
		public boolean add(Entry<K,V> entry) {
			for (int i = 0; i<this.data.length; i++) {
				if (entry.equals((Entry<K,V>)data[i])) {
					return false;
				} else if(this.data[i]==null) {
					this.data[i]=entry;
					return true;
				}
			}
			int nextIndex = this.data.length;
			growArray();
			this.data[nextIndex]=entry;
			return true;
		}
		
		/**
		 * TO STRING
		 * converts the set to a string
		 * @return the string representation
		 */
		@Override
		public String toString() {
			String printString ="[";
			for (int i = 0; i<this.data.length;i++) {
				printString+=this.data[i];
				if (i<this.data.length-1) {
					printString+=", ";
				}
			}
			printString+="]";
			return printString;
		}
		
		// EXTERNAL INTERFACE:
		/**
		 * ENTRY SET
		 * returns a version of the entry set useable by the abstract class and external use aside
		 * @return outputset, the entrySet for purposes of external use
		 */
		public Set<Entry<K,V>> entrySet() {
			Set<Entry<K,V>> outputSet = new HashSet<Entry<K,V>>();
			for (int i = 0; i<this.size();i++) {
				outputSet.add((Entry<K,V>)this.data[i]);
			}
			return outputSet;
		}
		
		// INTERNAL MECHANICS:
		/**
		 * GROW ARRAY
		 * doubles size of data array
		 */
		private void growArray() {
			this.data=ArrayMap.doubleArraySize(this.data);
		}
	}
	
	// ***** INSIDE CLASS ******
	// ITERATOR CLASS
	private class ArrayMapIterator<T> implements Iterator<T> {
		
		// INSTANCE VARIABLES
		private ArrayMapSet parentSet;
		private int accumulator;
		
		/**
		 * CONSTRUCTOR
		 * initializes default vals
		 * @param parentSet, the set to which the iterator belongs
		 */
		public ArrayMapIterator(ArrayMapSet parentSet) {
			this.parentSet=parentSet;
			this.accumulator=0;
		}
		
		// *** OVERRIDDEN METHODS ***
		/**
		 * HAS NEXT
		 * @return returns true if next element non-null; else false
		 */
		@Override
		public boolean hasNext() {
			if (accumulator+1 >= parentSet.data.length || parentSet.data[accumulator+1] == null) {
				accumulator=0;
				return false;
			} else {
				return true;
			}
		}
		
		/**
		 * NEXT 
		 * @return next, next element if applicable, else null
		 */
		@Override
		public T next() {
			if (accumulator+1 >= parentSet.data.length || parentSet.data[accumulator+1] == null) {
				accumulator=0;
				return null;
			} else {
				accumulator++;
				return (T) parentSet.data[accumulator+1];
			}
		}
		
	}
	
}

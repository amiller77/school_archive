package model;

/**
 * ROOM DATA STRUCTURE
 * if we wanted to change out the data structure used to store the availability of rooms for a particular day
 * 		then we would want that data structure to follow certain basic functionality defined in this interface
 * Although we are only storing room numbers right now, we might ultimately want to associate other data with each room
 * 		hence the key-value structure
 * @author Alexander Miller
 */

public interface RoomDataStructure<K extends Comparable<K>, V> {
	
	public int getSize();
	public void insert(K key, V value);
	public V get(K key);
	public void remove(K key);
	// requires the caller to instantiate the array to fill, then pass as parameter:
	public void inOrderTraversal(K[] keysOut);
}

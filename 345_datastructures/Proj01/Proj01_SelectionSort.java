/*
 * Author: Alexander Miller
 * Class: 345
 * Assignment: Project 1
 * File: Proj01_SelectionSort
 */

public class Proj01_SelectionSort implements Proj01_Sort{
	
	//VARIABLES
	private boolean debug;
	private int sortedEnd; //first index which is unsorted
	private int minValue;
	Comparable buffer;
	
	
	// CONSTRUCTOR
	public Proj01_SelectionSort(boolean debug) {
		this.debug = debug;
		this.sortedEnd=0;
		this.minValue=0;
	}
	
	// SORT, IMPLEMENTS PROJ01_SORT
	public void sort(Comparable[] arr) {
		this.sortedEnd=0;
		// stop when the entire list is sorted:
		while (sortedEnd <= arr.length-1) {
			//DEBUG 
			if (this.debug) {
				System.out.println("*Resulting Array*");
				Proj01_BubbleSort.printArray(arr);
			}
			// default min value index:
			this.minValue = sortedEnd;
			// iterate over unsorted part:
			for (int i = sortedEnd; i<arr.length; i++) {
				// if the current item is less than the current min, update current min
				if (arr[i].compareTo(arr[this.minValue])< 0) {
					this.minValue=i;
				}
			}
			// DEBUG
			if (this.debug) {
				System.out.println("Min Value: "+arr[this.minValue]);
				System.out.println("First unsorted value to swap: "+arr[this.sortedEnd]);
			}
			// take the min value, and swap with the sortedEnd
			this.buffer = arr[this.sortedEnd];
			arr[this.sortedEnd] = arr[this.minValue];
			arr[this.minValue]=this.buffer;	
			// iterate sortedEnd
			this.sortedEnd++;
		}
	}
}

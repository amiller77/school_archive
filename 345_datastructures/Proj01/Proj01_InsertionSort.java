/*
 * Author: Alexander Miller
 * Class: 345
 * Assignment: Project 1
 * File: Proj01_InsertionSort
 */

public class Proj01_InsertionSort implements Proj01_Sort{

	//VARIABLES
	private int sortedEnd; // 1st index which is unsorted
	private Comparable buffer;
	private boolean debug;
	
	
	//CONSTRUCTOR
	public Proj01_InsertionSort(boolean debug) {
		this.sortedEnd = 1;
		this.debug = debug;
	}
	
	
	// SORT, IMPLEMENTS PROJ01_SORT
	public void sort(Comparable[] arr) {
		int startingIndex=0;
		int endingIndex=arr.length-1;
		sort(startingIndex, endingIndex, arr);
	}
	
	public void sort(int startingIndex, int endingIndex, Comparable[] arr) {
		this.sortedEnd=startingIndex+1; 
		// sort the whole array:
		while (sortedEnd <= endingIndex) {
			// DEBUG
			if (debug) {
				System.out.println("^^^");
				Proj01_BubbleSort.printArray(arr);
			}
			// pop the first unsorted element:
			this.buffer = arr[this.sortedEnd];
			// iterate backwards thru sorted part until you hit the beginning:
			for (int i=sortedEnd-1; i>=startingIndex; i--) {
				// DEBUG
				if (debug) {
					System.out.println("Comparing "+arr[i]+" with "+this.buffer);
				}
				// basecase: we bottomed out with a low val:
				if (i==startingIndex && arr[startingIndex].compareTo(this.buffer)>=0) {
					arr[startingIndex+1]=arr[startingIndex];
					arr[startingIndex]=this.buffer;	
					break;
				}
				// if the sorted item is bigger than the popped item:
				if (arr[i].compareTo(this.buffer)>0) {
					// keep slidin'
					arr[i+1] = arr[i];
					// DEBUG
					if (this.debug) {
						System.out.println("Slide occured-");
						Proj01_BubbleSort.printArray(arr);
						continue;
					}
				} else {
					// otherwise, put the buffer val there, and quit
					// this keeps it to 1 comparison, if the popped item is larger than the sorted stuff
					arr[i+1] = this.buffer;
					break;
				}
			}
			this.sortedEnd++;
		}
	}
	
	
}

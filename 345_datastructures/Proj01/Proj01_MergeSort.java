/*
 * Author: Alexander Miller
 * Class: 345
 * Assignment: Project 1
 * File: Proj01_MergeSort
 */

public class Proj01_MergeSort implements Proj01_Sort{

	// VARIABLES
	boolean debug;
	int baseLen;
	
	// CONSTRUCTOR
	public Proj01_MergeSort (boolean debug, int baseLen) {
		this.debug=debug;
		this.baseLen = baseLen;
	}
	
	// SORT, IMPLEMENTS PROJ01_SORT
	public void sort(Comparable[] arr) {
		recursiveSort(0,arr.length-1,arr);
	}
	
	// RECURSIVE SORT
	public void recursiveSort(int startingIndex, int endingIndex, Comparable[] arr) {
		// DEBUG
		if (this.debug) {
			System.out.println("^^^");;
			System.out.println("Starting Index: "+startingIndex);
			System.out.println("Ending Index: "+endingIndex);
		}
		// base case:
		int arrayLength = endingIndex-startingIndex+1;
		if ( arrayLength <= this.baseLen) {
			// DEBUG
			if (this.debug) {
				System.out.println("Base case length : "+arrayLength);
				System.out.println("Baselen: "+this.baseLen);
				System.out.println("*Array Prior to Sort*");
				Proj01_BubbleSort.printArray(arr);
			}
			Proj01_InsertionSort n2Algorithm = new Proj01_InsertionSort(false);
			n2Algorithm.sort(startingIndex, endingIndex, arr);
			// DEBUG
			if (this.debug) {
				System.out.println("*Array Post Sort*");
				Proj01_BubbleSort.printArray(arr);
			}
			return;
		}
		// split the data set
		int midPoint = startingIndex+(arrayLength/2);
		// recursive call on both halves:
		recursiveSort(startingIndex,midPoint,arr);
		recursiveSort(midPoint+1,endingIndex,arr);
		// merge the two sorted halves in the buffer::
		Comparable[] buffer = new Comparable[arrayLength];
		int i = startingIndex;
		int j = midPoint+1;
		int k =0;
		// DEBUG
		if (this.debug) {
			System.out.println("...");
			System.out.println("Commencing Merge...");
		}
		while (k < buffer.length) {
			// avoid i overtaking original j range:
			if (i==midPoint+1) {
				// DEBUG
				if (this.debug) {
					System.out.println("i saturates");
				}
				buffer[k]=arr[j];
				k++;
				j++;
				continue;
			}
			// avoid j overtaking the endvalue:
			if (j==endingIndex+1) {
				// DEBUG
				if (this.debug) {
					System.out.println("j saturates");
				}
				buffer[k]=arr[i];
				k++;
				i++;
				continue;
			}
			// DEBUG
			if (this.debug) {
				System.out.println("Comparing: "+arr[i]+" with "+arr[j]);
			}
			if (arr[i].compareTo(arr[j])<=0) {
				buffer[k] = arr[i];
				// DEBUG
				if (this.debug) {
					System.out.println("Choose: "+arr[i]);
				}
				i++;
			} else {
				buffer[k] = arr[j];
				// DEBUG
				if (this.debug) {
					System.out.println("Choose: "+arr[j]);
				}
				j++;
			}
			k++;
		}
		// DEBUG
		if (this.debug) {
			System.out.println("Merging Buffer: ");
			Proj01_BubbleSort.printArray(buffer);
			System.out.println("*Array before Merge*");
			Proj01_BubbleSort.printArray(arr);
		}
		// paste the buffer back:
		int y = startingIndex;
		for (int z = 0; z<buffer.length; z++) {
			arr[y]=buffer[z];
			y++;
		}
		// DEBUG
		if (this.debug) {
			System.out.println("*Array after Merge*");
			Proj01_BubbleSort.printArray(arr);
		}
	}
}

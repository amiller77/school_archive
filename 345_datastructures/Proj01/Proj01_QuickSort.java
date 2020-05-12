/*
 * Author: Alexander Miller
 * Class: 345
 * Assignment: Project 1
 * File: Proj01_QuickSort
 */

public class Proj01_QuickSort implements Proj01_Sort{
	
	// VARIABLES
	private int baseLen;
	private int mode;
	private boolean debug;
	private Comparable buffer;
	
	// CONSTRUCTOR
	public Proj01_QuickSort(boolean debug, int mode, int baseLen) {
		this.debug=debug;
		this.mode=mode;
		this.baseLen=baseLen;
	}
	
	// SORT, IMPLEMENTS PROJ01_SORT
	public void sort(Comparable[] arr) {
		// supply the starting parameters to the recursive sort alg.
		recursiveSort(0,arr.length-1,arr);
	}

	// RECURSIVE SORT
	private void recursiveSort(int startingIndex, int endingIndex, Comparable[] arr) {
		// DEBUG
		if (this.debug) {
			System.out.println("^^^");
			System.out.println("Starting Index: "+startingIndex);
			System.out.println("Ending Index: "+endingIndex);
			System.out.println("*Array prior to further partitioning*");
			Proj01_BubbleSort.printArray(arr);
		}
		// base case conditional to switch to O(n2) algorithm
		int arrayLength = endingIndex-startingIndex+1;
		if(arrayLength <= baseLen) {
			// DEBUG
			if (this.debug) {
				System.out.println("");
				System.out.println("Base case length : "+arrayLength);
				System.out.println("Baselen: "+this.baseLen);
			}
			Proj01_InsertionSort n2Algorithm = new Proj01_InsertionSort(false);
			n2Algorithm.sort(startingIndex, endingIndex, arr);
			return;
		}
		// otherwise, find pivot: 
		Comparable first = arr[startingIndex];
		Comparable second = arr[startingIndex+(arrayLength/2)];
		Comparable third = arr[endingIndex];
		Comparable pivot= findPivot(first, second, third);
		// get pivot index:
		int pivotIndex = startingIndex; // default value
		if (pivot.compareTo(second)==0) {
			pivotIndex=startingIndex+(arrayLength/2);
		} else if (pivot.compareTo(third)==0) {
			pivotIndex=endingIndex;
		}
		// DEBUG
		if (this.debug) {
			System.out.println("Mode: "+this.mode);
			System.out.println("Pivot Index: "+pivotIndex);
		}
		// pop the pivot, put in starting position
		this.buffer = arr[pivotIndex];
		arr[pivotIndex]=arr[startingIndex];
		arr[startingIndex]=this.buffer;
		// DEBUG
		if (this.debug) {
			System.out.println("*Array with Pivot at Beginning*");
			Proj01_BubbleSort.printArray(arr);
		}
		// default iterators:
		int i = startingIndex+1;
		int j = endingIndex;
		// while the lower and higher arrays aren't overlapping:
		while (j-i >=1) {
			// iterate thru lower array until a higher value pops up
			while (arr[i].compareTo(pivot)<=0) {
				// prevent out of bounds:
				if (i==j) {
					// DEBUG 
					if (this.debug) {
						System.out.println("i met j - break loop");
					}
					break;
				}
				// DEBUG
				if (this.debug) {
					System.out.println("Comparing pivot with: "+arr[i]);
				}
				i++;
			} 
			// iterate thru higher array until a lower value pops up
			while (arr[j].compareTo(pivot)>=0) {
				// prevent out of bounds: 
				if (j==i) {
					// DEBUG 
					if (this.debug) {
						System.out.println("i met j - break loop");
					}
					break;
				}
				// DEBUG
				if (this.debug) {
					System.out.println("Comparing pivot with: "+arr[j]);
				}
				j--;
			}
			// swap the i and the j element, but only if the swap is valid
			// this helps to validate some edge cases
			if ((arr[i].compareTo(pivot)<=0 && arr[j].compareTo(pivot)>=0)
					|| (arr[j].compareTo(pivot)<=0 && arr[i].compareTo(pivot)>=0)) {
				// DEBUG
				if (this.debug) {
					System.out.println("Swapping "+arr[i]+" with "+arr[j]);
				}
				this.buffer = arr[j];
				arr[j]=arr[i];
				arr[i] = this.buffer;
				i++;
				j--;
			}
		}
		// double check that the i spot is lower than pivot, and didn't overlap w/ j
		if (arr[i].compareTo(pivot)>0) {
			i--;
		}
		// swap the pivot back into position
		this.buffer = arr[i];
		arr[i]=arr[startingIndex];
		arr[startingIndex]=this.buffer;
		//DEBUG
		if (this.debug) {
			System.out.println("*Array after Partitioning*");
			Proj01_BubbleSort.printArray(arr);
		}
		// recursive call, after validating that array sizes are larger than 1:
		if (i-1 != startingIndex) {
			recursiveSort(startingIndex,i-1,arr);
		}
		if (i+1 != endingIndex) {
			recursiveSort(i+1,endingIndex,arr);
		}
	}
	
	// FIND PIVOT
	public Comparable findPivot(Comparable first, Comparable second, Comparable third) {
		if (this.mode == 0) {
			return first;
		} else if (this.mode == 1) {
			return second;
		} else { //this.mode == 2
			// if there's a duplicate entry, then we can just return one of the duplicates:
			if (first.compareTo(second)==0 || first.compareTo(third)==0) {
				return first; // first is a duplicate
			} else if (second.compareTo(third)==0) {
				return second; // second is a duplicate
			}
			// if there are no duplicates, we'll choose the one that's neither max nor min
			// create a basket of our inputs to iterate over:
			Comparable[] inputArray= {first, second, third};
			// step 1: find the minimum
			Comparable minimum = first;
			for (int i = 0; i<3; i++) {
				if (inputArray[i].compareTo(minimum)<0) {
					minimum=inputArray[i];
				}
			}
			// step 2: find the maximum
			Comparable maximum = minimum;
			for (int i = 0; i<3; i++) {
				if (inputArray[i].compareTo(maximum)>0) {
					maximum = inputArray[i];
				}
			}
			// step 3: return the one that's neither
			for (int i = 0; i<3; i++) {
				if (inputArray[i].compareTo(minimum)!=0 && inputArray[i].compareTo(maximum)!=0) {
					return inputArray[i];
				}
			}
		}
		return null; //dead code for compiler to be happy
	}
	
}

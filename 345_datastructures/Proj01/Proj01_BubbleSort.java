/*
 * Author: Alexander Miller
 * Class: 345
 * Assignment: Project 1
 * File: Proj01_BubbleSort
 */

public class Proj01_BubbleSort implements Proj01_Sort{

	// VARIABLES
	private int i;
	private int j;
	private int numSwaps;
	private boolean debug;
	private boolean loop;
	private Comparable buffer;
	
	// CONSTRUCTOR
	public Proj01_BubbleSort(boolean debug) {
		reinitializeVariables();
		this.debug = debug;
		this.loop = true;
	}
	
	// SORT, IMPLEMENTING PROJ01_SORT
	public void sort(Comparable[] arr) {
		// keep going until everything has been sorted:
		while (this.loop==true) {
			reinitializeVariables();
			// DEBUG
			if (this.debug) {
				System.out.println("^^^");
				Proj01_BubbleSort.printArray(arr);
			}
			// iterate over the entire array:
			while (this.i<arr.length-1) {
				// if the previous element is greater than the latter:
				if (arr[i].compareTo(arr[j])>0) {
					// DEBUG
					if (this.debug) {
						System.out.println("Swapping "+arr[i]+" at "+i+" with "+arr[j]+" at "+j);
					}
					// swap the vals
					this.buffer = arr[i];
					arr[i]=arr[j];
					arr[j]=this.buffer;
					// DEBUG
					if (this.debug) {
						System.out.println("Result: "+arr[i]+" "+arr[j]);
						System.out.println("*Resulting Array*");
						Proj01_BubbleSort.printArray(arr);
						System.out.println("...");
					}
					this.numSwaps++;
				}
				iterateCounters();
			}
			// if we had no swaps over entire array, end:
			if (this.numSwaps==0) {
				this.loop=false;
			}
		}
	}
	
	//REINITIALIZE VARIABLES
	private void reinitializeVariables() {
		this.i = 0;
		this.j = 1;
		this.numSwaps=0;
	}
	
	//ITERATE VARIABLES
	private void iterateCounters() {
		this.i++;
		this.j++;
	}
	
	//PRINT ARRAY
	public static void printArray(Comparable[] arr) {
		System.out.println("Array: ");
		for (int i = 0; i<arr.length; i++) {
			System.out.print(arr[i]+" ");
		}
		System.out.println("");
	}
	
}

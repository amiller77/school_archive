/*
* File: count.c
* Author: Alexander Miller
* Description: counts input integers, and prints result in asc. order by input
* Input: sequence of integers from stdin
*       The first value, N > 0, is an integer specifying
*           number of ints to be processed
* Output: print out the number of times each distinct integer appears
*       These are printed out in ascending order of the input values
*       Print format: printf("%d %d\n", input_value, count);
* Error cases:
*       * cannot read at least one integer from stdin
*       * N is not positive
*       * number of ints to read is actually fewer than N
*           note: not an error if amount supplied is greater than N, just ignore extras
*/

# include <stdio.h>
# include <stdlib.h>

/*
* SWAP
* Description: swaps array elements at positions a, b
*/
void swap(int data[], int a, int b) {
    int buffer = data[a];
    data[a] = data[b];
    data[b] = buffer;
}

/*
* PARTITION SORT
* Description: sorts an array "in place" by using the QuickSort algorithm
* Inputs: a: starting index, z: last index of array, data: array to sort
*/
void partitionSort(int data[], int a, int z) {
    int lenArray = z+1-a;
    // base case: last index = first index
    if (lenArray <= 1) {
        return;
    }
    int firstVal = data[a];
    // middle of array is at starting point + length/2
    int medVal = data[a+lenArray/2];
    int lastVal = data[z];
    // find median value and location
    int median;
    int medianIndex;
    if ( (lastVal >= firstVal && lastVal <= medVal)
    || (lastVal <= firstVal && lastVal >= medVal) ) {
        median = lastVal;
        medianIndex = z;
    } else if ( (medVal >= firstVal && medVal <= lastVal)
    || (medVal <= firstVal && medVal >= lastVal) ) {
        median = medVal;
        medianIndex = a+lenArray/2;
    } else if ( (firstVal >= medVal && firstVal <= lastVal)
    || (firstVal <= medVal && firstVal >= lastVal) ) {
        median = firstVal;
        medianIndex = a;
    } // this block isn't possible, but it makes the compiler happy
    else {
        median = medVal;
        medianIndex = a+lenArray/2;
    }
    // move median value to first position:
    swap(data,a,medianIndex);
    /*
    * scan up from the left, creating a "less than" partition
    * scan down from the right, creating a "greater than" partition
    */
    int tail = z;
    int head = a+1;
    while (head < tail) {
        if (data[head] <= median) {
            head++;
            continue;
        }
        // case where we can swap
        if (data[tail] <= median) {
            swap(data,head,tail);
            head++;
            tail--;
            continue;
        }
        // case where we can't swap, move tail down
        tail--;
    }
    // perform some adjustments:
    if (data[head] > median) {
        head--;
    }

    // now head == tail, so
    // now that the data is partitioned, swap head, median (now in a)
    swap(data,a,head);
    // now head holds the median value, which is in its final location
    // now call parition sort on each partition
    partitionSort(data,a,head-1);
    partitionSort(data,head+1,z);
}

/*
* COUNT
* Description: iterates over input and makes a count of values
* Inputs: inputData: data to count, countData: stores counts,
*   valuesFound: records which ones we've already seen (prevent multiple counts for same value),
*   n: size of arrays
* Pre-conditions: countData values initialized to 0 (done with calloc),
*   valuesFound has non-initialized values
*/
void count(int inputData[], int countData[], int valuesFound[], int n) {
    int i;
    int valsFoundSoFar = 0;
    // iterate over input values
    for (i=0;i<n;i++){
        int inputVal = inputData[i];
        int duplicateFound = 0;
        int v;
        // iterate over values found so far
        for (v=0;v<valsFoundSoFar;v++) {
            // if we've found the value already, increment count
            if (valuesFound[v] == inputVal) {
                countData[v]++;
                duplicateFound = 1;
                break;
            }
        }
        // if no duplicate found, add a new entry to valuesFound
        if (duplicateFound == 0) {
            valuesFound[valsFoundSoFar]=inputVal;
            // also give a count of one to this value
            countData[valsFoundSoFar] = 1;
            valsFoundSoFar++;
        }
    }
}

/*
* GENERATE OUTPUT
* prints the values and their counts, separated by a space, to each line
* Inputs: valuesFound: contains unique values we are counting, countData: contains resp. counts
*       n: array size
*/
void generateOutput(int countData[], int valuesFound[], int n) {
    int i;
    for (i=0;i<n;i++) {
        // only report on values we have count data for
        if (countData[i]==0) {
            break;
        }
        printf("%d %d\n",valuesFound[i],countData[i]);
    }
}

/*
* MAIN
* gathers N and inputs from user, allocates memory, handles error conditions
* sorts the inputs by calling partitionSort(), counts the values by calling count(),
* generates the output by calling generateOutput()
*/
int main() {
    int N = 0;
    int terminationCondition = 0;

    // gather N
    int result = scanf("%d",&N);
    if (result == 0 || result == -1) {
        fprintf(stderr,"Error: couldn't read in a value for N\n");
        return(1);
    }

    // validate N
    if (N <= 0) {
        fprintf(stderr,"Error: size is not positive number\n");
        return(1);
    }

    // create arrays the size of N
    int *inputData;
    int *countData;
    int *valuesFound;
    inputData = malloc(N*sizeof(int));
    countData = calloc(N,sizeof(int));
    valuesFound = malloc(N*sizeof(int));
    if (inputData == NULL || countData == NULL || valuesFound == NULL) {
        fprintf(stderr,"Error: out of memory!\n");
        return(1);
    }

    // main input loop:
    int i = 0;
    for (i = 0; i<N; i++) {
        int val;
        result = scanf("%d",&val);
        if (result == 0) {
            fprintf(stderr,"Error: please enter integers\n");
            return(1);
        }
        if (result == -1) {
            fprintf(stderr,"Error: fewer integers provided than indicated\n");
            return(1);
        }
        inputData[i] = val;
    }

    // sort the input data:
    partitionSort(inputData,0,N-1);
    // count the values:
    count(inputData,countData,valuesFound,N);
    // generate output:
    generateOutput(countData,valuesFound,N);
    return terminationCondition;
}

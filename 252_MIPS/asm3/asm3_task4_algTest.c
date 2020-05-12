// asm3_task4_algTest
// len: length of integer array

# include <stdio.h>

int longestSorted(int *array, int len) {
    if (len == 0 || len == 1) {
        return len;
    }
    int longestFound = 1;
    int thisRunLength = 1;
    int previousElement = *array;       // prevElem = array[0];
    int i;
    for (i=1; i<len; i++) {
        int thisElement = *(array+i);   //will need to add (sizeof int)*i for asm
        // is this element ascending relative to last?
        if (thisElement >= previousElement) {
            thisRunLength++;
            // if last element of array, potent. need to update longest found
            if (i == len - 1 && thisRunLength > longestFound) {
                longestFound = thisRunLength;
            }
        } // if not, this run is over
        else {
            if (thisRunLength > longestFound) {
                longestFound = thisRunLength;
            }
            thisRunLength = 1;
        }
        previousElement = thisElement;
    }
    return longestFound;
}

int main() {
    int flag = 1;
    int buffer[10];
    int i = 0;
    while (flag) {
        if (i>=10) {
            break;
        }
        int input = 0;
        int result = scanf("%d",&input);
        if (result == 0 || result == -1) {
            break;
        }
        buffer[i] = input;
        i++;
    }
    int k;
    for (k=0;k<i;k++) {
        printf("debug: %d\n",buffer[k]);
    }
    int longestRun = longestSorted(buffer,i);
    printf("%d\n",longestRun);
}

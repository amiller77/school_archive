/*
* debug.c
*/
# include <stdio.h>
# include <stdlib.h>

typedef struct Turtle {
    int x;
} Turtle;

void turtle_sortByX_indirect(Turtle** arr, int arrLen) {
    Turtle** sortingHead = arr;
    Turtle** upperBound = arr + arrLen - 1; // [in asm] arr + arrlen*4
    // iterate until we have sorted entire array
    while (sortingHead <= upperBound) {
        // search rest of array for min
        Turtle** comp = sortingHead;
        Turtle** minPointer = comp;
        int minVal = (*comp)->x;
        while (comp <= upperBound) {
            if ((*comp)->x < minVal) {
                minVal = (*comp)->x;
                minPointer = comp;
            }
            comp++; // [in asm] comp + 4
        }
        // swap minPointer and sortingHead if smaller val found
        if (minVal < (*sortingHead)->x) {
            Turtle* buffer = *sortingHead;
            *sortingHead = *minPointer;
            *minPointer = buffer;
        }
        sortingHead++; // [in asm] SH + 4
    }
}

int main() {
    Turtle* arr[6];
    int i;
    for (i=0;i<6;i++) {
        arr[i] = malloc(sizeof(Turtle));
        if (i==0) {
            (arr[i])->x = 8;
        } else if (i==1) {
            (arr[i])->x = -5;
        } else if (i==2) {
            (arr[i])->x = 10;
        } else if (i==3) {
            (arr[i])->x = 1;
        } else if (i==4) {
            (arr[i])->x = 0;
        } else if (i==5) {
            (arr[i])->x = 4;
        }
    }
    printf("sizeof turtle: %d\n",sizeof(Turtle));
    printf("sizeof turtle*: %d\n",sizeof(Turtle*));
    printf("\n");

    printf("arr b4 sort...\n");
    for (i=0;i<6;i++) {
        printf("%lu -> %lu %d\n",&(arr[i]),arr[i],(arr[i])->x);
        fflush(stdout);
    }

    turtle_sortByX_indirect(&(arr[0]),6);

    printf("arr after sort...\n");
    for (i=0;i<6;i++) {
        printf("%lu -> %lu %d\n",&(arr[i]),arr[i],(arr[i])->x);
        fflush(stdout);
    }
    return 0;
}

// assg5_debugger.c

# include <stdio.h>
# include <stdlib.h>

/*
* FIND STRING LENGTH
* returns length of a string
*/
int findStringLength(char *str) {
    char *thisChar = str;
    int count = 0;
    while (*thisChar != '\0') {
        count++;
        thisChar++;
    }
    return count;
}

/*
* TO LOWER CASE
* Parameters: string and string length
* Returns: a newly-allocated string which is the lower case version
*   returns NULL if there is a non-alphabetic character inside
*/
char* toLowerCase(char *str, int strLen) {
    char *newStr = NULL;
    newStr = malloc(sizeof(char)*(strLen+1));
    if (newStr == NULL) {
        fprintf(stderr,"Error: out of memory!\n");
        exit(1);
    }
    int i;
    for (i=0; i<strLen; i++) {
        char thisChar = *(str+i);
        char *destination = newStr+i;
        if (thisChar >= 65 && thisChar <= 90) {
            *destination = thisChar + 32;
        } else if (thisChar >= 97 && thisChar <= 122) {
            *destination = thisChar;
        } else {
            free(newStr);
            return NULL;
        }
    }
    *(newStr+strLen)='\0';
    return newStr;
}

int main() {
    char i[10];
    scanf("%s",i);
    int strLen = findStringLength(i);
    printf("%s\n",toLowerCase(i,strLen));
    return 0;
}

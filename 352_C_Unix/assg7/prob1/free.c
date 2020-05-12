/*
* free.c
* test some free functionality
*/

#include <stdlib.h>
#include <stdio.h>
#include <string.h>

typedef struct Node {
    char *name;
} Node;


int main() {
    Node *node = malloc(sizeof(Node));
    char *word = "johnny";
    node->name = strdup(word);
    free(node);
}

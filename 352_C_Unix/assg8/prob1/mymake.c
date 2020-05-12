/*
* File: mymake.c
* Author: Alexander Miller
* Description: contains main() function for the mymake project
* Fatal Errors:
*   an input makefile or target is not specified
*   too many arguments
*   input makefile cannot be opened
*   input makefile in illegal format
*   specified target not defined in input makefile
* Input:
*   words no longer than 64
*/

#include "./parseInput.h"
#include "./graph.h"

int main(int argc, char* argv[]) {
    validateNumArguments(argc);
    Node* root = processMakefile(argv[1]);
    Node* target = getNode(root,argv[2]);
    if (target == NULL || target->isTarget == 0) {
        fprintf(stderr,"Error: Requested target not specified as a target in makefile\n");
        freeGraph(root);
        return 1;
    }
    postOrderTraversal(target);
    freeGraph(root);
    return 0;
}

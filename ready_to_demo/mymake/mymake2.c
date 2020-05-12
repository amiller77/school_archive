/*
* File: mymake2.c
* Author: Alexander Miller
* Description: contains main() function for the mymake project
* Invocations:
*   mymake2                 // tries myMakefile as filename, first target
*   mymake2 -f makeFile     // tries first target
*   mymake2 target          // tries myMakefile as filename
*   mymake2 -f makeFile target
*   mymake2 target -f makeFile
* Input:
*   words no longer than 64
*/

#include "./parseInput.h"
#include "./graph.h"

int main(int argc, char* argv[]) {
    // extract target, makefile from command arguments:
    char* fileName = NULL;
    char* targetString = NULL;
    extractArguments(argc, argv,&fileName,&targetString);

    // process the make file:
    Node* root = processMakefile(fileName);

    // get the target node, validate it:
    Node* target;
    // if no target was provided in invocation, use root of graph
    if (targetString==NULL) {
        target = root;
    } else {
        target = getNode(root,targetString);
    }
    // if we couldn't find a target,
    // or the target exists but wasn't specified as target, gotta problem
    if (target == NULL || target->isTarget == 0) {
        fprintf(stderr,"Error: Requested target not specified as a target in makefile\n");
        freeGraph(root);
        return 1;
    }

    // run the graph traversal
    int buildPerformed = 0;
    int POT = postOrderTraversal(target,&buildPerformed);

    // if no build performed, but no errors encountered, target up to date
    if (target->build == 0 && POT == 0) {
        printf("%s is up to date.\n",target->name);
    }
    // if build required, but no commands actually executed at any point,
    //      then target "up to date"
    if (target->build == 1 && POT == 0 && buildPerformed == 0) {
        printf("%s is up to date.\n",target->name);
    }
    freeGraph(root);
    free(fileName);
    free(targetString);
    return POT;
}

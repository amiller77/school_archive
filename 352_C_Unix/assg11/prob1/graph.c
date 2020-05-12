/*
* File: graph.c
* Author: Alexander Miller
* Description: handles graph-related utility functions for bacon.c
*
*/

#include "graph.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>


// **************************** CONSTRUCTORS ****************************
/*
* EDGE CONSTRUCTOR
* mallocs and returns an Edge
*   sets nextSequentialEdge to NULL
*   mallocs a deep copy of "movie"
*/
Edge* edgeConstructor(Actor* from, Actor* to, char* movie) {
    Edge* thisEdge = malloc(sizeof(Edge));
    memoryCheck(thisEdge);
    thisEdge->from = from;
    thisEdge->to = to;
    thisEdge->movie = strdup(movie);
    memoryCheck(thisEdge->movie);
    thisEdge->nextSequentialEdge = NULL;
    return thisEdge;
}

/*
* ACTOR CONSTRUCTOR
* mallocs and returns an Actor
*   mallocs a deep copy of the passed name
*   sets baconScore, mark to 0
*   initializes all other fields to NULL
*/
Actor* actorConstructor(char* name) {
    Actor* thisActor = malloc(sizeof(Actor));
    memoryCheck(thisActor);
    thisActor->name = strdup(name);
    memoryCheck(thisActor->name);
    thisActor->edges = NULL;
    thisActor->lastEdge = NULL;
    thisActor->nextSequentialActor = NULL;
    thisActor->nextQueueActor = NULL;
    thisActor->nextSecondaryQueueActor = NULL;
    thisActor->baconScore = 0;
    thisActor->mark = 0;
    return thisActor;
}

/*
* QUEUE CONSTRUCTOR
* mallocs and returns a Queue
*    initializes the queue to NULL
*/
Queue* queueConstructor() {
    Queue* thisQueue = malloc(sizeof(Queue));
    memoryCheck(thisQueue);
    thisQueue->actors=NULL;
    return thisQueue;
}

// ************************ GRAPH FUNCTIONS *********************

/*
* ADD EDGE
* creates and adds reciprocal edges to a pair of actors, for a certain movie
*/
void addEdges(Actor* A, Actor* B, char* movie) {
    // create reciprocal edges for A, B
    Edge* AEdge = edgeConstructor(A,B,movie);
    Edge* BEdge = edgeConstructor(B,A,movie);
    // add edge to A
    if (A->lastEdge == NULL) {
        A->edges = AEdge;
    } else {
        A->lastEdge->nextSequentialEdge = AEdge;
    }
    A->lastEdge = AEdge;
    // add edge to B
    if (B->lastEdge == NULL) {
        B->edges = BEdge;
    } else {
        B->lastEdge->nextSequentialEdge = BEdge;
    }
    B->lastEdge = BEdge;
}

/*
* GET ACTOR
* return actor of this name, or NULL if doesn't exist
*/
Actor* getActor(char* name, Actor* root) {
    Actor* thisActor = root;
    while (thisActor != NULL) {
        if (strcmp(thisActor->name, name)==0) {
            return thisActor;
        }
        thisActor = thisActor->nextSequentialActor;
    }
    return NULL;
}

/*
* ADD ACTOR TO GRAPH
* "adds actor to graph" in that we can keep track of actor
* precondition: root and actor both exist
*/
void addActorToGraph(Actor* root, Actor* actor) {
    Actor* thisActor = root;
    while (thisActor->nextSequentialActor != NULL) {
        thisActor = thisActor->nextSequentialActor;
    }
    thisActor->nextSequentialActor = actor;
}

/*
* CLEAR MARKS
* clears the marks for all nodes in the graph
*/
void clearMarks(Actor* root) {
    Actor* thisActor = root;
    while (thisActor != NULL) {
        thisActor->mark = 0;
        thisActor = thisActor->nextSequentialActor;
    }
}

/*
* FREE GRAPH
* goes thru actor by actor to free the graph
*/
void freeGraph(Actor* root) {
    Actor* thisActor = root;
    while (thisActor != NULL) {
        // free name
        free(thisActor->name);
        // free all its edges
        Edge* thisEdge = thisActor->edges;
        while (thisEdge != NULL) {
            free(thisEdge->movie);
            Edge* previousEdge = thisEdge;
            thisEdge = thisEdge->nextSequentialEdge;
            free(previousEdge);
        }
        // iterate, then free this actor
        Actor* previousActor = thisActor;
        thisActor = thisActor->nextSequentialActor;
        free(previousActor);
    }
}

// ******************** QUEUE FUNCTIONS *********************

/*
* QUEUE PUSH
* pushes an actor on (end of) queue
*/
void queuePush(Queue* queue, Actor* actor) {
    if (queue->actors == NULL) {
        queue->actors = actor;
        queue->lastActor = actor;
        return;
    }
    queue->lastActor->nextQueueActor = actor;
    queue->lastActor=actor;
}

/*
* QUEUE POP
* pops an actor off (top of) the queue
*/
Actor* queuePop(Queue* queue) {
    if (queue->actors == NULL) {
        return NULL;
    }
    // swap off the head
    Actor* head = queue->actors;
    queue->actors = head->nextQueueActor;
    head->nextQueueActor = NULL;
    // update lastActor if necessary [only time this changes is if we empty queue]
    if (queue->actors == NULL) {
        queue->lastActor = NULL;
    }
    return head;
}

// ******************* SECONDARY QUEUE FUNCTIONS ********************

/*
* SECONDARY QUEUE PUSH
* pushes an actor on (end of) queue
*/
void secondaryQueuePush(Queue* queue, Actor* actor) {
    if (queue->actors == NULL) {
        queue->actors = actor;
        queue->lastActor = actor;
        return;
    }
    queue->lastActor->nextSecondaryQueueActor = actor;
    queue->lastActor=actor;
}

/*
* SECONDARY QUEUE POP
* queue pop if actor simultaneously in 2 queues
*/
Actor* secondaryQueuePop(Queue* queue) {
    if (queue->actors == NULL) {
        return NULL;
    }
    // swap off the head
    Actor* head = queue->actors;
    queue->actors = head->nextSecondaryQueueActor;
    head->nextSecondaryQueueActor = NULL;
    // update lastActor if necessary [only time this changes is if we empty queue]
    if (queue->actors == NULL) {
        queue->lastActor = NULL;
    }
    return head;
}

/*
* GET SECONDARY QUEUE ELEMENT
* allows retrieval of interior queue element [for the secondary queue]
* returns NULL if none found
*/
Actor* getSecondaryQueueElement(Queue* queue, char* name) {
    Actor* thisActor = queue->actors;
    while (thisActor != NULL) {
        if (strcmp(name,thisActor->name)==0) {
            return thisActor;
        }
        thisActor = thisActor->nextSecondaryQueueActor;
    }
    return NULL;
}

/*
* FREE SECONDARY QUEUE
* reinitializes all of the interior actors' 'nextQueueActor' fields to NULL
*   by using the pop function
* then frees queue at end
*/
void freeSecondaryQueue(Queue* queue) {
    // pop 'em all off
    while ( secondaryQueuePop(queue) != NULL ) {}
    free(queue);
}


// ************************* UTILITY FUNCTIONS **********************
/*
* MEMORY CHECK
* aborts program if passed void-ptr is NULL
*/
void memoryCheck(void* ptr) {
    if (ptr == NULL) {
        fprintf(stderr,"Error: out of memory.\n");
        exit(1);
    }
}

/*
* File: graph.h
* Author: Alexander Miller
* Description: header file for graph.c [assg 11]
*/

#ifndef GRAPH_H
#define GRAPH_H

// ********************* STRUCTS  ****************
// EDGE
typedef struct Edge {
    struct Actor* from; // from node
    struct Actor* to; // to node
    char* movie; // name of movie connecting actors
    struct Edge* nextSequentialEdge; // next edge for this actor
} Edge;

// ACTOR
typedef struct Actor {
    char* name; // name of actor
    struct Edge* edges; // edges for this actor
    struct Edge* lastEdge; // allows for easy addition of edges
    struct Actor* nextSequentialActor; // next actor added to graph [allows storage of all actors]
    struct Actor* nextQueueActor; // allows us to traverse a queue
    struct Actor* nextSecondaryQueueActor; // allows membership in multiple queues at once
    int baconScore;
    int mark;
} Actor;

// QUEUE
typedef struct Queue {
    struct Actor* actors;
    struct Actor* lastActor; // makes it easier to add to queue
} Queue;

// ****************** DECLARATION OF FUNCTIONS ********************
// CONSTRUCTORS
Edge* edgeConstructor(Actor* from, Actor* to, char* movie);
Actor* actorConstructor(char* name);
Queue* queueConstructor();

// GRAPH FUNCTIONS
void addEdges(Actor* A, Actor* B, char* movie);
Actor* getActor(char* name, Actor* root);
void addActorToGraph(Actor* root, Actor* actor);
void clearMarks(Actor* root);
void freeGraph(Actor* root);

// QUEUE FUNCTIONS
void queuePush(Queue* queue, Actor* actor);
Actor* queuePop(Queue* queue);

// SECONDARY QUEUE FUNCTIONS
void secondaryQueuePush(Queue* queue, Actor* actor);
Actor* secondaryQueuePop(Queue* queue);
Actor* getSecondaryQueueElement(Queue* queue, char* name);
void freeSecondaryQueue(Queue* queue);


// UTILITY FUNCTIONS
void memoryCheck(void* ptr);



#endif

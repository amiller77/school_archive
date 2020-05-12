/*
* debug2.c
*/

#include <stdio.h>
#include <stdlib.h>

typedef struct Turtle {
    char x;
    char y;
    char dir;
    char* name;
    int odometer;
} Turtle;

void turtle_turnLeft(Turtle* obj) {
    if (obj->dir == 0) {
        obj->dir = 3;
    } else {
        obj->dir --;
    }
}

void turtle_turnRight(Turtle* obj) {
    if (obj->dir == 3) {
        obj->dir = 0;
    } else {
        obj->dir++;
    }
}

void turtle_move(Turtle* obj, int dist) {
    int direction = obj->dir;
    // handle neg distance case:
    if (dist < 0) {
        // swap direction
        // N -> S
        if (direction == 0) {
            direction = 2;
        } // E -> W
        else if (direction == 1) {
            direction = 3;
        } // S -> N
        else if (direction == 2) {
            direction = 0;
        } // W -> E
        else if (direction == 3) {
            direction = 1;
        }

        // get abs val of dist
        dist = -1 * dist;
    }

    // increment odometer by dist
    obj->odometer = obj->odometer + dist;
    // update x, y
    int changeX = 0;    // pay no head, compiler thing
    int changeY = 0;    // pay no heed, compiler thing
    // if N
    if (direction == 0) {
        changeX = 0;
        changeY = dist;
    } // if S
    else if (direction == 2) {
        changeX = 0;
        changeY = -dist;
    } // if E
    else if (direction == 1) {
        changeX = dist;
        changeY = 0;
    } // if W
    else if (direction == 3) {
        changeX = -dist;
        changeY = 0;
    }

    // ensure we don't exceed the board bounds when updating coords
    // for X
    int sumX = obj->x + changeX;
    if (sumX > 10) {
        obj->x = 10;
    } else if (sumX < -10) {
        obj->x = -10;
    } else {
        obj->x = sumX;
    }
    // for Y
    int sumY = obj->y + changeY;
    if (sumY > 10) {
        obj->y = 10;
    } else if (sumY < -10) {
        obj->y = -10;
    } else {
        obj->y = sumY;
    }
}

void printT(Turtle* t) {
    printf("t->x = %d\n",t->x);
    printf("t->y = %d\n",t->y);
    printf("t->dir = %d\n",t->dir);
    printf("t->name = %s\n",t->name);
    printf("t->odometer = %d\n",t->odometer);
    printf("\n");
}

int main() {
    Turtle* t = malloc(sizeof(Turtle));
    t->x = 0;
    t->y = 0;
    t->dir=0;
    char str[] = "johnny";
    t->name = str;
    t->odometer = 0;
    /*
    printT(t);
    turtle_move(t,1);
    printT(t);
    turtle_move(t,-1);
    printT(t);
    turtle_turnLeft(t);
    printT(t);
    turtle_turnRight(t);
    printT(t);
    turtle_turnRight(t);
    printT(t);
    turtle_move(t,2);
    printT(t);
    turtle_move(t,9);
    printT(t);
    turtle_turnRight(t);
    printT(t);
    turtle_turnRight(t);
    printT(t);
    turtle_move(t,25);
    printT(t);
    turtle_turnRight(t);
    */

    printT(t);
    turtle_move(t,11);
    printT(t);
    turtle_turnLeft(t);
    printT(t);
    turtle_turnLeft(t);
    printT(t);
    turtle_move(t,21);
    printT(t);
    return 0;
}

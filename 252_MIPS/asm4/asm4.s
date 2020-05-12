#
# File: asm4.s
# Author: Alexander Miller
# Description: collection of functions performing certain tasks to implement
#   turtle graphics
# Task 1: turtle_init
#   "Constructor" for turtle struct
# Task 2: turtle_debug
#   Print out all fields of turtle, then blank line
# Task 3: turtle_turnLeft & turtle_turnRight
#   Turns the turtle 90 degrees left or right
# Task 4: turtle_move
#   Moves the turtle forward a certain distance
# Task 5: turtle_searchName
#   Search thru an array of turtle objects
# Task 6: turtle_sortByX_indirect
#   Sort a set of turtle objects by their current x values
#
# Layout of turtle struct:
#   Byte        Var
#   0           char x;
#   1           char y;
#   2           char dir;   {0,1,2,3} -> {North, East, South, West}
#   3           ... empty pad ...
#   4-7         name pointer
#   8-11        odometer [total distance traveled]


# Task 1: turtle_init ################################################
# Description: "constructor" for turtle struct
# Params:
#       Turtle* obj : pointer to allocated space for struct
#       char* name : name of turtle
# Behavior:
#   Initialize position to (0,0), pointing 0 [north, odometer 0
#

# PROLOGUE
.text
.globl turtle_init
turtle_init:
    addiu           $sp, $sp, -24               # allocate stack frame
    sw              $fp, 0($sp)                 # store fp
    sw              $ra, 4($sp)                 # store ra
    addiu           $fp, $sp, 20                # update (new) fp

# BODY
    sb              $zero, 0($a0)               # turtle[0] = x = 0
    sb              $zero, 1($a0)               # turtle[1] = y = 0
    sb              $zero, 2($a0)               # turtle[2] = dir = 0
    sw              $a1, 4($a0)                 # turtle[4] = namePtr
    sw              $zero, 8($a0)               # turtle[8] = odom = 0

# EPILOGUE
    lw              $fp, 0($sp)                 # restore fp
    lw              $ra, 4($sp)                 # restore ra
    addiu           $sp, $sp, 24                # deallocate stack frame
    jr              $ra                         # return


# Task 2: turtle_debug ################################################
# Parameters: Turtle *obj : pointer to struct
# Print out fields of Turtle as follows:
# Turtle "charlie"
#   pos 10,3
#   dir North
#   odometer 0
#
# REGISTERS:
#   t0      Turtle *obj
#   t1      obj->name [char*]
#   t2      dir
.data
Turtle:         .asciiz         "Turtle "
Pos:            .asciiz         "  pos "
Dir:            .asciiz         "  dir "
Odom:           .asciiz         "  odometer "
North:          .asciiz         "North\n"
East:           .asciiz         "East\n"
South:          .asciiz         "South\n"
West:           .asciiz         "West\n"

# PROLOGUE
.text
.globl turtle_debug
turtle_debug:
    addiu           $sp, $sp, -24                # allocate stack frame
    sw              $fp, 0($sp)                 # store fp
    sw              $ra, 4($sp)                 # store ra
    addiu           $fp, $sp, 20                # update (new) fp

# BODY
# initalize registers
    add             $t0, $a0, $zero             # t0 = Turtle* obj
    lw              $t1, 4($a0)                 # t1 = obj->name [char*]
    lb              $t2, 2($a0)                 # t2 = dir
# print: "Turtle "[name]"\n"
    addi            $v0, $zero, 4               # indicate str print
    la              $a0, Turtle                 # print "Turtle "
    syscall
    addi            $v0, $zero, 11              # indicate char print
    addi            $a0, $zero, 34              # a0 = '\"'
    syscall
    addi            $v0, $zero, 4               # indicate str print
    add            $a0, $t1, $zero              # print str at obj->name
    syscall
    addi            $v0, $zero, 11              # indicate char print
    addi            $a0, $zero, 34              # a0 = '\"'
    syscall
    addi            $a0, $zero, '\n'            # a0 = \n
    syscall
# print: "  pos x,y\n"
    addi            $v0, $zero, 4               # indicate str print
    la              $a0, Pos                    # print "  pos "
    syscall
    addi            $v0, $zero, 1               # indicate int print
    lb              $a0, 0($t0)                 # a0 = obj[0] = x
    syscall
    addi            $v0, $zero, 11              # indicate char print
    addi            $a0, $zero, ','             # a0 = ','
    syscall
    addi            $v0, $zero, 1               # indicate int print
    lb              $a0, 1($t0)                 # a0 = obj[1] = y
    syscall
    addi            $v0, $zero, 11              # indicate char print
    addi            $a0, $zero, '\n'            # a0 = '\n'
    syscall
# print: "  dir [direction]\n"
    addi            $v0, $zero, 4               # indicate str print
    la              $a0, Dir                    # a0 = "  dir "
    syscall
    addi            $t3, $zero, 1               # t3 = 1 [East]
    addi            $t4, $zero, 2               # t4 = 2 [South]
    addi            $t5, $zero, 3               # t5 = 3 [West]
    beq             $t2, $zero, printNorth      # if dir == 0, North
    beq             $t2, $t3, printEast         # if dir == 1, East
    beq             $t2, $t4, printSouth        # if dir == 2, South
    beq             $t2, $t5, printWest         # if dir == 3, West
printNorth:
    la              $a0, North                  # a0 = "North\n"
    syscall
    j               afterPrint
printEast:
    la              $a0, East                   # a0 = "East\n"
    syscall
    j               afterPrint
printSouth:
    la              $a0, South                  # a0 = "South\n"
    syscall
    j               afterPrint
printWest:
    la              $a0, West                   # a0 = "West\n"
    syscall
afterPrint:
# print: "  odometer [odom]\n"
    la              $a0, Odom                   # a0 = "  odometer "
    syscall
    addi            $v0, $zero, 1               # indicate int print
    lw              $a0, 8($t0)                 # a0 = obj[8] = odom
    syscall
    addi            $v0, $zero, 11              # indicate char print
    addi            $a0, $zero, '\n'            # a0 = '\n'
    syscall
# print extra newline
    syscall

# EPILOGUE
    lw              $fp, 0($sp)                 # restore fp
    lw              $ra, 4($sp)                 # restore ra
    addiu           $sp, $sp, 24                # deallocate stack frame
    jr              $ra                         # return


# Task 3: turtle_turnLeft & turtle_turnRight #############################
# Parameters: Turtle *obj   pointer to struct
# Behavior, update dir to new direction, rotated left or right by 90 deg.
#
#
# void turtle_turnLeft(Turtle* obj) {
#   if (obj->dir == 0) {
#       obj->dir = 3;
#   } else {
#       obj->dir --;
#   }
# }
#
# REGISTERS
#   t0      obj->dir

# PROLOGUE
.globl turtle_turnLeft
turtle_turnLeft:
    addiu           $sp, $sp, -24               # allocate stack frame
    sw              $fp, 0($sp)                 # store fp
    sw              $ra, 4($sp)                 # store ra
    addiu           $fp, $sp, 20                # update (new) fp

# BODY
# if (obj->dir == 0) {
    lb              $t0, 2($a0)                 # t0 = obj->dir
    bne             $t0, $zero, turnLeftElse    #if obj->dir != 0, skip
# obj->dir = 3;
    addi            $t1, $zero, 3               # t1 = 3
    sb              $t1, 2($a0)                 # obj->dir = 3
    j               turnLeftEpilogue

turnLeftElse:
#  } else {
#    obj->dir --;
#  }
#}
    addi            $t1, $t0, -1                # t1 = obj->dir - 1
    sb              $t1, 2($a0)                 # obj->dir --

turnLeftEpilogue:
# EPILOGUE
    lw              $fp, 0($sp)                 # restore fp
    lw              $ra, 4($sp)                 # restore ra
    addiu           $sp, $sp, 24                # deallocate stack frame
    jr              $ra                         # return


# void turtle_turnRight(Turtle* obj) {
#   if (obj->dir == 3) {
#       obj->dir = 0;
#   } else {
#       obj->dir++;
#   }
# }
#
# REGISTERS
#   t0      obj->dir

# PROLOGUE
.globl turtle_turnRight
turtle_turnRight:
    addiu           $sp, $sp, -24               # allocate stack frame
    sw              $fp, 0($sp)                 # store fp
    sw              $ra, 4($sp)                 # store ra
    addiu           $fp, $sp, 20                # update (new) fp

# BODY
# if (obj->dir == 3) {
    addi            $t1, $zero, 3               # t1 = 3
    lb              $t0, 2($a0)                 # t0 = obj->dir
    bne             $t0, $t1, turnRightElse     # if obj->dir != 3, skip
# obj->dir = 0;
    sb              $zero, 2($a0)               # obj->dir = 0
    j               turnRightEpilogue           # skip else block

# } else {
#   obj->dir++;
# }
#}
turnRightElse:
    addi            $t1, $t0, 1                 # t1 = obj->dir + 1
    sb              $t1, 2($a0)                 # obj->dir++;

turnRightEpilogue:
# EPILOGUE
    lw              $fp, 0($sp)                 # restore fp
    lw              $ra, 4($sp)                 # restore ra
    addiu           $sp, $sp, 24                # deallocate stack frame
    jr              $ra                         # return


# Task 4: turtle_move #####################################################
# Description: move the turtle zero or more spaces, in pos or neg direction
#   Max and min for x and y is 10, -10
#   Increment odometer by the abs val of amount directed, rather than the amount moved
#       Therefore, if movement cut of at 10/-10 border, still increment odometer by full amt
# Parameters: Turtle *obj, int dist
#
# void turtle_move(Turtle* obj, int dist) {
#   int direction = obj->dir;
#
#   // handle neg distance case:
#   if (dist < 0) {
#       // swap direction
#       // N -> S
#       if (direction == 0) {
#           direction = 2;
#       } // E -> W
#       else if (direction == 1) {
#           direction = 3;
#       } // S -> N
#       else if (direction == 2) {
#           direction = 0;
#       } // W -> E
#       else if (direction == 3) {
#           direction = 1;
#       }
#
#       // get abs val of dist
#       dist = -1 * dist;
#   }
#
#   // increment odometer by dist
#   obj->odometer = obj->odometer + dist;
#   // update x, y
#   int changeX;
#   int changeY;
#   // if N
#   if (direction == 0) {
#       changeX = 0;
#       changeY = dist;
#   } // if S
#   else if (direction == 2) {
#       changeX = 0;
#       changeY = -dist;
#   } // if E
#   else if (direction == 1) {
#       changeX = dist;
#       changeY = 0;
#   } // if W
#   else if (direction == 3) {
#       changeX = -dist;
#       changeY = 0;
#   }
#
#   // ensure we don't exceed the board bounds when updating coords
#   // for X
#   int sumX = obj->x + changeX;
#   if (sumX > 10) {
#       obj->x = 10;
#   } else if (sumX < -10) {
#       obj->x = -10;
#   } else {
#       obj->x = sumX;
#   }
#   // for Y
#   int sumY = obj->y + changeY;
#   if (sumY > 10) {
#       obj->y = 10;
#   } else if (sumY < -10) {
#       obj->y = -10;
#   } else {
#       obj->y = sumY;
#   }
#
# }
#
# REGISTERS
#   a0          Turtle* obj
#   a1          dist
#   s0          direction
#   s1          changeX
#   s2          changeY
#   s3          sumX
#   s4          sumY

# PROLOGUE
.globl turtle_move
turtle_move:
    addiu           $sp, $sp, -24               # allocate stack frame
    sw              $fp, 0($sp)                 # store fp
    sw              $ra, 4($sp)                 # store ra
    addiu           $fp, $sp, 20                # set (new) fp

# BODY
# store s0 - s4
    addiu           $sp, $sp, -20               # allocate space for 5 words
    sw              $s0, 0($sp)                 # store s0
    sw              $s1, 4($sp)                 # store s1
    sw              $s2, 8($sp)                 # store s2
    sw              $s3, 12($sp)                # store s3
    sw              $s4, 16($sp)                # store s4

# int direction = obj->dir;
    lb              $s0, 2($a0)                 # s0 = direction = obj->dir



# if (dist < 0) {
    slt             $t0, $a1, $zero             # t0 = dist < 0
    beq             $t0, $zero, afterNegHandle  # if dist >= 0, skip block

# // N -> S
# if (direction == 0) {
#   direction = 2;
# }
    bne             $s0, $zero, eastToWest      # if direction != 0, try E2W
    addi            $s0, $zero, 2               # direction = 2
    j               getAbsVal

eastToWest:
# } // E -> W
# else if (direction == 1) {
#   direction = 3;
# }
    addi            $t0, $zero, 1               # t0 = 1
    bne             $s0, $t0, southToNorth      # if direction != 1, try S2N
    addi            $s0, $zero, 3               # direction = 3
    j               getAbsVal

southToNorth:
# } // S -> N
# else if (direction == 2) {
#   direction = 0;
# }
    addi            $t0, $zero, 2               # t0 = 2
    bne             $s0, $t0, westToEast        # if direction != 2, try W2E
    add             $s0, $zero, $zero           # direction = 0
    j               getAbsVal

westToEast:
# } // W -> E
# else if (direction == 3) {
#   direction = 1;
# }
    addi            $t0, $zero, 3               # t0 = 3
    bne             $s0, $t0, getAbsVal         # if direction != 3, skip
    addi            $s0, $zero, 1               # direction = 1

getAbsVal:
# // get abs val of dist
# dist = -1 * dist; // dist = 0 - dist
    sub             $a1, $zero, $a1             # dist = -dist



afterNegHandle:
#   obj->odometer = obj->odometer + dist;
    lw              $t0, 8($a0)                 # t0 = obj->odometer
    add             $t0, $t0, $a1               # t0 = obj->odometer + dist
    sw              $t0, 8($a0)                 # write obj->odometer back

# // update x, y
# int changeX;
# int changeY;
# // if N
# if (direction == 0) {
#   changeX = 0;
#   changeY = dist;
    bne             $s0, $zero, moveSouth       # if direction != 0, try MS
    add             $s1, $zero, $zero           # changeX = 0
    add             $s2, $zero, $a1             # changeY = dist
    j               checkXBounds

# } // if S
# else if (direction == 2) {
#   changeX = 0;
#   changeY = -dist; // changeY = 0 - dist
moveSouth:
    addi            $t0, $zero, 2               # t0 = 2
    bne             $s0, $t0, moveEast          # if direction != 2, try ME
    add             $s1, $zero, $zero           # changeX = 0
    sub             $s2, $zero, $a1             # changeY = -dist
    j               checkXBounds

# } // if E
# else if (direction == 1) {
#   changeX = dist;
#   changeY = 0;
moveEast:
    addi            $t0, $zero, 1               # t0 = 1
    bne             $s0, $t0, moveWest          # if direction != 1, try MW
    add             $s1, $zero, $a1             # changeX = dist
    add             $s2, $zero, $zero           # changeY = 0
    j               checkXBounds

# } // if W
# else if (direction == 3) {
#   changeX = -dist;
#   changeY = 0;
# }
moveWest:
    addi            $t0, $zero, 3               # t0 = 3
    bne             $s0, $t0, checkXBounds      # if direction != 3, skip
    sub             $s1, $zero, $a1             # changeX = -dist
    add             $s2, $zero, $zero           # changeY = 0



#   // ensure we don't exceed the board bounds when updating coords
#   // for X
#   int sumX = obj->x + changeX;
checkXBounds:
    lb              $t0, 0($a0)                 # t0 = obj[0] = obj->x
    add             $s3, $t0, $s1               # sumX = obj->x + changeX
# if (sumX > 10) {
#   obj->x = 10;
    addi            $t0, $zero, 10              # t0 = 10
    slt             $t1, $t0, $s3               # t1 = sumX > 10
    beq             $t1, $zero, move_leftBoundCheck # if sumX <= 10, skip
    sb              $t0, 0($a0)                 # obj->x = 10 [in mem]
    j               checkYBounds

# } else if (sumX < -10) {
#   obj->x = -10;
move_leftBoundCheck:
    addi            $t0, $zero, -10             # t0 = -10
    slt             $t1, $s3, $t0               # t1 = sumX < -10
    beq             $t1, $zero, move_xBoundValid # if sumX >= -10, LB valid
    sb              $t0, 0($a0)                 # obj->x = -10 [in mem]
    j               checkYBounds

# } else {
#   obj->x = sumX;
# }
move_xBoundValid:
    sb              $s3, 0($a0)                 # obj->x = sumX [in mem]


checkYBounds:
# // for Y
# int sumY = obj->y + changeY;
    lb              $t0, 1($a0)                 # t0 = obj->y
    add             $s4, $t0, $s2               # sumY = obj->y + changeY

# if (sumY > 10) {
#   obj->y = 10;
    addi            $t0, $zero, 10              # t0 = 10
    slt             $t1, $t0, $s4               # t1 = sumY > 10
    beq             $t1, $zero, move_bottomBoundCheck   # if sumY <= 10, check BB
    sb              $t0, 1($a0)                 # obj->y = 10 [in mem]
    j               move_restore

# } else if (sumY < -10) {
#   obj->y = -10;
move_bottomBoundCheck:
    addi            $t0, $zero, -10             # t0 = -10
    slt             $t1, $s4, $t0               # t1 = sumY < -10
    beq             $t1, $zero, move_yBoundValid    # if sumY >= -10, y valid
    sb              $t0, 1($a0)                 # obj->y = -10 [in mem]
    j               move_restore

# } else {
#   obj->y = sumY;
# }
move_yBoundValid:
    sb              $s4, 1($a0)                 # obj->y = sumY [in mem]


# restore s0 - s4
move_restore:
    lw              $s0, 0($sp)                 # restore s0
    lw              $s1, 4($sp)                 # restore s1
    lw              $s2, 8($sp)                 # restore s2
    lw              $s3, 12($sp)                # restore s3
    lw              $s4, 16($sp)                # restore s4
    addiu           $sp, $sp, 20                # deallocate space for 5 words

# EPILOGUE
    lw              $fp, 0($sp)                 # restore fp
    lw              $ra, 4($sp)                 # restore ra
    addiu           $sp, $sp, 24                # deallocate stack frame
    jr              $ra                         # return



# Task 5: turtle_searchName ################################################
# Parameters: Turtle* arr, int arrLen, char* needle
# Description: search thru array of turtles to find first instance with
#   name == needle
# Returns:
#   array index of turtle, or -1 if no match found
#
# int turtle_searchName(Turtle* arr, int arrLen, char* needle) {
#   int i;
#   Turtle* thisTurtle = arr;
#   for (i=0;i<arrLen;i++) {
#       if (srcmp(thisTurtle->name, needle)==0) {
#           return i;
#       }
#       thisTurtle++; // [in asm] thisTurtle = thisTurtle + sizeof(Turtle);
#   }
#   return -1;
# }
# REGISTERS
#   a0      Turtle* arr
#   a1      arrLen
#   a2      char* needle
#   s0      i
#   s1      thisTurtle
#

.globl turtle_searchName
turtle_searchName:

# PROLOGUE
    addiu           $sp, $sp, -24               # allocate stack frame
    sw              $fp, 0($sp)                 # store fp
    sw              $ra, 4($sp)                 # store ra
    addiu           $fp, $sp, 20                # update (new) fp

# BODY
# back up s0,s1
    addiu           $sp, $sp, -8                # allocate space for 2
    sw              $s0, 0($sp)                 # store s0
    sw              $s1, 4($sp)                 # store s1

# Turtle* thisTurtle = arr;
    add             $s1, $a0, $zero             # thisTurtle = arr;

# for (i=0;i<arrLen;i++) {
    add             $s0, $zero, $zero           # i = 0
TSLoop:
    beq             $s0, $a1, TSAfterLoop       # if i == arrLen, break
# if (srcmp(thisTurtle->name, needle)==0) {
#   return i;
# }
# back up a1 -> arrLen [we never use arr again, so not bothering to backup a0]
    sw              $a1, 20($sp)                # backup a1
# pass parameters:
    lw              $a0, 4($s1)                 # a0 = thisTurtle->name
    add             $a1, $zero, $a2             # a1 = needle
# call strcmp
    jal             strcmp                      # jump to strcmp
# restore a1
    lw              $a1, 20($sp)                # backup a1
# validate return
    bne             $v0, $zero, iterateTurtle   # if strcmp() != 0, skip
    add             $v0, $zero, $s0             # returnVal = i
    j               TS_EPILOGUE                 # epilogue, then return

# thisTurtle++; // [in asm] thisTurtle = thisTurtle + sizeof(Turtle);
iterateTurtle:
    addi            $s1, $s1, 12                # thisTurtle = thisTurtle + 12
    addi            $s0, $s0, 1                 # i++
    j               TSLoop

TSAfterLoop:
# return -1;
    addi            $v0, $zero, -1              # returnVal =  -1

TS_EPILOGUE:
# restore s0, s1
    lw              $s0, 0($sp)                 # restore s0
    lw              $s1, 4($sp)                 # restore s1
    addiu           $sp, $sp, 8                 # deallocate space for 2

# EPILOGUE
    lw              $fp, 0($sp)                 # restore fp
    lw              $ra, 4($sp)                 # restore ra
    addiu           $sp, $sp, 24                # deallocate stack frame
    jr              $ra                         # return


# Task 6: turtle_sortByX_indirect ##########################################
# Description: sorts an array of turtle pointers by comparing their x values
#
# void turtle_sortByX_indirect(Turtle** arr, int arrLen) {
#   Turtle** sortingHead = arr;
#   Turtle** upperBound = arr + arrLen - 1; // [in asm] arr + arrlen*4 - 4
#   // iterate until we have sorted entire array
#   while (sortingHead <= upperBound) {
#       // search rest of array for min
#       Turtle** comp = sortingHead;
#       Turtle** minPointer = comp;
#       char minVal = (*comp)->x;
#       while (comp <= upperBound) {
#           if ((*comp)->x < minVal) {
#               minVal = (*comp)->x;
#               minPointer = comp;
#           }
#           comp++; // [in asm] comp + 4
#       }
#       // swap minPointer and sortingHead if smaller val found
#       if (minVal < (*sortingHead)->x) {
#           Turtle* buffer = *sortingHead;
#           *sortingHead = *minPointer;
#           *minPointer = buffer;
#       }
#       sortingHead++; // [in asm] SH + 4
#   }
#
# }
#
# REGISTERS
#   a0      Turtle** arr
#   a1      int arrLen
#   s0      Turtle** minPointer
#   s1      char minVal
#   s2      Turtle** sortingHead
#   s3      Turtle** comp
#   s4      Turtle** upperBound
#

# PROLOGUE
.globl turtle_sortByX_indirect
turtle_sortByX_indirect:
    addiu           $sp, $sp, -24               # allocate stack frame
    sw              $fp, 0($sp)                 # store fp
    sw              $ra, 4($sp)                 # store ra
    addiu           $fp, $sp, 20                # update (new) fp

# BODY
# back up s0 - s4
    addiu           $sp, $sp, -20               # allocate space for 5
    sw              $s0, 0($sp)                 # store s0
    sw              $s1, 4($sp)                 # store s1
    sw              $s2, 8($sp)                 # store s2
    sw              $s3, 12($sp)                # store s3
    sw              $s4, 16($sp)                # store s4

# Turtle** sortingHead = arr;
    add             $s2, $zero, $a0             # SH = arr
# Turtle** upperBound = arr + arrLen - 1; // [in asm] arr + arrlen*4 - 4
# math: arrlen * 4 - 4 = 4 * (arrlen - 1) = (arrlen - 1) << 2
    addi            $s4, $a1, -1                # UB = arrlen - 1
    sll             $s4, $s4, 2                 # UB = 4arrlen - 4
    add             $s4, $s4, $a0               # UB = arr + 4arrlen - 4


# while (sortingHead <= upperBound) {
sortLoop:
    slt             $t0, $s4, $s2               # t0 = UB < SH = !(UB >= SH)
    bne             $t0, $zero, afterSortLoop   # if UB < SH, break
# Turtle** comp = sortingHead;
    add             $s3, $zero, $s2             # comp = SH
# Turtle** minPointer = comp;
    add             $s0, $zero, $s3             # MP = comp
# char minVal = (*comp)->x;
    lw              $t3, 0($s3)                 # t3 = *comp
    lb              $s1, 0($t3)                 # s1 = minVal = (*comp)->x


# while (comp <= upperBound) {
innerSortLoop:
    slt             $t0, $s4, $s3               # t0 = UB < comp
    bne             $t0, $zero, swap            # if UB < comp, break

# if ((*comp)->x < minVal) {
    lw              $t3, 0($s3)                 # t3 = *comp
    lb              $t4, 0($t3)                 # t4 = (*comp)->x
    slt             $t5, $t4, $s1               # t5 = (*comp)->x < minVal
    beq             $t5, $zero, iterateComp     # if (*comp)->x >= minVal, skip
# minVal = (*comp)->x;
    add             $s1, $zero, $t4             # minVal = (*comp)->x;
# minPointer = comp;
    add             $s0, $zero, $s3             # minPointer = comp
# }
# comp++; // [in asm] comp + 4
iterateComp:
    addi            $s3, $s3, 4                 # comp++
    j               innerSortLoop


# // swap minPointer and sortingHead if smaller val found
# if (minVal < (*sortingHead)->x) {
swap:
    lw              $t2, 0($s2)                 # t2 = *SH
    lb              $t3, 0($t2)                 # t3 = (*SH)->x
    slt             $t0, $s1, $t3               # t0 = minVal < (*SH)->x
    beq             $t0, $zero, iterateSortingHead  # if minVal >= *SH->x, skip
# Turtle* buffer = *sortingHead;
    add             $t4, $zero, $t2             # t4 = buff =*SH
# *sortingHead = *minPointer;
    lw              $t5, 0($s0)                 # t5 = *minPointer
    sw              $t5, 0($s2)                 # *SH = *minPointer
# *minPointer = buffer;
    sw              $t4, 0($s0)                 # *minPointer = buffer
# }
# sortingHead++; // [in asm] SH + 4
iterateSortingHead:
    addi            $s2, $s2, 4                 # sortingHead++;
    j               sortLoop


afterSortLoop:

# restore s0 - s4
    lw              $s0, 0($sp)                 # restore s0
    lw              $s1, 4($sp)                 # restore s1
    lw              $s2, 8($sp)                 # restore s2
    lw              $s3, 12($sp)                # restore s3
    lw              $s4, 16($sp)                # restore s4
    addiu           $sp, $sp, 20                # deallocate space for 5

# EPILOGUE
    lw              $fp, 0($sp)                 # restore fp
    lw              $ra, 4($sp)                 # restore ra
    addiu           $sp, $sp, 24                # deallocate stack frame
    jr              $ra                         # return

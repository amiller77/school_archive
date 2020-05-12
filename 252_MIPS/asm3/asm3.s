#
# File: asm3.s
# Author: Alexander Miller
# Description: collection of functions performing specific tasks
#   PRINT BLANK LINE AFTER RUNNING ANY TASK (? .. output is returns, not prints)
# Task 1: strlen()
#   Behavior: count number of chars in string
#   Parameters: the address of a string
#   Returns: the count of chars
# Task 2: gcf()
#   Behavior: calculate the GCF between 2 numbers RECURSIVELY
#   Parameters: integers a,b which are positive
# Task 3: bottles()
#   Behavior: prints outa  variation on the bottles of beer song
#   Parameters: count (int), thing (char *)
# Task 4: longestSorted()
#   Behavior: scan thru array, figure out the length of the longest
#       sorted run
#       * longest sorted run: sequence of integers within the array
#           which are already sorted in a non-descending order
#       * if empty array, return 0; if non-empty, smallest possible val is 1
#       * max possible return: length of array
#   Parameters: array (int *), len (int): the length of array
#   Returns: length of the longest consecutive run
# Task 5: rotate()
#   Behavior: executes C code, in part by calling a helper util() function
#       that will be provided by the test case
#   Parameters: (integers) count, a, b, c, d, e, f
#   Returns: retval
#
#


# Task 1: strlen() ###################################################
# int strlen(char *pointer) {
#   char *thisChar = pointer;
#   int count = 0;
#   while (*thisChar != '\0') {
#       count++;
#       thisChar++; // in assembly, need to increment by sizeof char
#   }
#   return count;
# }
#
# REGISTERS:
#   s0      thisChar
#   s1      count
#

# PROLOGUE
.text
.globl strlen
strlen:
    addiu       $sp, $sp, -24               # allocate stack frame
    sw          $fp, 0($sp)                 # backup old fp
    sw          $ra, 4($sp)                 # backup old ra
    addiu       $fp, $sp, 20                # fp for this function

    addiu       $sp, $sp, -8                # allocate space for sX
    sw          $s0, 0($sp)                # backup s0
    sw          $s1, 4($sp)                # backup s1

# BODY
# char *thisChar = pointer;
# int count = 0;
    add         $s0, $a0, $zero             #  s0 = thisChar = pointerParam
    add         $s1, $zero, $zero           #  s1 = count = 0

# while (*thisChar != '\0') {
#    count++;
#    thisChar++; // in assembly, need to increment by sizeof char
# }
strlenLoop:
    addi        $t0, $zero, '\0'            # t0 = '\0'
    lb          $t1, 0($s0)                 # t1 = *thisChar
    beq         $t0, $t1, strlenPostLoop    # if (*thisChar == '\0'), break
    addi        $s1, $s1, 1                 # count++
    addi        $s0, $s0, 1                 # thisChar++ // increment by byte
    j           strlenLoop

#   return count;
strlenPostLoop:
    add         $v0, $s1, $zero             # set return value

# EPILOGUE
    lw          $s0, 0($sp)                 # restore s0
    lw          $s1, 4($sp)                 # restore s1
    addiu       $sp, $sp, 8                 # deallocate sX space

    lw          $fp, 0($sp)                 # restore fp
    lw          $ra, 4($sp)                 # restore ra
    addiu       $sp, $sp, 24                # deallocate stack frame
    jr          $ra                         # return to caller


# Task 2: gcf() ######################################################
# int gcf(int a, int b) {
#   if (a<b) {
#       swap(a,b);
#   }
#   if (b==1) {
#       return 1;
#   }
#   int c = a%b;
#   if (c==0) {
#       return b;
#   } else {
#       return gcf(b, c);
#   }
# }
#
# REGISTERS:
#   a0          a
#   a1          b
#   s0          c
#

# PROLOGUE
.globl gcf
gcf:
    addiu       $sp, $sp, -24               # allocate stack frame
    sw          $fp, 0($sp)                 # backup fp
    sw          $ra, 4($sp)                 # backup ra
    addiu       $fp, $sp, 20                # update fp

    addiu       $sp, $sp, -4                # allocate space for sX
    sw          $s0, 0($sp)                 # backup s0

# BODY
# if (a<b) {
#   swap(a,b);
# }
    slt         $t0, $a0, $a1               # t0 = a<b
    beq         $t0, $zero, gcfSecondCondit # if a>=b, skip op
    add         $t0, $a0, $zero             # t0 = a //[buffer]
    add         $a0, $a1, $zero             # a = b
    add         $a1, $t0, $zero             # b = a

# if (b==1) {
#   return 1;
# }
gcfSecondCondit:
    addi        $t0, $zero, 1               # t0 = 1
    bne         $a1, $t0, gcfThirdConditA   # if (b!=1), skip op
    add         $v0, $a1, $zero             # v0 = b
    j           gcfEPILOGUE

# int c = a%b;
# if (c==0) {
#    return b;
# } else {
#    return gcf(b, c);
# }
gcfThirdConditA:
    div         $a0, $a1                    # lo = a / b ; hi = a % b
    mfhi        $s0                         # s0 = c = a%b
    bne         $s0, $zero, gcfThirdConditB # if (c!=0), go to else block
    add         $v0, $zero, $a1             # v0 = b
    j           gcfEPILOGUE

gcfThirdConditB:
    add         $a0, $a1, $zero             # a0 = b
    add         $a1, $s0, $zero             # a1 = c
    jal         gcf                         # recursive call
    # note: since we are returning v0 directly from the call, no work required

# EPILOGUE
gcfEPILOGUE:

    lw          $s0, 0($sp)                 # restore s0
    addiu       $sp, $sp, 4                 # de-allocate sX

    lw          $fp, 0($sp)                 # restore fp
    lw          $ra, 4($sp)                 # restore ra
    addiu       $sp, $sp, 24                # deallocate stack frame
    jr          $ra                         # return


# Task 3: bottles() ########################################################
# void bottles (int count, char *thing) {
#   for (int i = count; i>0; i--) {
#       printf("%d bottles of %s on the wall, %d bottles of %s!\n",i, thing, i thing);
#       printf("Take one down, pass it around, %d bottles of %s on the wall.\n",i-1,thing);
#       printf("\n");
#   }
#   printf("No more bottles of %s on the wall!\n", thing);
#   printf("\n");
# }
#
#
# REGISTERS:
#   s1          count
#   a1          thing [pointer]
#   s0          i
#

# MEMORY
.data
bottlesOf:      .asciiz     " bottles of "
onTheWall:      .asciiz     " on the wall"
takeDown:       .asciiz     "Take one down, pass it around, "
comma:          .asciiz     ", "
period:         .asciiz     ".\n"
exclamation:    .asciiz     "!\n"
noMoreBottles:  .asciiz     "No more bottles of "

# PROLOGUE
.text
.globl bottles
bottles:
    addiu       $sp, $sp, -24               # allocate stack space
    sw          $fp, 0($sp)                 # store old frame pointer
    sw          $ra, 4($sp)                 # store old return address
    addiu       $fp, $sp, 20                # update fp

    addiu       $sp, $sp, -8                # allocate space for s0, s1
    sw          $s0, 0($sp)                 # store s0
    sw          $s1, 4($sp)                 # store s1

# BODY
# initialize i, count
    add         $s0, $zero, $a0             # s0 = i = count
    add         $s1, $a0, $zero             # s1 = a0 = count
# for (int i = count; i>0; i--) {
bottlesLoop:
    beq         $s0, $zero, bottlesPostLoop # if i == 0, break

# printf("%d bottles of %s on the wall, %d bottles of %s!\n",i, thing, i thing);
    addi        $v0, $zero, 1                # indicate int print
    add         $a0, $s0, $zero              # a0 = i
    syscall
    addi        $v0, $zero, 4               # indicate str print
    la          $a0, bottlesOf              # a0 -> " bottles of "
    syscall
    add         $a0, $a1, $zero             # a0 = a1 = thing
    syscall
    la          $a0, onTheWall              # a0 -> " on the wall"
    syscall
    la          $a0, comma                  # a0 -> ", "
    syscall
    addi        $v0, $zero, 1               # indicate int print
    add         $a0, $s0, $zero             # a0 = i
    syscall
    addi        $v0, $zero, 4               # indicate str print
    la          $a0, bottlesOf              # a0 -> " bottles of "
    syscall
    add         $a0, $a1, $zero             # a0 = a1 = thing
    syscall
    la          $a0, exclamation            # a0 -> "!\n"
    syscall

# printf("Take one down, pass it around, %d bottles of %s on the wall.\n",i-1,thing);
    la          $a0, takeDown               # a0 -> "Take one down, pass it around, "
    syscall
    addi        $v0, $zero, 1               # indicate int print
    addi        $a0, $s0, -1                # a0 = i-1
    syscall
    addi        $v0, $zero, 4               # indicate str print
    la          $a0, bottlesOf              # a0 -> " bottles of "
    syscall
    add         $a0, $a1, $zero             # a0 = a1 = thing
    syscall
    la          $a0, onTheWall              # a0 -> " on the wall"
    syscall
    la          $a0, period                 # a0 -> ".\n"
    syscall

# printf("\n");
    addi        $v0, $zero, 11              # indicate char print
    addi        $a0, $zero, '\n'            # a0 = '\n'
    syscall

# i-- }
    addi        $s0, $s0, -1                # i--
    j           bottlesLoop

bottlesPostLoop:
# printf("No more bottles of %s on the wall!\n", thing);
    addi        $v0, $zero, 4               # indicate str print
    la          $a0, noMoreBottles          # a0 -> "No more bottles of "
    syscall
    add         $a0, $a1, $zero             # a0 = a1 = thing
    syscall
    la          $a0, onTheWall              # a0 -> " on the wall"
    syscall
    la          $a0, exclamation            # a0 -> "!\n"
    syscall

# printf("\n");
    addi        $v0, $zero, 11              # indicate char print
    addi        $a0, $zero, '\n'            # a0 = '\n'
    syscall

# EPILOGUE
    lw          $s0, 0($sp)                 # restore s0
    lw          $s1, 4($sp)                 # restore s1
    addiu       $sp, $sp, 8                 # deallocate space for s0, s1

    lw          $ra, 4($sp)                 # restore ra
    lw          $fp, 0($sp)                 # restore fp
    addiu       $sp, $sp, 24                # deallocate stack frame
    jr          $ra                         # return

# Task 4: longestSorted() #############################################
# // len: length of integer array
# int longestSorted(int *array, int len) {
#   if (len == 0 || len == 1) {
#       return len;
#   }
#   int longestFound = 1;
#   int thisRunLength = 1;
#   int previousElement = *array;       // prevElem = array[0];
#   int i;
#   for (i=1; i<len; i++) {
#       int thisElement = *(array+i);   //will need to add (sizeof int)*i for asm
#       // is this element ascending relative to last?
#       if (thisElement >= previousElement) {
#           thisRunLength++;
#           // if last element of array, potent. need to update longest found
#           if (i == len - 1 && thisRunLength > longestFound) {
#               longestFound = thisRunLength;
#           }
#       } // if not, this run is over
#       else {
#           if (thisRunLength > longestFound) {
#               longestFound = thisRunLength;
#           }
#           thisRunLength = 1;
#       }
#       previousElement = thisElement;
#   }
#   return longestFound;
# }
#
# REGISTERS:
#
# a0        array   [pointer to array start]
# a1        len     [length of array]
# s0        longestFound
# s1        thisRunLength
# s2        previousElement
# s3        thisElement
# s4        i
#
#

# PROLOGUE
.globl longestSorted
longestSorted:
    addiu       $sp, $sp, -24               # allocate stack frame
    sw          $fp, 0($sp)                 # store old fp
    sw          $ra, 4($sp)                 # store old ra
    addiu       $fp, $sp, 20                # update fp

    addiu       $sp, $sp, -20               # allocate space for sX
    sw          $s0, 0($sp)                 # store s0
    sw          $s1, 4($sp)                 # store s1
    sw          $s2, 8($sp)                 # store s2
    sw          $s3, 12($sp)                # store s3
    sw          $s4, 16($sp)                # store s4


# BODY
# if (len == 0 || len == 1) {
#   return len;
# }
    addi        $t0, $zero, 1               # t0 = 1
    add         $v0, $zero, $a1             # return val = len
    beq         $a1, $zero, lsEPILOGUE      # if len == 0, return len
    beq         $a1, $t0, lsEPILOGUE        # if len == 1, return len


# initialize variables:
    addi        $s0, $zero, 1               # s0 = longestFound = 1
    addi        $s1, $zero, 1               # s1 = thisRunLength = 1
    lw          $s2, 0($a0)                 # s2 = previousElement = *array
    addi        $s4, $zero, 1               # s4 = i = 1


# for (i=1; i<len; i++) {
lsLOOP:
    beq         $s4, $a1, lsLOOPAfter       # if i == len, break
# int thisElement = *(array+i);   //will need to add (sizeof int)*i for asm
    add         $t0, $s4, $zero             # t0 = s4 = i
    sll         $t0, $t0, 2                 # t0 = 4*i
    add         $t0, $a0, $t0               # t0 = array + 4*i
    lw          $s3, 0($t0)                 # thisElement = *(array+i)


# if (thisElement >= previousElement) {
    slt         $t0, $s3, $s2               # t0 = thisEl < prevEl
    addi        $t1, $zero, 1               # t1 = 1
    beq         $t0, $t1, lsELSE            # if thisEl < prevEl, goto else
# thisRunLength++;
# // if last element of array, potent. need to update longest found
# if (i == len - 1 && thisRunLength > longestFound) {
#   longestFound = thisRunLength;
# }
    addi        $s1, $s1, 1                 # thisRunLength++
    addi        $t0, $a1, -1                # t0 = len - 1
    bne         $s4, $t0, lsPostELSE        # if i != len -1, skip
    slt         $t0, $s0, $s1               # t0 = longestFound < thisRunLength
    beq         $t0, $zero, lsPostELSE      # if longestFound >= thisRunLength, skip
    add         $s0, $zero, $s1             # longestFound = thisRunLength
    j           lsPostELSE                  # skip else block

lsELSE:
# // if not, this run is over
# else {
#    if (thisRunLength > longestFound) {
#        longestFound = thisRunLength;
#    }
#    thisRunLength = 1;
# }
    slt         $t0, $s0, $s1               # t0 = longestFound < thisRunLength
    beq         $t0, $zero, lsELSEPostCondit    # if longestFound >= thisRunLength, skip
    add         $s0, $s1, $zero             # longestFound = thisRunLength
lsELSEPostCondit:
    addi        $s1, $zero, 1               # thisRunLength = 1


lsPostELSE:
#   previousElement = thisElement;
# }
    add         $s2, $s3, $zero             # previousElement = thisElement
    addi        $s4, $s4, 1                 # i++
    j           lsLOOP


lsLOOPAfter:
    add         $v0, $s0, $zero             # return val = longestFound

# EPILOGUE
lsEPILOGUE:
    lw          $s0, 0($sp)                 # restore s0
    lw          $s1, 4($sp)                 # restore s1
    lw          $s2, 8($sp)                 # restore s2
    lw          $s3, 12($sp)                # restore s3
    lw          $s4, 16($sp)                # restore s4
    addiu       $sp, $sp, 20                # de-allocate space for sX

    lw          $fp, 0($sp)                 # restore fp
    lw          $ra, 4($sp)                 # restore ra
    addiu       $sp, $sp, 24                # de-allocate stack frame
    jr          $ra                         # return

# Task 5: int rotate() ############################################
# int rotate (int count, int a, int b, int c, int d, int e, int f) {
#   int retval = 0;
#   for (int i = 0; i<count; i++) {
#       retval += util(a,b,c,d,e,f);
#       int tmp = a;
#       a = b;
#       b = c;
#       c = d;
#       d = e;
#       e = f;
#       f = tmp;
#   }
#   return retval;
# }
#
# REGISTERS:
#   a0          count
#   a1          a
#   a2          b
#   a3          c
#   s0          d
#   s1          e
#   s2          f
#   s3          retval
#   s4          i
#   s5          tmp
#
#

# PROLOGUE
.globl rotate
rotate:
    addiu       $sp, $sp, -36           # allocate stack frame
    sw          $fp, 0($sp)             # store fp
    sw          $ra, 4($sp)             # store ra
    addiu       $fp, $sp, 32            # set fp

    addiu       $sp, $sp, -24           # allocate space for sX
    sw          $s0, 0($sp)             # store s0
    sw          $s1, 4($sp)             # store s1
    sw          $s2, 8($sp)             # store s2
    sw          $s3, 12($sp)            # store s3
    sw          $s4, 16($sp)            # store s4
    sw          $s5, 20($sp)            # store s5

# BODY
# initialize variables:
    lw          $s0, 48($sp)            # s0 = d
    lw          $s1, 52($sp)            # s1 = e
    lw          $s2, 56($sp)            # s2 = f
    add         $s3, $zero, $zero       # s3 = retval = 0
    add         $s4, $zero, $zero       # s4 = i = 0

# for (int i = 0; i<count; i++) {
rLOOP:
    beq         $s4, $a0, rPostLOOP     # if i == count, break

# retval += util(a,b,c,d,e,f);
# backup a0 - a3 [count, a, b, c]
    sw          $a0, 32($sp)            # store count (a0)
    sw          $a1, 36($sp)            # store a (a1)
    sw          $a2, 40($sp)            # store b (a2)
    sw          $a3, 44($sp)            # store c (a3)
# util(a,b,c,d,e,f);
# pass parameters a-d
    add         $a0, $a1, $zero         # pass a as a0
    add         $a1, $a2, $zero         # pass b as a1
    add         $a2, $a3, $zero         # pass c as a2
    add         $a3, $s0, $zero         # pass d as a3
# pass parameters e,f
    sw          $s2, -4($sp)            # push f on stack
    sw          $s1, -8($sp)            # push e on stack
# call util
    jal         util
# restore a0 - a3 [count, a, b, c]
    lw          $a0, 32($sp)            # restore count (a0)
    lw          $a1, 36($sp)            # restore a (a1)
    lw          $a2, 40($sp)            # restore b (a2)
    lw          $a3, 44($sp)            # restore c (a3)
# retval += (return of util)
    add         $s3, $s3, $v0           # retval += (return of util)

# int tmp = a;
    add         $s5, $a1, $zero         # s5 = temp = a
# a = b;
    add         $a1, $a2, $zero         # a1 = a = b
# b = c;
    add         $a2, $a3, $zero         # a2 = b = c
# c = d;
    add         $a3, $s0, $zero         # a3 = c = d
# d = e;
    add         $s0, $s1, $zero         # s0 = d = e
# e = f;
    add         $s1, $s2, $zero         # s1 = e = f
# f = tmp;
    add         $s2, $s5, $zero         # s2 = f = tmp

    addi        $s4, $s4, 1             # i++
    j           rLOOP


rPostLOOP:
#  return retval;
    add         $v0, $s3, $zero         # v0 = s3 = retval

# EPILOGUE
    lw          $s0, 0($sp)             # restore s0
    lw          $s1, 4($sp)             # restore s1
    lw          $s2, 8($sp)             # restore s2
    lw          $s3, 12($sp)            # restore s3
    lw          $s4, 16($sp)            # restore s4
    lw          $s5, 20($sp)            # restore s5
    addiu       $sp, $sp, 24            # de-allocate space for sX

    lw          $fp, 0($sp)             # restore fp
    lw          $ra, 4($sp)             # restore ra
    addiu       $sp, $sp, 36            # de-allocate stack frame
    jr          $ra                     # return

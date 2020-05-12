#
# File: asm2.s
# Author: Alexander Miller
# Description: MIPS program to read in a certain number of variables
#   which are all a word in size and perform the operation if they are 1
# Task 1: Fibonacci: print out fibonacci sequence up to value of variable
# Task 2: square: print out a square
# Task 3: Run Check: scan thru array of ints, check for ascending or descending
# Task 4: countWords: read in string, count space-delineated words
# Task 5: revString: scan through string and reverse it in memory
#

# program preface code:
.text
.globl studentMain
studentMain:
    addiu   $sp, $sp, -24               # allocate stack space - default 24
    sw      $fp, 0($sp)                 # save caller’s frame pointer
    sw      $ra, 4($sp)                 # save return address
    addiu   $fp, $sp, 20                # setup main’s frame pointer


# Task 1: Fibonacci #######################################
# if (fib != 0) {
#   printf("Fibonacci Numbers: \n");
#   printf(" 0: 1\n");
#   printf(" 1: 1\n");
#   int prev = 1, beforeThat = 1;
#   int n = 2;
#   while (n <= fib) {
#       int cur = prev+beforeThat;
#       printf(" %d: %d\n", n, cur);
#       n++;
#       beforeThat = prev;
#       prev = cur;
#   }
#   printf("\n");
# }
#
# REGISTERS:
# s0        fib
# s1        prev
# s2        beforeThat
# s3        n
# s4        cur

# print strings:
.data
fibonacci:       .asciiz     "Fibonacci Numbers:\n"
fib0:            .asciiz     "  0: 1\n"
fib1:            .asciiz     "  1: 1\n"
fibspace:        .asciiz     "  "

.text
# validate the op, set fib:
    la      $t0, fib                    # t0 = fib address
    lw      $s0, 0($t0)                 # s0 = fib
    beq     $s0, $zero, SQUARE          # if (fib == 0) goto SQUARE
# print intro content:
    addi    $v0, $zero, 4               # v0: string print
    la      $a0, fibonacci              # a0: "Fibonacci Numbers:\n"
    syscall
    la      $a0, fib0                   # a0: " 0: 1\n"
    syscall
    la      $a0, fib1                   # a0: " 1: 1\n"
    syscall
# initialize other standard registers:
    addi    $s1, $zero, 1               # s1 == prev == 1
    addi    $s2, $zero, 1               # s2 == beforeThat == 1
    addi    $s3, $zero, 2               # s3 == n == 2

# loop:
# while (n <= fib) {
#   int cur = prev + beforeThat;
#   printf(" %d: %d\n",n,cur);
#   n++;
#   beforeThat = prev;
#   prev = cur;
# }
fibLoop:
# validate loop, update cur:
    slt     $t0, $s0, $s3               # t0 = fib < n
    bne     $t0, $zero, fibEnd          # if fib < n -> goto fibEnd
    add     $s4, $s1, $s2               # cur = prev + beforeThat
# print statement:
    addi    $v0, $zero, 4               # indicate string print
    la      $a0, fibspace               # a0 = '  '
    syscall
    addi    $v0, $zero, 1               # indicate int print
    add     $a0, $zero, $s3             # a0 = n
    syscall
    addi    $v0, $zero, 11              # indicate char print
    addi    $a0, $zero, ':'             # a0 = ':'
    syscall
    addi    $a0, $zero, ' '             # a0 = ' '
    syscall
    addi    $v0, $zero, 1               # indicate int print
    add     $a0, $zero, $s4             # a0 = cur
    syscall
    addi    $v0, $zero, 11              # indicate char print
    addi    $a0, $zero, '\n'            # a0 = '\n'
    syscall
# update values:
    addi    $s3, $s3, 1                 # n++
    add     $s2, $zero, $s1             # beforeThat = prev
    add     $s1, $zero, $s4             # prev = cur
# jump back
    j fibLoop

fibEnd:
# printf("\n");
    addi    $v0, $zero, 11              # indicate char print
    addi    $a0, $zero, '\n'            # a0 = '\n'
    syscall


# Task 2: square ###########################################
# if (square != 0) {
#   // NOTE: square_fill is a byte
#   // NOTE: square_size is a word
#   for (int row=0; row < square_size; row++) {
#       char lr, mid;
#       if (row == 0 || row == square_size-1) {
#           lr = ’+’;
#           mid = ’-’;
#       } else {
#           lr = ’|’;
#           mid = square_fill;
#       }
#       printf("%c", lr);
#       for (int i=1; i<square_size-1; i++) {
#           printf("%c", mid);
#       }
#       printf("%c\n", lr);
#   }
#   printf("\n");
# }
#
# REGISTERS:
# s0        square
# s1        square_fill
# s2        square_size
# s3        row
# s4        lr
# s5        mid
# s6        i
#

SQUARE:
# get square, validate the op:
    la      $t0, square
    lw      $s0, 0($t0)                 # s0 = square
    beq     $s0, $zero, RUNCHECK        # if square == 0, goto RUNCHECK
# get the other variables from memory:
    la      $t0, square_fill
    lb      $s1, 0($t0)                 # s1 = square_fill [byte]
    la      $t0, square_size
    lw      $s2, 0($t0)                 # s2 = square_size
# initialize row for loop:
    add     $s3, $zero, $zero           # row = 0;

# main loop:
sqLoop:
# validate loop:
    slt     $t0, $s3, $s2               # t0 = row < square_size
    beq     $t0, $zero, sqEnd           # if row >= square_size, goto sqEnd

# if (row == 0 || row == square_size - 1) {
#   lr = '+';
#   mid = '-';
# }
    beq     $s3, $zero, rowCaseOne      # if (row ==0 ) goto rowCaseOne
    addi    $t0, $s2, -1                # t0 = square_size - 1
    beq     $s3, $t0, rowCaseOne        # if (row == square_size-1) goto rowCaseOne
    j       rowCaseTwo                  # else goto rowCaseTwo
rowCaseOne:
    addi    $s4, $zero, '+'             # lr = '+'
    addi    $s5, $zero, '-'             # mid = '-'
    j       rowAfterConditional         # skip else block

# else {
#    lr = '|';
#    mid = square_fill;
# }
rowCaseTwo:
    addi    $s4, $zero, '|'             # lr = '|'
    add     $s5, $zero, $s1             # mid = square_fill

# printf("%c", lr);
rowAfterConditional:
    addi    $v0, $zero, 11              # indicate char print
    add     $a0, $zero, $s4             # a0 = lr
    syscall

# for (int i = 1; i<square_size - 1; i++) {
#   printf("%c",mid);
# }
    addi    $s6, $zero, 1               # i = 1
sqLoopTwo:
    addi    $t1, $s2, -1                # t1 = square_size - 1
    slt     $t0, $s6, $t1               # t0 = i < square_size - 1
    beq     $t0, $zero, afterSqLoopTwo  # if i >= sqsz-1, exit loop
    addi    $v0, $zero, 11              # indicate char print
    add     $a0, $zero, $s5             # a0 = mid
    syscall
# return to top of small loop:
    addi    $s6, $s6, 1                 # i++
    j       sqLoopTwo                   # return to top of small loop

# printf("%c\n", lr);
afterSqLoopTwo:
    addi    $v0, $zero, 11              # indicate char print
    add     $a0, $zero, $s4             # a0 = lr
    syscall
    addi    $a0, $zero, '\n'            # a0= '\n'
    syscall
# return to top of main loop:
    addi    $t0, $zero, 1               # t0 = 1
    add     $s3, $s3, $t0               # row++
    j       sqLoop                      # jump back to top of main loop

# printf("\n")
sqEnd:
    addi    $v0, $zero, 11              # indicate char print
    addi    $a0, $zero, '\n'            # a0 = '\n'
    syscall


# Task 3: Run Check ##########################################
# /* note: in this implementation,
# *       length 0 and length 1 are BOTH asc/desc (correct behavior)
# *  note: this pseudocode doesn't perform required operation validation
# */
# // init values:
# int ascendingTrue = 1;        // is ascending true?
# int descendingTrue = 1;       // is descending true?
# int i = 0;    // iterator
#
# // 0 length array case (don't want to index in at all)
# if (intArray_len == 0) {
#   GOTO report out;
# }
#
# // initialize last val (first round trivial)
# int lastVal = intArray[0];
#
# // perform check:
# for (i = 0; i<intArray_len; i++) {
#   int thisVal = intArray[i];
#   if (thisVal < lastVal) {
#       ascendingTrue = 0;
#   } else if (thisVal > lastVal) {
#       descendingTrue = 0;
#   }
#   lastVal = thisVal;
# }
#
# // report out:
# if (ascendingTrue == 1) {
#   printf("Run Check: ASCENDING\n");
# }
# if (descendingTrue == 1) {
#   printf("Run Check: DESCENDING\n");
# }
# if (ascendingTrue == 0 && descendingTrue == 0) {
#   printf("Run Check: NEITHER\n");
# }
# printf("\n");
#
# REGISTERS:
# s0        runCheck
# s1        *intArray[0]
# s2        intArray_len
# s3        i
# s4        lastVal
# s5        thisVal
# s6        ascendingTrue
# s7        descendingTrue
#

RUNCHECK:

# string variables:
.data
runCheckAscending:      .asciiz     "Run Check: ASCENDING\n"
runCheckDescending:     .asciiz     "Run Check: DESCENDING\n"
runCheckNeither:        .asciiz     "Run Check: NEITHER\n"

.text
# validate the op:
    la      $t0, runCheck
    lw      $s0, 0($t0)                 # s0 = runCheck
    addi    $t0, $zero, 1               # t0 = 1
    bne     $s0, $t0, COUNTWORDS        # if runCheck != 1, goto COUNTWORDS
# init values:
    add     $s3, $zero, $zero           # s3 = i = 0
    addi    $s6, $zero, 1               # s6 = ascendingTrue = 1
    addi    $s7, $zero, 1               # s7 = descendingTrue = 1
    la      $t0, intArray_len
    lw      $s2, 0($t0)                 # s2 = intArray_len
# do intArray_len == 0 edge case -> go straight to report out
    beq     $s2, $zero, runCheckReportOut
# else, access first element of array
    la      $s1, intArray               # s1 = *intArray[0]
    lw      $s4, 0($s1)                 # s4 = lastVal = intArray[0]

# main loop:
# for (i = 0; i<intArray_len; i++) {
#   int thisVal = intArray[i];
#   if (thisVal < lastVal) {
#       ascendingTrue = 0;
#   } else if (thisVal > lastVal) {
#       descendingTrue = 0;
#   }
#   lastVal = thisVal;
# }
runCheckLoop:
# validate loop:
    beq     $s3, $s2, runCheckReportOut # if i==intArray_len, exit loop

# int thisVal = intArray[i];
# address of ith element = base + offset = base + 4*i = base + i << 2
    add     $t0, $s1, $zero             # t0 = *intArray[0]
    sll     $t1, $s3, 2                 # t1 = 4*i
    add     $t0, $t0, $t1               # t0 = base + 4*i
    lw      $s5, 0($t0)                 # s5 = thisVal = intArray[i]

# if (thisVal < lastVal) {
#   ascendingTrue = 0;
# }
    slt     $t0, $s5, $s4               # t0 = thisVal < lastVal
    beq     $t0, $zero, descendingCheck # if thisVal >= lastVal, skip
    add     $s6, $zero, $zero           # set ascendingTrue = 0
    j       rcLoopLastOp                # may as well skip else block

# else if (thisVal > lastVal) {
#    descendingTrue = 0;
# }
descendingCheck:
    slt     $t0, $s4, $s5               # t0 = lastVal < thisVal
    beq     $t0, $zero, rcLoopLastOp    # if lastVal >= thisVal, skip
    add     $s7, $zero, $zero           # set descendingTrue = 0

# lastVal = thisVal
rcLoopLastOp:
    add     $s4, $s5, $zero             # lastVal = thisVal
    addi    $s3, $s3, 1                 # i++

# return to top of loop:
    j       runCheckLoop

# report out:
runCheckReportOut:
# if (ascendingTrue == 1) {
#   printf("Run Check: ASCENDING\n");
# }
    beq     $s6, $zero, reportDesc      # if ascendingTrue ==0, skip
    la      $a0, runCheckAscending      # a0 = &"Run Check: ASCENDING\n"
    addi    $v0, $zero, 4               # indicate string print
    syscall

# if (descendingTrue == 1) {
#   printf("Run Check: DESCENDING\n");
# }
reportDesc:
    beq     $s7, $zero, reportNeither   # if descendingTrue ==0, skip
    la      $a0, runCheckDescending     # a0 = &"Run Check: DESCENDING\n"
    addi    $v0, $zero, 4               # indicate string print
    syscall

# if (ascendingTrue == 0 && descendingTrue == 0) {
#   printf("Run Check: NEITHER\n");
# }
reportNeither:
    addi    $t0, $zero, 1               # t0 = 1
    beq     $s6, $t0, runCheckLastOp    # if ascendingTrue == 1, skip
    beq     $s7, $t0, runCheckLastOp    # if descendingTrue == 1, skip
    la      $a0, runCheckNeither        # a0 = "Run Check: NEITHER\n"
    addi    $v0, $zero, 4               # indicate string print
    syscall

# printf("\n");
runCheckLastOp:
    addi    $a0, $zero, '\n'            # a0 = '\n'
    addi    $v0, $zero, 11              # indicate char print
    syscall



# Task 4: countWords #########################################
# // note: C algorithm doesn't validate the op.
# char str[];
# int flag = 1;
# int count = 0;
# int i = 0;
# int firstLetter = 1;
# while (flag == 1) {
#      char thisChar = str[i];
#      // skipping whitespace:
#      if (thisChar == ' ' || thisChar == '\n') {
#           firstLetter = 1;
#           i++;
#           continue;
#       }
#       // end of string:
#       if (thisChar == '\0') {
#           break;
#       }
#       // non-whitespace char; only increment if first char in "word" (english)
#       if (firstLetter == 1) {
#           count++;
#           firstLetter = 0;
#       }
#
#      i++;
# }
# printf("Word Count: %d\n\n",count);
#
# REGISTERS:
# s0        *str
# s1        flag
# s2        i
# s3        firstLetter
# s4        thisChar
# s5        count
#
COUNTWORDS:

# string variables
.data
cwPrintStringStem:       .asciiz     "Word Count: "
cwPrintStringEnd:        .asciiz     "\n\n"

.text
# validate the op:
    la      $t0, countWords
    lw      $t1, 0($t0)                 # t1 = countWords
    addi    $t2, $zero, 1               # t2 = 1
    bne     $t1, $t2, REVSTRING         # if countWords != 1, skip op
# initialize values
    la      $s0, str                    # s0 = *str
    addi    $s1, $zero, 1               # s1 = flag = 1
    add     $s2, $zero, $zero           # s2 = i = 0
    addi    $s3, $zero, 1               # s3 = firstLetter = 1
    add     $s5, $zero, $zero           # s5 = count = 0

# main loop:
cwMainLoop:
# validate the loop:
    addi    $t0, $zero, 1               # t0 = 1
    bne     $s1, $t0, cwLastOp          # if flag != 1, exit loop
# char thisChar = str[i];
    add     $t0, $s0, $s2               # new address(t0)= base + i = *str + i
    lb      $s4, 0($t0)                 # thisChar = str[i]

# // skipping whitespace:
# if (thisChar == ' ' || thisChar == '\n') {
#    firstLetter = 1;
#    i++;
#    continue;
# }
    addi    $t0, $zero, ' '             # t0 = ' '
    addi    $t1, $zero, '\n'            # t1 = '\n'
    beq     $s4, $t0, cwWsHandling      # if thisChar == ' ', goto cwWsHandling
    beq     $s4, $t1, cwWsHandling      # if thisChar == '\n', goto cwWsHandling
    j       cwNullHandling              # else skip white space handling
cwWsHandling:
    addi    $s3, $zero, 1               # firstLetter = 1
    j       cwIncrementor               # iterate i, then return to top

# // end of string:
# if (thisChar == '\0') {
#    break;
# }
cwNullHandling:
    addi    $t0, $zero, '\0'            # t0 = '\0'
    beq     $s4, $t0, cwLastOp          # if (thisChar=='\0'), break loop

# // non-whitespace char; only increment if first char in "word" (english)
# if (firstLetter == 1) {
#    count++;
#    firstLetter = 0;
# }
# i++;
#
    addi    $t0, $zero, 1               # t0 = 1
    bne     $s3, $t0, cwIncrementor     # if firstLetter != 1, skip if block
    addi    $s5, $s5, 1                 # count++
    add     $s3, $zero, $zero           # firstLetter = 0;

cwIncrementor:
    addi    $s2, $s2, 1                 # i++
    j       cwMainLoop                  # continue;

# printf("Word Count: %d\n\n",count); # prefix removed due to spec discrepancy
cwLastOp:
    add     $a0, $zero, $s5             # a0 = count
    addi    $v0, $zero, 1               # indicate int print
    syscall
    la      $a0, cwPrintStringEnd       # a0 = "\n\n"
    addi    $v0, $zero, 4               # indicate string print
    syscall



# Task 5: revString ##########################################
# note: algorithm does not include op validation
# int head = 0;
# int tail = 0;
# while (str[tail]!='\0') {
#   tail++;
# }
# tail--;
#
# while (head < tail) {
#   swap(str[head],str[tail]);
#   head++;
#   tail--;
# }
#
# printf("String successfully swapped!\n");
# printf("\n");
#
# REGISTERS:
# s0        *str
# s1        head
# s2        tail
# s3        *str[head]
# s4        *str[tail]
# s5        str[head]
# s6        str[tail]
# s7        bufferByte
#

REVSTRING:

# print string:
.data
revPrintString:         .asciiz     "String successfully swapped!\n"

.text
# validate the op:
    addi        $t0, $zero, 1           # t0 = 1
    la          $t1, revString          # t1 = *revString
    lw          $t2, 0($t1)             # t2 = revString
    bne         $t0, $t2, EPILOGUE      # if (revString != 1), skip op
# initialize values:
    la          $s0, str                # s0 = *str
    add         $s1, $zero, $zero       # s1 = head = 0
    add         $s2, $zero, $zero       # s2 = tail = 0

# while (str[tail]!='\0') {
#   tail++;
# }
findStringSizeLoop:
    add         $s4, $s0, $s2           # s4 = *str[tail] = *str + tail
    lb          $s6, 0($s4)             # s6 = str[tail]
    addi        $t0, $zero, '\0'        # t0 = '\0'
    beq         $t0, $s6, decrementTail # if (str[tail]=='\0'), skip loop
    addi        $s2, $s2, 1             # tail++
    j           findStringSizeLoop      # return to top of loop

# tail--;
decrementTail:
    addi        $s2, $s2, -1            # tail--

# while (head < tail) {
#   swap(str[head],str[tail]);
#   head++;
#   tail--;
# }
swappingLoop:
    slt         $t0, $s1, $s2           # t0 = head < tail
    beq         $t0, $zero, rvLastOp    # if head >= tail, goto rvLastOp
    add         $s3, $s0, $s1           # *str[head] = *str + head
    lb          $s7, 0($s3)             # bufferByte = str[head]
    add         $s4, $s0, $s2           # *str[tail] = *str + tail
    lb          $s5, 0($s4)             # str[head] = str[tail]
    add         $s6, $zero, $s7         # str[tail] = bufferByte
    sb          $s5, 0($s3)             # write str[head] to memory at *str[head]
    sb          $s6, 0($s4)             # str[tail] to memory at *str[tail]
    addi        $s1, $s1, 1             # head++
    addi        $s2, $s2, -1            # tail--
    j           swappingLoop            # return to top of loop

# printf("String successfully swapped!\n");
# printf("\n");
rvLastOp:
    la          $a0, revPrintString     # a0= String successfully swapped!\n"
    addi        $v0, $zero, 4           # indicate string print
    syscall
    addi        $a0, $zero, '\n'        # a0 = '\n'
    addi        $v0, $zero, 11          # indicate char print
    syscall


# wrapping up the function code:
EPILOGUE:
    lw      $ra, 4($sp)                 # get return address from stack
    lw      $fp, 0($sp)                 # restore the caller’s frame pointer
    addiu   $sp, $sp, 24                # restore the caller’s stack pointer
    jr      $ra                         # return to caller’s code

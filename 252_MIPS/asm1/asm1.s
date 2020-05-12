# File: asm1.s
# Author: Alexander Miller
# Inputs: median, absVal, sum, rotate, dump : 32bits, either 1 or 0
#   If inputs are 1, run the associated task
# Variables to Read: one, two, three

# program preface code:
.text
.globl studentMain
studentMain:
    addiu   $sp, $sp, -24           # allocate stack space - default 24
    sw      $fp, 0($sp)             # save caller’s frame pointer
    sw      $ra, 4($sp)             # save return address
    addiu   $fp, $sp, 20            # setup main’s frame pointer


# REGISTERS FOR ENTIRE PROGRAM
#   s1 = one
#   s2 = two
#   s3 = three
# load these registers:
    la $t1, one
    la $t2, two
    la $t3, three
    lw $s1, 0($t1)
    lw $s2, 0($t2)
    lw $s3, 0($t3)


# Task 1: median ############################################
# if (median == 1) {
#   if (one == two || one == three)
#       print("median: %d\n", one);
#   else if (two == three)
#       print("median: %d\n", two);
#   else {
#       int cmp12 = (one < two);
#       int cmp13 = (one < three);
#       int cmp23 = (two < three);
#       printf("Comparisons: %d %d %d\n", cmp12,cmp13,cmp23);
#       if (cmp12 == cmp23) // a<b<c or a>b>c
#         printf("median: %d\n", two);
#       if (cmp12 != cmp13) // b<a<c or b>a>c
#           printf("median: %d\n", one);
#       if (cmp13 != cmp23) // a<c<b or a>c>b
#           printf("median: %d\n", three);
#   }
#   printf("\n");
# }
MEDIAN:

# validate the operation:
# if (median == 1) {
#   do op
# } else {
#   don't
# }
    la      $t0, median             # get address of median
    lw      $t1, 0($t0)             # t1 = value of median
    addi    $t2, $zero, 1           # put 1 in a temporary
    bne     $t1, $t2, ABSVAL        # skip median op if median != 1

# print strings for median operation:
.data
MEDIANMSG:    .asciiz     "median: "
COMPMSG:      .asciiz     "Comparisons: "

.text
# CASE 1: where there's a duplicate value:
# if (one == two || one == three)
#       print("median: %d\n", one);
    add     $t0, $zero, $s1         # hold on to one to print (maybe)
    beq     $s1, $s2, Print         # if one==two, print median=1
    beq     $s1, $s3, Print         # if one==three, print median=1
    j       CompareTwoThree         # else, skip to nxt conditional
Print:
    # .... printing whatever is in t0 ~ "median: t0\n" ....
    addi    $v0, $zero, 4           # indicate a string print
    la      $a0, MEDIANMSG          # load address of msg
    syscall                         # print root of msg
    addi    $v0, $zero, 1           # indicate int print
    add     $a0, $zero, $t0         # give value of t0 to print
    syscall                         # print the val of t0
    addi    $v0, $zero, 11          # indicate a char print
    addi    $a0, $zero, '\n'        # put a newline into a0
    syscall
    # .... end printing ....
    j       MedianLastOp            #  skip to very end of median op
#   else if (two == three)
#       print("median: %d\n", two);
CompareTwoThree:
    add     $t0, $zero, $s2         # hold on to two to print (maybe)
    beq     $s2, $s3, Print         # if two==three, print median=two


# CASE 2: where we check all 3 against each other
# else {
#   int cmp12 = (one < two);
#   int cmp13 = (one < three);
#   int cmp23 = (two < three);
#   printf("Comparisons: %d %d %d\n", cmp12,cmp13,cmp23);
#   if (cmp12 == cmp23) // a<b<c or a>b>c
#       printf("median: %d\n", two);
#   if (cmp12 != cmp13) // b<a<c or b>a>c
#       printf("median: %d\n", one);
#   if (cmp13 != cmp23) // a<c<b or a>c>b
#       printf("median: %d\n", three);
# }
# REGISTERS FOR CASE 2
#   s4 = cmp12
#   s5 = cmp13
#   s6 = cmp23
    slt     $s4, $s1, $s2           # int cmp12 = (one < two);
    slt     $s5, $s1, $s3           # int cmp13 = (one < three);
    slt     $s6, $s2, $s3           # int cmp23 = (two < three);
#   printf("Comparisons: %d %d %d\n", cmp12,cmp13,cmp23);
    addi    $v0, $zero, 4           # indicate string print
    la      $a0, COMPMSG            # configure pointer for string
    syscall
    addi    $v0, $zero, 1           # indicate integer print
    add     $a0, $zero, $s4         # print value: cmp12
    syscall
    addi    $v0, $zero, 11          # indicate character print
    addi    $a0, $zero, ' '         # print value: ' '
    syscall
    addi    $v0, $zero, 1           # indicate integer print
    add     $a0, $zero, $s5         # print value: cmp13
    syscall
    addi    $v0, $zero, 11          # indicate character print
    addi    $a0, $zero, ' '         # print value: ' '
    syscall
    addi    $v0, $zero, 1           # indicate integer print
    add     $a0, $zero, $s6         # print value: cmp23
    syscall
    addi    $v0, $zero, 11          # indicate character print
    addi    $a0, $zero, '\n'        # print value: '\n'
    syscall
#   if (cmp12 == cmp23) // a<b<c or a>b>c
#       printf("median: %d\n", two);
    bne     $s4, $s6, SecondCompare # if cmp12 == cmp23, skip
    add     $t0, $zero, $s2         # set t0 to two
    j       Print                   # printf("median: %d\n", two);

#   if (cmp12 != cmp13) // b<a<c or b>a>c
#       printf("median: %d\n", one);
SecondCompare:
    beq     $s4, $s5, ThirdCompare  # if cmp12 == cmp13, skip
    add     $t0, $zero, $s1         # set t0 to one
    j       Print                   # printf("median: %d\n", one);

#   if (cmp13 != cmp23) // a<c<b or a>c>b
#       printf("median: %d\n", three);
ThirdCompare:
    beq     $s5, $s6, MedianLastOp  # if cmp13 == cmp23, skip
    add     $t0, $zero, $s3         # set t0 to three
    j       Print                   # printf("median: %d\n", three);


# printf("\n");
MedianLastOp:
    addi    $a0, $zero, '\n'        # print value: '\n'
    addi    $v0, $zero, 11          # indicate char to print
    syscall



# Task 2: absVal ############################################
ABSVAL:
# variables for printing output
.data
ONESTRING:      .asciiz     "'one' was negative\n"
TWOSTRING:      .asciiz     "'two' was negative\n"
THREESTRING:    .asciiz     "'three' was negative\n"

.text
# validate the operation:
    la      $t0, absVal             # t0 = pointer for absVal
    lw      $t1, 0($t0)             # t1 = val for absVal
    addi    $t2, $zero, 1           # t2 = 1
    bne     $t1, $t2, SUM           # if absVal!=1, skip to task 3


# check one for negativity
    slt     $t0, $s1, $zero         # t0 = (one < zero)
    beq     $t0, $zero, CheckTwo    # if one >= zero -> go to CheckTwo
    # print out that it was negative
    la      $a0, ONESTRING          # get pointer to string
    addi    $v0, $zero, 4           # indicate a string print
    syscall
# reverse the value and write to memory
    sub     $s1, $zero, $s1         # one = -one
    la      $t0, one                # get address of one
    sw      $s1, 0($t0)             # write to memory


# check two for negativity
CheckTwo:
    slt     $t0, $s2, $zero         # t0 = (two < zero)
    beq     $t0, $zero, CheckThree  # if (two >= zero) -> go to CheckThree
    # print out that it was negative
    la      $a0, TWOSTRING          # get pointer to string
    addi    $v0, $zero, 4           # indicate a string print
    syscall
# reverse the value and write to memory
    sub     $s2, $zero, $s2         # two = -two
    la      $t0, two                # get address of two
    sw      $s2, 0($t0)             # write to memory


# check three for negativity
CheckThree:
    slt     $t0, $s3, $zero         # t0 = (three < zero)
    beq     $t0, $zero, PrintBlank         # if three >= zero, skip to print
    # print out that it was negative
    la      $a0, THREESTRING        # get pointer to string
    addi    $v0, $zero, 4           # indicate a string print
    syscall
# reverse the value and write to memory
    sub     $s3, $zero, $s3         # three = -three
    la      $t0, three              # get address of three
    sw      $s3, 0($t0)             # write to memory


PrintBlank:
    addi    $v0, $zero, 11          # indicate char print
    addi    $a0, $zero, '\n'        # a0 = '\n'
    syscall


# Task 3: sum  ############################################
SUM:
.data
SUMSTRING:   .asciiz    "sum: "

.text
# validate the operation:
    la      $t0, sum                # get pointer to sum
    lw      $t1, 0($t0)             # t1 = val of sum
    addi    $t2, $zero, 1           # t2 = 1
    bne     $t2, $t1, ROTATE        # if sum != 1, go to task 4
# sum the three values
    add     $t0, $s1, $s2           # t0 = one + two
    add     $t0, $t0, $s3           # t0 = one + two + three
# print out the sum root
    addi    $v0, $zero, 4           # indicate string print
    la      $a0, SUMSTRING          # get pointer to sumstring
    syscall
# print out the digit
    addi    $v0, $zero, 1           # indicate int print
    add     $a0, $zero, $t0         # a0 = one + two + three
    syscall
# print out newline end
    addi    $v0, $zero, 11          # indicate char print
    addi    $a0, $zero, '\n'        # a0 = '\n'
    syscall
# print out newline end (pretty sure we need 2)
    syscall


# Task 4: rotate  ############################################
ROTATE:
# validate the operation:
    la      $t0, rotate             # get address of rotate
    lw      $t1, 0($t0)             # t1 = val of rotate
    addi    $t2, $zero, 1           # t2 = 1
    bne     $t1, $t2, DUMP          # if rotate != 1, skip operation
# rotate the values:
    add     $t0, $s2, $zero         # copy two to t0
    add     $s2, $s1, $zero         # move one to two's spot
    add     $t1, $s3, $zero         # copy three to t1
    add     $s3, $t0, $zero         # move two copy to three
    add     $s1, $t1, $zero         # move three copy to one
# update memory
    la      $t1, one                # load variable addresses
    la      $t2, two
    la      $t3, three
    sw      $s1, 0($t1)             # write new var vals to memory
    sw      $s2, 0($t2)
    sw      $s3, 0($t3)


# Task 5: dump  ############################################
DUMP:
# validate the operation:
    la      $t0, dump               # t0 = address of dump
    lw      $t1, 0($t0)             # t1 = value of dump
    addi    $t2, $zero, 1           # t2 = 1
    bne     $t1, $t2, EPILOGUE      # if dump != 1, skip to epilogue
# variables to hold our print strings:
.data
DUMPONESTRING:      .asciiz     "one: "
DUMPTWOSTRING:      .asciiz     "two: "
DUMPTHREESTRING:    .asciiz     "three: "
.text
# print one: val
    la      $a0, DUMPONESTRING      # get pointer to DUMPONESTRING
    addi    $v0, $zero, 4           # indicate string print
    syscall
    add     $a0, $zero, $s1         # a0 = one (value)
    addi    $v0, $zero, 1           # indicate int print
    syscall
    addi    $a0, $zero, '\n'        # a0 = '\n'
    addi    $v0, $zero, 11          # indicate char print
    syscall
# print two: val
    la      $a0, DUMPTWOSTRING      # get pointer to DUMPTWOSTRING
    addi    $v0, $zero, 4           # indicate string print
    syscall
    add     $a0, $zero, $s2         # a0 = two (value)
    addi    $v0, $zero, 1           # indicate int print
    syscall
    addi    $a0, $zero, '\n'        # a0 = '\n'
    addi    $v0, $zero, 11          # indicate char print
    syscall
# print three: val
    la      $a0, DUMPTHREESTRING    # get pointer to DUMPTHREESTRING
    addi    $v0, $zero, 4           # indicate string print
    syscall
    add     $a0, $zero, $s3         # a0 = three (value)
    addi    $v0, $zero, 1           # indicate int print
    syscall
    addi    $a0, $zero, '\n'        # a0 = '\n'
    addi    $v0, $zero, 11          # indicate char print
    syscall
# print '/n'
    syscall


# wrapping up the function code:
EPILOGUE:
    lw      $ra, 4($sp)     # get return address from stack
    lw      $fp, 0($sp)     # restore the caller’s frame pointer
    addiu   $sp, $sp, 24    # restore the caller’s stack pointer
    jr      $ra             # return to caller’s code

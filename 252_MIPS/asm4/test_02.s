# test_02.s
#
# A testcase for Asm 4.



# ----------- main() -----------
.text


.globl	main
main:
	# fill the sX registers (and fp) with junk.  Each testcase will use a different
	# set of values.
	lui   $fp,      0xdf42
	ori   $fp, $fp, 0x983b
	lui   $s0,      0x826e
	ori   $s0, $s0, 0x8575
	lui   $s1,      0x37f7
	ori   $s1, $s1, 0xf81d
	lui   $s2,      0x3c2a
	ori   $s2, $s2, 0x5954
	lui   $s3,      0x8da6
	ori   $s3, $s3, 0xceac
	lui   $s4,      0x00e7
	ori   $s4, $s4, 0x531d
	lui   $s5,      0xa14c
	ori   $s5, $s5, 0x17ee
	lui   $s6,      0x7e3f
	ori   $s6, $s6, 0x3457
	lui   $s7,      0x09c6
	ori   $s7, $s7, 0x7fb6

	# instead of explicitly dumping the stack pointer, I'll push a dummy
	# variable onto the stack.  Some students are reporting different
	# stack values in their output.
	lui   $t0,      0xa302
	ori   $t0, $t0, 0xf09a
	addiu $sp, $sp,-4
	sw    $t0, 0($sp)


.data

TEST_NAME_1:
	.asciiz "Billy"
TEST_NAME_2:
	.asciiz "Blinky"
TEST_NAME_3:
	.asciiz "Pinky"
TEST_NAME_4:
	.asciiz "Inky"
TEST_NAME_5:
	.asciiz "Clyde"
TEST_NAME_6:
	.asciiz "Bonnie"
TEST_NAME_7:
	.asciiz "Prince"
TEST_NAME_8:
	.asciiz "Charlie"

	.word	0xabcdef01

TEST_TURTLE_ARRAY:
	.byte	0
	.byte	6
	.byte	1
	.byte	0
	.word	TEST_NAME_1
	.word	387

	.byte	-10
	.byte	-3
	.byte	2
	.byte	0
	.word	TEST_NAME_2
	.word	906

	.byte	10
	.byte	9
	.byte	0
	.byte	0
	.word	TEST_NAME_3
	.word	763

	.byte	10
	.byte	-9
	.byte	2
	.byte	0
	.word	TEST_NAME_4
	.word	383

	.byte	-9
	.byte	10
	.byte	0
	.byte	0
	.word	TEST_NAME_5
	.word	559

	.byte	0
	.byte	5
	.byte	3
	.byte	0
	.word	TEST_NAME_6
	.word	681

	.byte	-5
	.byte	3
	.byte	3
	.byte	0
	.word	TEST_NAME_7
	.word	144

	.byte	1
	.byte	2
	.byte	3
	.byte	0
	.word	TEST_NAME_8
	.word	123

	.word	0x23456789

TEST_TURTLE_ARRAY_LEN:
	.word	8

.text


.data
NEEDLE_1:
	.asciiz "No match"
NEEDLE_2:
	.asciiz "Billy"
NEEDLE_3:
	.asciiz "Charlie"
NEEDLE_4:
	.asciiz "Bonnie_x"
NEEDLE_5:
	.asciiz "Bonnie"
.text

	la      $a0, TEST_TURTLE_ARRAY
	la	$a1, TEST_TURTLE_ARRAY_LEN
	lw	$a1, 0($a1)
	la	$a2, NEEDLE_1
	jal     turtle_searchName

	add     $a0, $v0,$zero         # print_int(retval)
	addi    $v0, $zero,1
	syscall

	addi    $v0, $zero,11          # print_char('\n')
	addi    $a0, $zero,'\n'
	syscall

	la      $a0, TEST_TURTLE_ARRAY
	la	$a1, TEST_TURTLE_ARRAY_LEN
	lw	$a1, 0($a1)
	la	$a2, NEEDLE_2
	jal     turtle_searchName

	add     $a0, $v0,$zero         # print_int(retval)
	addi    $v0, $zero,1
	syscall

	addi    $v0, $zero,11          # print_char('\n')
	addi    $a0, $zero,'\n'
	syscall

	la      $a0, TEST_TURTLE_ARRAY
	la	$a1, TEST_TURTLE_ARRAY_LEN
	lw	$a1, 0($a1)
	la	$a2, NEEDLE_3
	jal     turtle_searchName

	add     $a0, $v0,$zero         # print_int(retval)
	addi    $v0, $zero,1
	syscall

	addi    $v0, $zero,11          # print_char('\n')
	addi    $a0, $zero,'\n'
	syscall

	la      $a0, TEST_TURTLE_ARRAY
	la	$a1, TEST_TURTLE_ARRAY_LEN
	lw	$a1, 0($a1)
	la	$a2, NEEDLE_4
	jal     turtle_searchName

	add     $a0, $v0,$zero         # print_int(retval)
	addi    $v0, $zero,1
	syscall

	addi    $v0, $zero,11          # print_char('\n')
	addi    $a0, $zero,'\n'
	syscall

	la      $a0, TEST_TURTLE_ARRAY
	la	$a1, TEST_TURTLE_ARRAY_LEN
	lw	$a1, 0($a1)
	la	$a2, NEEDLE_5
	jal     turtle_searchName

	add     $a0, $v0,$zero         # print_int(retval)
	addi    $v0, $zero,1
	syscall

	addi    $v0, $zero,11          # print_char('\n')
	addi    $a0, $zero,'\n'
	syscall


	# dump out all of the registers.
.data
TESTCASE_DUMP1:	.ascii  "\n"
               	.ascii 	"+-----------------------------------------------------------+\n"
		.ascii	"|    Magic Value (popped from the stack):                   |\n"
		.asciiz	"+-----------------------------------------------------------+\n"

TESTCASE_DUMP2:	.ascii  "\n"
               	.ascii 	"+-----------------------------------------------------------+\n"
		.ascii	"|    Testcase Register Dump (fp, then 8 sX regs):           |\n"
		.asciiz	"+-----------------------------------------------------------+\n"
.text
	addi	$v0, $zero, 4		# print_str(TESTCASE_DUMP1)
	la	$a0, TESTCASE_DUMP1
	syscall

	# we pop this from the stack so that, if the stack pointer is not
	# predictable, we'll still get reliable results.
	lw      $a0, 0($sp)
	addiu   $sp, $sp,4
	jal     printHex

	# the rest of the registers have hard-coded values
	addi	$v0, $zero, 4		# print_str(TESTCASE_DUMP2)
	la	$a0, TESTCASE_DUMP2
	syscall

	add	$a0, $fp, $zero
	jal     printHex
	add	$a0, $s0, $zero
	jal     printHex
	add	$a0, $s1, $zero
	jal     printHex
	add	$a0, $s2, $zero
	jal     printHex
	add	$a0, $s3, $zero
	jal     printHex
	add	$a0, $s4, $zero
	jal     printHex
	add	$a0, $s5, $zero
	jal     printHex
	add	$a0, $s6, $zero
	jal     printHex
	add	$a0, $s7, $zero
	jal     printHex

	# terminate the program
	addi	$v0, $zero, 10		# syscall_exit
	syscall
	# never get here!



# ---- some functions that the student code can call ----

# int strcmp(char *a, char *b)
# {
#     char *p1 = a;
#     char *p2 = b;
#
#     while (*p1 != '\0' && *p2 != '\0' && *p1 == *p2)
#     {
#         p1++;
#         p2++;
#     }
#
#     return *p1 - *p2;
# }
#
# REGISTERS
#   t0 -  p1
#   t1 -  p2
#   t2 - *p1
#   t3 - *p2
#   t8 - various temporaries

.globl strcmp
strcmp:
	# standard prologue
	addiu   $sp, $sp, -24
	sw      $fp, 0($sp)
	sw      $ra, 4($sp)
	addiu   $fp, $sp, 20

	add     $t0, $a0,$zero          # p1 = a
	add     $t1, $a1,$zero          # p2 = b

strcmp_LOOP:
	lb      $t2, 0($t0)             # read *p1
	lb      $t3, 0($t1)             # read *p2
	beq     $t2,$zero, strcmp_DONE  # if (*p1 == '\0') break
	beq     $t3,$zero, strcmp_DONE  # if (*p2 == '\0') break
	bne     $t2,$t3,   strcmp_DONE  # if (*p1 != *p2 ) break

	addi    $t0, $t0,1              # p1++
	addi    $t1, $t1,1              # p2++
	j       strcmp_LOOP

strcmp_DONE:
	sub     $v0, $t2,$t3            # return *p1 - *p2

	# standard epilogue
	lw      $ra, 4($sp)
	lw      $fp, 0($sp)
	addiu   $sp, $sp, 24
	jr      $ra



# ---- UTILITY FUNCTIONS, FOR THE TESTCASE ITSELF ----

debug_turtle_move:
	# standard prologue
	addiu   $sp, $sp, -24
	sw      $fp, 0($sp)
	sw      $ra, 4($sp)
	addiu   $fp, $sp, 20

	addiu   $sp, $sp,-8
	sw      $a0, 0($sp)
	sw      $a1, 4($sp)

.data
debug_turtle_move_MSG:
	.asciiz "turtle_move "
.text

	addi    $v0, $zero,4
	la      $a0, debug_turtle_move_MSG
	syscall

	addi    $v0, $zero,1       # print_int(dist)
	lw      $a0, 4($sp)
	syscall

	addi    $v0, $zero,11      # print_char('\n')
	addi    $a0, $zero,'\n'
	syscall

	# turtle_move(obj,dist)
	lw      $a0, 0($sp)
	lw      $a1, 4($sp)
	addiu   $sp, $sp,8
	jal     turtle_move

	# standard epilogue
	lw      $ra, 4($sp)
	lw      $fp, 0($sp)
	addiu   $sp, $sp, 24
	jr      $ra



debug_turtle_turnLeft:
	# standard prologue
	addiu   $sp, $sp, -24
	sw      $fp, 0($sp)
	sw      $ra, 4($sp)
	addiu   $fp, $sp, 20

	addiu   $sp, $sp,-4
	sw      $a0, 0($sp)

.data
debug_turtle_turnLeft_MSG:
	.asciiz "turtle_turnLeft\n"
.text

	addi    $v0, $zero,4
	la      $a0, debug_turtle_turnLeft_MSG
	syscall

	# turtle_turnLeft(obj)
	lw      $a0, 0($sp)
	addiu   $sp, $sp,4
	jal     turtle_turnLeft

	# standard epilogue
	lw      $ra, 4($sp)
	lw      $fp, 0($sp)
	addiu   $sp, $sp, 24
	jr      $ra



debug_turtle_turnRight:
	# standard prologue
	addiu   $sp, $sp, -24
	sw      $fp, 0($sp)
	sw      $ra, 4($sp)
	addiu   $fp, $sp, 20

	addiu   $sp, $sp,-4
	sw      $a0, 0($sp)

.data
debug_turtle_turnRight_MSG:
	.asciiz "turtle_turnRight\n"
.text

	addi    $v0, $zero,4
	la      $a0, debug_turtle_turnRight_MSG
	syscall

	# turtle_turnLeft(obj)
	lw      $a0, 0($sp)
	addiu   $sp, $sp,4
	jal     turtle_turnRight

	# standard epilogue
	lw      $ra, 4($sp)
	lw      $fp, 0($sp)
	addiu   $sp, $sp, 24
	jr      $ra



testcase_dump_bytes:
	# standard prologue
	addiu   $sp, $sp, -24
	sw      $fp, 0($sp)
	sw      $ra, 4($sp)
	addiu   $fp, $sp, 20

	# save the sX registers
	addiu   $sp $sp,-4
	sw      $s0, 0($sp)

	# s0 will save the address of the Turtle object
	add     $s0, $a0,$zero

	# print out the 4 bytes before the struct (to check for underruns)
	lw      $a0, -4($s0)
	jal     printHex

	# print out the 12 bytes of the struct.  But we'll not print the
	# 'name' field directly (since the address of the string is
	# unpredictable); instead, we'll print out the first 4 bytes of
	# the pointed-to string

	lb      $a0, 0($s0)
	addi    $a1, $zero,2
	jal     printHex_size

	lb      $a0, 1($s0)
	addi    $a1, $zero,2
	jal     printHex_size

	lb      $a0, 2($s0)
	addi    $a1, $zero,2
	jal     printHex_size

	lb      $a0, 3($s0)
	addi    $a1, $zero,2
	jal     printHex_size

	lw      $a0, 4($s0)
	lb      $a0, 0($a0)
	addi    $a1, $zero,2
	jal     printHex_size

	lw      $a0, 4($s0)
	lb      $a0, 1($a0)
	addi    $a1, $zero,2
	jal     printHex_size

	lw      $a0, 4($s0)
	lb      $a0, 2($a0)
	addi    $a1, $zero,2
	jal     printHex_size

	lw      $a0, 4($s0)
	lb      $a0, 3($a0)
	addi    $a1, $zero,2
	jal     printHex_size

	lb      $a0, 8($s0)
	addi    $a1, $zero,2
	jal     printHex_size

	lb      $a0, 9($s0)
	addi    $a1, $zero,2
	jal     printHex_size

	lb      $a0, 10($s0)
	addi    $a1, $zero,2
	jal     printHex_size

	lb      $a0, 11($s0)
	addi    $a1, $zero,2
	jal     printHex_size

	# print out the 4 bytes after the struct (to check for overruns)
	lw      $a0, 12($s0)
	jal     printHex

	# print_char('\n')
	addi    $v0, $zero,11
	addi    $a0, $zero,'\n'
	syscall

	# restore the sX registers
	lw      $s0, 0($sp)
	addiu   $sp $sp,4

	# standard epilogue
	lw      $ra, 4($sp)
	lw      $fp, 0($sp)
	addiu   $sp, $sp, 24
	jr      $ra



# void printHex(int val)
# {
#     printHex_recurse(val, 8);
#     printf("\n");
# }
printHex:
	# standard prologue
	addiu  $sp, $sp, -24
	sw     $fp, 0($sp)
	sw     $ra, 4($sp)
	addiu  $fp, $sp, 20

	# printHex(val, 8)
	addi   $a1, $zero, 8
	jal    printHex_recurse

	addi   $v0, $zero, 11      # print_char('\n')
	addi   $a0, $zero, 0xa
	syscall

	# standard epilogue
	lw     $ra, 4($sp)
	lw     $fp, 0($sp)
	addiu  $sp, $sp, 24
	jr     $ra



# void printHex_size(int val, int digits)
# {
#     printHex_recurse(val, digits);
#     printf("\n");
# }
printHex_size:
	# standard prologue
	addiu  $sp, $sp, -24
	sw     $fp, 0($sp)
	sw     $ra, 4($sp)
	addiu  $fp, $sp, 20

	# printHex_recurse(val, digits)
	jal    printHex_recurse

	addi   $v0, $zero, 11      # print_char('\n')
	addi   $a0, $zero, 0xa
	syscall

	# standard epilogue
	lw     $ra, 4($sp)
	lw     $fp, 0($sp)
	addiu  $sp, $sp, 24
	jr     $ra



printHex_recurse:
	# standard prologue
	addiu  $sp, $sp, -24
	sw     $fp, 0($sp)
	sw     $ra, 4($sp)
	addiu  $fp, $sp, 20

	# if (len == 0) return;    // base case (NOP)
	beq    $a1, $zero, printHex_recurse_DONE

	# recurse first, before we print this character.
	#
	# The reason for this is because the easiest way to break up
	# a long integer is using a small shift and modulo; so *this*
	# call will be responsible for the *last* hex digit, and we'll
	# use recursion to handle the things which come *before* it.
	#
	# As we've seen just above, if the current len==1, then the
	# recursive call will be the base case, and a NOP.

	# of course, we have to save a0 before we recurse.  We do *NOT*
	# need to save a1, since we'll never need it again.
	sw     $a0, 8($sp)

	# printHex_recurse(val >> 4, len-1)
	srl    $a0, $a0,4
	addi   $a1, $a1,-1
	jal    printHex_recurse

	# restore a0
	lw     $a0, 8($sp)

	# the value we will print is (val & 0xf), interpreted as hex.
	andi   $t0, $a0,0x0f      # digit = (val & 0xf)

	slti   $t1, $t0,10        # t1 = (digit < 10)
	beq    $t1, $zero, printHex_recurse_LETTER

	# if we get here, then $t0 contains an integer from 0 to 9, inclusive.
	addi   $v0, $zero, 11     # print_char(digit+'0')
	addi   $a0, $t0, '0'
	syscall

	j      printHex_recurse_DONE

printHex_recurse_LETTER:
	# if we get here, then $t0 contains an integer from 10 to 15, inclusive.
	# convert to the equivalent letter.
	addi   $t0, $t0,-10        # digit -= 10

	addi   $v0, $zero, 11     # print_char(digit+'a')
	addi   $a0, $t0, 'a'
	syscall

	# intentional fall-through to the epilogue

printHex_recurse_DONE:
	# standard epilogue
	lw     $ra, 4($sp)
	lw     $fp, 0($sp)
	addiu  $sp, $sp, 24
	jr     $ra



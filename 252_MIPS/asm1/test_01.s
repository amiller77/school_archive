# test_01.s
#
# A testcase for Asm 1.

.data

.globl median
median:
	.word	1

.globl absVal
absVal:
	.word	1

.globl sum
sum:
	.word	1

.globl rotate
rotate:
	.word	1

.globl dump
dump:
	.word	1

.globl one
one:
	.word	123

.globl two
two:
	.word	-456

.globl three
three:
	.word	789



# ----------- main() -----------
.text


.globl	main
main:
	# call the student code
	jal	studentMain


.data
MAIN_MSG:
	.asciiz	"Dumping out all of the variables, after the student code ran:\n"
.text

	# print_str(MSG)
	addi	$v0, $zero,4
	la	$a0, MAIN_MSG
	syscall

	# print_int(median)
	addi	$v0, $zero,1
	la	$a0, median
	lw	$a0, 0($a0)
	syscall

	# print_chr('\n')
	addi	$v0, $zero,11
	addi	$a0, $zero,0xa
	syscall

	# print_int(absVal)
	addi	$v0, $zero,1
	la	$a0, absVal
	lw	$a0, 0($a0)
	syscall

	# print_chr('\n')
	addi	$v0, $zero,11
	addi	$a0, $zero,0xa
	syscall

	# print_int(sum)
	addi	$v0, $zero,1
	la	$a0, sum
	lw	$a0, 0($a0)
	syscall

	# print_chr('\n')
	addi	$v0, $zero,11
	addi	$a0, $zero,0xa
	syscall

	# print_int(rotate)
	addi	$v0, $zero,1
	la	$a0, rotate
	lw	$a0, 0($a0)
	syscall

	# print_chr('\n')
	addi	$v0, $zero,11
	addi	$a0, $zero,0xa
	syscall

	# print_int(dump)
	addi	$v0, $zero,1
	la	$a0, dump
	lw	$a0, 0($a0)
	syscall

	# print_chr('\n')
	addi	$v0, $zero,11
	addi	$a0, $zero,0xa
	syscall

	# print_int(one)
	addi	$v0, $zero,1
	la	$a0, one
	lw	$a0, 0($a0)
	syscall

	# print_chr('\n')
	addi	$v0, $zero,11
	addi	$a0, $zero,0xa
	syscall

	# print_int(two)
	addi	$v0, $zero,1
	la	$a0, two
	lw	$a0, 0($a0)
	syscall

	# print_chr('\n')
	addi	$v0, $zero,11
	addi	$a0, $zero,0xa
	syscall

	# print_int(three)
	addi	$v0, $zero,1
	la	$a0, three
	lw	$a0, 0($a0)
	syscall

	# print_chr('\n')
	addi	$v0, $zero,11
	addi	$a0, $zero,0xa
	syscall


	# sys_exit()
	addi	$v0, $zero,10
	syscall


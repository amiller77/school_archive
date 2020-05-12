Alexander Miller
README.txt

	I choose to implement lb, andi, and bne, 3 I-format instructions:
lb	$t0, 0($s0)
andi	$t0, $s0, 5
bne	$s1, $s2, AFTER


bne:
	I used extra1 to represent a “branchNot” control bit; setting “branchNot” to 1 is used to negate zero alu output. The branch logic is exactly the same except now it only operates if alu inputs are not equal.

andi:
	I used extra3 to represent a “zero-extend” control bit, which “prevents” sign extension of the 16-bit imm. Since extract instruction fields occurs before this control bit is set, I “undo” the sign extension in getAluInput2 by using the 16-bit field instead; in real hardware, we would probably need to move the sign-extend to the EX phase, and then use a mux to zero-extend or sign-extend.

lb:
	I used extra2 to represent a “readByte” control bit; setting “readByte” to 1 caused execute_MEM to reduce the loaded word to the proper byte using masking. This was a bit annoying because, since it is actually stored in a 32-bit integer, we have to sign extend the 8-bit value to 32 bits after shifting and masking the desired bit range.
	
extra1 -> nor
extra2 -> lui
extra3 -> andi and ori
Caspardavidfriedrich3#
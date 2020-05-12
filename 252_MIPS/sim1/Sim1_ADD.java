/* Simulates a physical device that performs (signed) addition on
 * a 32-bit input.
 *
 * Author: Alexander Miller 
 */

public class Sim1_ADD {
	
	// INPUT 
	public RussWire[] a;
	public RussWire[] b;

	// OUTPUT
	public RussWire[] sum;
	public RussWire carryOut;
	public RussWire overflow;
	
	// CONSTRUCTOR
	public Sim1_ADD() {
		a   = new RussWire[32];
		b   = new RussWire[32];
		sum = new RussWire[32];

		for (int i=0; i<32; i++) {
			a  [i] = new RussWire();
			b  [i] = new RussWire();
			sum[i] = new RussWire();
		}

		carryOut = new RussWire();
		overflow = new RussWire();
	}
	
	// EXECUTE
	public void execute() {
		boolean carryBit = false;
		// enclosing loop:
		for (int i = 0; i < 32; i++) {
			boolean firstVal = a[i].get();
			boolean secondVal = b[i].get();
			// solve sum and carry value:
			
			// carry, first, second
			// 1, 1, 1 -> carry bit 1, sum 1
			if (carryBit && firstVal && secondVal) {
				carryBit = true;
				sum[i].set(true);
			} // 0, 0, 0 -> carry bit 0, sum 0
			else if (!carryBit && !firstVal && !secondVal) {
				carryBit = false;
				sum[i].set(false);
			} // 1, 1, 0 // 1, 0, 1 // 0, 1, 1 -> carry bit 1, sum 0
			else if ( (carryBit && firstVal && !secondVal) ||
					(carryBit && !firstVal && secondVal) ||
					(!carryBit && firstVal && secondVal) ) {
				carryBit = true;
				sum[i].set(false);
			} // 1, 0, 0 // 0, 1, 0 // 0, 0, 1 -> carry bit 0, sum 1
			else if ( (carryBit && !firstVal && !secondVal) ||
					(!carryBit && firstVal && !secondVal) ||
					(!carryBit && !firstVal && secondVal) ) {
				carryBit = false;
				sum[i].set(true);
			}	
		}
		
		// update carry-out:
		carryOut.set(carryBit);
		
		// perform overflow check:
		boolean sumSign = sum[31].get();
		// make sure it makes sense with sign bit of operands
		boolean signA = a[31].get();
		boolean signB = b[31].get();
		// check sign bits [true -> negative, else positive]
		// if A negative and B negative and sum positive -> overflow
		// if A positive and B positive and sum negative -> overflow
		if ( (signA && signB && !sumSign) || (!signA && !signB && sumSign) ) {
			overflow.set(true);
		} else {
			overflow.set(false);
		}
		
	}


}



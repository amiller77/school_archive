
public class Sim3_ALU {

	// INPUTS
	public RussWire[] aluOp;
	public RussWire bNegate;
	public RussWire[] a;
	public RussWire[] b;
	
	// HIDDEN MECHANICS
	private Sim3_ALUElement[] aluElements;
	private int size;
	private RussWire[] carryWires;
	
	// OUTPUTS
	public RussWire[] result;
	
	// CONSTRUCTOR
	public Sim3_ALU (int size) {
		this.size = size;
		// initialize inputs:
		aluOp = new RussWire[3];
		aluOp[0] = new RussWire();
		aluOp[1] = new RussWire();
		aluOp[2] = new RussWire();
		bNegate = new RussWire();
		
		// initialize arrays:
		a = new RussWire[size];
		b = new RussWire[size];
		result = new RussWire[size];
		aluElements = new Sim3_ALUElement[size];
		// (carryWires is one larger to hold the carry out of last op)
		carryWires = new RussWire[size+1];
		for (int i = 0; i<size; i++) {
			a[i] = new RussWire();
			b[i] = new RussWire();
			result[i] = new RussWire();
			aluElements[i] = new Sim3_ALUElement();
			carryWires[i] = new RussWire();
		}
		carryWires[size] = new RussWire();
	}
	
	// EXECUTE
	public void execute() {
		// first carry is bNegate
		carryWires[0].set(bNegate.get());
		// iterate over every ALUElement for first pass:
		for (int i = 0; i<this.size;i++) {
			Sim3_ALUElement aluE_i = aluElements[i];
			// set inputs
			aluE_i.a.set(this.a[i].get());
			aluE_i.b.set(this.b[i].get());
			aluE_i.carryIn.set(carryWires[i].get());
			aluE_i.aluOp[0].set(this.aluOp[0].get());
			aluE_i.aluOp[1].set(this.aluOp[1].get());
			aluE_i.aluOp[2].set(this.aluOp[2].get());
			aluE_i.bInvert.set(this.bNegate.get());
			// run pass 1
			aluE_i.execute_pass1();
			// update carryIn for next round
			carryWires[i+1].set(aluE_i.carryOut.get());
		}
		// plug last element's addResult as "less" of first element
		aluElements[0].less.set(aluElements[this.size-1].addResult.get());
		// run first element:
		aluElements[0].execute_pass2();
		// map output 
		this.result[0].set(aluElements[0].result.get());
		// now do the same idea for the rest, but with less set to 0
		for (int i = 1; i<this.size;i++) {
			Sim3_ALUElement aluE_i = aluElements[i];
			aluE_i.less.set(false);
			aluE_i.execute_pass2();
			this.result[i].set(aluE_i.result.get());
		}
	}
	
}

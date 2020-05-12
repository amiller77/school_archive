
public class Sim3_ALUElement {

	// INPUTS
	public RussWire aluOp[];
	public RussWire bInvert;
	public RussWire a,b;
	public RussWire carryIn;
	public RussWire less;
	
	// HIDDEN MECHANICS
	private AND and;
	private OR or;
	private FullAdder add;
	private Sim3_MUX_8by1 bMUX;
	private Sim3_MUX_8by1 opMUX;
	private XOR xor;
	
	// OUTPUTS
	public RussWire result;
	public RussWire addResult;
	public RussWire carryOut;
	
	// CONSTRUCTOR
	public Sim3_ALUElement () {
		// initialize inputs:
		bInvert= new RussWire();
		a = new RussWire();
		b = new RussWire();
		carryIn = new RussWire();
		less = new RussWire();
		aluOp = new RussWire[3];
		aluOp[0] = new RussWire();
		aluOp[1] = new RussWire();
		aluOp[2] = new RussWire();
		
		// initialize outputs:
		result = new RussWire();
		addResult = new RussWire();
		carryOut = new RussWire();
		
		// initialize hidden mechanics
		and = new AND();
		or = new OR();
		xor = new XOR();
		add = new FullAdder();
		bMUX = new Sim3_MUX_8by1();
		opMUX = new Sim3_MUX_8by1();
	}
	
	// EXECUTE PASS 1
	// pre-conditions: all inputs set except for less
	// post-conditions: addResult and carryOut set
	public void execute_pass1() {
		// AND
		and.a.set(this.a.get());
		and.b.set(this.b.get());
		and.execute();
		
		// OR
		or.a.set(this.a.get());
		or.b.set(this.b.get());
		or.execute();
		
		// XOR
		xor.a.set(this.a.get());
		xor.b.set(this.b.get());
		xor.execute();
		
		// BINVERT
		bMUX.control[2].set(false); // always off
		bMUX.control[1].set(false); // always off
		bMUX.control[0].set(bInvert.get()); // 0 or 1
		bMUX.in[0].set(this.b.get());	//	0 -> b
		bMUX.in[1].set(!this.b.get());	// 1 -> ~b
		for (int i = 2; i<8; i++) {
			bMUX.in[i].set(false);
		}
		bMUX.execute();
		
		// ADD
		add.a.set(this.a.get());
		add.b.set(this.bMUX.out.get());
		add.carryIn.set(this.carryIn.get());
		add.execute();

		// map ADD result out
		this.addResult.set(add.sum.get());
		this.carryOut.set(add.carryOut.get());
	}
	
	// EXECUTE PASS 2
	// pre-conditions: all inputs, including yes, are valid
	// post-conditions: generate result output
	public void execute_pass2() {
		
		// initialize OP MUX
		opMUX.control[0].set(aluOp[0].get());
		opMUX.control[1].set(aluOp[1].get());
		opMUX.control[2].set(aluOp[2].get());
		opMUX.in[0].set(and.out.get());
		opMUX.in[1].set(or.out.get());
		opMUX.in[2].set(add.sum.get());
		opMUX.in[3].set(less.get());
		opMUX.in[4].set(xor.out.get());
		opMUX.in[5].set(false);
		opMUX.in[6].set(false);
		opMUX.in[7].set(false);
		// execute
		opMUX.execute();
		// report out
		result.set(opMUX.out.get());
		
	}
	
	
}

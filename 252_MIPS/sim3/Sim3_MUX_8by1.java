
public class Sim3_MUX_8by1 {

	// INPUTS
	public RussWire[] control;
	public RussWire[] in;
	
	// HIDDEN MECHANICS
	private FourInputAND ANDarray[];
	private EightInputOR massiveOR;
	
	// OUTPUTS
	public RussWire out;
	
	// CONSTRUCTOR
	public Sim3_MUX_8by1 () {
		massiveOR = new EightInputOR();
		out = new RussWire();
		
		control = new RussWire[3];
		for (int i = 0; i<3; i++) {
			control[i] = new RussWire();
		}
		
		in = new RussWire[8];
		ANDarray = new FourInputAND[8];
		for (int i = 0; i<8; i++) {
			in[i] = new RussWire();
			ANDarray[i] = new FourInputAND();
		}

	}
	
	// EXECUTE
	// pre-condition: IN set to meaningful input
	public void execute() {
		
		// AND0: !c[0], !c[1], !c[2], in[0]
		// set inputs
		ANDarray[0].a.set(!control[0].get());
		ANDarray[0].b.set(!control[1].get());
		ANDarray[0].c.set(!control[2].get());
		ANDarray[0].d.set(in[0].get());
		// execute
		ANDarray[0].execute();
		// map to OR
		massiveOR.in[0].set(ANDarray[0].out.get());
		
		// AND1: c[0], !c[1], !c[2], in[1]
		// set inputs
		ANDarray[1].a.set(control[0].get());
		ANDarray[1].b.set(!control[1].get());
		ANDarray[1].c.set(!control[2].get());
		ANDarray[1].d.set(in[1].get());
		// execute
		ANDarray[1].execute();
		// map to OR
		massiveOR.in[1].set(ANDarray[1].out.get());
		
		// AND2: !c[0], c[1], !c[2], in[2]
		// set inputs
		ANDarray[2].a.set(!control[0].get());
		ANDarray[2].b.set(control[1].get());
		ANDarray[2].c.set(!control[2].get());
		ANDarray[2].d.set(in[2].get());
		// execute
		ANDarray[2].execute();
		// map to OR
		massiveOR.in[2].set(ANDarray[2].out.get());
		
		// AND3: c[0], c[1], !c[2], in[3]
		// set inputs
		ANDarray[3].a.set(control[0].get());
		ANDarray[3].b.set(control[1].get());
		ANDarray[3].c.set(!control[2].get());
		ANDarray[3].d.set(in[3].get());
		// execute
		ANDarray[3].execute();
		// map to OR
		massiveOR.in[3].set(ANDarray[3].out.get());
		
		// AND4: !c[0], !c[1], c[2], in[4]
		// set inputs
		ANDarray[4].a.set(!control[0].get());
		ANDarray[4].b.set(!control[1].get());
		ANDarray[4].c.set(control[2].get());
		ANDarray[4].d.set(in[4].get());
		// execute
		ANDarray[4].execute();
		// map to OR
		massiveOR.in[4].set(ANDarray[4].out.get());
		
		// AND5: c[0], !c[1], c[2], in[5]
		// set inputs
		ANDarray[5].a.set(control[0].get());
		ANDarray[5].b.set(!control[1].get());
		ANDarray[5].c.set(control[2].get());
		ANDarray[5].d.set(in[5].get());
		// execute
		ANDarray[5].execute();
		// map to OR
		massiveOR.in[5].set(ANDarray[5].out.get());
		
		// AND6: !c[0], c[1], c[2], in[6]
		// set inputs
		ANDarray[6].a.set(!control[0].get());
		ANDarray[6].b.set(control[1].get());
		ANDarray[6].c.set(control[2].get());
		ANDarray[6].d.set(in[6].get());
		// execute
		ANDarray[6].execute();
		// map to OR
		massiveOR.in[6].set(ANDarray[6].out.get());
		
		// AND7: c[0], c[1], c[2], in[7]
		// set inputs
		ANDarray[7].a.set(control[0].get());
		ANDarray[7].b.set(control[1].get());
		ANDarray[7].c.set(control[2].get());
		ANDarray[7].d.set(in[7].get());
		// execute
		ANDarray[7].execute();
		// map to OR
		massiveOR.in[7].set(ANDarray[7].out.get());
		
		// RUN OR
		massiveOR.execute();
		// map output to out
		this.out.set(massiveOR.out.get());

	}
	
	// *************** INSIDE CLASS: FOUR INPUT AND ****************
	private class FourInputAND {
		
		// INPUTS
		public RussWire a,b,c,d;
		
		// HIDDEN MECHANICS
		private AND A;
		private AND B;
		private AND C;
		
		// OUTPUTS
		public RussWire out;
		
		// CONSTRUCTOR
		public FourInputAND() {
			a = new RussWire();
			b = new RussWire();
			c = new RussWire();
			d = new RussWire();
			A = new AND();
			B = new AND();
			C = new AND();
			out = new RussWire();
		}
		
		// EXECUTE
		// pre-conditions: a-d have been set
		public void execute() {
			// plug inputs into A
			A.a.set(this.a.get());
			A.b.set(this.b.get());
			// run A
			A.execute();
			
			// plug inputs into B
			B.a.set(this.c.get());
			B.b.set(this.d.get());
			// run B
			B.execute();
			
			// plug outputs from A,B into C
			C.a.set(A.out.get());
			C.b.set(B.out.get());
			// run C
			C.execute();
			
			// set the out
			this.out.set(C.out.get());
		}
	}
	
	// *************** INSIDE CLASS: EIGHT INPUT OR ******************
	private class EightInputOR {
		
		// INPUTS
		public RussWire in[];
		
		// HIDDEN MECHANICS
		private OR A1, A2, A3, A4, B1, B2, C1;
		
		// OUTPUTS
		public RussWire out;
		
		// CONSTRUCTOR
		public EightInputOR() {
			in = new RussWire[8];
			for (int i = 0; i<8;i++) {
				in[i] = new RussWire();
			}
			A1 = new OR();
			A2 = new OR();
			A3 = new OR();
			A4 = new OR();
			B1 = new OR();
			B2 = new OR();
			C1 = new OR();
			out = new RussWire();
		}
		
		// EXECUTE
		// pre-conditions: IN has been set with meaningful inputs
		public void execute() {
			// set inputs to A1
			A1.a.set(in[0].get());
			A1.b.set(in[1].get());
			// run A1
			A1.execute();
			
			// set inputs to A2
			A2.a.set(in[2].get());
			A2.b.set(in[3].get());
			// run A2
			A2.execute();
			
			// set inputs to A3
			A3.a.set(in[4].get());
			A3.b.set(in[5].get());
			// run A3
			A3.execute();
			
			// set inputs to A4
			A4.a.set(in[6].get());
			A4.b.set(in[7].get());
			// run A3
			A4.execute();
			
			// set inputs to B1
			B1.a.set(A1.out.get());
			B1.b.set(A2.out.get());
			// run B1
			B1.execute();
			
			// set inputs to B2
			B2.a.set(A3.out.get());
			B2.b.set(A4.out.get());
			// run B2
			B2.execute();
			
			// set inputs to C1
			C1.a.set(B1.out.get());
			C1.b.set(B2.out.get());
			// run C1
			C1.execute();
			
			// map C1.out to this.out
			this.out.set(C1.out.get());
		}
	}
	
}


public interface Proj06_TuringMachine
{
	void addState(String stateName, boolean accept);

	void addTransition(String from, char read,
	                   char write, boolean moveDir, String to);

	void run(String startString, boolean debug);
}

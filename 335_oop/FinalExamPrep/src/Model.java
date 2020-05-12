import java.io.Serializable;

public class Model implements Serializable{
	public int[] data;
	public static final long serialVersionUID = 1;
	
	public Model() {
		data = new int[10];
	}
	
	public void setup() {
		for (int i = 0; i<10; i++) {
			data[i] = i;
		}
	}

}

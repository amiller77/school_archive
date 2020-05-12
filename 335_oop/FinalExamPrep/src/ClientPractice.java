import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientPractice extends Thread{
	public Socket s;
	
	public void run() {
		try {
			System.out.println("pre-connection client");
			s = new Socket("localhost",3000);
			System.out.println("post-connection client");
			ObjectOutputStream in = new ObjectOutputStream(s.getOutputStream());
			Model m = new Model();
			m.setup();
			in.writeObject(m);
			in.close();
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}

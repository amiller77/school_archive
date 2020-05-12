import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerPractice extends Thread {
	public ServerSocket ss;
	
	public void run() {
		try {
			ss = new ServerSocket(3000);
			System.out.println("pre-connection server");
			Socket connection = ss.accept();
			System.out.println("post-connection server");
			ObjectInputStream input = new ObjectInputStream(connection.getInputStream());
			Model m = (Model) input.readObject();
			input.close();
			PrintWriter out = new PrintWriter(new File("data"));
			for (int i = 0; i< m.data.length; i++ ) {
				System.out.print(m.data[i]);
				out.print(m.data[i]);
			}
			out.close();
			connection.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
}

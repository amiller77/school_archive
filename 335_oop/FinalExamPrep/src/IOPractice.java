import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringReader;

public class IOPractice {
	
	
	// FILE STREAM
	public void fileStreamOutPractice() {
		Model model = new Model();
		File file = new File("data");
		// write to file
		try {
			DataOutputStream fileOut = new DataOutputStream(new FileOutputStream(file));
			for (int i = 0; i<model.data.length; i++) {
				System.out.print(model.data[i]);
				fileOut.writeInt(model.data[i]);
			}
			fileOut.flush();
			fileOut.close();
		} catch (IOException e){
			System.out.println("error");
			e.printStackTrace();
		}
		
	}
	
	public void fileStreamInPractice() {
		try {
			DataInputStream fileIn = new DataInputStream(new FileInputStream("data"));
			boolean flag = true;
			while(flag) {
				int value = fileIn.readChar();
				if (value == -1) {
					return;
				} else {
					System.out.println(value);
				}
			}
			fileIn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void redirectingOut() {
		try {
			File file = new File("data");
			System.setOut(new PrintStream(new FileOutputStream(file)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Model model = new Model();
		for (int i = 0; i<model.data.length; i++) {
			System.out.print(model.data[i]);
		}

	}
	
	public void serializationPractice() {
		Model model = new Model();
		model.setup();
		// write:
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data"));	
			oos.writeObject(model);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Model newModel = new Model();
		// read:
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("data"));
			newModel = (Model) ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for (int i = 0; i< newModel.data.length; i++) {
			System.out.println(newModel.data[i]);
		}
		
	}
	
}

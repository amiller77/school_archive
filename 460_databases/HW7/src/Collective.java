/*
 * File: Collective.java
 * Author: Alexander Miller
 * Description: Text-based program to allow user to query database
 * 		Validates user input and implements DB constraints
 */

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.sql.*;

public class Collective {

	// MAIN
	// handles main control loop
	public static void main(String[] args) {
		// flag to run debug code to create tables:
		if (args.length > 0 && args[0].equals("DEBUG")) {
			debugDropTables();
			debugCreateTables();
		}
		Scanner keybd = new Scanner(System.in);
		while (true) {
			printMainMenu();
			String cmd;
			try {
				cmd = keybd.nextLine();
			} catch (Exception x) {
				break;
			}
			 
			if (cmd.equals("1")) {
				processOne(keybd);
			} else if (cmd.equals("2")) {
				processTwo(keybd);
			} else if (cmd.equals("3")) {
				processThree(keybd);
			} else if (cmd.equals("4")) {
				processFour(keybd);
			} else if (cmd.equals("5")) {
				break;
			}
		}
		keybd.close();
		// flag to clean up any debug changes to DB
		if (args.length > 0 && args[0].equals("DEBUG")) {
			debugDropTables();
		}
		return;
	}
	
	// FOR TESTING...
	public static void mainB(String[] args) {
	}
	
	// DEBUG DROP TABLES
	// performs table drops
	private static void debugDropTables() {
		DBData iPoint = new DBData(); // stores errors
		String query; // query string
		int type = 2; // indicate DB "update"
		
		query = "DROP TABLE SubscribesTo";
		queryDatabase(query,type,iPoint);
		query = "DROP TABLE ChannelSelection";
		queryDatabase(query,type,iPoint);
		query = "DROP TABLE Channel";
		queryDatabase(query,type,iPoint);
		query = "DROP TABLE DependentAccount";
		queryDatabase(query,type,iPoint);
		query = "DROP TABLE MediaService";
		queryDatabase(query,type,iPoint);
		query = "DROP TABLE Device";
		queryDatabase(query,type,iPoint);
		query = "DROP TABLE PrimaryAccount";
		queryDatabase(query,type,iPoint);
		query = "DROP TABLE Account";
		queryDatabase(query,type,iPoint);
	}
	
	// DEBUG_CREATE_TABLES
	// performs table creations
	private static void debugCreateTables() {
		DBData iPoint = new DBData(); // stores errors
		String query; // query string
		int type = 2; // indicate DB "update"
		
		query = "CREATE TABLE Account (\n" + 
				"	AccountNumber NUMBER(10),\n" + 
				"	Name VARCHAR(255),\n" + 
				"	DateOfBirth DATE,\n" + 
				"	PRIMARY KEY(AccountNumber)\n" + 
				")";
		queryDatabase(query,type,iPoint);
		
		query = "CREATE TABLE PrimaryAccount (\n" + 
				"	AccountNumber NUMBER(10),\n" + 
				"	StreetNumber VARCHAR(255),\n" + 
				"	Address VARCHAR(255),\n" + 
				"	ZipCode INTEGER,\n" + 
				"	PRIMARY KEY(AccountNumber),\n" + 
				"	FOREIGN KEY(AccountNumber) REFERENCES Account\n" + 
				")";
		queryDatabase(query,type,iPoint);
		
		
		query = "CREATE TABLE Device (\n" + 
				"	SerialNumber NUMBER(10),\n" + 
				"	PRIMARY KEY(SerialNumber)\n" + 
				")";
		queryDatabase(query,type,iPoint);
		
		query = "CREATE TABLE MediaService (\n" + 
				"	ServiceName VARCHAR(255),\n" + 
				"	MonthlyFee NUMBER(3,2),\n" + 
				"	isMusicService CHAR(1) CHECK (isMusicService in ('F','T')),\n" + 
				"	isVideoService CHAR(1) check (isVideoService in ('F','T')),\n" + 
				"	PRIMARY KEY(ServiceName)\n" + 
				")";
		queryDatabase(query,type,iPoint);
		
		query = "CREATE TABLE SubscribesTo (\n" + 
				"	AccountNumber NUMBER(10),\n" + 
				"	SerialNumber NUMBER(10),\n" + 
				"	ServiceName VARCHAR(255),\n" + 
				"	PRIMARY KEY (AccountNumber, SerialNumber, ServiceName),\n" + 
				"	FOREIGN KEY (AccountNumber) REFERENCES Account,\n" + 
				"	FOREIGN KEY (SerialNumber) REFERENCES Device,\n" + 
				"	FOREIGN KEY (ServiceName) REFERENCES MediaService\n" + 
				")";
		queryDatabase(query,type,iPoint);
		
		//need to create some devices, accounts and subscriptions to test
		query="INSERT INTO Account VALUES(1234,'John','12-DEC-94')";
		queryDatabase(query,type,iPoint);
		query="INSERT INTO PrimaryAccount VALUES(1234,'4','Chestnut Lane',85711)";
		queryDatabase(query,type,iPoint);
		query="INSERT INTO Account VALUES(2345,'Jane','04-FEB-86')";
		queryDatabase(query,type,iPoint);
		query="INSERT INTO PrimaryAccount VALUES(2345,'8','Ashmead Place',85711)";
		queryDatabase(query,type,iPoint);
		
		query="INSERT INTO Device VALUES(123456789)";
		queryDatabase(query,type,iPoint);
		query="INSERT INTO Device VALUES(234567890)";
		queryDatabase(query,type,iPoint);
		
		query="INSERT INTO MediaService VALUES('ABC',4.20,'T','F')";
		queryDatabase(query,type,iPoint);
		query="INSERT INTO MediaService VALUES('pandora',8.75,'T','F')";
		queryDatabase(query,type,iPoint);
		query="INSERT INTO MediaService VALUES('youtube',4.99,'T','F')";
		queryDatabase(query,type,iPoint);
		
		query="INSERT INTO SubscribesTo VALUES(1234,123456789,'ABC')";
		queryDatabase(query,type,iPoint);
		query="INSERT INTO SubscribesTo VALUES(2345,234567890,'pandora')";
		queryDatabase(query,type,iPoint);
	}
	
	// PRINT MAIN MENU
	// prints out the main menu
	private static void printMainMenu() {
		System.out.print(
			"1) Enter primary account information\n"
			+ "2) List all the primary accounts in a zip code\n"
			+ "3) Subscribe to a music service\n"
			+ "4) List music service subscriptions\n"
			+ "5) Quit\n"
			+ "Enter 1-4 or 5 to quit:\n"
		);
	}
	
	// PROCESS ONE
	// allows a user to create an account
	private static void processOne(Scanner keybd) {
		// get and validate acct no
		System.out.println("Enter new account number:");
		Integer accountNumber = captureIntegerInput(keybd);
		if (accountNumber == null) {
			return;
		}

		// get and validate acc name
		System.out.println("Enter name of account holder:");
		String name = captureStringInput(keybd);
		if (name == null) {
			return;
		}
		
		// get and validate date
		System.out.println("Enter the date of birth of account holder (as MM/DD/YYYY):");
		String DOB = getAndValidateDate(keybd);
		if (DOB == null) {
			return;
		}
		DOB = reformatDate(DOB);

		
		// get and validate street number [can contain letters]
		System.out.println("Enter street number:");
		String streetNumber = captureStringInput(keybd);
		if (streetNumber == null) {
			return;
		}
		
		// get and validate address
		System.out.println("Enter address:");
		String address = captureStringInput(keybd);
		if (address == null) {
			return;
		}
		
		// get and validate zipcode
		System.out.println("Enter zip code:");
		Integer zipcode = getAndValidateZipcode(keybd);
		if (zipcode == null) {
			return;
		}
		
		// update database:
		// ... the Account
		DBData iPoint = new DBData(); //stores query errors and results from 
		String query = "INSERT INTO Account "+
				"VALUES("+accountNumber+",'"+name+"','"+DOB+"')";
		queryDatabase(query,2,iPoint);
		// if error has occurred, don't run the second query
		if (iPoint.val==1) {
			System.out.println("Error adding account.");
			return;
		}
		// ... and the PrimaryAccount
		query = "INSERT INTO PrimaryAccount "+
				"VALUES("+accountNumber+",'"+streetNumber+"','"+
				address+"',"+zipcode+")";
		queryDatabase(query,2,iPoint);
		// if no error occurred, print message
		if (iPoint.val==0) {
			System.out.println("Primary account added!");
		} else {
			System.out.println("Primary account already exists!");
		}
		
	}
	
	
	// PROCESS TWO
	// returns results about a zipcode
	private static void processTwo(Scanner keybd) {
		System.out.println("Enter zip code:");
		Integer zipcode = getAndValidateZipcode(keybd);
		if (zipcode == null) {
			return;
		}
		
		DBData iPoint = new DBData(1); // store error msgs
		int type = 1; // indicate query type: query (as opposed to update)
		String query = "SELECT AccountNumber FROM PrimaryAccount "+
				"WHERE ZipCode = "+zipcode;
		queryDatabase(query,type,iPoint);
		// print results:
		System.out.println("The accounts in zipcode "+zipcode+" are");
		// ...... iterate over results ......
		for (int i = 0; i<iPoint.rows.size(); i++) {
			String[] row = iPoint.rows.get(i);
			System.out.println(row[0]);
		}
		
	}
	
	
	// PROCESS THREE
	// allows adding media services to an account
	private static void processThree(Scanner keybd) {
		int type = 1;
		
		// ACCOUNT NUMBER
		System.out.println("Enter account number:");
		Integer accountNumber = captureIntegerInput(keybd);
		if (accountNumber == null) {
			return;
		}
		DBData iPoint = new DBData(1);
		// make sure account exists in system
		String query = "SELECT AccountNumber FROM Account "+
				"WHERE AccountNumber = "+accountNumber;
		queryDatabase(query,type,iPoint);
		if (iPoint.rows.size()==0) {
			System.out.println("Account does not exist!");
			return;
		}
		iPoint.wipeRows(); //clear out row data for iPoint
		
		// DEVICE NUMBER
		// make sure device exists
		System.out.println("Enter device number:");
		Integer deviceNumber = captureIntegerInput(keybd);
		if (deviceNumber == null) {
			return;
		}
		query = "SELECT SerialNumber FROM Device "+
				"WHERE SerialNumber = "+deviceNumber;
		queryDatabase(query,type,iPoint);
		if (iPoint.rows.size()==0) {
			System.out.println("Device doesn't exist!");
			return;
		}
		iPoint.wipeRows(); //clear out row data for iPoint
		
		// MEDIA SERVICES AVAILABLE
		// make sure device doesn't have more than 2 media services associated
		query = "SELECT ServiceName \n"+
				"FROM SubscribesTo \n"+
				"WHERE AccountNumber = "+accountNumber+"\n"+
				"AND SerialNumber = "+deviceNumber;
		queryDatabase(query,type,iPoint);
		if (iPoint.rows.size()>=2) {
			System.out.println("That device already has two media services, the maximum allowed!");
			return;
		}
		iPoint.wipeRows();
		
		// get media services minus results from last one
		query = "SELECT ServiceName FROM MediaService\n"+
				"MINUS \n"+
				"SELECT ServiceName \n"+
				"FROM SubscribesTo \n"+
				"WHERE AccountNumber = "+accountNumber+"\n"+
				"AND SerialNumber = "+deviceNumber;
		queryDatabase(query,type,iPoint);
		
		// print out list of available music services
		String printString = "Music services available:\n";
		int i = 0;
		while (i<iPoint.rows.size()) {
			String[] row = iPoint.rows.get(i);
			String str = row[0];
			int rowNum = i+1;
			printString = printString+rowNum+" "+str+"\n";
			i++;
		}
		printString += "Enter number of music service desired:\n";
		System.out.print(printString);
		
		// process and validate user input
		Integer selectedService = null;
		try {
			selectedService = Integer.parseInt(keybd.nextLine());
		} catch (Exception x) {
			System.out.println("Invalid selection!");
			return;
		}
		if (selectedService < 0 || selectedService > iPoint.rows.size()) {
			System.out.println("Invalid selection!");
			return;
		}
		
		String selectedServiceName = iPoint.rows.get(selectedService-1)[0];
		query = "INSERT INTO SubscribesTo VALUES("+
				accountNumber+","+deviceNumber+",'"+selectedServiceName+"')";
		type = 2;
		iPoint = new DBData(); //get new DBData for update query
		queryDatabase(query,type,iPoint); //update query
		if (iPoint.val == 0) {
			System.out.println("Account "+accountNumber+" successfully "+
					"subscribed to music service "+selectedServiceName+
					" for device "+deviceNumber);
		}
		
	}
	
	
	// PROCESS FOUR
	// prints out all media services in system and metadata
	private static void processFour(Scanner keybd) {
		int type = 1;
		DBData iPoint = new DBData(4);
		String query = "SELECT m.ServiceName, m.MonthlyFee, s.AccountNumber,"+
				" s.SerialNumber \n"+
				"FROM MediaService m LEFT OUTER JOIN SubscribesTo s "+
				"ON m.ServiceName = s.ServiceName \n"+
				"ORDER BY m.ServiceName, s.AccountNumber";
		queryDatabase(query,type,iPoint);
		if (iPoint.rows.size() == 0) {
			System.out.println("None!");
			return;
		}
		System.out.println("Music Service"+"\t"+"Monthly Fee"+"\t"+
				"Account Number"+"\t"+"Device Number");
		String previousService = null;
		String previousAccount = null;
		for (int i = 0; i<iPoint.rows.size(); i++) {
			String[] row = iPoint.rows.get(i);
			String service=row[0];
			String fee = row[1];
			String acct = row[2];
			String device = row[3];
			if (previousService != null && previousService.equals(service)) {
				service = " ";
				fee = " ";
				if (previousAccount != null && previousAccount.equals(acct)) {
					acct = " ";
				}
			}
			if (fee == null) {
				fee="none";
			}
			if (acct == null) {
				acct="none";
			}
			if (device == null) {
				device="none";
			}
			if (fee != null && !fee.equals("none") && !fee.equals(" ")) {
				fee = "$"+fee;
			}

			System.out.println(service+"\t\t"+fee+"\t\t"+acct+"\t\t"+device);	
			previousService = row[0];
			previousAccount = row[2];
		}
	}
	

	
	// ****************************** HELPER FUNCTIONS ***********************
	
	// CAPTURE INTEGER INPUT
	// returns valid integer value or null if not valid
	// only considers positive integers as valid
	private static Integer captureIntegerInput(Scanner keybd) {
		Integer output = null;
		try {
			output = Integer.parseInt(keybd.nextLine());
		} catch (Exception ex) {
			System.out.println("Error: integer provided not valid.");
			return null;
		}
		if (output < 0) {
			System.out.println("Error: integer provided not valid.");
			return null;
		}
		return output;
		
	}
	
	// CAPTURE STRING INPUT
	// returns valid string, or null if invalid
	// only accepts strings up to 255 chars in length
	private static String captureStringInput(Scanner keybd) {
		String output = null;
		try {
			output = keybd.nextLine(); 
		} catch (Exception ex) {
			System.out.println("Error: Please provide valid string.");
			return null;
		}
		if (output.length() > 255) {
			System.out.println("Error: Please limit strings to 255 characters.");
			return null;
		}
		return output;
	}
	
	// GET AND VALIDATE DATE
	// ensures date is in correct format: MM/DD/YYYY
	// returns valid date
	private static String getAndValidateDate(Scanner keybd) {
		char[] DIGITS = {'0','1','2','3','4','5','6','7','8','9'};
		String date = null;
		int iteration = 0;
		if (iteration > 0) {
			System.out.println("Error: Provided date invalid.");
			return null;
		}
		// gather string
		date = captureStringInput(keybd);
		if (date == null) {
			return null;
		}
		// check length
		if (date.length() != 10) {
			System.out.println("Error: Provided date invalid.");
			return null;
		}
		// check all chars of date:
		for (int i = 0; i<10; i++) {
			// check slash indices
			if (i==2 || i== 5) {
				if (date.charAt(i)!='/') {
					System.out.println("Error: Provided date invalid.");
					return null;
				}
			} // ensure all other indices are digits
			else {
				boolean digitFound = false;
				for (int k = 0; k<DIGITS.length;k++) {
					if (date.charAt(i)==DIGITS[k]) {
						digitFound = true;
						break;
					}
				}
				if (digitFound == false) {
					System.out.println("Error: Provided date invalid.");
					return null;
				}
			}
		}
		// make sure that days and months are valid
		Integer month = Integer.parseInt(date.substring(0,2));
		Integer days = Integer.parseInt(date.substring(3,5));
		if (month < 1 || month > 12 || days < 1 || days > 31) {
			System.out.println("Error: Provided date invalid.");
			return null;
		}else if (month == 2 && days > 29) {
			System.out.println("Error: Provided date invalid.");
			return null;
		}
		return date;
	}
	
	// REFORMAT DATE
	// reformats a date to oracle's preferred format
	// input format: MM/DD/YYYY
	// output format: DD-MONTH-YY , where MONTH is 3 letter all caps
	private static String reformatDate(String date) {
		String day = date.substring(3, 5);
		String month = date.substring(0,2);
		String year = date.substring(8);
		if (month.equals("01")) {
			month = "JAN";
		} else if (month.equals("02")) {
			month = "FEB";
		} else if (month.equals("03")) {
			month = "MAR";
		} else if (month.equals("04")) {
			month = "APR";
		} else if (month.equals("05")) {
			month = "MAY";
		} else if (month.equals("06")) {
			month = "JUN";
		} else if (month.equals("07")) {
			month = "JUL";
		} else if (month.equals("08")) {
			month = "AUG";
		} else if (month.equals("09")) {
			month = "SEP";
		} else if (month.equals("10")) {
			month = "OCT";
		} else if (month.equals("11")) {
			month = "NOV";
		} else if (month.equals("12")) {
			month = "DEC";
		}
		String returnString = day+"-"+month+"-"+year;
		return returnString;
	}
	
	// GET AND VALIDATE ZIPCODE
	// returns valid zipcode or null if invalid
	private static Integer getAndValidateZipcode(Scanner keybd) {
		Integer zipcode = captureIntegerInput(keybd);
		if (zipcode == null) {
			return null;
		}
		// must be 5 digits
		if (zipcode > 99999 && zipcode < 10000) {
			System.out.println("Error: Zipcode should have 5 digits.");
			return null;
		} 
		return zipcode;
	}
	
	// QUERY DATABASE
	// Parameters:
	//		query to send to database
	// 		if type == 1, query
	// 		if type == 2, update
	//		DBData iPoint allows you to register whether an error has occurred
	//			iPoint == 0, no error ; iPoint == 1, error
	// 			DBData also stores result set as linked list of string arrays; and has a numcols field
	private static void queryDatabase(String query, int type, DBData iPoint) {
		iPoint.val = 0;
		String user_name = "alexandermiller";
		String password = "Oraclealoha";
		String connect_string = "jdbc:oracle:thin:@aloe.cs.arizona.edu:1521:oracle";
		Connection m_conn;
		Statement s;
		ResultSet rs = null;

          try{
		Class.forName("oracle.jdbc.OracleDriver");  // Registers drivers

		m_conn = DriverManager.getConnection(connect_string,user_name,password);  //get a connection
		if (m_conn == null) throw new Exception("getConnection failed");
		try {
		    m_conn.setAutoCommit(true);//optional, but it sets auto commit to true
		    s = m_conn.createStatement(); //create a statement
		    if (s == null) throw new Exception("createStatement failed");
		    
		    // MY CODE:
		    if (type == 2) {
		    	s.executeUpdate(query);
		    } else {
		    	rs = s.executeQuery(query);
		    	try {
		    		while (rs.next()) {
		    			// iterate over row to extract information
		    			String[] row = new String[iPoint.numCols];
		    			// for some reason first string is index "1"
		    			for (int i = 1; i<=iPoint.numCols; i++) {
		    				String str = rs.getString(i);
		    				row[i-1] = str;
		    			}
		    			iPoint.rows.add(row);
		    		}
		    	} catch (Exception x) {
		    		x.printStackTrace();
		    	}
		    	
		    }
		    m_conn.commit();
		    m_conn.close();
		        
		} finally {
		    if (m_conn != null) m_conn.close();
		}
	    } catch (Exception e) {
	    	e.printStackTrace();
			iPoint.val = 1;
		}
	}
	
	// DBDATA
	// stores information on errors encountered  by DB,
	// results from DB, and the number of columns in the rows
	private static class DBData {
		// val: did an error occur?
		public int val;
		// how many columns do our rows have?
		public int numCols;
		// store rows as linked list or arrays of size numCols
		public LinkedList<String[]> rows;
		
		public DBData() {
			val = 0;
			numCols = 0;
			rows = null;
		}
		
		public DBData(int numCols) {
			val = 0;
			this.numCols = numCols;
			rows = new LinkedList<String[]>();
		}
		
		public void wipeRows() {
			rows = new LinkedList<String[]>();
		}
		
	}
	
}

import java.io.*;
import java.sql.*;

class JDBCDemo{
    public static void main(String []args)
    {
    	if(args.length < 2){
    		System.err.println("Usage: java JDBCDemo user_name password");
    		System.exit(2);
    	}

    	String user_name = args[0];
    	String password = args[1];
	Statement s;

    	System.out.println("Begin JDBCDemo\n");

          try{
		Class.forName("oracle.jdbc.OracleDriver");  // Registers drivers

		m_conn = DriverManager.getConnection(connect_string,user_name,password);  //get a connection
		if (m_conn == null) throw new Exception("getConnection failed");
		try {
		    m_conn.setAutoCommit(true);//optional, but it sets auto commit to true
		    s = m_conn.createStatement(); //create a statement
		    if (s == null) throw new Exception("createStatement failed");

		    System.out.println("Creating table TEST19 and inserting data . . .\n");
		    s.executeUpdate("CREATE TABLE TEST19(Name VARCHAR(10), id NUMBER(5))");
		    s.executeUpdate("INSERT INTO TEST19 VALUES('bob',5)");
		    s.executeUpdate("INSERT INTO TEST19 VALUES('mary',6)");
		    m_conn.commit();

		    System.out.println("Query the table and print results . . .\n");
		    ResultSet rs;
		    rs = s.executeQuery("Select * from TEST19");
		    while(rs.next())
			System.out.println(rs.getString(1) + "\t" + rs.getString(2));

		    System.out.println("\nDropping table TEST19 . . .\n");
		    s.executeUpdate("DROP TABLE TEST19");
		    m_conn.commit();
		    m_conn.close();
		} finally {
		    if (m_conn != null) m_conn.close();
		}
	    } catch (Exception e) {
	    	e.printStackTrace();
			System.exit(1);
		}

    	System.out.println("JDBCDemo Successfully Completed");

    }


    private static final String connect_string = "jdbc:oracle:thin:@aloe.cs.arizona.edu:1521:oracle";
    private static Connection m_conn;

}
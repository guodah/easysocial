package com.easysocial.saver.sql;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

import com.easysocial.logging.EasySocialLogger;

public class H2Client extends SQLClient{

	public H2Client(String dbName, String dbPath, String username,
			String password) {
		super(dbName, dbPath, username, password);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void connectToDB() throws SQLException {
		String url = String.format("jdbc:h2:%s/%s", dbPath, dbName);
	//	System.out.printf("connecting to \"%s\"",url);
        connection = DriverManager.getConnection(
        		url, username, password);		
	}

	@Override
	public void disconnectDB() throws SQLException {
        if (connection != null) {
        	Statement st = connection.createStatement();
            st.execute("SHUTDOWN");
            connection.close();
            System.out.println(connection.isClosed());
        }
		
	}
	
	public static H2Client newInstance(String dbName, String dbPath,
			String username, String password) {
		try {
			Class.forName("org.h2.Driver");
		} catch (ClassNotFoundException e) {
			EasySocialLogger.log(Level.SEVERE, 
					"Failed to locate database driver for H2", e); 
		}

		return new H2Client(dbName, dbPath, username, password);
	}


}

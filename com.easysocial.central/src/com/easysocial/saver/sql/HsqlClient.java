package com.easysocial.saver.sql;

import java.sql.Connection;


import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

//import org.hsqldb.Server;

import com.easysocial.logging.EasySocialLogger;

public class HsqlClient extends SQLClient{
//	private Server hsqlServer;
	
	private HsqlClient(String dbName, String dbPath, String username,
			String password) {
		super(dbName, dbPath, username,password);
	}

	@Override
	public void connectToDB() throws SQLException {
		String url = String.format("jdbc:hsqldb:file:%s/%s", dbPath, dbName);
	//	System.out.printf("connecting to \"%s\"",url);
        connection = DriverManager.getConnection(
        		url, username, password);
	}

	@Override
	public void disconnectDB() 
			throws SQLException{
        if (connection != null) {
        	Statement st = connection.createStatement();
            st.execute("SHUTDOWN");
            connection.close();
            System.out.println(connection.isClosed());
        }
	}

	public static HsqlClient newInstance(String dbName, String dbPath,
			String username, String password) {
		try {
			Class.forName("org.hsqldb.jdbcDriver");
		} catch (ClassNotFoundException e) {
			EasySocialLogger.log(Level.SEVERE, 
					"Failed to locate database driver for hsqldb", e);
		}

		return new HsqlClient(dbName, dbPath, username, password);
	}

}

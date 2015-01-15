package com.easysocial.apis;

import java.sql.SQLException;
import java.util.logging.Level;

import com.easysocial.logging.EasySocialLogger;
import com.easysocial.saver.sql.H2Client;
import com.easysocial.saver.sql.HsqlClient;
import com.easysocial.saver.sql.SQLClient;

public class DBInterface {
	protected SQLClient client;
	protected String dbPath;
	protected String dbName;
	public void init(String dbPath, String dbName) {
		
		try {
//			client = HsqlClient.newInstance(dbName, 
//					dbPath, "SA", "");
			client = H2Client.newInstance(dbName, dbPath, 
					"SA", "");
			client.connectToDB();
			this.dbPath = dbPath;
			this.dbName = dbName;
			EasySocialLogger.log(Level.INFO, "Facebook db connected");
		} catch (SQLException e) {
			EasySocialLogger.log(Level.SEVERE, String.format(
					"Cannot open %s/%s", dbPath, dbName), e);
			client = null;
		}
	}

	public boolean connected() {
		return client!=null;
	}

	public void close() {
		try {
			client.disconnectDB();
		} catch (SQLException e) {
			EasySocialLogger.log(Level.SEVERE, String.format(
					"Cannot open %s/%s", dbPath, dbName), e);
		}
	}
}

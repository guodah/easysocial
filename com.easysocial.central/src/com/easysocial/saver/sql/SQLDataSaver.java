package com.easysocial.saver.sql;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

//import org.hsqldb.Server;


import com.easysocial.logging.EasySocialLogger;
import com.easysocial.saver.DataSaver;
import com.easysocial.types.packed.SavableObject;

public class  SQLDataSaver implements DataSaver{
	protected SQLClient client;
	
	private SQLDataSaver(SQLClient client) {
		this.client = client;
		uncommits = new ArrayList<String>();
		uncommits_size = 0;
	}
	
	
	public static SQLDataSaver newInstance(SQLClient client, List<String> classNames){
		List<Class<?extends SavableObject>> objClasses = 
				new ArrayList<Class<?extends SavableObject>>(); 
		for(String className:classNames){
			try {
				objClasses.add(Class.forName(className).
						asSubclass(SavableObject.class));
			} catch (ClassNotFoundException e) {
				EasySocialLogger.log(Level.SEVERE, 
						"Failed to find "+className,e);
				return null;
			}
		}
		List<String> commands = DBUtils.createTablesSQL(objClasses);

		try {
			client.executeSQLs(commands);
		} catch (SQLException e) {
			EasySocialLogger.log(Level.SEVERE, 
					"Failed to create all the tables in the database"+client.dbName,e);
			return null;
		}
		return new SQLDataSaver(client);
	}
	
	private List<String> uncommits;
	private int uncommits_size;
	private static final int UNCOMMIT_LIMIT = 1024*1024*2;
	
	@Override
	public void save(Collection<SavableObject> objs){
		for(SavableObject obj:objs){
			String command = getInsertSQL(obj); 
			uncommits_size += command.length();
			uncommits.add(command);
			
			if(uncommits_size>UNCOMMIT_LIMIT){
				commit();
			}
		}
	}
	
	private static <T extends SavableObject> String getInsertSQL(T record) {
		
		Field[] fields = record.getClass().getDeclaredFields();

		String sql = String.format("insert into  %s (",record.getTableName());
		for(int i=0;i<fields.length;i++){
			Field field = fields[i];
			sql += String.format("%s",field.getName());
			if(i!=fields.length-1){
				sql += ',';
			}
		}
		sql += ") values (";
		
		for(int i=0;i<fields.length;i++){
			Field field = fields[i];
			try {
				if(field.get(record)!=null){
					String str = (field.get(record).toString()).replaceAll("\'", "\'\'");
					sql += String.format("'%s'", str);
				}else{
					sql += String.format("NULL");
				}
			} catch (IllegalArgumentException e) {
				return null;
			} catch (IllegalAccessException e) {
				return null;
			}
			if(i!=fields.length-1){
				sql += ',';
			}
		}
		sql += ");";
	
		return sql;
	}
	
	private static SQLDataSaver newInstance(String dbName, String dbPath, 
			String username, String password, List<String> classNames){
//		SQLClient dbClient = HsqlClient.newInstance(dbName, 
//				dbPath, username, password);
		H2Client dbClient = H2Client.newInstance(dbName, 
				dbPath, username, password);
		if(dbClient==null){
			EasySocialLogger.log(Level.SEVERE, 
					"Failed to create a valid database client (HSQLDB).");
			return null;
		}
		try {
			dbClient.connectToDB();
			EasySocialLogger.log(Level.INFO, 
					"Connected to database name="+dbName
						+", dbPath="+dbPath);
			SQLDataSaver saver = SQLDataSaver.newInstance(dbClient, classNames);
			return saver;
		} catch (SQLException e) {
			EasySocialLogger.log(Level.SEVERE, 
					"Failed to connect to database name="+dbName
						+", dbPath="+dbPath, e);
			return null;
		}
	}
	
	/**
	 * Creates a SQLDataSaver, (currently) using HSQLDB.
	 * 
	 * All the SavableObject types are decalred in "tables.properties".
	 * @param dbName
	 * @param dbPath
	 * @return
	 */
	public static SQLDataSaver newInstance(String dbName, String dbPath){
		Properties properties = new Properties();
		try {
			properties.load(SQLDataSaver.class.getResourceAsStream("tables.properties"));
			String[] classArray=null;
			Enumeration keys = properties.keys();
			while(keys.hasMoreElements()){
				 String key = (String)keys.nextElement();
				 if(key.contains(dbName)){
					 classArray =  ((String) properties.
							 get(key)).split(", "); 
				 }
			 }
			
			return newInstance(dbName, dbPath, "sa", "", Arrays.asList(classArray));
		} catch (IOException e) {
			EasySocialLogger.log(Level.SEVERE, "SQLDataSaver failed to be created.", e);
			return null;
		}
		
		
	}

	private void commit(){
		Runtime runtime = Runtime.getRuntime();
		try {
			EasySocialLogger.log(Level.INFO, "SQLDataSaver.save (before save) " +
						(runtime.totalMemory()-runtime.freeMemory())/1000000 +" MB");
			client.executeSQLs(uncommits);
			uncommits.clear();
			uncommits_size = 0;
		//	System.gc();
			EasySocialLogger.log(Level.INFO, "SQLDataSaver.save (after save) " +
					(runtime.totalMemory()-runtime.freeMemory())/1000000 +" MB");
		} catch (SQLException e) {
			EasySocialLogger.log(Level.SEVERE, 
					"Failed to execute some sql commands", e);
		}		
	}

	@Override
	public void close() {
		commit();
		try {
			client.disconnectDB();
			EasySocialLogger.log(Level.INFO, String.format
					("Database \"%s\" disconnected.", client.dbName));
		} catch (SQLException e) {
			EasySocialLogger.log(Level.SEVERE, 
					"Failed to disconnect the database", e);			
		}
	}
	
}

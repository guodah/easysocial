package com.easysocial.saver.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public abstract class SQLClient {
	protected Connection connection;

	protected String dbName;
	protected String dbPath;
	protected String username;
	protected String password;
	public SQLClient(String dbName, String dbPath, String username, String password){
		this.dbName = dbName;
		this.dbPath = dbPath;
		this.username = username;
		this.password = password;
	}
	
	public abstract void connectToDB() 
			throws SQLException;
	public abstract void disconnectDB() throws SQLException;
	
	public Connection getConnection(){
		return connection;
	}
	
	public void executeSQL(String sql) 
			throws SQLException{
		PreparedStatement statement = null;
		try{
			statement = connection.prepareStatement(sql);
			statement.execute();
		}catch(SQLException e){
//			System.err.println(sql);
			throw e;
		}finally{
			if(statement!=null){
				statement.close();
			}
		}
	}
	
	public void executeSQLs(List<String> sqls) throws SQLException{
		for(String sql:sqls){
			executeSQL(sql);
		}
	}
	
	public ResultSet executeQuery(String sql) throws SQLException{
		try{
			return connection.prepareStatement(sql).executeQuery();
		}catch(SQLException e){
//			System.err.println(sql);
			throw e;
		}
	}
	
}

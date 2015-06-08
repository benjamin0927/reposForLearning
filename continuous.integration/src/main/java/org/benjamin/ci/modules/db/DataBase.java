package org.benjamin.ci.modules.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class DataBase {
	private String userName;
	private String password;
	private String className;
	private String url;
	
	private Connection conn = null;
	private PreparedStatement statement = null;
	private static final Logger LOGGER = Logger.getLogger(DataBase.class);
	
	public Connection getConnection(){
		try {
			Class.forName(className);
			if (conn != null) {
				conn = DriverManager.getConnection(url, userName, password);
				LOGGER.debug("" + " - Connect the Database successfully!");
			}
		} catch (ClassNotFoundException cnfex) {
			LOGGER.debug("It's failed when loading JDBC/ODBC driver progress", cnfex);
		} catch (SQLException sqlex) {
			LOGGER.debug("There is problem when closing Database", sqlex);
		} 
		
		return conn;
	}
	
	public void closeConnection(){
		try {
			if (conn != null)
				conn.close();
		} catch (Exception e) {
			LOGGER.debug("There is problem about closing Database", e);
		}
	}
	
	public void executeUpdateDataBase(String sql){
		try {
			statement = conn.prepareStatement(sql);
			statement.executeUpdate();
		} catch (SQLException e) {
			LOGGER.debug("There is problem about preparing the statement!", e);
		} finally{
			this.closeStatement();
		}
	}
	
	public void closeStatement(){
		try {
			if (statement != null){
				statement.close();
			}
		} catch (Exception e) {
			LOGGER.debug("There is problem about closing Statement!", e);
		}
	}
}

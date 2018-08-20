package com.fileupload.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MySqlFactory {

	private static final Logger LOGGER = Logger.getLogger(MySqlFactory.class.getName());
	
    public static final String DATABASE_USERNAME = "root";
    public static final String DATABASE_PASSWORD = "root";
    public static final String DATABASE_JDBC_URL = "jdbc:mysql:";
    public static final int DATABASE_PORT = 3306;
    public static final String DATABASE_SCHEMA = "filemanagerdb";
    public static final String DATABSE_SERVER = "localhost";
    public static final String DATABASE_DRIVER_CLASS = "com.mysql.jdbc.Driver";
    
    private static Connection mySqlConnection = null;

    public static Connection getMySqlConnection()
    throws Exception {
        try {
        	if (mySqlConnection == null) {
		        String mySqlConnUrl = DATABASE_JDBC_URL + "//" +
		        		DATABSE_SERVER + ":" + DATABASE_PORT +
		        		"/" + DATABASE_SCHEMA;
		        Class.forName(DATABASE_DRIVER_CLASS);
		        mySqlConnection = DriverManager.getConnection(
		        		mySqlConnUrl, DATABASE_USERNAME, DATABASE_PASSWORD);
        	}
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error while preparing connection", e);
            throw e;
        }
        return mySqlConnection;
    }
}

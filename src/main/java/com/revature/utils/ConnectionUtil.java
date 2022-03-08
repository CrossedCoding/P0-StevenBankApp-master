package com.revature.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.print.DocFlavor.INPUT_STREAM;

/**
 * Singleton utility for creating and retrieving database connection
 */
public class ConnectionUtil {
	
	private static ConnectionUtil cu = null;
	private static Properties prop = new Properties();
	private static Connection conn;
	private static String url = "jdbc:mysql://localhost:3306/revature";
	private static String username = "root";
	private static String password = "55668355566835";

	/**
	 * This method should read in the "database.properties" file and load the values
	 * into the Properties variable
	 */
	private ConnectionUtil() {

		try (InputStream input = new FileInputStream("/src/main/resources/database.properties")) {

			Properties prop = new Properties();

			prop.load(input);

		} catch (IOException e) {

			e.printStackTrace();

		}

		url = prop.getProperty("url");

		password = prop.getProperty("pswd");
		username = prop.getProperty("usr");

	}

	public static synchronized ConnectionUtil getConnectionUtil() {

		if (cu == null)

			cu = new ConnectionUtil();

		return cu;

	}

	/**
	 * This method should create and return a Connection object
	 * 
	 * @return a Connection to the database
	 */
	public static Connection getConnection() throws SQLException {
		// Hint: use the Properties variable to setup your Connection object

		conn = null;
		
		try {

			conn = DriverManager.getConnection(url, username, password);
			
			Class.forName("jdbc:mysql://localhost:3306/?user=root");
			
		} catch (ClassNotFoundException e) {

			e.printStackTrace();

		}

		return conn;

	}
	
}

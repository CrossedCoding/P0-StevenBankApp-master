package com.revature.driver;

import com.revature.beans.*;
import com.revature.beans.Account.AccountType;
import com.revature.beans.User.UserType;
import com.revature.dao.*;
import com.revature.exceptions.*;
import com.revature.services.UserService;
import com.revature.utils.SessionCache;
import com.revature.services.*;
import com.revature.driver.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This is the entry point to the application
 */
public class BankApplicationDriver {

	// static UserDriver ud = new UserDriver();
	private static Menu m = new Menu();
//	private static Scanner scan = new Scanner(System.in);
	private static Connection conn;
	private static String dbUrl = "\"jdbc:mysql://localhost:3306/revature";
	private static String username = "root";
	private static String pass = "55668355566835";
	private static Scanner scan = new Scanner(System.in);

	public static void main(String[] args) throws SQLException, ClassNotFoundException {

	
		m.menu();
		
		

	}

	public static Connection getConnection() {

		try {

			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/revature", "root", "55668355566835");

			// Class.forName("jdbc:mysql.cj.jdbc.Driver");
			// conn = DriverManager.getConnection(dbUrl, username, pass);

		} catch (SQLException e) {
			// TODO: handle exception

			e.printStackTrace();

		}

		return conn;

	}

}

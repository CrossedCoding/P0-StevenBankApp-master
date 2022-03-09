package com.revature.driver;

import com.revature.beans.*;
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
import java.util.List;
import java.util.Scanner;

public class UserDriver {

	private static Scanner scan = new Scanner(System.in);
	private static Connection conn;
	private static ResultSet rs;
	private static String dbUrl = "\"jdbc:mysql://localhost:3306/revature";
	private static String username = "root";
	private static String pass = "55668355566835";

	public static void userAdd() {

		User TestUser = new User();

		AccountDaoDB adb = new AccountDaoDB();
		UserDaoDB udb = new UserDaoDB();
		UserService usr = new UserService(udb, null);

		String Username;

		System.out.println("Please enter First Name: ");
		TestUser.setFirstName(scan.next());

		System.out.println("Please enter Last Name: ");
		TestUser.setLastName(scan.next());

		System.out.println("Please enter Username: ");
		TestUser.setUsername(scan.next());

		System.out.println("Please enter Password: ");
		TestUser.setPassword(scan.next());
		TestUser.setId(0);
		TestUser.setUserType(UserType.CUSTOMER);

//	user = TestUser;
		try {

//		System.out.println("Driver Try"  + TestUser);

			usr.register(TestUser);

//		asv.createNewAccount(TestUser);

		} catch (UsernameAlreadyExistsException e) {
			// TODO: handle exception

			System.out.println("Username already exists!");

		}

	}

	public static void getUserId() {

		UserDaoDB udb = new UserDaoDB();

		System.out.println("Please enter User ID: ");

		Integer userId = scan.nextInt();
		User t = udb.getUser(userId);

		if (t.getId() == null) {

			System.out.println("There is no user associated with this Id.");

		} else {

			System.out.println(udb.getUser(userId));

		}

	}

	public static void login() throws SQLException {

		getConnection();

		UserDaoDB udb = new UserDaoDB();
		AccountDaoDB adb = new AccountDaoDB();
		User t = new User();

		UserService ussr = new UserService(udb, adb);

		String username;
		String pass;

		System.out.println("Please enter username: ");
		username = scan.next();

		System.out.println("Please enter password: ");
		pass = scan.next();

		
		System.out.println("Test  Print");

		
		t = udb.getUser(username, pass);

		Statement stmt = conn.createStatement();
		String query = "Select * from users where username = '" + username + "' AND password = '" + pass + "'";
		rs = stmt.executeQuery(query);
		
		//System.out.println("Test 1print");

		if (t.getId() == null) {

			System.out.println("Incorrect Username/Password!");

		} else {

			try {
				
	//			System.out.println("Inside try");
				ussr.login(username, pass);

			} catch (InvalidCredentialsException e) {

				System.out.println("Incorrect login!");

			}

		}
		
	}

	public static void getAllUsers() {

		UserDaoDB udb = new UserDaoDB();

		udb.getAllUsers();

	}

	public static void updateUsers() {
		// Being taken from
		UserDaoDB udb = new UserDaoDB();
		// Final user object being sent
		User fin = new User();
		// Temp object
		User u = new User();
		int id;

		User checkName = new User();

		System.out.println("Please enter the Id you want to edit: ");
		id = scan.nextInt();
		u = udb.getUser(id);

		while (u.getId() == null) {

			System.out.println("There is no associated User with this Id.");

			System.out.println("Please enter a valid Id: ");
			id = scan.nextInt();
			u = udb.getUser(id);

		}

		System.out.println(udb.getUser(id));

		int choice;

		do {

			System.out.println("Please select what you want to edit: ");
			System.out.println("1. First Name");
			System.out.println("2. Last Name");
			System.out.println("3. Username");
			System.out.println("4. Password");
			System.out.println("5. Current User: ");
			System.out.println("6. Save and Exit");
			System.out.printf("Enter number: ");
			choice = scan.nextInt();

			switch (choice) {

			case 1:

				System.out.println("Current first name: " + u.getFirstName());
				System.out.println("Please enter new first name:(No spaces) ");
				String newFName = scan.next();

				u.setFirstName(newFName);

				System.out.println("New first name added: " + u.getFirstName());

				break;

			case 2:

				System.out.println("Current last name: " + u.getLastName());
				System.out.println("Please enter new first name:(No spaces) ");
				String newLName = scan.next();

				u.setLastName(newLName);
				System.out.println("New last name added: " + u.getLastName());

				break;

			case 3:

				String newUname;

				System.out.println("Current usernamme: " + u.getUsername());
				System.out.println("Please enter new username: ");

				newUname = scan.next();

				while (u.getUsername().equals(newUname)) {

					System.out.println("Username already exists.");
					System.out.println("Please enter new user: ");
					newUname = scan.next();

				}

				u.setUsername(newUname);
				System.out.println("New username: " + u.getUsername());

				break;

			case 4:

				String checkPass;
				String passCheck;

				do {

					System.out.println("Please enter new password: ");
					checkPass = scan.next();

					System.out.println("Please enter password again: ");
					passCheck = scan.next();

					if (checkPass.equals(passCheck)) {

						u.setPassword(passCheck);

						break;

					}

					System.out.println("");
					System.out.println("Passwords do not match! ");
					System.out.println("Please try again! ");

				} while (checkPass != passCheck);

				System.out.println("Password updated!");

				u.setPassword(passCheck);

				break;

			case 5:

				System.out.println("Current changes: ");
				System.out.println(u);

				break;

			case 6:

				udb.updateUser(u);
				System.out.println("Changes saved!");
				System.out.println("New edits: ");
				System.out.println(u);

				System.exit(0);

				break;

			default:

				System.out.println("Please enter a number between 1-6: ");

				break;

			}

		} while (choice != 6);

	}

	public static void deleteUser() {

		UserDaoDB udb = new UserDaoDB();

		User u = new User();

		do {

			System.out.println("Please enter the user Id you want to DELETE: ");
			Integer deleteId = scan.nextInt();
			u = udb.getUser(deleteId);

			if (u.getId() != null) {

				System.out.println("Choosen User: ");
				System.out.println(u.getId());

				System.out.println("Enter CONFIRM to delete and EXIT to leave: ");
				String confirm;
				char confirmed;
				confirm = scan.next().toLowerCase();

				confirmed = confirm.charAt(0);

				switch (confirmed) {

				case ('c'):

					System.out.println("User deleted: ");
					udb.removeUser(u);

					break;

				case ('e'):

					System.out.println("User delete canceled.");

					break;

				default:

					System.out.println("There is no user associated with this Id.");
					break;

				}

			}

		} while (u.getId() == null);

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

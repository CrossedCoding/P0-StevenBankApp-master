package com.revature.driver;

import com.revature.beans.*;
import com.revature.beans.User.UserType;
import com.revature.dao.*;
import com.revature.exceptions.*;
import com.revature.services.UserService;
import com.revature.services.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * This is the entry point to the application
 */
public class BankApplicationDriver {

	static Scanner scan = new Scanner(System.in);
	
	public static void main(String[] args) throws SQLException, ClassNotFoundException {

//		userAdd();
		getUserId();

//		login();

//		getAllUsers();
		
//		updateUsers();
		
	}

	public static void userAdd() {

//		User user = new User();
		User TestUser = new User();

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

//		user = TestUser;
		try {
			
		usr.register(TestUser);
		
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

	public static void login() {

		UserDaoDB udb = new UserDaoDB();
		User t = new User();
		
		UserService ussr = new UserService(udb, null);

		String username;
		String pass;

		System.out.println("Please enter username: ");
		username = scan.next();

		System.out.println("Please enter password: ");
		pass = scan.next();
		
		t = udb.getUser(username, pass);

		if (t.getId() == null) {

			System.out.println("Incorrect Username/Password!");

		} else {

			ussr.login(username, pass);
			
			System.out.println("Successfully logged in!");
			
		}

	}
	
	public static void getAllUsers() {
		
		UserDaoDB udb = new UserDaoDB();
		
		udb.getAllUsers();
	
	}
	
	public static void updateUsers() {
		// Being taken from
		UserDaoDB udb = new UserDaoDB(); 
		// Being sent to 
		User u = new User(); 
		int id;
		
		System.out.println("Please enter the Id you want to edit: ");
		id = scan.nextInt();
		u = udb.getUser(id);
		
		while(u.getId() == null) {
			
			System.out.println("There is no associated User with this Id.");
			
			System.out.println("Please enter a valid Id: ");
			id = scan.nextInt();
			u = udb.getUser(id);
			
		}
			
			System.out.println(udb.getUser(id));
			
			System.out.println("Please select what you want to edit: ");
			System.out.println("1. First Name");
			System.out.println("2. Last Name");
			System.out.println("3. Username");
			System.out.println("4. Password");
			System.out.printf("Enter number: ");
			int choice = scan.nextInt(); 
			
			switch(choice) {
			
				case 1: 
					
					System.out.println("Current first name: " + u.getFirstName());
					System.out.println("Please enter new first name:(No spaces) ");
					String newFName = scan.next();
					
					u.setFirstName(newFName);
					
					System.out.println("New first name: " + u.getFirstName());
					
					System.out.println(u);
					udb.updateUser(u);
					
					break;
					
				case 2:
					
					System.out.println("Current last name: " + u.getLastName());
					System.out.println("Please enter new first name:(No spaces) ");
					String newLName = scan.next();
					
					u.setLastName(newLName);
					
					System.out.println("New last name: " + u.getLastName());
					
					break;
					
				case 3:
					
					System.out.println("Current usernamme: " + u.getUsername());
					System.out.println("Please enter new username: ");
					
					u.setUsername(scan.next());
					
					
					
					break;
					
				case 4:
					
					break;
					
			
			}
	
	}
	
}
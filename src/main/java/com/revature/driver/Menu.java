package com.revature.driver;

import com.mysql.cj.Session;
import com.mysql.cj.x.protobuf.MysqlxCrud.Update;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Menu {

	static UserDaoDB udb = new UserDaoDB();
	static UserDriver ud = new UserDriver();
	static AccountDaoDB act = new AccountDaoDB();
	static AccountService usv = new AccountService(act);
	static BankApplicationDriver aa = new BankApplicationDriver();
	static Scanner scan = new Scanner(System.in);
	private static Connection conn;
	private static ResultSet rs;

	static User loggedUser;

	public void menu() throws SQLException {

		getConnection();

		Boolean status = true;

		User current = null;

		SessionCache.setCurrentUser(current);

		while (status) {

			if (!SessionCache.getCurrentUser().isPresent()) {

				System.out.println("1 Login: ");
				System.out.println("2 Add new user: ");
				System.out.println("3 Get all current users: ");
				System.out.println("4 Exit");
				int men = scan.nextInt();
				switch (men) {

				case 1:

					ud.login();

					break;

				case 2:

					ud.userAdd();

					break;

				case 3:

					ud.getAllUsers();

					break;

				case 4:

					System.out.println("Goodbye");

					System.exit(0);

					break;

				}
			}

			else if (SessionCache.getCurrentUser().isPresent()) {

				if (SessionCache.getCurrentUser().get().getUserType() == UserType.CUSTOMER) {

					loggedUser = SessionCache.getCurrentUser().get();

					System.out.println("Welcome: " + SessionCache.getCurrentUser().get());

					System.out.println("Account Menu: Customer");
					System.out.println("1. Add new bank account");
					System.out.println("2. Get account by userId");
					System.out.println("3. Get account by user");
					System.out.println("4. Deposit ");
					System.out.println("5. Withdraw ");
					System.out.println("6. Transfer ");
					System.out.println("7. Exit");

					int choice = scan.nextInt();

					switch (choice) {

					case 1:

						usv.createNewAccount(loggedUser);

						break;

					case 2:

						System.out.println("Please enter UserID: ");
						int id = scan.nextInt();

						act.getAccount(id);

						break;

					case 3:

						System.out.println("Please enter Username:");
						String uname = scan.next();
						User t = new User();
						t.setUsername(uname);

						act.getAccountsByUser(t);

						break;

					case 4:

						Account acId = new Account();
						User uId = new User();
						int idt;
						double amount;

						System.out.println("Please enter userId: ");
						idt = scan.nextInt();

						acId = act.getAccount(idt);

						System.out.println("Please enter deposit amount: ");
						amount = scan.nextInt();

						usv.deposit(acId, amount);

						break;

					case 5:

						withDraw();

						break;

					case 6:

						transfer();

						break;
						
					case 7: 
						
						System.out.println("Goodbye");
						
						System.exit(0);
						
						break;

					default:

						System.out.println("Invalid option, please try again!");

					}

				}

				else if (SessionCache.getCurrentUser().get().getUserType() == UserType.EMPLOYEE) {

					System.out.println("Employee: ");

					employeeAccountMenu();

					break;

				}

			}
		}
	}

	public void employeeAccountMenu() {
		
		loggedUser = SessionCache.getCurrentUser().get();

		getConnection();

		System.out.println("Account Menu: Employees");

		System.out.println("1. Add new bank account");
		System.out.println("2. Get account by userId");
		System.out.println("3. Get account by user");
		System.out.println("4. Get all accounts");
		System.out.println("5. Approve accounts");
		System.out.println("6. Deposit");
		System.out.println("7. Withdraw");
		System.out.println("8. Delete");
		System.out.println("9. Update");
		System.out.println("10. Exit");
		int choice = scan.nextInt();

		switch (choice) {

		case 1:

			usv.createNewAccount(loggedUser);

			break;

		case 2:

			System.out.println("Please enter UserID: ");
			int id = scan.nextInt();

			act.getAccount(id);

			break;

		case 3:

			System.out.println("Please enter Username: ");
			String uname = scan.next();
			User t = new User();
			t.setUsername(uname);

			System.out.println(act.getAccount(t.getId()));

			break;

		case 4:

			System.out.println(act.getAccounts());

			break;

		case 5:
			
			getConnection();

			System.out.println("Current unapproved: ");
			List<Account> approve = new ArrayList();
			List<Account> unapprove = new ArrayList();
			approve = act.getAccounts();
			for (Account a : approve) {
				
				System.out.println(a.isApproved());
				if (a.isApproved() == false) {
					
					unapprove.add(a);
					
					System.out.println(a);
					
				}

			}
			System.out.println("Please enter acountID: ");

			int ap = scan.nextInt();
			Account apa = act.getAccount(ap);
			System.out.println("Choosen Account: " + apa);
			System.out.println("1. Approve");

			System.out.println("2. Reject");

			int dec = scan.nextInt();
			
			if (dec == 1) {

				apa.setApproved(true);
				act.updateAccount(apa);
				System.out.println("Account sucessfully approved");

			} else {
				
				
				apa.setApproved(false);
				act.updateAccount(apa);
				System.err.println("Account successfully denied");
				
				
			}
			
			act.removeAccount(apa);
			

			break;

		case 6:

			Account acId = new Account();
			int idt;
			double amount;

			System.out.println("Please enter userId: ");
			idt = scan.nextInt();

			System.out.println("Please enter deposit amount: ");
			amount = scan.nextInt();

			usv.deposit(acId, amount);

			break;

		case 7:

			withDraw();

			break;
			
//		case 8:
//			
//			transfer();
//			
//			break;
	
		case 8: 
			
			ud.deleteUser();
			
			break;
			
		case 9: 
			
			ud.updateUsers();
			
			break;

		default:

			System.out.println("Invalid option, please try again!");

			break;
		}

	}

	public void userAccountMenu() {

		System.out.println("Account Menu: Customer");
		System.out.println("1. Add new bank account");
		System.out.println("2. Get account by userId");
		System.out.println("3. Get account by user");
		System.out.println("4. Exit");

		int choice = scan.nextInt();

		switch (choice) {

		case 1:

			usv.createNewAccount(loggedUser);

			break;

		case 2:

			System.out.println("Please enter UserID: ");
			int id = scan.nextInt();

			act.getAccount(id);

			break;

		case 3:

			System.out.println("Please enter Username:");
			String uname = scan.next();
			User t = new User();
			t.setUsername(uname);

			act.getAccountsByUser(t);

			break;

		case 4:

			System.exit(0);

			break;

		default:

			System.out.println("Invalid option, please try again!");

		}

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

//	public static void deposit() {
//
//		Account acId = new Account();
//		int id;
//		double amount;
//
//		System.out.println("Please enter userId: ");
//		id = scan.nextInt();
//
//		System.out.println("Please enter deposit amount: ");
//		amount = scan.nextInt();
//
//		usv.deposit(acId, amount);
//
//	}

	public static void withDraw() {

		Account acId = new Account();
		int id;
		double amount;

		System.out.println("Please enter withdraw amount: ");
		amount = scan.nextDouble();

		usv.withdraw(acId, amount);

	}

	public static void transfer() {

		Account toAcId = new Account();
		Account fAcId = new Account();
		int toId;
		int frId;
		double amount;

		System.out.println("Please enter withdraw accountId: ");
		toId = scan.nextInt();

		System.out.println("Please enter amount: ");
		amount = scan.nextDouble();

		System.out.println("Please enter deposit accountId: ");
		frId = scan.nextInt();

		toAcId = act.getAccount(toId);
		fAcId = act.getAccount(frId);

		usv.transfer(fAcId, toAcId, amount);

	}

}

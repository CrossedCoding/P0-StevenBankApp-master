package com.revature.driver;

import com.mysql.cj.Session;
import com.mysql.cj.x.protobuf.MysqlxCrud.Update;
import com.revature.beans.*;
import com.revature.beans.Transaction.TransactionType;
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

	private static UserDaoDB udb = new UserDaoDB();
	private static UserDriver ud = new UserDriver();
	private static AccountDaoDB act = new AccountDaoDB();
	private static AccountService usv = new AccountService(act);
	private static TransactionDaoDB tda = new TransactionDaoDB();
	private static BankApplicationDriver aa = new BankApplicationDriver();
	private static Scanner scan = new Scanner(System.in);
	private static Connection conn;
	private static ResultSet rs;

	static User loggedUser;

	public void menu() throws SQLException {

		getConnection();

		Boolean status = true;

		User current = null;

		SessionCache.setCurrentUser(current);

		// Loop for the entire menu
		while (status) {
			// Checks current user
			// Until someone has logged in, only these four options are available
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

				// Deciding between Customer and Employee menus
				// Customer menu was made here

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
						// Making a new checkings/savings account based on the logged in user's ID

						usv.createNewAccount(loggedUser);

						break;

					case 2:
						// Looks for any account by their UserID
						// Labeled OwnerID within database

						System.out.println("Please enter AccountID: ");
						int id = scan.nextInt();

						System.out.println(act.getAccount(id));

						break;

					case 3:
						// Same as case 2, but with userName of the account holder

						System.out.println("Please enter Username:");
						String uname = scan.next();
						User t = new User();
						t.setUsername(uname);

						act.getAccountsByUser(t);

						break;

					case 4:
						// Depositing money based of AccountID
						// Also lists the all accounts held by user

						System.out.println("Your current accounts: ");

						for (Account cuAc : act.getAccountsByUser(loggedUser)) {

							System.out.println("Owner Id: " + cuAc.getOwnerId() + " Account Id: " + cuAc.getId()
									+ " Account Type: " + cuAc.getType() + " Balance: " + cuAc.getBalance());

						}

						Account depoId = new Account();
						int idD;
						double depAmount;

						System.out.println("Please enter accountId: ");
						idD = scan.nextInt();
						depoId = act.getAccount(idD);
						System.out.println("Chosen Account: " + act.getAccount(idD));

						System.out.println("Please enter deposit amount: ");
						depAmount = scan.nextDouble();

						usv.deposit(depoId, depAmount);

						// Transaction information made here
						Transaction transac = new Transaction();

						transac.setAmount(depAmount);
						transac.setSender(act.getAccount(idD));
						// transac.setRecipient(act.getAccount(idD));
						transac.setTimestamp();
						transac.setType(TransactionType.DEPOSIT);

						tda.addTransaction(transac);

						break;

					case 5:
						// Withdraw based on AccountId

						System.out.println("Your current accounts: ");

						for (Account cuAc : act.getAccountsByUser(loggedUser)) {

							System.out.println("Owner Id: " + cuAc.getOwnerId() + " Account Id: " + cuAc.getId()
									+ " Account Type: " + cuAc.getType() + " Balance: " + cuAc.getBalance());

						}

						Account witId = new Account();
						int idW;
						double witAmount;

						System.out.println("Please enter accountId: ");
						idW = scan.nextInt();
						witId = act.getAccount(idW);
						System.out.println("Chosen Account: " + act.getAccount(idW));

						System.out.println("Please enter withdraw amount: ");
						witAmount = scan.nextDouble();

						usv.withdraw(witId, witAmount);

						// Transaction info

						Transaction witTransac = new Transaction();

						witTransac.setAmount(witAmount);
						witTransac.setRecipient(act.getAccount(idW));
						witTransac.setSender(act.getAccount(idW));
						witTransac.setTimestamp();
						witTransac.setType(TransactionType.DEPOSIT);

						tda.addTransaction(witTransac);

						break;

					case 6:

						// Transaction between two accounts

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

						// Transaction information

						Transaction transaction = new Transaction();

						transaction.setSender(fAcId);
						transaction.setRecipient(toAcId);
						transaction.setAmount(amount);
						transaction.setType(TransactionType.TRANSFER);
						transaction.setTimestamp();

						tda.addTransaction(transaction);

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

					loggedUser = SessionCache.getCurrentUser().get();

					System.out.println("Employee: " + loggedUser.getFirstName());

					System.out.println("1. Add new bank account");
					System.out.println("2. Get account by accountId");
					System.out.println("3. Get all accounts");
					System.out.println("4. Approve accounts");
					System.out.println("5. Deposit");
					System.out.println("6. Withdraw");
					System.out.println("7. Transfer");
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

						System.out.println(act.getAccount(id));

						break;

					case 3:

						System.out.println(act.getAccounts());

						break;

					case 4:

						getConnection();

						System.out.println("Current unapproved: ");
						List<Account> approve = new ArrayList();
						List<Account> unapprove = new ArrayList();
						approve = act.getAccounts();
						for (Account a : approve) {

							if (!a.isApproved()) {

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

						switch (dec) {

						case 1:

							boolean approved = true;

							apa.setApproved(approved);

							act.updateAccount(apa);

							break;

						case 2:

							boolean notapproved = false;

							apa.setApproved(notapproved);

							act.updateAccount(apa);

							break;

						}

						unapprove.remove(apa);

						break;

					case 5:

						System.out.println("Your current accounts: ");

						for (Account cuAc : act.getAccountsByUser(loggedUser)) {

							System.out.println("Owner Id: " + cuAc.getOwnerId() + " Account Id: " + cuAc.getId()
									+ " Account Type: " + cuAc.getType() + " Balance: " + cuAc.getBalance());

						}

						Account depoId = new Account();
						int idD;
						double depAmount;

						System.out.println("Please enter accountId: ");
						idD = scan.nextInt();
						depoId = act.getAccount(idD);
						System.out.println("Chosen Account: " + act.getAccount(idD));

						System.out.println("Please enter deposit amount: ");
						depAmount = scan.nextDouble();

						usv.deposit(depoId, depAmount);

						// Transaction information made here
						Transaction transac = new Transaction();

						transac.setAmount(depAmount);
						transac.setSender(act.getAccount(idD));
						// transac.setRecipient(act.getAccount(idD));
						transac.setTimestamp();
						transac.setType(TransactionType.DEPOSIT);

						tda.addTransaction(transac);

						break;

					case 6:

						System.out.println("Your current accounts: ");

						for (Account cuAc : act.getAccountsByUser(loggedUser)) {

							System.out.println("Owner Id: " + cuAc.getOwnerId() + " Account Id: " + cuAc.getId()
									+ " Account Type: " + cuAc.getType() + " Balance: " + cuAc.getBalance());

						}

						Account witId = new Account();
						int idW;
						double witAmount;

						System.out.println("Please enter accountId: ");
						idW = scan.nextInt();
						witId = act.getAccount(idW);
						System.out.println("Chosen Account: " + act.getAccount(idW));

						System.out.println("Please enter withdraw amount: ");
						witAmount = scan.nextDouble();

						usv.withdraw(witId, witAmount);

						// Transaction info

						Transaction witTransac = new Transaction();

						witTransac.setAmount(witAmount);
						witTransac.setRecipient(act.getAccount(idW));
						witTransac.setSender(act.getAccount(idW));
						witTransac.setTimestamp();
						witTransac.setType(TransactionType.DEPOSIT);

						tda.addTransaction(witTransac);

						break;

					case 7:

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

						// Transaction information

						Transaction transaction = new Transaction();

						transaction.setSender(fAcId);
						transaction.setRecipient(toAcId);
						transaction.setAmount(amount);
						transaction.setType(TransactionType.TRANSFER);
						transaction.setTimestamp();

						tda.addTransaction(transaction);

						break;

					case 8:

						ud.deleteUser();

						break;

					case 9:

						ud.updateUsers();

						break;

					case 10:

						System.out.println("GoodBye!");

						System.exit(0);

						break;

					default:

						System.out.println("Invalid option, please try again!");

						break;

					}

				}
			}

		}

	} // <--- End of menu's main scope
		// There were attempts to make separate menus based on activity but that made
		// issues in variable scopes

	
	
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

//	public static void withDraw() {
//
//		Account acId = new Account();
//		int id;
//		double amount;
//
//		System.out.println("Please enter withdraw amount: ");
//		amount = scan.nextDouble();
//
//		usv.withdraw(acId, amount);
//
//	}

//	public static void transfer() {
//
//		Account toAcId = new Account();
//		Account fAcId = new Account();
//		int toId;
//		int frId;
//		double amount;
//
//		System.out.println("Please enter withdraw accountId: ");
//		toId = scan.nextInt();
//
//		System.out.println("Please enter amount: ");
//		amount = scan.nextDouble();
//
//		System.out.println("Please enter deposit accountId: ");
//		frId = scan.nextInt();
//
//		toAcId = act.getAccount(toId);
//		fAcId = act.getAccount(frId);
//
//		usv.transfer(fAcId, toAcId, amount);
//
//	}

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

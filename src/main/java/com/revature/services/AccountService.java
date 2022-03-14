package com.revature.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.revature.beans.Account;
import com.revature.beans.Transaction;
import com.revature.beans.User;
import com.revature.beans.Account.AccountType;
import com.revature.beans.Transaction.TransactionType;
import com.revature.beans.User.UserType;
import com.revature.dao.AccountDao;
import com.revature.dao.AccountDaoDB;
import com.revature.dao.TransactionDaoDB;
import com.revature.exceptions.OverdraftException;
import com.revature.exceptions.UnauthorizedException;
import com.revature.utils.SessionCache;

/**
 * This class should contain the business logic for performing operations on
 * Accounts
 */
public class AccountService {

	public AccountDao actDao;
	public static final double STARTING_BALANCE = 25d;

	public static TransactionDaoDB tDao;
	public static Connection conn;

	public AccountService(AccountDao dao) {
		this.actDao = dao;
	}

	/**
	 * Withdraws funds from the specified account
	 * 
	 * @throws OverdraftException            if amount is greater than the account
	 *                                       balance
	 * @throws UnsupportedOperationException if amount is negative
	 */
	public void withdraw(Account a, Double amount) {

		getConnection();

		double newBal = a.getBalance() - amount;

		// Makes sure new balance won't go negative or if transaction is approved
		if (newBal < 0 || !a.isApproved()) {

			throw new OverdraftException();

		} else if (amount < 0) {

			throw new UnsupportedOperationException();

		} else {

			a.setBalance(newBal);

			List<Transaction> transac;

			// If there isn't an existing Transaction array it makes one
			if (a.getTransactions() == null) {

				transac = new ArrayList<>();

			} else {

				transac = a.getTransactions();

			}

			Transaction transc = new Transaction();
			// Temporarily holds the transaction amount and determines the transaction type
			// It then makes final changes to the account's transaction list and updates

			actDao.updateAccount(a);

		}

	}

	/**
	 * Deposit funds to an account
	 * 
	 * @throws UnsupportedOperationException if amount is negative
	 */
	public void deposit(Account a, Double amount) {

		getConnection();

		Boolean approveCheck = a.isApproved();

		if (approveCheck.equals(0)) {

			System.out.println("Sorry, your account is not approved!");

			// throw new UnsupportedOperationException();

		}

		else if (amount > 0) {

			double newBalance = 0;

			newBalance = a.getBalance() + amount;

			a.setBalance(newBalance);

			System.out.println("Succesfully deposited to account");

			// Adding transactions information to transaction table

			actDao.updateAccount(a);
		}

		else {
			System.out.println("The amount entered was a negative dollar amount. Please try again");
		}

	}

	/**
	 * Transfers funds between accounts
	 * 
	 * @throws UnsupportedOperationException if amount is negative or the
	 *                                       transaction would result in a negative
	 *                                       balance for either account or if either
	 *                                       account is not approved
	 * @param fromAct the account to withdraw from
	 * @param toAct   the account to deposit to
	 * @param amount  the monetary value to transfer
	 */

	public void transfer(Account fromAct, Account toAct, double amount) {

		getConnection();

		double fBal = fromAct.getBalance() - amount;
		double tBal = toAct.getBalance() + amount;

		if (amount < 0 || (fBal < 0)) {

			throw new UnsupportedOperationException();

			// Initial attempt at putting transaction information inside AccountService
		}
//		 else {
//
//			fromAct.setBalance(fBal);
//			toAct.setBalance(tBal);
//
//			Transaction transc = new Transaction();
//			Transaction transca = new Transaction();
//
//			transc.setSender(fromAct);
//			transc.setRecipient(toAct);
//			transc.setAmount(amount);
//			transc.setType(TransactionType.TRANSFER);
//
//			transca.setSender(fromAct);
//			transca.setRecipient(toAct);
//			transca.setAmount(amount);
//			transca.setType(TransactionType.TRANSFER);
//
//			List<Transaction> tranFr;
//			List<Transaction> tranTo;
//
//			if (fromAct.getTransactions() == null) {
//
//				tranFr = new ArrayList<>();
//
//			} else {
//
//				tranFr = fromAct.getTransactions();
//
//			}
//
//			tranFr.add(transc);
//
//			if (toAct.getTransactions() == null) {
//
//				tranTo = new ArrayList<>();
//
//			} else {
//
//				tranTo = toAct.getTransactions();
//
//			}
//
//			tranTo.add(transca);
//			toAct.setTransactions(tranTo);

		toAct.setBalance(tBal);
		actDao.updateAccount(toAct);
		
		fromAct.setBalance(fBal);
		actDao.updateAccount(fromAct);

	}

	/**
	 * Creates a new account for a given User
	 * 
	 * @return the Account object that was created
	 */
	public Account createNewAccount(User u) {

		getConnection();

		// System.out.println("Inside create user");

		getConnection();

		Account a = new Account();
		AccountDaoDB adb = new AccountDaoDB();

		a = new Account();

		a.setOwnerId(u.getId());

//		UserType type = u.getUserType();

//		List<Account> accounts = adb.getAccountsByUser(SessionCache.getCurrentUser().get());

		System.out.println("Please choose account type CHECKINGS, SAVINGS: ");

		Scanner scan = new Scanner(System.in);
		String accType = scan.next().toLowerCase();

		switch (accType.charAt(0)) {

		case 'c':

			a.setBalance(STARTING_BALANCE);
			a.setApproved(false);
			a.setType(AccountType.CHECKING);
			a.setId(0);

			break;

		case 's':

			a.setBalance(STARTING_BALANCE);
			a.setApproved(false);
			a.setType(AccountType.SAVINGS);
			a.setId(0);

			break;

		}

		adb.addAccount(a);

		System.out.println(a.getType() + " account made!");

		return a;

	}

	/**
	 * Approve or reject an account.
	 * 
	 * @param a
	 * @param approval
	 * @throws UnauthorizedException if logged in user is not an Employee
	 * @return true if account is approved, or false if unapproved
	 */
	public boolean approveOrRejectAccount(Account a, boolean approval) {

		getConnection();

		if (approval == true) {

			a.setApproved(true);
			actDao.updateAccount(a);

		} else {

			a.setApproved(false);
			actDao.updateAccount(a);

		}

//		
//		Optional<User> use = SessionCache.getCurrentUser();
//
//		if (use.isPresent()) {
//
//			User us = use.get();
//
//			if (us.getUserType() == User.UserType.EMPLOYEE) {
//
//				a.setApproved(approval); 
//
//				return a.isApproved();
//
//			} else {
//
//				throw new UnauthorizedException();
//
//			}
//
//		}

		return false;

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

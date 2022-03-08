package com.revature.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.revature.beans.Account;
import com.revature.beans.Transaction;
import com.revature.beans.User;
import com.revature.beans.Transaction.TransactionType;
import com.revature.dao.AccountDao;
import com.revature.exceptions.OverdraftException;
import com.revature.exceptions.UnauthorizedException;
import com.revature.utils.SessionCache;

/**
 * This class should contain the business logic for performing operations on Accounts
 */
public class AccountService {
	
	public AccountDao actDao;
	public static final double STARTING_BALANCE = 25d;
	
	public AccountService(AccountDao dao) {
		this.actDao = dao;
	}
	
	/**
	 * Withdraws funds from the specified account
	 * @throws OverdraftException if amount is greater than the account balance
	 * @throws UnsupportedOperationException if amount is negative
	 */
	public void withdraw(Account a, Double amount) {
		
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
		
		transc.setAmount(amount);
		
		transc.setType(TransactionType.WITHDRAWAL);
		
		transac.add(transc);
		
		a.setTransactions(transac);
		
		actDao.updateAccount(a);
		
		}
		
	}
	
	/**
	 * Deposit funds to an account
	 * @throws UnsupportedOperationException if amount is negative
	 */
	public void deposit(Account a, Double amount) {
		
		double newBal = a.getBalance() + amount;
		
		if (newBal < 0 || !a.isApproved()) {
	
			throw new UnsupportedOperationException();
		
		} else {
			
			a.setBalance(newBal);
			
			List<Transaction> transac;
			
			if (a.getTransactions() == null) {
				
				transac = new ArrayList<>();
				
			} else {
				
				transac = a.getTransactions();
			}
			
			Transaction transc = new Transaction(); 
			
			transc.setAmount(amount);
			
			transc.setType(TransactionType.DEPOSIT);
			
			transac.add(transc);
			
			a.setTransactions(transac);
			
			actDao.updateAccount(a);
			
		}
	
	}
	
	/**
	 * Transfers funds between accounts
	 * @throws UnsupportedOperationException if amount is negative or 
	 * the transaction would result in a negative balance for either account
	 * or if either account is not approved
	 * @param fromAct the account to withdraw from
	 * @param toAct the account to deposit to
	 * @param amount the monetary value to transfer
	 */
	public void transfer(Account fromAct, Account toAct, double amount) {
		
		double fBal = fromAct.getBalance() - amount;
		double tBal = toAct.getBalance() + amount;
		
		if (amount < 0 || (fBal < 0)) {
			
			throw new UnsupportedOperationException();
			
		} else {
			
			fromAct.setBalance(fBal);
			toAct.setBalance(tBal);
			
			Transaction transc = new Transaction();
			Transaction transca = new Transaction();
			
			transc.setSender(fromAct);
			transc.setRecipient(toAct);
			transc.setAmount(amount);
			transc.setType(TransactionType.TRANSFER);
			
			transca.setSender(fromAct);
			transca.setRecipient(toAct);
			transca.setAmount(amount);
			transca.setType(TransactionType.TRANSFER);
			
			List<Transaction> tranFr;
			List<Transaction> tranTo;
			
			if(fromAct.getTransactions() == null) {
				
				tranFr = new ArrayList<>();
				
			} else {
				
				tranFr = fromAct.getTransactions();
			
			} 
			
			tranFr.add(transc);
			
			if (toAct.getTransactions() == null) {
				
				tranTo = new ArrayList<>();
				
			} else {
				
				tranTo = toAct.getTransactions();
				
			}
			
			tranTo.add(transca);
			toAct.setTransactions(tranTo);
			
			actDao.updateAccount(toAct);
			actDao.updateAccount(fromAct);
			
		}
		
	}
	
	/**
	 * Creates a new account for a given User
	 * @return the Account object that was created
	 */
	public Account createNewAccount(User u) {
		
		Account acc = new Account();
		
		acc.setApproved(false);
		acc.setBalance(STARTING_BALANCE);
		
		List<Account> account;
		
		if (u.getAccounts() == null) {
			
			account = new ArrayList<>();
			
		} else {
			
			account = u.getAccounts();
			
		}
		
		account = new ArrayList<>();
		
		account.add(acc);
		
		u.setAccounts(account);
		
		actDao.addAccount(acc);
		
		return acc;
		
	}
	
	/**
	 * Approve or reject an account.
	 * @param a
	 * @param approval
	 * @throws UnauthorizedException if logged in user is not an Employee
	 * @return true if account is approved, or false if unapproved
	 */
	public boolean approveOrRejectAccount(Account a, boolean approval) {
		
		Optional<User> use = SessionCache.getCurrentUser();
		
		if(use.isPresent()) {
			
			User us = use.get();
			
			if (us.getUserType() == User.UserType.EMPLOYEE) {
				
				a.setApproved(approval);
				
				return a.isApproved();
				
			} else {
				
				throw new UnauthorizedException();
				
			}
			
		}
		
		return false;
		
	}
	
}

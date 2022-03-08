package com.revature.dao;

import java.io.FileWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.ArrayList;

import com.revature.beans.Account;
import com.revature.beans.User;

/**
 * Implementation of AccountDAO which reads/writes to files
 */
public class AccountDaoFile implements AccountDao {
	// use this file location to persist the data to
	public static String fileLocation = "";

	public Account addAccount(Account a) {
		// TODO Auto-generated method stub

		List<Account> account = getAccounts();

		account.add(a);

		try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fileLocation))) {

			output.writeObject(account);

		} catch (IOException e) {

			System.out.println("addAccount() exception");

		}

		return a;
	}

	public Account getAccount(Integer actId) {
		// TODO Auto-generated method stub

		List<Account> account = getAccounts();

		Account acc = null;

		for (Account count : account) {

			if (count.getId().equals(actId)) {

				acc = count;

				break;

			}
		}

		try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fileLocation))) {

			output.writeObject(account);

		} catch (IOException e) {

			System.out.println("getAccount() exception");

		}

		return acc;
	}

	public List<Account> getAccounts() {
		// TODO Auto-generated method stub

		List<Account> account = new ArrayList<>();

		try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(fileLocation))) {

			account = (List<Account>) input.readObject();

		} catch (FileNotFoundException e) {

			return account;

		} catch (IOException | ClassNotFoundException e) {

			System.out.println("getAccounts() exception" + e.getClass().getName());

		}

		return account;
	}

	public List<Account> getAccountsByUser(User u) {
		// TODO Auto-generated method stub

		List<Account> account = getAccounts();

		for (Account count : account) {

			if (count.getOwnerId().equals(u.getId())) {

				account.add(count);

			}

		}

		try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fileLocation))) {

			output.writeObject(account);

		} catch (IOException e) {

			System.out.println("getAccount() Exception");

		}

		return account;
	}

	public Account updateAccount(Account a) {
		// TODO Auto-generated method stub
		
		List<Account> account = getAccounts();
	
		try {

			if (account.removeIf(count -> count.getId().equals(a.getId()))) {

				account.add(a);

				return a;

			}
			
		} finally {

			try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fileLocation))) {

				output.writeObject(account);

			} catch (IOException e) {

				System.out.println("updateAccount() Exception");

			}

		}

		return null;
	}

	public boolean removeAccount(Account a) {
		// TODO Auto-generated method stub

		List<Account> account = getAccounts();
		
		boolean removed = account.removeIf(count -> count.equals(a));

		try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fileLocation))) {

			output.writeObject(account);

		} catch (IOException e) {

			System.out.println("removeAccount() Exception");

		}

		return removed;
	}

}

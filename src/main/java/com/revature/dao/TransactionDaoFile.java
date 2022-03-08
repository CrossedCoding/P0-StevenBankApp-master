package com.revature.dao;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.revature.beans.Account;
import com.revature.beans.Transaction;

public class TransactionDaoFile implements TransactionDao {
	
	public static String fileLocation = "";

	public List<Transaction> getAllTransactions() {
		// TODO Auto-generated method stub
		
		AccountDaoFile adf = new AccountDaoFile();
		
		fileLocation = adf.fileLocation;
		
		List<Account> account = adf.getAccounts();
		
		List<Transaction> transac = new ArrayList<>();
		
		for (Account acc : account) {
			
			for (Transaction tran : acc.getTransactions()) {
				
				transac.add(tran);
				
			}
			
		}
		
		try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fileLocation))) {
			
			output.writeObject(account);
			
		} catch (IOException e) {
			
			System.out.println("getAllTransactions() Exception");
			
		}
		
		return transac;
		
	}

}

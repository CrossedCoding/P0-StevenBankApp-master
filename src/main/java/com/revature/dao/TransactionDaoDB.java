package com.revature.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.revature.beans.Account;
import com.revature.beans.Transaction;
import com.revature.beans.Transaction.TransactionType;
import com.revature.utils.ConnectionUtil;

public class TransactionDaoDB implements TransactionDao {

	private static Connection conn;
	private static Statement stmt;
	private static PreparedStatement pstmt;
	private static ResultSet rs;

//	public TransactionDaoDB() {
//
//		getConnection();
//		
//		conn = ConnectionUtil.getConnection();
//
//	}

	public List<Transaction> getAllTransactions() {
		// TODO Auto-generated method stub
		
		getConnection();

		List<Transaction> transactionList = new ArrayList<Transaction>();

		String query = "SELECT * from transaction";

		try {

			stmt = conn.createStatement();

			rs = stmt.executeQuery(query);

			while (rs.next()) {

				Transaction transaction = new Transaction();

				transaction.setType((TransactionType) rs.getObject("type"));

				transaction.setSender((Account) rs.getObject("fromAccountId"));

				if (rs.getObject("transaction_type") == TransactionType.TRANSFER) {

					transaction.setRecipient((Account) rs.getObject("toAccountId"));
					
				}
				
				transaction.setTimestamp((LocalDateTime) rs.getObject("timetamp"));
				
				transaction.setAmount(rs.getDouble("amount"));
				
				transactionList.add(transaction);
				
			}

		} catch (SQLException e) {

			e.printStackTrace();

		}

		return transactionList;
		
	}
	
	public static void getConnection() {
		
		try {
			
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/revature", "root", "55668355566835");
			
		} catch (SQLException e) {
			// TODO: handle exception
			
			e.printStackTrace();
			
		}
		
		
	}

}

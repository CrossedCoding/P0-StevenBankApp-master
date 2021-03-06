package com.revature.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.beans.Account;
import com.revature.beans.Account.AccountType;
import com.revature.beans.Transaction;
import com.revature.beans.User;
import com.revature.utils.ConnectionUtil;

/**
 * Implementation of AccountDAO which reads/writes to a database
 */
public class AccountDaoDB implements AccountDao {

	private static Connection conn;
	private static Statement stmt;
	private static PreparedStatement pstmt;
	private static ResultSet rs;

	public Account addAccount(Account a) {
		// TODO Auto-generated method stub
	
		getConnection();
	
		String query = "insert into account (ownerId, accountId, balance, type, approved) values (?,?,?,?,?)";

		try {

			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, a.getOwnerId());
			pstmt.setInt(2, a.getId());
			pstmt.setDouble(3, a.getBalance());
			pstmt.setObject(4, a.getType().name());
			pstmt.setBoolean(5, a.isApproved());

			pstmt.executeUpdate();

		} catch (SQLException e) {

			e.printStackTrace();

		}

		return a;

	}

	public Account getAccount(Integer actId) {
		// TODO Auto-generated method stub

		getConnection();
		
		Account a = null;
		
		String query = "select * from account where accountId = " + actId;

		try {

			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);

			if (rs.next()) {

				a = new Account();
				
				a.setId(rs.getInt("accountId"));
				a.setOwnerId(rs.getInt("ownerId"));
				a.setBalance(rs.getDouble("balance"));
				a.setApproved(rs.getBoolean("approved"));
				
				if (rs.getString("type") != null) {
					
					if(rs.getString("type").equals(AccountType.CHECKING.name())) {
						
						a.setType(AccountType.CHECKING);
						
					} else {
						
						a.setType(AccountType.SAVINGS);
						
					}
					
				}
				
			}

		} catch (SQLException e) {

			e.printStackTrace();

		}

		
		return a;

	}

	public List<Account> getAccounts() {
		// TODO Auto-generated method stub
		
		getConnection();
		
		List<Account> accountList = new ArrayList<Account>();
		
		String query = "select * from account";
		
		try {
			
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			
			while (rs.next()) {
				
				Account account = new Account();
				
				account.setId(rs.getInt("accountId"));
				account.setOwnerId(rs.getInt("ownerId"));
				account.setBalance(rs.getDouble("balance"));
				account.setApproved(rs.getBoolean("approved"));
				
//				String transac = rs.getString("transactions");
//				Transaction enumVal
				
				String type = rs.getString("type");
				AccountType enumVal = AccountType.valueOf(type);
				account.setType(enumVal);
				
				accountList.add(account);
				
			}
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			
		}
		
		return accountList;
	}

	public List<Account> getAccountsByUser(User u) {
		// TODO Auto-generated method stub

		getConnection();
		
		String query = "SELECT * from account where ownerId = " + u.getId();
		
		List<Account> accountList = new ArrayList<Account>();
		
		try {
			
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			
			while (rs.next()) {
				
				
				Account account = new Account();
			
				account.setOwnerId(rs.getInt("ownerId"));
				account.setBalance(rs.getDouble("balance"));
				account.setId(rs.getInt("accountId"));
				
				String type = rs.getString("type");
				AccountType enumVal = AccountType.valueOf(type);
				account.setType(enumVal);
				
				accountList.add(account);
				
			}
			
			
		} catch (SQLException e) {
			// TODO: handle exception
			
			e.printStackTrace();
			
		}
		
		
		return accountList;
	}

	public Account updateAccount(Account a) {
		// TODO Auto-generated method stub
		
		getConnection();
		
		String query = "update account set ownerId = ?, balance = ?, type = ?, approved = ? WHERE accountId = ?";
		
		try {
			
			pstmt = conn.prepareStatement(query);
			pstmt.setDouble(5, a.getId());
			pstmt.setDouble(2, a.getBalance());
			pstmt.setString(3, a.getType().name());
			pstmt.setBoolean(4, a.isApproved());
			pstmt.setInt(1, a.getOwnerId());
			pstmt.executeUpdate();
			
			return a;
			
		} catch (SQLException e) {
			// TODO: handle exception
			
			e.printStackTrace();
			
		}
		
		return null;
	}

	public boolean removeAccount(Account a) {
		// TODO Auto-generated method stub
		
		getConnection();
		
		String query = "DELETE account WHERE accountId = ?";
		
		try {
			
			pstmt = conn.prepareStatement(query);
		
			pstmt.setInt(1, a.getId());
			
			pstmt.executeQuery();
			
			return true;
			
		} catch (SQLException e) {
			// TODO: handle exception
			
			e.printStackTrace();
			
		}
		
		return false;
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

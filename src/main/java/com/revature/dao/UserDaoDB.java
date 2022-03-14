package com.revature.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.beans.User;
import com.revature.beans.User.UserType;
import com.revature.utils.ConnectionUtil;

/**
 * Implementation of UserDAO that reads/writes to a relational database
 */

public class UserDaoDB implements UserDao {

	private static User user = new User();

	private static Connection conn;
	private static Statement stmt;
	private static PreparedStatement pstmt;
	private static ResultSet rs;

//	public UserDaoDB() {
//
//		getConnection();
//		
//		String query = "insert int user (id, username, password, firstName, lastName, userType) values (?,?,?,?,?,?)";
//
//		try {
//
//			pstmt = conn.prepareStatement(query);
//
//			pstmt.executeUpdate(query);
//
//		} catch (SQLException e) {
//
//			e.printStackTrace();
//
//		}
//
//	}

	public User addUser(User user) {
		// TODO Auto-generated method stub

		getConnection();

		String query = "Insert into users () values (?, ?, ?, ?, ?, ?)";

		try {

			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, user.getId());
			pstmt.setString(2, user.getFirstName());
			pstmt.setString(3, user.getLastName());
			pstmt.setString(4, user.getPassword());
			pstmt.setString(5, user.getUsername());

			System.out.println("userType = " + user.getUserType());
			pstmt.setString(6, user.getUserType().name());

			pstmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();

		}

		System.out.println("New user added!");

		return user;
	}

	public User getUser(Integer userId) {
		// TODO Auto-generated method stub

		getConnection();

		String query = "Select * from users where id = " + userId;

		User user = new User();

		try {

			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);

			if (rs.next()) {

				user.setId(userId);
				user.setFirstName(rs.getString("firstName"));
				user.setLastName(rs.getString("lastName"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));

				String type = rs.getString("userType");
				UserType enumVal = UserType.valueOf(type);
				user.setUserType(enumVal);

			}

		} catch (SQLException e) {

			e.printStackTrace();

		}

		return user;
	}

	public User getUser(String username, String pass) {
		// TODO Auto-generated method stub

		getConnection();

		// conn = ConnectionUtil.getConnection();

		String query = "Select * from users where username = '" + username + "' AND password = '" + pass + "'";

		User user = new User();

		try {

//			pstmt = conn.prepareStatement(query);
//			pstmt.setString(1, username);
//			pstmt.setString(2, pass);
//			rs = pstmt.executeQuery(query);

			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);

			if (rs.next()) {

				user.setId(rs.getInt("id"));
				user.setFirstName(rs.getString("firstName"));
				user.setLastName(rs.getString("lastName"));
				user.setUsername(rs.getString("username"));
				user.setPassword(pass);

				String type = rs.getString("userType");
				UserType enumVal = UserType.valueOf(type);
				user.setUserType(enumVal);

			}

		} catch (SQLException e) {

			e.printStackTrace();

		}

		return user;

	}

	public List<User> getAllUsers() {
		// TODO Auto-generated method stub

		getConnection();

		String query = "Select * from users";

		List<User> userList = new ArrayList<User>();

		try {

			stmt = conn.createStatement();

			rs = stmt.executeQuery(query);

			while (rs.next()) {

				User user = new User();

				user.setId(rs.getInt("id"));
				user.setFirstName(rs.getString("firstName"));
				user.setLastName(rs.getString("lastName"));
				user.setPassword(rs.getString("password"));
				user.setUsername(rs.getString("username"));
				
				String type = rs.getString("userType");
				UserType enumVal = UserType.valueOf(type);
				user.setUserType(enumVal);

				userList.add(user);

			}

		} catch (Exception e) {

			e.printStackTrace();

		}

		for (User user : userList) {

			System.out.println(user);

		}

		return userList;

	}

	public User updateUser(User u) {
		// TODO Auto-generated method stub

		getConnection();

		String query = "Update users set firstName = ?, lastName = ?, password = ?, username = ?, userType = ? where id = ?";

		try {

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(6, u.getId());
			pstmt.setString(1, u.getFirstName());
			pstmt.setString(2, u.getLastName());
			pstmt.setString(3, u.getPassword());
			pstmt.setString(4, u.getUsername());

//			String type = rs.getString("userType");
//			UserType enumVal = UserType.valueOf(type);
			
			pstmt.setString(5, u.getUserType().name());

			System.out.println(u);

			int result = pstmt.executeUpdate();

			if (result == 0) {

				return null;

			}

		} catch (SQLException e) {
			// TODO: handle exception

			e.printStackTrace();

		}

		return u;

	}

	public boolean removeUser(User u) {
		// TODO Auto-generated method stub

		getConnection();

		String query = "DELETE from users where id = " + u.getId();

		boolean status = false;

		try {

			stmt = conn.createStatement();

			status = stmt.execute(query);

		} catch (SQLException e) {

			e.printStackTrace();

		}
		return status;
	}

	public static Connection getConnection() {

		try {

			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/revature", "root", "55668355566835");

		} catch (SQLException e) {
			// TODO: handle exception

			e.printStackTrace();

		}

		return conn;

	}

}

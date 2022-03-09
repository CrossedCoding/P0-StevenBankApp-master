package com.revature.services;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.beans.User;
import com.revature.dao.AccountDao;
import com.revature.dao.UserDao;
import com.revature.exceptions.InvalidCredentialsException;
import com.revature.exceptions.UsernameAlreadyExistsException;
import com.revature.utils.SessionCache;

/**
 * This class should contain the business logic for performing operations on
 * users
 */
public class UserService {

	UserDao userDao;
	AccountDao accountDao;

	private static Connection conn;
	private static ResultSet rs;

	public UserService(UserDao udao, AccountDao adao) {
		this.userDao = udao;
		this.accountDao = adao;
	}

	/**
	 * Validates the username and password, and return the User object for that user
	 * 
	 * @throws InvalidCredentialsException if either username is not found or
	 *                                     password does not match
	 * @return the User who is now logged in
	 * @throws SQLException
	 */
	public User login(String username, String password) {

		User login = userDao.getUser(username, password);
		
//		System.out.println("Inside Service");
		
		if (login == null) {

			throw new InvalidCredentialsException();

		} else {

			SessionCache.setCurrentUser(login);

//			System.out.println(SessionCache.getCurrentUser().get());
			
			return login;

		}

	}

	/**
	 * Creates the specified User in the persistence layer
	 * 
	 * @param newUser the User to register
	 * @throws UsernameAlreadyExistsException if the given User's username is taken
	 */
	public void register(User newUser) {

		//List<User> users = new ArrayList<>();
			List<User> users = userDao.getAllUsers();

//		int i = 0;

//		for (i = 0; i < users.size(); i++) {
//			
//			if  (users.get(i).equals(newUser.getUsername())) {
//				
//				throw new UsernameAlreadyExistsException();
//				
//			}
//			
//		}

		for (User u : users) {

			// System.out.println(u.getUsername());

			if (u.getUsername().equals(newUser.getUsername())) {

				throw new UsernameAlreadyExistsException();

			}
		}

//		if (userDao.getUser(newUser.getUsername(), newUser.getPassword()) == null) {
//
//			userDao.addUser(newUser);
//
//		} else {
//
//			throw new UsernameAlreadyExistsException();
//
//		}

		userDao.addUser(newUser);
		
//
	}

}

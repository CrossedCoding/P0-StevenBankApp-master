package com.revature.dao;

import java.io.IOException;
import java.io.EOFException;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FilterInputStream;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.List;
import java.util.ArrayList;

import com.revature.beans.User;

/**
 * Implementation of UserDAO that reads and writes to a file
 */
public class UserDaoFile implements UserDao {
	
	public static String fileLocation = "/src/test/test.txt";

	public User addUser(User user) {
		// TODO Auto-generated method stub

		List<User> getUser = getAllUsers();
		
		getUser.add(user);

		try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fileLocation))) {

			output.writeObject(user);

		} catch (IOException e) {
			// TODO: handle exception

			System.out.println("addUser() Exception");

		}

		return user;

	}

	public User getUser(Integer userId) {
		// TODO Auto-generated method stub

		User use = null;
		
		List<User> user = getAllUsers();
		
		for (User name : user) {

			if (name.getId().equals(userId)) {

				use = name;

				break;

			}

		}

		try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fileLocation))) {

			output.writeObject(user);

		} catch (IOException e) {
			// TODO: handle exception

			System.out.println("getUser() Exception");

		}

		return use;

	}

	public User getUser(String username, String pass) {
		// TODO Auto-generated method stub

		User u = null;
		
		List<User> user = getAllUsers();
		
		for (User use : user) {

			if ((u.getUsername() == username && (u.getPassword() == pass))) {

				u = use;

				break;

			}
			
		}

		try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fileLocation))) {

			output.writeObject(user);

		} catch (IOException e) {
			// TODO: handle exception

			System.out.println("getUser(username, pass) Exception");

		}

		return u;
		
	}

	public List<User> getAllUsers() {
		// TODO Auto-generated method stub

		List<User> user = new ArrayList<User>();
		
		try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(fileLocation))) {

			user = (List<User>) input.readObject();

		} catch (FileNotFoundException e) {

			return user;

		} catch (IOException | ClassNotFoundException e) {

			System.out.println("getAllUser() Exception");

		}

		return user;

	}

	public User updateUser(User u) {
		// TODO Auto-generated method stub
		
		List<User> user = getAllUsers();
		
		for (User use : user) {
			
			if(use.getId().equals(u.getId())) {
				
				user.remove(u);
				
				user.add(u);
				
				return u;
				
			}
			
			try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fileLocation))) {

				output.writeObject(user);

			} catch (IOException e) {
				// TODO: handle exception

				System.out.println("updateUser() Exception");

			}
			
		}
		
		return null;
	}

	public boolean removeUser(User u) {
		// TODO Auto-generated method stub
		
		List<User> user = getAllUsers();
		
		boolean removed = user.removeIf(use -> user.equals(u));
		
		try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fileLocation))) {

			output.writeObject(user);

		} catch (IOException e) {
			// TODO: handle exception

			System.out.println("removeUser() Exception");

		}
		
		return removed;
	}

}

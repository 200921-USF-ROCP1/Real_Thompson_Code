package com.revature.dao;

import java.util.ArrayList;
import java.util.List;

import com.revature.Model.Account;
import com.revature.Model.User;

public interface UserDAO {
	// log in
	public User LoginDAO(String username, String password);
	
	// lot out
	public void LogOutDAO();
	
	// create new user
	public User Register(User newuser);
	
	// find users, all of them.
	public ArrayList<User> FindUsers(); // restful
	
	// find user by id
	public User FindUserByID(int userid);
	
	// update user
	public boolean UpdateUserDAO(User user);
	
	// FindUsersByAccount: given account, find users
	public List<Integer> FindUsersByAccount(int accountid);
	
	// link users to an account, or vice versa
	public boolean LinkUserToAccount(int userid, int accountid);

}

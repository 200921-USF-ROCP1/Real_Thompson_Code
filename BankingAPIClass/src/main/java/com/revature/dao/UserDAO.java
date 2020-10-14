package com.revature.dao;

import java.util.ArrayList;
import java.util.List;

import com.revature.Model.Account;
import com.revature.Model.User;

public interface UserDAO {
	
	public User LoginDAO(String username, String password);
	public void LogOutDAO();
	public User Register(User newuser);
	public ArrayList<User> FindUsers(); // restful
	public User FindUserByID(int userid);
	public boolean UpdateUserDAO(User user);
	public List<Integer> FindUsersByAccount(int accountid);
	

}

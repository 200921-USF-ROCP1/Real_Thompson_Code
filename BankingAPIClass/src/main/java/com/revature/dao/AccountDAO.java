package com.revature.dao;

import java.util.ArrayList;
import java.util.List;

import com.revature.Model.Account;
import com.revature.Model.AccountStatus;
import com.revature.Model.User;

public interface AccountDAO {
	//Withdraw money from account
	// accountID = withdraw money from this account
	// amount: withdraw this amount
	public String Withdraw(int accountID, double amount);
	
	// Deposit: Deposit money into an account
	// accountID: account number to which to deposit the money
	// amount: amount of money to deposit
	public String Deposit(int accountID, double amount);
	
	// Transfer: Transfer money from one account to another
	// sourceAccountId: the account the money is coming out of 
	// targetAccountId: the account the money is going into
	public String Transfer(int sourceAccountId, int targetAccountId, double amount);
	
	// FindAccounts: find all accounts
	public ArrayList<Account> FindAccounts();
	
	// FindAccountsByID: find accounts given a user id
	// userID: the userID of the account being searched for
	public Account FindAccountsByID(int userID);
	
	// FindAccountsByStatus: find all accounts given a status
	// status: the status: 0 = open, 1 = closed
	public ArrayList<Account>  FindAccountsByStatus(AccountStatus status);
	
	// FindAccountsByUser: find the accound given a user id    
	// userId: userid
	public List<Account> FindAccountsByUser(int userId);
	
	// SubmitAccount: create account
	// account: account submitted for adding to the table
	public Account SubmitAccount(Account account); // create account
	
	// UpdateAccount
	// update the account
	// account: update the fields in this account
	public Account UpdateAccount(Account account);
	
	// not needed
	//public int FindUserByAccountIdAndUserId(int account, int userid);

}

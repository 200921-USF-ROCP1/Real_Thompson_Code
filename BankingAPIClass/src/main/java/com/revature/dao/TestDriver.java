package com.revature.dao;

import com.revature.Model.Role;
import com.revature.Model.User;
import com.revature.servlets.FindAccountsByUserServlet;

public class TestDriver {
	public static void main(String[] args) {

	//	FindAccountsByUserServlet fabyu = new FindAccountsByUserServlet();
		
		
//		User user = new User();
//		//User userguy =  user.LoginDAO("mario", "a");
//		user.setUsername("thompo");
//		user.setPassword("a");
//		user.setFirstName("ghan");
//		user.setLastName("ghengis");
//		user.setEmail("thompo@hotmail.com");
//		Role role = new Role();
//		role.setRoleId(0);
//		role.setRole("Admin");
//		user.setRole(role);
//		
//		UserDAO dao = new UserImpl();
//		dao.Register(user);
		AccountDAO acct = new AccountImpl();
		acct.Transfer(3, 2, 399); //Withdraw(2, 100.00);
		//
//		private String username; // not null, unique
//		private String password; // not null
//		private String firstName; // not null
//		private String lastName; // not null
//		private String email; // not null
//		private Role role;

	}

}

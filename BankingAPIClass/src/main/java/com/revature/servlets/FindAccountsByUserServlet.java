package com.revature.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Stream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.DAOUtilities.DAOUtilities;
import com.revature.Model.Account;
import com.revature.Model.AccountStatus;
import com.revature.Model.AccountType;
import com.revature.Model.User;
import com.revature.Model.UserIdClass;
import com.revature.dao.AccountDAO;
import com.revature.dao.AccountImpl;
import com.revature.dao.UserDAO;
import com.revature.dao.UserImpl;

@WebServlet("/accounts/owner/:userId")
public class FindAccountsByUserServlet extends HttpServlet {

	public FindAccountsByUserServlet() {
		// TODO Auto-generated constructor stub
	}

	// get accounts linked to given user
	@Override
	// log in first, then run this
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter pw = response.getWriter();

		User user = (User) request.getSession().getAttribute(DAOUtilities.LOGGED_IN_KEY);
		if (user != null) {
			AccountDAO accountdao = new AccountImpl();
			UserDAO userdao = new UserImpl();
			String parm = request.getParameter("userId");
			int intparm = Integer.parseInt(parm);
			int loggedInUser = user.getUserId();
			int rolePlayed = user.getRole().getRoleId();
		
			
			if (rolePlayed == 0 || rolePlayed == 1 || intparm == loggedInUser) {
				
				List<Account> foundaccounts = accountdao.FindAccountsByUser(intparm);

				ObjectMapper mapper2 = new ObjectMapper(); // Create the mapper
				String jsonString = mapper2.writeValueAsString(foundaccounts); // To marshal to a String
				pw.println(jsonString);
				response.setStatus(200);

			} else {
				response.setStatus(400);
			}
		}
		else
		{
			pw.println(DAOUtilities.USER_NOT_LOGGED_IN);
		}
	}

}

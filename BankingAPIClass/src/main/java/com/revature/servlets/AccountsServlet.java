package com.revature.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

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
//import com.revature.Model.UserIdClass;
import com.revature.dao.AccountDAO;
import com.revature.dao.AccountImpl;
import com.revature.dao.UserDAO;
import com.revature.dao.UserImpl;

@WebServlet("/accounts")
public class AccountsServlet extends HttpServlet {

	public AccountsServlet() {
		// TODO Auto-generated constructor stub
	}

	@Override
	// log in first, then run this
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter pw = response.getWriter();
		User user = (User) request.getSession().getAttribute(DAOUtilities.LOGGED_IN_KEY);
		if (user != null) {
			ObjectMapper mapper = new ObjectMapper();

			if (user.getRole().getRoleId() == 0 || user.getRole().getRoleId() == 1) {
				// admin or employee
				AccountDAO accountdao = new AccountImpl();
				List<Account> foundaccounts = accountdao.FindAccounts();

				String jsonstring = mapper.writeValueAsString(foundaccounts);
				pw.println(jsonstring);
				response.setStatus(200);
			} else {
				response.setStatus(400);
			}
		} else {
			pw.println(DAOUtilities.USER_NOT_LOGGED_IN);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String jsonString = null; // mapper2.writeValueAsString(temp); // To marshal to a String
//		
		PrintWriter pw = response.getWriter();
		User user = (User) request.getSession().getAttribute(DAOUtilities.LOGGED_IN_KEY);
		if (user != null) {
			pw = response.getWriter();
			ObjectMapper mapper = new ObjectMapper(); // Create the mapper
			Account unmarshalled = null;
			try {
				unmarshalled = mapper.readValue(request.getReader(), Account.class); // Unmarshal
			} catch (Exception ex) {
				ex.getStackTrace();
				throw new ServletException("{\"message\": \"Could not add account\"}");
			}

			if (user.getRole().getRoleId() == 0 || user.getRole().getRoleId() == 1 /* || founduser */) {
				// admin or employee

				AccountDAO accountdao = new AccountImpl();
				Account submittedAccount = accountdao.SubmitAccount(unmarshalled); // SubSubFindAccountsByUser(un);

				ObjectMapper mapper3 = new ObjectMapper(); // Create the mapper
				jsonString = mapper3.writeValueAsString(submittedAccount); // To marshal to a String
				pw.println(jsonString);
				response.setStatus(200);

			} else {
				response.setStatus(400);
			}
		}
		else {
			pw.println(DAOUtilities.USER_NOT_LOGGED_IN);
		}
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String jsonString = null; // To marshal to a String
//		
		PrintWriter pw = response.getWriter();
		User user = (User) request.getSession().getAttribute(DAOUtilities.LOGGED_IN_KEY);
		if (user != null) {
			pw = response.getWriter();

			if (user.getRole().getRoleId() == 0) {
				// admin or employee
				ObjectMapper mapper = new ObjectMapper(); // Create the mapper
				Account unmarshalled = null;

				try {
					unmarshalled = mapper.readValue(request.getReader(), Account.class); // Unmarshal
				} catch (Exception ex) {
					ex.printStackTrace();

				}

				AccountDAO accountdao = new AccountImpl();
				Account submittedAccount = accountdao.UpdateAccount(unmarshalled); // SubSubFindAccountsByUser(un);

				ObjectMapper mapper3 = new ObjectMapper(); // Create the mapper
				jsonString = mapper3.writeValueAsString(submittedAccount); // To marshal to a String
				pw.println(jsonString);
				response.setStatus(200);

			} else {
				response.setStatus(400);
			}
		}
		else {
			pw.println(DAOUtilities.USER_NOT_LOGGED_IN);
		}
	}

}

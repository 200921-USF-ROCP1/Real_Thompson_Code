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
import com.revature.Model.Account;
import com.revature.Model.AccountStatus;
import com.revature.Model.AccountType;
import com.revature.Model.User;
import com.revature.Model.UserIdClass;
import com.revature.dao.AccountDAO;
import com.revature.dao.AccountImpl;
import com.revature.dao.UserDAO;
import com.revature.dao.UserImpl;

@WebServlet("/accounts/owner/")
public class FindAccountsByUserServlet extends HttpServlet {

	public FindAccountsByUserServlet() {
		// TODO Auto-generated constructor stub
	}

	@Override
	// log in first, then run this
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter pw = response.getWriter();

		User user = (User) request.getSession().getAttribute("UserLoggedIn");
		if (user != null) {
			AccountDAO accountdao = new AccountImpl();
			UserDAO userdao = new UserImpl();
			String parm = request.getParameter("userId");
			int intparm = Integer.parseInt(parm);

			User usernotloggedin = userdao.FindUserByID(intparm);
			
			if (user.getRole().getRoleId() == 0 || user.getRole().getRoleId() == 1 ||
					(usernotloggedin != null && usernotloggedin.getUserId() == user.getUserId())) {
				// admin or employee
				ObjectMapper mp = new ObjectMapper();
				UserIdClass input = new UserIdClass();
				ObjectMapper mapper = new ObjectMapper(); // Create the mapper
				UserIdClass unmarshalled = null;
				try {
					unmarshalled = mapper.readValue(request.getReader(), UserIdClass.class); // Unmarshal
																								// from a
																								// Strin
				} catch (Exception ex) {
					ex.printStackTrace();

				}
				
				int userInt = unmarshalled.userId;

				AccountStatus acctStat = new AccountStatus();
				List<Account> foundaccounts = accountdao.FindAccountsByUser(userInt);

				ObjectMapper mapper2 = new ObjectMapper(); // Create the mapper
				String jsonString = mapper2.writeValueAsString(foundaccounts); // To marshal to a String
				pw.println(jsonString);
				response.setStatus(200);

			} else {
				response.setStatus(400);
			}

		}
	}

}

package com.revature.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.DAOUtilities.DAOUtilities;
import com.revature.Model.Account;
import com.revature.Model.AccountTran;
import com.revature.Model.Role;
import com.revature.Model.User;
import com.revature.dao.AccountDAO;
import com.revature.dao.AccountImpl;
import com.revature.dao.UserDAO;
import com.revature.dao.UserImpl;

@WebServlet("/accounts/withdraw")
public class AccountsWithdrawServlet extends HttpServlet {

	public AccountsWithdrawServlet() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		User user = (User) request.getSession().getAttribute(DAOUtilities.LOGGED_IN_KEY);
		if (user != null) {

			PrintWriter pw = response.getWriter();
			AccountDAO dao = new AccountImpl();

			ObjectMapper mapper = new ObjectMapper(); // Create the mapper
			AccountTran unmarshalled = mapper.readValue(request.getReader(), AccountTran.class); // Unmarshal
			
			boolean loggedInUserHasSecurity =  DAOUtilities.CompareUsersByAccountNumber(unmarshalled.getAccountId(), user.getUserId());
			if (user.getRole().getRoleId() == 0 || loggedInUserHasSecurity)
			{
				String output = dao.Withdraw(unmarshalled.getAccountId(), unmarshalled.getAmount());
				if (output.contains("Success")) {

					response.setStatus(200);
					String strAmount = Double.toString(unmarshalled.getAmount());
					String message = "{\"message\" : \"$" + strAmount + " has been withdrawn from Account #";
					String strAccount = Integer.toString(unmarshalled.getAccountId()); 
					message += strAccount + "\"}";
					pw.println(message);

				} else {

					response.setStatus(400);
				}
			}
		}
	}
}

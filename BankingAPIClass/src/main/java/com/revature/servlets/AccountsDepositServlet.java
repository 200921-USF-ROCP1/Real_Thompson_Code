package com.revature.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.DAOUtilities.DAOUtilities;
import com.revature.Model.AccountTran;
import com.revature.Model.User;
import com.revature.dao.AccountDAO;
import com.revature.dao.AccountImpl;
import com.revature.dao.UserDAO;
import com.revature.dao.UserImpl;

@WebServlet("/accounts/deposit")
public class AccountsDepositServlet extends HttpServlet {

	public AccountsDepositServlet() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter pw = response.getWriter();
		try {
			User user = (User) request.getSession().getAttribute(DAOUtilities.LOGGED_IN_KEY);
			if (user != null) {

				pw = response.getWriter();
				AccountDAO dao = new AccountImpl();
				UserDAO userdao = new UserImpl();
				// get account belonging to the user
				ObjectMapper mapper = new ObjectMapper(); // Create the mapper
				AccountTran unmarshalled = mapper.readValue(request.getReader(), AccountTran.class); // Unmarshal
				if (unmarshalled == null)
				{
				   throw new ServletException("Could not unmarshal request");	
				}
				
				List<Integer> userOfAccount = userdao.FindUsersByAccount(unmarshalled.getAccountId());

				boolean foundUser = false;

				for (Integer accountid : userOfAccount) {

					if ((int) accountid == user.getUserId()) {
						foundUser = true;
						break;
					}
				}

				if (user.getRole().getRoleId() == 0 || foundUser) {
					String output = dao.Deposit(unmarshalled.getAccountId(), unmarshalled.getAmount());
					if (output.contains("Success")) {

						response.setStatus(200);
						String strAmount = Double.toString(unmarshalled.getAmount());
						String message = "{\"message\" : \"$" + strAmount + " has been deposited to Account #";
						String strAccount = Integer.toString(unmarshalled.getAccountId()); // + "\"}";
						message += strAccount + "\"}";
						pw.println(message);

					} else {

						response.setStatus(400);
					}
				}
			}
			else
			{
				pw.println(DAOUtilities.USER_NOT_LOGGED_IN);
			}
		} catch (Exception sve) {
			throw new ServletException("accounts/deposit: " + sve.getMessage());
		}
	}
}

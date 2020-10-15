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
import com.revature.Model.Account;
import com.revature.Model.AccountTran;
import com.revature.Model.TransferJSON;
import com.revature.Model.User;
import com.revature.dao.AccountDAO;
import com.revature.dao.AccountImpl;

@WebServlet("/accounts/transfer") 
public class AccountsTransferServlet extends HttpServlet {

	public AccountsTransferServlet() {
		// TODO Auto-generated constructor stub
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter pw = response.getWriter();
		User user = (User) request.getSession().getAttribute(DAOUtilities.LOGGED_IN_KEY);
		if (user != null) {

			ObjectMapper mapper = new ObjectMapper(); // Create the mapper
			TransferJSON unmarshalled = mapper.readValue(request.getReader(), TransferJSON.class); // Unmarshal
			AccountDAO dao = new AccountImpl();
			boolean loggedInUserHasSecurity =  DAOUtilities.CompareUsersByAccountNumber(unmarshalled.getSourceAccountId(), user.getUserId());
			
			pw = response.getWriter();

			if (user.getRole().getRoleId() == 0 || (loggedInUserHasSecurity)) {
				
				String output = dao.Transfer(unmarshalled.getSourceAccountId(), unmarshalled.getTargetAccountId(), 
						unmarshalled.getAmount());
				
				if (output.contains("Success") || output.contains("has been transferred")) {

					response.setStatus(200);
					String message = ""; // "$" + amount + " has been deposited to Account #" + accountid;
					message += "{\"message\": \"$";
					message += Double.toString(unmarshalled.getAmount());
					message +=  " has been transferred from Account #";
					message += Integer.toString(unmarshalled.getSourceAccountId());
					message += " to Account #";
					message +=  Integer.toString(unmarshalled.getTargetAccountId());
					message += "\"}";
					pw.println(message);

				} else {

					response.setStatus(400);
				}
			}
		}
		else {
			pw.println(DAOUtilities.USER_NOT_LOGGED_IN);
		}

	}

}

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
import com.revature.dao.AccountDAO;
import com.revature.dao.AccountImpl;

@WebServlet("/accounts/status/:statusId")
public class FindAccountsByStatusServlet extends HttpServlet {

	public FindAccountsByStatusServlet() {
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
				String statusId = request.getParameter("statusId");
				int statusIdInt = Integer.parseInt(statusId);

				AccountDAO accountdao = new AccountImpl();
				AccountStatus acctStat = new AccountStatus();
				acctStat.setStatusId(statusIdInt);
				List<Account> foundaccounts = accountdao.FindAccountsByStatus(acctStat);

				String resultjson = mapper.writeValueAsString(foundaccounts);
				pw.println(resultjson);
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

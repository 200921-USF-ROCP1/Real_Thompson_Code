package com.revature.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.Model.Account;
import com.revature.Model.User;
import com.revature.dao.AccountDAO;
import com.revature.dao.AccountImpl;
import com.revature.dao.UserDAO;
import com.revature.dao.UserImpl;

@WebServlet("/accounts/:id")
public class FindAccountsByIDServlet extends HttpServlet {

	public FindAccountsByIDServlet() {
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
			ObjectMapper mapper = new ObjectMapper();
			String accountid = request.getParameter("id");
			int intid = Integer.parseInt(accountid);
			int sameuserasloggedin = accountdao.FindUserByAccountIdAndUserId(intid, user.getUserId());
			if (user.getRole().getRoleId() == 0 || user.getRole().getRoleId() == 1 || user.getUserId() == sameuserasloggedin) {
				// admin or employee
				
				Account foundaccount = accountdao.FindAccountsByID(intid);
				if (foundaccount != null) {
					
					String sendtoclient  =  mapper.writeValueAsString(foundaccount);
					pw.println(sendtoclient);
					response.setStatus(200);
				} else {
					response.setStatus(400);
					pw.println("Account not found");
				}
			}
		}
	}
}

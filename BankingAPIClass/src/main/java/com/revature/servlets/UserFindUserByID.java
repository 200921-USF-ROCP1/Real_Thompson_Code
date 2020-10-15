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
//import com.revature.Model.ReadUserId;
import com.revature.Model.User;
import com.revature.dao.UserDAO;
import com.revature.dao.UserImpl;

@WebServlet("/users/:id")
public class UserFindUserByID extends HttpServlet {

	public UserFindUserByID() {
		// TODO Auto-generated constructor stub

	}

	@Override
	// log in first, then run this
	// find user by id
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		
		PrintWriter pw = response.getWriter();
		User user = (User) request.getSession().getAttribute(DAOUtilities.LOGGED_IN_KEY);
		if (user != null) {
			String userid = request.getParameter("id"); // use when using the param tab in postman
			
			
			int intid = Integer.parseInt(userid);
			if (user.getRole().getRoleId() == 0 || user.getRole().getRoleId() == 1) {
				// admin or employee
				UserDAO userdao = new UserImpl();
				User founduser = userdao.FindUserByID(intid);
				if (founduser != null) {
					ObjectMapper map = new ObjectMapper();
					String userfnd = map.writeValueAsString(founduser);
					pw.println(userfnd);
					response.setStatus(200);
				} else {
					response.setStatus(400);
					pw.println("User not found");
				}
			}
		}
		else
		{
			pw.println(DAOUtilities.USER_NOT_LOGGED_IN);
		}
	}
}

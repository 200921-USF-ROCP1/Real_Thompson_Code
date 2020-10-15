package com.revature.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.DAOUtilities.DAOUtilities;
import com.revature.Model.*;
import com.revature.dao.UserDAO;
import com.revature.dao.UserImpl;

@WebServlet("/users")
public class UsersServlet extends HttpServlet {

	public UsersServlet() {
		// TODO Auto-generated constructor stub
	}

	@Override
	// log in first, then run this
	// find all users
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter pw = response.getWriter();
		User user = (User) request.getSession().getAttribute(DAOUtilities.LOGGED_IN_KEY);
		if (user != null) {
			if (user.getRole().getRoleId() == 0 || user.getRole().getRoleId() == 1) {
				
				// admin or employee
				UserDAO userdao = new UserImpl();
				List<User> list = userdao.FindUsers();
				ObjectMapper mapper = new ObjectMapper(); // Create the mapper
				String jsonstring = mapper.writeValueAsString(list);
				pw.println(jsonstring);

				response.setStatus(200);
			} else {
				response.setStatus(400);

				pw.println("User not authorized to look up other users");
			}
		}
		else
		{
			pw.println(DAOUtilities.USER_NOT_LOGGED_IN);
		}
	}

	@Override
	// log in first, then run this-Update User
	// update user
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter pw = response.getWriter();

		User loginuser = (User) request.getSession().getAttribute(DAOUtilities.LOGGED_IN_KEY);
		if (loginuser != null) {

			ObjectMapper toclass = new ObjectMapper();
			User userguy = toclass.readValue(request.getReader(), User.class);

			if (loginuser.getRole().getRoleId() == 0 || loginuser.getUserId() == userguy.getUserId()) {
				// admin or employee
				UserDAO userdao = new UserImpl();
				boolean updatedUser = userdao.UpdateUserDAO(userguy);
				if (updatedUser) {
					User founduser = userdao.FindUserByID(userguy.getUserId());
					String jsonuser = toclass.writeValueAsString(founduser);
					pw.println(jsonuser);
					response.setStatus(200);
				} else {
					response.setStatus(400);
					pw.println("User not found");
				}
			} else {
				response.setStatus(400);

				pw.println("User not authorized to look up other users");
			}
		}
		else
		{
			pw.println(DAOUtilities.USER_NOT_LOGGED_IN);
		}
	}

	private void PrintUser(User user, HttpServletResponse response) {
		try {
			PrintWriter pw = response.getWriter();

			pw.println(user.getUserId());
			pw.println(user.getUsername());
			pw.println(user.getFirstName());
			pw.println(user.getLastName());
			pw.println(user.getEmail());
			pw.println(user.getRole().getRole());

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}

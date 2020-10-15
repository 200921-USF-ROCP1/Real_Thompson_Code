package com.revature.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revature.DAOUtilities.DAOUtilities;
import com.revature.Model.User;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

	final String SUCCESS_LOGOUT = "{\"message\": \"You have successfully logged out "; // followed by username
	final String NO_USER = "{\"message\": \"There was no user logged into the session\"}";

	public LogoutServlet() {
		// TODO Auto-generated constructor stub
	}

	// logout
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter pw = response.getWriter();

		try {
			User user = (User) request.getSession().getAttribute(DAOUtilities.LOGGED_IN_KEY);
			if (user != null) {
				String name = user.getFirstName() + " " + user.getLastName();
				request.getSession().setAttribute(DAOUtilities.LOGGED_IN_KEY, null);
				pw.println(SUCCESS_LOGOUT + name + "\"}");
				response.setStatus(200);
			} else {
				request.getSession().setAttribute(DAOUtilities.LOGGED_IN_KEY, null);
				pw.println(NO_USER);
				response.setStatus(400);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			pw.println(NO_USER);
			response.setStatus(400);
		} finally {

		}
	}

}

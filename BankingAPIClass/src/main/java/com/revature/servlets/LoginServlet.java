package com.revature.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.DAOUtilities.DAOUtilities;
import com.revature.Model.Account;
import com.revature.Model.LoginClass;
import com.revature.Model.User;
import com.revature.Model.UserIdClass;
import com.revature.dao.UserDAO;
import com.revature.dao.UserImpl;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

	final String INVALID_CREDS = "{\"message\": \"Invalid Credentials\"}";
	public LoginServlet() {
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter pw = response.getWriter();
		ObjectMapper mapper = new ObjectMapper(); // Create the mapper
		LoginClass unmarshalled = null;
		
		try {
			unmarshalled = mapper.readValue(request.getReader(), LoginClass.class); // Unmarshal

			// Populate the list into a variable that will be stored in the session
			UserDAO dao = new UserImpl(); 
			User theUser = dao.LoginDAO(unmarshalled.username, unmarshalled.password);

			if (theUser.getUserId() != 0) {
				request.getSession().setAttribute(DAOUtilities.LOGGED_IN_KEY, theUser);
				String jsonString = mapper.writeValueAsString(theUser); // To marshal to a String
				pw.println(jsonString);
				response.setStatus(200);
			} else {
				request.getSession().setAttribute(DAOUtilities.LOGGED_IN_KEY, null);
				pw.println(INVALID_CREDS); 
						
				response.setStatus(400);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			pw.println(INVALID_CREDS);
			response.setStatus(400);
		}
	}
}

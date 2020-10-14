package com.revature.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.dmr.Parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.Model.Account;
import com.revature.Model.Role;
import com.revature.Model.User;
import com.revature.dao.UserDAO;
import com.revature.dao.UserImpl;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

	public RegisterServlet() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter pw = response.getWriter();
		User user = new User();
		User userLoggedIn = (User) request.getSession().getAttribute("UserLoggedIn");
		if (userLoggedIn != null) {
			if (userLoggedIn.getRole().getRoleId() == 0) {
				ObjectMapper mapper = new ObjectMapper(); // Create the mapper
				User unmarshalled = mapper.readValue(request.getReader(), User.class); // Unmarshal

				UserDAO dao = new UserImpl(); // .getBookDAO();
				User successfulRegister = dao.Register(unmarshalled);

				if (successfulRegister != null) {
					String jsonString = mapper.writeValueAsString(successfulRegister); // To marshal to a String
					response.setStatus(201);
					pw.println(jsonString);
				} else {
					response.setStatus(400);
					pw.println("{\"message\": \"Invalid fields\"}");
					// could be a violatiion of a unique key--email or username.
				}
			} else {
				pw.println("{\"message\": \"Invalid fields\"}");
			}

		} else {
			pw.println("{\"message\": \"Invalid fields\"}");
		}

	}
}

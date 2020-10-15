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
import com.revature.DAOUtilities.DAOUtilities;
import com.revature.Model.Account;
import com.revature.Model.Linkage;
import com.revature.Model.Role;
import com.revature.Model.User;
import com.revature.dao.UserDAO;
import com.revature.dao.UserImpl;

@WebServlet("/users/linking")
public class LinkUserToAccount extends HttpServlet {

	// link user to account
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter pw = response.getWriter();
		User userLoggedIn = (User) request.getSession().getAttribute(DAOUtilities.LOGGED_IN_KEY);
		if (userLoggedIn != null) {
			int roleplayed = userLoggedIn.getRole().getRoleId();
			if (roleplayed == 0 || roleplayed == 1) {
				ObjectMapper mapper = new ObjectMapper(); // Create the mapper
				Linkage unmarshalled = mapper.readValue(request.getReader(), Linkage.class); // Unmarshal
				
				int intid = unmarshalled.userid; //usInteger.parseInt(userid);
				int acctid = unmarshalled.accountid; // Integer.parseInt(accid);

				UserDAO dao = new UserImpl(); // .getBookDAO();
				boolean successfulRegister = dao.LinkUserToAccount(intid, acctid);

				
				
				if (successfulRegister) {
					String message = "{\"message\": \"User " + ((Integer) intid).toString() + 
							" linked to account " + ((Integer) acctid).toString() + "\"}";
					response.setStatus(201);
					pw.println(message);
				} else {
					response.setStatus(400);
					pw.println("{\"message\": \"Invalid fields\"}");
					// could be a violatiion of a unique key--email or username.
				}
			} else {
				pw.println("{\"message\": \"Invalid fields\"}");
			}

		} 
		else
		{
			pw.println(DAOUtilities.USER_NOT_LOGGED_IN);
		}			
	}
}

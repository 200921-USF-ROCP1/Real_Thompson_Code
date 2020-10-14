package com.revature.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/error")
public class ErrorCatcher extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int debug = 0;
		PrintWriter pw = response.getWriter();

		int statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		String exceptionType = ((Class) request.getAttribute("javax.servlet.error.exception_type")).toString();
		Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
		String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");
		response.setStatus(400);
		  if (exception != null) {
			  
			  String errormess = "{\"error\": {";
//	    	  pw.println("<p>Exception type: " + exceptionType + "</p>");
//	    	  pw.println("<p>Exception message: " + exception.getMessage() + "</p>");
//	    	  
//	    	  pw.println("<div>Exception stack trace: <ul>");
	    	  
	    	  StackTraceElement[] stackTrace = exception.getStackTrace();
	    	  for (int i = 0; i < stackTrace.length; ++i) {
	    		  //pw.print("<li>" + stackTrace[i] + "</li>");
	    		  errormess += stackTrace[i] + ",";
	    	  }
	    	  
	    	  String NoComma = "";
	    	  if (errormess != "")
	    	  {
	    		  NoComma = errormess.substring(0, errormess.length() - 1);	
	    		  NoComma += "\"}";
	    		  pw.print(errormess);
	    	  }
	    	 
	    	 
	      }
		
	}

	public ErrorCatcher() {
		// TODO Auto-generated constructor stub
	}

}

package com.revature.DAOUtilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.revature.dao.UserDAO;
import com.revature.dao.UserImpl;

import java.sql.Blob;

/*import examples.pubhub.dao.BookDAO;
import examples.pubhub.dao.BookDAOImpl;
import examples.pubhub.model.Book;*/

/**
 * Class used to retrieve DAO Implementations. Serves as a factory. Also manages
 * a single instance of the database connection.
 */
public class DAOUtilities {

	// Connection connection = null; // Our connection to the database
	//private static PreparedStatement stmt = null; // We use prepared statements to help protect against SQL injection
	
	public static final String USER_NOT_LOGGED_IN = "{\"message\": \"User not logged in\"}";
	public static final String LOGGED_IN_KEY = "UserLoggedIn"; 

	private static final String CONNECTION_USERNAME = "postgres";
	private static final String CONNECTION_PASSWORD = "password";
	private static final String URL = "jdbc:postgresql://localhost:5432/BankingAPI";
	
	private static Connection connection;

	public static synchronized Connection getConnection() throws SQLException {
		if (connection == null) {
			try {
				Class.forName("org.postgresql.Driver");
			} catch (ClassNotFoundException e) {
				System.out.println("Could not register driver!");
				e.printStackTrace();
			}
			
			connection = DriverManager.getConnection(URL, CONNECTION_USERNAME, CONNECTION_PASSWORD);
		}

		// If connection was closed then retrieve a new connection
		if (connection.isClosed()) {
			System.out.println("Opening new connection...");
			connection = DriverManager.getConnection(URL, CONNECTION_USERNAME, CONNECTION_PASSWORD);
		}
		return connection;
	}

	public static void CloseConnection(PreparedStatement stmt) {
		try {
			if (stmt != null)
				stmt.close();
		} catch (SQLException e) {
			System.out.println("Could not close statement!");
			e.printStackTrace();
		}

		try {
			if (connection != null)
				connection.close();
		} catch (SQLException e) {
			System.out.println("Could not close connection!");
			e.printStackTrace();
		}
	}

	// Closing all resources is important, to prevent memory leaks.
	// Ideally, you really want to close them in the reverse-order you open them
	public static void closeResources(PreparedStatement stmt) {
		try {
			if (stmt != null)
				stmt.close();
		} catch (SQLException e) {
			System.out.println("Could not close statement!");
			e.printStackTrace();
		}

		try {
			if (connection != null)
				connection.close();
		} catch (SQLException e) {
			System.out.println("Could not close connection!");
			e.printStackTrace();
		}
	}
	
	// Does the user who is logged in have security to perform the operation or do we need to 
	// bump up to Admin or other?
	public static boolean CompareUsersByAccountNumber(int acctId, int userId)
	{
		boolean foundUser  = false;
		UserDAO userdao = new UserImpl();
		List<Integer> userOfAccount = userdao.FindUsersByAccount(acctId);
		
		for (Integer useridOfAccountBeingProcessed : userOfAccount) {
		
		   if ((int)useridOfAccountBeingProcessed == userId)	
		   {
			   foundUser = true;
			   break;
		   }
		}
		
		return foundUser;
	}
}

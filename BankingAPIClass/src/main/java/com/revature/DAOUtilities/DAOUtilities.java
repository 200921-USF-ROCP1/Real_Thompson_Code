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

	/*
	 * public static BookDAO getBookDAO() {
	 * 
	 * BookDAOImpl onebook = new BookDAOImpl(); try { connection =
	 * DAOUtilities.getConnection(); // Get our database connection from the manager
	 * String sql = "SELECT * FROM Books"; // Our SQL query PreparedStatement stmt =
	 * connection.prepareStatement(sql); // Creates the prepared statement from the
	 * query
	 * 
	 * ResultSet rs = stmt.executeQuery(); // Queries the database
	 * 
	 * // So long as the ResultSet actually contains results... while (rs.next()) {
	 * // We need to populate a Book object with info for each row from our query //
	 * result Book book = new Book();
	 * 
	 * // Each variable in our Book object maps to a column in a row from our
	 * results. book.setIsbn13(rs.getString("isbn_13"));
	 * book.setAuthor(rs.getString("author")); book.setTitle(rs.getString("title"));
	 * 
	 * // The SQL DATE datatype maps to java.sql.Date... which isn't well supported
	 * // anymore. // We use a LocalDate instead, because this is Java 8.
	 * book.setPublishDate(rs.getDate("publish_date").toLocalDate());
	 * book.setPrice(rs.getDouble("price"));
	 * 
	 * // The PDF file is tricky; file data is stored in the DB as a BLOB - Binary
	 * // Large Object. It's // literally stored as 1's and 0's, so this one data
	 * type can hold any type of // file. book.setContent(rs.getBytes("content"));
	 * 
	 * // Finally we add it to the list of Book objects returned by this query.
	 * //books.add(book);
	 * 
	 * }
	 * 
	 * rs.close();
	 * 
	 * } catch (SQLException e) { e.printStackTrace(); } finally { // We need to
	 * make sure our statements and connections are closed, // or else we could wind
	 * up with a memory leak //closeResources(); }
	 * 
	 * // return the list of Book objects populated by the DB. return onebook; }
	 */

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

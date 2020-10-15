package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLType;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

//import examples.pubhub.model.Book;
import com.revature.DAOUtilities.*;
import com.revature.Model.Account;
import com.revature.Model.Role;
import com.revature.Model.User;

public class UserImpl implements UserDAO {

	private PreparedStatement stmt = null;
	private Connection connection = null;

	public UserImpl() {
		// TODO Auto-generated constructor stub
	}

	public User LoginDAO(String username, String password) {
		// TODO Auto-generated method stub
		User result = new User();
		try {
			connection = DAOUtilities.getConnection(); // Get our database connection from the manager
			String sql =

					"select *\r\n" + "from usertable\r\n" + "inner join roletable on\r\n"
							+ "usertable.user_role = roletable.roleid\r\n" + "where username = ? and "
							+ "user_password = ?"; // Our
													// SQL
													// query;

			stmt = connection.prepareStatement(sql); // Creates the prepared statement from the query
			stmt.setString(1, username);
			stmt.setString(2, password);

			ResultSet rs = stmt.executeQuery(); // Queries the database

			// So long as the ResultSet actually contains results...
			while (rs.next()) {
				int userid = rs.getInt("user_id");
				String gotusername = rs.getString("username");
				String gotpword = "";
				String firstn = rs.getString("firstname");
				String lastn = rs.getString("lastname");
				String email = rs.getString("email");
				int role = rs.getInt("user_role");

				String roleplayed = rs.getString("roleplayed");
				Role roler = new Role();
				roler.setRoleId(role);
				roler.setRole(roleplayed);

				result.setUserId(userid);
				result.setUsername(gotusername);
				result.setPassword(gotpword);
				result.setFirstName(firstn);
				result.setLastName(lastn);
				result.setEmail(email);
				result.setRole(roler);
			}

			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// We need to make sure our statements and connections are closed,
			// or else we could wind up with a memory leak
			DAOUtilities.closeResources(stmt);
		}

		return result;
	}

	public void LogOutDAO() {
		// TODO Auto-generated method stub

	}

	public boolean LinkUserToAccount(int userid, int accountid) {
		boolean success = false;
		try {
			connection = DAOUtilities.getConnection(); // Get our database connection from the manager
			String sql =

					"insert into user_account_table\r\n" + "values (default, ?, ?)";

			stmt = connection.prepareStatement(sql); // Creates the prepared statement from

			// the query
			// values (default, 'username', 'password', 'firstnamen', 'lname', 'emailcom',
			// roleid 0, null);
			stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, userid);
			stmt.setInt(2, accountid);

			if (stmt.executeUpdate() != 0) {

				success = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DAOUtilities.closeResources(stmt);
		}

		return success;
	}

	public User Register(User newuser) {
		// TODO Auto-generated method stub
		boolean registered = false;
		User result = null;
		try {
			connection = DAOUtilities.getConnection(); // Get our database connection from the manager
			String sql = "insert into " + " usertable " + " values " + "(" + " default " + " , " + "?" + " , " + "?"
					+ " , " + "?" + " , " + "?" + " , " + "?" + " , " + "?" + " , " + "?" + ");";

			stmt = connection.prepareStatement(sql); // Creates the prepared statement from

			// the query
			// values (default, 'username', 'password', 'firstnamen', 'lname', 'emailcom',
			// roleid 0, null);
			stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, newuser.getUsername());
			stmt.setString(2, newuser.getPassword());
			stmt.setString(3, newuser.getFirstName());
			stmt.setString(4, newuser.getLastName());
			stmt.setString(5, newuser.getEmail());
			Role role = newuser.getRole();
			stmt.setInt(6, role.getRoleId());
			stmt.setNull(7, Types.NULL); // setNull(6, sqlType);

			if (stmt.executeUpdate() != 0) {

				ResultSet keys = stmt.getGeneratedKeys();
				keys.next();
				int thekey = keys.getInt(1);
				newuser.setUserId(thekey);
				result = newuser;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// We need to make sure our statements and connections are closed,
			// or else we could wind up with a memory leak
			DAOUtilities.closeResources(stmt);
		}

		return result;
	}

	public ArrayList<User> FindUsers() {
		// TODO Auto-generated method stub
		boolean found = false;
		ArrayList<User> list = new ArrayList();
		// UserDAO user = new UserImpl();
		try {
			connection = DAOUtilities.getConnection(); // Get our database connection from the manager

			String sql = "select *\r\n" + "from usertable ut\r\n" + "left join roletable rt on\r\n"
					+ "ut.user_role = rt.roleid";

			stmt = connection.prepareStatement(sql); // Creates the prepared statement from

			ResultSet rs = stmt.executeQuery(); // Queries the database new ResultSet();

			while (rs.next()) {
				User user = new User();
				user.setUserId(rs.getInt("user_id"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("user_password"));
				user.setFirstName(rs.getString("firstname"));
				user.setLastName(rs.getString("lastname"));
				user.setEmail(rs.getString("email"));
				Role roler = new Role();
				roler.setRoleId(rs.getInt("user_role"));
				roler.setDescription(rs.getInt("user_role"));
				user.setRole(roler); // setLastName(rs.getString("lastname"));
				list.add(user);
				// registered = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// We need to make sure our statements and connections are closed,
			// or else we could wind up with a memory leak
			DAOUtilities.closeResources(stmt);
		}

		return list;
	}

	public User FindUserByID(int userid) {
		// TODO Auto-generated method stub

		boolean found = false;
		User user = null;
		// UserDAO user = new UserImpl();
		try {
			connection = DAOUtilities.getConnection(); // Get our database connection from the manager

			String sql = "select *\r\n" + "from usertable ut\r\n" + "left join roletable rt on\r\n"
					+ "ut.user_role = rt.roleid\r\n" + "where ut.user_id = ?";

			stmt = connection.prepareStatement(sql); // Creates the prepared statement from
			stmt.setInt(1, userid);
			ResultSet rs = stmt.executeQuery(); // Queries the database new ResultSet();

			while (rs.next()) {
				user = new User();
				user.setUserId(rs.getInt("user_id"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("user_password"));
				user.setFirstName(rs.getString("firstname"));
				user.setLastName(rs.getString("lastname"));
				user.setEmail(rs.getString("email"));
				Role roler = new Role();
				roler.setRoleId(rs.getInt("user_role"));
				roler.setDescription(rs.getInt("user_role"));
				user.setRole(roler); // setLastName(rs.getString("lastname"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// We need to make sure our statements and connections are closed,
			// or else we could wind up with a memory leak
			DAOUtilities.closeResources(stmt);
		}

		return user;

	}

	public boolean UpdateUserDAO(User user) {
		// TODO Auto-generated method stub

		boolean updated = false;
		try {
			connection = DAOUtilities.getConnection(); // Get our database connection from the manager

			String sql =

					"update usertable\r\n" + "set username = ?, user_password = ?,\r\n"
							+ "firstname = ?, lastname = ?,\r\n" + "email = ?, user_role = ?\r\n"
							+ "where user_id = ?;";

			stmt = connection.prepareStatement(sql); // Creates the prepared statement from
			stmt.setString(1, user.getUsername());
			stmt.setString(2, user.getPassword());
			stmt.setString(3, user.getFirstName());
			stmt.setString(4, user.getLastName());
			stmt.setString(5, user.getEmail());
			stmt.setInt(6, user.getRole().getRoleId());
			stmt.setInt(7, user.getUserId());

			int rowsaffected = stmt.executeUpdate(); // Queries the database new ResultSet();

			if (rowsaffected > 0) {
				updated = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// We need to make sure our statements and connections are closed,
			// or else we could wind up with a memory leak
			DAOUtilities.closeResources(stmt);
		}

		return updated;
	}

	public void SetFields(User user) {
		// TODO Auto-generated method stub

	}

	public List<Integer> FindUsersByAccount(int accountid) {
		// TODO Auto-generated method stub
		int gotuser = -1;
		List<Integer> listofUsersOnAccount = null;
		try {
			connection = DAOUtilities.getConnection(); // Get our database connection from the manager
			String sql =

					"select ut.user_id\r\n" + "from account acc\r\n" + "inner join user_account_table uat on\r\n"
							+ "acc.account_id = uat.account_id\r\n" + "inner join usertable ut on\r\n"
							+ "uat.user_id = ut.user_id\r\n" + "where acc.account_id = ?";

			stmt = connection.prepareStatement(sql);

			stmt.setInt(1, accountid);
			// stmt.setInt(2, userid);
			ResultSet rs = stmt.executeQuery(); // Queries the database new ResultSet();
			while (rs.next()) {
				if (listofUsersOnAccount == null) {
					listofUsersOnAccount = new ArrayList<Integer>();
				}
				Integer gotuserint = rs.getInt(1);
				listofUsersOnAccount.add(gotuserint);
			}

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			// We need to make sure our statements and connections are closed,
			// or else we could wind up with a memory leak
			DAOUtilities.closeResources(stmt);
		}

		// TODO Auto-generated method stub
		return listofUsersOnAccount;
	}

}

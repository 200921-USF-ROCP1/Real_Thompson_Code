package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.DAOUtilities.DAOUtilities;
import com.revature.Model.Account;
import com.revature.Model.AccountStatus;
import com.revature.Model.AccountType;
import com.revature.Model.Role;
import com.revature.Model.User;

public class AccountImpl implements AccountDAO {

	private PreparedStatement stmt = null;
	private Connection connection = null;

	// this method assumes the account already exists
	public String Withdraw(int accountID, double amount) {
		// TODO Auto-generated method stub

		String stringresult = "Could not withdraw amount";
		if (amount >= 0) {

			try {
				String stramount = Double.toString(amount);
				String straccountid = Integer.toString(accountID);
				connection = DAOUtilities.getConnection(); // Get our database connection from the manager
				String sql =

						"update account\r\n" + "set balance =\r\n" + "(\r\n" + "select sum (account.balance - ?)\r\n"
								+ "from account\r\n" + "where account.account_id = ?\r\n" + ")\r\n"
								+ "where account.account_id = ?\r\n";

				stmt = connection.prepareStatement(sql); // Creates the prepared statement from the query
				stmt.setDouble(1, amount);// setString(1, stramount); //(1, accountID);
				stmt.setInt(2, accountID);// (2, straccountid);//t(2, accountID);//(2, s); //(2, accountID);
				stmt.setInt(3, accountID);// (3, straccountid);//t(2, accountID);//(2, s); //(2, accountID);
				int updated = stmt.executeUpdate();
				if (updated != 0) {
					stringresult = "Successful withdrawal";
				}

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				// We need to make sure our statements and connections are closed,
				// or else we could wind up with a memory leak
				DAOUtilities.closeResources(stmt);
			}
		}

		return stringresult;
	}

	public String Deposit(int accountID, double amount) {
		String stringresult = "Could not deposit amount";
		if (amount >= 0) {

			try {
				String stramount = Double.toString(amount);
				String straccountid = Integer.toString(accountID);
				connection = DAOUtilities.getConnection(); // Get our database connection from the manager
				String sql =

						"update account\r\n" + "set balance =\r\n" + "(\r\n" + "select sum (account.balance + ?)\r\n"
								+ "from account\r\n" + "where account.account_id = ?\r\n" + ")\r\n"
								+ "where account.account_id = ?\r\n";

				stmt = connection.prepareStatement(sql); // Creates the prepared statement from the query
				stmt.setDouble(1, amount);// setString(1, stramount); //(1, accountID);
				stmt.setInt(2, accountID);// (2, straccountid);//t(2, accountID);//(2, s); //(2, accountID);
				stmt.setInt(3, accountID);// (3, straccountid);//t(2, accountID);//(2, s); //(2, accountID);
				int updated = stmt.executeUpdate();
				if (updated != 0) {
					stringresult = "Successful deposit";
				}

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				// We need to make sure our statements and connections are closed,
				// or else we could wind up with a memory leak
				DAOUtilities.closeResources(stmt);
			}
		}

		return stringresult;
	}

	public String Transfer(int sourceAccountId, int targetAccountId, double amount) {
		// TODO Auto-generated method stub
		String stringresult = "Could not transfer amount";
		if (amount >= 0) {

			try {
				// connection = DAOUtilities.getConnection(); // Get our database connection
				// from the manager
				String resultWithdraw = this.Withdraw(sourceAccountId, amount);
				if (resultWithdraw.contains("Success")) {
					String resultDeposit = this.Deposit(targetAccountId, amount);
					if (resultDeposit.contains("Success")) {
						stringresult = "Successful transfer";
						stringresult = "$" + Double.toString(amount) + " has been transferred from Account " + "#"
								+ Integer.toString(sourceAccountId) + " to Account # "
								+ Integer.toString(targetAccountId);
					}
				}

			} finally {
				// We need to make sure our statements and connections are closed,
				// or else we could wind up with a memory leak
				DAOUtilities.closeResources(stmt);
			}
		}

		return stringresult;

	}

	public ArrayList<Account> FindAccounts() {
		boolean found = false;
		ArrayList<Account> list = new ArrayList<Account>();
		try {
			connection = DAOUtilities.getConnection(); // Get our database connection from the manager

			String sql =

					"select *\r\n" + "from account ac\r\n" + "left join accountstatus ast on\r\n"
							+ "ac.account_status = ast.account_status_id\r\n" + "left join accounttype atp on\r\n"
							+ "ac.account_type = atp.account_type_id";

			stmt = connection.prepareStatement(sql); // Creates the prepared statement from

			ResultSet rs = stmt.executeQuery(); // Queries the database new ResultSet();

			while (rs.next()) {
				Account account = new Account();
				this.FillAccount(account, rs);
				list.add(account);
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

	public Account FindAccountsByID(int accountID) {
		// TODO Auto-generated method stub
//

		boolean found = false;
		Account account = null; // new Account();
		try {
			connection = DAOUtilities.getConnection(); // Get our database connection from the manager

			String sql =

					"select *\r\n" + "from account ac\r\n" + "left join accountstatus ast on\r\n"
							+ "ac.account_status = ast.account_status_id\r\n" + "left join accounttype atp on\r\n"
							+ "ac.account_type = atp.account_type_id\r\n" + "where ac.account_id = ?";

			stmt = connection.prepareStatement(sql); // Creates the prepared statement from
			stmt.setInt(1, accountID);
			ResultSet rs = stmt.executeQuery(); // Queries the database new ResultSet();

			while (rs.next()) {
				account = new Account();
				this.FillAccount(account, rs);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// We need to make sure our statements and connections are closed,
			// or else we could wind up with a memory leak
			DAOUtilities.closeResources(stmt);
		}

		return account;

	}

	public ArrayList<Account> FindAccountsByStatus(AccountStatus status) {
		// TODO Auto-generated method stub
		boolean found = false;
		ArrayList<Account> list = new ArrayList<Account>();
		try {
			connection = DAOUtilities.getConnection(); // Get our database connection from the manager

			String sql =

					"select *\r\n" + "from account ac\r\n" + "left join accountstatus ast on\r\n"
							+ "ac.account_status = ast.account_status_id\r\n" + "left join accounttype atp on\r\n"
							+ "ac.account_type = atp.account_type_id\r\n" + "where ac.account_status = ?";

			stmt = connection.prepareStatement(sql); // Creates the prepared statement from
			stmt.setInt(1, status.getStatusId());
			ResultSet rs = stmt.executeQuery(); // Queries the database new ResultSet();

			while (rs.next()) {
				Account account = new Account();
				this.FillAccount(account, rs);

				list.add(account);
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

	public List<Account> FindAccountsByUser(int userId) {
		// TODO Auto-generated method stub
		boolean found = false;
		ArrayList<Account> list = null; //new ArrayList<Account>();
		try {
			connection = DAOUtilities.getConnection(); // Get our database connection from the manager

			String sql =

					"select ut.user_id, ut.username, acc.account_id, acc.balance, acc.account_status,\r\n" +
					  "acst.account_status, acc.account_type, acctyp.account_type\r\n" +  
					"from usertable ut\r\n" +
					"inner join user_account_table uat on\r\n" +
					"ut.user_id = uat.user_id\r\n" +
					"inner join account acc on\r\n" +
					"uat.account_id = acc.account_id\r\n" +
					"left join accountstatus acst on\r\n" +
					"acc.account_status = acst.account_status_id\r\n" +
					"left join accounttype acctyp on\r\n" +
					"acc.account_type = acctyp.account_type_id\r\n" +
					"where ut.user_id = ?";


			stmt = connection.prepareStatement(sql); // Creates the prepared statement from
			stmt.setInt(1, userId);
			ResultSet rs = stmt.executeQuery(); // Queries the database new ResultSet();

			while (rs.next()) {
				
				if (list == null)
				{
					list = new ArrayList<Account>();
				}
				Account account = new Account();
				account.setAccountId(rs.getInt("account_id"));
				account.setBalance(rs.getDouble("balance"));
				AccountStatus accstat = new AccountStatus();
				int tempstatus = rs.getInt(5);
				String tempStringStatus = rs.getString(6);
				accstat.setStatusId(tempstatus);
				accstat.setStatus(tempStringStatus);
				account.setStatus(accstat);
				AccountType acctype = new AccountType();;
				int acctypeint = rs.getInt(7);
				acctype.setTypeId(acctypeint);
				acctype.setType(rs.getString(8));
				account.setType(acctype);
				list.add(account);
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

	public Account SubmitAccount(Account account) {
		// TODO Auto-generated method stub
		Account result = null; // "Could not deposit amount";

		try {
			connection = DAOUtilities.getConnection(); // Get our database connection from the manager
			String sql =

					"insert into account\r\n" + "values (default, ?, ?, ?)";

			stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setDouble(1, account.getBalance());
			AccountStatus acst = account.getStatus();
			stmt.setInt(2, acst.getStatusId());
			AccountType type = account.getType();
			stmt.setInt(3, type.getTypeId());

			int updated = stmt.executeUpdate();
			if (updated != 0) {
				ResultSet keys = stmt.getGeneratedKeys();
				keys.next();
				int thekey = keys.getInt(1);
				account.setAccountId(thekey);
				result = account;
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

	public Account UpdateAccount(Account account) {
		Account result = null; // "Could not deposit amount";

		try {
			connection = DAOUtilities.getConnection(); // Get our database connection from the manager

			String sql =

					"update account\r\n" + "set balance = ?, account_status = ?, account_type = ?\r\n"
							+ "where account_id = ?";
			stmt = connection.prepareStatement(sql);

			stmt.setDouble(1, account.getBalance());
			AccountStatus acst = account.getStatus();
			stmt.setInt(2, acst.getStatusId());
			AccountType type = account.getType();
			stmt.setInt(3, type.getTypeId());
			stmt.setInt(4, account.getAccountId());

			int updated = stmt.executeUpdate();
			if (updated != 0) {
				result = account;
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

	private void FillAccount(Account account, ResultSet rs) {
		try {
			account.setAccountId(rs.getInt("account_id"));
			account.setBalance(rs.getDouble("balance"));
			AccountStatus accstat = new AccountStatus();
			accstat.setStatusId(rs.getInt("account_status_id"));
			accstat.setStatus(rs.getString(6));
			account.setStatus(accstat);
			AccountType acctype = new AccountType();
			acctype.setTypeId(rs.getInt("account_type_id"));
			acctype.setType(rs.getString(8));
			account.setType(acctype);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	// not needed
//	public int FindUserByAccountIdAndUserId(int account, int userid) {
//		
//		int gotuser = -1;
//		try {
//			connection = DAOUtilities.getConnection(); // Get our database connection from the manager
//			String sql =
//
//					"select ut.user_id\r\n" + "from account acc\r\n" + "inner join user_account_table uat on\r\n"
//							+ "acc.account_id = uat.account_id\r\n" + "inner join usertable ut on\r\n"
//							+ "uat.user_id = ut.user_id\r\n" + "where acc.account_id = ? and ut.user_id = ?";
//
//			stmt = connection.prepareStatement(sql);
//			
//			stmt.setInt(1, account);
//			stmt.setInt(2, userid);
//			ResultSet rs = stmt.executeQuery(); // Queries the database new ResultSet();
//			while (rs.next()) {
//			  gotuser = rs.getInt(1);
//			}
//			
//		} catch (Exception ex) {
//			ex.printStackTrace();
//
//		}
//		finally {
//			// We need to make sure our statements and connections are closed,
//			// or else we could wind up with a memory leak
//			DAOUtilities.closeResources(stmt);
//		}
//
//		// TODO Auto-generated method stub
//		return gotuser;
//	}
}

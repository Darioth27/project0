package com.projectzero.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.projectzero.model.BankAccount;
import com.projectzero.model.User;
import com.projectzero.util.ConnectionUtil;

public class UserDao implements BankDao {

	private static UserDao bankDao;
	final static Logger log = Logger.getLogger(UserDao.class);
	
	private UserDao() {
		
	}
	
	public static UserDao getInstance() {
		if (bankDao == null) {
			bankDao = new UserDao();
		}
		return bankDao;
	}

	public boolean insertProcedure(User user) {
		try(Connection conn = ConnectionUtil.getConnection()){
			int statementIndex = 0;
			
			String storedProc = "CALL INSERT_USER(?,?,?,?,?)";

			CallableStatement c = conn.prepareCall(storedProc);
			
			//Set attributes to insert
			c.setString(++statementIndex, user.getUsername());
			c.setString(++statementIndex, user.getPassword());
			c.setLong(++statementIndex, user.getPinNumber());
			c.setString(++statementIndex, user.getFirstName());
			c.setString(++statementIndex, user.getLastName());
			
			if(c.executeUpdate() == 0) {
				return true;
			}
			
		} catch (SQLException e) {
			log.error("Exception occured in insertProcedure: " + e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean insertAdminProcedure(User user) {
		try(Connection conn = ConnectionUtil.getConnection()){
			int statementIndex = 0;
			
			String storedProc = "CALL INSERT_ADMIN(?,?,?,?,?)";

			CallableStatement c = conn.prepareCall(storedProc);
			
			//Set attributes to insert
			c.setString(++statementIndex, user.getUsername());
			c.setString(++statementIndex, user.getPassword());
			c.setLong(++statementIndex, user.getPinNumber());
			c.setString(++statementIndex, user.getFirstName());
			c.setString(++statementIndex, user.getLastName());
			//c.regisOut...soemthing
			if(c.executeUpdate() == 0) {
				return true;
			}
			
		} catch (SQLException e) {
			log.error("Exception occured in insertAdminProcedure: " + e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	public User select(User user) {
		try (Connection conn = ConnectionUtil.getConnection()) {
			String sql = "select * from useraccount";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				return new User(
						rs.getInt("USER_ID"),
						rs.getString("USER_NAME"),
						rs.getString("PASS_WORD"),
						rs.getInt("PIN_NUMBER"),
						rs.getString("FIRST_NAME"),
						rs.getString("LAST_NAME"),
						rs.getString("USER_TYPE"));
			}
		} catch(SQLException s) {
			System.out.println(s.getMessage());
		}
		return new User();
	}
	
	public User selectAndValidate(String username, String password) {
		try (Connection conn = ConnectionUtil.getConnection()) {
			int statementIndex = 0;
			
			//String storedProc = "CALL VALIDATE_USER(?,?)";

			//CallableStatement c = conn.prepareCall(storedProc);
			String sql = "SELECT * FROM UserAccount WHERE USER_NAME=? AND PASS_WORD=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			
			//Set attributes to insert
			ps.setString(++statementIndex, username);
			ps.setString(++statementIndex, password);
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				return new User(
						rs.getInt("USER_ID"),
						rs.getString("USER_NAME"),
						rs.getString("PASS_WORD"),
						rs.getInt("PIN_NUMBER"),
						rs.getString("FIRST_NAME"),
						rs.getString("LAST_NAME"),
						rs.getString("USER_TYPE"));
			}
		} catch(SQLException s) {
			log.error("Exception occured in selectAndValidate: " + s.getMessage());
			System.out.println(s.getMessage());
		}
		return null;
	}

	public List<User> selectAll() {
		List<User> newUsers = new ArrayList<User>();
		try (Connection conn = ConnectionUtil.getConnection()) {

			String sql = "SELECT * FROM UserAccount";
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				newUsers.add(new User(
						rs.getInt("USER_ID"),
						rs.getString("USER_NAME"),
						rs.getString("PASS_WORD"),
						rs.getInt("PIN_NUMBER"),
						rs.getString("FIRST_NAME"),
						rs.getString("LAST_NAME"),
						rs.getString("USER_TYPE")));
			}
		} catch(SQLException s) {
			log.error("Exception occured in selectAll: " + s.getMessage());
		}
		return newUsers;
	}
	
	public List<User> selectAllNew() {
		List<User> newUsers = new ArrayList<User>();
		try (Connection conn = ConnectionUtil.getConnection()) {

			String sql = "SELECT * FROM UserAccount WHERE USER_TYPE = 'new'";
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				newUsers.add(new User(
						rs.getInt("USER_ID"),
						rs.getString("USER_NAME"),
						rs.getString("PASS_WORD"),
						rs.getInt("PIN_NUMBER"),
						rs.getString("FIRST_NAME"),
						rs.getString("LAST_NAME"),
						rs.getString("USER_TYPE")));
			}
		} catch(SQLException s) {
			log.error("Exception occured in selectAllNew: " + s.getMessage());
		}
		return newUsers;
	}
	
	public boolean approveUser(User user) {
		try(Connection conn = ConnectionUtil.getConnection()){
			int statementIndex = 0;
			
			String storedProc = "CALL APPROVE_USER(?)";

			CallableStatement c = conn.prepareCall(storedProc);
			
			//Set attributes to insert
			c.setLong(++statementIndex, user.getUserID());
			
			if(c.executeUpdate() == 0) {
				return true;
			}
			
		} catch (SQLException e) {
			log.error("Exception occured in approveUser: " + e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean createBankAccount(User user) {
		try(Connection conn = ConnectionUtil.getConnection()){
			int statementIndex = 0;
			
			String storedProc = "CALL INSERT_ACCOUNT(?)";

			CallableStatement c = conn.prepareCall(storedProc);
			
			//Set attributes to insert
			c.setLong(++statementIndex, user.getUserID());
			
			if(c.executeUpdate() == 0) {
				return true;
			}
			
		} catch (SQLException e) {
			log.error("Exception occured in createBankAccount: " + e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	public List<BankAccount> viewBankAccounts(User user) {
		List<BankAccount> accounts = new ArrayList<BankAccount>();
		try (Connection conn = ConnectionUtil.getConnection()) {

			String sql = "SELECT * FROM ACCOUNTLIST WHERE USER_ID=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, user.getUserID());
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				accounts.add(new BankAccount(
						rs.getInt("ACCOUNT_ID"),
						rs.getDouble("BALANCE")));
			}
		} catch(SQLException s) {
			log.error("Exception occured in viewBankAccounts: " + s.getMessage());
		}
		return accounts;
	}
	
	public boolean withdraw(BankAccount ba, double amount) {
		try(Connection conn = ConnectionUtil.getConnection()){
			int statementIndex = 0;
			
			String storedProc = "CALL WITHDRAW_FROM_ACCOUNT(?,?)";

			CallableStatement c = conn.prepareCall(storedProc);
			
			//Set attributes to insert
			c.setLong(++statementIndex, ba.getAccountID());
			c.setDouble(++statementIndex, amount);
			
			if(c.executeUpdate() == 0) {
				return true;
			}
			
		} catch (SQLException e) {
			log.error("Exception occured in withdraw: " + e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean deposit(BankAccount ba, double amount) {
		try(Connection conn = ConnectionUtil.getConnection()){
			int statementIndex = 0;
			
			String storedProc = "CALL DEPOSIT_IN_ACCOUNT(?,?)";

			CallableStatement c = conn.prepareCall(storedProc);
			
			//Set attributes to insert
			c.setLong(++statementIndex, ba.getAccountID());
			c.setDouble(++statementIndex, amount);
			
			if(c.executeUpdate() == 0) {
				return true;
			}
			
		} catch (SQLException e) {
			log.error("Exception occured in deposit: " + e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	public String getUserHash(User user) {
		// TODO Auto-generated method stub
		return null;
	}

}

package com.projectzero.service;

import java.util.List;

import org.apache.log4j.Logger;

import com.projectzero.dao.UserDao;
import com.projectzero.model.BankAccount;
import com.projectzero.model.User;

public class UserService {
	//private instance of UserService variable
	private static UserService userService;
	final static Logger log = Logger.getLogger(UserService.class);
	//applying singleton singleton pattern
	private UserService() {
	}

	//to access the instance of the UserService using userService
	public static UserService getInstance() {
		if (userService == null) {
			userService = new UserService();
		}
		return userService;
	}
	
	//calls the insert method which will call the stored procedure named "insert_user"
	public boolean registerUserSecure(User user) {
		return UserDao.getInstance().insertProcedure(user);
	}
	
	public boolean registerAdminSecure(User user) {
		return UserDao.getInstance().insertAdminProcedure(user);
	}
	
	public boolean approveUser(User user) {
		return UserDao.getInstance().approveUser(user);
	}
	//service to call selectAll method in DAO
	public List<User> listAllUsers(){
		return UserDao.getInstance().selectAll();
	}
	
	//
	public User login(String username, String password) {
		return UserDao.getInstance().selectAndValidate(username, password);
	}
	
	public List<User> getNewUserList() {
		return UserDao.getInstance().selectAllNew();
	}
	
	public boolean createNewAccount(User user) {
		return UserDao.getInstance().createBankAccount(user);
	}
	
	public List<BankAccount> getBankAccountList(User user) {
		return UserDao.getInstance().viewBankAccounts(user);
	}
	
	public boolean withdraw(BankAccount ba, double amount) {
		return UserDao.getInstance().withdraw(ba, amount);
	}
	
	public boolean deposit(BankAccount ba, double amount) {
		return UserDao.getInstance().deposit(ba, amount);
	}
}

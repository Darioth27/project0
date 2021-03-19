package com.projectzero.dao;

import java.util.List;

import com.projectzero.model.BankAccount;
import com.projectzero.model.User;

public interface BankDao {

	public boolean insertProcedure(User user);
	public User select(User user);
	public List<User> selectAllNew();
	public List<User> selectAll();
	public boolean deposit(BankAccount ba, double amount);
	public boolean withdraw(BankAccount ba, double amount);
	public boolean createBankAccount(User user);
	public List<BankAccount> viewBankAccounts(User user);
	public String getUserHash(User user);
}

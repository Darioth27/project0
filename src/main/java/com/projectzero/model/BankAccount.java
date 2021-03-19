package com.projectzero.model;

public class BankAccount {

	private int accountID;
	private double balance;
	
	public BankAccount() {
		
	}

	public BankAccount(int accountID, double balance) {
		super();
		this.accountID = accountID;
		this.balance = balance;
	}

	public int getAccountID() {
		return accountID;
	}

	public void setAccountID(int accountID) {
		this.accountID = accountID;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	
}

package com.projectzero.model;

public class User {
	private int userID;
	private String username;
	private String password;
	private int pinNumber;
	private String firstName;
	private String lastName;
	private String userType;
	
	public User() {
		
	}
	
	public User(int userID, String username, String password, int pinNumber, String firstName, String lastName,
			String userType) {
		super();
		this.userID = userID;
		this.username = username;
		this.password = password;
		this.pinNumber = pinNumber;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userType = userType;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserId(int userId) {
		this.userID = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getPinNumber() {
		return pinNumber;
	}

	public void setPinNumber(int pinNumber) {
		this.pinNumber = pinNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	
	
}

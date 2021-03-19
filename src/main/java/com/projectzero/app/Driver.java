package com.projectzero.app;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.projectzero.model.BankAccount;
import com.projectzero.model.User;
import com.projectzero.service.UserService;

public class Driver {
	
	private static Scanner scanner;
	private static User currentUser;
	final static Logger log = Logger.getLogger(Driver.class);
	
	public static void main(String[] args) {
		scanner = new Scanner( System.in );
	    mainMenu();
	    scanner.close();
	    System.exit(0);
	}
	
	private static void mainMenu() {
		System.out.println("\n\n\n");
		System.out.println("  ----------------------------------------------------------------------");
		System.out.println("/ ---------------------------------------------------------------------  \\");
		System.out.println("| |                  $$$$$      $$     $$  $$   $$  $$                  | |");
		System.out.println("| |                  $$  $$    $$$$    $$$ $$   $$ $$                   | |");
		System.out.println("| |                  $$$$$    $$  $$   $$$$$$   $$$$                    | |");
		System.out.println("| |                  $$  $$   $$$$$$   $$ $$$   $$  $$                  | |");
		System.out.println("| |                  $$$$$    $$  $$   $$  $$   $$  $$                  | |");
		System.out.println("\\ ---------------------------------------------------------------------  /");
		System.out.println("  ----------------------------------------------------------------------\n\n");
	    System.out.println("Welcome to BANK!  What would you like to do?" );
	    System.out.println("1. Login");
	    System.out.println("2. Create New Account");
	    System.out.println("3. Exit");
	    System.out.print( "Enter option number: ");

	    int option = verifyOption(1, 3);

	    if (option == 1) {
	    	loginProcess();
	    }
	    else if (option == 2) {
	    	accountCreationProcess();
	    }
	    else if (option == 3) {
	    	System.out.println("Good-bye.  Come again!");
	    	System.exit(0);
	    }
	}
	
	private static int verifyOption(int first, int last) {
		int option = 0;
	    while (option == 0) {
	    	try {
	    		option = Integer.parseInt(scanner.nextLine());
	    		if (option < first || option > last) {
	    			throw new IllegalArgumentException();
	    		}
	    	} catch (Exception e) {
	    		option = 0;
	    		System.out.print("Invalid option.  Please try again: ");
	    	}
	    }
	    return option;
	}
	
	private static void loginProcess() {
		space();
		System.out.print("Please enter your username: ");
		String username = scanner.nextLine();
		System.out.print("Please enter your password: ");
		String password = scanner.nextLine();
		currentUser = UserService.getInstance().login(username, password);
		if (currentUser == null) {
			System.out.println("Incorrect username or password.  Try again.");
			loginProcess();
		}
		else if (currentUser.getUserType().equals("new")) {
			System.out.println("Account still awaiting admin approval.  Try again later.\n");
			log.info("Login of unapproved account attempted.");
			currentUser = null;
			try {
				Thread.sleep(2500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			mainMenu();
		}
		else if (currentUser.getUserType().equals("user")) {
			userMenu();
		}
		else if (currentUser.getUserType().equals("admin")) {
			adminMenu();
		}
	}
	
	private static void userMenu() {
		space();
		System.out.println("Welcome, " + currentUser.getFirstName() + " " + currentUser.getLastName() + 
				".\nWhat would you like to do?\n"
				+ "1. Open new banking account\n"
				+ "2. Access existing account\n"
				+ "3. Quit");
		System.out.print("Enter option: ");
		int option = verifyOption(1, 3);
		if (option == 1) {
			bankAccountCreation();
		}
		else if (option == 2) {
			viewBankAccounts();
		}
		else if(option == 3) {
			System.out.println("\nLogged out.");
			currentUser = null;
			mainMenu();
		}
	}
	
	private static void adminMenu() {
		space();
		System.out.println("Welcome, " + currentUser.getFirstName() + " " + currentUser.getLastName() + 
				".\nWhat would you like to do?\n"
				+ "1. Open new banking account\n"
				+ "2. Access existing account\n"
				+ "3. Approve new accounts\n"
				+ "4. View all accounts\n"
				+ "5. Log out");
		System.out.print("Enter option: ");
		int option = verifyOption(1, 5);
		if (option == 1) {
			bankAccountCreation();
		}
		else if (option == 2) {
			viewBankAccounts();
		}
		else if (option == 3) {
			while (true) {
				ArrayList<User> newUsers = (ArrayList<User>) UserService.getInstance().getNewUserList();
				if (newUsers.size() < 1) {
					System.out.println("There are currently no users awaiting approval");
					break;
				}
				System.out.println("Currently " + newUsers.size() + " new users awaiting approval:\n");
				System.out.println("--------------------------------------------"
						+ "-----------------------------------------------------");
				System.out.format("%10s%20s%20s%20s%20s\n", "ID", "Username", "First Name", "Last Name", "Status");
				System.out.println("--------------------------------------------"
						+ "-----------------------------------------------------");
				for (User u : newUsers) {
					System.out.format("%10d%20s%20s%20s%20s\n",
							u.getUserID(), 
							u.getUsername(), 
							u.getFirstName(),
							u.getLastName(),
							u.getUserType());
					//System.out.println(u.getUserID()+ "\t\t" + u.getUsername() + "\t\t" + u.getFirstName() + 
					//		"\t\t" + u.getLastName() + "\t\t" + u.getUserType());
				}
				System.out.println("--------------------------------------------"
						+ "-----------------------------------------------------\n");
			
				System.out.println("Enter ID of those you wish approved or type 'back' to go back: ");
				String input = scanner.nextLine();
				if (input.equals("back")) {
					break;
				}
				else {
					int id = 0;
				    try {
				    	id = Integer.parseInt(input);
				    	if (option < 0 || option > 10000000) {
				    		throw new IllegalArgumentException();
				    	}
				    } catch (Exception e) {
				    	id = 0;
				    	continue;
				    }
				    boolean found = false;
					for (User temp : newUsers) {
						if (temp.getUserID() == id) {
							if (UserService.getInstance().approveUser(temp)) {
								System.out.println("Account " + "'" + temp.getUsername() + "' has been approved.");
								found = true;
							}
							break;
						}
					}
					if (!found) {
						System.out.println("User ID does not exist or has already been approved.");
					}
				}
			}
			adminMenu();
		}
		else if (option == 4) {
			ArrayList<User> allUsers = (ArrayList<User>) UserService.getInstance().listAllUsers();
			System.out.println("--------------------------------------------"
					+ "-----------------------------------------------------");
			System.out.format("%10s%20s%20s%20s%20s\n", "ID", "Username", "First Name", "Last Name", "Status");
			System.out.println("--------------------------------------------"
					+ "-----------------------------------------------------");
			for (User u : allUsers) {
				System.out.format("%10d%20s%20s%20s%20s\n",
						u.getUserID(), 
						u.getUsername(), 
						u.getFirstName(),
						u.getLastName(),
						u.getUserType());
				//System.out.println(u.getUserID()+ "\t\t" + u.getUsername() + "\t\t" + u.getFirstName() + 
				//		"\t\t" + u.getLastName() + "\t\t" + u.getUserType());
			}
			System.out.println("--------------------------------------------"
					+ "-----------------------------------------------------\n");
		
			System.out.println("Type 'back' to go back: ");
			while (true) {
				String input = scanner.nextLine();
				if (input.equals("back")) {
					break;
				}
				else {
					System.out.print("Invalid command.  Try again: ");
				}
			}
			adminMenu();
			
		}
		else if (option == 5) {
			System.out.println("\nLogged out.");
			currentUser = null;
			mainMenu();
		}
	}
	
	private static void accountCreationProcess() {
		System.out.println("What type of account would you like to make?\n"
				+ "1. Regular User\n"
				+ "2. Admin User");
		System.out.print("Enter option: ");
		int option = verifyOption(1, 2);
		System.out.print("Please enter your desired username: ");
		String username = scanner.nextLine();
		System.out.print("Please enter your desired password: ");
		String password = scanner.nextLine();
		System.out.print("Please enter your desired 4-digit PIN number: ");
		int pinNumber = verifyOption(0,9999);
		System.out.print("Please enter your first name: ");
		String firstName = scanner.nextLine();
		System.out.print("Please enter your last name: ");
		String lastName = scanner.nextLine();
		User temp = new User();
		temp.setUsername(username);
		temp.setPassword(password);
		temp.setPinNumber(pinNumber);
		temp.setFirstName(firstName);
		temp.setLastName(lastName);
		if (option == 2) {
			while (true) {
				System.out.print("Enter the Admin Code: ");
				if (scanner.nextLine().equals("power overwhelming")) {
					if (UserService.getInstance().registerAdminSecure(temp)) {
						System.out.println("Account has been successfully created.");
						System.out.println("Admin rights granted.  Account has automatically been approved.");
						System.out.println("Returning to main menu...");
						log.info("An admin account '" + username + "' was created");
					}
					break;
				}
				else {
					System.out.println("Incorrect code. Try again.");
				}
			}
		}
		else if (option == 1) {
			if (UserService.getInstance().registerUserSecure(temp)) {
				System.out.println("Account has been successfully created.  Please wait for admin approval.");
				System.out.println("Returning to main menu...");
				log.info("A new account '" + username + "' was created");
			}
		}
		else {
			System.out.println("Error has occured.  System Exiting.");
			System.exit(0);
		}
		mainMenu();
	}
	
	private static void bankAccountCreation() {
		System.out.println("Are you sure you wish to create a new account?  Y/N");
		String input = scanner.nextLine();
		try {
			if (currentUser == null) {
				throw new Exception("The user is currently null.  You are not logged in.");
			}
			if (input.equals("y") || input.equals("Y")) {	
				if (UserService.getInstance().createNewAccount(currentUser)) {
					System.out.println("Account has successfully been created.");
					log.info("A bank account was created for '" + currentUser.getUsername() + "'");
					viewBankAccounts();
				}
			}
			else if (input.equals("n") || input.equals("N")) {
				System.out.println("Account not created.\n");
				if (currentUser.getUserType().equals("admin")) {
					adminMenu();
				}
				else {
					userMenu();
				}
			}
		} catch (Exception e) {
			log.error("Error occured in bankAccountCreation: " + e.getMessage());
			System.out.println("Critical Error occured.  Exiting.");
			System.exit(0);
		}
	}
	
	private static void viewBankAccounts() {
		space();
		ArrayList<BankAccount> accounts = 
				(ArrayList<BankAccount>) UserService.getInstance().getBankAccountList(currentUser);
		if (accounts.size() < 1) {
			System.out.println("You currently do not have any accounts.");
			if (currentUser.getUserType().equals("admin")) {
				adminMenu();
			}
			else {
				userMenu();
			}
		}
		System.out.println("Your accounts: ");
		System.out.println("-------------------------------------------------------");
		System.out.format("%12s%25s\n", "Account ID", "Balance");
		System.out.println("-------------------------------------------------------");
		for (BankAccount ba : accounts) {
			String bal = "" + NumberFormat.getCurrencyInstance().format(ba.getBalance());
			System.out.format("%12d%25s\n",
					ba.getAccountID(), 
					bal);
			//System.out.println(u.getUserID()+ "\t\t" + u.getUsername() + "\t\t" + u.getFirstName() + 
			//		"\t\t" + u.getLastName() + "\t\t" + u.getUserType());
		}
		System.out.println("-------------------------------------------------------\n");
		System.out.println("What would you like to do?\n" 
				+ "1. Withdraw\n" 
				+ "2. Deposit\n"
				+ "3. Go Back");
		System.out.print("Enter option number: ");
		int option = verifyOption(1, 3);
		if (option == 1) {
			System.out.println("Which account would you like to withdraw from?");
			int id = verifyOption(1, 10000000);
			boolean isID = false;
			BankAccount ba = new BankAccount();
			for (BankAccount temp : accounts) {
				if (temp.getAccountID() == id) {
					isID = true;
					ba = temp;
					break;
				}
			}
			if (isID) {
				System.out.println("How much would you like to withdraw?");
				String str = scanner.nextLine();
				if (isMonetaryAmount(str)) {
					double amount = Double.parseDouble(str);
					if (amount > ba.getBalance()) {
						System.out.println("Attempting to withdraw more money than the account holds.\n\n"
								+ "Unable to complete transaction.");
					}
					else {
						if (UserService.getInstance().withdraw(ba, amount)) {
							System.out.println("Transaction successful.\nWithdrew " 
									+ NumberFormat.getCurrencyInstance().format(amount) + " from account " + ba.getAccountID());
							log.info("A withdraw transaction occured for account '" + ba.getAccountID());
						}
					}
				}
				else {
					System.out.println("Invalid amount entered.");
				}
			}
			else {
				System.out.println("Account does not exist.");;
			}
			viewBankAccounts();
		}
		else if (option == 2) {
			System.out.println("Which account would you like to deposit into?");
			int id = verifyOption(1, 10000000);
			boolean isID = false;
			BankAccount ba = new BankAccount();
			for (BankAccount temp : accounts) {
				if (temp.getAccountID() == id) {
					isID = true;
					ba = temp;
					break;
				}
			}
			if (isID) {
				System.out.println("How much would you like to deposit?");
				String str = scanner.nextLine();
				if (isMonetaryAmount(str)) {
					double amount = Double.parseDouble(str);
					if (UserService.getInstance().deposit(ba, amount)) {
						System.out.println("Transaction successful.\nDeposited " 
								+ NumberFormat.getCurrencyInstance().format(amount) + " into account " + ba.getAccountID());
						log.info("A deposit transaction occured for account '" + ba.getAccountID());
					}
				}
				else {
					System.out.println("Invalid amount entered.");
				}
			}
			else {
				System.out.println("Account does not exist.");;
			}
			viewBankAccounts();
		}
		else if (option == 3) {
			if (currentUser.getUserType().equals("admin")) {
				adminMenu();
			}
			else {
				userMenu();
			}
		}
	}
	
	private static void space() {
		System.out.println("\n\n------------------------------------------------------------------\n\n");
	}
	
	private static boolean isMonetaryAmount(String amount) {
		try {
			if (Double.parseDouble(amount) < 0) {
				throw new IllegalArgumentException();
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}

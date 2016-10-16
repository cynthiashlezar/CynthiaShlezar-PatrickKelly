/*
 * Author: Cynthia Shlezar
 * 
 * Class: AccountCollection
 * 
 * Purpose: The purpose of the AccountCollection class is to keep track
 * of all valid users that can access the Jukebox. The accounts are kept
 * track by an account with a username and password, and collected in a
 * HashMap, so that duplicates are not allowed, and accessing an account 
 * is efficient. When initialized, the AccountCollection class reads a file
 * of valid accounts, listed username password, per line, and then creates
 * a new accuont based on the username and password, and then it adds that
 * new account to the accounts hashmap.
 * 
 * Variables: HashMap<String, Account> accounts
 * 				--> the collection of account objects
 * 
 * Methods: public void createAllAccounts();
 * 			public void createNewAccount(String, String);
 * 			public boolean userExists();
 * 			public Account getAccount(String);
 */
package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/*
 * Class AccountCollection
 * 
 * Responsibility: Maintains a collection of accounts for use in Jukebox authentication
 * and accounting.
 * 
 * Reads all account info from extra_files/accounts.txt, creates account objects, and stores them
 * in a HashMap.  (The key to the HashMap being username.)  Contains methods to create accounts, see
 * whether an account exists, and return account objects.
 */
public class AccountCollection {

	HashMap<String, Account> accounts;
	
	public AccountCollection() {
		
		//initializes accounts, and adds things to it from a file
		createAllAccounts(); 
	}
	
	/*
	 * Method: createAllAccounts
	 * 
	 * Purpose: initializes accounts HashMap, reads a username and password
	 *          line by line from a file located in extrafiles/accounts.txt,
	 *          creates a new account object based on the username and password,
	 *          and then adds that account to the HashMap. Exceptions are thrown
	 *          in case the file cannot be found or if there was something wrong
	 *          encountered while reading the file.
	 *          
	 * Arguments: none
	 * 
	 * Return: void
	 */
	public void createAllAccounts() {
		
		accounts = new HashMap<>();
		
		try {
			
			File fileName = new File("extra_files/accounts.txt");
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line = "";
			StringTokenizer st;
			boolean entered;
			
			//reads a line in the file, until there are no more lines
			while((line = bufferedReader.readLine()) != null) {
				
				//separate the username and the password by the space
				//using a StringTokenizer
				st = new StringTokenizer(line, " ");
				
				//adds the tokens found as the username and password
				//for a new account
				entered = createNewAccount(st.nextToken(), st.nextToken());
				if(!entered) {
					System.out.println("Account couldn't be created");
				}
			}
		} 
		catch (FileNotFoundException e) {
			System.out.println("The accounts file was not found!");
			e.printStackTrace();
		}
		catch(IOException e) {
			System.out.println("An error was encountered reading the accounts file!");
			e.printStackTrace();
		}
	
	}
	
	/*
	 * Method: createNewAccount
	 * 
	 * Purpose: creates a new account object based on the arguments
	 *          of the method (the username and password) and puts that
	 *          account as well as a string (the username) as the key into
	 *          the HashMap. If there already exists an account of the
	 *          username entered, we need to know, so returns a boolean
	 *          value based on how things go
	 *          
	 * Arguments: String username - the username of the new account
	 *            String password - the password of the new account
	 *  
	 * Return: boolean - true if an account was created, false if
	 *                   an account already existed
	 *                   
	 * Programmer: Cynthia Shlezar
	 */
	public boolean createNewAccount(String username, String password) {
		if(userExists(username)) return false;
		accounts.put(username, new Account(username, password));
		return true;
	}
	
	/*
	 * Method: userExists
	 * 
	 * Purpose: checks the HashMap to see if the username denoted as a string
	 *          as the argument exists, and if it does, then return true, else
	 *          return false
	 *          
	 * Arguments: String user - the username
	 * 
	 * Return: boolean (see purpose)
	 */
	
	public boolean userExists(String user) {
		return accounts.containsKey(user);
	}
	
	/*
	 * Method: getAccount
	 * 
	 * Purpose: gets an Account (value) based on a key. The key is the
	 *          username submitted 
	 */
	
	public Account getAccount(String user) {
		if(!userExists(user)) return null;
		return accounts.get(user);
	}
	
}

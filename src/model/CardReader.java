package model;

/*
 * class CardReader.  
 * 
 * Responsibility: This class handles all account authentication for the jukebox.
 */

/*
 * Programmer: Patrick Kelly
 * Course: CSC 335 (Fall '16)
 * Section:  Miranda's
 */

public class CardReader {
	
	private AccountCollection accounts; // creates and holds the account collection
	
	public CardReader() {
		accounts = new AccountCollection();
	}
	
	
	/*
	 * Attempts to authenticate a given username and password.  If authenticated, this method
	 * returns the associated account object.  (If not, returns null.)
	 */
	public Account authenticateUser(String userName, String password) {
		
		Account authenticateMe;
		
		// check to see if the account exists, and if so, grab it for examination.
		if (accounts.userExists(userName)) {
			authenticateMe = accounts.getAccount(userName);
		} else {
			// account does not exist - return null
			return null;
		}
		
		// attempt to authenticate the user with the supplied password
		if (authenticateMe.getPassword().equals(password)) {
			// return the account object if authenticated
			return authenticateMe;
		}
		// wrong password - return null.
		return null;
		
	}
	

}
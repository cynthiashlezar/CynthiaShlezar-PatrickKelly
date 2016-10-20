/*
 * Names: Cynthia Shlezar & Patrick Kelly
 * 
 * File: Account.java
 * 
 * Class: Account
 * 
 * Purpose: Account basically functions as a node of data required for an account
 *          in an AccountCollection. Important information that an Account must 
 *          hold includes the name and password for authentication, amount of
 *          credit that is available for the user to use to play songs, a counting
 *          variable that keeps track of how many songs are played in a single day,
 *          since the maximum is 3, and a LocalDate variable, so that the account's
 *          tracking variable for amount of songs played in one day can be reset at
 *          the start of each day. The methods in this class involve retrieving all 
 *          of the instance variables' data, but only involve changing creditAvailable,
 *          numSongsPlayedToday, and dateLastPlayed
 *          
 * Instance Variables: String name - the username
 *                     String password - the password
 *                     int numSongsPlayedToday - the amount of times the user has selected
 *                                               a song, in a day
 *                     int creditAvailable - each user can play a maximum of 1500 minutes of
 *                                           content
 *                     LocalDate dateLastPlayed - keeps track of the last date/time that the
 *                                                user selected a song to play
 *                                                
 *  Methods:
 *  		  public String getName();
 *            public String getPassword();
 *            public int getNumSongsPlayedToday();
 *            public int getCreditAvailable();
 *            public void incrementNumSongsPlayedToday();
 *            public void subtractCredit(int subtract);
 */
package model;

import java.io.Serializable;
import java.time.LocalDate;

@SuppressWarnings("serial")
public class Account implements Serializable {
	
	String name;					//username login
	String password;				//password for login
	int numSongsPlayedToday;		//number of songs a user has played in one day
	int creditAvailable;			//credit available to the user
	LocalDate dateLastPlayed;
	
	/*
	 * Constructor.  Provides a new account with 1500 minutes of credit.
	 */
	public Account(String name, String password) {
		this.name = name;
		this.password = password;
		numSongsPlayedToday = 0;
		creditAvailable = (1500 * 60); // changed to 1500 minutes times 60 seconds
		dateLastPlayed = LocalDate.now().minusDays(1);  // changed to allow testing; effective functionality the same
	}
	/*
	 * Method: getName
	 * 
	 * Purpose: gets the name for the acount as a String
	 */
	public String getName() {
		return name;
	}
	
	/*

	 * Method: getPassword
	 * 
	 * Purpose: gets the String password for the account
	 */
	
	public String getPassword() {
		return password;
	}
	/*
	 * Method: getNumSongsPlayedToday
	 * 
	 * Purpose: getter for getNumSongsPlayedToday, unless
	 * it's a new day, in which it resets to 0
	 */
	public int getNumSongsPlayedToday() {
		if (! dateLastPlayed.equals(LocalDate.now())) {
			numSongsPlayedToday = 0;
		} 
		return numSongsPlayedToday;
	}

	/*
	 * Method: getCreditAvailable
	 * 
	 * Purpose: getter for creditAvailable (seconds)
	 */
	public int getCreditAvailable() {
		return creditAvailable;
	}

	/*
	 * Method: incrementNumSongsPlayedToday
	 * 
	 * Purpose: increases the number of songs that was
	 * played today for a maximum of 3 songs.

	 */
	public void incrementNumSongsPlayedToday() {
		if (dateLastPlayed.equals(LocalDate.now())) {
			// previously played today
			numSongsPlayedToday++;
		} else {
			// not yet played today: set the lastPlayedDate to today
			// and set the number of times played today to 1
			// (incrementing from 0 to 1 for today)
			dateLastPlayed = LocalDate.now();
			numSongsPlayedToday = 1;
		}
	}

	/*
	 * Method: withdrawCredit
	 * 
	 * Purpose: anytime a song is played by the user, we need to withdraw
	 *          credit (the amount of minutes allowed for playing)

	 */
	public void withdrawCredit(int subtract) {
		creditAvailable -= subtract;
	}

}

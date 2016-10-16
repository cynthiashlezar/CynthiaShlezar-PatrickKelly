package model;

/*
 * Class: Decider
 * 
 * Responsibility: This class decides whether a given song may be played by a given user today.  
 * 
 * Uses rules set
 * up by the Supreme Jukebox Commission that a song may be played 3 times per day, and a given
 * user account may play 3 songs per day.
 */

/*
 * Programmer: Patrick Kelly
 * Course: CSC 335 (Fall '16)
 * Section:  Miranda's
 */

public class Decider {
	
	private int songsPerDayPerPerson;      // the number of songs a person can play per day
	private int numTimesPlayedPermissible; // the number of times per day a song may be played
	
	public Decider() {
		
		// if we decide to implement a strategy pattern, we can add arguments to the
		// constructor to set these variables at Decider creation time.
		this.songsPerDayPerPerson = 3;
		this.numTimesPlayedPermissible = 3;
	}
	
	/*
	 * Determines whether the selected song may be played by the user today.  To do this,
	 * the decider examines:
	 * 			1) the user's credit in seconds remaining vs. the length of the song
	 * 			2) whether the user has played her max number of songs for the day
	 * 			3) whether the song has been played the max number of times for the day.
	 * If all these items check out, the decider does the following:
	 * 			1) Removes the appropriate amount of credit from the user's account
	 * 			2) Increments the number of songs the user has played today
	 * 			3) Increments the number of times the song has been played today
	 * 			4) Returns true
	 * Otherwise, the Decider returns false.
	 */
	public boolean canPlaySong(Account user, Song song) {
		// Check to see whether this user may play this song today.
		if (user.getCreditAvailable() >= song.getLength() && user.getNumSongsPlayedToday() < songsPerDayPerPerson
				&& song.getTimesPlayedToday() < numTimesPlayedPermissible) {
			// increment user songs
			user.incrementNumSongsPlayedToday();
			// increment song's num times played
			song.setTimesPlayedToday();
			// remove credit from user's account
			user.withdrawCredit(song.getLength());
			// return true
			return true;
		}
		return false;
	}
}

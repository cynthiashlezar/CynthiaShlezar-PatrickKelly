package model;

import java.io.Serializable;

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

@SuppressWarnings("serial")
public class Decider implements Serializable {
	
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
	 * Otherwise, the Decider a failure Enum (see SongSelection Enum for examples.)
	 * 
	 * Note: This method has a bias toward reporting that a user has played too many songs
	 * rather than that the song has already been played too many times.
	 */
	public SongSelection canPlaySong(Account user, Song song) {
		// Check to see whether the user has enough credit
		if (user.getCreditAvailable() < song.getLength()) {
			return SongSelection.NOT_ENOUGH_CREDIT;
		}
		// see whether the user has played max # songs today
		if (user.getNumSongsPlayedToday() >= songsPerDayPerPerson) {
			return SongSelection.NO_PLAYS_REMAINING_USER;
		}
		// see whether the song has been played max # times today
		if (song.getTimesPlayedToday() >= numTimesPlayedPermissible) {
			return SongSelection.NO_PLAYS_REMAINING_SONG;
		}
		// and if all that is okay, we can proceed...
		
		// increment user # songs played today
		user.incrementNumSongsPlayedToday();
		// increment song's # times played today
		song.setTimesPlayedToday();
		// remove credit from user's account
		user.withdrawCredit(song.getLength());
		// return SUCCESS
		return SongSelection.SUCCESS;
	}
}

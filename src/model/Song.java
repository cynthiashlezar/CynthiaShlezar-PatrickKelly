package model;

import java.io.File;
import java.time.LocalDate;

/*
 * Song class.  Responsibility: Contains all information about a song.
 * 
 * This includes metadata needed by the Jukebox when playing a song,
 * and also the last date played and number of times played that day.
 */

/*
 * Programmer: Patrick Kelly
 * Course: CSC 335 (Fall '16)
 * Section:  Miranda's
 */

public class Song {

	private File file;
	private String artist, title;
	private int length; //seconds
	private int timesPlayedToday;
	private LocalDate lastPlayedDate;
	
	/*
	 * Constructor.
	 * 
	 * @params: File file (the filename of the song.)
	 *          String artist
	 *          String title
	 *          int length
	 *          LocalDate lastPlayedDate
	 *          
	 */
	public Song(File file, String artist, String title, int length, LocalDate lastPlayedDate) {
		
		this.file = file;
		this.artist = artist;
		this.title = title;
		this.length = length;
		this.timesPlayedToday = 0; // assumes song objects are created once at the beginning of the day
		this.lastPlayedDate = lastPlayedDate; 
		
	}
	
	/*
	 * Returns the file object (filename) associated with the song.
	 */
	public File getFile() {
		return file;
	}
	
	/*
	 * Returns the artist's name.
	 */
	public String getArtist() {
		return artist;
	}
	
	/*
	 * returns the title of the song.
	 */
	public String getTitle() {
		return title;
	}
	
	/*
	 * Returns the length of the song in seconds.
	 */
	public int getLength() {
		return length;
	}
	
	
	/*
	 * getTimesPlayedToday()
	 * 
	 * Returns the number of times the song has been played today.  If today is
	 * not the date the song was played last, the song has not been played today,
	 * and that answer is zero.  (Sets the value of the timesPlayedToday variable
	 * to 0 in that case.)
	 * 
	 * @params:  nada
	 * @returns: int  (the number of times played today.)
	 */
	public int getTimesPlayedToday() {
		if (! lastPlayedDate.equals(LocalDate.now())) {
			timesPlayedToday = 0;
		} 
		return timesPlayedToday;
	}
	
	/*
	 * setTimesPlayedToday()
	 * 
	 * Increments the number of times the song has been played today.  Does no checking
	 * to make sure the number of times played < 3; that functionality goes into the decider.
	 * 
	 * If the lastPlayedDate is not today, sets it to today.
	 * 
	 * This method should only be called by the decider.
	 * 
	 * @params: void
	 * @returns: null (since we do no checking here.)
	 */
	public void setTimesPlayedToday() {
		if (lastPlayedDate.equals(LocalDate.now())) {
			// previously played today
			timesPlayedToday++;
		} else {
			// not yet played today: set the lastPlayedDate to today
			// and set the number of times played today to 1
			// (incrementing from 0 to 1 for today)
			lastPlayedDate = LocalDate.now();
			timesPlayedToday = 1;
		}
	}

	public String getFileName() {
		return file.getName();
	}

}

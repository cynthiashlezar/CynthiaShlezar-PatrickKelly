package model;

import java.io.Serializable;

import songplayer.EndOfSongListener;
import songplayer.SongPlayer;

/*
 * Programmer: Patrick Kelly
 * Course: CSC 335 (Fall '16)
 * Section:  Miranda's
 */

/*
 * Class: SongRequest
 * 
 * Responsibility: This class creates song play requests which can be stored and executed
 * at a later time.  
 * 
 * These requests are held by the SongQueue until their turn for execution.  When executed,
 * a request sets the Jukebox's currentSong variable (so that the view may update itself)
 * and uses the SongPlayer class to play the song.
 */

@SuppressWarnings("serial")
public class SongRequest implements Serializable {
	
	EndOfSongListener listener;
	String fileName;  // the file name of the song; this is needed by SongPlayer.
	Song song;  // contains the song info that will be needed when executing the request.
	Jukebox jukebox;  // so that jukebox.currentSong may be set, and also so
	                           // that we can grab the EndOfSongListener from the jukebox
	                           // (also needed by SongPlayer).

	/*
	 * Construct a SongRequest.  
	 */
	public SongRequest(Song song, EndOfSongListener listener) {
		this.listener = listener;
		fileName = "songfiles/" + song.getFileName();
		this.song = song;
	}
	
	/*
	 * Execute the request.  Uses the SongPlayer.playFile() method.
	 */
	public void execute() {
		SongPlayer.playFile(listener, fileName);
		System.out.println(song.getTitle());
	}
	
	/*
	 * This toString() method provides a String representation of the SongRequest object.
	 * Used to display info about the queue of songs in the GUI's play list.
	 * 
	 * Returns a String.
	 * 
	 * 
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		int length = song.getLength();
		int minutes = (length/60)%60;
		int seconds = length%60;
		return "" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds) + " " + song.getTitle() + " by " + song.getArtist();
	}
	
	/*
	 * Returns the Song object associated with the SongRequest objecct.
	 */
	public Song getSong() {
		return song;
	}

}

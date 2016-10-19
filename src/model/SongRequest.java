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
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			System.out.println("Sleep interrupted by JUnit");
//		}
		System.out.println(song.getTitle());
	}
	
	/*
	 * TODO: remember to think about formatting seconds into MM:SS
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "" + song.getLength() + " " + song.getTitle() + " " + song.getArtist();
	}
	
	public Song getSong() {
		return song;
	}

}

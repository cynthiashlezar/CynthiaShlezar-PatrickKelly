package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Map;
import java.util.Observable;
import java.util.TreeMap;

import songplayer.EndOfSongEvent;
import songplayer.EndOfSongListener;
import songplayer.SongPlayer;

/*
 * Class: Jukebox  
 * 
 * Responsibility: Coordinates the activities of the objects in the Jukebox system.  
 * 
 * (You know.  Plays songs.)
 */

/*
 * Programmer: Cynthia Shlezar and Patrick Kelly
 * Course: CSC 335 (Fall '16)
 */

public class Jukebox extends Observable {
	
	private CardReader authenticator;
	private Decider decider;
	private SongLibrary library;
	private SongPlayer player;
	private Account currentAccount;
	private Song currentSong;
	private SongQueue songQueue;
	
	public Jukebox() {
		authenticator = new CardReader();
		decider = new Decider();
		library = new SongLibrary("songs.txt");
		songQueue = new SongQueue();
		currentAccount = null;
		currentSong = null;
	}
	
	/*
	 * Handles incoming song requests from a view.
	 * 
	 * @params: String title: the title of the song
	 * 			Account account: the account of the logged in user.
	 * 
	 * @returns: boolean.  true if the song can be played and is added to the queue
	 *                     false if the title doesn't belong to a song or if the decider
	 *                            determines that the song may not be played.
	 */
	public boolean requestSongFromMenu(String title) {
		Song requested = null;
		// check to see if the song exists and get the song object
		if (library.songExists(title)) {
			requested = library.getSong(title);
			// see if the user can play the song today
			if (decider.canPlaySong(currentAccount, requested)) {
				// yay!  add the song to the queue and return true.
				addSongToQueue(requested);
				return true;
			}
		}
		// cannot play song - either it doesn't exist or the user can't play it today.
		// return false.
		return false;
		
	}
	
	/* 
	 * Uses the CardReader to authenticate a user signing in.  Returns an Account object if
	 * the user is authenticated (null if not).  This Account object may then be used as
	 * an argument when requesting songs.
	 */
	public boolean useCardReader(String username, String password) {
		currentAccount = authenticator.authenticateUser(username, password);
		setChanged();
		notifyObservers();
		return (currentAccount != null);
	}
	
	
	/*
	 * addSongToQueue()
	 * 
	 * Given a Song object, this method creates a SongRequest and adds it to the SongQueue.
	 * 
	 * @params: a Song
	 * @returns: void
	 */
	public void addSongToQueue(Song song) {
		SongRequest songRequest = new SongRequest(song, this);
		songQueue.addSong(songRequest);
		setChanged();
		notifyObservers();
	}
	
	/*
	 * Returns the title of the song currently being played by the Jukebox so
	 * that the view may display the title when it updates itself.
	 * 
	 * Assumes that a SongRequest has been executed; the SongRequest sets
	 * this variable.  

	 */
	public String getCurrentSongTitle() {
		if (currentSong == null) {
			return "";
		}
		return currentSong.getTitle();
	}
	
	/*
	 * This method sets the currentSong variable to reflect the song currently
	 * being played by the jukebox.  Notifies observers so that they can
	 * update.
	 * 
	 * Used by a SongRequest object when it executes.
	 */
	public void setCurrentSong(Song song) {
		currentSong = song;
		setChanged();
		notifyObservers();
	}
	
	/*
	 * Returns the SongQueue object, which listens for the end of a song before
	 * playing another.  This is used in the creation of SongRequest objects (since
	 * an EndOfSongListener object is used by the SongPlayer when reporting that
	 * a song is done.
	 */
	public EndOfSongListener getSongQueue() {
		return songQueue;
	}
	
	
	/*
	 * Private inner class SongQueue.  This class handles the queue of song requests, listening
	 * for the end of a song and then executing the next request.  (Or immediately executes
	 * a request if the queue is empty.
	 */
	private class SongQueue implements EndOfSongListener {
		
		ArrayList<SongRequest> requests;  // hold onto those SongRequests!
		
		// constructor.
		public SongQueue() {
			requests = new ArrayList<SongRequest>();
		}
		
		/*
		 * Adds a song request to the queue.  If the queue was empty, this method
		 * then immediately executes the request.
		 * 
		 * @param: SongRequest.  The SongRequest being added to the queue.
		 * 
		 * Note: the song currently being played stays in the 0th index of the ArrayList
		 * until the song is over.  Then it's removed.
		 */
		public void addSong(SongRequest request) {
			if (requests.isEmpty()) {
				requests.add(request);
				requests.get(0).execute();
				// for testing
				// System.out.println("Executing song request for " + request.fileName);
			} else {
				requests.add(request);
			}
		}

		/*
		 * This method implements the EndOfSongListener interface.  When the SongPlayer is done playing
		 * a song, it calls the songFinishedPlaying() method on the listener object which was provided to
		 * SongPlayer's playFile() method.
		 * 
		 * Since our Jukebox views and rules don't really care about the song that just finished, the
		 * contents of the event aren't really important.  What's important is that the event gets sent.
		 */
		@Override
		public void songFinishedPlaying(EndOfSongEvent eventWithFileNameAndDateFinished) {
			// System.out.println("got EndOfSongEvent message...");
			requests.remove(0);
			setCurrentSong(null);
			if (! requests.isEmpty()) {
				requests.get(0).execute();
			}
		}
		
		 
	}
	
	/*
	 * Returns an ArrayList containing the titles of all the songs in the SongLibrary.  Used
	 * by the view to be able to list the songs.
	 */
	public ArrayList<String> fetchLibrary() {
		
		ArrayList<String> titles = new ArrayList<>();
		
		for(Map.Entry<String, Song> entry : this.library.retrieveTitles().entrySet()) {
			String song = entry.getKey();
			titles.add(song);
		}
		
		
		return titles;
		
		
	}

	/*
	 * Used by the view to display information regarding the current user.
	 * Returns an Account object.
	 */
	public Account getCurrentAccount() {
		return currentAccount;
	}
	
	/*
	 * Logs out the current user.  (Used by the view).
	 */
	public void removeCurrentAccount() {
		currentAccount = null;
	}
	
}

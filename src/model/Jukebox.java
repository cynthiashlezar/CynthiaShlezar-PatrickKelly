package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.Observable;
import java.util.TreeMap;

import javax.swing.ListModel;
import javax.swing.event.ListDataListener;
import javax.swing.table.TableModel;

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
	// private Song currentSong;  <------- not really used; commenting out for now
	private SongQueue songQueue;
	
	public Jukebox() {
		authenticator = new CardReader();
		decider = new Decider();
		library = SongLibrary.getInstance("songs.txt");
		songQueue = new SongQueue();
		currentAccount = null;
		// currentSong = null;    <------ commenting out.  We don't really need it anymore.
	}
	
	
	/*
	 * Handles incoming song requests from a view.
	 * 
	 * @params: String title: the title of the song
	 * 			Account account: the account of the logged in user.
	 * 
	 * @returns: SongSelection ENUM.  Possibilities:
	 *                   NOT_LOGGED_IN : no user is logged in
	 *                   SONG_NOT_EXIST : no song by that title in the collection
	 *                   NOT_ENOUGH_CREDIT : the user doesn't have enough seconds remaining
	 *                   NO_PLAYS_REMAINING_USER: the user already played her/his max songs for the day
	 *                   NO_PLAYS_REMAINING_SONG: the song has already been played its max # of times for the day.
	 */
	public SongSelection requestSongFromMenu(String title) {
		Song requested = null;
		SongSelection result = null;
		// first make sure a user is logged in.
		if (currentAccount == null) {
			return SongSelection.NOT_LOGGED_IN;
		}
		// check to see if the song exists and get the song object
		if (! library.songExists(title)) {
			System.out.println("Song doesn't exist!!!!");
			return SongSelection.SONG_NOT_EXIST;
		} else {
			requested = library.getSong(title);
			result = decider.canPlaySong(currentAccount, requested);
		}	
		if (result == SongSelection.SUCCESS) {
			addSongToQueue(requested);
		}
		return result;
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
		SongRequest songRequest = new SongRequest(song, songQueue);
		System.out.println(songRequest);
		System.out.println("" + songQueue.getSize());
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
	 * 
	 * Just used for testing at this point.  Perhaps testing can be re-done such that
	 * we can get rid of this method.

	 */
	public String getCurrentSongTitle() {
		if (songQueue.getSize() == 0) {
			return "";
		}
		String currentSongTitle = songQueue.getElementAt(0).toString();
		currentSongTitle = currentSongTitle.substring(currentSongTitle.indexOf(' ') + 1);
		currentSongTitle = currentSongTitle.substring(0, currentSongTitle.indexOf(' '));
		return currentSongTitle;
	}
	
	/*
	 * This method sets the currentSong variable to reflect the song currently
	 * being played by the jukebox.  Notifies observers so that they can
	 * update.
	 * 
	 * Used by a SongRequest object when it executes.
	 * 
	 * Not really needed, since we're not displaying anything based on the currentSong
	 * variable.  Commenting out.
	 */
//	public void setCurrentSong(Song song) {
//		currentSong = song;
//		setChanged();
//		notifyObservers();
//	}
	
	/*
	 * Returns the SongQueue object, which listens for the end of a song before
	 * playing another.  This is used in the creation of SongRequest objects (since
	 * an EndOfSongListener object is used by the SongPlayer when reporting that
	 * a song is done.
	 * 
	 * Used for testing only, really.  But if I re-do some tests differently, I can perhaps
	 * get rid of this method.
	 */
	public EndOfSongListener getSongQueueListener() {
		return songQueue;
	}
	
//	/*
//	 * Returns an ArrayList containing all the songs in the SongLibrary.  Used
//	 * by the view to be able to list the songs.
//	 */
//	public ArrayList<Song> fetchLibrary() {
//		
//		ArrayList<Song> songs = new ArrayList<>();
//		
//		for(Map.Entry<String, Song> entry : this.library.retrieveTitles().entrySet()) {
//			Song song = entry.getValue();
//			songs.add(song);
//		}
//		return songs;	
//	}

	/*
	 * Used by the view to display information regarding the current user.
	 * Returns an Account object.
	 * 
	 * Commenting this method out for now to see if we can get by without returning the
	 * whole account object to the view.
	 */
	public Account getCurrentAccount() {
		return currentAccount;
	}
	
	/*
	 * Logs out the current user.  (Used by the view).
	 */
	public void removeCurrentAccount() {
		currentAccount = null;
		setChanged();
		notifyObservers();
	}
	
	/*
	 * Returns the username of the currently logged in user.  Used by the view.
	 */
	public String printUsername() {
		if (currentAccount == null) {
			return "";
		}
		return currentAccount.getName();
	}
	
	/*
	 * Returns an integer value representing the number of seconds of credit left
	 * in the user's account.  Used by the view.
	 */
	public int getUserCredit() {
		return currentAccount.getCreditAvailable();
	}
	
	/*
	 * Returns the SongQueue in ListModel form for use by the view.
	 */
	public ListModel getPlaylist() {
		return songQueue;
		
	}
	
	public TableModel getLibraryTable() {
		return library;
	}
	
	public String getUserSongsPlayed() {
		// TODO Auto-generated method stub
		return "" + currentAccount.getNumSongsPlayedToday();
	}
	


//                            public outer Jukebox class above

// ============================================================================================ //
// ============================================================================================ //

//                           private inner SongQueue Class Below

	
	/*
	 * Private inner class SongQueue.  
	 * 
	 * Responsibility: manages the Jukebox's queue of songs.
	 * 
	 * This class handles the queue of song requests, listening for the end of a song and then executing 
	 * the next request.  (Or immediately executes a request if the queue is empty.
	 * 
	 * Added ListModel implementation so that the SongQueue object can be provided as a ListModel to
	 * the view.
	 */
	public class SongQueue implements EndOfSongListener, ListModel<String> {
		
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
			setChanged();
			notifyObservers();
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
			// setCurrentSong(null);    currentSong isn't used any more, but just commenting out for now.
			if (! requests.isEmpty()) {
				requests.get(0).execute();
			}
			setChanged();
			notifyObservers();
		}

		@Override
		public int getSize() {
			return requests.size();
		}

		@Override
		public String getElementAt(int index) {
			if (requests.size() == 0) {
				return "";
			}
			return requests.get(index).toString();
		}

		@Override
		public void addListDataListener(ListDataListener l) {
			//nothing
		}

		@Override
		public void removeListDataListener(ListDataListener l) {
			//nothing	
		} 
		

		public ArrayList<Song> getSongQueue() {
			
			ArrayList<Song> songs = new ArrayList<>();
			for(int i = 0; i < requests.size(); i++) {
				songs.add(requests.get(i).getSong());
			}
			return songs;
		}
		
		 
	

	} // End private inner SongQueue class





	
}	// end Jukebox class


	
	
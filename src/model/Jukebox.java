package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;
import javax.swing.table.TableModel;

import songplayer.EndOfSongEvent;
import songplayer.EndOfSongListener;

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

@SuppressWarnings("serial")
public class Jukebox extends Observable implements Serializable {
	
	private CardReader authenticator;
	private Decider decider;
	private SongLibrary library;
	private Account currentAccount;
	private SongQueue songQueue;
	
	public Jukebox() {
		authenticator = new CardReader();
		decider = new Decider();
		library = SongLibrary.getInstance("songs.txt");
		songQueue = new SongQueue();
		currentAccount = null;
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
		// check to see if the song exists
		if (! library.songExists(title)) {
			// if not, say so
			return SongSelection.SONG_NOT_EXIST;
		} else {
			// song exists - get it from the library and see if
			// it can be played by the current user today
			requested = library.getSong(title);
			result = decider.canPlaySong(currentAccount, requested);
		}	
		if (result == SongSelection.SUCCESS) {
			// if so, add the song to the queue
			addSongToQueue(requested);
		}
		// let the view know whether the request was successful (and if not, why)
		return result;
	}
	
	/* 
	 * Uses the CardReader to authenticate a user signing in.  Returns an Account object if
	 * the user is authenticated (null if not).  This Account object may then be used as
	 * an argument when requesting songs.
	 */
	public boolean useCardReader(String username, String password) {
		currentAccount = authenticator.authenticateUser(username, password);
		// update the views
		setChanged();
		notifyObservers();
		// let the calling method know whether a user is now logged in
		// NOTE: the case where someone was already logged in is handled by the view...
		// if someone is already logged in, the view spits out an error message
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
		// construct a song request object
		SongRequest songRequest = new SongRequest(song, songQueue);
		// add it to the queue
		songQueue.addSong(songRequest);
		// update view
		setChanged();
		notifyObservers();
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
	public ListModel<String> getPlaylist() {
		return songQueue;
		
	}
	
	/*
	 * Returns the song library as a TableModel so that the view can display its wares
	 */
	public TableModel getLibraryTable() {
		return library;
	}
	
	/*
	 * Returns (as a String, so as to be usable by the view) the number of songs
	 * the current user has played today.  Used by the view to display the user's status.
	 */
	public String getUserSongsPlayed() {
		return "" + currentAccount.getNumSongsPlayedToday();
	}
	
	/*
	 * This method is called by the GUI when the system begins.  It causes the
	 * jukebox to tell its Observers to update themselves.  Also, if there
	 * is a song in the queue, it executes the songRequest object, starting the cycle
	 * of playing the songs again.
	 * 
	 * NOTE:  This should only be called when starting the Jukebox.  It's used to get the views
	 * updated and the SongQueue executing requests when the Jukebox is loaded from a saved state.
	 */
	public void homeMadeNotify() {
		if (songQueue.getSize() != 0) {
			songQueue.executeZerothSong();
		}
		setChanged();
		notifyObservers();
		
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
	public class SongQueue implements EndOfSongListener, ListModel<String>, Serializable {
		
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
				// if the queue is empty, add the request to the queue and execute it.
				requests.add(request);
				requests.get(0).execute();
			} else {
				// otherwise, a song is playing... just add the request to the queue.
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
			// remove the request that just finished playing.
			requests.remove(0);
			if (! requests.isEmpty()) {
				// play the next song if there's another request
				requests.get(0).execute();
			}
			setChanged();
			notifyObservers();
		}

		/*
		 * The methods below are for implementing the ListModel interface.  getSize()
		 * and getElementAt() are the only methods required for our purposes.
		 * 
		 * getSize() returns the size on the queue.
		 * 
		 * (non-Javadoc)
		 * @see javax.swing.ListModel#getSize()
		 */
		@Override
		public int getSize() {
			return requests.size();
		}

		/*
		 * getElementAt() returns a string representation of the SongRequest
		 * object in the given index of the SongQueue.
		 * 
		 * @param:   integer (the index of the desired element)
		 * @returns: a String
		 * 
		 * (non-Javadoc)
		 * @see javax.swing.ListModel#getElementAt(int)
		 */
		@Override
		public String getElementAt(int index) {
			if (requests.size() == 0) {
				return "";
			}
			return requests.get(index).toString();
		}

		@Override
		public void addListDataListener(ListDataListener l) {
			//nothing.  Not used in our program.
		}

		@Override
		public void removeListDataListener(ListDataListener l) {
			//nothing	Not used in our program.
		} 
		
		/*
		 * This method is used when loading a saved jukebox state.  It executes the song
		 * request in index 0 of the queue so that the cycle of playing songs resumes
		 * without having to add an additional song to the queue.
		 */
		public void executeZerothSong() {
			if (requests.size() != 0) {
				requests.get(0).execute();
			}
		}
		

	} // End private inner SongQueue class


}	// end Jukebox class


	
	
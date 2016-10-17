package tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.junit.Test;

import model.*;
import songplayer.SongPlayer;

public class Tests2 {
	
	@Test
	public void testSongLibrary1() {
		SongLibrary library = new SongLibrary("songs.txt");
		File testSongFile = new File("songfiles/flute.aif");
		Song mySong = library.getSong("Flute");
		assertTrue(mySong.getLength() == 6);
		assertTrue(mySong.getFile().compareTo(testSongFile) == 0);
	}
	
	@Test
	public void testSongLibrary2() {
		SongLibrary library = new SongLibrary("songs.txt");
		File testSongFile = new File("songfiles/UntameableFire.mp3");
		System.out.println(testSongFile.getName() + " test");
		Song mySong = library.getSong("Untameable Fire");
		//System.out.println("Untameable fire filename: " + mySong.getFile().getPath());
		assertTrue(mySong.getLength() == 282);
		assertTrue(mySong.getFile().compareTo(testSongFile) == 0);
		assertTrue(mySong.getArtist().equals("Pierre Langer"));
		assertTrue(mySong.getTimesPlayedToday() == 0);
		mySong.setTimesPlayedToday();
		assertTrue(mySong.getTimesPlayedToday() == 1);
		assertTrue(mySong.getTitle().equals("Untameable Fire"));
	}
	
	@Test
	public void testSongLibrary3() {
		// FreePlay Music:Determined Tumbao:DeterminedTumbao.mp3:20
		SongLibrary library = new SongLibrary("songs.txt");
		File testSongFile = new File("songfiles/DeterminedTumbao.mp3");
		Song mySong = library.getSong("Determined Tumbao");
		assertTrue(mySong.getLength() == 20);
		assertTrue(mySong.getFile().compareTo(testSongFile) == 0);
		assertTrue(mySong.getArtist().equals("FreePlay Music"));
		assertTrue(mySong.getTimesPlayedToday() == 0);
		mySong.setTimesPlayedToday();
		assertTrue(mySong.getTimesPlayedToday() == 1);
		assertTrue(mySong.getTitle().equals("Determined Tumbao"));
		Song myOtherSong = library.getSong("Hit Me Baby One More Time");
		assertTrue(myOtherSong == null);
	}
	
	@Test
	public void testSongLibrary4() {
		// test exception handling
		SongLibrary library = new SongLibrary("drunkSongs.txt");
		SongLibrary library2 = new SongLibrary("drunkSongs2.txt");
		SongLibrary library3 = new SongLibrary("drunkSongs3.txt");
		SongLibrary library4 = new SongLibrary("drunkSongs34.txt");
	}
	
	
	@Test
	public void testCardReader1() {
		CardReader myVeryFirstCardReader = new CardReader();
		Account cynthia = myVeryFirstCardReader.authenticateUser("cynthia", "shlezar");
		assertTrue(cynthia != null);
		assertTrue(cynthia.getName().equals("cynthia"));
		assertTrue(cynthia.getNumSongsPlayedToday() == 0);
		assertTrue(cynthia.getCreditAvailable() == 1500 * 60);
		assertTrue(cynthia.getPassword().equals("shlezar"));
		cynthia.incrementNumSongsPlayedToday();
		assertTrue(cynthia.getNumSongsPlayedToday() == 1);
	}
	
	@Test
	public void testCardReader2() {
		CardReader myCardReader = new CardReader();
		Account patrick = myCardReader.authenticateUser("patrick", "is fancy");
		assertTrue(patrick == null);
	}
	
	@Test
	public void testCardReader3() {
		CardReader myCardReader = new CardReader();
		Account test = myCardReader.authenticateUser("Captain Kirk", "Enterprise");
		assertTrue(test == null);
	}
	
	@Test
	public void testSong() {
		Song bigPoppa = new Song(new File("songfiles/flute.aif"), "Biggie Smalls", "I Love It When They Call Me Big Poppa", 747474, LocalDate.now());
		bigPoppa.setTimesPlayedToday();
		bigPoppa.setTimesPlayedToday();
		assertTrue(bigPoppa.getTimesPlayedToday() == 2);
		assertTrue(bigPoppa.getFileName().equals("flute.aif"));
	}
	
	@Test
	public void testDeciderAndSongSelectionEnum() {
		Account fred = new Account("fred", "");
		Account fred2 = new Account("freddy", "");
		Song gaga = new Song(new File("songfiles/flute.aif"), "Lady Gaga", "Telephone", 1, LocalDate.now());
		Decider fancyDecider = new Decider();
		assertEquals(SongSelection.SUCCESS, fancyDecider.canPlaySong(fred, gaga));
		assertEquals(SongSelection.SUCCESS, fancyDecider.canPlaySong(fred, gaga));
		assertEquals(SongSelection.SUCCESS, fancyDecider.canPlaySong(fred, gaga));
		assertEquals(SongSelection.NO_PLAYS_REMAINING_USER, fancyDecider.canPlaySong(fred, gaga));
		assertEquals(SongSelection.NO_PLAYS_REMAINING_SONG, fancyDecider.canPlaySong(fred2, gaga));
	}
	
	@Test
	public void testDeciderAndSongSelectionEnum2() {
		Account fred = new Account("fred", "");
		Account fred2 = new Account("freddy", "");
		Song gaga1 = new Song(new File("songfiles/flute.aif"), "Lady Gaga", "Telephone", 1, LocalDate.now());
		Song gaga2 = new Song(new File("songfiles/flute.aif"), "Lady Gaga", "Poker Face", 1, LocalDate.now());
		Song gaga3 = new Song(new File("songfiles/flute.aif"), "Lady Gaga", "Alejandro", 1, LocalDate.now());
		Song gaga4 = new Song(new File("songfiles/flute.aif"), "Lady Gaga", "Bad Romance", 1, LocalDate.now());
		Decider fancyDecider = new Decider();
		assertEquals(SongSelection.SUCCESS, fancyDecider.canPlaySong(fred, gaga1));
		assertEquals(SongSelection.SUCCESS, fancyDecider.canPlaySong(fred, gaga2));
		assertEquals(SongSelection.SUCCESS, fancyDecider.canPlaySong(fred, gaga3));
		assertEquals(SongSelection.NO_PLAYS_REMAINING_USER, fancyDecider.canPlaySong(fred, gaga1));
		assertEquals(SongSelection.SUCCESS, fancyDecider.canPlaySong(fred2, gaga1));
		assertEquals(SongSelection.NO_PLAYS_REMAINING_USER, fancyDecider.canPlaySong(fred, gaga4));
	}
	
	/*
	 * This one makes a song that (falsely) claims to take an entire user's credit.
	 */
	@Test
	public void testDeciderAndSongSelectionEnum3() {
		Account fred = new Account("fred", "");
		Account fred2 = new Account("freddy", "");
		Account fred3 = new Account("freddyPie", "");
		Account fred4 = new Account("freddyPies", "");
		Account fred5 = new Account("fredo", "");
		Song gaga1 = new Song(new File("songfiles/flute.aif"), "Lady Gaga", "Telephone", 1, LocalDate.now());
		Song gaga2 = new Song(new File("songfiles/flute.aif"), "Lady Gaga", "Poker Face", 1500*60, LocalDate.now());
		Decider fancyDecider = new Decider();
		assertEquals(SongSelection.SUCCESS, fancyDecider.canPlaySong(fred, gaga1));
		assertEquals(SongSelection.SUCCESS, fancyDecider.canPlaySong(fred, gaga1));
		assertEquals(SongSelection.NOT_ENOUGH_CREDIT, fancyDecider.canPlaySong(fred, gaga2));
		assertEquals(SongSelection.SUCCESS, fancyDecider.canPlaySong(fred, gaga1));
		assertEquals(SongSelection.SUCCESS, fancyDecider.canPlaySong(fred2, gaga2));
		assertEquals(SongSelection.NOT_ENOUGH_CREDIT, fancyDecider.canPlaySong(fred2, gaga1));
		assertEquals(SongSelection.SUCCESS, fancyDecider.canPlaySong(fred3, gaga2));
		assertEquals(SongSelection.SUCCESS, fancyDecider.canPlaySong(fred4, gaga2));
		assertEquals(SongSelection.NO_PLAYS_REMAINING_SONG, fancyDecider.canPlaySong(fred5, gaga1));
		assertEquals(SongSelection.NO_PLAYS_REMAINING_SONG, fancyDecider.canPlaySong(fred5, gaga2));
	}
	
	
	@Test
	public void testJukeboxPrototype() throws InterruptedException {
		//Microsoft:Tada:tada.wav:2
		//Sun Microsystems:Flute:flute.aif:6
		 //* NOTE: for complete testing including hearing the sounds when they play,
		 //*       run the JukeboxPrototype as a java application.  (The JUnit environment
		 //*       seems not to like to deal with waiting for other threads or
		 //*       something.)
		 
		Jukebox jukebox = new Jukebox();
		jukebox.useCardReader("patrick", "kelly");
		String myAccount = jukebox.printUsername();
		assertTrue(myAccount.equals("patrick"));
		assertTrue(jukebox.getCurrentSongTitle().equals(""));
		assertEquals(SongSelection.SONG_NOT_EXIST, jukebox.requestSongFromMenu("NotASong"));
		assertEquals(SongSelection.SUCCESS, jukebox.requestSongFromMenu("Tada"));
		assertTrue(jukebox.getCurrentSongTitle().equals("Tada"));
		System.out.println("queue size should be 1: " + jukebox.getPlaylist().getSize());
		System.out.println("should be 'Tada': " + jukebox.getCurrentSongTitle());
		assertEquals(SongSelection.SUCCESS, jukebox.requestSongFromMenu("Flute"));
		// assertTrue(jukebox.getCurrentSongTitle().equals("Flute"));
		// test above won't work in JUnit; we never start executing the 2nd song request
		// test in main() of JukeboxPrototype
		assertEquals(SongSelection.SUCCESS, jukebox.requestSongFromMenu("Tada"));
		assertEquals(SongSelection.NO_PLAYS_REMAINING_USER, jukebox.requestSongFromMenu("Flute"));
		jukebox.removeCurrentAccount();
		assertTrue(jukebox.printUsername().equals(""));
	}

	
//	@Test
//	public void testSongRequest() {
//		Jukebox jukebox = new Jukebox();
//		File songFile = new File("songfiles/tada.wav");
//		assertTrue(songFile.exists());
//		Song mySong = new Song(songFile, "Microsoft", "Tada", 6, LocalDate.now());
//		SongRequest myRequest = new SongRequest(mySong, jukebox.getSongQueueListener());
//		myRequest.execute();
//		
//	}
	
	@Test
	public void testAccount() {
		Account fancyAccount = new Account("fancy", "account");
		assertTrue(fancyAccount.getNumSongsPlayedToday() == 0);
		fancyAccount.incrementNumSongsPlayedToday();
		assertTrue(fancyAccount.getNumSongsPlayedToday() == 1);
		
	}
	
	@Test
	public void testSongLibraryRetrieveTitles() {
		SongLibrary library = new SongLibrary("songs.txt");
		TreeMap<String, Song> theMap = library.retrieveTitles();
		assertTrue(theMap.get("Flute").getArtist().equals("Sun Microsystems"));
		assertFalse(theMap.containsKey("Ziggy Stardust"));
		
	}
	

	@Test
	public void testCreatingNewAccountCollection() {
		File fileName = new File("extra_files/accounts.txt");
		System.out.println(fileName.getAbsolutePath());
		
		AccountCollection acc = new AccountCollection();
		FileReader fileReader;
		try {
			fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line = "";
			StringTokenizer st;
			
			while((line = bufferedReader.readLine()) != null) {
				st = new StringTokenizer(line, " ");
				String tok = st.nextToken();
				assertTrue(acc.userExists(tok));
				assertTrue(acc.getAccount(tok).getName().equals(tok));
				System.out.println("User Entered: " + tok);
			}
			
		} 
		catch (FileNotFoundException e) {
			System.out.println("File Not Found!!!");
			e.printStackTrace();
		}
		catch(IOException e) {
			System.out.println("Error Reading File!!!");
			e.printStackTrace();
		}

	}



}

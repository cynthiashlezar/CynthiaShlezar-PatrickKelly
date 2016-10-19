package model;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.time.LocalDate;

/*
 * Programmer: Patrick Kelly
 * Course: CSC 335 (Fall '16)
 * Section:  Miranda's
 */

/*
 * Class SongLibrary
 * 
 * Responsibility: This class maintains the collection of songs in the Jukebox system. 
 * 
 * Contains methods for determining whether a song is present as well as returning song
 * objects to be played.
 * 
 * Builds the collection of songs from a text file called "songs.txt" in the "extra_files"
 * folder of the project.
 */

public class SongLibrary implements TableModel, Serializable {
	
	private TreeMap<String, Song> library;
	private static SongLibrary self;
	
	/*
	 * Constructor.  Private, because the we're using the Singleton pattern and only ever
	 * want one instance of the SongLibrary.
	 * 
	 * Requires a filename.  Assumes this file is in the "extra_files" folder.
	 */
	private SongLibrary(String songsFileName) {
		library = new TreeMap<String, Song>();
		createSongs(songsFileName);
	}
	
	/*
	 * Gets a reference to the Singleton SongLibrary object.  (If the object hasn't
	 * been created yet, this method calls the private constructor.)
	 * 
	 * Requires a filename.  Assumes this file is in the "extra_files" folder.
	 */
	public static synchronized SongLibrary getInstance(String songsFileName) {
		if (self == null) {
			self = new SongLibrary(songsFileName);
		}
		return self;
	}
	
	/*
	 * Loads data from the songs.txt file to add all songs into the SongLibrary.  This file
	 * is colon delimited and should have 4 fields per line, all strings.  EG:
	 * 			artist:title:filename:length(in seconds) 
	 */
	private void createSongs(String songsFileName)  {
		// variables for opening and parsing the songs text file
		File songsFile;
		String line;
		StringTokenizer tokens;
		// variables for creating the song
		String title, artist, fileName;
		int length;
		File currentSongFile;
		
		
		try{
			//open the text file with song info
			songsFile = new File("extra_files/" + songsFileName);
			Scanner fileReader = new Scanner(songsFile);
			// read each line of the file
			while (fileReader.hasNextLine()) {
				// parse the line into tokens
				tokens = new StringTokenizer(fileReader.nextLine().trim(), ":");
				// there should be 4 tokens in the line ... otherwise, we don't have complete song info
				if (tokens.countTokens() == 4) {
					// get song file and song metadata from tokens
					artist = tokens.nextToken();
					title = tokens.nextToken();
					fileName = "songfiles/" + tokens.nextToken();
					currentSongFile = new File(fileName);
					length = Integer.parseInt(tokens.nextToken());
					// create the song object
					Song songToAdd = new Song(currentSongFile, artist, title, length, LocalDate.now().minusDays(1));
					// add the song object to the library
					library.put(title, songToAdd);
				} 
			}
			fileReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("Couldn't open file " + e.getMessage());
		}
	}
	
	/*
	 * Returns true if the library contains a Song object associated
	 * with the provided title.  (False otherwise.)
	 */
	public boolean songExists(String title) {
		return library.containsKey(title);
	}
	
	
	/*
	 * Returns the song object associated with the title.  If the song isn't in the library,
	 * returns null.
	 * 
	 * @params: String title: the title of the song.
	 * @return: the song
	 */
	public Song getSong(String title) {
		if (songExists(title)) {
			return library.get(title);
		} else {
			return null;
		}
	}
	
	/*
	 * Returns an ArrayList containing all the songs in the SongLibrary.  Used
	 * by the view to be able to list the songs.
	 */
	public ArrayList<Song> fetchLibrary() {
		
		ArrayList<Song> songs = new ArrayList<>();
		
		
		for(Map.Entry<String, Song> entry : library.entrySet()) {
			Song song = entry.getValue();
			songs.add(song);
		}
		return songs;	
	}

	

	/*
	 * Returns the Java Class of a column in the TableModel.
	 * 
	 * (Hint: all columns are String.)
	 * 
	 * (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnClass(int)
	 */
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	@Override
	public int getColumnCount() {
		return 3;
	}

	/*
	 * Returns the names of the columns in the TableModel as Strings.
	 * 
	 * (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int columnIndex) {
		switch (columnIndex) {
		case 0: return "Title";
		case 1: return "Artist";
		case 2: return "Seconds";
		default: return null;
	}
	}

	/*
	 * Returns the number of rows in the TableModel.
	 * 
	 * (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	@Override
	public int getRowCount() {
		return library.size();
	}

	/*
	 * Returns the String value for the data (song title, artist, or length) stored
	 * at the given RowIndex and ColumnIndex.
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		ArrayList<Song> songsArrayList = fetchLibrary();
		if (rowIndex < 0 || rowIndex >= songsArrayList.size()) {
			return null;
		}
		Song currentSong = songsArrayList.get(rowIndex);
		switch (columnIndex) {
			case 0: return currentSong.getTitle();
			case 1: return currentSong.getArtist();
			case 2: return "" + currentSong.getLength();
			default: return null;
		}
	}

	/*
	 * The rest of the TableModel methods below are not used by our program.
	 * 
	 * (non-Javadoc)
	 * @see javax.swing.table.TableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		// No.
		return false;
	}
	
	@Override
	public void addTableModelListener(TableModelListener l) {
		// no.  Not used by our program.
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		// no.  Not used by our program.
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		// no.  Not used by our program.
	}
	
	
}

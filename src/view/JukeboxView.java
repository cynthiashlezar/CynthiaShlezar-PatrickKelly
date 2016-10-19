/*
 * Name: Cynthia Shlezar & Patrick Kelly
 * 
 * File: JukeboxView
 * 
 * Purpose: The purpose of this class is to create a total view of the Jukebox
 * 			It holds JPanels for the user Login view, and the song select menu
 * 			view
 * 
 */
package view;


import java.awt.Font;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Jukebox;

public class JukeboxView extends JPanel implements Observer {
	
	//private SongMenu songSelectionPart;
	private UserLogin loginScreen; 
	private Jukebox system;
	private LibraryView library;
	private QueueView queue;

	public JukeboxView(Jukebox system) {
		
		this.system = system;
		
		this.setLayout(null);
		
		Font myFont = new Font("Arial", Font.TRUETYPE_FONT, 20);
		
		JLabel libraryTitle = new JLabel();
		libraryTitle.setText("Library: Pick a Song");
		libraryTitle.setLocation(25, -12);
		libraryTitle.setFont(myFont);
		libraryTitle.setSize(300,50);

		
		this.add(libraryTitle);
		
		library = new LibraryView(520, 375, system);
		library.setLocation(25, 25);
		this.add(library);
		system.addObserver(library);
		
		/*songSelectionPart = new SongMenu(system);	//setup song menu
		songSelectionPart.setLocation(25, 25);
		songSelectionPart.setSize(350, 375);
		this.add(songSelectionPart);
		system.addObserver(songSelectionPart);
		*/
		
		loginScreen = new UserLogin(system);	//set up user log in
		loginScreen.setLocation(25, 425);
		loginScreen.setSize(350, 125);
		this.add(loginScreen);
		system.addObserver(loginScreen);	
		
		
		JLabel upNext = new JLabel();
		upNext.setText("Playlist");
		upNext.setLocation(600, -12);
		upNext.setFont(myFont);
		upNext.setSize(300,50);
		this.add(upNext);
		
		queue = new QueueView(300, 375, system);
		queue.setLocation(600, 25);
		this.add(queue);
		system.addObserver(queue);

		
		
	}
	
	
	/*
	 * Method: update
	 * 
	 * Purpose: we don't really need to send information to the Jukebox
	 * 
	 * (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {
		
	}
}
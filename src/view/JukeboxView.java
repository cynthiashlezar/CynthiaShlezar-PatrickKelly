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


import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Jukebox;

public class JukeboxView extends JPanel implements Observer {
	
	private SongMenu songSelectionPart;
	private UserLogin loginScreen; 
	private Jukebox system;

	public JukeboxView(Jukebox system) {
		
		this.system = system;
		
		this.setLayout(null);
		
		songSelectionPart = new SongMenu(system);	//setup song menu
		songSelectionPart.setLocation(100, 0);
		songSelectionPart.setSize(800, 600);
		this.add(songSelectionPart);
		system.addObserver(songSelectionPart);
		
		loginScreen = new UserLogin(system);	//set up user log in
		loginScreen.setLocation(25, 150);
		loginScreen.setSize(350, 125);
		this.add(loginScreen);
		system.addObserver(loginScreen);	 

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
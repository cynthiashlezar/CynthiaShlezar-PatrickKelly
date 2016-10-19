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

public class AA_JukeboxView2 extends JPanel implements Observer {
	
	//private SongMenu songSelectionPart;
	private AA_UserLogin2 loginScreen; 
	private Jukebox system;
	private AA_LibraryView2 library;
	private AA_QueueView2 queue;

	public AA_JukeboxView2(Jukebox system) {
		
		this.system = system;
		
		this.setLayout(null);
		
		library = new AA_LibraryView2(350, 375, system);
		library.setLocation(25, 25);
		this.add(library);
		system.addObserver(library);
		
		/*songSelectionPart = new SongMenu(system);	//setup song menu
		songSelectionPart.setLocation(25, 25);
		songSelectionPart.setSize(350, 375);
		this.add(songSelectionPart);
		system.addObserver(songSelectionPart);
		*/
		
		loginScreen = new AA_UserLogin2(system);	//set up user log in
		loginScreen.setLocation(25, 425);
		loginScreen.setSize(350, 125);
		this.add(loginScreen);
		system.addObserver(loginScreen);	
		
		queue = new AA_QueueView2(350, 400, system);
		queue.setLocation(375, 25);
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
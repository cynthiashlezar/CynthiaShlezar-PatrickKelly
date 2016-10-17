/*
 * Names: Cynthia Shlezar & Patrick Kelly
 * 
 * File: JukeboxGUI
 * 
 * Classes: JukeboxGUI
 * 
 * Purpose: this is the controller file for the Jukebox. It launches the GUI
 * so that the user can interact with the Jukebox. It contains a view, in which
 * that contains different JPanels for the user login and the song menu. It includes
 * methods that sets up the GUI, and then the main method runs the whole thing
 * 
 * Instance Variables: 
 * 					   Jukebox jukebox - the system of the jukebox. It coordinates
 *                                       everything between all of the classes
 *                                       located in the model package
 *                     JukeboxView - the view of the jukebox. holds panels for the user
 *                                   login, the status, and the song menu
 *                     int width, height - the width and height of the window size
 *                   
 * Methods: 
 *                    private void setup() - sets up the the view and adds it as an
 *                                           observer to the jukebox class
 *                    private void addAllObservers() - adds the view as an observer
 *                    public static void main(String [] args) - runs the thing
 *                                                              and launches the
 *                                                              GUI
 *                                                              
 */
package controller;

import javax.swing.JFrame;

import model.Jukebox;
import view.JukeboxView;

public class JukeboxGUI extends JFrame {
	
	private Jukebox jukebox;
	private JukeboxView view;
	private static final int width = 800;
	private static final int height = 600;
	
	public JukeboxGUI() {
		setup();
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(width, height);
		this.setTitle("Jukebox");
	}
	
	/*
	 * Method: setup
	 * 
	 * Purpose: sets up the view for the GUI and adds the view
	 *          as an observer to the system
	 * 
	 * Arguments: none
	 * 
	 * Return: none
	 */
	
	private void setup() {
		jukebox = new Jukebox();
		view = new JukeboxView(jukebox);
		addAllObservers();
		add(view);
		view.repaint();
		view.revalidate();
	}
	
	/*
	 * Method: addAllObservers
	 * 
	 * Purpose: adds all the observers to the jukebox (Observable).
	 *          however, there only exists one view
	 * 
	 * Arguments: none
	 * 
	 * Return: void
	 */
	
	private void addAllObservers() {
		jukebox.addObserver(view);
	}
	
	/*
	 * Method: main
	 * 
	 * Purpose: runs jukebox and launches the GUI for it
	 * 
	 * Arguments: String [] args - trivial
	 * 
	 * Return: void
	 */
	
	public static void main(String [] args) {
		JukeboxGUI g = new JukeboxGUI();
		g.setVisible(true);
	}

}

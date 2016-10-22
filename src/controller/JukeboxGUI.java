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

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import model.Jukebox;
import view.JukeboxView;

public class JukeboxGUI extends JFrame {
	
	private Jukebox jukebox;
	private JukeboxView view;
	private static final int width = 1000;
	private static final int height = 600;
	private WindowHandler handler;
	
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
	
	private void setup()  {
		handler = new WindowHandler();
		this.addWindowListener(handler);
		int loadQuestion = JOptionPane.showConfirmDialog(null, "Do you want to load the business?", "Load?", JOptionPane.YES_NO_OPTION);
		if (loadQuestion == JOptionPane.YES_OPTION) {
			try {
				FileInputStream fileIn = new FileInputStream("extra_files/savestate.ser");
				ObjectInputStream in = new ObjectInputStream(fileIn);
				jukebox = (Jukebox) in.readObject();
				in.close();
				fileIn.close();
				jukebox.homeMadeNotify();
			} catch (IOException i) {
				System.out.println("couldn't open file");
				i.printStackTrace();
				
			} catch (ClassNotFoundException e) {
				System.out.println("No jukebox in here");
				e.printStackTrace();
			}
		} else {
			jukebox = new Jukebox();
		} 
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
	
	/*
	 * Class: WindowHandler
	 * 
	 * Purpose: We need to do things whenever the gui's window is closed, so
	 *          the WindowHandler does that. It's basically a listener for when
	 *          the user does something with With closing the window so 
	 *          For example when the user closes the window then the GUI knows
	 *          Whether or not to save the current state of the object to a file.
	 *          The method window closing can save the jukebox object into a file 
	 *          and if the file couldn't be read or couldn't be written in then 
	 *          an exception is thrown
	 */
	
	private class WindowHandler extends WindowAdapter {


		/*Method: windowClosing
		 * 
		 * Purpose: AJ option pain shows a confirmed dialogue with the options
		 *  yes or now asking whether or not you want to save the state of the 
		 *  jukebox is the user wants to save the state of the jukebox then it
		 *   is written to a file if not nothing is written into the file and 
		 *   the GUI closes
		 *   
		 * (non-Javadoc)
		 * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
		 */

		@Override
		public void windowClosing(WindowEvent e) {
			// TODO Auto-generated method stub
			int reply = JOptionPane.showConfirmDialog(null, "Do you want to save the business?", "Save?", JOptionPane.YES_NO_OPTION);
			if (reply == JOptionPane.YES_OPTION) {
				// save things
				try {
					System.out.println("closing");
					FileOutputStream fileOut = new FileOutputStream("extra_files/savestate.ser");
					ObjectOutputStream out = new ObjectOutputStream(fileOut);
					out.writeObject(jukebox);
					out.close();
					fileOut.close();
				} catch (IOException exception) {
					System.out.println("You couldn't save to an outpult file.  WAH WAH.");
					exception.printStackTrace();
				}
			} else {
				System.exit(0);
			}
			
		}


		
	}

}

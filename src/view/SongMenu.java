///*
// * Names: Cynthia Shlezar & Patrick Kelly
// * 
// * File: SongMenu
// * 
// * Purpose: creates a part of the view, full of buttons, so that the user
// * can click on 4 different buttons, and then play a song if they are logged in
// * and allowed to plya a song, as per the Decider
// */
//package view;
//
//import java.awt.GridLayout;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.util.ArrayList;
//import java.util.Observable;
//import java.util.Observer;
//import java.util.TreeMap;
//
//import javax.swing.AbstractButton;
//import javax.swing.JButton;
//import javax.swing.JLabel;
//import javax.swing.JOptionPane;
//import javax.swing.JPanel;
//
//import model.Jukebox;
//import model.Song;
//
//public class SongMenu extends JPanel implements Observer {
//	
//	private static final int width = 500;
//	private static final int height = 50;
//	 
//	
//	JButton[] songButtons;
//	ButtonListener buttonLis;
//	Jukebox system;
//	
//	public SongMenu(Jukebox system) {
//		
//		this.system = system;
//		this.setSize(width, height);
//		this.setLocation(0, 0);
//		this.setLayout(new GridLayout(2,2,10,10));
//		
//		setButtons();
//	}
//	/*
//	 * Method: setButtons
//	 * 
//	 * Purpose: sets up 4 buttons in a grid layout, and 
//	 * assigns buttonlisteners to them, so that when clicked
//	 * they will play a song
//	 */
//	private void setButtons() {
//		
//		songButtons = new JButton[4];
//		buttonLis = new ButtonListener();
//		
//		for(int i = 0; i < 4; i++) {
//			songButtons[i] = new JButton("Select Song " + (i+1));
//			songButtons[i].addActionListener(buttonLis);
//			this.add(songButtons[i]);
//		}
//	}
//	
//	/*
//	 * Class: ButtonListener
//	 * 
//	 * Purpose: when a button is clicked, do actionPerformed
//	 */
//	
//	private class ButtonListener implements ActionListener {
//		
//		ArrayList<String> songs;
//		/*
//		 * Method: actionPerformed
//		 * 
//		 * Purpose: when a button is clicked, a song will play. A 
//		 * song will only play if the song has been played less than
//		 * 3 times, the user is logged in, and the user has only played
//		 * a song less that 3 times. Otherwise, a JOptionPane will show
//		 * a message, letting them know why a song couldn't be played
//		 * 
//		 * (non-Javadoc)
//		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
//		 */
//		@Override
//		public void actionPerformed(ActionEvent e) {
//			//if button says Select Song 1...Select Song 9
//			JButton buttonClicked = (JButton) e.getSource();
//			if (system.getCurrentAccount() == null) {
//				JOptionPane.showMessageDialog(null, "Log in first.");
//				return;
//			}
//			
//			//temporary song list
//			boolean truth = false;
//			addSongs();
//			
//			for(int i = 1; i < 5; i++) {
//				if(buttonClicked.getText().equals("Select Song " + i)) {
//					if(system.getCurrentAccount() == null) {
//						JOptionPane.showMessageDialog(null, "You must log in to play a song!");
//						break;
//					}
//					truth = system.requestSongFromMenu(songs.get(i));
//					if(!truth) {
//						JOptionPane.showMessageDialog(null, "You can't play the song!");
//					}
//				}
//			}
//		}
//		/*
//		 * Method: addSongs
//		 * 
//		 * Purpose: puts songs into an arraylist for easy access (for now,
//		 *          only temporary)
//		 */
//		private void addSongs(){
//			songs = system.fetchLibrary();
//		}		
//	}
//
//	/*
//	 * Method: update
//	 * 
//	 * Purpose: we don't really need to send information to the Jukebox
//	 * 
//	 * (non-Javadoc)
//	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
//	 */
//	@Override
//	public void update(Observable o, Object arg) {
//	}
//	
//	
//}

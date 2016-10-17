package view;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import model.Jukebox;

public class QueueView extends JPanel implements Observer {
	
	/*
	 * 
	 * set up size and location
	 * set up jlist
	 * have now playing and up next
	 * put it into the panel
	 * call update in jukebox, any time a song finishes
	 */
	private Jukebox system;
	//private SongQueue queue;

	public QueueView(int width, int height, Jukebox system) {
		
	}

	@Override
	public void update(Observable o, Object arg) {
		
		
	}

}

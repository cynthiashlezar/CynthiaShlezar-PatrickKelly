package view;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListModel;

import model.Jukebox;
import model.Jukebox.SongQueue;

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
	private ListModel queue;
	private JList list;

	public QueueView(int width, int height, Jukebox system) {
		
		setSize(width, height);
		
		this.system = system;
		this.queue = system.getPlaylist();
		
		list = new JList();
		list.setModel(queue);
		
		list.setSize(width, height);
		this.add(list);
		
	}

	@Override
	public void update(Observable o, Object arg) {
		queue = system.getPlaylist();
		list.setModel(queue);
		
	}

}

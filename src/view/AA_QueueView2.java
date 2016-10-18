package view;

import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListModel;

import model.Jukebox;
import model.Jukebox.SongQueue;

public class AA_QueueView2 extends JPanel implements Observer {
	
	/*
	 * 
	 * set up size and location
	 * set up jlist
	 * have now playing and up next
	 * put it into the panel
	 * call update in jukebox, any time a song finishes
	 */
	private Jukebox system;
	private ListModel<String> queue;
	private JList<String> list;

	public AA_QueueView2(int width, int height, Jukebox system) {
		
		this.setLayout(new BorderLayout());
		setSize(width, height);
		
		this.system = system;
		this.queue = system.getPlaylist();
		
		
		list = new JList<String>();
		list.setModel(queue);
		
		list.setSize(width, height);
		
		list.setVisible(true);
		this.add(list);
		
		this.setVisible(true);
		
		
	}

	@Override
	public void update(Observable o, Object arg) {
		queue = system.getPlaylist();
		list.setModel(queue);
		
	}

}

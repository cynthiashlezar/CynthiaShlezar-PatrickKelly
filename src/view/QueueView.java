/*
 * Name: Cynthia Shlezar and Patrick Kelly
 * 
 * Class: QueueView
 * 
 * Purpose: the purpose of QueueView is to provide a panel in which the entire
 *  song playlist can be seen. The class utilizes JList to do so. The JList's model
 *  is based on the SongQueue class that exists in Jukebox. SongQueue extends ListModel.
 *  The JList is updated an redrawn anytime the Jukebox updates the observers, which is
 *  basically anythime a song ends or is added to the playlist (or at least in concern
 *  for this class). The update method in this class grabs a new reference to the SongQueue
 *  in Jukebox and add that into the JList.
 */
package view;

import java.awt.Color;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;

import model.Jukebox;
import model.Jukebox.SongQueue;
import model.SongRequest;

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
		setBackground(Color.WHITE);
		
		this.system = system;
		this.queue = system.getPlaylist();

		
//		DefaultListModel<String> listModel = new DefaultListModel<>();
//		listModel.addElement("USA");
//		listModel.addElement("India");
//		listModel.addElement("Vietnam");
//		listModel.addElement("Canada");
//		listModel.addElement("Denmark");
//		listModel.addElement("France");
//		listModel.addElement("Great Britain");
//		listModel.addElement("Japan");
		
		list = new JList();
		
		list.setModel(queue);
		
		
		this.add(list);
		//JScrollPane scroll = new JScrollPane(list);
		//this.add(scroll);
	}
	
	/*Method: update
	 * 
	 * Purpose: The purpose of update is to retrieve a new SongQueue reference from the system
	 *          , add that to the JList, and have the JList display the new songlist
	 *          
	 * (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */


	@Override
	public void update(Observable o, Object arg) {
		System.out.println("update the queue gui!!");
		System.out.println("\t\tThings in the queue: ");
		for(int i = 0; i < queue.getSize(); i++) {
			System.out.println("\t\t\t" + queue.getElementAt(i));
		}
		queue = (SongQueue) system.getPlaylist();
		list.setModel(queue);
		
		remove(list);
		queue = system.getPlaylist();
		list = new JList<String>(queue);
		add(list);
		
		validate();
		repaint();
			
	}

}

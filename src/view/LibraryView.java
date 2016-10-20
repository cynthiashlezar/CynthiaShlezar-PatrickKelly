/*
 * Names: Cynthia Shlezar & Patrick Kelly
 * 
 * Class: LibraryView
 * 
 * Purpose: the purpose of the library view is to create a way for the user to look
 *          at all avaiable songs to pick from, and then select one song to play, 
 *          granted that they are logged in, they have enough credit, they have played
 *          less than 3 songs in one day, and a song they have chosen has also been
 *          played less than 3 times in one day.
 */
package view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import model.Jukebox;
import model.SongSelection;

public class LibraryView extends JPanel implements Observer {
	
	private int width, height;
	private TableModel model;
	private JTable table;
	private Jukebox system;
	
	public LibraryView(int width, int height, Jukebox system) {
		this.setLayout(null);
		
		this.system = system;
		setSize(width, height);
		
		JPanel songList = new JPanel();
		
		songList.setSize(width - 60, height);
		songList.setLocation(0, 0);
		
		this.model = (TableModel) system.getLibraryTable();
		table = new JTable(model);
		table.setSize(width - 50, height);
		table.setLocation(0, 0);
		table.setBackground(Color.WHITE);
		songList.add(table);
		
		
		JScrollPane scroll = new JScrollPane(table, 
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		
		songList.add(scroll);

		this.add(songList);
		
		
		RowSorter<TableModel> tableRowSorter = new TableRowSorter<>(model);
		table.setRowSorter(tableRowSorter);
		JButton selectSong = new JButton("->");
		selectSong.setSize(45, 45);
		selectSong.setLocation(465, height/2 - 45);
		
		this.add(selectSong);
		selectSong.addActionListener(new ButtonListener());
	}
	
	/*
	 * Class: ButtonListener
	 * 
	 *Purpose: the purpose of button listener is to select a song from 
	 *the table send that to the jukebox and play the song if the user 
	 *is logged in and the user has enough available plays and if the song 
	 *is available to play as well. The action performed method utilizes e-news
	 * to tell whether or not there was an error occurred
	 */
	
	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			SongSelection truth = SongSelection.FAILURE;
			
			if (system.printUsername().equals("")) {
				JOptionPane.showMessageDialog(null, "Log in first.");
				return;
			}
			
			if (table.getSelectedRow() < 0 || table.getSelectedRow() >= model.getRowCount()) {
				JOptionPane.showMessageDialog(null, "How can I play a song if you don't select one?  I don't even know how long that would take.");
				return;
			}

			
			truth = system.requestSongFromMenu((String) table.getValueAt(table.getSelectedRow(), 0));
			
			
			
			if(truth != SongSelection.SUCCESS) {
				JOptionPane.showMessageDialog(null, "You can't play the song!");
			}
			update(system, null);
//			SongSelection truth = SongSelection.FAILURE;
//			
//			if (system.getCurrentAccount() == null) {
//				JOptionPane.showMessageDialog(null, "Log in first.");
//				return;
//			}
//			
//			System.out.println("***Song to be requested: " + (String) table.getValueAt(table.getSelectedRow(), 0));
//			truth = system.requestSongFromMenu((String) table.getValueAt(table.getSelectedRow(), 0));
//			
//			if(truth != SongSelection.SUCCESS) {
//				JOptionPane.showMessageDialog(null, "You can't play the song!");
//			}
//			update(system, null);
		}
		
	}

	@Override
	public void update(Observable o, Object arg) {
		//nothing
	}

}

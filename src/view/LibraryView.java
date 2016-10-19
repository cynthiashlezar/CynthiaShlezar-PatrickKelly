package view;

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
		selectSong.setLocation(295, height/2 - 45);
		
		this.add(selectSong);
		selectSong.addActionListener(new ButtonListener());
	}
	
	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			SongSelection truth = SongSelection.FAILURE;
			
			if (system.getCurrentAccount() == null) {
				JOptionPane.showMessageDialog(null, "Log in first.");
				return;
			}
			
			System.out.println("***Song to be requested: " + (String) table.getValueAt(table.getSelectedRow(), 0));
			truth = system.requestSongFromMenu((String) table.getValueAt(table.getSelectedRow(), 0));
			
			if(truth != SongSelection.SUCCESS) {
				JOptionPane.showMessageDialog(null, "You can't play the song!");
			}
			update(system, null);
		}
		
	}

	@Override
	public void update(Observable o, Object arg) {
		
	}

}

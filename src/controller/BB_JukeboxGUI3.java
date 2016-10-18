package controller;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowSorter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import model.Jukebox;
import model.SongSelection;

public class BB_JukeboxGUI3 extends JFrame implements Observer {
	
	
	private Jukebox jukebox;
	private JPanel libraryPanel;
	private JPanel queuePanel;
	private JPanel selectorPanel;
	private JPanel loginPanel;
	private JPanel statusPanel;
	private JPanel emptyPanel;
	private ButtonListener listener;
	
	private JButton songSelector;
	
	//login stuff
	private JButton loginButton, signoutButton;
	JLabel status;
	JLabel statusLabel;
	JLabel accLabel, passLabel;
	JTextField accField;
	JPasswordField passField;
	
	//library stuff
	private TableModel model;
	private JTable table;
	
	
	
	
	
	public BB_JukeboxGUI3() {
		
		jukebox = new Jukebox();
		
		this.setSize(1000, 400);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new GridLayout(3, 2, 20, 20));
		
		libraryPanel = new JPanel();
		setUpLibrary();
		
		queuePanel = new JPanel();
		setUpQueue();
		
		selectorPanel = new JPanel();
		setUpSelector();
		
		emptyPanel = new JPanel();
		setUpEmpty();
		
		loginPanel = new JPanel();
		setUpLoginPanel();
		
		statusPanel = new JPanel();
		setUpStatusPanel();
		
		jukebox.addObserver(this);
		
		
	}
	
	public static void main(String [] args) {
		BB_JukeboxGUI3 g = new BB_JukeboxGUI3();
		g.setVisible(true);
	}



	private void setUpStatusPanel() {
		// TODO Auto-generated method stub
		
		
		statusPanel.setLayout(new GridLayout(2, 1));
		
		statusLabel = new JLabel("Status: ");
		status = new JLabel("Please Log In");
		
		statusPanel.add(statusLabel);
		statusPanel.add(status);
		
		statusPanel.setVisible(true);
		
		this.add(statusPanel);
		
	}



	private void setUpLoginPanel() {
		// TODO Auto-generated method stub
		
		loginPanel.setVisible(true);
		loginPanel.setLayout(new GridLayout(3, 2));
		
		accLabel = new JLabel("Account: ");
		accField = new JTextField("username");
		passLabel = new JLabel("Password");
		passField = new JPasswordField("password");
		loginButton = new JButton("Log in");
		signoutButton = new JButton("Sign out");
		

		
		loginButton.addActionListener(listener);
		signoutButton.addActionListener(listener);
		
		loginPanel.add(accLabel);
		loginPanel.add(accField);
		loginPanel.add(passLabel);
		loginPanel.add(passField);
		loginPanel.add(loginButton); 
		loginPanel.add(signoutButton);
		
		
		this.add(loginPanel);
		
	}



	private void setUpEmpty() {
		// TODO Auto-generated method stub
		this.add(emptyPanel);
		
	}



	private void setUpSelector() {
		// TODO Auto-generated method stub
		
		selectorPanel.setLayout(new BorderLayout());
		
		JButton selectSong = new JButton("Select Song");
		
		selectorPanel.add(selectSong, BorderLayout.NORTH);
		

		selectSong.addActionListener(new ButtonListener());
		
		this.add(selectorPanel);
		
	}



	private void setUpQueue() {
		// TODO Auto-generated method stub
		
		this.add(queuePanel);
		
	}



	private void setUpLibrary() {
		// TODO Auto-generated method stub
		libraryPanel.setLayout(new BorderLayout());
		
		model = jukebox.getLibraryTable();
		table = new JTable(model);
		
		JScrollPane scroll = new JScrollPane(table);
		libraryPanel.add(scroll);
		
		
		RowSorter<TableModel> tableRowSorter = new TableRowSorter<>(model);
		table.setRowSorter(tableRowSorter);
		
		
		
		
		
		this.add(libraryPanel);
		
	}



	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		{
			if (jukebox.printUsername() == "") {
				status.setText("Please log in.");
			} else {
				status.setText(jukebox.printUsername() + " logged in.\n" + 
						"Played: " + jukebox.getUserSongsPlayed()
					+ " Credit: " + (jukebox.getUserCredit()/60)/60 + ":"
					+ ((jukebox.getUserCredit()/60)%60) + ":"
					+ ((jukebox.getUserCredit()))%60);
			}
			
		}
		
	}
	
	
	
	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			JButton buttonClicked = (JButton) e.getSource();
			
			
			// login button
			if(buttonClicked.getText().equals("Log in")) {

				StringBuilder pass = new StringBuilder();
				pass.append(passField.getPassword());
				
				
				if(! jukebox.printUsername().equals("")) {
					JOptionPane.showMessageDialog(null, "Someone's already logged in!");
				}
				else if(!jukebox.useCardReader(accField.getText(), pass.toString())){
					
					JOptionPane.showMessageDialog(null, "Incorrect Username or Password!");
					
				}

			}
			
			// song selector button
			if (buttonClicked.getText().equals("Select Song")) {
				SongSelection truth = SongSelection.FAILURE;
				
				if (jukebox.printUsername().equals("")) {
					JOptionPane.showMessageDialog(null, "Log in first.");
					return;
				}
				
				if (table.getSelectedRow() < 0 || table.getSelectedRow() >= model.getRowCount()) {
					JOptionPane.showMessageDialog(null, "How can I play a song if you don't select one?  I don't even know how long that would take.");
					return;
				}

				
				truth = jukebox.requestSongFromMenu((String) table.getValueAt(table.getSelectedRow(), 0));
				
				
				
				if(truth != SongSelection.SUCCESS) {
					JOptionPane.showMessageDialog(null, "You can't play the song!");
				}
				update(jukebox, null);
			}
			
			// sign out button
			if(buttonClicked.getText().equals("Sign out")) {
				jukebox.removeCurrentAccount();
			}
			
			
		} 

		
	}
	
	

}

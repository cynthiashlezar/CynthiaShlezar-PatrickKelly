/*
 * Names: Cynthia Shlezar & Patrick Kelly
 * 
 * File: UserLogin
 * 
 * Purpose: creates a part of the view, of textfields and buttons, so that the user
 * can login and sign out
 */
package view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import model.Jukebox;

public class UserLogin extends JPanel implements Observer {
	
	private static final int width = 450;
	private static final int height = 325;
	
	Jukebox system;
	
	JButton login, signout;
	JLabel status;
	JLabel statusLabel;
	JLabel accLabel, passLabel;
	JTextField accField;
	JPasswordField passField;
	
	ButtonListener buttonLis;
	
	public UserLogin(Jukebox system) {
		this.system = system;
		this.setSize(width, height);
		this.setLayout(new GridLayout(4, 2));
		
		setup();
	}
	/*
	 * Method: setup
	 * 
	 * Purpose: sets up the positioning and listeners for the textfields
	 *         and buttons responsible for logging in a user and signing out
	 */
	private void setup() {
		
		buttonLis = new ButtonListener();
		
		accLabel = new JLabel("Account: ");
		accField = new JTextField("username");
		passLabel = new JLabel("Password");
		passField = new JPasswordField("password");
		login = new JButton("Log in");
		signout = new JButton("Sign out");
		statusLabel = new JLabel("Status: ");
		status = new JLabel("Not logged in");

		
		login.addActionListener(buttonLis);
		signout.addActionListener(buttonLis);

		
		this.add(accLabel);
		this.add(accField);
		this.add(passLabel);
		this.add(passField);
		this.add(login); 
		this.add(signout);
		this.add(statusLabel);
		this.add(status);

		
	}
	
	
	/*
	 * Method: update
	 * 
	 * Purpose: we don't really need to send information to the Jukebox,
	 * but we do need to update the status
	 * 
	 * (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {
//		status.setText("Played: " + system.getCurrentAccount().getNumSongsPlayedToday()
//				+ " Credit: " + (system.getCurrentAccount().getCreditAvailable()/60)/60 + ":"
//				+ ((system.getCurrentAccount().getCreditAvailable()/60)%60) + ":"
//				+ ((system.getCurrentAccount().getCreditAvailable()))%60);
		if (system.printUsername() == "") {
			status.setText("Please log in.");
		} else {
			status.setText("Played: " + system.getUserSongsPlayed()
				+ " Credit: " + (system.getUserCredit()/60)/60 + ":"
				+ ((system.getUserCredit()/60)%60) + ":"
				+ ((system.getUserCredit()))%60);
		}
		
	}
	
	/*
	 * Class: ButtonListener
	 * 
	 * Purpose: when a button is clicked, do actionPerformed
	 */
	private class ButtonListener implements ActionListener {
		
		/*
		 * Method: actionPerformed
		 * 
		 * Purpose: when a button is clicked, if it was sign in, then the username
		 * and password is validated. If invalid a JOptionPane warns you. If valid,
		 * Status will display. If the button was sign out, then a user is removed
		 * from being the current user. If no user was logged in, then tell the user
		 * that
		 * 
		 * (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			
			JButton buttonClicked = (JButton) e.getSource();
			
			if(buttonClicked.getText().equals("Log in")) {

				StringBuilder pass = new StringBuilder();
				pass.append(passField.getPassword());
				
				if(system.getCurrentAccount() != null) {
					JOptionPane.showMessageDialog(null, "Someone's already logged in!");
				}
				else if(!system.useCardReader(accField.getText(), pass.toString())){
					
					JOptionPane.showMessageDialog(null, "Incorrect Username or Password!");
					
				}
				else {
					status.setText("User:" + system.getCurrentAccount().getName() +
							" Credit:" + system.getCurrentAccount().getCreditAvailable()/60/60 + " hours");
				}
				update(system, null);

			}
			
			if(buttonClicked.getText().equals("Sign out")) {
				if(system.getCurrentAccount() == null)
					JOptionPane.showMessageDialog(null, "There was no one logged in!");
				system.removeCurrentAccount();
				status.setText("Not logged in");
				update(system, null);
			}
			
			
		} 
		
	}
	

}

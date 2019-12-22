package Admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Acl.User;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.*;
import com.google.firebase.auth.UserRecord.CreateRequest;
import com.google.firebase.auth.UserRecord.UpdateRequest;

public class Admin extends JPanel {
	
	private JFrame frame;
	
	static ArrayList<Object> emailAndPassword;

	static DefaultListModel<String> model;
	
	static JList list;
	
	static DefaultListModel dlm;
	
	Scanner scanner;
	
	public Admin() {
		initialize();
	}
		
	 public static void main(String[] args) throws IOException, FirebaseAuthException {
		
		 
		 list = new JList();
		 
		 EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						Admin window = new Admin();
						window.frame.setVisible(true);
						
						FirebaseOptions options = new FirebaseOptions.Builder()
				    		    .setCredentials(GoogleCredentials.getApplicationDefault())
				    		    .setDatabaseUrl("https://digitallocker-aedeb.firebaseio.com/")
				    		    .build();

				    		FirebaseApp.initializeApp(options);
				    		
				    		dlm = new DefaultListModel();
				    		
				    		ListUsersPage page = FirebaseAuth.getInstance().listUsers(null);
				    		while (page != null) {
				    		  for (ExportedUserRecord user : page.getValues()) {
				    			  if (user.getEmail() != null)
				    			  {
			
				    				  dlm.addElement(user.getEmail());

				    				 list.setModel(dlm);
				    			  }
				    			  if (user.getPhoneNumber() != null) {
				    				  
				    				  dlm.addElement(user.getPhoneNumber());		
				    				  
				    				  list.setModel(dlm);
				    				  

				    			  }
				    			  
				    			  
				    		  }
				    		  
				 
				    		  page = page.getNextPage();
				    		}
				    		
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});     			 
}



private void initialize() {
	frame = new JFrame();
	frame.setBounds(100, 100, 450, 300);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	JPopupMenu popupMenu = new JPopupMenu();
	addPopup(list, popupMenu);
	
	JLabel lblRetrieve = new JLabel("  Retrieve  ");
	popupMenu.add(lblRetrieve);
	
	lblRetrieve.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent arg0) {
			String email = (String) list.getSelectedValue();
			UserRecord userRecord = null;
			try {
				userRecord = FirebaseAuth.getInstance().getUserByEmail(email);
				
			} catch (FirebaseAuthException e1) {
				// TODO Auto-generated catch block
				try {
					userRecord = FirebaseAuth.getInstance().getUserByPhoneNumber(email);
				} catch (FirebaseAuthException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				FirebaseAuth.getInstance().getUser(userRecord.getUid());
				System.out.println("ID: " + userRecord.getUid() + "\nEmail: "+ userRecord.getEmail() + "\nPhone Number: " + userRecord.getPhoneNumber() + "\nProvider ID: " + userRecord.getProviderId() + "\n");
				
			} catch (FirebaseAuthException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
	});
	
	JLabel lblUpdate = new JLabel("  Update  ");
	popupMenu.add(lblUpdate);
	
	lblUpdate.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent arg0) {
			String email = (String) list.getSelectedValue();
			UserRecord userRecord = null;
			try {
				userRecord = FirebaseAuth.getInstance().getUserByEmail(email);
				
			} catch (FirebaseAuthException e1) {
				// TODO Auto-generated catch block
				try {
					userRecord = FirebaseAuth.getInstance().getUserByPhoneNumber(email);
				} catch (FirebaseAuthException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				FirebaseAuth.getInstance().getUser(userRecord.getUid());
				
				System.out.println("Enter email: ");
				scanner = new Scanner(System.in);
				String email1 = scanner.next();
				
				System.out.println("Enter password: ");
				scanner = new Scanner(System.in);
				String password = scanner.next();
				
				System.out.println("Enter username: ");
				scanner = new Scanner(System.in);
				String name = scanner.next();
				
				UpdateRequest request = new UpdateRequest(userRecord.getUid())
					    .setEmail(email1)
					    //.setPhoneNumber("+11234567890")
					    //.setEmailVerified(true)
					    .setPassword(password)
					    .setDisplayName(name);
					    //.setPhotoUrl("http://www.example.com/12345678/photo.png")
					    //.setDisabled(true);
				
				FirebaseAuth.getInstance().updateUser(request);
				System.out.println("Successfully updated user: " + userRecord.getUid() + "\n");
				
			} catch (FirebaseAuthException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			} finally {
		        if (scanner != null)
		            scanner.close();
		        }
		}
	});
	
	JLabel lblDelete = new JLabel("  Delete  ");
	popupMenu.add(lblDelete);
	
	lblDelete.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent arg0) {
			String email = (String) list.getSelectedValue();
			UserRecord userRecord = null;
			try {
				userRecord = FirebaseAuth.getInstance().getUserByEmail(email);
				
			} catch (FirebaseAuthException e1) {
				// TODO Auto-generated catch block
				try {
					userRecord = FirebaseAuth.getInstance().getUserByPhoneNumber(email);
				} catch (FirebaseAuthException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				FirebaseAuth.getInstance().deleteUser(userRecord.getUid());
				model = (DefaultListModel) list.getModel();
				int selectedIndex = list.getSelectedIndex();
				if (selectedIndex != -1) {
				    model.remove(selectedIndex);
				}
			} catch (FirebaseAuthException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
	});
	
	frame.getContentPane().setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
	frame.getContentPane().add(list);
	
	JButton btnNewButton = new JButton("Create User");
	btnNewButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
				
				System.out.println("Enter email: ");
				scanner = new Scanner(System.in);
				String email1 = scanner.next();
				
				System.out.println("Enter password: ");
				scanner = new Scanner(System.in);
				String password = scanner.next();
				
				System.out.println("Enter username: ");
				scanner = new Scanner(System.in);
				String name = scanner.next();
				
				CreateRequest request = new CreateRequest()
					    .setEmail(email1)
					    .setEmailVerified(false)
					    .setPassword(password)
					    //.setPhoneNumber("+11234567890")
					    .setDisplayName(name);
					    //.setPhotoUrl("http://www.example.com/12345678/photo.png")
					    //.setDisabled(false);

					UserRecord userRecord = null;
					try {
						userRecord = FirebaseAuth.getInstance().createUser(request);
					} catch (FirebaseAuthException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("Successfully created new user: " + userRecord.getUid());
					
			
		}
	});
	frame.getContentPane().add(btnNewButton);
}

private static void addPopup(Component component, final JPopupMenu popup) {
	component.addMouseListener(new MouseAdapter() {
		public void mousePressed(MouseEvent e) {
			if (e.isPopupTrigger()) {
				showMenu(e);
			}
		}
		public void mouseReleased(MouseEvent e) {
			if (e.isPopupTrigger()) {
				showMenu(e);
			}
		}
		private void showMenu(MouseEvent e) {
			popup.show(e.getComponent(), e.getX(), e.getY());
		}
	});
}
}



//Aleksandar Petrovic

package onstart;

import registry.RegistryManager;
import server.UserDbI;


import user.UserI;
import user.UserImpl;
import user.UserInfo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@SuppressWarnings("serial")
public class StartWindow extends JFrame {
	private static final Logger logger = Logger.getLogger(StartWindow.class.getName());
	private JTextField txtName;
	private JTextField txtPass;	
	private JTextField txtName1;
	private JTextField txtPass1;
	private JTextField txtPass2;
	JLabel lblWins;
	JLabel lblLosses;
	private JComboBox<ImageIcon> cbxAvatars;
	private JTable table;
	private UserDbI userDb;
	private UserI me;
	private UserInfo myInfo;
	private UserListModel userList;
	public String dbHost;
	
	//kreira prozor koji sadrzi ip adresu servera dbHost
	StartWindow(String dbHost){
		try {
			userDb = (UserDbI) RegistryManager.get(dbHost).lookup(UserDbI.RMI_NAME);
		} catch (AccessException e1) {
			e1.printStackTrace();
		} catch (RemoteException e1) {
			e1.printStackTrace();
		} catch (NotBoundException e1) {
			e1.printStackTrace();
		}
		
		this.dbHost=dbHost;
		
		start();
	}
	
	public void addWin(){
		myInfo.addWin();
	}
	
	public void addLoss(){
		myInfo.addLoss();
	}
	
	//kreira pocetni graficki interfejs
	public void start(){
		getContentPane().removeAll();
		JButton btnSignUp=new JButton("Sign Up");
		JButton btnLogin=new JButton("Log In");
		
		btnSignUp.setAlignmentX(CENTER_ALIGNMENT);
		btnSignUp.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				signUp();
			}
		});
		
		btnLogin.setAlignmentX(CENTER_ALIGNMENT);
		btnLogin.setPreferredSize(btnSignUp.getPreferredSize());
		btnLogin.setMaximumSize(btnSignUp.getMaximumSize());
		btnLogin.setMinimumSize(btnSignUp.getMinimumSize());
		btnLogin.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});
		
		JPanel panel=new JPanel();
		BoxLayout box=new BoxLayout(panel, BoxLayout.PAGE_AXIS);

		panel.setLayout(box);
		panel.add(Box.createVerticalGlue());
		panel.add(btnLogin);
		panel.add(Box.createVerticalStrut(10));
		panel.add(btnSignUp);
		panel.add(Box.createVerticalGlue());
		
		getContentPane().add(panel);
		
		repaint();
		revalidate();
	}
	
	private String getImageFromPath(String path){
		return path.substring(path.lastIndexOf("/")+1);
	}
	
	private boolean isAlreadyRegistered(String name){
		try {
			List<UserInfo> users=userDb.getAccounts();
			for(UserInfo u: users){
				if(u.getName().toLowerCase().equals(name))
					return true;
			}
		} catch(RemoteException e){
			reportError("Cannot get users from server", true, e);
		}
		return false;
	}
	
	//kreira signUp graficki interfejs
	private void signUp(){
		getContentPane().removeAll();
		
		txtName1=new JTextField(); 							setSize(txtName1);
		txtPass1=new JTextField(); 							setSize(txtPass1);
		txtPass2=new JTextField(); 							setSize(txtPass2);
		JLabel lblName=new JLabel("Name: "); 				setSize(lblName);
		JLabel lblPass1=new JLabel("Password: "); 			setSize(lblPass1);
		JLabel lblPass2=new JLabel("Re-enter password: ");	setSize(lblPass2);
		JButton btnSignUp=new JButton("Sign Up"); 
		JButton btnBack=new JButton("Back");
		
		
		btnSignUp.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(txtName1.getText().length()>0 && txtPass1.getText().length()>0 && txtPass2.getText().length()>0){
					if(txtPass1.getText().equals(txtPass2.getText())){
						UserInfo user=new UserInfo(txtName1.getText(), txtPass1.getText(), getImageFromPath(((ImageIcon)cbxAvatars.getSelectedItem()).toString()), 0, 0, null);
						if(!isAlreadyRegistered(txtName1.getText())){
							try {
								userDb.addAccount(user);
							} catch (RemoteException e1) {
								reportError("Failed registration on server", false, e1);
							}
							JOptionPane.showMessageDialog(null, "Registration successful", "Infromation", JOptionPane.INFORMATION_MESSAGE); 
						}
						else
							JOptionPane.showMessageDialog(null, "User already exists", "Infromation", JOptionPane.INFORMATION_MESSAGE); 
					}
					else{
						JOptionPane.showMessageDialog(null, "Password mismatch", "Warning", JOptionPane.WARNING_MESSAGE); 
						txtPass1.selectAll();
						txtPass1.requestFocus();
					}
				}
				else
					JOptionPane.showMessageDialog(null, "Field name or password is empty", "Error", JOptionPane.CANCEL_OPTION); 
			}
		});
		
		btnBack.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				start();
			}
		});
		
		JPanel panelUp=new JPanel(new FlowLayout(FlowLayout.LEADING));
		btnBack.setAlignmentX(LEFT_ALIGNMENT);
		panelUp.add(btnBack);
		getContentPane().add(panelUp, BorderLayout.NORTH);
		
		JPanel panel=new JPanel();
		BoxLayout box=new BoxLayout(panel, BoxLayout.PAGE_AXIS);
		panel.setLayout(box);
		
		panel.add(Box.createVerticalGlue());
		
		JLabel lbl = new JLabel("Avatar:");
		lbl.setAlignmentX(0.5f);
		panel.add(lbl);
		final int n = 5;
		ImageIcon[] icons = new ImageIcon[n];
		for (int i = 1; i <= n; i++)
			icons[i - 1] = new ImageIcon(getClass().getResource("res/a" + i + ".png"));
		cbxAvatars = new JComboBox<>(icons);
		cbxAvatars.setMaximumSize(new Dimension(64, 64));
		cbxAvatars.setSelectedIndex((int)(Math.random() * n));
		panel.add(cbxAvatars);
		
		panel.add(Box.createVerticalStrut(5));

		JPanel row1=new JPanel();
		BoxLayout box1=new BoxLayout(row1, BoxLayout.LINE_AXIS);
		row1.setLayout(box1);
		row1.add(lblName);
		row1.add(txtName1);
		panel.add(row1);
		
		panel.add(Box.createVerticalStrut(5));
		
		JPanel row2=new JPanel();
		BoxLayout box2=new BoxLayout(row2, BoxLayout.LINE_AXIS);
		row2.setLayout(box2);
		row2.add(lblPass1);
		row2.add(txtPass1);
		panel.add(row2);
		
		panel.add(Box.createVerticalStrut(5));
		
		JPanel row3=new JPanel(new FlowLayout());
		BoxLayout box3=new BoxLayout(row3, BoxLayout.LINE_AXIS);
		row3.setLayout(box3);
		row3.add(lblPass2);
		row3.add(txtPass2);
		panel.add(row3);
		
		panel.add(Box.createVerticalStrut(10));
		
		panel.add(btnSignUp);
		
		panel.add(Box.createVerticalGlue());
		
		getContentPane().add(panel);
		
		txtName1.requestFocus();
		
		repaint();
		revalidate();
	}
	
	private void setSize(JComponent cp){
		cp.setPreferredSize(new Dimension(100, 20));
		cp.setMaximumSize(new Dimension(100, 20));
		cp.setMinimumSize(new Dimension(100, 20));
	}
	
	//proverava da li user postoji u bazi registrovanih pri ulogovanju
	private boolean isAccountExists(UserInfo user){
		boolean ima=false;
		try{
			List<UserInfo> users=userDb.getAccounts();
			for(UserInfo u: users){
				if(u.getName().toLowerCase().equals(txtName.getText().toLowerCase()) && u.getPass().equals(user.getPass())){
					ima=true;
					user.setAvatar(u.getAvatar());
					user.setWins(u.getWins());
					user.setLosses(u.getLosses());
				}
			}
		}  catch(RemoteException e){
			reportError("Cannot get users from server", true, e);
		}
		if(ima==true)
			return true;
		return false;
	}
	
	//proverava da li je user vec online, ako jeste nemoguce prijavljivanje
	private boolean isLogged(UserInfo u){
		try {
			List<UserInfo> users = userDb.get();
			if(users.indexOf(u)!=-1)
				return true;
		} catch (RemoteException e) {
			// neuspela konekcija sa serverom
			e.printStackTrace();
		}
		return false;
	}
	
	//kreira login graficki interfejs
	private void login(){
		getContentPane().removeAll();
		txtName=new JTextField(); setSize(txtName);					setSize(txtName);
		txtPass=new JTextField(); setSize(txtPass);					setSize(txtPass);
		JLabel lblName=new JLabel("Name: "); setSize(lblName);		setSize(lblName);
		JLabel lblPass=new JLabel("Password: "); setSize(lblPass);	setSize(lblPass);
		JButton btnBack=new JButton("Back");
		JButton btnLogin=new JButton("Log In");
		
		btnLogin.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(txtName.getText().length()>0 && txtPass.getText().length()>0){
					//otvaranje veze sa serverom i trazenje da li taj korisnik postoji
					//ubacivanje user-a u listu onih koji su online i otvaranje prozora
					try {
						userDb=(UserDbI) RegistryManager.get(dbHost).lookup(UserDbI.RMI_NAME);
						String host = getIp();
						if (host == null)
							host = "localhost";
						myInfo=new UserInfo(txtName.getText(), txtPass.getText(), "", 0, 0, host);
						if(isAccountExists(myInfo)){
							if(!isLogged(myInfo)){
								try {
									Registry reg = RegistryManager.get(dbHost);
									me=new UserImpl(StartWindow.this);
									reg.rebind(txtName.getText(), me);
								
									userList=new UserListModel(dbHost, myInfo);
									table=new JTable(userList);
									table.setFillsViewportHeight(true);
									table.setRowHeight(64);
									table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
									table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

										@Override
										public void valueChanged(
												ListSelectionEvent event) {
											if ( table.getSelectedRowCount() == 0 ) return;
											if ( event.getValueIsAdjusting() == true ) return;
										
											UserInfo user = userList.getInfo(event.getFirstIndex());
											//ako slucajno klikne na nekog protivnika treba potvrda da zeli tu igru
											if(JOptionPane.YES_OPTION==JOptionPane.showConfirmDialog(StartWindow.this, "Challenge " + user.getName(), "Play?", JOptionPane.YES_NO_OPTION)){
												try {
													userDb.requestGame(myInfo, user, dbHost);
												} catch (RemoteException e) {
													reportError("Error while trying to challenge " + user.getName(), false, e);
												}
											}
											table.clearSelection();
										}
									});
								
									userList.add(myInfo);
								
									userDb.refreshOnline();
								} catch (AlreadyBoundException e1) {
									reportError("User name already exists", false, e1);
								}
								whenLogin();
							}
							else
								JOptionPane.showMessageDialog(null, "Account is already logged on server", "Error", JOptionPane.CANCEL_OPTION);
						}
						else
							JOptionPane.showMessageDialog(null, "Set account does not match any account on the server", "Error", JOptionPane.CANCEL_OPTION);  
						
					} catch (RemoteException e1) {
						reportError("Cannot create User object", true, e1);
					} catch (NotBoundException e1) {
						reportError("That object is not bind", true, e1);
					}
				}
				else
					JOptionPane.showMessageDialog(null, "Field name or password is empty", "Error", JOptionPane.CANCEL_OPTION);  
			}
		});
		
		btnBack.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				start();
			}
		});
		
		JPanel panelUp=new JPanel(new FlowLayout(FlowLayout.LEADING));
		btnBack.setAlignmentX(LEFT_ALIGNMENT);
		panelUp.add(btnBack);
		getContentPane().add(panelUp, BorderLayout.NORTH);
		
		JPanel panel=new JPanel();
		BoxLayout box=new BoxLayout(panel, BoxLayout.PAGE_AXIS);
		panel.setLayout(box);
		
		panel.add(Box.createVerticalGlue());
		
		JPanel row1=new JPanel();
		BoxLayout box1=new BoxLayout(row1, BoxLayout.LINE_AXIS);
		row1.setLayout(box1);
		row1.add(lblName);
		row1.add(txtName);
		panel.add(row1);
		
		panel.add(Box.createVerticalStrut(5));
		
		JPanel row2=new JPanel();
		BoxLayout box2=new BoxLayout(row2, BoxLayout.LINE_AXIS);
		row2.setLayout(box2);
		row2.add(lblPass);
		row2.add(txtPass);
		panel.add(row2);
		
		panel.add(Box.createVerticalStrut(10));
		
		panel.add(btnLogin);
		panel.add(Box.createVerticalGlue());
		
		getContentPane().add(panel);
		
		txtName.requestFocus();
		
		repaint();
		revalidate();
	}
	
	//uzima lokalnu ip adresu
	public static String getIp(){
	    Enumeration<NetworkInterface> net = null;
	    
	    try {
			net = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			System.out.println("Error while getting local ip address");
		}
	   
	    while(net.hasMoreElements()){
	        NetworkInterface element = net.nextElement();
	        Enumeration<InetAddress> addresses = element.getInetAddresses();
	        while (addresses.hasMoreElements()){
	            InetAddress ip = addresses.nextElement();
	            if (ip instanceof Inet4Address){
	                if (ip.isSiteLocalAddress()){
	                    return ip.getHostAddress();
	                }
	            }
	        }
	    }
	    return null;
	}
	
	//dogadjaj kada se zatvara prozor, unbind user i odjavljivanje
	WindowListener windowClosing=new WindowAdapter() {
		@Override
		public void windowClosing(WindowEvent e)
		{
			try {
				userList.remove(myInfo);
				userDb.refreshOnline();
				Registry reg = RegistryManager.get(dbHost);;
				try {
					reg.unbind(myInfo.getName());
				} catch (NotBoundException e1) {
					// ako korisnik nije online
					e1.printStackTrace();
				}
			} catch (RemoteException e1) {
				reportError("Error while logging out", false, e1);
			}
		}
	};
	
	//poziva UserI objekat kada se neki user odjavi 
	public void reloadOnlineUsers(){
		try {
			userList.reload();
		} catch (RemoteException e) {
			reportError("Error while reloading online users", false, e);
		}
	}
	
	//vraca 3 najgora igraca
	private List<UserInfo> getWorst(List<UserInfo> accounts){
		List<UserInfo> best=new LinkedList<>();
		best.addAll(accounts);
		Collections.sort(best);
		int indStart=best.size()-3;
		if(0>indStart)
			indStart=0;
		Collections.reverse(best.subList(indStart, best.size()));
	    return best.subList(indStart, best.size());
	}
	
	//vraca 3 najbolja igraca
	public List<UserInfo> getBest(List<UserInfo> accounts){
		List<UserInfo> best=new LinkedList<>();
		best.addAll(accounts);
		Collections.sort(best);
		int indEnd=3;
		if(best.size()<indEnd)
			indEnd=best.size();
	    return best.subList(0, indEnd);
	}
	
	
	//kreira graficki interfejs nakon sto se korisnik uloguje na server
	private void whenLogin(){
		getContentPane().removeAll();
		setSize(300, 600);
		
		JLabel image=new JLabel(new ImageIcon(getClass().getResource("res/"+myInfo.getAvatar())));  
		JLabel lblName=new JLabel(myInfo.getName());
		JLabel lblOnline=new JLabel("Online users:");
		JButton btnLogOut=new JButton("Log Out");
		lblWins=new JLabel("Wins: " + myInfo.getWins());
		lblLosses=new JLabel("Losses: " + myInfo.getLosses());
		JButton btngetBest=new JButton("Show 3 best");
		JButton btngetWorst=new JButton("Show 3 worst");
		
		JScrollPane scrol=new JScrollPane(table);
		
		btngetBest.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				List<UserInfo> users=new LinkedList<>();
				try {
					users.addAll(getBest(userDb.getAccounts()));
				} catch (RemoteException e) {
					// neuspela konekcija sa serverom
					e.printStackTrace();
				}
				String str="";
				for(UserInfo u: users){
					str+=u.getName() + " have " + u.getWins() + " wins" + "\n";
				}
				JOptionPane.showMessageDialog(StartWindow.this, str);
			}
		});
		
		btngetWorst.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				List<UserInfo> users=new LinkedList<>();
				try {
					users.addAll(getWorst(userDb.getAccounts()));
				} catch (RemoteException e) {
					// neuspela konekcija sa serverom
					e.printStackTrace();
				}
				String str="";
				for(UserInfo u: users){
					str+=u.getName() + " have " + u.getWins() + " wins" + "\n";
				}
				JOptionPane.showMessageDialog(StartWindow.this, str);
			}
		});
		
		btnLogOut.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//prekini sve igre koje igra klijent
				//prekini vezu sa serverom
				try {
					userList.remove(myInfo);
					userDb.refreshOnline();
					Registry reg = RegistryManager.get(dbHost);;
					try {
						reg.unbind(myInfo.getName());
					} catch (NotBoundException e1) {
						// user nije online
						e1.printStackTrace();
					}
					start();
					setSize(new Dimension(300, 300));
					removeWindowListener(windowClosing);
				} catch (RemoteException e) {
					reportError("Error while logging out", false, e);
				}
			}
		});
		
		addWindowListener(windowClosing);

		scrol.setPreferredSize(new Dimension(100, Integer.MAX_VALUE));
		
		image.setAlignmentX(CENTER_ALIGNMENT);
		lblName.setAlignmentX(CENTER_ALIGNMENT);
		lblOnline.setAlignmentX(CENTER_ALIGNMENT);
		btnLogOut.setAlignmentX(CENTER_ALIGNMENT);
		lblWins.setAlignmentX(CENTER_ALIGNMENT);
		lblLosses.setAlignmentX(CENTER_ALIGNMENT);
		btngetBest.setAlignmentX(CENTER_ALIGNMENT);
		btngetWorst.setAlignmentX(CENTER_ALIGNMENT);
		
		JPanel leftComponent=new JPanel();
		BoxLayout boxLeft=new BoxLayout(leftComponent, BoxLayout.PAGE_AXIS);
		leftComponent.setLayout(boxLeft);
		leftComponent.add(Box.createVerticalStrut(10));
		
		JPanel panelForButton=new JPanel();
		BoxLayout box1=new BoxLayout(panelForButton, BoxLayout.LINE_AXIS);
		panelForButton.setLayout(box1);
		panelForButton.add(Box.createRigidArea(new Dimension(10,10)));
		panelForButton.add(btnLogOut);
		panelForButton.add(Box.createRigidArea(new Dimension(10,10)));
		
		leftComponent.add(panelForButton);
		leftComponent.add(Box.createVerticalStrut(10));
		leftComponent.add(image);
		leftComponent.add(Box.createVerticalStrut(5));
		leftComponent.add(lblName);
		leftComponent.add(Box.createVerticalStrut(10));
		leftComponent.add(lblWins);
		lblWins.setAlignmentX(CENTER_ALIGNMENT);
		leftComponent.add(lblLosses);
		leftComponent.add(Box.createVerticalStrut(10));
		JPanel panel=new JPanel();
		BoxLayout box2=new BoxLayout(panel, BoxLayout.LINE_AXIS);
		panel.setLayout(box2);
		panel.add(btngetBest);
		panel.add(Box.createHorizontalStrut(5));
		panel.add(btngetWorst);
		leftComponent.add(panel);
		
		JPanel rightComponent=new JPanel();
		BoxLayout boxRight=new BoxLayout(rightComponent, BoxLayout.PAGE_AXIS);
		rightComponent.setLayout(boxRight);
		rightComponent.add(Box.createVerticalStrut(5));
		rightComponent.add(lblOnline);
		rightComponent.add(Box.createVerticalStrut(10));
		rightComponent.add(scrol);
		rightComponent.add(Box.createVerticalStrut(5));
		
		BoxLayout box=new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS);
		getContentPane().setLayout(box);
		getContentPane().add(leftComponent);
		getContentPane().add(rightComponent);

		revalidate();
		repaint();
	}
	
	public void setWinsOnScreen(int wins){
		lblWins.setText("Wins: " + wins);
	}
	
	public void setLossesOnScreen(int losses){
		lblLosses.setText("Losses: " + losses);
	}
	
	private void reportError(String msg, boolean exit, Throwable th)
	{
		logger.log(exit ? Level.SEVERE : Level.WARNING, msg, th);
		if (exit)
			msg += "\nThe program will now exit.";
		JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
		if (exit)
			System.exit(-1);
	}

	public static void main(String[] args) {
		
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception e){}
		
		String dbHost = JOptionPane.showInputDialog(null, "Host of the RMI registry holding the User DB?");
		if (dbHost == null || dbHost.length() == 0)
			return;
		
		JFrame frame=new StartWindow(dbHost);	
		frame.setSize(300,300);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}


}

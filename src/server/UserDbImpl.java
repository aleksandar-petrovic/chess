//Aleksandar Petrovic 

package server;
import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import registry.RegistryManager;
import user.UserI;
import user.UserInfo;

public class UserDbImpl extends UnicastRemoteObject implements UserDbI
{
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(UserDbImpl.class.getName());
	private List<UserInfo> users;
	private List<UserInfo> accounts;
	private int totalNumberOfGames = 0;
	
	//kreira server sa praznom listom online klijenata i ucitava iz fajla sve registrovane korisnike
	protected UserDbImpl() throws RemoteException
	{
		users = new ArrayList<>();
		accounts = new ArrayList<>();
		try(BufferedReader in=new BufferedReader(new FileReader("accounts.txt"))){
			int n=Integer.parseInt(in.readLine());
			for(int i=0;i<n;i++){
				String[] niz=in.readLine().split(" ");
				accounts.add(new UserInfo(niz[0], niz[1], niz[2], Integer.parseInt(niz[3]), Integer.parseInt(niz[4]), null));
			}
		} catch(IOException e){
			try {
				PrintWriter out=new PrintWriter("accounts.txt");
				out.close();
			} catch (FileNotFoundException e1) {
				System.out.println("Can not create file");
			}
		}
	}
	
	
	//dodaje online korisnika
	@Override
	public void add(UserInfo user) throws RemoteException
	{
		users.add(user);
	}
	
	//odjavljuje korisnika
	@Override
	public void remove(UserInfo user) throws RemoteException
	{
		users.remove(user);
	}

	//vraca listu online korisnika
	@Override
	public List<UserInfo> get() throws RemoteException
	{
		return users;
	}

	//vraca listu registrovanih korisnika
	@Override
	public List<UserInfo> getAccounts() throws RemoteException {
		return accounts;
	}

	//registruje korisnika
	@Override
	public void addAccount(UserInfo user) throws RemoteException {
		accounts.add(user);
		saveToFile();
	}
	
	public void saveToFile(){
		try {
			PrintWriter out = new PrintWriter("accounts.txt");
			out.println(accounts.size());
			for(UserInfo u: accounts){
				out.println(u.getName() + " " + u.getPass() + " " + u.getAvatar() + " " + u.getWins() + " " + u.getLosses());
			}
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	//poziva kod svakog korisnika metod koji refresuje listu online korisnika, 
	//poziva se kada se neki korisnik izloguje
	@Override
	public void refreshOnline() throws RemoteException {
		EventQueue.invokeLater(new Runnable(){

			@Override
			public void run() {
				for(UserInfo u: users){
					Registry reg;
					try {
						reg = RegistryManager.get(u.getHost());
						((UserI) reg.lookup(u.getName())).reloadOnlineUsers();
					} catch (RemoteException e) {
						System.out.println(e.getMessage());
					} catch (NotBoundException e) {
						System.out.println(e.getMessage());
					}
				}
			}
			
		});
	}
	
	private synchronized int increaseNumberOfGames() {
		return ++this.totalNumberOfGames;
	}

	@Override
	public void requestGame(final UserInfo user1, final UserInfo user2, final String dbHost) throws RemoteException {
		final int numberOfGame = this.increaseNumberOfGames();
		
		new Thread() {
			@Override
			public void run() {
				final Registry reg;
				try {
					reg = RegistryManager.get(user1.getHost());
					boolean acceptedGame = ((UserI) reg.lookup(user2.getName())).requestGame(user1);
					if ( acceptedGame ) {
						makeThreadStartGame( reg, user1, user2, true, numberOfGame, dbHost );
						makeThreadStartGame( reg, user2, user1, false, numberOfGame, dbHost );
					}
				}
				catch (RemoteException e1) {
					e1.printStackTrace();
				} catch (NotBoundException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	private void makeThreadStartGame( final Registry reg, final UserInfo user1, final UserInfo user2, final boolean mainPlayer, final int numberOfGame, final String dbHost ) {
		new Thread() {
			@Override
			public void run() {
				try {
					((UserI) reg.lookup(user1.getName())).startGame(user1, user2, mainPlayer, numberOfGame, dbHost);
				} catch (RemoteException e) {
					e.printStackTrace();
				} catch (NotBoundException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	// vraca lokalnu ip adresu racunara
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
	
	public static void main(String[] args)
	{
		try
		{
			String host = getIp();
			Registry reg;
			if(host==null)
				reg = RegistryManager.get();
			else
				reg = RegistryManager.get(host);
			reg.rebind(UserDbImpl.RMI_NAME, new UserDbImpl());
			if(host!=null)
				logger.info("Server created on " + host);
			logger.info("User DB ready...");
		} catch (RemoteException ex)
		{
			logger.log(Level.SEVERE, "Unable to start User DB.", ex);
		} 
	}



	@Override
	public int getWins(UserInfo user) throws RemoteException {
		return users.get(users.indexOf(user)).getWins();
	}



	@Override
	public int getLosses(UserInfo user) throws RemoteException {
		return users.get(users.indexOf(user)).getLosses();
	}

	@Override
	public void addWin(UserInfo user) throws RemoteException {
		users.get(users.indexOf(user)).addWin();
		accounts.get(accounts.indexOf(user)).addWin();
		Registry reg = RegistryManager.get(user.getHost());
		try {
			((UserI) reg.lookup(user.getName())).addWin();
			((UserI) reg.lookup(user.getName())).setWinsOnScren(user.getWins() + 1);
		} catch (NotBoundException e) {
			System.out.println("User is offline");
		}
		saveToFile();
	}
	
	
	@Override
	public void addLoss(UserInfo user) throws RemoteException {
		users.get(users.indexOf(user)).addLoss();
		accounts.get(accounts.indexOf(user)).addLoss();
		Registry reg = RegistryManager.get(user.getHost());
		try {
			((UserI) reg.lookup(user.getName())).addLoss();
			((UserI) reg.lookup(user.getName())).setLossesOnScren(user.getLosses() + 1);
		} catch (NotBoundException e) {
			System.out.println("User is offline");
		}
		saveToFile();
	}
	
}

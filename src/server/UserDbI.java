//Aleksandar Petrovic

package server;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import user.UserInfo;

public interface UserDbI extends Remote {
	
	public static final String RMI_NAME = "UserDb";
	
	void add(UserInfo user) throws RemoteException;
	
	void remove(UserInfo user) throws RemoteException;
	
	List<UserInfo> getAccounts() throws RemoteException;
	
	List<UserInfo> get() throws RemoteException;
	
	public void addAccount(UserInfo user) throws RemoteException;
	
	public void refreshOnline() throws RemoteException;
	
	public void requestGame(UserInfo user1, UserInfo user2, String dbHost) throws RemoteException;
	
	public int getWins(UserInfo user) throws RemoteException;
	
	public int getLosses(UserInfo user) throws RemoteException;
	
	public void addWin(UserInfo user) throws RemoteException;
	
	public void addLoss(UserInfo user) throws RemoteException;
}

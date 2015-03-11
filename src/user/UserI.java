//Aleksandar Petrovic

package user;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface UserI extends Remote
{
	public void setWinsOnScren(int wins) throws RemoteException;
	
	public void setLossesOnScren(int losses) throws RemoteException;
	
	public void addWin() throws RemoteException;
	
	public void addLoss() throws RemoteException;
	
	public void reloadOnlineUsers() throws RemoteException;
	
	public boolean requestGame(UserInfo user) throws RemoteException;
	
	public void startGame(UserInfo user1, UserInfo user2, boolean mainPlayer, int numberOfGame, String dbHost) throws RemoteException;
}

//Aleksandar Petrovic

package user;

import java.awt.EventQueue;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import javax.swing.JOptionPane;

import chess.ChessGame;
import onstart.StartWindow;

//Aleksandar Petrovic
public class UserImpl extends UnicastRemoteObject implements UserI
{
	private static final long serialVersionUID = 1L;
	private transient StartWindow wnd;

	//konstruktor dodeljuje prozor klijentu
	public UserImpl(StartWindow wnd) throws RemoteException
	{
		this.wnd = wnd;
	}
	
	//refresuje listu online klijenata
	@Override
	public void reloadOnlineUsers() throws RemoteException {
		EventQueue.invokeLater(new Runnable(){
			@Override
			public void run() {
				wnd.reloadOnlineUsers();
			}	
		});
		
	}

	
	@Override
	public boolean requestGame(UserInfo user) throws RemoteException {
		int acceptedGame = JOptionPane.showConfirmDialog(wnd, "Do you want to play agains " + user.getName(), "Play?", JOptionPane.YES_NO_OPTION);
		return acceptedGame == 0;
	}

	@Override
	public void startGame(UserInfo user1, UserInfo user2, boolean mainPlayer, int numberOfGame, String dbHost) {
		try {
			new Thread(
				new ChessGame(user1, user2, mainPlayer, numberOfGame, dbHost)
			).start();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//refresuje broj pobeda u labeli u prozoru
	@Override
	public void setWinsOnScren(int wins) throws RemoteException {
		wnd.setWinsOnScreen(wins);
	}

	//refresuje broj gubitaka u labeli u prozoru
	@Override
	public void setLossesOnScren(int losses) throws RemoteException {
		wnd.setLossesOnScreen(losses);
	}
	
	@Override
	public void addWin() throws RemoteException {
		wnd.addWin();
	}

	@Override
	public void addLoss() throws RemoteException {
		wnd.addLoss();
	}

}

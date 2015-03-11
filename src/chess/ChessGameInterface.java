/*
 * Author: Demjan Grubic
 * interface for ChessGame class which declares methods that should be called remotely
 */

package chess;

import java.rmi.Remote;
import java.rmi.RemoteException;

interface ChessGameInterface extends Remote {
	public void setColor(boolean white) throws RemoteException;
	public void makeMove(int x1, int y1, int x2, int y2) throws RemoteException;
}

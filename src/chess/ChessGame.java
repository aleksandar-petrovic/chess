/*
 * Author: Demjan Grubic
 * Main class for game communication logic
 */

package chess;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import chess.ChessPackage.Game;
import chess.ChessPackage.Position;
import registry.RegistryManager;
import user.UserInfo;
import server.UserDbI;

public class ChessGame extends UnicastRemoteObject implements Runnable, ChessGameInterface {
	private static final long serialVersionUID = 1L;
	
	private UserInfo user;
	private UserInfo opponentUser;
	private GameState gameState;
	private Chessboard chessboard;
	private boolean mainPlayer;
	
	private String chessboardName;
	private String opponentNameChessboard;
	
	private Registry registry;
	
	private JFrame window;
	
	private String dbHost;
	private Boolean win = null;

	public ChessGame(UserInfo user, UserInfo opponentInfo, boolean mainPlayer, int numberOfGame, String dbHost) throws RemoteException {
		this.window = new JFrame();
		
		this.window.setVisible(true);
		this.window.setSize(500, 500);
		this.window.setMinimumSize(new Dimension(500, 500));
		this.window.setTitle("Playing against " + opponentInfo.getName());
		this.window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				ChessGame.this.win = false;
				ChessGame.this.gameState = GameState.GameOver;
			}
		});
		
		this.dbHost = dbHost; 
		this.registry = RegistryManager.get(dbHost);
		
		this.opponentUser = opponentInfo;
		this.user = user;
		
		this.chessboard = new Chessboard(this, this.window);
		this.mainPlayer = mainPlayer;
		this.gameState = GameState.EstablishingConnection;
		this.chessboardName = user.getName() + "_" + opponentUser.getName() + "_" + numberOfGame;
		this.opponentNameChessboard = opponentUser.getName() + "_" + user.getName() + "_" + numberOfGame;
		
		this.win = null;
		
		this.RegisterBoard();
	}
	
	public GameState getGameState() {
		return this.gameState;
	}
	
	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}
	
	private void WaitForOpponentToConnect() {
		try {
			this.registry.lookup(this.opponentNameChessboard);
			this.gameState = GameState.Start;
		} catch (RemoteException | NotBoundException e) {
			// waiting for opponent to connect
		}
	}
	
	private void UnregisterBoard() {
		try {
			this.registry.unbind(this.chessboardName);
		} catch (RemoteException e) {
			// something went wrong with connection
			e.printStackTrace();
		} catch (NotBoundException e) {
			// chessboard is already unregistered
		}
	}
	
	private void RegisterBoard() {
		try {
			this.registry.bind(this.chessboardName, this);
		} catch (RemoteException e) {
			// something went wrong with connection
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			// this is not going to happen since chessboardName is unique for each game
			// but log error if something happens wrong
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		
		// this is thread so if user closes client window this thread should be killed
		while (!Thread.interrupted()) {
			
			// if this user goes offline (logout, lost connection), win goes to their opponent
			if ( !Online() ) {
				this.win = false;
				this.gameState = GameState.GameOver;
			}
			
			// opponent lost connection or closed game window
			if (this.gameState != GameState.EstablishingConnection && !this.opponentOnline()) {
				this.win = true;
				this.gameState = GameState.GameOver;
			}
			
			if (this.gameState == GameState.WaitingForOpponentMove || this.gameState == GameState.ChoosingFigure) {
				if (Game.whiteWinner(this.chessboard.getWhiteFigures(), this.chessboard.getBlackFigures())) {
					this.win = this.chessboard.isWhitePlayer();
					this.gameState = GameState.GameOver;
				}
				
				if (Game.blackWinner(this.chessboard.getWhiteFigures(), this.chessboard.getBlackFigures())) {
					this.win = !this.chessboard.isWhitePlayer();
					this.gameState = GameState.GameOver;
				}
				
				boolean whitePlaysNow = false;
				if ( (this.gameState == GameState.ChoosingFigure && this.chessboard.isWhitePlayer()) ||
					 (this.gameState == GameState.WaitingForOpponentMove && !this.chessboard.isWhitePlayer())
					 ) {
					whitePlaysNow = true;
				}
				
				if (Game.draw(this.chessboard.getBlackFigures(), this.chessboard.getBlackFigures(), whitePlaysNow)) {
					this.gameState = GameState.GameOver;
				}
			}
			
			switch (this.gameState) {
				// EstablishingConnection => Start
				case EstablishingConnection:
					this.WaitForOpponentToConnect();
					break;
			
				// Start => ChoosingColor OR WaitingForOpponentToChooseColor
				case Start:
					this.Start();
					break;
				
				// ChoosingColor => Initialization
				case ChoosingColor:
					break;
				case WaitingForOpponentToChooseColor:
					break;
					
				// Initialization => ChoosingFigure OR WaitingForOpponentMove
				case Initialization:
					this.Initialization();
					break;
					
				// ChoosingFigure => ChoosingFigure OR ChoosingPotentialCell (based on listener)
				case ChoosingFigure:
					break;
					
				// ChoosingPotentialCell => ChoosingPotentialCell OR ChoosingFigure (based on listener)
				case ChoosingPotentialCell:
					break;
					
				// MoveFigure => WaitingForOpponentMove
				case MoveFigure:
					break;
				case WaitingForOpponentMove:
					break;
					
				// GameOver => unbind board, update score, destroy JFrame and kill thread
				case GameOver:
					this.GameOver();
					return;
			}
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// something went wrong
				e.printStackTrace();
			}
		}
		
		this.GameOver();
	}
	
	private boolean Online() {
		try {
			this.registry.lookup(this.user.getName());
		}
		catch (NotBoundException e) {
			// user is offline
			return false;
		}
		catch (RemoteException e) {
			// something went wrong with connection
			e.printStackTrace();
		}
		
		return true;
	}

	private void GameOver() {
		// update for this user, opponent user will be updated in it's thread
		// is it tie?
		if ( this.win != null ) {
			try {
				if ( this.win ) {
					((UserDbI)RegistryManager.get(this.dbHost).lookup(UserDbI.RMI_NAME)).addWin(user);
				}
				else {
					((UserDbI)RegistryManager.get(this.dbHost).lookup(UserDbI.RMI_NAME)).addLoss(user);
				}
			} catch (RemoteException | NotBoundException e) {
				// server down or connection problem
				e.printStackTrace();
			}
		}
		
		String message = "It's draw!";
		if ( this.win != null ) {
			if ( this.win ) {
				message = "You won!";
			}
			else {
				message = "You lost!";
			}
		}
		
		JOptionPane.showMessageDialog(window, message);
		this.UnregisterBoard();
		this.window.dispose();
	}
	
	private boolean opponentOnline() {
		try {
			this.registry.lookup(this.opponentNameChessboard);
			this.registry.lookup(this.opponentUser.getName());
		} catch (RemoteException e) {
			// user is offline or didn't register board
		} catch (NotBoundException e) {
			// opponent exited game
			return false;
		}

		return true;
	}

	private void Initialization() {
		this.chessboard.MakeStartPosition();
		if ( !this.chessboard.isWhitePlayer() ) {
			this.chessboard.getStatusBar().setText("Waiting for opponent to make move");
			this.gameState = GameState.WaitingForOpponentMove;
		}
		else {
			this.chessboard.getStatusBar().setText("It's your turn");
			this.gameState = GameState.ChoosingFigure;
		}
	}

	private void ChoosingColor(Boolean whitePlayer) {
		this.chessboard.setPlayerColor(whitePlayer);
		this.gameState = GameState.Initialization;
		
		try {
			((ChessGameInterface)this.registry.lookup(this.opponentNameChessboard)).setColor(!this.chessboard.isWhitePlayer());
		} catch (RemoteException | NotBoundException e) {
			// connection problem (will be gameOver in next iteration if opponent is offline)
			e.printStackTrace();
		}
	}

	private void Start() {
		if ( this.mainPlayer ) {
			this.ChooseColor(this.window.getContentPane());
			this.gameState = GameState.ChoosingColor;
		}
		else {
			this.WaitForOpponentToChoseColor(this.window.getContentPane());
			this.gameState = GameState.WaitingForOpponentToChooseColor;
		}
	}

	private void WaitForOpponentToChoseColor(Container contentPane) {
		contentPane.removeAll();
		
		JLabel label = new JLabel("Waiting for opponent to choose color");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setVerticalAlignment(SwingConstants.CENTER);
		
		contentPane.add(label);
		
		contentPane.validate();
		contentPane.repaint();
	}

	private void ChooseColor(Container contentPane) {
		contentPane.removeAll();
		
		JPanel panel = new JPanel();
		contentPane.add(panel);
		
		JLabel label = new JLabel("Please choose color");
		
		JButton white = new JButton("White");
		JButton black = new JButton("Black");
		
		panel.add(label);
		panel.add(white);
		panel.add(black);
		
		white.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ChoosingColor(true);
			}
		});
		
		black.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ChoosingColor(false);
			}
		});
		
		contentPane.validate();
		contentPane.repaint();
	}

	@Override
	public void setColor(boolean white) throws RemoteException {
		this.chessboard.setPlayerColor(white);
		this.gameState = GameState.Initialization;
	}

	public void sendMove(Position previousPosition, Position position) {
		try {
			((ChessGameInterface)this.registry.lookup(this.opponentNameChessboard)).makeMove(previousPosition.getX(),previousPosition.getY(), position.getX(), position.getY());
		} catch (RemoteException | NotBoundException e) {
			// connection problem (will be gameOver in next iteration if opponent is offline)
			e.printStackTrace();
		}
	}
	
	public void makeMove(int x1, int y1, int x2, int y2) throws RemoteException {
		chessboard.makeMove(new Position(x1,y1), new Position(x2, y2));
		gameState = GameState.ChoosingFigure;
		this.chessboard.getStatusBar().setText("It's your turn");
	}
	
}

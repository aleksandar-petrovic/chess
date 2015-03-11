/*
 * Author: Demjan Grubic
 * Main class for game itself
 */

package chess;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import chess.ChessPackage.Bishop;
import chess.ChessPackage.Figure;
import chess.ChessPackage.FigureColor;
import chess.ChessPackage.King;
import chess.ChessPackage.Knight;
import chess.ChessPackage.Pawn;
import chess.ChessPackage.Position;
import chess.ChessPackage.Queen;
import chess.ChessPackage.Rook;
import chess.ChessPackage.AbstractFigure;
import chess.ChessPackage.AbstractLabel;
import chess.ChessPackage.DarkLabel;
import chess.ChessPackage.LightLabel;

public class Chessboard {
	private static final int Dimension = 8;
	
	private Boolean whitePlayer = null;
	
	private List<AbstractFigure> myFigures;
	private List<AbstractFigure> opponentFigures;
	private List<Position> potentialMoves;
	
	private List<AbstractFigure> whiteFigures;
	private List<AbstractFigure> blackFigures;
	
	Position previousPosition;
	
	private ChessGame chessGame;
	private Container contentPane;
	private JPanel mainPanel;
	private JLabel statusBar;
	
	AbstractLabel[][] cell;
	
	public Chessboard( ChessGame chessGame, JFrame window ) {
		this.chessGame = chessGame;
		this.contentPane = window.getContentPane();
		
		this.myFigures = new ArrayList<AbstractFigure>();
		this.opponentFigures = new ArrayList<AbstractFigure>();
		this.potentialMoves = new ArrayList<Position>();
		
		this.mainPanel = new JPanel();
		this.statusBar = new JLabel();
		
		this.cell = new AbstractLabel[Chessboard.Dimension][];
		for (int i = 0; i < Chessboard.Dimension; ++i) {
			this.cell[i] = new AbstractLabel[Chessboard.Dimension];
			for (int j = 0; j < Chessboard.Dimension; ++j) {
				if ( (i + j) % 2 == 0 ) {
					this.cell[i][j] = new DarkLabel( j, Chessboard.Dimension - 1 - i );
				}
				else {
					this.cell[i][j] = new LightLabel( j, Chessboard.Dimension - 1 - i );
				}
				this.cell[i][j].setHorizontalAlignment(SwingConstants.CENTER);
				this.cell[i][j].setVerticalAlignment(SwingConstants.CENTER);
				
				this.cell[i][j].addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						Position position = ((AbstractLabel)e.getSource()).getPosition();
						AbstractFigure clickedFigure = Chessboard.this.FindFigureAtPosition( position );
						
						boolean occupiedPosition = clickedFigure != null;
						boolean potentialMove = Chessboard.this.PotentialMove(position);
						
						switch (Chessboard.this.chessGame.getGameState()) {
							case ChoosingFigure:
								if ( occupiedPosition && Chessboard.this.myFigures.contains(clickedFigure)) {
									Chessboard.this.previousPosition = position;
									Chessboard.this.potentialMoves = clickedFigure.possibleMoves(Chessboard.this.whiteFigures, Chessboard.this.blackFigures);;
									Chessboard.this.DrawTable();
									Chessboard.this.chessGame.setGameState( GameState.ChoosingPotentialCell );
								}
								break;
							case ChoosingPotentialCell:
								if ( occupiedPosition ) {
									AbstractFigure previousFigure = Chessboard.this.FindFigureAtPosition(Chessboard.this.previousPosition);
									if ( clickedFigure.getColor() == previousFigure.getColor() ) {
										Chessboard.this.previousPosition = position;
										Chessboard.this.potentialMoves = clickedFigure.possibleMoves(Chessboard.this.whiteFigures, Chessboard.this.blackFigures);
										Chessboard.this.DrawTable();
										Chessboard.this.chessGame.setGameState( GameState.ChoosingPotentialCell );
									}
									else if ( potentialMove ) {
										Chessboard.this.opponentFigures.remove(clickedFigure);
										Chessboard.this.potentialMoves.clear();
										previousFigure.setPosition(position.getX(), position.getY());
										Chessboard.this.DrawTable();
										
										Chessboard.this.chessGame.sendMove( previousPosition, position );
										Chessboard.this.chessGame.setGameState( GameState.WaitingForOpponentMove );
										Chessboard.this.statusBar.setText("Waiting for opponent to make move");
									}
								}
								else if ( potentialMove ) {
									AbstractFigure previousFigure = Chessboard.this.FindFigureAtPosition(Chessboard.this.previousPosition);
									Chessboard.this.potentialMoves.clear();
									previousFigure.setPosition(position.getX(), position.getY());
									Chessboard.this.DrawTable();
									
									Chessboard.this.chessGame.sendMove( previousPosition, position );
									Chessboard.this.chessGame.setGameState( GameState.WaitingForOpponentMove );
									Chessboard.this.statusBar.setText("Waiting for opponent to make move");
								}
								break;
							default:
								// do nothing
								break;
						}
					}
				});
				
				mainPanel.add(this.cell[i][j]);
			}
		}
	}
	
	public JLabel getStatusBar() {
		return this.statusBar;
	}
	
	public List<AbstractFigure> getWhiteFigures() {
		return this.whiteFigures;
	}
	
	public List<AbstractFigure> getBlackFigures() {
		return this.blackFigures;
	}
	
	private void CopyLabelProperties( AbstractLabel source, AbstractLabel destination ) {
		destination.setIcon(source.getIcon());
		destination.setBorder(source.getBorder());
	}
	
	private AbstractFigure FindFigureAtPosition( Position position ) {
		for (AbstractFigure figure : this.myFigures) {
			if ( position.equals(figure.getPosition()) ) {
				return figure;
			}
		}
		
		for (AbstractFigure figure : this.opponentFigures) {
			if ( position.equals(figure.getPosition()) ) {
				return figure;
			}
		}
		
		return null;
	}
	
	private boolean PotentialMove(Position position) {
		for (Position pos : potentialMoves) {
			if ( pos.equals(position) ) {
				return true;
			}
		}
		
		return false;
	}
	
	public Boolean isWhitePlayer() {
		return this.whitePlayer;
	}
	
	public void setPlayerColor(Boolean whitePlayer) {
		this.whitePlayer = whitePlayer;
	}
	
	public void MakeStartPosition() {
		this.contentPane.removeAll();
		
		this.contentPane.add(mainPanel,BorderLayout.CENTER);
		this.contentPane.add(statusBar, BorderLayout.SOUTH);
		
		if ( this.whitePlayer ) {
			this.whiteFigures = this.myFigures;
			this.blackFigures = this.opponentFigures;
		}
		else {
			this.whiteFigures = this.opponentFigures;
			this.blackFigures = this.myFigures;
		}
		
		this.mainPanel.removeAll();
		GridLayout gridLayout = new GridLayout(Chessboard.Dimension, Chessboard.Dimension);
		this.mainPanel.setLayout(gridLayout);
		
		for (int row = 0; row < Chessboard.Dimension; ++row) {
			for (int column = 0; column < Chessboard.Dimension; ++column) {
				this.mainPanel.add(cell[row][column]);
			}
		}
		
		this.ArrangeFigures();
		this.previousPosition = null;
		
		this.DrawTable();
	}
	
	private void DrawTable() {
		for (int row = 0; row < Chessboard.Dimension; ++row) {
			for (int column = 0; column < Chessboard.Dimension; ++column) {
				AbstractLabel label = GUI.getCell( column, Chessboard.Dimension - 1 - row, this.whiteFigures, this.blackFigures, this.potentialMoves );
				this.CopyLabelProperties(label, cell[row][column]);
			}
		}
		
		contentPane.validate();
		contentPane.repaint();
	}

	private void ArrangeFigures() {
		for (FigureColor figColor : FigureColor.values()) {
			List<AbstractFigure> figures;
			int i;
			
			if(figColor == FigureColor.white) {
				i = 0;
				if ( this.whitePlayer ) {
					figures = myFigures;
				}
				else {
					figures = opponentFigures;
				}
			}
			else {
				i = Chessboard.Dimension - 1;
				if ( !this.whitePlayer ) {
					figures = myFigures;
				}
				else {
					figures = opponentFigures;
				}
			}
			
			figures.add(setFigure(0, i, Figure.rook, figColor));
			figures.add(setFigure(Chessboard.Dimension - 1, i, Figure.rook, figColor));
			
			figures.add(setFigure(1, i, Figure.knight, figColor));
			figures.add(setFigure(Chessboard.Dimension - 2, i,Figure.knight, figColor));
			
			figures.add(setFigure(2, i, Figure.bishop, figColor));
			figures.add(setFigure(Chessboard.Dimension - 3, i, Figure.bishop, figColor));
			
			figures.add(setFigure(3, i, Figure.queen, figColor));
			
			figures.add(setFigure(4, i, Figure.king, figColor));
			
			if(figColor == FigureColor.white)
				i++;
			else
				i--;
			
			for(int j = 0; j < Chessboard.Dimension; j++) 
				figures.add(setFigure(j, i, Figure.pawn, figColor));
		}
	}
	
	public AbstractFigure setFigure(int x, int y, Figure fig, FigureColor figColor) {
		switch (fig) {
			case pawn:
				return new Pawn(x, y, figColor);
			case rook:
				return new Rook(x, y, figColor);
			case knight:
				return new Knight(x, y, figColor);
			case bishop:
				return new Bishop(x, y, figColor);
			case queen:
				return new Queen(x, y, figColor);
			case king:
				return new King(x, y, figColor);
		}
		return null;
	}
	
	public void makeMove(Position previousPosition, Position position) {
		for (AbstractFigure figure : this.myFigures) {
			if ( figure.getPosition().equals(position) ) {
				this.myFigures.remove(figure);
				break;
			}
		}
		
		for (AbstractFigure figure : this.opponentFigures) {
			if ( figure.getPosition().equals(previousPosition) ) {
				figure.setPosition(position.getX(), position.getY());
				break;
			}
		}
		
		this.DrawTable();
	}
}

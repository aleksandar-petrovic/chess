// Marko Kabic
package chess.ChessPackage;

import java.util.ArrayList;
import java.util.List;


public class Game {
	private Table table;
	
	public Game() {
		
	}
	
	public static boolean whiteWinner(List<AbstractFigure> whiteFigures, List<AbstractFigure> blackFigures) {
		AbstractFigure king = null;
		
		for(AbstractFigure fig : blackFigures) {
			if(fig instanceof King) {
				king = fig;
				break;
			}
		}

		boolean cantMove = false;
		
		if(king.possibleMoves(whiteFigures, blackFigures).isEmpty())
			cantMove = true;
		
		if(!cantMove)
			return false;
		else {
			boolean kingAttacked = false;
			for(AbstractFigure fig: whiteFigures) {
				if(fig.possibleMoves(whiteFigures, blackFigures).contains(king.getPosition())) {
					kingAttacked = true;
					break;
				}
					
			}
			
			if(!kingAttacked)
				return false;
			else {
				
				if(!king.possibleMoves(whiteFigures, blackFigures).isEmpty())
					return false;
				
				boolean moveFound = false;
				for(AbstractFigure fig : blackFigures) {
					if(fig == king)
						continue;
					List<Position> possibilities = fig.possibleMoves(whiteFigures, blackFigures);
					int x = fig.getPosition().getX();;
					int y = fig.getPosition().getY();
					
					
					if(!possibilities.isEmpty()) { 
						for(Position p : possibilities) {
							fig.makeMove(p.getX(), p.getY());
							
							kingAttacked = false;
							for(AbstractFigure fig2: whiteFigures) 
								if(fig2.possibleMoves(whiteFigures, blackFigures).contains(king.getPosition())) {
									kingAttacked = true;
									fig.makeMove(x, y);
									break;
								}
									
							if(!kingAttacked) {
								moveFound = true;
								fig.makeMove(x, y);
								break;
							}
							
							fig.makeMove(x, y);
						}
						
					}
				}
				
				return !moveFound;	
			}
		}
		
		
	}
	
	public static boolean blackWinner(List<AbstractFigure> whiteFigures, List<AbstractFigure> blackFigures) {
		AbstractFigure king = null;
		
		for(AbstractFigure fig : whiteFigures) {
			if(fig instanceof King) {
				king = fig;
				break;
			}
		}

		boolean cantMove = false;
		
		if(king.possibleMoves(whiteFigures, blackFigures).isEmpty())
			cantMove = true;
		
		if(!cantMove)
			return false;
		else {
			boolean kingAttacked = false;
			for(AbstractFigure fig: blackFigures) {
				if(fig.possibleMoves(whiteFigures, blackFigures).contains(king.getPosition())) {
					kingAttacked = true;
					break;
				}
					
			}
			
			if(!kingAttacked)
				return false;
			else {
				if(!king.possibleMoves(whiteFigures, blackFigures).isEmpty())
					return false;
				
				boolean moveFound = false;
				for(AbstractFigure fig : whiteFigures) {
					if(fig == king)
						continue;
					List<Position> possibilities = fig.possibleMoves(whiteFigures, blackFigures);
					int x = fig.getPosition().getX();;
					int y = fig.getPosition().getY();
					
					
					if(!possibilities.isEmpty()) { 
						for(Position p : possibilities) {
							fig.makeMove(p.getX(), p.getY());
							
							kingAttacked = false;
							for(AbstractFigure fig2: blackFigures) 
								if(fig2.possibleMoves(whiteFigures, blackFigures).contains(king.getPosition())) {
									kingAttacked = true;
									fig.makeMove(x, y);
									break;
								}
									
							if(!kingAttacked) {
								moveFound = true;
								fig.makeMove(x, y);
								break;
							}
							
							fig.makeMove(x, y);
						}
						
					}
				}
				
				return !moveFound;	
			}
		}
		
		
	}
	public static boolean draw(List<AbstractFigure> whiteFigures, List<AbstractFigure> blackFigures, boolean whitePlaysNow) {
		
		
		if(whitePlaysNow) {
			if(blackWinner(whiteFigures, blackFigures))
				return false;
			
			boolean moveFound = false;
			for(AbstractFigure fig : whiteFigures) {
				if(!fig.possibleMoves(whiteFigures, blackFigures).isEmpty()) {
					moveFound = true;
					break;
				}
			}
			
			if(moveFound) 
				return false;
			else {
				return (blackFigures.size() == 1) && (blackFigures.get(0) instanceof King)
						&& (whiteFigures.size() == 1) && (whiteFigures.get(0) instanceof King);
			}
				
		} else {
			if(whiteWinner(whiteFigures, blackFigures))
				return false;
			
			boolean moveFound = false;
			for(AbstractFigure fig : blackFigures) {
				if(!fig.possibleMoves(whiteFigures, blackFigures).isEmpty()) {
					moveFound = true;
					break;
				}
			}
			
			if(moveFound) 
				return false;
			else {
				return (blackFigures.size() == 1) && (blackFigures.get(0) instanceof King)
						&& (whiteFigures.size() == 1) && (whiteFigures.get(0) instanceof King);
			}
			
			
		}
	}
	
	public static boolean gameOver(List<AbstractFigure> whiteFigures, List<AbstractFigure> blackFigures) {
		AbstractFigure whiteKing = null;
		
		for(AbstractFigure fig : whiteFigures)
			if(fig instanceof King) {
				whiteKing = fig;
				break;
			}
		
		if(whiteKing.possibleMoves(whiteFigures, blackFigures).isEmpty())
			return true;
		
		AbstractFigure blackKing = null;
		
		for(AbstractFigure fig : blackFigures)
			if(fig instanceof King) {
				blackKing = fig;
				break;
			}
		
		if(blackKing.possibleMoves(whiteFigures, blackFigures).isEmpty())
			return true;
		
		return false;
	}
	
}


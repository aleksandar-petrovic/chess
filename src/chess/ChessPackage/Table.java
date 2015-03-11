// Marko Kabic
package chess.ChessPackage;

import java.util.ArrayList;
import java.util.List;

public class Table {
	private AbstractFigure[][] table;
	
	public static int DIMENSION = 8;
	
	public List<AbstractFigure> getWhiteFigures() {
		List<AbstractFigure> whiteFigures = new ArrayList<AbstractFigure>();
		
		for(int i = 0; i < DIMENSION; i++)
			for(int j = 0; j < DIMENSION; j++)
				if(getFigureAt(i, j) != null && getFigureAt(i, j).getColor() == FigureColor.white)
					whiteFigures.add(getFigureAt(i, j));
		
		return whiteFigures;
					
	}
	public List<AbstractFigure> getBlackFigures() {
		List<AbstractFigure> whiteFigures = new ArrayList<AbstractFigure>();
		
		for(int i = 0; i < DIMENSION; i++)
			for(int j = 0; j < DIMENSION; j++)
				if(getFigureAt(i, j) != null && getFigureAt(i, j).getColor() == FigureColor.black)
					whiteFigures.add(getFigureAt(i, j));
		
		return whiteFigures;
					
	}
	
	
	public Table (List<AbstractFigure> whiteFigures, List<AbstractFigure> blackFigures) {
		table = new AbstractFigure[DIMENSION][DIMENSION];
		
		for(AbstractFigure fig : whiteFigures)
			setFigure(fig.getPosition().getX(), fig.getPosition().getY(), fig);
		
		for(AbstractFigure fig : blackFigures)
			setFigure(fig.getPosition().getX(), fig.getPosition().getY(), fig);
	}
	
	public boolean isWithinBounds(int x, int y) {
		return (x >= 0) && (x < Table.DIMENSION) && (y >= 0) && (y < Table.DIMENSION); 
	}

	
	public void setFigure(int x, int y, Figure fig, FigureColor figColor) {
		
		if(fig == null)
			table[x][y] = null;
		else {
		
			switch (fig) {
		
			case pawn:
				table[x][y] = new Pawn(x, y, figColor);
				break;
			case rook:
				table[x][y] = new Rook(x, y, figColor);
				break;
			case knight:
				table[x][y] = new Knight(x, y, figColor);
				break;
			case bishop:
				table[x][y] = new Bishop(x, y, figColor);
				break;
			case queen:
				table[x][y] = new Queen(x, y, figColor);
				break;
			case king:
				table[x][y] = new King(x, y, figColor);
				break;
			}
		}
	}
	
	
	public void setFigure(int x, int y, AbstractFigure fig) {
		if(fig == null)
			table[x][y] = null;
		else {
			fig.setPosition(x, y);
			setFigure(x, y, fig.getFigure(), fig.getColor());
			
		}
	}
	
	public boolean isOccupiedByWhite(int x, int y) {
		if(table[x][y] == null) return false;
		
		return table[x][y].getColor() == FigureColor.white;
	}
	
	public boolean isOccupiedByBlack(int x, int y) {
		if(table[x][y] == null) return false;
		
		return table[x][y].getColor() == FigureColor.black;
	}
	
	public boolean isOccupied(int x, int y) {
		return table[x][y] != null;
	}
	
	public void move(int x, int y, int newX, int newY) {
		AbstractFigure fig = table[x][y];
		table[x][y] = null;
		fig.setPosition(newX, newY);
		
		if(fig.getColor() == FigureColor.white) {
			if((fig instanceof Pawn) && (newX - x == 1) && (newY - y == 1)
					&& (table[newX][newY] == null))
				table[x + 1][y] = null;
			else if ((fig instanceof Pawn) && (newX - x == -1) && (newY - y == 1)
					&& (table[newX][newY] == null))
				table[x - 1][y] = null;
		}
		else {
			if((fig instanceof Pawn) && (newX - x == -1) && (newY - y == -1)
					&& (table[newX][newY] == null))
				table[x - 1][y] = null;
			else if ((fig instanceof Pawn) && (newX - x == 1) && (newY - y == -1)
					&& (table[newX][newY] == null))
				table[x + 1][y] = null;
		}
		
		table[newX][newY] = fig;
		
	}
	
	public AbstractFigure getFigureAt(int x, int y) {
		return table[x][y];
	}
	
}
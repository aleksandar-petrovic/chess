// Marko Kabic
package chess.ChessPackage;


import java.util.ArrayList;

public class King extends AbstractFigure {
	public King(int x, int y, FigureColor figColor) {
		super(new Position(x, y), Figure.king, figColor);
	}
	
	public ArrayList<Position> getAvailablePositions() {
		Table table = getTable();
		ArrayList<Position> list = new ArrayList<Position>();
		int x = getPosition().getX();
		int y = getPosition().getY();
		FigureColor color = getColor();
		
		int i = x - 1;
		int j = y + 1;
		
		if(isWithinBounds(i, j) && 
				  (!table.isOccupied(i,  j) || ((color == FigureColor.white) ? table.isOccupiedByBlack(i, j) : table.isOccupiedByWhite(i,  j))))
					list.add(new Position(i, j));
		
		i = x;
		j = y + 1;
		
		if(isWithinBounds(i, j) && 
				  (!table.isOccupied(i,  j) || ((color == FigureColor.white) ? table.isOccupiedByBlack(i, j) : table.isOccupiedByWhite(i,  j))))
					list.add(new Position(i, j));
		
		i = x + 1;
		j = y + 1;
		
		if(isWithinBounds(i, j) && 
				  (!table.isOccupied(i,  j) || ((color == FigureColor.white) ? table.isOccupiedByBlack(i, j) : table.isOccupiedByWhite(i,  j))))
					list.add(new Position(i, j));
		
		i = x - 1;
		j = y;
		
		if(isWithinBounds(i, j) && 
				  (!table.isOccupied(i,  j) || ((color == FigureColor.white) ? table.isOccupiedByBlack(i, j) : table.isOccupiedByWhite(i,  j))))
					list.add(new Position(i, j));
		
		i = x + 1;
		j = y;
		
		if(isWithinBounds(i, j) && 
				  (!table.isOccupied(i,  j) || ((color == FigureColor.white) ? table.isOccupiedByBlack(i, j) : table.isOccupiedByWhite(i,  j))))
					list.add(new Position(i, j));
		
		i = x - 1;
		j = y - 1;
		
		if(isWithinBounds(i, j) && 
				  (!table.isOccupied(i,  j) || ((color == FigureColor.white) ? table.isOccupiedByBlack(i, j) : table.isOccupiedByWhite(i,  j))))
					list.add(new Position(i, j));
		
		i = x;
		j = y - 1;
		
		if(isWithinBounds(i, j) && 
				  (!table.isOccupied(i,  j) || ((color == FigureColor.white) ? table.isOccupiedByBlack(i, j) : table.isOccupiedByWhite(i,  j))))
					list.add(new Position(i, j));
		
		i = x + 1;
		j = y - 1;
		
		if(isWithinBounds(i, j) && 
				  (!table.isOccupied(i,  j) || ((color == FigureColor.white) ? table.isOccupiedByBlack(i, j) : table.isOccupiedByWhite(i,  j))))
					list.add(new Position(i, j));
		
		return list;
	}
}


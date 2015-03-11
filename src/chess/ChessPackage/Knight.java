// Marko Kabic
package chess.ChessPackage;
import java.util.ArrayList;

public class Knight extends AbstractFigure {
	public Knight(int x, int y, FigureColor figColor) {
		super(new Position(x, y), Figure.knight, figColor);
	}
	
	@Override
	public ArrayList<Position> getAvailablePositions() {
		Table table = getTable();
		ArrayList<Position> list = new ArrayList<Position>();
		int x = getPosition().getX();
		int y = getPosition().getY();
		FigureColor color = getColor();
		
		int i = x - 1;
		int j = y + 2;
		
		if(isWithinBounds(i, j) && 
		  (!table.isOccupied(i,  j) || ((color == FigureColor.white) ? table.isOccupiedByBlack(i, j) : table.isOccupiedByWhite(i,  j))))
			list.add(new Position(i, j));
		
		i = x + 2;
		j = y + 1;
		
		if(isWithinBounds(i, j) && 
				  (!table.isOccupied(i,  j) || ((color == FigureColor.white) ? table.isOccupiedByBlack(i, j) : table.isOccupiedByWhite(i,  j))))
					list.add(new Position(i, j));
		
		i = x + 1;
		j = y - 2;
		
		if(isWithinBounds(i, j) && 
				  (!table.isOccupied(i,  j) || ((color == FigureColor.white) ? table.isOccupiedByBlack(i, j) : table.isOccupiedByWhite(i,  j))))
					list.add(new Position(i, j));
		
		i = x - 2;
		j = y - 1;
		
		if(isWithinBounds(i, j) && 
				  (!table.isOccupied(i,  j) || ((color == FigureColor.white) ? table.isOccupiedByBlack(i, j) : table.isOccupiedByWhite(i,  j))))
					list.add(new Position(i, j));
		
		i = x + 1;
		j = y + 2;
		
		if(isWithinBounds(i, j) && 
				  (!table.isOccupied(i,  j) || ((color == FigureColor.white) ? table.isOccupiedByBlack(i, j) : table.isOccupiedByWhite(i,  j))))
					list.add(new Position(i, j));
		
		i = x - 2;
		j = y + 1;
		
		if(isWithinBounds(i, j) && 
				  (!table.isOccupied(i,  j) || ((color == FigureColor.white) ? table.isOccupiedByBlack(i, j) : table.isOccupiedByWhite(i,  j))))
					list.add(new Position(i, j));
		
		i = x - 1;
		j = y - 2;
		
		if(isWithinBounds(i, j) && 
				  (!table.isOccupied(i,  j) || ((color == FigureColor.white) ? table.isOccupiedByBlack(i, j) : table.isOccupiedByWhite(i,  j))))
					list.add(new Position(i, j));
		
		i = x + 2;
		j = y - 1;
		
		if(isWithinBounds(i, j) && 
				  (!table.isOccupied(i,  j) || ((color == FigureColor.white) ? table.isOccupiedByBlack(i, j) : table.isOccupiedByWhite(i,  j))))
					list.add(new Position(i, j));
		
		return list;
	}
}

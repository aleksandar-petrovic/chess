// Marko Kabic
package chess.ChessPackage;

import java.util.ArrayList;
public class Bishop extends AbstractFigure {
	public Bishop(int x, int y, FigureColor figColor) {
		super(new Position(x, y), Figure.bishop, figColor);
	}
	
	@Override
	public ArrayList<Position> getAvailablePositions() {
		Table table = getTable();
		ArrayList<Position> list = new ArrayList<Position>();
		int x = getPosition().getX();
		int y = getPosition().getY();
		FigureColor color = getColor();
		
		for(int i = x + 1, j = y + 1; (i < Table.DIMENSION) && (j < Table.DIMENSION); i++, j++) {
			if(!table.isOccupied(i,  j))
				list.add(new Position(i, j));
			else if((color == FigureColor.white) && (table.isOccupiedByBlack(i,  j))) {
				list.add(new Position(i, j));
				break;
			}
			else if((color == FigureColor.black) && (table.isOccupiedByWhite(i, j))) {
				list.add(new Position(i, j));
				break;
			}
			else
				break;
		}
		
		for(int i = x - 1, j = y - 1; (i >= 0) && (j >= 0); i--, j--) {
			if(!table.isOccupied(i,  j))
				list.add(new Position(i, j));
			else if((color == FigureColor.white) && (table.isOccupiedByBlack(i,  j))) {
				list.add(new Position(i, j));
				break;
			}
			else if((color == FigureColor.black) && (table.isOccupiedByWhite(i, j))) {
				list.add(new Position(i, j));
				break;
			}
			else
				break;
		}
		
		for(int i = x + 1, j = y - 1; (i < Table.DIMENSION) && (j >= 0); i++, j--) {
			if(!table.isOccupied(i,  j))
				list.add(new Position(i, j));
			else if((color == FigureColor.white) && (table.isOccupiedByBlack(i,  j))) {
				list.add(new Position(i, j));
				break;
			}
			else if((color == FigureColor.black) && (table.isOccupiedByWhite(i, j))) {
				list.add(new Position(i, j));
				break;
			}
			else
				break;
		}
		
		for(int i = x - 1, j = y + 1; (j < Table.DIMENSION) && (i >= 0); i--, j++) {
			if(!table.isOccupied(i,  j))
				list.add(new Position(i, j));
			else if((color == FigureColor.white) && (table.isOccupiedByBlack(i,  j))) {
				list.add(new Position(i, j));
				break;
			}
			else if((color == FigureColor.black) && (table.isOccupiedByWhite(i, j))) {
				list.add(new Position(i, j));
				break;
			}
			else
				break;
		}
		
	
		return list;
	}
	
}

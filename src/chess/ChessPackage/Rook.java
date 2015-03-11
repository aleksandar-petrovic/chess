// Marko Kabic
package chess.ChessPackage;

import java.util.ArrayList;

public class Rook extends AbstractFigure {
	public Rook(int x, int y, FigureColor figColor) {
		super(new Position(x, y), Figure.rook, figColor);
	}
	
	@Override
	public ArrayList<Position> getAvailablePositions() {
		Table table = getTable();
		ArrayList<Position> list = new ArrayList<Position>();
		int x = getPosition().getX();
		int y = getPosition().getY();
		FigureColor color = getColor();
		
		//up
		for(int i = y + 1; i < Table.DIMENSION; i++) {
			
			if(!table.isOccupied(x,  i))
				list.add(new Position(x, i));
			else if((color == FigureColor.white) && (table.isOccupiedByBlack(x,  i))) {
				list.add(new Position(x, i));
				break;
			}
			else if((color == FigureColor.black) && (table.isOccupiedByWhite(x, i))) {
				list.add(new Position(x, i));
				break;
			}
			else
				break;
		}
		
		//down		
		for(int i = y - 1; i >= 0; i--) {
			if(!table.isOccupied(x,  i))
				list.add(new Position(x, i));
			else if((color == FigureColor.white) && (table.isOccupiedByBlack(x,  i))) {
				list.add(new Position(x, i));
				break;
			}
			else if((color == FigureColor.black) && (table.isOccupiedByWhite(x, i))) {
				list.add(new Position(x, i));
				break;
			}
			else
				break;
		}
		
		//left
		for(int i = x - 1; i >= 0; i--) {
			if(!table.isOccupied(i, y))
				list.add(new Position(i, y));
			else if((color == FigureColor.white) && (table.isOccupiedByBlack(i,  y))) {
				list.add(new Position(i, y));
				break;
			}
			else if((color == FigureColor.black) && (table.isOccupiedByWhite(i, y))) {
				list.add(new Position(i, y));
				break;
			}
			else
				break;
		}
		
		//right
		for(int i = x + 1; i < Table.DIMENSION; i++) {
			if(!table.isOccupied(i, y))
				list.add(new Position(i, y));
			else if((color == FigureColor.white) && (table.isOccupiedByBlack(i,  y))) {
				list.add(new Position(i, y));
				break;
			}
			else if((color == FigureColor.black) && (table.isOccupiedByWhite(i, y))) {
				list.add(new Position(i, y));
				break;
			}
			else
				break;
		}
		
		return list;
	}
}


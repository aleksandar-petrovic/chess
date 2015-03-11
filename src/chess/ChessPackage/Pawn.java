// Marko Kabic
package chess.ChessPackage;
import java.util.ArrayList;

public class Pawn extends AbstractFigure {
	public Pawn(int x, int y, FigureColor figColor) {
		super(new Position(x, y), Figure.pawn, figColor);
		//pastPosition = new Position(x, y);
		//timeStep = 0;
	}
	
	Position pastPosition;
	private int timeStep;
	
	
	@Override
	public ArrayList<Position> getAvailablePositions() {
		Table table = getTable();
		ArrayList<Position> list = new ArrayList<Position>();
		int x = getPosition().getX();
		int y = getPosition().getY();
		FigureColor color = getColor();
		
		if(color == FigureColor.white) {
		
			if((y < Table.DIMENSION - 1) && !table.isOccupied(x, y + 1))
				list.add(new Position(x, y + 1));
		
			if((y == 1) && !table.isOccupied(x, y + 2) && !table.isOccupied(x, y + 1))
				list.add(new Position(x, y + 2));
			
			if((x > 0) && (y < Table.DIMENSION - 1) && table.isOccupiedByBlack(x - 1, y + 1))
				list.add(new Position(x - 1, y + 1));
			//else if((x > 0) && (y < Table.DIMENSION - 2)&& table.isOccupiedByBlack(x - 1, y) &&  (((Pawn)(table.getFigureAt(x - 1, y))).getPastPosition().equals(new Position(x - 1, y + 2))))
				//list.add(new Position(x - 1, y + 1));
			
			
			if((y < Table.DIMENSION - 1) && (x < Table.DIMENSION - 1) && table.isOccupiedByBlack(x + 1, y + 1))
				list.add(new Position(x + 1, y + 1));
			//else if((x < Table.DIMENSION - 1) && (y < Table.DIMENSION - 2) && table.isOccupiedByBlack(x + 1, y) &&  (((Pawn)(table.getFigureAt(x + 1, y))).getPastPosition().equals(new Position(x + 1, y + 2))))
				//list.add(new Position(x + 1, y + 1));
			
		}
		else {
			
			if((y >= 0) && !table.isOccupied(x, y - 1))
				list.add(new Position(x, y - 1));
		
			if((y == Table.DIMENSION - 2) && !table.isOccupied(x, y - 2) && !table.isOccupied(x, y - 1))
				list.add(new Position(x, y - 2));
			
			if((y > 0) && (x < Table.DIMENSION - 1) && table.isOccupiedByWhite(x + 1, y - 1))
				list.add(new Position(x + 1, y - 1));
			//else if((x < Table.DIMENSION - 1) && (x > 0) && (y >= 2) && table.isOccupiedByWhite(x + 1, y) && (((Pawn)(table.getFigureAt(x + 1, y))).getPastPosition().equals(new Position(x + 1, y - 2))))
			//	list.add(new Position(x + 1, y - 1));
			
			if((y > 0) && (x > 0) && table.isOccupiedByWhite(x - 1, y - 1))
				list.add(new Position(x - 1, y - 1));
			//else if((x > 0) && (y >= 2) && table.isOccupiedByWhite(x - 1, y) && (((Pawn)(table.getFigureAt(x - 1, y))).getPastPosition().equals(new Position(x - 1, y - 2))))
				//list.add(new Position(x - 1, y - 1));
			
		}
		
		return list;
	}
	/*
	public void increaseTimeStep() {
		timeStep++;
	}
	
	public int getTimeStep() {
		return timeStep;
	}
	
	public Position getPastPosition(){
		return pastPosition;
	}
	*/
}

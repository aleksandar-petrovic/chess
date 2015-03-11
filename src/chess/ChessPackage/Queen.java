// Marko Kabic
package chess.ChessPackage;

import java.util.ArrayList;
import java.util.List;

public class Queen extends AbstractFigure {
	public Queen(int x, int y, FigureColor figColor) {
		super(new Position(x, y), Figure.queen, figColor);
	}
	
	@Override
	public List<Position> getAvailablePositions() {
		List<Position> list = new ArrayList<Position>();
		int x = getPosition().getX();
		int y = getPosition().getY();
		FigureColor color = getColor();
		
		Bishop b = new Bishop(x, y, color);
		Rook r = new Rook(x, y, color);
		
		list = b.getAvailablePositions();
		
		list.addAll(r.getAvailablePositions());
		
		
		return list;
	}
	
	
}


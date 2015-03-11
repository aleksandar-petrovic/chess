// Marko Kabic
package chess.ChessPackage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

public abstract class AbstractFigure {
	private ImageIcon image;
	private Position pos;
	private Figure fig;
	private FigureColor figColor;
	private static Table table;
	public AbstractFigure(Position pos, Figure fig, FigureColor figColor) {
		this.pos = pos;
		this.figColor = figColor;
		this.fig = fig;
		if(fig != null) 
			setImage();
	}
	
	public void makeMove(int x, int y) {
		pos = new Position(x, y);
		//table.move(pos.getX(), pos.getY(), x, y);
	}
	
	public static void setTable(Table t) {
		table = t;
	}
	
	public Table getTable() {
		return table;
	}
	
	public void setImage() {
		image = new ImageIcon(getClass().getResource("images/" + figColor + "/" + fig + ".png"));
	}
	
	public ImageIcon getImage() {
		return image;
	}
	
	public abstract List<Position> getAvailablePositions();
	
	public boolean isLegalMove(int x, int y) {
		return getAvailablePositions().contains(new Position(x, y));
	}
	
	public List<Position> getAttackedPositions() {
		ArrayList<Position> list = new ArrayList<>();
		
		for(Position p : getAvailablePositions())
			if(table.isOccupied(p.getX(), p.getY()))
				list.add(p);
		
		return list;
				
	}
	
	public List<Position> clearList(List<Position> list) {
		List<AbstractFigure> whiteFigures = table.getWhiteFigures();
		List<AbstractFigure> blackFigures = table.getBlackFigures();
		
		List<Position> returnList = new ArrayList<Position>();
		
		if(chess(whiteFigures, blackFigures)) {
			int oldX = getPosition().getX();
			int oldY = getPosition().getY();
			
			for(Position position : list) {
				makeMove(position.getX(), position.getY());
				if(!chess(whiteFigures, blackFigures)) {
					returnList.add(position);
				}
				makeMove(oldX, oldY);
			}
		}
		else {
			returnList = list;
		}
		
		return returnList;
	}
	
	public boolean chess(List<AbstractFigure> whiteFigures, List<AbstractFigure> blackFigures) {
		List<AbstractFigure> myFigures;
		List<AbstractFigure> oppositeFigures;
		
		if(figColor == FigureColor.white) {
			myFigures = whiteFigures;
			oppositeFigures = blackFigures;
		} else {
			myFigures = blackFigures;
			oppositeFigures = whiteFigures;
		}
		
		AbstractFigure king = null;
		
		for(AbstractFigure fig : myFigures) {
			if(fig instanceof King){
				king = fig;
				break;
			}	
		}
		
		boolean kingAttacked = false;
		
		for(AbstractFigure fig : oppositeFigures) {
			table = new Table(whiteFigures, blackFigures);
			if(fig.getAvailablePositions().contains(king.getPosition())) {
				kingAttacked = true;
				break;
			}
				
		}
		
		return kingAttacked;
		
	}
	
	public List<Position> possibleMoves(List<AbstractFigure> whiteFigures, List<AbstractFigure> blackFigures) {
		table = new Table(whiteFigures, blackFigures);
		List<Position> list = getAvailablePositions();
		return clearList(list);
	}
	
	public Position getPosition() {
		return pos;
	}
	
	public FigureColor getColor() {
		return figColor;
	}
	
	public Figure getFigure() {
		return fig;
	}
	
	public void setPosition(int x, int y) {
		pos = new Position(x, y);
	}
	
	public static boolean isWithinBounds(int x, int y) {
		return (x >= 0) && (x < Table.DIMENSION) && (y >= 0) && (y < Table.DIMENSION);
	}
}
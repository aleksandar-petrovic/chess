// Marko Kabic
package chess.ChessPackage;
public class Position {

	private int x, y;
	//x and y denotes mathematical, curtesian coordinates with (0, 0) in the bottom left edge
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o)
			return true;
		if(o == null)
			return false;
		if(getClass() != o.getClass())
			return false;
		
		Position pos = (Position) o;
		if((x == pos.x) && (y == pos.y))
			return true;
		else
			return false;
		
	}
	
	@Override 
	public int hashCode() {
		int result = 1;
		int prime = 31;
		
		result = result * prime + x;
		result = result * prime + y;
		
		return result;
	}
	
	public void setX(int newX) {
		x = newX;
	}
	
	public void setY(int newY) {
		y = newY;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
}
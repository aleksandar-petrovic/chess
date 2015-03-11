// Marko Kabic
package chess.ChessPackage;

import javax.swing.JLabel;

public abstract class AbstractLabel extends JLabel {
	private Position pos;
	public AbstractLabel(int x, int y) {
		super();
		pos = new Position(x, y);
	}
	
	public Position getPosition() {
		return pos;
		
	}
	
}

// Marko Kabic
package chess.ChessPackage;

import java.awt.Color;

public class DarkLabel extends AbstractLabel {
	private Color color = new Color(171, 128, 75);
	
	public DarkLabel(int x, int y) {
		super(x, y);
		setOpaque(true);
		setBackground(color);
	}
	
}

// Marko Kabic
package chess.ChessPackage;
import java.awt.Color;

public class LightLabel extends AbstractLabel {
	private Color color = new Color(234, 210, 161);
	
	public LightLabel(int x, int y) {
		super(x, y);
		setOpaque(true);
		setBackground(color);	
	}
	
}
/*
 * Author: Demjan Grubic
 * GUI
 */

package chess;

import java.awt.Color;
import java.util.List;

import javax.swing.BorderFactory;

import chess.ChessPackage.AbstractFigure;
import chess.ChessPackage.DarkLabel;
import chess.ChessPackage.LightLabel;
import chess.ChessPackage.Position;
import chess.ChessPackage.AbstractLabel;

public class GUI {
	public static AbstractLabel getCell( int row, int column, List<AbstractFigure> whiteFigures, List<AbstractFigure> blackFigures, List<Position> potentialMoves ) {
		AbstractLabel label;
		
		if( (row + column) % 2 == 0) {
			label = new DarkLabel(row, column);
		}
		else {
			label = new LightLabel(row, column);
		}
		
		// draw images of white figures
		for (AbstractFigure figure : whiteFigures) {
			if ( figure.getPosition().equals(new Position(row,column)) ) {
				label.setIcon(figure.getImage());
			}
		}
		
		// draw images of black figures
		for (AbstractFigure figure : blackFigures) {
			if ( figure.getPosition().equals(new Position(row,column)) ) {
				label.setIcon(figure.getImage());
			}
		}
		
		Position position = new Position(row,column);
		if ( potentialMoves.contains(position) ) {	
			// draw border on occupied cells
			boolean occupiedCell = false;
			
			for (AbstractFigure figure : whiteFigures) {
				if ( position.equals(figure.getPosition()) ) {
					occupiedCell = true;
				}
			}
			
			for (AbstractFigure figure : blackFigures) {
				if ( position.equals(figure.getPosition()) ) {
					occupiedCell = true;
				}
			}
			
			if ( !occupiedCell ) {
				label.setBorder(BorderFactory.createLineBorder(Color.yellow, 2));
			}
			else {
				label.setBorder(BorderFactory.createLineBorder(Color.red, 2));
			}
		}
		
		return label;
	}
}

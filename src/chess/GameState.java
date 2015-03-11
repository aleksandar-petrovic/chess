/*
 * Author: Demjan Grubic
 * States for state machine
 */

package chess;

public enum GameState {
	EstablishingConnection, Start, Initialization, WaitingForOpponentToChooseColor, ChoosingColor, ChoosingFigure, ChoosingPotentialCell, MoveFigure, WaitingForOpponentMove, GameOver;
}
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JOptionPane;


public class Game {

	private static Player[] players;
	private static int numPlayers;
	private static int numRemainingPlayers;
	private static Player currentPlayer;
	private static int turn;
	private static Color[] colors;
	private static Point[] startingPoints;
	private final static String[] options = {"Play Again", "Quit"};
	
	public static void init(int n){
		numPlayers = n;
		numRemainingPlayers = n;
		if(n == 2) {
			colors = new Color[]{
					new Color(255, 153, 0, 235),
					new Color(153, 0, 255)
			};
//			startingPoints = new Point[]{
//					new Point(Board.width / 3, Board.height / 3),
//					new Point(Board.width - 1 - Board.width / 3, Board.height - 1 - Board.height / 3)
//			};
			startingPoints = new Point[]{
					new Point(Board.width / 3, Board.height - 1 - Board.height / 3),
					new Point(Board.width - 1 - Board.width / 3, Board.height / 3)
			};
		} else if(n == 4) {
			colors = new Color[]{
					new Color(0, 0, 235, 235), 
					new Color(235, 235, 0, 235), 
					new Color(235, 0, 0, 235), 
					new Color(0, 235, 0, 235)
			};
			startingPoints = new Point[]{
					new Point(0, 0),
					new Point(0, Board.height - 1),
					new Point(Board.width - 1, Board.height - 1),
					new Point(Board.width - 1, 0)
			};
		}
		players = new Player[n];
		for(int i = 0; i < n; i++)
			players[i] = new Player(i + 1);
		players[0] = new Computer(1);
		currentPlayer = players[0];
		turn = 1;
	}
	
	public static void start(){
		currentPlayer.startTurn();
	}
	
	public static void end(){
		int max = 0;
		ArrayList<Integer> indices = new ArrayList<Integer>();
		for(int i = 0; i < numPlayers; i++){
			if(players[i].getScore() >= max){
				if(players[i].getScore() > max)
					indices = new ArrayList<Integer>();
				max = players[i].getScore();
				indices.add(i + 1);
			}
		}
		String message;
		if(indices.size() == 1)
			message = "Player " + indices.get(0) + " wins with a score of " + max;
		else
			message = "Players " + indices.toString() + " tie with a score of " + max;

		int opt = JOptionPane.showOptionDialog(BoardPainter.boardPainter, message, "Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, null);
		if(opt == 0)
			Blokus.reset();
		else
			System.exit(1);
	}
	
	public static void endTurn(Piece piece){
		if(piece != null)
			currentPlayer.endTurn(piece);
		if(numRemainingPlayers == 0){
			Game.end();
			return;
		}
		turn += 1;
		if(turn > numPlayers)
			turn = 1;
		currentPlayer = players[turn - 1];
		BoardArea.boardArea.repaint();
		currentPlayer.startTurn();
	}
	
	public static void playerOut(){
		numRemainingPlayers -= 1;
	}

	public static Player[] getPlayers() {
		return players;
	}
	
	public static int getNumPlayers() {
		return numPlayers;
	}

	public static void setNumPlayers(int numPlayers) {
		Game.numPlayers = numPlayers;
	}

	public static Player getCurrentPlayer() {
		return currentPlayer;
	}

	public static void setCurrentPlayer(Player currentPlayer) {
		Game.currentPlayer = currentPlayer;
	}

	public static int getTurn() {
		return turn;
	}

	public static void setTurn(int turn) {
		Game.turn = turn;
	}
	
	public static Color[] getColors() {
		return colors;
	}
	
	public static Color idToColor(int id) {
		return colors[id - 1];
	}
	
	public static Point getStartingPoint(int id){
		return startingPoints[id - 1];
	}
}
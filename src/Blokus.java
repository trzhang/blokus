

public class Blokus {
	
	static int width = 14, height = 14;
	static int numPlayers = 2;
	
	public static void main(String[] args){
		Board.setDimensions(width, height);
		Game.init(numPlayers);
		InfluenceMap.init();
		BoardPainter.boardPainter = new BoardPainter();
		InputManager.inputManager = new InputManager();
		Game.start();
	}
	
	public static void reset(){
		Board.setDimensions(width, height);
		Game.init(numPlayers);
		InfluenceMap.init();
		Game.start();
	}
}

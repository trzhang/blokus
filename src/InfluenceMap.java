import java.awt.Point;
import java.util.Arrays;

public class InfluenceMap {

	private static double[][][] grid;
	static int OCCUPIED = Integer.MAX_VALUE;
	static int UNOCCUPIED = 0;
	
	public static void init() {
		grid = new double[Board.width][Board.height][Game.getNumPlayers()];
		update();
	}
	
	public static void add(Piece piece){
		for(Point square : piece.getSquares())
			grid[square.x][square.y][piece.getPlayer().getId() - 1] = InfluenceMap.OCCUPIED;
	}
	
	public static void remove(Piece piece){
		for(Point square : piece.getSquares())
			grid[square.x][square.y][piece.getPlayer().getId() - 1] = InfluenceMap.UNOCCUPIED;
	}
	
	public static double sum(Player player){
		double sum = 0;
		for(int i = 0; i < Board.width; i++)
			for(int j = 0; j < Board.width; j++)
				for(int k = 0; k < Game.getNumPlayers(); k++){
					if(k == player.getId() - 1)
						sum += grid[i][j][k];
					else
						sum -= grid[i][j][k];
				}
		return sum;
	}
	
	public static void update() {
		for(double[][] row : grid)
			for(double[] column : row)
				Arrays.fill(column, InfluenceMap.UNOCCUPIED);
		for(Player player : Game.getPlayers()){
			double max = 0;
			for(Point corner : player.getCorners()){
				for(char key : player.getPieces()){
					PieceIterator itr = new PieceIterator(key);
					while(itr.hasNext()){
						Piece piece = itr.next();
						piece.setPlayer(player);
						for(Point square : piece.getSquares()){
							piece.translate(corner.x - square.x, corner.y - square.y);
							if(Board.canPlacePiece(piece))
								for(Point p : piece.getSquares()){
									grid[p.x][p.y][player.getId() - 1] += 1;//piece.getSquares().size();
									if(grid[p.x][p.y][player.getId() - 1] > max)
										max = grid[p.x][p.y][player.getId() - 1];
								}
						}
					}
				}
			}
			if(max > 0){
				for(int i = 0; i < Board.width; i++)
					for(int j = 0; j < Board.height; j++)
						if(grid[i][j][player.getId() - 1] != InfluenceMap.OCCUPIED)
							grid[i][j][player.getId() - 1] /= (double) (2 * max);
			}
		}
	}
	
	public static double[][][] getGrid() {
		return grid;
	}
}

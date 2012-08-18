import java.awt.Point;
import java.util.HashSet;

public class Board {
	
	static int width, height;
	private static int[][] grid;
	
	public static void setDimensions(int width, int height){
		Board.width = width;
		Board.height = height;
		grid = new int[width][height];
	}
	
	public static void add(Piece piece){
		for(Point square : piece.getSquares()){
			grid[square.x][square.y] = piece.getPlayer().getId();
			for(Player player : Game.getPlayers())
				if(player.getCorners().contains(square))
					player.removeCorner(square);
		}
		for(Point corner : piece.getCorners())
			if(Board.canPlaceTile(corner) && !piece.getPlayer().hasCorner(corner))
				piece.getPlayer().addCorner(corner);
		InfluenceMap.add(piece);
		BoardArea.boardArea.repaint();
	}
	
	public static HashSet<Point> addTempPiece(Piece piece){
		HashSet<Point> newCorners = new HashSet<Point>();
		for(Point square : piece.getSquares())
			grid[square.x][square.y] = piece.getPlayer().getId();
		for(Point corner : piece.getCorners())
			if(Board.canPlaceTile(corner) && !piece.getPlayer().hasCorner(corner)){
				piece.getPlayer().addCorner(corner);
				newCorners.add(corner);
			}
		InfluenceMap.add(piece);
		return newCorners;
	}
	
	public static void removeTempPiece(Piece piece, HashSet<Point> newCorners){
		for(Point square : piece.getSquares())
			grid[square.x][square.y] = 0;
		for(Point corner : newCorners)
			piece.getPlayer().removeCorner(corner);
		InfluenceMap.remove(piece);
	}
	
	public static boolean contains(Point p){
		return (0 <= p.x && p.x < width) && (0 <= p.y && p.y < height);
	}
	
	public static boolean contains(int i, int j){
		return (0 <= i && i < width) && (0 <= j && j < height);
	}
	
	public static boolean contains(Piece piece){
		for(Point p : piece.getSquares())
			if(!Board.contains(p))
				return false;
		return true;
	}
	
	public static int getPlayerId(Point p){
		if(!Board.contains(p))
			return -1;
		return grid[p.x][p.y];
	}
	
	public static int getPlayerId(int i, int j){
		if(!Board.contains(i, j))
			return -1;
		return grid[i][j];
	}

	public static boolean canPlaceTile(Point p){
		return Board.contains(p) && grid[p.x][p.y] == 0;
	}
	
	public static boolean canPlaceTile(int i, int j){
		return Board.contains(i, j) && grid[i][j] == 0;
	}
	
	public static boolean canPlacePiece(Piece piece){
		for(Point square : piece.getSquares())
			if(!canPlaceTile(square))
				return false;
		for(Point border : piece.getBorder())
			if(getPlayerId(border) == piece.getPlayer().getId())
				return false;
		HashSet<Point> squares = new HashSet<Point>(piece.getSquares()); 
		squares.retainAll(piece.getPlayer().getCorners());
		return squares.size() > 0;
	}
	
	public static boolean canOrientPiece(char key, Point corner, Player player){
		PieceIterator itr = new PieceIterator(key);
		itr.getPiece().setPlayer(player);
		while(itr.hasNext()){
			Piece next = itr.next();
			for(Point square : next.getSquares()){
				next.translate(corner.x - square.x, corner.y - square.y);
				if(Board.canPlacePiece(next))
					return true;
			}
		}
		return false;
	}
	
	public static boolean canOrientPiece(Piece piece, Point corner){
		return canOrientPiece(piece.getName(), corner, piece.getPlayer());
	}
	
	public static Point gridToScreen(int i, int j){
		return new Point(i * BoardArea.squareWidth, (height - j - 1) * BoardArea.squareHeight);
	}
	
	public static Point screenToGrid(Point p){
		return new Point(p.x / BoardArea.squareWidth, height - p.y / BoardArea.squareHeight - 1);
	}
}
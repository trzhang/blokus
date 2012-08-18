import java.awt.Point;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class Piece {

	private HashSet<Point> squares;
	private HashSet<Point> border;
	private HashSet<Point> corners;
	private Point center;
	private Player player;
	private char name;
	private int numRotations;
	private int numFlips;
	public static final HashMap<Character, Piece> charToPiece = new HashMap<Character, Piece>();
	public static final Character[] ALL_PIECES = {
		'1','2','3','v','4','l','o','s','t','c','f','5','L','n','p','S','T','V','w','x','y'
	};
	
	private Piece(HashSet<Point> squares, HashSet<Point> border, HashSet<Point> corners, Point center, char name, int numRotations, int numFlips){
		this.squares = squares;
		this.border = border;
		this.corners = corners;
		this.center = center;
		this.name = name;
		this.numRotations = numRotations;
		this.numFlips = numFlips;
		Piece.charToPiece.put(name, this);
	}
	
	public Piece(Piece piece){
		this.squares = new HashSet<Point>();
		for(Point square : piece.squares)
			this.squares.add(new Point(square));
		this.border = new HashSet<Point>();
		for(Point border : piece.border)
			this.border.add(new Point(border));
		this.corners = new HashSet<Point>();
		for(Point corner : piece.corners)
			this.corners.add(new Point(corner));
		this.center = new Point(piece.center);
		this.player = piece.player;
		this.name = piece.name;
		this.numRotations = piece.numRotations;
		this.numFlips = piece.numFlips;
	}
	
	public Piece(char key){
		this(Piece.getPiece(key));
	}

	public void translate(int dx, int dy){
		for(Point p : this.getSquares())
			p.translate(dx, dy);
		for(Point p : this.getCorners())
			p.translate(dx, dy);
		for(Point p : this.getBorder())
			p.translate(dx, dy);
		this.center.translate(dx, dy);
	}
	
	public void rotate(Transformation transformation){
		if(transformation == Transformation.CLOCKWISE){
			int dx = center.x - center.y, dy = center.y + center.x; 
			for(Point p : this.getSquares())
				p.setLocation(p.y + dx, -p.x + dy);
			for(Point p : this.getCorners())
				p.setLocation(p.y + dx, -p.x + dy);
			for(Point p : this.getBorder())
				p.setLocation(p.y + dx, -p.x + dy);
		} else if(transformation == Transformation.COUNTERCLOCKWISE){
			int dx = center.x + center.y, dy = center.y - center.x;
			for(Point p : this.getSquares())
				p.setLocation(-p.y + dx, p.x + dy);
			for(Point p : this.getCorners())
				p.setLocation(-p.y + dx, p.x + dy);
			for(Point p : this.getBorder())
				p.setLocation(-p.y + dx, p.x + dy);
		}
	}
	
	public void flip(Transformation transformation){
		if(transformation == Transformation.HORIZONTAL){
			int dx = 2 * center.x;
			for(Point p : this.getSquares())
				p.setLocation(-p.x + dx, p.y);
			for(Point p : this.getCorners())
				p.setLocation(-p.x + dx, p.y);
			for(Point p : this.getBorder())
				p.setLocation(-p.x + dx, p.y);
		} else if(transformation == Transformation.VERTICAL){
			int dy = 2 * center.y;
			for(Point p : this.getSquares())
				p.setLocation(p.x, -p.y + dy);
			for(Point p : this.getCorners())
				p.setLocation(p.x, -p.y + dy);
			for(Point p : this.getBorder())
				p.setLocation(p.x, -p.y + dy);
		}
	}
	
	public void move(Point p){
		int dx = p.x - center.x, dy = p.y - center.y;
		this.translate(dx, dy);
	}
	
	public HashSet<Point> getSquares() {
		return squares;
	}

	public void setSquares(HashSet<Point> squares) {
		this.squares = squares;
	}

	public HashSet<Point> getBorder() {
		return border;
	}
	
	public void setBorder(HashSet<Point> border) {
		this.border = border;
	}
	
	public HashSet<Point> getCorners() {
		return corners;
	}
	
	public void setCorners(HashSet<Point> corners) {
		this.corners = corners;
	}

	public Point getCenter() { 
		return center;
	}
	
	public void setCenter(Point center) {
		this.center = center;
	}
	
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public char getName() {
		return name;
	}
	
	public int getNumRotations() { 
		return numRotations;
	}
	
	public int getNumFlips() {
		return numFlips;
	}
	
	public static Piece getPiece(char key){
		return Piece.charToPiece.get(key);
	}
	
	public static final Piece I1 = new Piece(
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(0, 0)})),
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(-1, 0), new Point(0, -1), new Point(0, 1), new Point(1, 0)})),
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(-1, -1), new Point(-1, 1), new Point(1, -1), new Point(1, 1)})),
		new Point(0, 0),
		'1', 1, 1
	);
	
	public static Piece testPiece = new Piece(I1);

	public static final Piece I2 = new Piece(
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(0, -1), new Point(0, 0)})),
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(-1, -1), new Point(-1, 0), new Point(0, -2), new Point(0, 1), new Point(1, -1), new Point(1, 0)})),
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(-1, -2), new Point(-1, 1), new Point(1, -2), new Point(1, 1)})),
        new Point(0, 0),
        '2', 2, 1
	);

	public static final Piece I3 = new Piece(
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(0, -1), new Point(0, 0), new Point(0, 1)})),
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(-1, -1), new Point(-1, 0), new Point(-1, 1), new Point(0, -2), new Point(0, 2), new Point(1, -1), new Point(1, 0), new Point(1, 1)})),
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(-1, -2), new Point(-1, 2), new Point(1, -2), new Point(1, 2)})),
        new Point(0, 0),
		'3', 2, 1
	);
	public static final Piece V3 = new Piece(
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(0, 0), new Point(0, 1), new Point(1, 0)})),
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(-1, 0), new Point(-1, 1), new Point(0, -1), new Point(0, 2), new Point(1, -1), new Point(1, 1), new Point(2, 0)})),
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(-1, -1), new Point(-1, 2), new Point(1, 2), new Point(2, -1), new Point(2, 1)})),
        new Point(0, 0),
        'v', 4, 1
	);

	public static final Piece I4 = new Piece(
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(0, -2), new Point(0, -1), new Point(0, 0), new Point(0, 1)})),
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(-1, -2), new Point(-1, -1), new Point(-1, 0), new Point(-1, 1), new Point(0, -3), new Point(0, 2), new Point(1, -2), new Point(1, -1), new Point(1, 0), new Point(1, 1)})),
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(-1, -3), new Point(-1, 2), new Point(1, -3), new Point(1, 2)})),
        new Point(0, 0),
        '4', 2, 1
	);
	public static final Piece L4 = new Piece(
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(0, 0), new Point(0, 1), new Point(0, 2), new Point(1, 0)})),
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(-1, 0), new Point(-1, 1), new Point(-1, 2), new Point(0, -1), new Point(0, 3), new Point(1, -1), new Point(1, 1), new Point(1, 2), new Point(2, 0)})),
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(-1, -1), new Point(-1, 3), new Point(1, 3), new Point(2, -1), new Point(2, 1)})),
        new Point(0, 0),
        'l', 4, 2
	);
	public static final Piece O = new Piece(
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(0, -1), new Point(0, 0), new Point(1, -1), new Point(1, 0)})),
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(-1, -1), new Point(-1, 0), new Point(0, -2), new Point(0, 1), new Point(1, -2), new Point(1, 1), new Point(2, -1), new Point(2, 0)})),
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(-1, -2), new Point(-1, 1), new Point(2, -2), new Point(2, 1)})),
        new Point(0, 0),
        'o', 1, 1
	);
	public static final Piece S4 = new Piece(
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(-1, -1), new Point(0, -1), new Point(0, 0), new Point(1, 0)})),
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(-2, -1), new Point(-1, -2), new Point(-1, 0), new Point(0, -2), new Point(0, 1), new Point(1, -1), new Point(1, 1), new Point(2, 0)})),
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(-2, -2), new Point(-2, 0), new Point(-1, 1), new Point(1, -2), new Point(2, -1), new Point(2, 1)})),
        new Point(0, 0),
        's', 2, 2
	);
	public static final Piece T4 = new Piece(
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(-1, 0), new Point(0, -1), new Point(0, 0), new Point(1, 0)})),
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(-2, 0), new Point(-1, -1), new Point(-1, 1), new Point(0, -2), new Point(0, 1), new Point(1, -1), new Point(1, 1), new Point(2, 0)})),
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(-2, -1), new Point(-2, 1), new Point(-1, -2), new Point(1, -2), new Point(2, -1), new Point(2, 1)})),
        new Point(0, 0),
        't', 4, 1
	);

	public static final Piece C = new Piece(
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(-1, 0), new Point(-1, 1), new Point(0, 0), new Point(1, 0), new Point(1, 1)})),
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(-2, 0), new Point(-2, 1), new Point(-1, -1), new Point(-1, 2), new Point(0, -1), new Point(0, 1), new Point(1, -1), new Point(1, 2), new Point(2, 0), new Point(2, 1)})),
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(-2, -1), new Point(-2, 2), new Point(0, 2), new Point(2, -1), new Point(2, 2)})),
        new Point(0, 0),
        'c', 4, 1
	);
	public static final Piece F = new Piece(		
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(-1, -1), new Point(0, -1), new Point(0, 0), new Point(0, 1), new Point(1, 0)})),	
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(-2, -1), new Point(-1, -2), new Point(-1, 0), new Point(-1, 1), new Point(0, -2), new Point(0, 2), new Point(1, -1), new Point(1, 1), new Point(2, 0)})),	
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(-2, -2), new Point(-2, 0), new Point(-1, 2), new Point(1, -2), new Point(1, 2), new Point(2, -1), new Point(2, 1)})),
        new Point(0, 0),
        'f', 4, 2
	);
	public static final Piece I5 = new Piece(
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(0, -2), new Point(0, -1), new Point(0, 0), new Point(0, 1), new Point(0, 2)})),
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(-1, -2), new Point(-1, -1), new Point(-1, 0), new Point(-1, 1), new Point(-1, 2), new Point(0, -3), new Point(0, 3), new Point(1, -2), new Point(1, -1), new Point(1, 0), new Point(1, 1), new Point(1, 2)})),
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(-1, -3), new Point(-1, 3), new Point(1, -3), new Point(1, 3)})),
        new Point(0, 0),
        '5', 2, 1
	);
	public static final Piece L5 = new Piece(
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(0, 0), new Point(0, 1), new Point(0, 2), new Point(0, 3), new Point(1, 0)})),
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(-1, 0), new Point(-1, 1), new Point(-1, 2), new Point(-1, 3), new Point(0, -1), new Point(0, 4), new Point(1, -1), new Point(1, 1), new Point(1, 2), new Point(1, 3), new Point(2, 0)})),
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(-1, -1), new Point(-1, 4), new Point(1, 4), new Point(2, -1), new Point(2, 1)})),
        new Point(0, 0),
        'L', 4, 2
	);
	public static final Piece N = new Piece(
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(0, -2), new Point(0, -1), new Point(0, 0), new Point(1, 0), new Point(1, 1)})),
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(-1, -2), new Point(-1, -1), new Point(-1, 0), new Point(0, -3), new Point(0, 1), new Point(1, -2), new Point(1, -1), new Point(1, 2), new Point(2, 0), new Point(2, 1)})),
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(-1, -3), new Point(-1, 1), new Point(0, 2), new Point(1, -3), new Point(2, -1), new Point(2, 2)})),
        new Point(0, 0),
        'n', 4, 2
	);
	public static final Piece P = new Piece(
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(0, -1), new Point(0, 0), new Point(0, 1), new Point(1, -1), new Point(1, 0)})),
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(-1, -1), new Point(-1, 0), new Point(-1, 1), new Point(0, -2), new Point(0, 2), new Point(1, -2), new Point(1, 1), new Point(2, -1), new Point(2, 0)})),
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(-1, -2), new Point(-1, 2), new Point(1, 2), new Point(2, -2), new Point(2, 1)})),
        new Point(0, 0),
        'p', 4, 2
	);
	public static final Piece S5 = new Piece(
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(-1, -1), new Point(-1, 0), new Point(0, 0), new Point(1, 0), new Point(1, 1)})),
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(-2, -1), new Point(-2, 0), new Point(-1, -2), new Point(-1, 1), new Point(0, -1), new Point(0, 1), new Point(1, -1), new Point(1, 2), new Point(2, 0), new Point(2, 1)})),
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(-2, -2), new Point(-2, 1), new Point(0, -2), new Point(0, 2), new Point(2, -1), new Point(2, 2)})),
        new Point(0, 0),
        'S', 2, 2
	);
	public static final Piece T5 = new Piece(
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(-1, 1), new Point(0, -1), new Point(0, 0), new Point(0, 1), new Point(1, 1)})),
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(-2, 1), new Point(-1, -1), new Point(-1, 0), new Point(-1, 2), new Point(0, -2), new Point(0, 2), new Point(1, -1), new Point(1, 0), new Point(1, 2), new Point(2, 1)})),
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(-2, 0), new Point(-2, 2), new Point(-1, -2), new Point(1, -2), new Point(2, 0), new Point(2, 2)})),
        new Point(0, 0),
        'T', 4, 1
	);
	public static final Piece V5 = new Piece(
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(0, 0), new Point(0, 1), new Point(0, 2), new Point(1, 0), new Point(2, 0)})),
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(-1, 0), new Point(-1, 1), new Point(-1, 2), new Point(0, -1), new Point(0, 3), new Point(1, -1), new Point(1, 1), new Point(1, 2), new Point(2, -1), new Point(2, 1), new Point(3, 0)})),
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(-1, -1), new Point(-1, 3), new Point(1, 3), new Point(3, -1), new Point(3, 1)})),
        new Point(0, 0),
        'V', 4, 1
	);
	public static final Piece W = new Piece(
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(-1, -1), new Point(0, -1), new Point(0, 0), new Point(1, 0), new Point(1, 1)})),
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(-2, -1), new Point(-1, -2), new Point(-1, 0), new Point(0, -2), new Point(0, 1), new Point(1, -1), new Point(1, 2), new Point(2, 0), new Point(2, 1)})),
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(-2, -2), new Point(-2, 0), new Point(-1, 1), new Point(0, 2), new Point(1, -2), new Point(2, -1), new Point(2, 2)})),
        new Point(0, 0),
        'w', 4, 1
	);
	public static final Piece X = new Piece(
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(-1, 0), new Point(0, -1), new Point(0, 0), new Point(0, 1), new Point(1, 0)})),
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(-2, 0), new Point(-1, -1), new Point(-1, 1), new Point(0, -2), new Point(0, 2), new Point(1, -1), new Point(1, 1), new Point(2, 0)})),
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(-2, -1), new Point(-2, 1), new Point(-1, -2), new Point(-1, 2), new Point(1, -2), new Point(1, 2), new Point(2, -1), new Point(2, 1)})),
        new Point(0, 0),
        'x', 1, 1
	);
	public static final Piece Y = new Piece(
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(0, -2), new Point(0, -1), new Point(0, 0), new Point(0, 1), new Point(1, 0)})),
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(-1, -2), new Point(-1, -1), new Point(-1, 0), new Point(-1, 1), new Point(0, -3), new Point(0, 2), new Point(1, -2), new Point(1, -1), new Point(1, 1), new Point(2, 0)})),
		new HashSet<Point>(Arrays.asList(new Point[]{new Point(-1, -3), new Point(-1, 2), new Point(1, -3), new Point(1, 2), new Point(2, -1), new Point(2, 1)})),
        new Point(0, 0),
        'y', 4, 2
	);
}

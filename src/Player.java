import java.awt.Color;
import java.awt.Point;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.JOptionPane;


public class Player {
	protected int id;
	protected Color color;
	protected HashSet<Point> corners;
	protected HashSet<Character> pieces;
	protected int score;
	protected boolean hasMove;
	
	public Player(int id){
		this.id = id;
		color = Game.idToColor(id);
		corners = new HashSet<Point>();
		corners.add(Game.getStartingPoint(this.id));
		pieces = new HashSet<Character>(Arrays.asList(Piece.ALL_PIECES));
		score = 0;
		hasMove = true;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Color getColor() {
		return color;
	}
	
	public HashSet<Point> getCorners(){
		return corners;
	}
	
	public boolean hasCorner(Point corner){
		return this.corners.contains(corner);
	}
	
	public void addCorner(Point corner){
		this.corners.add(corner);
	}
	
	public void removeCorner(Point corner){
		this.corners.remove(corner);
	}
	
	public void updateCorners(){
		Piece.testPiece.setPlayer(this);
		Iterator<Point> itr = corners.iterator();
		while(itr.hasNext()){
			Point corner = itr.next();
			if(this.hasPiece(Piece.I1.getName())){
				Piece.testPiece.move(corner);
				if(!Board.canPlacePiece(Piece.testPiece))
					itr.remove();
			} else {
				boolean valid = false;
				for(char key : pieces)
					if(Board.canOrientPiece(key, corner, this)){
						valid = true;
						break;
					}
				if(!valid)
					itr.remove();
			}
		}
	}
	
	public HashSet<Character> getPieces(){
		return pieces;
	}
	
	public boolean hasPiece(char piece){
		return pieces.contains(piece);
	}
	
	public boolean addPiece(char piece){
		return pieces.add((Character) piece);
	}
	
	public boolean removePiece(char piece){
		return pieces.remove((Character) piece);
	}
	
	public int getScore() {
		return score;
	}

	public void addScore(int score) {
		this.score += score;
		ScorePanel.scorePanel.updateScore(this.id, this.score);
	}
	
	public boolean hasMove(){
		return hasMove;
	}
	
	public void update(){
		if(!hasMove){
			Game.endTurn(null);
			return;
		}
		updateCorners();
		if(corners.size() == 0){
			hasMove = false;
			JOptionPane.showMessageDialog(BoardArea.boardArea, "Player " + id + " is out of moves");
			Game.playerOut();
			Game.endTurn(null);
			return;
		}
		InfluenceMap.update();
	}
	
	public void startTurn(){
		update();
		InfoArea.infoArea.repaint();
	}
	
	public void endTurn(Piece piece){
		addScore(piece.getSquares().size());
		removePiece(piece.getName());
	}
}
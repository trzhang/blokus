import java.awt.Point;
import java.util.HashSet;

import javax.swing.JOptionPane;


public class Computer extends Player {

	public Computer(int id) {
		super(id);	
	}
	
	public void startTurn(){
		long start = System.currentTimeMillis();
		update();
		BoardArea.boardArea.paintImmediately(0, 0, BoardArea.width, BoardArea.height);
		InfoArea.infoArea.paintImmediately(0, 0, InfoArea.width, InfoArea.height);
		
		double maxSum = Integer.MIN_VALUE;
		Piece newPiece = null;
		
		HashSet<Point> currentCorners = new HashSet<Point>(corners);
		for(Point corner : currentCorners){
			for(char key : pieces){
				PieceIterator itr = new PieceIterator(key);
				while(itr.hasNext()){
					Piece piece = itr.next();
					piece.setPlayer(this);
					for(Point square : piece.getSquares()){
						piece.translate(corner.x - square.x, corner.y - square.y);
						if(Board.canPlacePiece(piece)){
							//System.out.println("Adding " + piece.getName() + " " + piece.getSquares());
							HashSet<Point> newCorners = Board.addTempPiece(piece);
							InfluenceMap.update();
							double sum = InfluenceMap.sum(this) + piece.getSquares().size();
							//System.out.println(piece.getName() + " " + corner + " " + sum + " " + maxSum);
							if(sum > maxSum){
								maxSum = sum;
								newPiece = new Piece(piece);
							}
							//System.out.println("Removing " + piece.getName() + " " + piece.getSquares());
							Board.removeTempPiece(piece, newCorners);
						}
					}
				}
			}
		}
//		System.out.println("MAX " + maxSum + " " + newPiece.getName() + " " + newPiece.getSquares());
		long end = System.currentTimeMillis();
		if(end - start < 2500)
			try {
				BoardArea.boardArea.paintImmediately(0, 0, BoardArea.width, BoardArea.height);
				Thread.sleep(2500 - (end - start));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		Board.add(newPiece);
		Game.endTurn(newPiece);
	}

	public void endTurn(Piece piece){
		addScore(piece.getSquares().size());
		removePiece(piece.getName());
	}
}

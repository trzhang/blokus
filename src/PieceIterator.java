import java.util.Iterator;

public class PieceIterator implements Iterator<Piece> {

	private Piece piece;
	private int position;
	
	public PieceIterator(Piece piece) {
		this.piece = new Piece(piece);
		this.position = 0;
	}
	
	public PieceIterator(char key) {
		this.piece = new Piece(key);
		this.position = 0;
	}
	
	public boolean hasNext() {
		return piece != null && position < piece.getNumRotations() * piece.getNumFlips();
	}

	public Piece next() {
		piece.rotate(Transformation.CLOCKWISE);
		if(position == piece.getNumRotations())
			piece.flip(Transformation.HORIZONTAL);
		position += 1;
		return piece;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}
	
	public Piece getPiece() {
		return piece;
	}
	
	public void setPiece(Piece piece) {
		this.piece = piece;
	}
	
	public void reset(){
		position = 0;
	}
}

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class BoardPainter extends JFrame {
	
	static final int width = 1200, height = 800;
	static BoardPainter boardPainter;
	
	public BoardPainter(){
		this.setTitle("Blokus");
		this.setPreferredSize(new Dimension(width, height + 50));
		this.setLayout(new BorderLayout(0, 20));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.add(BoardArea.boardArea, BorderLayout.WEST);
		this.add(InfoArea.infoArea, BorderLayout.CENTER);
		this.pack();
		this.setVisible(true);
	}
	
	public void reset(){
		ScorePanel.scorePanel.reset();
	}
}

class BoardArea extends JPanel {
	
	static final int width = BoardPainter.height, height = BoardPainter.height;
	static final int squareWidth = width / Board.width, squareHeight = height / Board.height;
	static BoardArea boardArea = new BoardArea();
	private Piece currentPiece = null;
	
	public BoardArea(){
		this.setPreferredSize(new Dimension(width, height));
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		this.setVisible(true);
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		drawGridLines(g);
		drawPieces(g);
//		drawCorners(g);
		drawInfluenceMap(g, InfluenceMap.getGrid());
		drawCurrentPiece(g);
	}
	
	public void drawGridLines(Graphics g){
		g.setColor(Color.black);
		for(int i = 0; i < Board.width + 1; i++)
			g.drawLine(i * squareWidth, 0, i * squareWidth, height);
		for(int i = 0; i < Board.height + 1; i++)
			g.drawLine(0, i * squareHeight, width, i * squareHeight);	
	}
	
	public void drawTile(Graphics g, int i, int j, Color color){
		g.setColor(color);
		Point p = Board.gridToScreen(i, j);
		g.fillRect(p.x, p.y, squareWidth, squareHeight);
	}
	
	public void drawCurrentPiece(Graphics g){
		if(currentPiece != null){
			Color color = currentPiece.getPlayer().getColor();
			color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 200);
			for(Point square : currentPiece.getSquares())
				drawTile(g, square.x, square.y, color);
		}
	}
	
	public void drawPieces(Graphics g){
		for(int i = 0; i < Board.width; i++)
			for(int j = 0; j < Board.height; j++)
				if(Board.getPlayerId(i, j) != 0)
					drawTile(g, i, j, Game.idToColor(Board.getPlayerId(i, j)));
	}
	
	public void drawCorners(Graphics g){
		Color color = Game.getCurrentPlayer().getColor();
		color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 120);
		for(Point corner : Game.getCurrentPlayer().getCorners())
			drawTile(g, corner.x, corner.y, color);
	}
	
	public void drawInfluenceMap(Graphics g, double[][][] influenceMap){
		for(int i = 0; i < influenceMap.length; i++)
			for(int j = 0; j < influenceMap[i].length; j++){
				double max = 0;
				int indexOfMax = 0;
				for(int k = 0; k < Game.getNumPlayers(); k++)
					if(influenceMap[i][j][k] > max){
						max = influenceMap[i][j][k];
						indexOfMax = k;
					}
				Color color = Game.getPlayers()[indexOfMax].getColor();
				color = new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.min((int) (influenceMap[i][j][indexOfMax] * 235), 235));
				drawTile(g, i, j, color);
				
				String s = max != 0 && max != InfluenceMap.OCCUPIED ? String.format("%.2f", max) : "-";
	            int len = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
	            Point p = Board.gridToScreen(i, j);
	            g.setColor(Color.black);
	            g.drawString(s, p.x + squareWidth / 2 - len / 2, p.y + squareHeight / 2);
			}
	}
	
	public Piece getCurrentPiece() {
		return currentPiece;
	}

	public void setCurrentPiece(Piece currentPiece) {
		this.currentPiece = currentPiece;
	}
}

class InfoArea extends JPanel {
	
	static final int width = BoardPainter.width - BoardPainter.height, height = BoardPainter.height;
	static InfoArea infoArea = new InfoArea();
	
	public InfoArea(){
		this.setPreferredSize(new Dimension(width, height));
		this.setLayout(new BorderLayout());
		
		this.add(ScorePanel.scorePanel, BorderLayout.NORTH);
		this.add(ButtonPanel.buttonPanel, BorderLayout.CENTER);
		this.add(PieceArea.pieceArea, BorderLayout.SOUTH);
		this.setVisible(true);
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
	}
}

class ScorePanel extends JPanel {
	
	static ScorePanel scorePanel = new ScorePanel();
	private JLabel[] scores;
	
	public ScorePanel(){
		this.setPreferredSize(new Dimension(InfoArea.width, 100));
		this.setLayout(new GridLayout(2, Game.getNumPlayers() / 2));
		scores = new JLabel[]{
			new JLabel("Player 1: 0", SwingConstants.CENTER),
			new JLabel("Player 2: 0", SwingConstants.CENTER),
			new JLabel("Player 3: 0", SwingConstants.CENTER),
			new JLabel("Player 4: 0", SwingConstants.CENTER)
		};
		for(int i = 0; i < 4; i++){
			scores[i].setOpaque(true);
			scores[i].setBackground(new Color(0, 255, 0, 120));
			scores[i].setBorder(BorderFactory.createLineBorder(Color.green.darker()));
			scores[i].setFont(new Font("Arial", Font.BOLD, 20));
			
		}
		for(int i = 0; i < Game.getNumPlayers(); i++)
			this.add(scores[i]);
	}
	
	public void updateScore(int playerId, int score){
		scores[playerId - 1].setText("Player " + playerId + ": " + score);
		repaint();
	}
	
	public void reset(){
		for(int i = 0; i < Game.getNumPlayers(); i++)
			scores[i].setText("Player " + (i + 1) + ": " + 0);
	}
}

class ButtonPanel extends JPanel {
	
	static ButtonPanel buttonPanel = new ButtonPanel();
	
	public ButtonPanel(){
		this.setPreferredSize(new Dimension(InfoArea.width, 50));
		this.setLayout(new GridLayout(1, 4));
		
		JButton counterclockwise = new JButton(new ImageIcon("images/counterclockwise.png"));
		counterclockwise.setFocusable(false);
		counterclockwise.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if(BoardArea.boardArea.getCurrentPiece() != null)
					BoardArea.boardArea.getCurrentPiece().rotate(Transformation.COUNTERCLOCKWISE);
			}
		});
		JButton clockwise = new JButton(new ImageIcon("images/clockwise.png"));
		clockwise.setFocusable(false);
		clockwise.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if(BoardArea.boardArea.getCurrentPiece() != null)
					BoardArea.boardArea.getCurrentPiece().rotate(Transformation.CLOCKWISE);
			}
		});
		JButton horizontal = new JButton(new ImageIcon("images/horizontal.png"));
		horizontal.setFocusable(false);
		horizontal.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if(BoardArea.boardArea.getCurrentPiece() != null)
					BoardArea.boardArea.getCurrentPiece().flip(Transformation.HORIZONTAL);
			}
		});
		JButton vertical = new JButton(new ImageIcon("images/vertical.png"));
		vertical.setFocusable(false);
		vertical.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if(BoardArea.boardArea.getCurrentPiece() != null)
					BoardArea.boardArea.getCurrentPiece().flip(Transformation.VERTICAL);
			}
		});
		this.add(counterclockwise);
		this.add(clockwise);
		this.add(horizontal);
		this.add(vertical);
	}
}

class PieceArea extends JPanel {
	
	static PieceArea pieceArea = new PieceArea();
	private PiecePanel[] piecePanels = new PiecePanel[21];
	
	public PieceArea() {
		this.setPreferredSize(new Dimension(InfoArea.width, InfoArea.height - 150));
		this.setLayout(new GridLayout(7, 3));
		
		for(int i = 0; i < 21; i++){
			piecePanels[i] = new PiecePanel(Piece.getPiece(Piece.ALL_PIECES[i]));
			this.add(piecePanels[i]);
		}
	}
}

class PiecePanel extends JPanel {
	
	private final int width = 100, height = 100;
	private final int squareWidth = width / 5, squareHeight = height / 5;
	private Piece piece;
	
	public PiecePanel(Piece piece) {
		this.setPreferredSize(new Dimension(100, 100));
		this.piece = new Piece(piece);
		if(this.piece.getName() == 'V' || Character.toLowerCase(this.piece.getName()) == 'l')
			this.piece.translate(0, -1);
		else if(this.piece.getName() == 't' || this.piece.getName() == 'y')
			this.piece.translate(0, 1);
		
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e){
				if(Game.getCurrentPlayer().hasPiece(getPieceName())){
					Piece piece = new Piece(getPieceName());
					piece.setPlayer(Game.getCurrentPlayer());
					piece.move(InputManager.inputManager.getMousePosition());
					BoardArea.boardArea.setCurrentPiece(piece);
				}
			}
		});
	}
	
	public void paintComponent(Graphics g){
		Color color;
		if(Game.getCurrentPlayer().hasPiece(piece.getName())){
			color = Game.getCurrentPlayer().getColor();
			color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 200);
		}
		else
			color = new Color(128, 128, 128, 200);
		drawPiece(g, piece, color);
	}
	
	public void drawTile(Graphics g, int i, int j, Color color){
		g.setColor(color);
		Point p = gridToPanel(i, j);
		g.fillRect(p.x, p.y, squareWidth, squareHeight);
	}
	
	public void drawPiece(Graphics g, Piece piece, Color color){
		for(Point square : piece.getSquares())
			drawTile(g, square.x, square.y, color);
	}
	
	public Point gridToPanel(int i, int j){
		return new Point((i + 2) * squareWidth, (5 - (j + 2) - 1) * squareHeight);
	}
	
	public char getPieceName(){
		return piece.getName();
	}
}
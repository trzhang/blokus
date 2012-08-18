import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Arrays;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

public class InputManager {
	
	static InputManager inputManager = new InputManager(); 
	private InputMap inputMap;
	private ActionMap actionMap;
	private int mouseX, mouseY = 0;
	
	public InputManager(){
		inputMap = BoardArea.boardArea.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		actionMap = BoardArea.boardArea.getActionMap();
		
		BoardArea.boardArea.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				doMousePressed(e);
			}
		});
		
		BoardArea.boardArea.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent e) {
				doMouseMoved(e);
			}
		});
		
		this.addKeys(new char[]{'1','2','3','v','4','l','o','s','t','c','f','5','L','n','p','S','T','V','w','x','y'});
	}
	
	public void doMousePressed(MouseEvent e){
		if(BoardArea.boardArea.getCurrentPiece() != null)
			if(Board.canPlacePiece(BoardArea.boardArea.getCurrentPiece())){
				Board.add(BoardArea.boardArea.getCurrentPiece());
				Game.endTurn(BoardArea.boardArea.getCurrentPiece());
				BoardArea.boardArea.setCurrentPiece(null);
			}
	}
	
	public void doMouseMoved(MouseEvent e){
		mouseX = e.getX();
		mouseY = e.getY();
		Point p = Board.screenToGrid(e.getPoint());
		if(BoardArea.boardArea.getCurrentPiece() != null){
			BoardArea.boardArea.getCurrentPiece().move(p);
			BoardArea.boardArea.repaint();
		}
		if(Board.contains(p))
			BoardArea.boardArea.setToolTipText(Arrays.toString(InfluenceMap.getGrid()[p.x][p.y]));
	}
	
	public void addKeys(char[] keys){
		for(final char key : keys){
			inputMap.put(KeyStroke.getKeyStroke(key), key);
			actionMap.put(key, new AbstractAction(){
				public void actionPerformed(ActionEvent e) {
					if(!(Game.getCurrentPlayer() instanceof Computer) && Game.getCurrentPlayer().hasPiece(key)){
						Piece piece = new Piece(key);
						piece.setPlayer(Game.getCurrentPlayer());
						piece.move(Board.screenToGrid(new Point(mouseX, mouseY)));
						BoardArea.boardArea.setCurrentPiece(piece);
						BoardArea.boardArea.repaint();
					}
				}
			});
		}
		
		inputMap.put(KeyStroke.getKeyStroke((char) KeyEvent.VK_ENTER), "enter");
		actionMap.put("enter", new AbstractAction(){
			public void actionPerformed(ActionEvent e) {
				if(!(Game.getCurrentPlayer() instanceof Computer)){
					doMousePressed(null);
					BoardArea.boardArea.repaint();
				}
			}
		});
		
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "esc");
		actionMap.put("esc", new AbstractAction(){
			public void actionPerformed(ActionEvent e) {
				if(!(Game.getCurrentPlayer() instanceof Computer)){
					BoardArea.boardArea.setCurrentPiece(null);
					BoardArea.boardArea.repaint();
				}
			}
		});
		
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "left");
		actionMap.put("left", new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				if(BoardArea.boardArea.getCurrentPiece() != null){
					BoardArea.boardArea.getCurrentPiece().translate(-1, 0);
					if(!Board.contains(BoardArea.boardArea.getCurrentPiece()))
						BoardArea.boardArea.getCurrentPiece().translate(1, 0);
					BoardArea.boardArea.repaint();
				}
			}
		});
		
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "right");
		actionMap.put("right", new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				if(BoardArea.boardArea.getCurrentPiece() != null){
					BoardArea.boardArea.getCurrentPiece().translate(1, 0);
					if(!Board.contains(BoardArea.boardArea.getCurrentPiece()))
						BoardArea.boardArea.getCurrentPiece().translate(-1, 0);
					BoardArea.boardArea.repaint();
				}
			}
		});
		
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "up");
		actionMap.put("up", new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				if(BoardArea.boardArea.getCurrentPiece() != null){
					BoardArea.boardArea.getCurrentPiece().translate(0, 1);
					if(!Board.contains(BoardArea.boardArea.getCurrentPiece()))
						BoardArea.boardArea.getCurrentPiece().translate(0, -1);
					BoardArea.boardArea.repaint();
				}
			}
		});
		
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "down");
		actionMap.put("down", new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				if(BoardArea.boardArea.getCurrentPiece() != null){
					BoardArea.boardArea.getCurrentPiece().translate(0, -1);
					if(!Board.contains(BoardArea.boardArea.getCurrentPiece()))
						BoardArea.boardArea.getCurrentPiece().translate(0, 1);
					BoardArea.boardArea.repaint();
				}
			}
		});
		
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.CTRL_DOWN_MASK), "ccw");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, InputEvent.CTRL_DOWN_MASK), "ccw");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, InputEvent.SHIFT_DOWN_MASK), "ccw");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_OPEN_BRACKET, 0), "ccw");
		actionMap.put("ccw", new AbstractAction(){
			public void actionPerformed(ActionEvent e) {
				if(BoardArea.boardArea.getCurrentPiece() != null){
					BoardArea.boardArea.getCurrentPiece().rotate(Transformation.COUNTERCLOCKWISE);
					BoardArea.boardArea.repaint();
				}
			}
		});
		
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.CTRL_DOWN_MASK), "cw");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "cw");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_CLOSE_BRACKET, 0), "cw");
		actionMap.put("cw", new AbstractAction(){
			public void actionPerformed(ActionEvent e) {
				if(BoardArea.boardArea.getCurrentPiece() != null){
					BoardArea.boardArea.getCurrentPiece().rotate(Transformation.CLOCKWISE);
					BoardArea.boardArea.repaint();
				}
			}
		});
		inputMap.put(KeyStroke.getKeyStroke((char) KeyEvent.VK_LEFT, InputEvent.SHIFT_DOWN_MASK), "h");
		inputMap.put(KeyStroke.getKeyStroke((char) KeyEvent.VK_RIGHT, InputEvent.SHIFT_DOWN_MASK), "h");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SLASH, 0), "h");
		actionMap.put("h", new AbstractAction(){
			public void actionPerformed(ActionEvent e) {
				if(BoardArea.boardArea.getCurrentPiece() != null){
					BoardArea.boardArea.getCurrentPiece().flip(Transformation.HORIZONTAL);
					BoardArea.boardArea.repaint();
				}
			}
		});
		
		inputMap.put(KeyStroke.getKeyStroke((char) KeyEvent.VK_UP, InputEvent.SHIFT_DOWN_MASK), "v");
		inputMap.put(KeyStroke.getKeyStroke((char) KeyEvent.VK_DOWN, InputEvent.SHIFT_DOWN_MASK), "v");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, 0), "v");
		actionMap.put("v", new AbstractAction(){
			public void actionPerformed(ActionEvent e) {
				if(BoardArea.boardArea.getCurrentPiece() != null){
					BoardArea.boardArea.getCurrentPiece().flip(Transformation.VERTICAL);
					BoardArea.boardArea.repaint();
				}
			}
		});
	}
	
	public Point getMousePosition(){
		return Board.screenToGrid(new Point(mouseX, mouseY));
	}
}

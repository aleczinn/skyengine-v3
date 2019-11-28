package de.games.chess.internal;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import de.games.chess.piece.Piece;
import de.games.chess.piece.pieces.Bauer;
import de.games.chess.piece.pieces.Dame;
import de.games.chess.piece.pieces.Koenig;
import de.games.chess.piece.pieces.Lauefer;
import de.games.chess.piece.pieces.Springer;
import de.games.chess.piece.pieces.Turm;
import de.skyengine.core.Input;
import de.skyengine.graphics.ImmediateRenderer;
import de.skyengine.graphics.ImmediateRenderer.ShapeType;
import de.skyengine.resources.Resources;
import de.skyengine.utils.math.Vector2d;

@SuppressWarnings("unused")
public class Grid {

	private float x;
	private float y;
	
	private Field[][] fields;
	private Field selected;
	
	private final int TILE_WIDTH = 64;
	private final int TILE_HEIGHT = 64;
	
	private final int GRID_WIDTH = this.TILE_WIDTH * 8;
	private final int GRID_HEIGHT = this.TILE_HEIGHT * 8;
	
	private PieceType currentPlayer;
	private List<Piece> whiteThrowed;	// Figuren vom Weiﬂen Spieler die rausgeworfen wurden
	private List<Piece> blackThrowed;		// Figuren vom Schwarzen Spieler die rausgeworfen wurden
	
	private Vector2d last;
	private int lastTextureID;
	
	private float animateIndex = 0;
	private float animateX;
	private float animateY;
	
	public Grid(float posX, float posY) {
		this.x = posX;
		this.y = posY;
		
		this.fields = new Field[8][8];
		
		for(int x = 0; x < 8; x++) {
			for(int y = 0; y < 8; y++) {
				int white = x + y;
				white %= 2;
				
				this.fields[x][y] = new Field(x * this.TILE_WIDTH, y * this.TILE_HEIGHT, white == 0 ? true : false);
			}
		}
		
		this.currentPlayer = PieceType.WHITE;
		this.whiteThrowed = new ArrayList<Piece>();
		this.blackThrowed = new ArrayList<Piece>();
	}
	
	public void input(Input input) {
		if(input.isMousePressed(GLFW.GLFW_MOUSE_BUTTON_1)) {
			Field hover = this.getFieldOnScreen(input.getMouseX(), input.getMouseY());
			
			if(this.selected == null) {
				// Wenn weiﬂ dran ist kannst du keine weiﬂen Felder markieren
				if(this.currentPlayer.equals(PieceType.WHITE) && !hover.getPiece().isWhite()) {
					return;
				}

				// Wenn schwarz dran ist kannst du keine schwarzen Felder markieren
				if(this.currentPlayer.equals(PieceType.BLACK) && !hover.getPiece().isBlack()) {
					return;
				}
				
				if(hover.getPiece() != null) {
					this.selected = hover;
				}
			} else {
				List<Field> possible = this.selected.getPiece().getPossibleFields(this.fields);
				if(!possible.isEmpty()) {
					if(possible.contains(hover)) {
						this.moveTo(this.selected, hover);
					} else {
						System.out.println("Ung¸ltiges Feld! - Bitte w‰hle ein anderes.");
					}
				} else {
					this.endGame();
				}
			}
		}
		
		if(input.isMousePressed(GLFW.GLFW_MOUSE_BUTTON_2)) {
			this.selected = null;
		}
	}
	
	public void update() {
		if(this.last != null) {
			
		}
	}
	
	public void render(ImmediateRenderer renderer, float partialTicks) {
		renderer.texture(Resources.textures().chessBackground, this.x - 128, this.y - 128, this.GRID_WIDTH + 256, this.GRID_HEIGHT + 256, 0xFFFFFFFF);
		renderer.rect(this.x - 32, this.y - 32, this.x + this.GRID_WIDTH + 32, this.y + this.GRID_WIDTH + 32, 0xFF6ab04c);
		renderer.rect(this.x - 16, this.y - 16, this.x + this.GRID_WIDTH + 16, this.y + this.GRID_WIDTH + 16, 0xFF52873B);
		
		// Render Tile
		for(int x = 0; x < 8; x++) {
			for(int y = 0; y < 8; y++) {
				Field field = this.fields[x][y];
				
				renderer.rect(this.x + field.getX(), this.y + field.getY(), this.x + field.getX() + this.TILE_WIDTH, this.y + field.getY() + this.TILE_HEIGHT, field.isWhite() ? 0xFFd2dae2 : 0xFF30363D);
				
				// Render Piece
				if(field.getPiece() != null) {
					renderer.texture(Resources.textures().chessSprites[field.getPiece().getTextureID()], this.x + field.getX() + 7, this.y + field.getY() + 7, 50, 50, 0xFFFFFFFF);
				}
			}
		}
		
		// Render Selected Tile
		if(this.selected != null) {
			renderer.lineWidth(4.0F);
			renderer.rect(ShapeType.LINE, this.x + this.selected.getX(), this.y + this.selected.getY(), this.x + this.selected.getX() + this.TILE_WIDTH, this.y + this.selected.getY() + this.TILE_HEIGHT, 0xFF6ab04c);
			
			for(Field f : this.selected.getPiece().getPossibleFields(this.fields)) {
				renderer.rect(this.x + f.getX(), this.y + f.getY(), this.x + f.getX() + this.TILE_WIDTH, this.y + f.getY() + this.TILE_HEIGHT, f.isWhite() ? 0xFF6ab04c : 0xFF52873B);
			}
		}
		
		renderer.drawString(Resources.fonts().main, "Spieler: " + this.currentPlayer.toString(), 5, 5, 0xFFFFFFFF);
		renderer.drawString(Resources.fonts().main, "Selektierte Feldfarbe: " + (this.selected != null ? this.selected.isWhite() : null), 5, 30, 0xFFFFFFFF);
		
		if(!this.whiteThrowed.isEmpty() || !this.blackThrowed.isEmpty()) {
			renderer.drawString(Resources.fonts().main, "Weiﬂe Geworfen: ", 5, 120, 0xFFFFFFFF);

			int index = 1;
			for(Piece wPiece : this.whiteThrowed) {
				renderer.drawString(Resources.fonts().main, "-" + wPiece.toString(), 5, 120 + (index * 30), 0xFFFFFFFF);	
				index++;
			}
			
			index += 2;
			
			renderer.drawString(Resources.fonts().main, "Schwarze Geworfen: ", 5, 120 + (index * 30), 0xFFFFFFFF);
			index++;
			for(Piece bPiece : this.blackThrowed) {
				renderer.drawString(Resources.fonts().main, "-" + bPiece.toString(), 5, 120 + (index * 30), 0xFFFFFFFF);	
				index++;
			}
		}
		
		// Animation when Piece move
		if(this.last != null) {
//			renderer.texture(Resources.textures().chessSprites[this.lastTextureID], this.x + this.last.xAsFloat() + 7, this.y + this.last.yAsFloat() + 7, 50, 50, 0xFFFFFF00);
		}
	}
	
	private Field getFieldOnScreen(double posX, double posY) {
		if(posX >= this.x && posX <= this.x + this.GRID_WIDTH && posY >= this.y && posY <= this.y + this.GRID_HEIGHT) {
			for(int x = 0; x < 8; x++) {
				for(int y = 0; y < 8; y++) {
					Field field = this.fields[x][y];
					
					if(posX >= (this.x + field.getX()) && posX <= (this.x + field.getX() + this.TILE_WIDTH)) {
						if(posY >= (this.y + field.getY()) && posY <= (this.y + field.getY() + this.TILE_HEIGHT)) {
							return field;
						}
					}
				}
			}
		}
		return null;
	}
	
	private void moveTo(Field from, Field to) {
		if(from != null && from.getPiece() != null) {
			Piece pFrom = from.getPiece();
			pFrom.moves++;
			
			if(to.getPiece() != null) {
				if(to.getPiece().isWhite()) {
					this.whiteThrowed.add(to.getPiece());
				} else {
					this.blackThrowed.add(to.getPiece());
				}
			}
			
			from.setPiece(null);
//			pFrom.setParent(to);
			to.setPiece(pFrom);
			
			this.last = new Vector2d(from.getX(), from.getY());
			this.lastTextureID = pFrom.getTextureID();
		}
		this.endTurn();
	}
	
	private void endTurn() {
		this.currentPlayer = this.currentPlayer.equals(PieceType.BLACK) ? PieceType.WHITE : PieceType.BLACK;
		this.selected = null;
	}
	
	private void endGame() {
		System.out.println("Das Spiel ist vorbei!");
	}
	
	public void setupCharacters() {
		this.clearField();
		
		// Schwarze Figuren
		this.fields[0][0].setPiece(new Turm(this.fields[0][0], 6));
		this.fields[1][0].setPiece(new Springer(this.fields[1][0], 2));
		this.fields[2][0].setPiece(new Lauefer(this.fields[2][0], 4));
		this.fields[3][0].setPiece(new Dame(this.fields[3][0], 8));
		this.fields[4][0].setPiece(new Koenig(this.fields[4][0], 10));
		this.fields[5][0].setPiece(new Lauefer(this.fields[5][0], 4));
		this.fields[6][0].setPiece(new Springer(this.fields[6][0], 2));
		this.fields[7][0].setPiece(new Turm(this.fields[7][0], 6));
		
		for(int i = 0; i < 8; i++) {
			Field field = this.fields[i][1];
			field.setPiece(new Bauer(field, 0));
		}
		
		// Weiﬂe Figuren
		this.fields[0][7].setPiece(new Turm(this.fields[0][7], 7));
		this.fields[1][7].setPiece(new Springer(this.fields[1][7], 3));
		this.fields[2][7].setPiece(new Lauefer(this.fields[2][7], 5));
		this.fields[3][7].setPiece(new Dame(this.fields[3][7], 9));
		this.fields[4][7].setPiece(new Koenig(this.fields[4][7], 11));
		this.fields[5][7].setPiece(new Lauefer(this.fields[5][7], 5));
		this.fields[6][7].setPiece(new Springer(this.fields[6][7], 3));
		this.fields[7][7].setPiece(new Turm(this.fields[7][7], 7));
		
		for(int i = 0; i < 8; i++) {
			Field field = this.fields[i][6];
			field.setPiece(new Bauer(field, 1));
		}
	}
	
	private void clearField() {
		for(int x = 0; x < this.fields.length; x++) {
			for(int y = 0; y < this.fields[x].length; y++) {
				this.fields[x][y].setPiece(null);
			}
		}
	}
}

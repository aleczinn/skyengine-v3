package de.games.chess.internal;

import de.games.chess.piece.Piece;

public class Field {

	private float x;
	private float y;
	
	private Piece piece;
	
	private boolean white;
	
	public Field(float posX, float posY, boolean white) {
		this.x = posX;
		this.y = posY;
		this.white = white;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public Piece getPiece() {
		return piece;
	}
	
	public void setPiece(Piece piece) {
		this.piece = piece;
		if(piece != null) {
			this.piece.setParent(this);	
		}
	}
	
	public boolean isWhite() {
		return white;
	}
	
	public boolean isBlack() {
		return !this.white;
	}
}

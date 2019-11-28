package de.games.chess.piece;

import java.util.List;

import de.games.chess.internal.Field;

public abstract class Piece {

	private Field parent;
	
	private int textureID;
	private boolean white;
	
	public int moves = 0;
	
	public Piece(Field parent, int textureID) {
		this.parent = parent;
		this.textureID = textureID;
		this.white = textureID % 2 == 1;
	}
	
	public abstract List<Field> getPossibleFields(Field[][] field);
	
	public Field getParent() {
		return parent;
	}
	
	public void setParent(Field parent) {
		this.parent = parent;
	}
	
	public int getTextureID() {
		return textureID;
	}
	
	public boolean isWhite() {
		return this.white;
	}
	
	public boolean isBlack() {
		return !this.white;
	}
	
	public Field getFieldAt(Field[][] field, int x, int y) {
		if(x < 8 && y < 8 && (x >= 0 && y >= 0)) {
			if(field[x][y].getPiece() != null) {
				int cThis = this.isWhite() ? 0 : 1;
				int cField = field[x][y].getPiece().isWhite() ? 0 : 1;
				
				if(cThis == cField) {
					return null;
				}
			}
			
			return field[x][y];
		}
		return null;
	}
}

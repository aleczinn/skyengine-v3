package de.games.chess.piece.pieces;

import java.util.ArrayList;
import java.util.List;

import de.games.chess.internal.Field;
import de.games.chess.piece.Piece;

public class Dame extends Piece {
	
	public Dame(Field parent, int textureID) {
		super(parent, textureID);
	}

	@Override
	public List<Field> getPossibleFields(Field[][] map) {
		List<Field> fields = new ArrayList<Field>();
		
			int x = (int) (this.getParent().getX() / 64);
			int y = (int) (this.getParent().getY() / 64);
			
			// Oben - Rechts
			for(int i = 1; i < 8; i++) {
				Field oR = this.getFieldAt(map, x + i, y - i);
				if(this.check(fields, oR)) {
					break;
				}
			}
			
			// Oben - Links
			for(int i = 1; i < 8; i++) {
				Field oL = this.getFieldAt(map, x - i, y - i);
				if(this.check(fields, oL)) {
					break;
				}
			}
			
			// Unten - Rechts
			for(int i = 1; i < 8; i++) {
				Field uR = this.getFieldAt(map, x + i, y + i);
				if(this.check(fields, uR)) {
					break;
				}
			}
			
			// Unten - Links
			for(int i = 1; i < 8; i++) {
				Field uL = this.getFieldAt(map, x - i, y + i);
				if(this.check(fields, uL)) {
					break;
				}
			}
			
			// TURM
			
			// Oben
			for(int i = 1; i < 8; i++) {
				Field oR = this.getFieldAt(map, x, y - i);
				if(this.check(fields, oR)) {
					break;
				}
			}
			
			// Unten
			for(int i = 1; i < 8; i++) {
				Field oL = this.getFieldAt(map, x, y + i);
				if(this.check(fields, oL)) {
					break;
				}
			}
			
			// Links
			for(int i = 1; i < 8; i++) {
				Field uR = this.getFieldAt(map, x - i, y);
				if(this.check(fields, uR)) {
					break;
				}
			}
			
			// Rechts
			for(int i = 1; i < 8; i++) {
				Field uL = this.getFieldAt(map, x + i, y);
				if(this.check(fields, uL)) {
					break;
				}
			}
		return fields;
	}
	
	private boolean check(List<Field> fields, Field field) {
		if(field != null) {
			fields.add(field);
			if(field.getPiece() != null) {
				return true;
			}
		} else {
			return true;
		}
		return false;
	}
}

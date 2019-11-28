package de.games.chess.piece.pieces;

import java.util.ArrayList;
import java.util.List;

import de.games.chess.internal.Field;
import de.games.chess.piece.Piece;

public class Turm extends Piece {

	public Turm(Field parent, int textureID) {
		super(parent, textureID);
	}

	@Override
	public List<Field> getPossibleFields(Field[][] field) {
		List<Field> fields = new ArrayList<Field>();
		
		int x = (int) (this.getParent().getX() / 64);
		int y = (int) (this.getParent().getY() / 64);
		
		// Oben
		for(int i = 1; i < 8; i++) {
			Field oR = this.getFieldAt(field, x, y - i);
			if(oR != null) {
				fields.add(oR);
				
				if(oR.getPiece() != null) {
					break;
				}
			} else {
				break;	
			}
		}
		
		// Unten
		for(int i = 1; i < 8; i++) {
			Field oL = this.getFieldAt(field, x, y + i);
			if(oL != null) {
				fields.add(oL);
				
				if(oL.getPiece() != null) {
					break;
				}	
			} else {
				break;	
			}
		}
		
		// Links
		for(int i = 1; i < 8; i++) {
			Field uR = this.getFieldAt(field, x - i, y);
			if(uR != null) {
				fields.add(uR);
				
				if(uR.getPiece() != null) {
					break;
				}
			} else {
				break;	
			}
		}
		
		// Rechts
		for(int i = 1; i < 8; i++) {
			Field uL = this.getFieldAt(field, x + i, y);
			if(uL != null) {
				fields.add(uL);
				
				if(uL.getPiece() != null) {
					break;
				}
			} else {
				break;	
			}
		}
		
		return fields;
	}
}

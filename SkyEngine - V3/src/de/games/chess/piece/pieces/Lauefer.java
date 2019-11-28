package de.games.chess.piece.pieces;

import java.util.ArrayList;
import java.util.List;

import de.games.chess.internal.Field;
import de.games.chess.piece.Piece;

public class Lauefer extends Piece {

	public Lauefer(Field parent, int textureID) {
		super(parent, textureID);
	}

	@Override
	public List<Field> getPossibleFields(Field[][] map) {
		List<Field> fields = new ArrayList<Field>();
		
		int x = (int) (this.getParent().getX() / 64);
		int y = (int) (this.getParent().getY() / 64);
			
		// Oben Rechts
		for(int i = 1; i < 8; i++) {
			Field oR = this.getFieldAt(map, x + i, y - i);
			if(oR != null) {
				fields.add(oR);
				
				if(oR.getPiece() != null) {
					break;
				}
			} else {
				break;	
			}
		}
		
		// Oben Links
		for(int i = 1; i < 8; i++) {
			Field oL = this.getFieldAt(map, x - i, y - i);
			if(oL != null) {
				fields.add(oL);
				
				if(oL.getPiece() != null) {
					break;
				}	
			} else {
				break;	
			}
		}
		
		// Unten Rechts
		for(int i = 1; i < 8; i++) {
			Field uR = this.getFieldAt(map, x + i, y + i);
			if(uR != null) {
				fields.add(uR);
				
				if(uR.getPiece() != null) {
					break;
				}
			} else {
				break;	
			}
		}
		
		// Unten Links
		for(int i = 1; i < 8; i++) {
			Field uL = this.getFieldAt(map, x - i, y + i);
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

package de.games.chess.piece.pieces;

import java.util.ArrayList;
import java.util.List;

import de.games.chess.internal.Field;
import de.games.chess.piece.Piece;

public class Bauer extends Piece {

	public Bauer(Field field, int textureID) {
		super(field, textureID);
	}

	@Override
	public List<Field> getPossibleFields(Field[][] field) {
		List<Field> fields = new ArrayList<Field>();
		
		int x = (int) (this.getParent().getX() / 64);
		int y = (int) (this.getParent().getY() / 64);
		
		boolean white = this.isWhite();
		
		// Gradeaus 1
		Field forwardOne = this.getFieldAt(field, x, white ? y - 1 : y + 1);
		if(forwardOne != null) {
			fields.add(forwardOne);
		}
		
		// Gradeaus 2 
		Field forwardTwo = this.getFieldAt(field, x, white ? y - 2 : y + 2);
		if(this.moves == 0) {
			if(forwardTwo != null) {
				fields.add(forwardTwo);
			}
		}
		
		// Gradeaus Links
		Field forwardLeft = this.getFieldAt(field, x - 1, white ? y - 1 : y + 1);
		if(forwardLeft != null && forwardLeft.getPiece() != null) {
			fields.add(forwardLeft);
		}

		// Gradeaus Rechts
		Field forwardRight = this.getFieldAt(field, x + 1, white ? y - 1 : y + 1);
		if(forwardRight != null && forwardRight.getPiece() != null) {
			fields.add(forwardRight);
		}
		return fields;
	}
}

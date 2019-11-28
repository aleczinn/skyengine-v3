package de.games.chess.piece.pieces;

import java.util.ArrayList;
import java.util.List;

import de.games.chess.internal.Field;
import de.games.chess.piece.Piece;

public class Springer extends Piece {

	public Springer(Field parent, int textureID) {
		super(parent, textureID);
	}

	@Override
	public List<Field> getPossibleFields(Field[][] field) {
		List<Field> fields = new ArrayList<Field>();
		
		int x = (int) (this.getParent().getX() / 64);
		int y = (int) (this.getParent().getY() / 64);
		
		// Oben
		Field upLeft = this.getFieldAt(field, x - 1, y - 2);
		if(upLeft != null) {
			fields.add(upLeft);
		}
		
		Field upRight = this.getFieldAt(field, x + 1, y - 2);
		if(upRight != null) {
			fields.add(upRight);
		}
		
		// Links
		Field leftUp = this.getFieldAt(field, x - 2, y - 1);
		if(leftUp != null) {
			fields.add(leftUp);
		}
		
		Field leftDown = this.getFieldAt(field, x - 2, y + 1);
		if(leftDown != null) {
			fields.add(leftDown);
		}

		// Rechts
		Field rightUp = this.getFieldAt(field, x + 2, y - 1);
		if(rightUp != null) {
			fields.add(rightUp);
		}
		
		Field rightDown = this.getFieldAt(field, x + 2, y + 1);
		if(rightDown != null) {
			fields.add(rightDown);
		}
		
		// Unten
		Field downLeft = this.getFieldAt(field, x - 1, y + 2);
		if(downLeft != null) {
			fields.add(downLeft);
		}
		
		Field downRight = this.getFieldAt(field, x + 1, y + 2);
		if(downRight != null) {
			fields.add(downRight);
		}
		return fields;
	}
}

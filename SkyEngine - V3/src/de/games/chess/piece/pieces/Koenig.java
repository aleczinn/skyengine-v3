package de.games.chess.piece.pieces;

import java.util.ArrayList;
import java.util.List;

import de.games.chess.internal.Field;
import de.games.chess.piece.Piece;

public class Koenig extends Piece {

	public Koenig(Field parent, int textureID) {
		super(parent, textureID);
	}

	@Override
	public List<Field> getPossibleFields(Field[][] map) {
		List<Field> fields = new ArrayList<Field>();
		
		int x = (int) (this.getParent().getX() / 64);
		int y = (int) (this.getParent().getY() / 64);
		
		for(int mx = -1; mx < 2; mx++) {
			for(int my = -1; my < 2; my++) {
				Field field = this.getFieldAt(map, x + mx, y + my);
				
				if(field != this.getParent() && field != null) {
					fields.add(field);
				}
			}
		}
		return fields;
	}
}

package de.skyengine.graphics.font;

public class Glyph {

	public final int x;
	public final int y;
	
	public final float width;
	public final float height;
	
	public final float advance;
	
	public Glyph(int x, int y, float width, float height, float advance) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.advance = advance;
	}
	
	public String toString() {
		return "X: " + this.x + " Y: " + this.y + " Width: " + this.width + " Height: " + this.height + " Advance: " + this.advance;
	}
}

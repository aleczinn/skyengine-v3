package de.skyengine.utils.math;

public class Vector2i {

	public int x;
	public int y;
	
	public Vector2i() {}
	
	public Vector2i(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2i(Vector2i vector) {
		this.x = vector.x;
		this.y = vector.y;
	}
	
	public Vector2i set(int x, int y) {
		this.x = x;
		this.y = y;
		return this;
	}
	
	public Vector2i set(Vector2i vector) {
		this.x = vector.x;
		this.y = vector.y;
		return this;
	}
	
	@Override
	public String toString() {
		return "Vector2i {" + "x=" + x + ", y=" + y + "}";
	}
}

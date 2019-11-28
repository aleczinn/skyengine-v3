package de.skyengine.utils.math;

import de.skyengine.utils.MathUtils;

public class Vector4f {

	public float x;
	public float y;
	public float z;
	public float w;
	
	public Vector4f() {}
	
	public Vector4f(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public Vector4f(Vector4f vector) {
		this.x = vector.x;
		this.y = vector.y;
		this.z = vector.z;
		this.w = vector.w;
	}
	
	public Vector4f set(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
		return this;
	}
	
	public Vector4f set(Vector4f vector) {
		this.x = vector.x;
		this.y = vector.y;
		this.z = vector.z;
		this.w = vector.w;
		return this;
	}
	
    /**
     * Returns the length of this vector.
     * @return the length of this vector
     */
	public double length() {
		return StrictMath.sqrt(this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w);
	}
	
    /**
     * Returns the squared length of this vector.
     * @return the squared length of this vector
     */
	public double lengthSquared() {
		return this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w;
	}
	
	public void normalize() {
		double lengthSq = this.lengthSquared();
		
		if(lengthSq > MathUtils.EPSILON_SQ) {
			double invLength = 1.0D / StrictMath.sqrt(lengthSq);
			this.x *= invLength;
			this.y *= invLength;
		}
	}
	
	public double distance(double x, double y) {
		double pX = x - this.x;
		double pY = y - this.y;
		return Math.sqrt(pX * pX + pY * pY);
	}
	
	public double distance(Vector2d vector) {
		double pX = vector.x - this.x;
		double pY = vector.y - this.y;
		return Math.sqrt(pX * pX + pY * pY);
	}
}

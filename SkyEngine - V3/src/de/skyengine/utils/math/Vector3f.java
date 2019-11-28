package de.skyengine.utils.math;

import de.skyengine.utils.MathUtils;

public class Vector3f {

	public float x;
	public float y;
	public float z;
	
	public Vector3f() {}
	
	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3f(Vector3f vector) {
		this.x = vector.x;
		this.y = vector.y;
		this.z = vector.z;
	}
	
	public Vector3f set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}
	
	public Vector3f set(Vector3f vector) {
		this.x = vector.x;
		this.y = vector.y;
		this.z = vector.z;
		return this;
	}
	
    /**
     * Returns the length of this vector.
     * @return the length of this vector
     */
	public double length() {
		return StrictMath.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
	}
	
    /**
     * Returns the squared length of this vector.
     * @return the squared length of this vector
     */
	public double lengthSquared() {
		return this.x * this.x + this.y * this.y + this.z * this.z;
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

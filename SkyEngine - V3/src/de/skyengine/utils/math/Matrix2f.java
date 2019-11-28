package de.skyengine.utils.math;

public class Matrix2f {

	public float m00, m01;
	public float m10, m11;
	
	public Matrix2f() {}
	
	public Matrix2f(float radians) {
		this.set(radians);
	}
	
	public Matrix2f(float a, float b, float c, float d) {
		this.set(a, b, c, d);
	}
	
	public Matrix2f(Matrix2f matrix) {
		this.set(matrix);
	}
	
	public void set(float radians) {
		float c = (float) StrictMath.cos(radians);
		float s = (float) StrictMath.sin(radians);

		this.m00 = c;
		this.m01 = -s;
		this.m10 = s;
		this.m11 = c;
	}
	
	public void set(float a, float b, float c, float d) {
		this.m00 = a;
		this.m01 = b;
		this.m10 = c;
		this.m11 = d;
	}
	
	public void set(Matrix2f matrix) {
		this.m00 = matrix.m00;
		this.m01 = matrix.m01;
		this.m10 = matrix.m10;
		this.m11 = matrix.m11;
	}
	
	/**
	 * Sets out to the transformation of v by this matrix.
	 */
	public Vector2f multiply(Vector2f vector, Vector2f out) {
		return multiply(vector.x, vector.y, out);
	}
	
	public Vector2d multiply(Vector2d vector, Vector2d out) {
		return multiply(vector.xAsFloat(), vector.yAsFloat(), out);
	}
	
	/**
	 * Returns a new vector that is the transformation of v by this matrix.
	 */
	public Vector2f multiply(Vector2f v) {
		return multiply(v.x, v.y, new Vector2f());
	}
	
	public Vector2f multiply(Vector2d v) {
		return multiply(v.xAsFloat(), v.yAsFloat(), new Vector2f());
	}
	
	/**
	 * Sets out the to transformation of {x,y} by this matrix.
	 */
	public Vector2f multiply(float x, float y, Vector2f out) {
		out.x = m00 * x + m01 * y;
		out.y = m10 * x + m11 * y;
		return out;
	}
	
	public Vector2d multiply(float x, float y, Vector2d out) {
		out.x = m00 * x + m01 * y;
		out.y = m10 * x + m11 * y;
		return out;
	}
	
	/**
	 * Multiplies this matrix by x.
	 */
	public void multiplyIntern(Matrix2f matrix) {
		this.set(this.m00 * matrix.m00 + this.m01 * matrix.m10, this.m00 * matrix.m01 + this.m01 * matrix.m11, this.m10 * matrix.m00 + this.m11 * matrix.m10, this.m10 * matrix.m01 + this.m11 * matrix.m11);
	}
	
	/**
	 * Transforms v by this matrix.
	 */
	public Vector2f multiplyIntern(Vector2f vector) {
		return multiply(vector.x, vector.y, vector);
	}
	
	public Vector2d multiplyIntern(Vector2d vector) {
		return multiply(vector.xAsFloat(), vector.yAsFloat(), vector);
	}
	
	/**
	 * Sets out to the transpose of this matrix.
	 */
	public Matrix2f transpose(Matrix2f out) {
		out.m00 = m00;
		out.m01 = m10;
		out.m10 = m01;
		out.m11 = m11;
		return out;
	}
	
	/**
	 * Returns a new matrix that is the transpose of this matrix.
	 */
	public Matrix2f transpose() {
		return transpose(new Matrix2f());
	}
}

package de.skyengine.utils.math;

import java.math.BigDecimal;

import de.skyengine.utils.MathUtils;

public class Vector2d {

	public double x;
	public double y;
	
	public Vector2d() {}
	
	public Vector2d(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2d(Vector2d vector) {
		this.x = vector.x;
		this.y = vector.y;
	}
	
	public Vector2d set(double x, double y) {
		this.x = x;
		this.y = y;
		return this;
	}
	
	public Vector2d set(Vector2d vector) {
		this.x = vector.x;
		this.y = vector.y;
		return this;
	}
	
	public Vector2d addIntern(double value) {
		return this.add(value, value, this);
	}
	
	public Vector2d addIntern(double x, double y) {
		return this.add(x, y, this);
	}
	
	public Vector2d addIntern(Vector2d vector) {
		return this.add(vector, this);
	}
	
	public Vector2d add(double x, double y) {
		return this.add(x, y, new Vector2d());
	}
	
	public Vector2d add(Vector2d vector) {
		return this.add(vector, new Vector2d());
	}
	
	public Vector2d add(double x, double y, Vector2d out) {
		out.x = this.x + x;
		out.y = this.y + y;
		return out;
	}
	
	public Vector2d add(Vector2d vector, Vector2d out) {
		out.x = this.x + vector.x;
		out.y = this.y + vector.y;
		return out;
	}
	
	public Vector2d sub(double x, double y) {
		return this.sub(new Vector2d(x, y), new Vector2d());
	}
	
	public Vector2d sub(Vector2d vector) {
		return this.sub(vector, new Vector2d());
	}
	
	public Vector2d sub(Vector2d vector, Vector2d out) {
		out.x = this.x - vector.x;
		out.y = this.y - vector.y;
		return out;
	}
	
	public Vector2d subIntern(Vector2d vector) {
		return this.sub(vector, this);
	}
	
	public Vector2d multiplyIntern(Vector2d vector) {
		return this.multiply(vector, this);
	}
	
	public Vector2d multiplyIntern(double value) {
		return this.multiply(value, this);
	}
	
	public Vector2d multiply(Vector2d vector, Vector2d out) {
		out.x = this.x * vector.x;
		out.y = this.y * vector.y;
		return out;
	}
	
	public  Vector2d multiply(double value, Vector2d out) {
		out.x = this.x * value;
		out.y = this.y * value;
		return out;
	}
	
	public Vector2d multiply(double value) {
		return this.multiply(value, new Vector2d());
	}
	
	public Vector2d divideIntern(double value) {
		return this.divide(value, this);
	}
	
	public Vector2d divideIntern(double x, double y) {
		return this.divide(x, y, this);
	}
	
	public Vector2d divideIntern(Vector2d vector) {
		return this.divide(vector, this);
	}
	
	public Vector2d divide(double value, Vector2d out) {
		return this.divide(value, value, out);
	}
	
	public Vector2d divide(double x, double y, Vector2d out) {
		out.x = this.x / x;
		out.y = this.y / y;
		return out;
	}
	
	public Vector2d divide(Vector2d vector, Vector2d out) {
		out.x = this.x / vector.x;
		out.y = this.y / vector.y;
		return out;
	}
	
    /**
     * Returns the length of this vector.
     * @return the length of this vector
     */
	public double length() {
		return StrictMath.sqrt(this.x * this.x + this.y * this.y);
	}
	
    /**
     * Returns the squared length of this vector.
     * @return the squared length of this vector
     */
	public double lengthSquared() {
		return this.x * this.x + this.y * this.y;
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
	
	public double cross(Vector2d vector) {
		return this.x * vector.y - this.y * this.x;
	}
	
    public void cross(double a, Vector2d vector) {
        this.x = -a * vector.y;
        this.y = a * vector.x;
    }
    
	public static Vector2d cross(Vector2d v, double a, Vector2d out) {
		out.x = v.y * a;
		out.y = v.x * -a;
		return out;
	}

	public static Vector2d cross(double a, Vector2d v, Vector2d out) {
		out.x = v.y * -a;
		out.y = v.x * a;
		return out;
	}
	
	public static double cross(Vector2d a, Vector2d b) {
		return a.x * b.y - a.y * b.x;
	}
    
	public double dot(Vector2d vector) {
		 return this.x * vector.x + this.y * vector.y;
	}
	
	public static double dot(Vector2d a, Vector2d b) {
		return a.x * b.x + a.y * b.y;
	}
	
	public Vector2d addsIntern(Vector2d vector, double value) {
		return this.adds(vector, value, this);
	}
	
	public Vector2d adds(Vector2d vector, double value) {
		return this.adds(vector, value, new Vector2d());
	}
	
	public Vector2d adds(Vector2d vector, double value, Vector2d out) {
		out.x = this.x + vector.x * value;
		out.y = this.y + vector.y * value;
		return out;
	}
	
	public Vector2d negIntern() {
		return this.neg(this);
	}
	
	public Vector2d neg() {
		return this.neg(new Vector2d());
	}
	
	public Vector2d neg(Vector2d out) {
		out.x = -x;
		out.y = -y;
		return out; 
	}
	
	public Vector2d perp() {
		return this.set(-y, x);
	}
	
	public Vector2d round(int n) {
		BigDecimal bX = new BigDecimal(this.x);
		this.x = bX.setScale(n, BigDecimal.ROUND_HALF_UP).doubleValue();
		
		BigDecimal bY = new BigDecimal(this.y);
		this.y = bY.setScale(n, BigDecimal.ROUND_HALF_UP).doubleValue();
		return this;
	}
	
	public float xAsFloat() {
		return (float) this.x;
	}
	
	public float yAsFloat() {
		return (float) this.y;
	}
	
	public static Vector2d[] arrayOf(int length) {
		Vector2d[] array = new Vector2d[length];

		while (--length >= 0) {
			array[length] = new Vector2d();
		}
		return array;
	}
	

	public static double distanceSq(Vector2d a, Vector2d b) {
		double dx = a.x - b.x;
		double dy = a.y - b.y;
		return dx * dx + dy * dy;
	}
	
	@Override
	public String toString() {
		return "Vector2d {" + "x=" + x + ", y=" + y + "}";
	}
}

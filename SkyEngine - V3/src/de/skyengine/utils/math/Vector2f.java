package de.skyengine.utils.math;

import java.math.BigDecimal;

import de.skyengine.utils.MathUtils;

public class Vector2f {

	public float x;
	public float y;
	
	public static Vector2f ZERO = new Vector2f(0, 0);
	
	public Vector2f() {}
	
	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2f(Vector2f vector) {
		this.x = vector.x;
		this.y = vector.y;
	}
	
	public Vector2f set(float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}
	
	public Vector2f set(Vector2f vector) {
		this.x = vector.x;
		this.y = vector.y;
		return this;
	}
	
	public Vector2f addIntern(float value) {
		return this.add(value, value, this);
	}
	
	public Vector2f addIntern(float x, float y) {
		return this.add(x, y, this);
	}
	
	public Vector2f addIntern(Vector2f vector) {
		return this.add(vector, this);
	}
	
	public Vector2f add(float x, float y) {
		return this.add(x, y, new Vector2f());
	}
	
	public Vector2f add(Vector2f vector) {
		return this.add(vector, new Vector2f());
	}
	
	public Vector2f add(float x, float y, Vector2f out) {
		out.x = this.x + x;
		out.y = this.y + y;
		return out;
	}
	
	public Vector2f add(Vector2f vector, Vector2f out) {
		out.x = this.x + vector.x;
		out.y = this.y + vector.y;
		return out;
	}
	
	public Vector2f sub(float x, float y) {
		return this.sub(new Vector2f(x, y), new Vector2f());
	}
	
	public Vector2f sub(Vector2f vector) {
		return this.sub(vector, new Vector2f());
	}
	
	public Vector2f sub(Vector2f vector, Vector2f out) {
		out.x = this.x - vector.x;
		out.y = this.y - vector.y;
		return out;
	}
	
	public Vector2f subIntern(Vector2f vector) {
		return this.sub(vector, this);
	}
	
	public Vector2f multiplyIntern(Vector2f vector) {
		return this.multiply(vector, this);
	}
	
	public Vector2f multiplyIntern(float value) {
		return this.multiply(value, this);
	}
	
	public Vector2f multiply(Vector2f vector, Vector2f out) {
		out.x = this.x * vector.x;
		out.y = this.y * vector.y;
		return out;
	}
	
	public  Vector2f multiply(float value, Vector2f out) {
		out.x = this.x * value;
		out.y = this.y * value;
		return out;
	}
	
	public Vector2f multiply(float value) {
		return this.multiply(value, new Vector2f());
	}
	
	public Vector2f divideIntern(float value) {
		return this.divide(value, this);
	}
	
	public Vector2f divideIntern(float x, float y) {
		return this.divide(x, y, this);
	}
	
	public Vector2f divideIntern(Vector2f vector) {
		return this.divide(vector, this);
	}
	
	public Vector2f divide(float value, Vector2f out) {
		return this.divide(value, value, out);
	}
	
	public Vector2f divide(float x, float y, Vector2f out) {
		out.x = this.x / x;
		out.y = this.y / y;
		return out;
	}
	
	public Vector2f divide(Vector2f vector, Vector2f out) {
		out.x = this.x / vector.x;
		out.y = this.y / vector.y;
		return out;
	}
	
    /**
     * Returns the length of this vector.
     * @return the length of this vector
     */
	public float length() {
		return (float) StrictMath.sqrt(this.x * this.x + this.y * this.y);
	}
	
    /**
     * Returns the squared length of this vector.
     * @return the squared length of this vector
     */
	public float lengthSquared() {
		return this.x * this.x + this.y * this.y;
	}
	
	public void normalize() {
		float lengthSq = this.lengthSquared();
		
		if(lengthSq > MathUtils.EPSILON_SQ) {
			float invLength = (float) (1.0D / StrictMath.sqrt(lengthSq));
			this.x *= invLength;
			this.y *= invLength;
		}
	}
	
	public float distance(float x, float y) {
		float pX = x - this.x;
		float pY = y - this.y;
		return (float) Math.sqrt(pX * pX + pY * pY);
	}
	
	public float distance(Vector2f vector) {
		float pX = vector.x - this.x;
		float pY = vector.y - this.y;
		return (float) Math.sqrt(pX * pX + pY * pY);
	}
	
	public float cross(Vector2f vector) {
		return this.x * vector.y - this.y * this.x;
	}
	
    public void cross(float a, Vector2f vector) {
        this.x = -a * vector.y;
        this.y = a * vector.x;
    }
    
	public static Vector2f cross(Vector2f v, float a, Vector2f out) {
		out.x = v.y * a;
		out.y = v.x * -a;
		return out;
	}

	public static Vector2f cross(float a, Vector2f v, Vector2f out) {
		out.x = v.y * -a;
		out.y = v.x * a;
		return out;
	}
	
	public static float cross(Vector2f a, Vector2f b) {
		return a.x * b.y - a.y * b.x;
	}
    
	public float dot(Vector2f vector) {
		 return this.x * vector.x + this.y * vector.y;
	}
	
	public static float dot(Vector2f a, Vector2f b) {
		return a.x * b.x + a.y * b.y;
	}
	
	public Vector2f addsIntern(Vector2f vector, float value) {
		return this.adds(vector, value, this);
	}
	
	public Vector2f adds(Vector2f vector, float value) {
		return this.adds(vector, value, new Vector2f());
	}
	
	public Vector2f adds(Vector2f vector, float value, Vector2f out) {
		out.x = this.x + vector.x * value;
		out.y = this.y + vector.y * value;
		return out;
	}
	
	public Vector2f negIntern() {
		return this.neg(this);
	}
	
	public Vector2f neg() {
		return this.neg(new Vector2f());
	}
	
	public Vector2f neg(Vector2f out) {
		out.x = -x;
		out.y = -y;
		return out; 
	}
	
	public Vector2f perp() {
		return this.set(-y, x);
	}
	
	public float xAsFloat() {
		return (float) this.x;
	}
	
	public float yAsFloat() {
		return (float) this.y;
	}
	
	public static Vector2f[] arrayOf(int length) {
		Vector2f[] array = new Vector2f[length];

		while (--length >= 0) {
			array[length] = new Vector2f();
		}
		return array;
	}
	
	public Vector2f round(int n) {
		BigDecimal bX = new BigDecimal(this.x);
		this.x = bX.setScale(n, BigDecimal.ROUND_HALF_UP).floatValue();
		
		BigDecimal bY = new BigDecimal(this.y);
		this.y = bY.setScale(n, BigDecimal.ROUND_HALF_UP).floatValue();
		return this;
	}
	
	public static float distanceSq(Vector2f a, Vector2f b) {
		float dx = a.x - b.x;
		float dy = a.y - b.y;
		return dx * dx + dy * dy;
	}
	
	@Override
	public String toString() {
		return "Vector2f {" + "x=" + x + ", y=" + y + "}";
	}
}

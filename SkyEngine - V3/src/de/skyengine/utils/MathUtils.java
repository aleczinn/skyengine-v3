package de.skyengine.utils;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

import de.games.physicsold.PhysicsEngine;
import de.skyengine.utils.math.Vector2d;
import de.skyengine.utils.math.Vector2f;
import de.skyengine.utils.math.Vector2i;

public class MathUtils {

	public static final float PI = 3.1415927F;
	public static final float PI2 = PI * 2.0F;
	
	public static final float E = 2.7182818f;
	
	private static final int SIN_BITS = 14; // 16KB. Adjust for accuracy.
	private static final int SIN_MASK = ~(-1 << SIN_BITS);
	private static final int SIN_COUNT = SIN_MASK + 1;
	
	private static final float radFull = PI * 2.0F;
	private static final float degFull = 360.0F;
	private static final float radToIndex = SIN_COUNT / radFull;
	private static final float degToIndex = SIN_COUNT / degFull;
	
	public static final float degreesToRadians = PI / 180.0F;
	public static final float degRad = degreesToRadians;
	
	public static ThreadLocalRandom RANDOM = ThreadLocalRandom.current();
	
	public static final double DT = 1.0D / 30.0D;
	public static final double EPSILON = 0.0001D;
	public static final double EPSILON_SQ = EPSILON * EPSILON;
	public static final double DISTANCE_ALLOWANCE = 0.05D;
	public static final double DISTANCE_CORRETION = 0.4D;
	public static final double RESTING = PhysicsEngine.GRAVITY.multiply(DT).lengthSquared() + EPSILON;
	
	public static boolean equal(double a, double b) {
		return StrictMath.abs(a - b) <= EPSILON;
	}
	
	/** Returns the sine in radians from a lookup table. */
	public static float sin (float radians) {
		return Sin.table[(int)(radians * radToIndex) & SIN_MASK];
	}

	/** Returns the cosine in radians from a lookup table. */
	public static float cos (float radians) {
		return Sin.table[(int)((radians + PI / 2) * radToIndex) & SIN_MASK];
	}
	
	/** Returns the sine in radians from a lookup table. */
	public static float sinDeg (float degrees) {
		return Sin.table[(int)(degrees * degToIndex) & SIN_MASK];
	}

	/** Returns the cosine in radians from a lookup table. */
	public static float cosDeg (float degrees) {
		return Sin.table[(int)((degrees + 90) * degToIndex) & SIN_MASK];
	}
	
	public static double round(double value, int n) {
		BigDecimal b = new BigDecimal(value);
		return b.setScale(n, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	public static float round(float value, int n) {
		BigDecimal b = new BigDecimal(value);
		return b.setScale(n, BigDecimal.ROUND_HALF_UP).floatValue();
	}
	
	private static class Sin {
		public static final float[] table = new float[SIN_COUNT];
		static {
			for (int i = 0; i < SIN_COUNT; i++)
				table[i] = (float)Math.sin((i + 0.5F) / SIN_COUNT * radFull);
			for (int i = 0; i < 360; i += 90)
				table[(int)(i * degToIndex) & SIN_MASK] = (float)Math.sin(i * degreesToRadians);
		}
	}
	
	public static byte[] floatToByteArray(float value) {
		int intBits = Float.floatToIntBits(value);
		return new byte[] { (byte) (intBits >> 24), (byte) (intBits >> 16), (byte) (intBits >> 8), (byte) (intBits) };
	}

	public static float byteArrayToFloat(byte[] bytes) {
		int intBits = bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
		return Float.intBitsToFloat(intBits);
	}
	
	// 1e3 = 1 with 3x 0 = 1000
	public static double convertNanoInMS(long nano) {
		return nano / 1e3 / 1e3;
	}
	
	public static int random(int range) {
		return RANDOM.nextInt(range + 1);
	}
	
	public static int random (int start, int end) {
		return start + RANDOM.nextInt(end - start + 1);
	}
	
	public static float random(float min, float max) {
		return min + (RANDOM.nextFloat() * (max - min));
	}
	
	public static double random(double min, double max) {
		return min + (RANDOM.nextDouble() * (max - min));
	}
	
	public static double min(double valueA, double valueB) {
		if(valueA < valueB) {
			return valueA;
		}
		if(valueB < valueA) {
			return valueB;
		}
		return valueA;
	}
	
	public static double max(double valueA, double valueB) {
		if(valueA > valueB) {
			return valueA;
		}
		if(valueB > valueA) {
			return valueB;
		}
		return valueA;
	}
	
	public static double clamp(double value, double min, double max) {
		if(min > max) throw new IllegalArgumentException("MathUtils - clamp(); - MIN is higher than MAX");
		
		if(value < min) {
			value = min;
		}
		if(value > max) {
			value = max;
		}
		return value;
	}
	
	public static Vector2d interpolate(Vector2d vector, Vector2d last, float partialTicks) {
		double x = vector.x * partialTicks + last.x * (1.0D - partialTicks);
		double y = vector.y * partialTicks + last.y * (1.0D - partialTicks);
		return new Vector2d(x, y);
	}
	
	public static Vector2f interpolate(Vector2f vector, Vector2f last, float partialTicks) {
		float x = vector.x * partialTicks + last.x * (1.0F - partialTicks);
		float y = vector.y * partialTicks + last.y * (1.0F - partialTicks);
		return new Vector2f(x, y);
	}
	
	public static Vector2i interpolatei(Vector2f vector, Vector2f last, float partialTicks) {
		int x = (int) (vector.x * partialTicks + last.x * (1.0F - partialTicks));
		int y = (int) (vector.y * partialTicks + last.y * (1.0F - partialTicks));
		return new Vector2i(x, y);
	}
	
	public static Vector2i interpolate(Vector2i vector, Vector2i last, float partialTicks) {
		int x = (int) (vector.x * partialTicks + last.x * (1.0F - partialTicks));
		int y = (int) (vector.y * partialTicks + last.y * (1.0F - partialTicks));
		return new Vector2i(x, y);
	}
	
	public static float interpolate(float value, float lastValue, float partialTicks) {
		return value * partialTicks + lastValue * (1.0F - partialTicks);
	}
	
	public static double interpolate(double value, double lastValue, float partialTicks) {
		return value * partialTicks + lastValue * (1.0D - partialTicks);
	}
	
	public static float interpolateToFloat(double value, double lastValue, float partialTicks) {
		return (float) (value * partialTicks + lastValue * (1.0D - partialTicks));
	}
	
	public static boolean intersect(float mouseX, float mouseY, float x1, float y1, float x2, float y2) {
		return (mouseX >= x1 && mouseX <= x2) && (mouseY >= y1 && mouseY <= y2);
	}
	
	public static boolean intersect(double mouseX, double mouseY, double x1, double y1, double x2, double y2) {
		return (mouseX >= x1 && mouseX <= x2) && (mouseY >= y1 && mouseY <= y2);
	}
}

package de.games.physicsold;

public class PhysicUtils {

//	public static boolean intersects(RectObject object, RectObject target) {
//		if(object.getX() < (target.getX() + target.getWidth()) && (object.getX() + object.getWidth()) > target.getX()) {
//			if(object.getY() < (target.getY() + target.getHeight()) && (object.getY() + object.getHeight()) > target.getY()) {
//				return true;
//			}
//		}
//		return false;
//	}
//	
//	/**
//	 * 
//	 * @param object
//	 * @param target
//	 * @return {0} = DONT_INTERSECT | {1} = COLLINEAR | {2, windowXIntersection, windowYIntersection} = DO_INTERSECT
//	 */
//	public static double[] intersects(LineObject object, LineObject target) {
//		double windowX = 0;
//		double windowY = 0;
//		
//		// Line A
//		double x1 = object.getX();
//		double y1 = object.getY();
//		double x2 = object.getX2();
//		double y2 = object.getY2();
//		
//		// Line B
//		double x3 = target.getX();
//		double y3 = target.getY();
//		double x4 = target.getX2();
//		double y4 = target.getY2();
//		
//		double a1, a2, b1, b2, c1, c2;
//		double r1, r2, r3, r4;
//		double denom, offset, num;
//		
//		a1 = y2 - y1;
//		b1 = x1 - x2;
//		c1 = (x2 * y1) - (x1 * y2);
//		r3 = ((a1 * x3) + (b1 * y3) + c1);
//		r4 = ((a1 * x4) + (b1 * y4) + c1);
//		
//		if((r3 != 0) && (r4 != 0) && (r3 * r4 >= 0)) return  new double[] {0}; // DONT_INTERSECT
//
//		a2 = y4 - y3; // Compute a2, b2, c2
//		b2 = x3 - x4;
//		c2 = (x4 * y3) - (x3 * y4);
//		r1 = (a2 * x1) + (b2 * y1) + c2; // Compute r1 and r2
//		r2 = (a2 * x2) + (b2 * y2) + c2;
//		
//		if((r1 != 0) && (r2 != 0) && (r1 * r2 >= 0)) return new double[] {0}; // DONT_INTERSECT
//		
//		denom = (a1 * b2) - (a2 * b1);
//		
//		if(denom == 0) return new double[] {1}; // COLLINEAR
//		
//		if(denom < 0) offset = -denom / 2; else offset = denom / 2;
//		
//		num = (b1 * c2) - (b2 * c1);
//		if(num < 0) windowX = (num - offset) / denom; else windowX = (num + offset) / denom;
//
//		num = (a2 * c1) - (a1 * c2);
//		if(num < 0) windowY = (num - offset) / denom; else windowY = (num + offset) / denom;
//		
//		return new double[] {2, windowX, windowY};
//	}
}

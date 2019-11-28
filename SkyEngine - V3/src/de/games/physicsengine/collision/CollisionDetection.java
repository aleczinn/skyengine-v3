package de.games.physicsengine.collision;

import de.games.physicsengine.internal.PhysicObject;
import de.games.physicsengine.objects.CircleObject;
import de.games.physicsengine.objects.PolygonObject;
import de.skyengine.utils.MathUtils;
import de.skyengine.utils.math.Vector2d;

public class CollisionDetection {

	/*
	 * Called in Contact.class - solve();
	 */
	public static void handleCircleCircle(PhysicObject object, PhysicObject target, Contact collision) {
		CircleObject a = (CircleObject) object;
		CircleObject b = (CircleObject) target;
		
		Vector2d normal = b.position.sub(a.position);
		
		double distanceSq = normal.lengthSquared();
		float radius = a.getRadius() + b.getRadius();
		
		if(distanceSq > radius * radius) {
			collision.contactCount = 0;
			return; // NO COLLISION
		}
		
		double distance = StrictMath.sqrt(distanceSq);
		collision.contactCount = 1;
		
		if(distance == 0) {
			collision.distance = a.getRadius();
			collision.normal.set(1.0, 0.0);
			collision.contacts[0].set(a.position);
		} else {
			collision.distance = radius - distance;
			collision.normal.set(normal).divideIntern(distance);
			collision.contacts[0].set(collision.normal).multiplyIntern(a.getRadius()).addIntern(a.position);
		}
	}
	
	public static void handleCirclePolygon(PhysicObject object, PhysicObject target, Contact collision) {
		CircleObject a = (CircleObject) object;
		PolygonObject b = (PolygonObject) target;
		
		collision.contactCount = 0;
		
		Vector2d center = b.u.transpose().multiplyIntern(a.position.sub(b.position));
		
		// Find edge with minimum distance
		// Exact concept as using support points in Polygon vs Polygon
		double separation = -Double.MAX_VALUE;
		int faceNormal = 0;
		
		for(int i = 0; i < b.vertexCount; i++) {
			double s = Vector2d.dot(b.normals[i], center.sub(b.vertices[i]));
			
			if(s > a.getRadius()) {
				return;
			}
			
			if(s > separation) {
				separation = s;
				faceNormal = i;
			}
		}
		
		// Grab face's vertices
		Vector2d v1 = b.vertices[faceNormal];
		int i2 = faceNormal + 1 < b.vertexCount ? faceNormal + 1 : 0;
		Vector2d v2 = b.vertices[i2];
		
		// Check to see if center is within polygon
		if(separation < MathUtils.EPSILON) {
			collision.contactCount = 1;
			b.u.multiply(b.normals[faceNormal], collision.normal ).negIntern();
			collision.contacts[0].set(collision.normal).multiplyIntern(a.getRadius()).addIntern( a.position );
			collision.distance = a.getRadius();
			return;
		}
		
		double dot1 = Vector2d.dot(center.sub(v1), v2.sub(v1));
		double dot2 = Vector2d.dot(center.sub(v2), v1.sub(v2));
		collision.distance = a.getRadius() - separation;
		
		// Closest to v1
		if (dot1 <= 0.0F) {
			if (Vector2d.distanceSq(center, v1) > a.getRadius() * a.getRadius()) {
				return;
			}

			collision.contactCount = 1;
			b.u.multiplyIntern(collision.normal.set(v1).subIntern(center)).normalize();
			b.u.multiply(v1, collision.contacts[0]).addIntern(b.position);
		} else if (dot2 <= 0.0f) {
			// Closest to v2
			
			if (Vector2d.distanceSq(center, v2) > a.getRadius() * a.getRadius()) {
				return;
			}

			collision.contactCount = 1;
			b.u.multiplyIntern(collision.normal.set(v2).subIntern(center)).normalize();
			b.u.multiply(v2, collision.contacts[0]).addIntern(b.position);
		} else {
			// Closest to face
			
			Vector2d n = b.normals[faceNormal];

			if (Vector2d.dot(center.sub(v1), n) > a.getRadius()) {
				return;
			}

			collision.contactCount = 1;
			b.u.multiply(n, collision.normal).negIntern();
			collision.contacts[0].set(a.position).addsIntern(collision.normal, a.getRadius());
		}
	}
	
	public static void handlePolygonCircle(PhysicObject object, PhysicObject target, Contact collision) {
		CollisionDetection.handleCirclePolygon(target, object, collision);
		
		if(collision.contactCount > 0) {
			collision.normal.negIntern();
		}
	}
}

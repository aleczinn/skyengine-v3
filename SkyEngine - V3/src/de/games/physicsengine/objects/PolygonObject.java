package de.games.physicsengine.objects;

import de.games.physicsengine.internal.PhysicObject;
import de.skyengine.graphics.ImmediateRenderer;
import de.skyengine.utils.math.Matrix2f;
import de.skyengine.utils.math.Vector2d;

public class PolygonObject extends PhysicObject {

	public static final int MAX_POLY_VERTEX_COUNT = 16;
	
	public int vertexCount;
	public Vector2d[] vertices = Vector2d.arrayOf(MAX_POLY_VERTEX_COUNT);
	public Vector2d[] normals = Vector2d.arrayOf(MAX_POLY_VERTEX_COUNT);
	
	public final Matrix2f u = new Matrix2f();
	
	public PolygonObject(double x, double y, Vector2d... vertices) {
		super(x, y, 0.0D);
		
		this.set(vertices);
		this.computeMass(1.0F);
	}

	public PolygonObject(double x, double y, double width, double height) {
		super(x, y, 0.0D);
		this.setRect(width, height);
		
		this.computeMass(1.0F);
	}
	
	@Override
	public void render(ImmediateRenderer renderer, float partialTicks) {
		renderer.polygon(this, 0xFFFFFFFF);
	}

	public void computeMass(float density) {
		// Calculate centroid and moment of inertia
		Vector2d c = new Vector2d(0.0D, 0.0D); // centroid
		double area = 0.0D;
		double I = 0.0D;
		final double k_inv3 = 1.0D / 3.0D;

		for (int i = 0; i < vertexCount; ++i) {
			// Triangle vertices, third vertex implied as (0, 0)
			Vector2d p1 = vertices[i];
			Vector2d p2 = vertices[(i + 1) % vertexCount];

			double D = Vector2d.cross(p1, p2);
			double triangleArea = 0.5f * D;

			area += triangleArea;

			// Use area to weight the centroid average, not just vertex position
			double weight = triangleArea * k_inv3;
			c.addsIntern( p1, weight );
			c.addsIntern( p2, weight );

			double intx2 = p1.x * p1.x + p2.x * p1.x + p2.x * p2.x;
			double inty2 = p1.y * p1.y + p2.y * p1.y + p2.y * p2.y;
			I += (0.25f * k_inv3 * D) * (intx2 + inty2);
		}

		c.multiplyIntern(1.0D / area);

		// Translate vertices to centroid (make the centroid (0, 0)
		// for the polygon in model space)
		// Not really necessary, but I like doing this anyway
		for (int i = 0; i < vertexCount; ++i) {
			vertices[i].subIntern(c);
		}

		this.mass = density * area;
		this.invMass = (this.mass != 0.0D) ? 1.0D / this.mass : 0.0D;
		this.inertia = I * density;
		this.invInertia = (this.inertia != 0.0D) ? 1.0D / this.inertia : 0.0D;
	}
	
	public void setRect(double width, double height) {
		this.vertexCount = 4;
		
		this.vertices[0].set(-width, -height);
		this.vertices[1].set(width, -height);
		this.vertices[2].set(width, height);
		this.vertices[3].set(-width, height);

		this.normals[0].set(0.0D, -1.0D);
		this.normals[1].set(1.0D, 0.0D);
		this.normals[2].set(0.0D, 1.0D);
		this.normals[3].set(-1.0D, 0.0D);
	}
	
	public void set(Vector2d... verts) {
		// Find the right most point on the hull
		int rightMost = 0;
		double highestXCoord = vertices[0].x;
		for (int i = 1; i < verts.length; ++i) {
			double x = vertices[i].x;

			if (x > highestXCoord) {
				highestXCoord = x;
				rightMost = i;
			}
			// If matching x then take farthest negative y
			else if (x == highestXCoord) {
				if (verts[i].y < verts[rightMost].y) {
					rightMost = i;
				}
			}
		}

		int[] hull = new int[MAX_POLY_VERTEX_COUNT];
		int outCount = 0;
		int indexHull = rightMost;

		for (;;) {
			hull[outCount] = indexHull;

			// Search for next index that wraps around the hull
			// by computing cross products to find the most counter-clockwise
			// vertex in the set, given the previos hull index
			int nextHullIndex = 0;
			for (int i = 1; i < verts.length; ++i) {
				// Skip if same coordinate as we need three unique
				// points in the set to perform a cross product
				if (nextHullIndex == indexHull) {
					nextHullIndex = i;
					continue;
				}

				// Cross every set of three unique vertices
				// Record each counter clockwise third vertex and add
				// to the output hull
				// See : http://www.oocities.org/pcgpe/math2d.html
				Vector2d e1 = verts[nextHullIndex].sub(verts[hull[outCount]]);
				Vector2d e2 = verts[i].sub(verts[hull[outCount]]);
				double c = Vector2d.cross(e1, e2);
				if (c < 0.0f) {
					nextHullIndex = i;
				}

				// Cross product is zero then e vectors are on same line
				// therefore want to record vertex farthest along that line
				if (c == 0.0f && e2.lengthSquared() > e1.lengthSquared()) {
					nextHullIndex = i;
				}
			}

			outCount++;
			indexHull = nextHullIndex;

			// Conclude algorithm upon wrap-around
			if (nextHullIndex == rightMost) {
				vertexCount = outCount;
				break;
			}
		}

		// Copy vertices into shape's vertices
		for (int i = 0; i < vertexCount; ++i) {
			vertices[i].set(verts[hull[i]]);
		}

		// Compute face normals
		for (int i = 0; i < vertexCount; ++i) {
			Vector2d face = vertices[(i + 1) % vertexCount].sub(vertices[i]);

			// Calculate normal with 2D cross product between vector and scalar
			normals[i].set(face.y, -face.x);
			normals[i].normalize();
		}
	}
	
	@Override
	public void setOrient(double radians) {
		this.u.set(0);
	}
}

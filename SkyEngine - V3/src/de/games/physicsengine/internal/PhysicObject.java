package de.games.physicsengine.internal;

import de.skyengine.graphics.ImmediateRenderer;
import de.skyengine.utils.MathUtils;
import de.skyengine.utils.math.Vector2d;

public abstract class PhysicObject {

	public Vector2d position = new Vector2d();
	public Vector2d lastPosition = new Vector2d();
	
	public Vector2d velocity = new Vector2d();
	public Vector2d force = new Vector2d();
	
	// Angular Components
	public double angularVelocity;
	public double orient, torque;
	
	public double mass, invMass, inertia, invInertia;
	public double restitution, staticFriction, dynamicFriction;
	
	public PhysicObject(double x, double y, double mass) {
		this.position.set(x, y);
		this.lastPosition.set(x, y);
		
		this.mass = mass;
		
		this.velocity.set(0, 0);
		this.force.set(0, 0);
		
		this.orient = MathUtils.random(-MathUtils.PI, MathUtils.PI);
		
		this.torque = 0;
		this.restitution = 0.2D;
		this.staticFriction = 0.5D;
		this.dynamicFriction = 0.3D;
		
		this.restitution = 0.2D;
		this.staticFriction = 0.4D;
		this.dynamicFriction = 0.2D;
	}
	
	public abstract void render(ImmediateRenderer renderer, float partialTicks);
	
	public void applyForce(Vector2d force) {
		this.force.addIntern(force);
	}

	/**
	 * Add Impulse to this object.
	 * @param impulse
	 * @param contactVector
	 */
	public void applyImpulse(Vector2d impulse, Vector2d contactVector) {
		this.velocity.addsIntern(impulse, this.invMass);
		this.angularVelocity += this.invInertia * Vector2d.cross(contactVector, impulse);
	}
	
	public double getX() {
		return this.position.x;
	}
	
	public double getY() {
		return this.position.y;
	}
	
	public void setOrient(double radians) {
		this.orient = radians;
	}
	
	public void setStatic() {
		this.inertia = 0F;
		this.invInertia = 0F;
		
		this.mass = 0F;
		this.invMass = 0F;
	}
	
	public boolean isStatic() {
		return this.mass == 0;
	}
}

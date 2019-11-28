package de.games.physicsengine.collision;

import de.games.physicsengine.internal.PhysicObject;
import de.games.physicsengine.objects.CircleObject;
import de.games.physicsengine.objects.PolygonObject;
import de.skyengine.graphics.ImmediateRenderer;
import de.skyengine.utils.MathUtils;
import de.skyengine.utils.math.Vector2d;

public class Contact {

	private PhysicObject a;
	private PhysicObject b;
	
	public final Vector2d normal = new Vector2d();
	public final Vector2d[] contacts = {new Vector2d(), new Vector2d()};
	public int contactCount;
	
	public double distance;
	public double e, sFriction, dFriction;
	
	public Contact(PhysicObject a, PhysicObject b) {
		this.a = a;
		this.b = b;
	}
	
	public void solve() {
		if(this.a instanceof CircleObject && this.b instanceof CircleObject) {
			CollisionDetection.handleCircleCircle(this.a, this.b, this);
		}
		
		if(this.a instanceof CircleObject && this.b instanceof PolygonObject) {
			CollisionDetection.handleCirclePolygon(this.a, this.b, this);
		}
		
		if(this.a instanceof PolygonObject && this.b instanceof CircleObject) {
			CollisionDetection.handlePolygonCircle(this.a, this.b, this);
		}
	}
	
	public void render(ImmediateRenderer renderer, float strength) {
		for(int i = 0; i < this.contactCount; i++) {
			Vector2d v = this.contacts[i];
			Vector2d n = this.normal;

			renderer.line(v.xAsFloat(), v.yAsFloat(), v.xAsFloat() + n.xAsFloat() * strength, v.yAsFloat() + n.yAsFloat() * strength, 0xFFFF0000);
		}
	}
	
	public void init() {
		this.e = StrictMath.min(this.a.restitution, this.b.restitution);
		
		this.sFriction = StrictMath.sqrt(this.a.staticFriction * this.a.staticFriction + this.b.staticFriction * this.b.staticFriction);
		this.dFriction = StrictMath.sqrt(this.a.dynamicFriction * this.a.dynamicFriction + this.b.dynamicFriction * this.b.dynamicFriction);
		
		for(int i = 0; i < this.contactCount; i++) {
			Vector2d ra = this.contacts[i].sub(this.a.position);
			Vector2d rb = this.contacts[i].sub(this.b.position);
			
			Vector2d rVelocity = this.b.velocity.add(Vector2d.cross(this.b.angularVelocity, rb, new Vector2d())).subIntern(this.a.velocity).subIntern(Vector2d.cross(this.a.angularVelocity, ra, new Vector2d()));
			
			if(rVelocity.lengthSquared() < MathUtils.RESTING) {
				this.e = 0.0D;
			}
		}
	}
	
	public void applyImpulse() {
		if(MathUtils.equal(this.a.invMass + this.b.invMass, 0)) {
			// Unendliche Masse
			this.a.velocity.set(0, 0);
			this.b.velocity.set(0, 0);
			return;
		}
		
		for(int i = 0; i < this.contactCount; i++) {
			// Calculate radii from COM to contact
			Vector2d ra = this.contacts[i].sub(this.a.position);
			Vector2d rb = this.contacts[i].sub(this.b.position);
			
			// Relative velocity
			Vector2d rVelocity = this.b.velocity.add(Vector2d.cross(this.b.angularVelocity, rb, new Vector2d())).subIntern(this.a.velocity).subIntern(Vector2d.cross(this.a.angularVelocity, ra, new Vector2d()));
			
			// Relative velocity along the normal
			double contactVelocity = Vector2d.dot(rVelocity, this.normal);
			
			// Do not resolve if velocities are separating
			if(contactVelocity > 0) return;
			
			double raCross = Vector2d.cross(ra, this.normal);
			double rbCross = Vector2d.cross(rb, this.normal);
			double invMassAll = this.a.invMass + this.b.invMass + (raCross * raCross) * this.a.invInertia + (rbCross * rbCross) * this.b.invInertia;
			
			// Calculate impulse scalar
			double j = -(1.0D + this.e) * contactVelocity;
			j /= invMassAll;
			j /= this.contactCount;
			
			// Apply impulse
			Vector2d impulse = this.normal.multiply(j);
			this.a.applyImpulse(impulse.neg(), ra);
			this.b.applyImpulse(impulse, rb);
			
			// Friction impulse
			rVelocity = this.b.velocity.add(Vector2d.cross(this.b.angularVelocity, rb, new Vector2d())).subIntern(this.a.velocity).subIntern(Vector2d.cross(this.a.angularVelocity, ra, new Vector2d()));
			
			Vector2d t = new Vector2d(rVelocity);
			t.addsIntern(this.normal, -Vector2d.dot(rVelocity, this.normal));
			t.normalize();
			
			// j tangent magnitude
			double jt = -Vector2d.dot(rVelocity, t);
			jt /= invMassAll;
			jt /= contactCount;
			
			// Don't apply tiny friction impulses
			if(MathUtils.equal(jt, 0.0D)) {
				return;
			}
			
			// Coulumb's law
			Vector2d tangentImpulse;
			if(StrictMath.abs(jt) < j * this.sFriction) {
				tangentImpulse = t.multiply(jt);
			} else {
				tangentImpulse = t.multiply(j).multiplyIntern(-this.dFriction);
			}
			
			// Apply friction impulse
			this.a.applyImpulse(tangentImpulse.neg(), ra);
			this.b.applyImpulse(tangentImpulse, rb);
		}
	}
	
	public void correct() {
		double correction = StrictMath.max(this.distance - MathUtils.DISTANCE_ALLOWANCE, 0.0D) / (this.a.invMass + this.b.invMass) * MathUtils.DISTANCE_CORRETION;
		
		this.a.position.addsIntern(this.normal, -this.a.invMass * correction);
		this.b.position.addsIntern(this.normal, this.b.invMass * correction);
	}
}

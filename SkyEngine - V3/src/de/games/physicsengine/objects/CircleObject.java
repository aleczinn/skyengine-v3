package de.games.physicsengine.objects;

import de.games.physicsengine.internal.PhysicObject;
import de.skyengine.graphics.ImmediateRenderer;
import de.skyengine.graphics.ImmediateRenderer.ShapeType;
import de.skyengine.utils.MathUtils;

public class CircleObject extends PhysicObject {

	private float radius;
	
	public CircleObject(double x, double y, float radius) {
		super(x, y, 0.0D);
		this.radius = radius;
		
		this.computeMass(1.0F);
	}

	@Override
	public void render(ImmediateRenderer renderer, float partialTicks) {
		float iX = MathUtils.interpolateToFloat(this.position.x, this.lastPosition.x, partialTicks);
		float iY = MathUtils.interpolateToFloat(this.position.y, this.lastPosition.y, partialTicks);
		
		float rX = (float) (StrictMath.cos(this.orient) * this.radius);
		float rY = (float) (StrictMath.sin(this.orient) * this.radius);
		
		renderer.circle(ShapeType.LINE, iX, iY, this.radius, 0xFFFFFFFF);
		renderer.line(iX, iY, iX + rX, iY + rY, 1.0F, 0xFFEEEEEE);
	}
	
	public void computeMass(float density) {
		this.mass = MathUtils.PI * radius * radius * density; // π * r²
		this.invMass = (this.mass != 0.0D) ? 1.0D / this.mass : 0.0D; 
		this.inertia = this.mass * radius * radius;
		this.invInertia = (this.inertia != 0.0D) ? 1.0D / this.inertia : 0.0D;
	}
	
	public float getRadius() {
		return radius;
	}
}

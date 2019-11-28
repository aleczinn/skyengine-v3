package de.games.factory.entity;

import de.skyengine.graphics.ImmediateRenderer;
import de.skyengine.utils.math.Vector2f;

public abstract class Entity {

	public Vector2f position = new Vector2f();
	public Vector2f lastPosition = new Vector2f();
	
	public Vector2f acceleration = new Vector2f();
	public Vector2f velocity = new Vector2f();
	public Vector2f printVelocity = new Vector2f();
	
	public final float width;
	public final float height;
	
	public Entity(float x, float y, float width, float height) {
		this.position.set(x, y);
		this.lastPosition.set(this.position);
		
		this.velocity.set(0, 0);
		
		this.width = width;
		this.height = height;
	}
	
	public void update() {
		this.lastPosition.set(this.position);
		this.position.addIntern(this.acceleration);
		this.position.addIntern(this.velocity);
		
		this.printVelocity.set(this.velocity);
	}
	
	public abstract void render(ImmediateRenderer renderer, float partialTicks);
}

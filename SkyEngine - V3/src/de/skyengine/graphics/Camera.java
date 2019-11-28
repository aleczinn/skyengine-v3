package de.skyengine.graphics;

import org.lwjgl.opengl.GL11;

import de.games.factory.entity.Entity;
import de.skyengine.core.SkyEngine;
import de.skyengine.utils.MathUtils;
import de.skyengine.utils.math.Vector2f;

public class Camera {

	public Vector2f position;
	public Vector2f lastPosition;
	
	public float scale = 1.0F;
	
	public Camera(float x, float y) {
		this.position = new Vector2f();
		this.position.set((int) x, (int) y);
		
		this.lastPosition = new Vector2f();
		this.lastPosition.set(this.position);
	}
	
	public void begin(float partialTicks) {
		Vector2f interpolation = MathUtils.interpolate(this.position, this.lastPosition, partialTicks);
		interpolation.round(1);
		
		GL11.glPushMatrix();
		GL11.glTranslatef(-interpolation.x, -interpolation.y, 0);
	}
	
	public void end() {
		GL11.glPopMatrix();
	}
	
	public void resize(Entity entity, int width, int height) {
		this.position.x = (float) (this.position.x + (entity.position.x + (entity.width / 2.0D) - this.position.x - (SkyEngine.getWidth() / 2.0D)));
		this.position.y = (float) (this.position.y + (entity.position.y - this.position.y - (SkyEngine.getHeight() / 2.0D)));
		
		this.lastPosition.set(this.position);
	}
	
	/**
	 * Follows an entity with a slide effect
	 * @param entity is the Object that the camera is following
	 * @param lerp is the value how many the camera should slide
	 */
	public void update(Entity entity, float lerp) {
		this.lastPosition.set(this.position);
		
		this.position.x = (float) (this.position.x + (entity.position.x + (entity.width / 2.0D) - this.position.x - (SkyEngine.getWidth() / 2.0D)) * lerp);
		this.position.y = (float) (this.position.y + (entity.position.y - this.position.y - (SkyEngine.getHeight() / 2.0D)) * lerp);
	}
}

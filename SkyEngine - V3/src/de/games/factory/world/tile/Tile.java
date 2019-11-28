package de.games.factory.world.tile;

import de.skyengine.graphics.ImmediateRenderer;
import de.skyengine.graphics.internal.Texture;
import de.skyengine.utils.MathUtils;

public abstract class Tile {

	private Texture texture;
	
	public final float speedMultiplier;
	
	private float rotation;
	
	public Tile(Texture texture) {
		this.texture = texture;
		
		this.speedMultiplier = 1.0F;
		this.rotation = MathUtils.random(3) * 90.0F;
	}
	
	public abstract void update();
	public abstract void render(ImmediateRenderer renderer, float x, float y, float partialTicks);
	
	public Texture getTexture() {
		return texture;
	}
	
	public float getRotation() {
		return rotation;
	}
	
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}
}

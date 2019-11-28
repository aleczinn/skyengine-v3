package de.games.factory.world.object;

import de.skyengine.graphics.ImmediateRenderer;
import de.skyengine.graphics.internal.Texture;

public abstract class GameObject {

	public Texture texture;
	
	public int tileSizeX;
	public int tileSizeY;
	
	private GameObject parent;
	
	public GameObject(Texture texture, int tileSizeX, int tileSizeY) {
		this.texture = texture;
		this.tileSizeX = tileSizeX;
		this.tileSizeY = tileSizeY;
	}
	
	public abstract void render(ImmediateRenderer renderer, float x, float y, float partialTicks);
	public abstract void update();
	
	public GameObject getParent() {
		return parent;
	}
}

package de.skyengine.graphics.screen.components;

import de.skyengine.graphics.ImmediateRenderer;

public abstract class GuiComponent {

	public float x;
	public float y;
	
	public float width;
	public float height;
	
	public GuiComponent(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public abstract void update();
	public abstract void render(ImmediateRenderer renderer, float partialTicks);
}

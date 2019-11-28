package de.games;

import de.skyengine.core.GameContainer;
import de.skyengine.core.Input;
import de.skyengine.core.SkyEngine;
import de.skyengine.graphics.ImmediateRenderer;

public class GraphicsDemo extends GameContainer {

	private ImmediateRenderer renderer;
	private int[] pixels;
	
	@Override
	public void init() {
		super.init();
		
		int width = SkyEngine.getWidth();
		int height = SkyEngine.getHeight();
		
		this.renderer = new ImmediateRenderer();
		this.pixels = new int[width * height];
	}
	
	@Override
	public void input(Input input) {
		super.input(input);
	}
	
	@Override
	public void update() {
		super.update();
	}
	
	@Override
	public void render(float mouseX, float mouseY, float partialTicks) {
		super.render(mouseX, mouseY, partialTicks);
		
		int width = SkyEngine.getWidth();
		int height = SkyEngine.getHeight();
		
		if(this.pixels != null) {
			float pixelWidth = 8.0F;
			
			float x = 0;
			float y = 0;
			
			for(int pixel = 0; pixel < this.pixels.length; pixel++) {
				this.renderer.rect(x, y, x + pixelWidth, y + pixelWidth, 0xFFFFFFFF);
				y += pixel % height;
			}
		}
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}
	
	@Override
	public void exitGame() {
		super.exitGame();
	}
}

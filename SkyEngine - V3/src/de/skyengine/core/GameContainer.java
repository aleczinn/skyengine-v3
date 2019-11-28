package de.skyengine.core;

import de.skyengine.core.internal.IGameContainer;
import de.skyengine.graphics.screen.IScreen;

public abstract class GameContainer implements IGameContainer {

	private IScreen screen;
	
	/**
	 * Called when your game initializes.
	 */
	@Override
	public void init() {
		if(this.screen != null) this.screen.init();
	}

	/**
	 * Here you can handle your input.
	 */
	@Override
	public void input(Input input) {
		if(this.screen != null) this.screen.input(input);
	}
	
	/**
	 * Update the Game 30 times per second.
	 */
	@Override
	public void update() {
		if(this.screen != null) this.screen.update();
	}

	/**
	 * Here you can render your game (also with interpolation)
	 * @param partialTicks is the time for interpolation. You can use it to calculate the value to the next update. [0 - 1] 
	 * @param partialTicks2 
	 * @param f 
	 */
	@Override
	public void render(float mouseX, float mouseY, float partialTicks) {
		if(this.screen != null) this.screen.render(partialTicks);
	}
	
	/**
	 * Called when the Window is resized.
	 */
	@Override
	public void resize(int width, int height) {
		if(this.screen != null) this.screen.resize(width, height);
	}
	
	/**
	 * Called when the game is closed.
	 * Dispose here all your Shaders/Textures
	 */
	@Override
	public void exitGame() {
		if(this.screen != null) this.screen.close();
	}
	
	public IScreen getScreen() {
		return screen;
	}
	
	public void setScreen(IScreen screen) {
		if(this.screen != null) {
			this.screen.close();
		}
		
		this.screen = screen;
		
		if(screen != null) {
			this.screen.init();
			this.screen.resize(SkyEngine.application().getWidth(), SkyEngine.application().getHeight());
		}
	}
}

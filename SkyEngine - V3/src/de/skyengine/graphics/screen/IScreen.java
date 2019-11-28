package de.skyengine.graphics.screen;

import de.skyengine.core.Input;

public interface IScreen {

	/**
	 * Calls when this screen becomes the current screen
	 */
	public void init();
	
	/**
	 * Called when the screen gets input from mouse/keyboard/controller
	 * @param delta | The time in seconds since the last render.
	 */
	public void input(Input input);
	
	/**
	 * Called when the screen should update itself.
	 * @param delta | The time in seconds since the last render.
	 */
	public void update();
	
	/**
	 * Called when the screen should render itself.
	 */
	public void render(float partialTicks);
	
	/**
	 * Called when the Window is resized.
	 */
	public void resize(int width, int height);
	
	/**
	 * Calls when this screen is no longer the current screen
	 */
	public void close();
}

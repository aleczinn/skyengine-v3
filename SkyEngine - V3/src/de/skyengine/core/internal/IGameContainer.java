package de.skyengine.core.internal;

import de.skyengine.core.Input;

public interface IGameContainer {

	public void init();
	public void input(Input input);
	public void update();
	public void render(float mouseX, float mouseY, float partialTicks);
	public void resize(int width, int height);
	public void exitGame();
}

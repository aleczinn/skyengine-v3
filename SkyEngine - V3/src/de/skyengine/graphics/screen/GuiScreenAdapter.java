package de.skyengine.graphics.screen;

import java.util.ArrayList;
import java.util.List;

import de.skyengine.core.Input;
import de.skyengine.graphics.ImmediateRenderer;
import de.skyengine.graphics.screen.components.GuiComponent;

public class GuiScreenAdapter implements IScreen {

	private ImmediateRenderer renderer;
	public List<GuiComponent> components;
	
	@Override
	public void init() {
		this.renderer = new ImmediateRenderer();
		
		this.components = new ArrayList<GuiComponent>();
	}

	@Override
	public void input(Input input) {
		
	}

	@Override
	public void update() {
		for(GuiComponent component : this.components) {
			component.update();
		}
	}

	@Override
	public void render(float partialTicks) {
		for(GuiComponent component : this.components) {
			component.render(this.renderer, partialTicks);
		}
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void close() {
			
	}
}

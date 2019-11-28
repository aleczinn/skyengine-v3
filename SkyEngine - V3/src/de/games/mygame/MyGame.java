package de.games.mygame;

import org.lwjgl.glfw.GLFW;

import de.games.mygame.client.World;
import de.skyengine.core.GameContainer;
import de.skyengine.core.Input;
import de.skyengine.core.SkyEngine;
import de.skyengine.graphics.ImmediateRenderer;
import de.skyengine.graphics.ShapeRenderer;
import de.skyengine.resources.Resources;
import de.skyengine.utils.SpecsUtil;

public class MyGame extends GameContainer {

	public ImmediateRenderer renderer;
	public ShapeRenderer shapeRenderer;
	
	public World world;
	
	@Override
	public void init() {
		super.init();
		
		this.renderer = new ImmediateRenderer();
		this.shapeRenderer = new ShapeRenderer(true);
		
		this.world = new World("world");
		this.world.loadMap(this.world.exampleMap(40, 40));
	}
	
	@Override
	public void input(Input input) {
		super.input(input);
		
		if(input.isKeyPressed(GLFW.GLFW_KEY_F)) {
			this.world.loadMap(this.world.exampleMap(40, 40));
		}
		
		this.world.input(input);
	}
	
	@Override
	public void update() {
		super.update();
		
		this.world.update();
	}
	
	@Override
	public void render(float mouseX, float mouseY, float partialTicks) {
		super.render(mouseX, mouseY, partialTicks);
		
		this.world.render(this.renderer, partialTicks);
		
		// INFORMATION'S
		this.renderer.drawString(Resources.fonts().main, "Mem: " + SpecsUtil.getRamUsageInPercent() + "% " + SpecsUtil.getRamUsage() + "/" + SpecsUtil.getMaxRam() + " mb", 2, 2, 0xFFFFFFFF);
		int w = SkyEngine.getWidth();
		String fps = "FPS: " + SkyEngine.getInstance().getDebugFPS();
		float sWitdth = this.renderer.getWidth(Resources.fonts().main, fps);
		this.renderer.drawString(Resources.fonts().main, fps, w - sWitdth - 2, 2, 0xFFFFFFFF);
	}
	
	@Override
	public void exitGame() {
		super.exitGame();
	}
}

package de.games.factory;

import org.lwjgl.glfw.GLFW;

import de.games.factory.ui.IngameUI;
import de.games.factory.ui.screens.ScreenMainMenu;
import de.skyengine.core.GameContainer;
import de.skyengine.core.Input;
import de.skyengine.core.SkyEngine;
import de.skyengine.graphics.ImmediateRenderer;
import de.skyengine.graphics.ShapeRenderer;
import de.skyengine.resources.Resources;
import de.skyengine.utils.SpecsUtil;

public class FactoryGame extends GameContainer {

	private static FactoryGame instance;
	
	public ImmediateRenderer renderer;
	public ShapeRenderer shapeRenderer;
	
	private FactoryWorld world;
	private IngameUI ingameUI;
	
	private boolean debugMode = true;
	@SuppressWarnings("unused")
	private boolean pauseGame = false;

	public FactoryGame() {
		instance = this;
	}
	
	@Override
	public void init() {
		super.init();
		
		this.renderer = new ImmediateRenderer();
		this.shapeRenderer = new ShapeRenderer(true);
		
		this.world = new FactoryWorld("world");
		
		this.ingameUI = new IngameUI();
		
		this.setScreen(new ScreenMainMenu(this.renderer));
	}
	
	@Override
	public void input(Input input) {
		if(this.getScreen() == null) {
			this.world.input(input);
		}
		
		if(input.isKeyPressed(GLFW.GLFW_KEY_F3)) {
			this.debugMode = !this.debugMode;
			System.out.println("[Debug Mode] - Debug mode is now " + (this.debugMode ? "enabled." : "disabled."));
		}
		super.input(input);
	}
	
	@Override
	public void update() {
		if(this.getScreen() == null) {
			this.world.update();
			this.ingameUI.update(this.world.getMap());
		}
		super.update();
	}
	
	@Override
	public void render(float mouseX, float mouseY, float partialTicks) {
		if(this.getScreen() == null) {
			this.world.render(this.renderer, mouseX, mouseY, partialTicks);
			this.ingameUI.render(this.renderer, partialTicks);
		}

		// Minimap
//		int width = SkyEngine.getWidth() - 10;
//		int height = 40;
//		
//		this.renderer.rect(width - 205, height - 5, width + 5, height + 205, 0xFF353b48);
//		this.renderer.rect(width - 200, height, width, height + 200, 0xFF57606f);
//		
		// Render Minimap
//		Tile[][] map = this.world.getTiles();
//		
//		int drawX = width - 200;
//		int drawY = height;
//		
//		Vector2i playerTile = this.world.getTilePositionInWorld(this.world.getPlayer().position.x, this.world.getPlayer().position.y);
//		
//		int offset = 12;
//		for(int x = playerTile.x - offset; x < playerTile.x + offset; x++) {
//			if(drawX >= width + 200 || drawY >= height + 200) {
//				break;
//			}
//			
//			for(int y = playerTile.y - offset; y < playerTile.y + offset; y++) {
//				if(drawX >= width + 200 || drawY >= height + 200) {
//					break;
//				}
//				
//				if(x < 0 || x > map.length - 1 || y < 0 || y > map[map.length - 1].length - 1) {
//					this.renderer.rect(drawX, drawY, drawX + 8, drawY + 8, 0xFF000000);	
//				} else {
//					Tile tile = map[x][y];
//					
//					int color = 0xFF000000;
//					if(tile instanceof TileDirt) {
//						color = 0xFFcd6133;
//					}
//					if(tile instanceof TileWater) {
//						color = 0xFF34ace0;
//					}
//					this.renderer.rect(drawX, drawY, drawX + 8, drawY + 8, color);	
//				}
//				drawY += 8;
//			}
//			drawX += 8;
//			drawY = height;
//		}
//		
//		this.renderer.point(width - 100, height + 100, 7, 0xFFFFFFFF);
//		
//		this.renderer.line(width - 100, height, width - 100, height + 200, 0x80FFFFFF);
//		this.renderer.line(width - 200, height + 100, width, height + 100, 0x80FFFFFF);
		
		super.render(mouseX, mouseY, partialTicks);
		
		if(this.debugMode) {
			this.renderer.drawString(Resources.fonts().debug, "Mem: " + SpecsUtil.getRamUsageInPercent() + "% " + SpecsUtil.getRamUsage() + "/" + SpecsUtil.getMaxRam() + " mb", 5, 5, 0xFFFFFFFF);
			this.renderer.drawString(Resources.fonts().debug, "Java: " + SpecsUtil.getJava(), 5, 5 + (25 * 2), 0xFFFFFFFF);
			this.renderer.drawString(Resources.fonts().debug, "CPU: " + SpecsUtil.getCPU(), 5, 5 + (25 * 3), 0xFFFFFFFF);
			this.renderer.drawString(Resources.fonts().debug, "GPU: " + SpecsUtil.getRenderer(), 5, 5 + (25 * 4), 0xFFFFFFFF);
			
			String fps = "FPS: " + SkyEngine.getInstance().getDebugFPS();
			float sWitdth = this.renderer.getWidth(Resources.fonts().main, fps);
			this.renderer.drawString(Resources.fonts().main, fps, SkyEngine.getWidth() - sWitdth - 2, 2, 0xFFFFFFFF);
		}
	}
	
	@Override
	public void resize(int width, int height) {
		if(this.getScreen() == null) {
			this.world.resize(width, height);
			this.ingameUI.resize(width, height);
		}
		super.resize(width, height);
	}
	
	@Override
	public void exitGame() {
		super.exitGame();
	}
	
	public FactoryWorld getWorld() {
		return world;
	}
	
	public IngameUI getIngameUI() {
		return ingameUI;
	}
	
	public static FactoryGame getInstance() {
		return instance;
	}
}

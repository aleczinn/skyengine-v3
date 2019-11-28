package de.games.chess;

import de.games.chess.internal.Grid;
import de.skyengine.core.GameContainer;
import de.skyengine.core.Input;
import de.skyengine.core.SkyEngine;
import de.skyengine.graphics.ImmediateRenderer;

public class ChessGame extends GameContainer {

	public ImmediateRenderer renderer;
	
	private Grid grid;
	
	public ChessGame() {
		this.renderer = new ImmediateRenderer();
	}
	
	@Override
	public void init() {
		super.init();
		
		this.grid = new Grid(SkyEngine.getWidth() / 2 - 256, SkyEngine.getHeight() / 2 - 256);
		this.grid.setupCharacters();
	}
	
	@Override
	public void input(Input input) {
		super.input(input);
		
		this.grid.input(input);
	}
	
	@Override
	public void update() {
		super.update();
		
		this.grid.update();
	}
	
	@Override
	public void render(float mouseX, float mouseY, float partialTicks) {
		super.render(mouseX, mouseY, partialTicks);
		
		this.grid.render(this.renderer, partialTicks);
	}
	
	@Override
	public void exitGame() {
		super.exitGame();
	}
}

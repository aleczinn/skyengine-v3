package de.games.factory.ui;

import de.games.factory.FactoryGame;
import de.games.factory.entity.EntityPlayer;
import de.games.factory.world.tile.Tile;
import de.skyengine.core.SkyEngine;
import de.skyengine.graphics.ImmediateRenderer;
import de.skyengine.utils.math.Vector2i;

public class MiniMap {

	private float relativeFromTop;
	private float relativeFromRight;
	
	private float x;
	private float y;
	
	private float width;
	private float height;
	
	private Tile[][] map;
	private Vector2i pTile = new Vector2i();
	
	private final float BORDER_WIDTH = 5.0F;
	
	public MiniMap(float relativeFromRight, float relativeFromTop, float width, float height) {
		this.relativeFromRight = relativeFromRight;
		this.relativeFromTop = relativeFromTop;
		
		this.x = SkyEngine.getWidth() - relativeFromRight;
		this.y = relativeFromTop;
		
		this.width = width;
		this.height = height;
	}
	
	public void update(Tile[][] map) {
		this.map = map;
		EntityPlayer player = FactoryGame.getInstance().getWorld().getPlayer();
		Vector2i vector = FactoryGame.getInstance().getWorld().getTilePositionInWorld(player.position.x, player.position.y);
		this.pTile.set(vector);
	}
	
	public void render(ImmediateRenderer renderer, float partialTicks) {
		renderer.rect(this.x - this.BORDER_WIDTH, this.y - this.BORDER_WIDTH, this.x + this.width + this.BORDER_WIDTH, this.y + this.height + this.BORDER_WIDTH, 0xFF535c68);
		renderer.rect(this.x, this.y, this.x + this.width, this.y + this.height, 0xFF747d8c);
		
		float w = this.width / 2.0F;
		float h = this.height / 2.0F;
		
		renderer.line(this.x + w, this.y, this.x + w, this.y + this.height, 0xFFEEEEEE);
		renderer.line(this.x, this.y + h, this.x + this.width, this.y + h, 0xFFEEEEEE);
		
		float pxWidth = 8.0F;
		
		int indexX = 0;
		for(int x = 0; x < this.width; x += pxWidth) {
			
		}
	}
	
	public void resize(int width, int height) {
		this.x = width - relativeFromRight;
		this.y = relativeFromTop;
		
		System.out.println(this.x);
	}
}

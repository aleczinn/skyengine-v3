package de.games.factory.world.tile.tiles;

import de.games.factory.FactoryWorld;
import de.games.factory.world.tile.Tile;
import de.skyengine.graphics.ImmediateRenderer;
import de.skyengine.resources.Resources;
import de.skyengine.utils.MathUtils;

public class TileWater extends Tile {

	private int index = 0;
	
	public TileWater() {
		super(Resources.textures().water);
		this.index = MathUtils.random(8);
	}

	@Override
	public void update() {
		
	}
	
	@Override
	public void render(ImmediateRenderer renderer, float x, float y, float partialTicks) {
		renderer.startRotate(this.getRotation(), x + (FactoryWorld.TILE_SIZE / 2.0F), y + (FactoryWorld.TILE_SIZE / 2.0F));
		renderer.textureRegion(this.getTexture(), x, y, this.index * FactoryWorld.TILE_SIZE, 0, FactoryWorld.TILE_SIZE, FactoryWorld.TILE_SIZE, 0xFFFFFFFF);
		renderer.stopRotate();
	}
}

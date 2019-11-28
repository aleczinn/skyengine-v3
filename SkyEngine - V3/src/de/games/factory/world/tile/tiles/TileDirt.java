package de.games.factory.world.tile.tiles;

import de.games.factory.FactoryWorld;
import de.games.factory.world.tile.Tile;
import de.skyengine.graphics.ImmediateRenderer;
import de.skyengine.resources.Resources;

public class TileDirt extends Tile {

	public TileDirt() {
		super(Resources.textures().dirt);
	}

	@Override
	public void update() {
		
	}
	
	@Override
	public void render(ImmediateRenderer renderer, float x, float y, float partialTicks) {
		renderer.startRotate(this.getRotation(), x + (FactoryWorld.TILE_SIZE / 2.0F), y + (FactoryWorld.TILE_SIZE / 2.0F));
		renderer.texture(this.getTexture(), x, y, FactoryWorld.TILE_SIZE, FactoryWorld.TILE_SIZE, 0xFFFFFFFF);
		renderer.stopRotate();
	}
}

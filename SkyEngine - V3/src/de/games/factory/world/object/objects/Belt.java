package de.games.factory.world.object.objects;

import de.games.factory.FactoryWorld;
import de.games.factory.world.object.GameObject;
import de.skyengine.graphics.ImmediateRenderer;
import de.skyengine.resources.Resources;

public class Belt extends GameObject {

	public Belt() {
		super(Resources.textures().beltRightAtlas, 1, 1);
	}

	@Override
	public void render(ImmediateRenderer renderer, float x, float y, float partialTicks) {
		renderer.textureRegion(this.texture, x, y, 0, 0, (this.tileSizeX * FactoryWorld.TILE_SIZE), (this.tileSizeY * FactoryWorld.TILE_SIZE), 0xFFFFFFFF);
	}
	
	@Override
	public void update() {
		
	}
}

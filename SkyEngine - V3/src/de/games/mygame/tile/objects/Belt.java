package de.games.mygame.tile.objects;

import de.games.factory.world.tile.Tile;
import de.games.mygame.client.World;
import de.skyengine.graphics.ImmediateRenderer;

public class Belt {

	@SuppressWarnings("unused")
	private Tile tile;
	
	private static int animationIndexX = 0;
	private static int animationIndexY = 0;
	
	public Belt(Tile tile) {
		this.tile = tile;
	}
	
	@SuppressWarnings("unused")
	public void render(ImmediateRenderer renderer, float partialTicks) {
		float animationX = (Belt.animationIndexX * World.TILE_SIZE); 
		float animationY = (Belt.animationIndexY * World.TILE_SIZE);
		
		System.out.println(animationX);
		
//		renderer.textureRegion(Resources.textures().beltRightAtlas, tile.getX(), tile.getY(), animationX, animationY, World.TILE_SIZE, World.TILE_SIZE, 0xFFFFFFFF);
	}
	
	public static void updateBeltAnimation(int speed) {
		for(int i = 0; i < speed; i++) {
			Belt.animationIndexX++;

			if (Belt.animationIndexX > 15) {
				Belt.animationIndexX = 0;
				Belt.animationIndexY = Belt.animationIndexY == 0 ? 1 : 0;
			}
		}
	}
}

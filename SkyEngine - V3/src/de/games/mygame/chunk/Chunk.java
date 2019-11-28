package de.games.mygame.chunk;

import de.games.factory.world.tile.Tile;
import de.games.mygame.client.World;
import de.skyengine.graphics.ImmediateRenderer;

public class Chunk {

	public static final int SIZE_X = 2;
	public static final int SIZE_Y = 2;
	
	private final int chunkX;
	private final int chunkY;
	
	private Tile[][] tiles;
	private int chunkSizeX;
	private int chunkSizeY;
	
	public Chunk(Tile[][] map, int chunkX, int chunkY, int startTileX, int startTileY) {
		this.chunkX = chunkX;
		this.chunkY = chunkY;
		
		int mapLengthX = map.length;
		int mapLengthY = map[map.length - 1].length;
		
		this.chunkSizeX = Chunk.SIZE_X;
		this.chunkSizeY = Chunk.SIZE_Y;
		
		if(startTileX + Chunk.SIZE_X > mapLengthX) {
			this.chunkSizeX = mapLengthX - startTileX;
		}
		
		if(startTileY + Chunk.SIZE_Y > mapLengthY) {
			this.chunkSizeY = mapLengthY - startTileY;
		}
		
		this.tiles = new Tile[this.chunkSizeX][this.chunkSizeY];
		
		for(int x = 0; x < this.chunkSizeX; x++) {
			for(int y = 0; y < this.chunkSizeY; y++) {
				this.tiles[x][y] = map[startTileX + x][startTileY + y];
			}
		}
	}
	
	public void update() {
		
	}
	
	@SuppressWarnings("unused")
	public void render(ImmediateRenderer renderer, float partialTicks) {
		if(this.tiles != null) {
			int startX = (int) (this.chunkX * Chunk.SIZE_X * World.TILE_SIZE);
			int startY = (int) (this.chunkY * Chunk.SIZE_Y * World.TILE_SIZE);
			
			for(int x = 0; x < this.chunkSizeX; x++) {
				for(int y = 0; y < this.chunkSizeY; y++) {
					Tile tile = this.tiles[x][y];
					
//					renderer.texture(tile.getType().getTexture(), startX + (x * World.TILE_SIZE), startY + (y * World.TILE_SIZE), World.TILE_SIZE, World.TILE_SIZE, 0xFFFFFFFF);
				}
			}
		}
	}
	
	public Tile[][] getTiles() {
		return tiles;
	}
	
	public int getChunkX() {
		return chunkX;
	}
	
	public int getChunkY() {
		return chunkY;
	}
	
	public int getChunkSizeX() {
		return chunkSizeX;
	}
	
	public int getChunkSizeY() {
		return chunkSizeY;
	}
}

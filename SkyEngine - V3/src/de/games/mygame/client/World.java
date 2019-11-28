package de.games.mygame.client;

import java.util.ArrayList;
import java.util.List;

import de.games.factory.entity.EntityPlayer;
import de.games.factory.world.tile.Tile;
import de.games.mygame.chunk.Chunk;
import de.games.mygame.tile.objects.Belt;
import de.skyengine.core.Input;
import de.skyengine.core.SkyEngine;
import de.skyengine.graphics.ImmediateRenderer;
import de.skyengine.graphics.ImmediateRenderer.ShapeType;
import de.skyengine.utils.math.Vector2f;

public class World {
	
	private String name;
	
	private Chunk[][] chunks;
	private List<Chunk> loadedChunks;
	
	private EntityPlayer player;
	
	// Selected Tile (Mouse)
	private Vector2f selectedTile;
	private Belt belt;
	
	public static final float TILE_SIZE = 64.0F;
	
	public World(String name) {
		this.name = name;
		this.loadedChunks = new ArrayList<Chunk>();
		
		this.player = new EntityPlayer(SkyEngine.getWidth() / 2, SkyEngine.getHeight() / 2);
		
		
		this.selectedTile = new Vector2f();
		this.selectedTile.set(-1, -1);
	}
	
	public void input(Input input) {
		this.player.input(input);
		this.updateSelectedTile(input.getMouseX(), input.getMouseY());
	}
	
	public void update() {
		this.player.update();
		
		this.updateLoadedChunks();
		
		Belt.updateBeltAnimation(1);
	}
	
	public void render(ImmediateRenderer renderer, float partialTicks) {
		if(this.chunks != null) {
			for(Chunk chunk : this.loadedChunks) {
				chunk.render(renderer, partialTicks);
			}
			
			if(this.selectedTile != null && this.selectedTile.x != -1 && this.selectedTile.y != -1) {
				renderer.lineWidth(0.5F);
				renderer.rect(ShapeType.LINE, this.selectedTile.x, this.selectedTile.y, this.selectedTile.x + World.TILE_SIZE, this.selectedTile.y + World.TILE_SIZE, 0xFFFFFFFF);
			}
			
			// OUTLINE
//			for(int cX = 0; cX < this.chunks.length; cX++) {
//				for(int cY = 0; cY < this.chunks[this.chunks.length - 1].length; cY++) {
//					float x = Chunk.SIZE_X * World.TILE_SIZE;
//					float y = Chunk.SIZE_Y * World.TILE_SIZE;
//					
//					renderer.rect(ShapeType.LINE, cX * x, cY * x, cX * x + x, cY * y + y, 0xFFFFFFFF);
//				}
//			}
		}
		
		if(this.belt != null) {
			this.belt.render(renderer, partialTicks);
		}
		
		this.player.render(renderer, partialTicks);
	}
	
	/**
	 * @param worldX	| from 0 to screechWidth
	 * @param worldY 	| from 0 to screenHeight
	 */
	public void updateSelectedTile(double worldX, double worldY) {
		int chunkX = (int) Math.floor(worldX / World.TILE_SIZE / (Chunk.SIZE_X + 0.0D));
		int chunkY = (int) Math.floor(worldY / World.TILE_SIZE / (Chunk.SIZE_Y + 0.0D));
		
		Chunk chunk = this.chunks[chunkX][chunkY];
		
		if(this.loadedChunks.contains(chunk)) {
			int tileX = (int) (Math.floor(worldX / World.TILE_SIZE) % Chunk.SIZE_X);
			int tileY = (int) (Math.floor(worldY / World.TILE_SIZE) % Chunk.SIZE_Y);
			
			Tile tile = chunk.getTiles()[tileX][tileY];
			if(tile != null) {
//				this.selectedTile.set(tile.getX(), tile.getY());
			} else {
				this.selectedTile.set(-1, -1);
			}
		} else {
			this.selectedTile.set(-1, -1);
		}
	}
	
	private void updateLoadedChunks() {
		this.loadedChunks.clear();
		
		Vector2f pos = this.player.position;
		
		int playerChunkX = (int) Math.floor((pos.x + this.player.width / 2.0F) / World.TILE_SIZE / (Chunk.SIZE_X + 0.0D));
		int playerChunkY = (int) Math.floor((pos.y + this.player.height / 2.0F) / World.TILE_SIZE / (Chunk.SIZE_Y + 0.0D));
		
		this.addToLoadedChunks(playerChunkX, playerChunkY);
		
		this.addToLoadedChunks(playerChunkX - 1, playerChunkY - 1);
		this.addToLoadedChunks(playerChunkX - 1, playerChunkY);
		this.addToLoadedChunks(playerChunkX - 1, playerChunkY + 1);
		
		this.addToLoadedChunks(playerChunkX, playerChunkY - 1);
		this.addToLoadedChunks(playerChunkX, playerChunkY + 1);

		this.addToLoadedChunks(playerChunkX + 1, playerChunkY - 1);
		this.addToLoadedChunks(playerChunkX + 1, playerChunkY);
		this.addToLoadedChunks(playerChunkX + 1, playerChunkY + 1);
	}
	
	private void addToLoadedChunks(int chunkX, int chunkY) {
		if(chunkX >= 0 && chunkX <= this.chunks.length) {
			if(chunkY >= 0 && chunkY <= this.chunks[this.chunks.length - 1].length) {
				this.loadedChunks.add(this.chunks[chunkX][chunkY]);
			}
		}
	}
	
	public void loadMap(Tile[][] map) {
		System.out.println("Loading Map... {MapX: " + map.length + " | MapY: " + map[map.length - 1].length + "}");
		
		int mapX = map.length;
		int mapY = map[map.length - 1].length;
		
		double lengthX = mapX + 0.0D;
		double lengthY = mapY + 0.0D;
		
		int sizeX = (int) Math.ceil(lengthX / Chunk.SIZE_X);
		int sizeY = (int) Math.ceil(lengthY / Chunk.SIZE_Y);
		
		this.chunks = new Chunk[sizeX][sizeY];
		
		for(int cX = 0; cX < sizeX; cX++) {
			for(int cY = 0; cY < sizeY; cY++) {
				this.chunks[cX][cY] = new Chunk(map, cX, cY, cX * Chunk.SIZE_X, cY * Chunk.SIZE_Y);
			}
		}
		
		this.belt = new Belt(this.chunks[0][0].getTiles()[1][1]);
	}
	
	public Tile[][] exampleMap(int sizeX, int sizeY) {
		Tile[][] tiles = new Tile[sizeX][sizeY];
		
		for(int x = 0; x < sizeX; x++) {
			for(int y = 0; y < sizeY; y++) {
//				tiles[x][y] = new Tile(x * World.TILE_SIZE, y * World.TILE_SIZE, World.TILE_SIZE, World.TILE_SIZE, TileType.DIRT);
			}
		}
		return tiles;
	}
	
	public String getName() {
		return name;
	}
	
	public Chunk[][] getChunks() {
		return chunks;
	}
}



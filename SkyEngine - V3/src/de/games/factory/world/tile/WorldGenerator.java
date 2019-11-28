package de.games.factory.world.tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.games.factory.world.tile.tiles.TileWater;
import de.skyengine.utils.MathUtils;
import de.skyengine.utils.math.Vector2i;

public class WorldGenerator {

	private Random random;
	
	private int width;
	private int height;
	
	public WorldGenerator(int width, int height, long seed) {
		this.random = new Random();
		this.random.setSeed(seed);
		
		this.width = width;
		this.height = height;
	}
	
	@SuppressWarnings("unused")
	public Tile[][] generate(int islandsCount, int minIslandSize, int maxIslandSize) {
		Tile[][] map = new Tile[this.width][this.height];
		
		// Fill with Water
		for(int x = 0; x < map.length; x++) {
			for(int y = 0; y < map[map.length - 1].length; y++) {
				map[x][y] = new TileWater();
			}
		}
		
		// Create Island Points
		List<Island> islands = new ArrayList<Island>();
		for(int i = 0; i < islandsCount; i++) {
			Island island = new Island(this.randomVector(), MathUtils.random(minIslandSize, maxIslandSize));
			islands.add(island);
		}
		
		for(Island island : islands) {
			
		}
		return map;
	}
	
	@SuppressWarnings("unused")
	public class Island {
		private Vector2i center;
		private int radius;
		
		public Island(Vector2i center, int radius) {
			this.center = center;
			this.radius = radius;
		}
	}
	
	private Vector2i randomVector() {
		Vector2i vector = new Vector2i();
		vector.x = this.random.nextInt(this.width);
		vector.y = this.random.nextInt(this.height);
		return vector;
	}
}

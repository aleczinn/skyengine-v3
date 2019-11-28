package de.games.factory;

import java.util.HashSet;
import java.util.Set;

import org.lwjgl.glfw.GLFW;

import de.games.factory.entity.Entity;
import de.games.factory.entity.EntityPlayer;
import de.games.factory.world.object.GameObject;
import de.games.factory.world.object.objects.Belt;
import de.games.factory.world.tile.Tile;
import de.games.factory.world.tile.WorldGenerator;
import de.games.factory.world.tile.tiles.TileDirt;
import de.skyengine.core.Input;
import de.skyengine.core.SkyEngine;
import de.skyengine.graphics.Camera;
import de.skyengine.graphics.ImmediateRenderer;
import de.skyengine.graphics.ImmediateRenderer.ShapeType;
import de.skyengine.utils.MathUtils;
import de.skyengine.utils.math.Vector2i;

public class FactoryWorld {

	private String name;
	
	private EntityPlayer player;
	
	private Tile[][] map;
	private GameObject[][] objects;
	private Set<Entity> entities;
	
	private Camera camera;
	
	public static final int TILE_SIZE = 64;
	
	private WorldGenerator generator;
	
	public FactoryWorld(String name) {
		this.name = name;
		
		int mapSizeX = 24;
		int mapSizeY = 12;
		
		this.map = new Tile[mapSizeX][mapSizeY];
		this.objects = new GameObject[mapSizeX][mapSizeY];
		
		this.generator = new WorldGenerator(40, 40, MathUtils.RANDOM.nextLong());
		this.map = this.generator.generate(4, 3, 10);
		
//		for(int x = 0; x < this.tiles.length; x++) {
//			for(int y = 0; y < this.tiles[this.tiles.length - 1].length; y++) {
//				this.tiles[x][y] = new TileWater();
//			}
//		}
		
		this.player = new EntityPlayer(12 * FactoryWorld.TILE_SIZE, 6 * FactoryWorld.TILE_SIZE);
		
		this.entities = new HashSet<Entity>();
		this.entities.add(this.player);
		
		this.camera = new Camera(SkyEngine.getWidth() / 2.0F, SkyEngine.getHeight() / 2.0F);
		this.camera.resize(this.player, SkyEngine.getWidth(), SkyEngine.getHeight());
	}
	
	public void input(Input input) {
		this.player.input(input);
		
		float mouseX = this.camera.position.x + input.getMouseX();
		float mouseY = this.camera.position.y + input.getMouseY();
		
		if(input.isMouseDown(GLFW.GLFW_MOUSE_BUTTON_1)) {
			this.setObjectInWorld(new Belt(), mouseX, mouseY);
		}
		
		if(input.isMouseDown(GLFW.GLFW_MOUSE_BUTTON_3)) {
			this.setTileInWorld(new TileDirt(), mouseX, mouseY);
		}
		
		if(input.isKeyPressed(GLFW.GLFW_KEY_G)) {
			this.map = this.generator.generate(4, 3, 10);
			System.out.println("FactoryWorld.java - Generate New World.");
		}
	}
	
	public void update() {
		this.camera.update(this.player, 0.16F);
		
		for(int x = 0; x < this.map.length; x++) {
			for(int y = 0; y < this.map[this.map.length - 1].length; y++) {
				Tile tile = this.map[x][y];
				
				tile.update();
			}
		}
		
		for(int x = 0; x < this.objects.length; x++) {
			for(int y = 0; y < this.objects[this.objects.length - 1].length; y++) {
				GameObject object = this.objects[x][y];
				
				if(object != null) {
					object.update();
				}
			}
		}
		
		for(Entity entity : this.entities) {
			entity.update();
		}
	}
	
	public void render(ImmediateRenderer renderer, float mouseX, float mouseY, float partialTicks) {
		float cameraX = this.camera.position.x;
		float cameraY = this.camera.position.y;
		
		this.camera.begin(partialTicks);
		
		for(int x = 0; x < this.map.length; x++) {
			for(int y = 0; y < this.map[this.map.length - 1].length; y++) {
				Tile tile = this.map[x][y];
				
				float posX = x * FactoryWorld.TILE_SIZE;
				float posY = y * FactoryWorld.TILE_SIZE;
				
				if(this.isInCamera(cameraX, cameraY, posX, posY, FactoryWorld.TILE_SIZE, FactoryWorld.TILE_SIZE)) {
					tile.render(renderer, posX, posY, partialTicks);
				}
			}
		}
		
		for(int x = 0; x < this.objects.length; x++) {
			for(int y = 0; y < this.objects[this.objects.length - 1].length; y++) {
				GameObject object = this.objects[x][y];
				
				float posX = x * FactoryWorld.TILE_SIZE;
				float posY = y * FactoryWorld.TILE_SIZE;
				
				if(object != null && this.isInCamera(cameraX, cameraY, posX, posY, FactoryWorld.TILE_SIZE, FactoryWorld.TILE_SIZE)) {
					object.render(renderer, posX, posY, partialTicks);
				}
			}
		}
		
		for(Entity entity : this.entities) {
			if(this.isInCamera(cameraX, cameraY, entity.position.x, entity.position.y, entity.width, entity.height)) {
				entity.render(renderer, partialTicks);	
			}
		}

		float sX = (int) Math.floor((mouseX + cameraX) / (FactoryWorld.TILE_SIZE + 0.0F)) * FactoryWorld.TILE_SIZE;
		float sY = (int) Math.floor((mouseY + cameraY) / (FactoryWorld.TILE_SIZE + 0.0F)) * FactoryWorld.TILE_SIZE;
		if(this.isInCamera(cameraX, cameraY, sX, sY, FactoryWorld.TILE_SIZE, FactoryWorld.TILE_SIZE)) {
			renderer.rect(ShapeType.LINE, sX, sY, sX + FactoryWorld.TILE_SIZE, sY + FactoryWorld.TILE_SIZE, 0xFFFFFFFF);	
		}
		this.camera.end();
	}
	
	public void resize(int width, int height) {
		this.camera.resize(this.player, width, height);
	}
	
	private boolean isInCamera(float cameraX, float cameraY, float x, float y, float width, float height) {
		float screenOffsetX = width;
		float screenOffsetY = height;
		
		if(x >= cameraX - width - screenOffsetX && x <= (cameraX + SkyEngine.getWidth() + screenOffsetX)) {
			if(y >= cameraY - height - screenOffsetY && y <= (cameraY + SkyEngine.getHeight() + screenOffsetY)) {
				return true;
			}
		}
		return false;
	}
	
	/* Tile Managment */
	public Tile getTileInWorld(float screenX, float screenY) {
		int x = (int) Math.floor(screenX / (FactoryWorld.TILE_SIZE + 0.0F));
		int y = (int) Math.floor(screenY / (FactoryWorld.TILE_SIZE + 0.0F));
		return this.getTile(x, y);
	}
	
	public Tile getTile(int x, int y) {
		if(x < 0 || x > this.map.length - 1 || y < 0 || y > this.map[this.map.length - 1].length - 1) return null;
		return this.map[x][y];
	}
	
	public void setTileInWorld(Tile tile, float screenX, float screenY) {
		int x = (int) Math.floor(screenX / (FactoryWorld.TILE_SIZE + 0.0F));
		int y = (int) Math.floor(screenY / (FactoryWorld.TILE_SIZE + 0.0F));
		this.setTile(tile, x, y);
	}
	
	public void setTile(Tile tile, int x, int y) {
		if(x < 0 || x > this.map.length - 1 || y < 0 || y > this.map[this.map.length - 1].length - 1) return;
		
		Tile old = this.map[x][y];
		if(old != null) {
			tile.setRotation(old.getRotation());
		}
		
		this.map[x][y] = tile;
		System.out.println("FactoryWorld.java - Set Tile at {" + x + ", y: " + y + "}");
	}
	
	public Vector2i getTilePositionInWorld(float screenX, float screenY) {
		Vector2i vector = new Vector2i();
		vector.x = (int) Math.floor(screenX / (FactoryWorld.TILE_SIZE + 0.0F));
		vector.y = (int) Math.floor(screenY / (FactoryWorld.TILE_SIZE + 0.0F));
		return vector;
	}
	
	/* Object Managment */
	public GameObject getObjectInWorld(float screenX, float screenY) {
		int x = (int) Math.floor(screenX / (FactoryWorld.TILE_SIZE + 0.0F));
		int y = (int) Math.floor(screenY / (FactoryWorld.TILE_SIZE + 0.0F));
		return this.getObject(x, y);
	}
	
	public GameObject getObject(int x, int y) {
		if(x < 0 || x > this.objects.length - 1|| y < 0 || y > this.objects[this.objects.length - 1].length - 1) return null;
		return this.objects[x][y];
	}
	
	public void setObjectInWorld(GameObject object, float screenX, float screenY) {
		int x = (int) Math.floor(screenX / (FactoryWorld.TILE_SIZE + 0.0F));
		int y = (int) Math.floor(screenY / (FactoryWorld.TILE_SIZE + 0.0F));
		this.setObject(object, x, y);
	}
	
	public void setObject(GameObject object, int x, int y) {
		if(object == null || x < 0 || x > this.objects.length - 1|| y < 0 || y > this.objects[this.objects.length - 1].length - 1) return;
		
		this.objects[x][y] = object;
		System.out.println("FactoryWorld.java - Set Object at {" + x + ", y: " + y + "}");
	}
	
	public String getName() {
		return name;
	}
	
	public Camera getCamera() {
		return camera;
	}
	
	public Tile[][] getMap() {
		return map;
	}
	
	public GameObject[][] getObjects() {
		return objects;
	}
	
	public Set<Entity> getEntities() {
		return entities;
	}
	
	public EntityPlayer getPlayer() {
		return player;
	}
}

package de.skyengine.resources.internal;

import java.util.ArrayList;
import java.util.List;

import de.skyengine.core.internal.IDisposable;
import de.skyengine.graphics.internal.Texture;
import de.skyengine.utils.logging.Logger;

public class Textures implements IDisposable {

	private final Logger logger = Logger.getLogger(Textures.class.getName());
	private List<Texture> textures;
	
	public Texture logoPanda;
	
	public Texture characterBlue;
	public Texture characterOrange;
	public Texture characterPink;
	
	public Texture chessBackground;
	public Texture[] chessSprites;
	
	public Texture water;
	public Texture dirt;
	public Texture grass;
	public Texture desertGrass;
	
	public Texture beltRightAtlas;
	
	public Texture backgroundMainMenu;
	
	public Textures() {
		this.textures = new ArrayList<Texture>();
		
		try {
			this.logoPanda = this.loadTexture("assets/textures/logo/panda.png");
			
			this.chessBackground = this.loadTexture("assets/chess/chess-background.png");
			this.chessSprites = new  Texture[12];
			
			// Bauer
			this.chessSprites[0] = this.loadTexture("assets/chess/black/blackPawn.png");
			this.chessSprites[1] = this.loadTexture("assets/chess/white/whitePawn.png");
			
			// Springer
			this.chessSprites[2] = this.loadTexture("assets/chess/black/blackKnight.png");
			this.chessSprites[3] = this.loadTexture("assets/chess/white/whiteKnight.png");
			
			// Läufer
			this.chessSprites[4] = this.loadTexture("assets/chess/black/blackBishop.png");
			this.chessSprites[5] = this.loadTexture("assets/chess/white/whiteBishop.png");
			
			// Turm
			this.chessSprites[6] = this.loadTexture("assets/chess/black/blackRook.png");
			this.chessSprites[7] = this.loadTexture("assets/chess/white/whiteRook.png");
			
			// Dame
			this.chessSprites[8] = this.loadTexture("assets/chess/black/blackQueen.png");
			this.chessSprites[9] = this.loadTexture("assets/chess/white/whiteQueen.png");
			
			// König
			this.chessSprites[10] = this.loadTexture("assets/chess/black/blackKing.png");
			this.chessSprites[11] = this.loadTexture("assets/chess/white/whiteKing.png");
			
			/*
			 *  Factory Game
			 */
			this.water = this.loadTexture("assets/textures/ground/waterAtlas.png");
			this.dirt = this.loadTexture("assets/textures/ground/dirt.png");
			this.grass = this.loadTexture("assets/textures/ground/grass.png");
			this.desertGrass = this.loadTexture("assets/textures/ground/desert-grass.png");
			
			this.beltRightAtlas = this.loadTexture("assets/textures/ground/belt/beltRight.png");
			
			this.backgroundMainMenu = this.loadTexture("assets/textures/background/main-menu-background.png");
			
			this.logger.info("Textures initialized.");	
		} catch (Exception e) {
			this.logger.fatal("Textures -  Could not be initialized!", e);	
			e.printStackTrace();
		}
	}

	private Texture loadTexture(String path) {
		Texture texture = Texture.loadTexture(path);
		this.textures.add(texture);
		return texture;
	}
	
	@Override
	public void dispose() {
		for(Texture texture : this.textures) {
			texture.dispose();
		}
	}
}


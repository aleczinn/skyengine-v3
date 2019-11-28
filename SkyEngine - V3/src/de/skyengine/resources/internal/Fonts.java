package de.skyengine.resources.internal;

import java.awt.FontFormatException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.skyengine.core.internal.IDisposable;
import de.skyengine.graphics.font.TrueTypeFont;
import de.skyengine.utils.logging.Logger;

public class Fonts implements IDisposable {

	private final Logger logger = Logger.getLogger(Sounds.class.getName());
	private List<TrueTypeFont> fonts;
	
	public TrueTypeFont main;
	public TrueTypeFont mainSmall;
	
	public TrueTypeFont splashScreen;
	public TrueTypeFont splashScreenSmall;
	
	public TrueTypeFont debug;
	
	public TrueTypeFont mainMenuTitle;
	public TrueTypeFont mainMenu;
	public TrueTypeFont mainMenuSmall;
	
	public TrueTypeFont calculatorBig;
	public TrueTypeFont calculator;
	
	public Fonts() {
		this.fonts = new ArrayList<TrueTypeFont>();
		
		try {
			this.main = this.loadFont("assets/fonts/roboto/roboto-light.ttf", 22);
			this.mainSmall = this.loadFont("assets/fonts/roboto/roboto-light.ttf", 16);
			
			this.splashScreen = this.loadFont("assets/fonts/imperfecta_regular_rough.ttf", 72);
			this.splashScreenSmall = this.loadFont("assets/fonts/imperfecta_regular_rough.ttf", 18);
			
			this.debug = this.loadFont("assets/fonts/roboto/roboto-light.ttf", 22);
			
			this.mainMenuTitle = this.loadFont("assets/fonts/roboto/roboto-light.ttf", 56);
			this.mainMenu = this.loadFont("assets/fonts/roboto/roboto-light.ttf", 42);
			this.mainMenuSmall = this.loadFont("assets/fonts/roboto/roboto-light.ttf", 42);
			
			this.calculatorBig = this.loadFont("assets/fonts/roboto/roboto-light.ttf", 56);
			this.calculator = this.loadFont("assets/fonts/roboto/roboto-regular.ttf", 28);
			
			this.logger.info("Fonts initialized.");	
		} catch (Exception e) {
			this.logger.fatal("Fonts -  Could not be initialized!", e);	
			e.printStackTrace();
		}
	}

	private TrueTypeFont loadFont(String path, int size) throws FileNotFoundException, FontFormatException, IOException {
		TrueTypeFont font = new TrueTypeFont(path, size);
		this.fonts.add(font);
		return font;
	}
	
	@Override
	public void dispose() {
		for(TrueTypeFont font : this.fonts) {
			font.getTexture().dispose();
		}
	}
}

package de.skyengine;

import de.games.factory.FactoryGame;
import de.skyengine.config.ApplicationConfig;
import de.skyengine.core.SkyEngine;

public class DesktopLauncher {

	public static void main(String[] args) {
		ApplicationConfig config = new ApplicationConfig();
		config.title = "SkyEngine";
		config.version = "v1.0.0";
		config.fullscreen = false;
		config.resizeable = true;
		config.useVSync = false;
		config.msaa = true;
		
		new SkyEngine(new FactoryGame(), config);
	}
}

package de.skyengine.resources;

import de.skyengine.resources.internal.Fonts;
import de.skyengine.resources.internal.Shaders;
import de.skyengine.resources.internal.Sounds;
import de.skyengine.resources.internal.Textures;

public class Resources {

	private static Fonts fonts;
	private static Textures textures;
	private static Sounds sounds;
	private static Shaders shaders;
	
	public static void load() {
		fonts = new Fonts();
		textures = new Textures();
		sounds = new Sounds();
		shaders = new Shaders();
	}
	
	public static Fonts fonts() {
		return fonts;
	}
	
	public static Textures textures() {
		return textures;
	}
	
	public static Sounds sounds() {
		return sounds;
	}
	
	public static Shaders shaders() {
		return shaders;
	}
	
	public static void dispose() {
		fonts.dispose();
		textures.dispose();
		sounds.dispose();
		shaders.dispose();
	}
}

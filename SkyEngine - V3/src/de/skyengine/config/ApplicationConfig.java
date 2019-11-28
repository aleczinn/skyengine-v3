package de.skyengine.config;

import java.awt.Toolkit;

public class ApplicationConfig {

	public String title;
	public String version;
	public int width = 1280;
	public int height = 720;
	
	public boolean fullscreen = false;
	public boolean resizeable = true;
	public boolean useVSync = false;
	public boolean msaa = true;
	
	public boolean gamepadSupport = true;
	public String iconPath16 = "assets/textures/logo/skyengine-logo-big-64.png";
	public String iconPath32 = "assets/textures/logo/skyengine-logo-big-64.png";
	
	public int[] limits = new int[] {640, 480, new DisplayMode().getWidth(), new DisplayMode().getHeight()};
	
	public void setLimits(int minX, int minY, int maxX, int maxY) {
		this.limits = new int[] {minX, minY, maxX, maxX};
	}
	
	public class DisplayMode {
		public int getWidth() {
			return (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		}
		
		public int getHeight() {
			return (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		}
	}
}

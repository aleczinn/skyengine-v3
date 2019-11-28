package de.skyengine.graphics.internal;

public class Color {

	public float red;
	public float green;
	public float blue;
	public float alpha;

	public static Color WHITE = new Color(1.0F, 1.0F, 1.0F, 1.0F);
	public static Color BLACK = new Color(0.0F, 0.0F, 0.0F, 1.0F);
	
	public static Color RED = new Color(1.0F, 0.0F, 0.0F, 1.0F);
	public static Color GREEN = new Color(0.0F, 1.0F, 0.0F, 1.0F);
	public static Color BLUE = new Color(0.0F, 0.0F, 1.0F, 1.0F);
	
	public Color(Color color) {
		this.red = color.red;
		this.green = color.green;
		this.blue = color.blue;
		this.alpha = color.alpha;
	}
	
	public Color(int color) {
		this.alpha = ((color & 0xff000000) >>> 24) / 255F;
		this.red = ((color & 0x00ff0000) >>> 16) / 255F;
		this.green = ((color & 0x0000ff00) >>> 8) / 255F;
		this.blue = ((color & 0x000000ff)) / 255F;
		this.clamp();
	}
	
	public Color(int color, float alpha) {
		this.alpha = alpha;
		this.red = ((color & 0x00ff0000) >>> 16) / 255F;
		this.green = ((color & 0x0000ff00) >>> 8) / 255F;
		this.blue = ((color & 0x000000ff)) / 255F;
		this.clamp();
	}
	
	public Color(float red, float green, float blue, float alpha) {		
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
		this.clamp();
	}
	
	public Color set(Color color) {
		this.red = color.red;
		this.green = color.green;
		this.blue = color.blue;
		this.alpha = color.alpha;
		return this;
	}

	public Color set(float red, float green, float blue, float alpha) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
		return clamp();
	}
	
	public Color set(int color) {
		this.alpha = ((color & 0xff000000) >>> 24) / 255F;
		this.red = ((color & 0x00ff0000) >>> 16) / 255F;
		this.green = ((color & 0x0000ff00) >>> 8) / 255F;
		this.blue = ((color & 0x000000ff)) / 255f;
		return this.clamp();
	}
	
	public Color setAlpha(float alpha) {
		this.alpha = alpha;
		return this.clamp();
	}
	
	public Color hexToRGBA(int color) {
		this.alpha = ((color & 0xff000000) >>> 24) / 255f;
		this.red = ((color & 0x00ff0000) >>> 16) / 255f;
		this.green = ((color & 0x0000ff00) >>> 8) / 255f;
		this.blue = ((color & 0x000000ff)) / 255f;
		return this;
	}
	
	public Color mul(float value) {
		this.red *= value;
		this.green *= value;
		this.blue *= value;
		this.alpha *= value;
		return clamp();
	}

	public Color clamp() {
		if (red < 0)
			red = 0;
		else if (red > 1)
			red = 1;

		if (green < 0)
			green = 0;
		else if (green > 1)
			green = 1;

		if (blue < 0)
			blue = 0;
		else if (blue > 1)
			blue = 1;

		if (alpha < 0)
			alpha = 0;
		else if (alpha > 1)
			alpha = 1;
		return this;
	}
	
	public int toHex() {
		String red = this.formatHex(Integer.toHexString(Math.round(this.red * 255.0F)));
		String green = this.formatHex(Integer.toHexString(Math.round(this.green * 255.0F)));
		String blue = this.formatHex(Integer.toHexString(Math.round(this.blue * 255.0F)));
	    String alpha = this.formatHex(Integer.toHexString(Math.round(this.alpha * 255.0F)));
	    
	    String hex = alpha + red + green + blue;
	    return (int) Long.parseLong(hex, 16);
	}
	
	private String formatHex(String s) {
		return s.length() == 1 ? 0 + s : s;
	}
	
	public java.awt.Color getColor() {
		return new java.awt.Color(this.red, this.green, this.blue, this.alpha);
	}
	
	public java.awt.Color getColor(float alpha) {
		return new java.awt.Color(this.red, this.green, this.blue, alpha);
	}
	
	public Color copy () {
		return new Color(this);
	}
}

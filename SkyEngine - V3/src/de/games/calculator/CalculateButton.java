package de.games.calculator;

public class CalculateButton {

	private int buttonID;
	
	private float x;
	private float y;
	
	private float width;
	private float height;
	
	private String displayText;
	
	private boolean visible;
	
	public CalculateButton(int buttonID, float x, float y, float width, float height, String displayText) {
		this.buttonID = buttonID;
		
		this.x = x;
		this.y = y;
		
		this.width = width;
		this.height = height;
		
		this.displayText = displayText;
		this.visible = true;
	}
	
	public int getButtonID() {
		return buttonID;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
	
	public String getDisplayText() {
		return displayText;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}

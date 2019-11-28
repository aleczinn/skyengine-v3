package de.games.factory.ui;

import de.games.factory.item.ItemStack;

public class UISlot {

	private float x;
	private float y;
	
	private ItemStack stack;
	
	public static final float SLOT_WIDTH = 48.0F;
	
	public UISlot(float x, float y) {
		this(x, y, null);
	}
	
	public UISlot(float x, float y, ItemStack stack) {
		this.x = x;
		this.y = y;
		
		this.stack = stack;
	}
	
	public float getX() {
		return x;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public ItemStack getStack() {
		return stack;
	}
}

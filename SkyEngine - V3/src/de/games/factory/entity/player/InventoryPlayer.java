package de.games.factory.entity.player;

import de.games.factory.entity.EntityPlayer;
import de.games.factory.item.ItemStack;

public class InventoryPlayer {

	private EntityPlayer player;
	
	/** This is the selected Stack **/
	private ItemStack stack;
	
	public InventoryPlayer(EntityPlayer player) {
		this.player = player;
		this.stack = ItemStack.NULL;
	}
	
	public EntityPlayer getPlayer() {
		return player;
	}
	
	public ItemStack getStack() {
		return stack;
	}
	
	public void setStack(ItemStack stack) {
		this.stack = stack;
	}
}

package de.games.factory.item;

public class ItemStack {

	public static final ItemStack NULL = new ItemStack((Item) null);
	
	private Item item;
	
    /** Size of the stack. */
    private int stackSize;
	
	private int maxStackSize;
	
	public ItemStack(Item item) {
		this(item, 1, 64);
	}
	
	public ItemStack(Item item, int amount) {
		this(item, amount, 64);
	}
	
	public ItemStack(Item item, int amount, int maxStackSize) {
		this.item = item;
		this.maxStackSize = maxStackSize;
	}
	
	public Item getItem() {
		return item;
	}
	
	public int getStackSize() {
		return stackSize;
	}
	
	public void setStackSize(int stackSize) {
		this.stackSize = stackSize;
	}
	
	public int getMaxStackSize() {
		return maxStackSize;
	}
	
	public void setMaxStackSize(int maxStackSize) {
		this.maxStackSize = maxStackSize;
	}
}

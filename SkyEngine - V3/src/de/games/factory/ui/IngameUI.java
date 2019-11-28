package de.games.factory.ui;

import java.util.HashSet;
import java.util.Set;

import de.games.factory.world.tile.Tile;
import de.skyengine.core.SkyEngine;
import de.skyengine.graphics.ImmediateRenderer;
import de.skyengine.utils.MathUtils;

public class IngameUI {

	private Set<UISlot> hotbarSlots;
	
	private MiniMap miniMap;
	
	public IngameUI() {
		this.hotbarSlots = new HashSet<UISlot>();
		this.miniMap = new MiniMap(210.0F, 10.0F, 200.0F, 200.0F);
		
		this.prepareHotbar(5.0F);
	}
	
	public void update(Tile[][] map) {
		this.miniMap.update(map);
	}
	
	public void render(ImmediateRenderer renderer, float partialTicks) {
		this.renderHotbar(renderer, partialTicks);
		this.miniMap.render(renderer, partialTicks);
	}
	
	public void resize(int width, int height) {
		this.prepareHotbar(5.0F);
		this.miniMap.resize(width, height);
	}
	
	private void renderHotbar(ImmediateRenderer renderer, float partialTicks) {
		for(UISlot slot : this.hotbarSlots) {
			boolean mouseOver = MathUtils.intersect(SkyEngine.input().getMouseX(), SkyEngine.input().getMouseY(), slot.getX(), slot.getY(), slot.getX() + UISlot.SLOT_WIDTH, slot.getY() + UISlot.SLOT_WIDTH);
			
			renderer.rect(slot.getX(), slot.getY(), slot.getX() + UISlot.SLOT_WIDTH, slot.getY() + UISlot.SLOT_WIDTH, mouseOver ? 0x90EEEEEE : 0x90535c68);
		}
	}
	
	private void prepareHotbar(float spaceBetweenSlots) {
		float slotX = SkyEngine.getWidth() / 2.0F - (4.0F * UISlot.SLOT_WIDTH) - (4.0F * spaceBetweenSlots) - (UISlot.SLOT_WIDTH / 2.0F);
		float hotbarY = SkyEngine.getHeight() - 60;
		
		if(this.hotbarSlots.isEmpty()) {
			for(int slot = 0; slot < 9; slot++) {
				this.hotbarSlots.add(new UISlot(slotX, hotbarY));
				slotX += UISlot.SLOT_WIDTH + spaceBetweenSlots;
			}
		} else {
			for(UISlot slot : this.hotbarSlots) {
				slot.setX(slotX);
				slot.setY(hotbarY);
				
				slotX += UISlot.SLOT_WIDTH + spaceBetweenSlots;
			}
		}
	}
}

package de.games.factory.entity;

import org.lwjgl.glfw.GLFW;

import de.games.factory.entity.player.InventoryPlayer;
import de.skyengine.core.Input;
import de.skyengine.core.internal.Axis;
import de.skyengine.graphics.ImmediateRenderer;
import de.skyengine.utils.MathUtils;
import de.skyengine.utils.math.Vector2f;

public class EntityPlayer extends EntityLivingBase {
	
	private final float WALK_SPEED = 10;
	private final float SPRINT_SPEED = this.WALK_SPEED * 1.8F;
	
	public InventoryPlayer inventory = new InventoryPlayer(this);
	
	public EntityPlayer(float x, float y) {
		super(x, y, 40, 80);
	}

	public void input(Input input) {
		float h = input.getAxisRaw(Axis.HORIZONTAL);
		float v = input.getAxisRaw(Axis.VERTICAL);
		
		float speed = input.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT) ? this.SPRINT_SPEED : this.WALK_SPEED;
		
		this.velocity.set(h * speed, v * speed);
	}

	@Override
	public void update() {
		super.update();
	}

	@Override
	public void render(ImmediateRenderer renderer, float partialTicks) {
		Vector2f interpolation = MathUtils.interpolate(this.position, this.lastPosition, partialTicks);
		
		renderer.rect(interpolation.x, interpolation.y - this.height, interpolation.x + this.width, interpolation.y, 0xFFFFFFFF);
		renderer.point(interpolation.x, interpolation.y, 5.0F, 0xFFFF0000);
	}
}

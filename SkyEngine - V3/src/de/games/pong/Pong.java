package de.games.pong;

import org.lwjgl.glfw.GLFW;

import de.skyengine.core.GameContainer;
import de.skyengine.core.Input;
import de.skyengine.core.SkyEngine;
import de.skyengine.graphics.ImmediateRenderer;
import de.skyengine.utils.MathUtils;
import de.skyengine.utils.math.Vector2f;

public class Pong extends GameContainer {

	public ImmediateRenderer renderer;
	
	private Vector2f ballPosition;
	private Vector2f lastBallPosition;
	
	private Vector2f ballVelocity;
	
	private float paddleLeftY;
	private float lastPaddleLeftY;
	
	private float paddleRightY;
	private float lastPaddleRightY;
	
	private float paddleLeftHeight;
	private float paddleRightHeight;
	
	@Override
	public void init() {
		super.init();
		
		this.renderer = new ImmediateRenderer();
		
		this.ballPosition = new Vector2f();
		this.ballPosition.set(SkyEngine.getWidth() / 2.0F, SkyEngine.getHeight() / 2.0F);
		
		this.lastBallPosition = new Vector2f();
		this.lastBallPosition.set(this.ballPosition);
		
		this.ballVelocity = new Vector2f();
		this.ballVelocity.set(MathUtils.RANDOM.nextBoolean() ? -12 : 12, 0);
		
		this.paddleLeftHeight = 200.0F;
		this.paddleRightHeight = 200.0F;
		
		this.paddleLeftY = SkyEngine.getHeight() / 2.0F - (this.paddleLeftHeight / 2.0F);
		this.paddleRightY = SkyEngine.getHeight() / 2.0F - (this.paddleRightHeight / 2.0F);
		
		this.lastPaddleLeftY = this.paddleLeftY;
		this.lastPaddleRightY = this.paddleRightY;
	}

	@Override
	public void input(Input input) {
		super.input(input);
		
		this.lastPaddleRightY = this.paddleRightY;
		this.paddleRightY = (float) input.getMouseY() - (this.paddleRightHeight / 2.0F);
		
		if(input.isKeyPressed(GLFW.GLFW_KEY_F)) {
			this.ballVelocity.set(MathUtils.RANDOM.nextBoolean() ? -12 : 12, 0);
		}
	}
	
	@Override
	public void update() {
		super.update();
		
		this.lastBallPosition.set(this.ballPosition);
		this.ballPosition.addIntern(this.ballVelocity);
		
		this.updateCollision();
	}
	
	@Override
	public void render(float mouseX, float mouseY, float partialTicks) {
		super.render(mouseX, mouseY, partialTicks);
		
		float width = SkyEngine.getWidth();
		float height = SkyEngine.getHeight();
		
		// Center Line
		this.renderer.line(width / 2, 0, width/2, height, 0xFFFFFFFF);
		
		// Paddle Left
		float newLeftY = MathUtils.interpolate(this.paddleLeftY, this.lastPaddleLeftY, partialTicks);
		this.renderer.rect(5, newLeftY, 10, newLeftY + this.paddleLeftHeight, 0xFFFFFFFF);

		// Paddle Right
		float newRightY = MathUtils.interpolate(this.paddleRightY, this.lastPaddleRightY, partialTicks);
		this.renderer.rect(width - 10, newRightY, width - 5, newRightY + this.paddleRightHeight, 0xFFFFFFFF);
		
		// Ball
		Vector2f ball = MathUtils.interpolate(this.ballPosition, this.lastBallPosition, partialTicks);
		this.renderer.circle(ball.x, ball.y, 10.0F, 0xFFFFFFFF);
	}
	
	@Override
	public void exitGame() {
		super.exitGame();
	}
	
	private void updateCollision() {
		
	}
}

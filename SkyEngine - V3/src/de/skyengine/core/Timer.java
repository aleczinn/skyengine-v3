package de.skyengine.core;

import org.lwjgl.glfw.GLFW;

public class Timer {

	private double lastLoopTime;
	private float timeCount;
	
	private int fps;
	private int fpsCount;
	
	private int ups;
	private int upsCount;

	public void init() {
		this.lastLoopTime = getTime();
	}

	public double getTime() {
		return GLFW.glfwGetTime();
	}

	public float getDelta() {
		double time = getTime();
		float delta = (float) (time - lastLoopTime);
		this.lastLoopTime = time;
		this.timeCount += delta;
		return delta;
	}

	public void updateFPS() {
		this.fpsCount++;
	}

	public void updateUPS() {
		this.upsCount++;
	}

	// Update the FPS and UPS if a whole second has passed
	public void update() {
		if(this.timeCount > 1F) {
			this.fps = this.fpsCount;
			this.fpsCount = 0;

			this.ups = this.upsCount;
			this.upsCount = 0;

			this.timeCount -= 1F;
		}
	}

	public int getFramesPerSecond() {
		return this.fps > 0 ? this.fps : this.fpsCount;
	}

	public int getUpdatesPerSecond() {
		return this.ups > 0 ? this.ups : this.upsCount;
	}

	public double getLastLoopTime() {
		return lastLoopTime;
	}
}


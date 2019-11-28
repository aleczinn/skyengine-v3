package de.skyengine.core;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.glfw.GLFW;

public class GamePad {

	private String name;
	
	private ByteBuffer buttons;
	
	private FloatBuffer axes;
	
	public void update() {
		this.name = GLFW.glfwGetGamepadName(GLFW.GLFW_JOYSTICK_1);
		this.buttons = GLFW.glfwGetJoystickButtons(GLFW.GLFW_JOYSTICK_1);
		this.axes = GLFW.glfwGetJoystickAxes(GLFW.GLFW_JOYSTICK_1);
	}
	
	/*
	 * AXIS 
	 */
	public float getAxisStickL_LeftRight() {
		return  this.axes != null ? this.axes.get(0) : 0;
	}
	
	public float getAxisStickL_UpDown() {
		return  this.axes != null ? this.axes.get(1) : 0;
	}
	
	public float getAxisStickR_LeftRight() {
		return  this.axes != null ? this.axes.get(2) : 0;
	}
	
	public float getAxisStickR_UpDown() {
		return  this.axes != null ? this.axes.get(3) : 0;
	}
	
	// Meist als Gas Taste benutzt
	public float getAxisRight() {
		return  this.axes != null ? this.axes.get(4) : 0;
	}
	
	// Meist als Bremsen Taste benutzt
	public float getAxisLeft() {
		return  this.axes != null ? this.axes.get(5) : 0;
	}
	
	/*
	 * Menü Tasten
	 */
	public boolean getButtonStart_Options() {
		return this.buttons != null ? this.buttons.get(7) == 1 : false;
	}
	
	public boolean getButtonBack_Share() {
		return this.buttons != null ? this.buttons.get(6) == 1 : false;
	}
	
	/*
	 * Sticks (Buttons)
	 */
	public boolean getButtonStickL() {
		return this.buttons != null ? this.buttons.get(8) == 1 : false;
	}
	
	public boolean getButtonStickR() {
		return this.buttons != null ? this.buttons.get(9) == 1 : false;
	}
	
	// Meist als Sprungknopf benutzt [Xbox: Green]
	public boolean getButtonA_Kreuz() {
		return this.buttons != null ? this.buttons.get(0) == 1 : false;
	}
	
	// Meist als Angriffstaste benutzt [Xbox: Blue]
	public boolean getButtonX_Quad() {
		return this.buttons != null ? this.buttons.get(2) == 1 : false;
	}
	
	// [Xbox: Yellow]
	public boolean getButtonY_Triangle() {
		return this.buttons != null ? this.buttons.get(3) == 1 : false;
	}
	
	// Meist als Schleichentaste benutzt [Xbox: Red]
	public boolean getButtonB_Circle() {
		return this.buttons != null ? this.buttons.get(1) == 1 : false;
	}
	
	/*
	 * Pfeiltasten
	 */
	public boolean getButtonCrosshairsUp() {
		return this.buttons != null ? this.buttons.get(10) == 1 : false;
	}
	
	public boolean getButtonCrosshairsDown() {
		return this.buttons != null ? this.buttons.get(12) == 1 : false;
	}
	
	public boolean getButtonCrosshairsLeft() {
		return this.buttons != null ? this.buttons.get(13) == 1 : false;
	}
	
	public boolean getButtonCrosshairsRight() {
		return this.buttons != null ? this.buttons.get(11) == 1 : false;
	}
	
	/*
	 * Schräge Pfeiltasten -> Xbox
	 */
	public boolean getButtonCrosshairsUpRight() {
		return this.getButtonCrosshairsUp() && this.getButtonCrosshairsRight();
	}
	
	public boolean getButtonCrosshairsUpLeft() {
		return this.getButtonCrosshairsUp() && this.getButtonCrosshairsLeft();
	}
	
	public boolean getButtonCrosshairsDownRight() {
		return this.getButtonCrosshairsDown() && this.getButtonCrosshairsRight();
	}
	
	public boolean getButtonCrosshairsDownLeft() {
		return this.getButtonCrosshairsDown() && this.getButtonCrosshairsLeft();
	}
	
	/*
	 * Zeigefinger Tasten (Meist ÜBER Gas/Bremsen)
	 */
	public boolean getButtonRight() {
		return this.buttons != null ? this.buttons.get(5) == 1 : false;
	}
	
	public boolean getButtonLeft() {
		return this.buttons != null ? this.buttons.get(4) == 1 : false;
	}
	
	public String getName() {
		return name;
	}
}

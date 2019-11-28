package de.skyengine.core;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

import de.skyengine.core.internal.Axis;

public class Input {
	
	private Cursor cursor;
	private Mouse mouse;
	private MouseScroll mouseScroll;
	private Keyboard keyboard;
	
	public static int KEY_FORWARD = GLFW.GLFW_KEY_W;
	public static int KEY_BACKWARD = GLFW.GLFW_KEY_S;
	
	public static int KEY_LEFT = GLFW.GLFW_KEY_A;
	public static int KEY_RIGHT = GLFW.GLFW_KEY_D;
	
	public static int KEY_JUMP = GLFW.GLFW_KEY_SPACE;
	
	private boolean useGamepad = false;
	private GamePad gamePad;
	private final float gpMicroMotionX = 0.18F;
	private final float gpMicroMotionY = 0.18F;
	
	public Input(boolean useGamepad) {
		this.useGamepad = useGamepad;
		
		this.cursor = new Cursor();
		this.mouse = new Mouse();
		this.mouseScroll = new MouseScroll();
		this.keyboard = new Keyboard();
		
		this.gamePad = new GamePad();
	}
	
	public float getMouseX() {
		return this.cursor.mouseX;
	}
	
	public float getMouseY() {
		return this.cursor.mouseY;
	}
	
	// Keyboard
	public boolean isKeyPressed(int key) {
		if(this.keyboard.keys[key] && !this.keyboard.keyStates[key]) {
			this.keyboard.keyStates[key] = true;	
			return true;
		}
		return false;
	}
	
	public boolean isKeyDown(int key) {
		return this.keyboard.keys[key];
	}
	
	public boolean isKeyReleased(int key) {
		return !this.keyboard.keys[key];
	}
	
	// Mouse
	public boolean isMousePressed(int button) {
		if(this.mouse.buttons[button] && !this.mouse.buttonStates[button]) {
			this.mouse.buttonStates[button] = true;	
			return true;
		}
		return false;
	}
	
	public boolean isMouseDown(int button) {
		return this.mouse.buttons[button];
	}
	
	public boolean isMouseReleased(int button) {
		return !this.mouse.buttons[button];
	}
	
	public float getScrollX() {
		return this.mouseScroll.offsetX;
	}
	
	public float getScrollY() {
		return this.mouseScroll.offsetY;
	}
	
	public float getAxisRaw(Axis axis) {
		switch (axis) {
		case HORIZONTAL:
			boolean left = this.isKeyDown(Input.KEY_LEFT);
			boolean right = this.isKeyDown(Input.KEY_RIGHT);
			
			if(this.useGamepad) {
				float l = left ? -1.0F : 0.0F;
				float r = right ? 1.0F : 0.0F;
				float a = this.gamePad.getAxisStickL_LeftRight();
				
				float result =  l + r + a;
				
				if(Math.abs(result) < this.gpMicroMotionX) return 0;
				return result < -1.0F ? -1.0F : result > 1.0F ? 1.0F : result;
			}
			return left ? -1.0F : right ? 1.0F : 0.0F;
		case VERTICAL:
			boolean forward = this.isKeyDown(Input.KEY_FORWARD);
			boolean backward = this.isKeyDown(Input.KEY_BACKWARD);
			
			if(this.useGamepad) {
				float f = forward ? -1.0F : 0.0F;
				float b = backward ? 1.0F : 0.0F;
				float a = this.gamePad.getAxisStickL_UpDown();
				
				float result =  f + b + a;
				
				if(Math.abs(result) < this.gpMicroMotionY) return 0;
				return result < -1.0F ? -1.0F : result > 1.0F ? 1.0F : result;
			}
			return forward ? -1.0F : backward ? 1.0F : 0.0F;
		default:
			break;
		}
		return 0;
	}
	
	public boolean getJumpDown() {
		return this.isKeyDown(Input.KEY_JUMP) || (this.useGamepad ? this.gamePad.getButtonA_Kreuz() : false);
	}
	
	public boolean getJump() {
		return this.isKeyPressed(Input.KEY_JUMP) || (this.useGamepad ? this.gamePad.getButtonA_Kreuz() : false);
	}
	
	public Cursor getCursor() {
		return cursor;
	}
	
	public Mouse getMouse() {
		return mouse;
	}
	
	public MouseScroll getMouseScroll() {
		return mouseScroll;
	}
	
	public Keyboard getKeyboard() {
		return keyboard;
	}
	
	public GamePad getGamePad() {
		return gamePad;
	}
	
	public class Cursor extends GLFWCursorPosCallback {
		public float mouseX;
		public float mouseY;
		
		@Override
		public void invoke(long window, double xPos, double yPos) {
			this.mouseX = (float) xPos;
			this.mouseY = (float) yPos;
		}
	}
	
	public class MouseScroll extends GLFWScrollCallback {
		public float offsetX = 0;
		public float offsetY = 0;
		
		@Override
		public void invoke(long window, double xoffset, double yoffset) {
			this.offsetX = (float) xoffset;
			this.offsetY = (float) yoffset;
		}
	}
	
	public class Mouse extends GLFWMouseButtonCallback {
		public boolean buttons[];
		public boolean buttonStates[];
		
		public Mouse() {
			this.buttons = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];
			this.buttonStates = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];
		}
		
		@Override
		public void invoke(long window, int button, int action, int mods) {
			this.buttons[button] = action != GLFW.GLFW_RELEASE;
			
			if(action == GLFW.GLFW_RELEASE) {
				this.buttonStates[button] = false;
			}
		}
		
		public boolean[] getButtons() {
			return buttons;
		}
	}
	
	public class Keyboard extends GLFWKeyCallback {
		public long window;
		public boolean keys[];
		public boolean keyStates[];
		
		public Keyboard() {
			this.keys = new boolean[GLFW.GLFW_KEY_LAST];
			this.keyStates = new boolean[GLFW.GLFW_KEY_LAST];
		}
		
		@Override
		public void invoke(long window, int key, int scancode, int action, int mods) {
			if(key >= 0) {
				this.window = window;
				this.keys[key] = action != GLFW.GLFW_RELEASE;
				if(action == GLFW.GLFW_RELEASE) {
					this.keyStates[key] = false;
				}
			}
		}
	}
	
	public static class Buttons {
		public static final int LEFT = 0;
		public static final int RIGHT = 1;
		public static final int MIDDLE = 2;
		public static final int BACK = 3;
		public static final int FORWARD = 4;
	}
	
	public static class Keys {
		public static final int SPACE = GLFW.GLFW_KEY_SPACE;
		public static final int Q = GLFW.GLFW_KEY_Q;
		public static final int W = GLFW.GLFW_KEY_W;
		public static final int R = GLFW.GLFW_KEY_R;
		public static final int S = GLFW.GLFW_KEY_S;
	}
}

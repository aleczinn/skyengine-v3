package de.skyengine.core;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.Platform;

import de.skyengine.graphics.internal.Color;
import de.skyengine.utils.BufferUtils;

public class Window {

	private Long windowID;
	
	private String title;
	private int width;
	private int height;
	
	private boolean fullscreen = false;
	private boolean resizable = true;
	private boolean useVsync = false;
	private boolean msaa = true;
	
	private Color clearColor;
	
	private boolean resized = false;
	
	private GLFWWindowSizeCallback windowSizeCallback;
	
	private boolean useGamepad;
	private Input input;
	
	public Window(String title, int width, int height, boolean useGamepad) {
		this.title = title;
		this.width = width;
		this.height = height;
		this.clearColor = Color.BLACK;
		this.useGamepad = useGamepad;
	}
	
	public void create() {
		GLFWErrorCallback.createPrint(System.err).set();
		
		if(!GLFW.glfwInit()) throw new IllegalStateException("Unable to initialize glfw");
		
		GLFW.glfwDefaultWindowHints();
        
        if (Platform.get() == Platform.MACOSX) {
        	GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
        }
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_DEBUG_CONTEXT, GLFW.GLFW_TRUE);
        
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, this.resizable ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
		
		// Multi Sample Anti Aliasing
		if(this.msaa) {
			GLFW.glfwWindowHint(GLFW.GLFW_STENCIL_BITS, 4);
			GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, 4);
		}
		
		if(Platform.get() == Platform.MACOSX) {
			GLFW.glfwWindowHint(GLFW.GLFW_COCOA_RETINA_FRAMEBUFFER, GLFW.GLFW_FALSE);
		}
		
		this.windowID = GLFW.glfwCreateWindow(width, height, title, this.fullscreen ? GLFW.glfwGetPrimaryMonitor() : 0L, MemoryUtil.NULL);
		if (this.windowID == MemoryUtil.NULL) {
			throw new RuntimeException("Failed to create window!");
		}
		
		if (!this.fullscreen) {
			GLFWVidMode vid = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
			GLFW.glfwSetWindowPos(this.windowID, (vid.width() - width) / 2, (vid.height() - height) / 2);
		}
		
		GLFW.glfwMakeContextCurrent(this.windowID);
		GLFW.glfwSwapInterval(this.useVsync ? 1 : 0);
		this.setCallbacks();
		
		GL.createCapabilities();
	}
	
	public void setProjection() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
//		GL11.glOrtho(0, this.width, 0, this.height, 1, -1);	// LEFT BOTTOM 	[.  ]
		GL11.glOrtho(0, this.width, this.height, 0, 1, -1);	// LEFT TOP			['  ]
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        
	}
	
	public void resize() {
		GL11.glViewport(0, 0, this.width, this.height);
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, this.width, this.height, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
		this.resized = false;
	}
	
	public void initGL() {
		GL11.glClearColor(this.clearColor.red, this.clearColor.green, this.clearColor.blue, this.clearColor.alpha);

		this.setProjection();
		
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);

//        GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
	}
	
	public void updateInput() {
		if(this.input != null) {
			if(this.input.getMouseScroll().offsetY > 0 || this.input.getMouseScroll().offsetY < 0) {
				this.input.getMouseScroll().offsetY = 0;	
			}
		}
	}
	
	public void update() {
		if(this.input != null) {
			this.input.getGamePad().update();
		}
	}
	
	private void setCallbacks() {
		this.windowSizeCallback = new GLFWWindowSizeCallback() {
			@Override
			public void invoke(long windowID, int wWidth, int wHeight) {
				width = wWidth;
				height = wHeight;
				resized = true;
			}
		};
		GLFW.glfwSetWindowSizeCallback(this.windowID, this.windowSizeCallback);
		
		this.input = new Input(this.useGamepad);
		GLFW.glfwSetCursorPosCallback(this.windowID, this.input.getCursor());
		GLFW.glfwSetMouseButtonCallback(this.windowID, this.input.getMouse());
		GLFW.glfwSetScrollCallback(this.windowID, this.input.getMouseScroll());
		GLFW.glfwSetKeyCallback(this.windowID, this.input.getKeyboard());
	}
	
	public void setIcon(String path16, String path32) {
		if (this.windowID == 0) {
			System.err.println("Window.java - You must create the Window before you can add an Icon!");
			return;
		}

		if(path16 == null || path32 == null || path16.isEmpty() || path32.isEmpty()) {
			System.err.println("Window.java - Icon Path is not valid!");
			return;
		}
		
		try {
			IntBuffer w = MemoryUtil.memAllocInt(1);
			IntBuffer h = MemoryUtil.memAllocInt(1);
			IntBuffer comp = MemoryUtil.memAllocInt(1);

			{
				ByteBuffer icon16;
				ByteBuffer icon32;
				try {
					icon16 = BufferUtils.ioResourceToByteBuffer(path16, 2048);
					icon32 = BufferUtils.ioResourceToByteBuffer(path32, 4096);
				} catch (Exception e) {
					System.err.println("Icon Path's not found!");
					throw new RuntimeException(e);
				}

				try (GLFWImage.Buffer icons = GLFWImage.malloc(2)) {
					ByteBuffer pixels16 = STBImage.stbi_load_from_memory(icon16, w, h, comp, 4);
					icons.position(0).width(w.get(0)).height(h.get(0)).pixels(pixels16);

					ByteBuffer pixels32 = STBImage.stbi_load_from_memory(icon32, w, h, comp, 4);
					icons.position(1).width(w.get(0)).height(h.get(0)).pixels(pixels32);

					icons.position(0);
					GLFW.glfwSetWindowIcon(this.windowID, icons);

					STBImage.stbi_image_free(pixels32);
					STBImage.stbi_image_free(pixels16);
				}
			}

			MemoryUtil.memFree(comp);
			MemoryUtil.memFree(h);
			MemoryUtil.memFree(w);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setSizeLimits(int minWidth, int minHeight, int maxWidth, int maxHeight) {
		if(this.windowID == null) {
			System.err.println("Create the window first! Then you can set the limits.");
			return;
		}
		GLFW.glfwSetWindowSizeLimits(this.windowID, minWidth, minHeight, maxWidth, maxHeight);
	}
	
	public boolean shouldClose() {
		return GLFW.glfwWindowShouldClose(this.windowID);
	}
	
	public void destroy() {
		// Free the window callbacks and destroy the window
		Callbacks.glfwFreeCallbacks(this.windowID);
		GLFW.glfwDestroyWindow(this.windowID);

		// Terminate GLFW and free the error callback
		GLFW.glfwTerminate();
		GLFW.glfwSetErrorCallback(null).free();
		
		Thread.currentThread().interrupt();
	}
	
	public void setWindowPosition(int x, int y) {
		GLFW.glfwSetWindowPos(this.windowID, x, y);
	}
	
	public void showWindow() {
		if(this.windowID == null) {
			System.err.println("Window cannot be shown! -> WindowID is null");
			return;
		}
		GLFW.glfwShowWindow(this.windowID);
	}
	
	public void hideWindow() {
		GLFW.glfwHideWindow(this.windowID);
	}

	public boolean isWindowsFocused() {
		return GLFW.glfwGetWindowAttrib(this.windowID, GLFW.GLFW_FOCUSED) == 1;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public float getWidthAsFloat() {
		return width;
	}
	
	public float getHeightAsFloat() {
		return height;
	}
	
	public float getHalfWidth() {
		return width / 2.0F;
	}
	
	public float getHalfHeight() {
		return height / 2.0F;
	}
	
	public boolean isFullscreen() {
		return fullscreen;
	}
	
	public void setFullscreen(boolean fullscreen) {
		this.fullscreen = fullscreen;
	}
	
	public boolean isResizable() {
		return resizable;
	}
	
	public void setResizable(boolean resizable) {
		this.resizable = resizable;
	}
	
	public boolean isUseVsync() {
		return useVsync;
	}
	
	public void setUseVsync(boolean useVsync) {
		this.useVsync = useVsync;
	}
	
	public boolean isMSAA() {
		return msaa;
	}
	
	public void setMSAA(boolean msaa) {
		this.msaa = msaa;
	}
	
	public boolean wasResized() {
		return resized;
	}
	
	public Long getID() {
		return windowID;
	}
	
	public void setClearColor(Color color) {
		this.clearColor = color;
	}
	
	public void setClearColor(float red, float green, float blue, float alpha) {
		this.clearColor = new Color(red, green, blue, alpha);
	}
	
	public Input getInput() {
		return input;
	}
}

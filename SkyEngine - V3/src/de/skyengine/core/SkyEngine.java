package de.skyengine.core;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.Platform;

import de.skyengine.config.ApplicationConfig;
import de.skyengine.resources.Resources;
import de.skyengine.sound.AudioEngine;
import de.skyengine.utils.FileUtils;
import de.skyengine.utils.SpecsUtil;

public class SkyEngine {

	private static SkyEngine instance;
	
	private final String engineVersion = "3.0.0-E";
	
	private Thread mainGameLoop;
	private boolean running = false;
	
	private final int UPDATE_CAP = 30;
	
	private Timer timer;
	private GameContainer container;
	private ApplicationConfig config;
	
	private static Window window;
	private static Input input;
	private static AudioEngine audioEngine;
	private static FileUtils files;
	
	public SkyEngine(GameContainer container, ApplicationConfig config) {
		instance = this;
		
		System.setProperty("sun.java2d.opengl", "true");
		if (Platform.get() == Platform.MACOSX) System.setProperty("java.awt.headless", "true");
		
		this.mainGameLoop = new Thread(new Runnable() {
			@Override
			public void run() {
				SkyEngine.this.gameloop(container, config);	
			}
		}, "GameLoop");
		this.mainGameLoop.setName("Client Thread");
		this.mainGameLoop.start();
	}
	
	private void initDisplay(GameContainer container, ApplicationConfig config) {
		window = new Window(config.title == null ? container.getClass().getSimpleName() : config.title + (config.title.equalsIgnoreCase("SkyEngine") ? (" " + this.engineVersion) : ""), config.width, config.height, config.gamepadSupport);
		window.setUseVsync(config.useVSync);
		window.setFullscreen(config.fullscreen);
		window.setResizable(config.resizeable);
		window.setMSAA(config.msaa);
		window.create();
		
		window.setSizeLimits(config.limits[0], config.limits[1], config.limits[2], config.limits[3]);
		if(!config.iconPath16.isEmpty() && config.iconPath16 != null && !config.iconPath32.isEmpty() &&  config.iconPath32 != null) window.setIcon(config.iconPath16, config.iconPath32);

		input = window.getInput();
		audioEngine = new AudioEngine();
		files = new FileUtils();
		
		this.timer = new Timer();
		this.container = container;
		this.config = config;
	}
	
	private void init() {
		this.printInfos();
		
		Resources.load();
		
		this.timer.init();
		this.running = true;
		
		if(this.container != null) {
			this.container.init();
		}
		
		GLUtil.setupDebugMessageCallback();
		window.showWindow();
	}
	
	private void onInput() {
		// Billiglösung um scrollY auf 0 zu setzen
		window.updateInput();
		
		GLFW.glfwPollEvents();
		
		window.update();
		
		if(this.container != null) {
			 this.container.input(input);
		}
	}
	
	private void onUpdate() {
		if(window.wasResized()) {
			window.resize();
			this.container.resize(window.getWidth(), window.getHeight());
		}
		
		if(this.container != null) {
			this.container.update();
		}
	}
	
	private void onRender(float partialTicks) {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		if(this.container != null) {
			this.container.render(input.getMouseX(), input.getMouseY(), partialTicks);
		}
		
		GLFW.glfwSwapBuffers(window.getID());
	}
	
	/**
	 * This method closes the game
	 */
	public void shutdown() {
		this.running = false;
	}
	
	public void gameloop(GameContainer container, ApplicationConfig config) {
		this.initDisplay(container, config);
		this.init();

		window.initGL();
		
		float delta;
		float accumulator = 0F;
		float interval = 1.0F / this.UPDATE_CAP;
		float partialTicks;
		
		long timer = System.currentTimeMillis();
		
		while(this.running) {
			delta = this.timer.getDelta();
			accumulator += delta;
			
			while(accumulator >= interval) {
				this.onInput();
				this.onUpdate();
				this.timer.updateUPS();
				accumulator -= interval;
			}

			partialTicks = accumulator / interval;
			this.onRender(partialTicks);
			this.timer.updateFPS();
			
			if(System.currentTimeMillis() - timer > 1000) {
				System.out.println("FPS: " + this.timer.getFramesPerSecond() + " | Updates: " + this.timer.getUpdatesPerSecond());
				timer += 1000;
				this.timer.update();
			}
			
			if(window.shouldClose()) {
				this.container.exitGame();
				this.shutdown();
			}
		}
		
		Resources.dispose();
		audioEngine.dispose();
		window.destroy();
	}
	
	public static Window application() {
		return window;
	}
	
	public static Input input() {
		return input;
	}
	
	public static AudioEngine sound() {
		return audioEngine;
	}
	
	public static FileUtils files() {
		return files;
	}
	
	public String getEngineVersion() {
		return engineVersion;
	}
	
	public ApplicationConfig getConfig() {
		return config;
	}
	
	public GameContainer getContainer() {
		return container;
	}
	
	public static SkyEngine getInstance() {
		return instance;
	}
	
	public Timer getTimer() {
		return timer;
	}
	
	public int getDebugFPS() {
		return this.timer.getFramesPerSecond();
	}
	
	public int getDebugUPS() {
		return this.timer.getUpdatesPerSecond();
	}
	
	public static int getWidth() {
		return window.getWidth();
	}
	
	public static int getHeight() {
		return window.getHeight();
	}
	
	private void printInfos() {
		System.out.println("---------------------------------------------");
		System.out.println("Using LWJGL " + SpecsUtil.getLWJGLVersion());
		System.out.println("OS: " + SpecsUtil.getOS());
		System.out.println("OpenGL Vendor : " + GL11.glGetString(GL11.GL_VENDOR));
		System.out.println("Driver Version: " + GL11.glGetString(GL11.GL_VERSION));
		System.out.println("OpenGL Renderer : " + GL11.glGetString(GL11.GL_RENDERER));
		System.out.println("Java: " + SpecsUtil.getJava());
		System.out.println("---------------------------------------------");
	}
}

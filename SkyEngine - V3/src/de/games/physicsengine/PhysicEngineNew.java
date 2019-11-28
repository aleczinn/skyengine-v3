package de.games.physicsengine;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import de.games.physicsengine.collision.Contact;
import de.games.physicsengine.internal.PhysicObject;
import de.games.physicsengine.objects.CircleObject;
import de.games.physicsengine.objects.PolygonObject;
import de.games.physicsold.PhysicsEngine;
import de.skyengine.core.GameContainer;
import de.skyengine.core.Input;
import de.skyengine.core.SkyEngine;
import de.skyengine.graphics.ImmediateRenderer;
import de.skyengine.resources.Resources;
import de.skyengine.utils.MathUtils;
import de.skyengine.utils.math.Vector2d;

public class PhysicEngineNew extends GameContainer {

	public ImmediateRenderer renderer;
	
	public static final Vector2d GRAVITY = new Vector2d(0, 9.81D).divideIntern(MathUtils.DT);
	
	private List<PhysicObject> objects;
	private List<Contact> contacts;
	
	public PhysicEngineNew() {
		this.renderer = new ImmediateRenderer();
	}
	
	@Override
	public void init() {
		super.init();
		
		this.objects = new ArrayList<PhysicObject>();
		this.contacts = new ArrayList<Contact>();
		
		CircleObject circle = new CircleObject(SkyEngine.application().getWidth() / 2.0D, SkyEngine.application().getHeight() - 75.0F, 50.0F);
		circle.setStatic();
		this.objects.add(circle);
	}
	
	@Override
	public void input(Input input) {
		super.input(input);
		
		// Circle
		if(input.isMousePressed(GLFW.GLFW_MOUSE_BUTTON_1)) {
			float radius = MathUtils.random(10.0F, 50.0F);
			this.objects.add(new CircleObject(input.getMouseX(), input.getMouseY(), radius));
			System.out.println("Add Circle to World. (" + radius + ")");
		}
		
		if(input.isMousePressed(GLFW.GLFW_MOUSE_BUTTON_2)) {
			if(input.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
				// Polygon
				double r = MathUtils.random(15.0D, 80.0D);
				int vertexCount = MathUtils.random(3, PolygonObject.MAX_POLY_VERTEX_COUNT);

				Vector2d[] vertices = Vector2d.arrayOf(vertexCount);
				for (int i = 0; i < vertexCount; i++) {
					vertices[i].set(MathUtils.random(-r, r), MathUtils.random(-r, r));
				}
				
				PolygonObject polygon = new PolygonObject(input.getMouseX(), input.getMouseY(), vertices);
				polygon.setOrient(MathUtils.random(-MathUtils.PI, MathUtils.PI));
				polygon.restitution = 0.2D;
				polygon.dynamicFriction = 0.2D;
				polygon.staticFriction = 0.4D;
				
				this.objects.add(polygon);
				
				System.out.println("Add Polygon to World.");
			} else {
				// Rectangle
				double width = MathUtils.random(10.0D, 50.0D);
				double height = MathUtils.random(10.0D, 50.0D);
				
				PolygonObject polygon = new PolygonObject(input.getMouseX(), input.getMouseY(), width, height);
				polygon.setOrient(0.0D);
				this.objects.add(polygon);
				
				System.out.println("Add Rectangle to World. (" + width + ", " + height + ")");
			}
		}
	}

	@Override
	public void update() {
		super.update();
		
		// Check for Collisions
		this.contacts.clear();
		
		for(int i = 0; i < this.objects.size(); i++) {
			PhysicObject a = this.objects.get(i);
			
			for(int j = i + 1; j < this.objects.size(); j++) {
				PhysicObject b = this.objects.get(j);
				
				Contact contact = new Contact(a, b);
				contact.solve();
				
				if(contact.contactCount > 0) {
					this.contacts.add(contact);
				}
			}
		}
		
		for(int i = 0; i < this.objects.size(); i++) {
			
		}
	}
	
	@Override
	public void render(float mouseX, float mouseY, float partialTicks) {
		super.render(mouseX, mouseY, partialTicks);
		
		this.renderer.drawString(Resources.fonts().main, "Gravity: " + PhysicsEngine.GRAVITY.toString(), 5, 5, 0xFFFFFFFF);
		this.renderer.drawString(Resources.fonts().main, "Objects: " + this.objects.size(), 5, 30, 0xFFFFFFFF);
		this.renderer.drawString(Resources.fonts().main, "Collisions: " + this.contacts.size(), 5, 30 + 25, 0xFFFFFFFF);
		
		this.renderer.drawString(Resources.fonts().mainSmall, "Mouse L: Circle", 5, SkyEngine.getHeight() - 64, 0xFFFFFFFF);
		this.renderer.drawString(Resources.fonts().mainSmall, "Mouse R: Rectangle", 5, SkyEngine.getHeight() - 48 + 5, 0xFFFFFFFF);
		this.renderer.drawString(Resources.fonts().mainSmall, "Shift + Mouse R: Rectangle", 5, SkyEngine.getHeight() - 32 + 10, 0xFFFFFFFF);
		
		for(Contact collision : this.contacts) {
			collision.render(this.renderer, 8.0F);
		}
		
		for(PhysicObject object : this.objects) {
			object.render(this.renderer, partialTicks);
		}
	}
	
	@Override
	public void exitGame() {
		super.exitGame();
	}
}

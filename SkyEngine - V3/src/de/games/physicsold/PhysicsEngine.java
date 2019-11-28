package de.games.physicsold;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import de.games.physicsengine.collision.Contact;
import de.games.physicsengine.internal.PhysicObject;
import de.games.physicsengine.objects.CircleObject;
import de.skyengine.core.GameContainer;
import de.skyengine.core.Input;
import de.skyengine.core.SkyEngine;
import de.skyengine.graphics.ImmediateRenderer;
import de.skyengine.resources.Resources;
import de.skyengine.utils.MathUtils;
import de.skyengine.utils.math.Vector2d;

public class PhysicsEngine extends GameContainer {

	public ImmediateRenderer renderer;
	
	public static final Vector2d GRAVITY = new Vector2d(0, 9.81D).divideIntern(MathUtils.DT);
	
	public List<PhysicObject> objects;
	public List<Contact> contacts;
	
	public PhysicsEngine() {
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
		
//		this.objects.add(new LineObject(50, 50, 50, 450, PhysicType.STATIC));
//		this.objects.add(new LineObject(50, 450, 300, 300, PhysicType.STATIC));
//		this.objects.add(new LineObject(300, 300, 600, 450, PhysicType.STATIC));
//		this.objects.add(new LineObject(600, 350, 600, 50, PhysicType.STATIC));
//		this.objects.add(new LineObject(600, 500, 1280, 500, PhysicType.STATIC));
	}
	
	@Override
	public void input(Input input) {
		super.input(input);
		
		if(input.isMousePressed(GLFW.GLFW_MOUSE_BUTTON_1)) {
			float radius = MathUtils.random(25.0F, 50.0F);
			this.objects.add(new CircleObject(input.getMouseX(), input.getMouseY(), radius));
			System.out.println("Add GameObject to World. (" + radius + ")");
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
		
		// Integrate Force's
		for(int i = 0; i < this.objects.size(); i++) {
			this.integrateForces(this.objects.get(i), MathUtils.DT);
		}
		
		// Init Collision
		for(int i = 0; i < this.contacts.size(); i++) {
			this.contacts.get(i).init();
		}
		
		// Solve Collision
		for(int j = 0; j < 10; j++) {
			for(int i = 0; i < this.contacts.size(); i++) {
				this.contacts.get(i).applyImpulse();
			}
		}
		
		// Integrate Velocity
		for(int i = 0; i < this.objects.size(); i++) {
			this.integrateVelocity(this.objects.get(i), MathUtils.DT);
		}
		
		// Corret Positions
		for(int i = 0; i < this.contacts.size(); i++) {
			this.contacts.get(i).correct();
		}
		
		// Clear Force's
		for(int i = 0; i < this.objects.size(); i++) {
			PhysicObject object = this.objects.get(i);
			object.force.set(0, 0);
			object.torque = 0;
		}
		
		// Simple Wall Check TODO : Delete this
		for(int i = 0; i < this.objects.size(); i++) {
			PhysicObject object = this.objects.get(i);
			this.checkWallCollisions(object, 0.2F);	
		}
		
		// Remove from World
		for(int i = 0; i < this.objects.size(); i++) {
			PhysicObject object = this.objects.get(i);
			
			if(object.position.y > SkyEngine.application().getHeight() + 100) {
				this.objects.remove(i);
				System.err.println("Remove GameObject from World.");
			}
		}
	}
	
	@Override
	public void render(float mouseX, float mouseY, float partialTicks) {
		super.render(mouseX, mouseY, partialTicks);

		this.renderer.drawString(Resources.fonts().main, "Gravity: " + PhysicsEngine.GRAVITY.toString(), 5, 5, 0xFFFFFFFF);
		this.renderer.drawString(Resources.fonts().main, "Objects: " + this.objects.size(), 5, 30, 0xFFFFFFFF);
		
		int index = 0;
		for(int i = 0; i < this.objects.size(); i++) {
			PhysicObject object = this.objects.get(i);
			
			if(object instanceof CircleObject) {
				this.renderer.drawString(Resources.fonts().main, "Mass: " + MathUtils.round(object.mass, 2) + " | " + "Orient: " + MathUtils.round(object.orient, 2) + " | " + "X: " + object.position.x + " | Y: " + object.position.y + " | MX: " + object.velocity.x + " | MY: " + object.velocity.y, 5, index * 30 + 80, 0x50FFFFFF);
				index++;
			}
		}
		
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
	
	private void integrateForces(PhysicObject object, double dt) {
		if(object.invMass == 0) return;
		
		double dts = dt * 0.5D;
		
		object.velocity.addsIntern(object.force, object.invMass * dts);
		object.velocity.addsIntern(PhysicsEngine.GRAVITY, dts);
		object.angularVelocity += object.torque * object.invInertia * dts;
	}
	
	private void integrateVelocity(PhysicObject object, double dt) {
		if(object.invMass == 0) return;
		
		object.lastPosition.set(object.position);
		object.position.addsIntern(object.velocity, dt);
		object.orient += object.angularVelocity * dt;
		object.setOrient(object.orient);
		
		this.integrateForces(object, dt);
	}
	
	private void checkWallCollisions(PhysicObject object, float bounce) {
		if(object instanceof CircleObject) {
			CircleObject circle = (CircleObject) object;
			
			float maxY = SkyEngine.application().getHeight() - circle.getRadius();
			float maxX = SkyEngine.application().getWidth() - circle.getRadius();
			
			if(object.getX() < circle.getRadius()) {
				object.position.set(circle.getRadius(), object.getY());
				object.velocity.set((object.velocity.x * -bounce), object.velocity.y);
			}
			
			if(object.getX() > maxX) {
				object.position.set(maxX, object.getY());
				object.velocity.set((object.velocity.x * -bounce), object.velocity.y);
			}
			
			if(object.getY() > maxY) {
				object.position.set(object.getX(), maxY);
				object.velocity.set(object.velocity.x, (object.velocity.y * -bounce));
			}
		}
	}
}

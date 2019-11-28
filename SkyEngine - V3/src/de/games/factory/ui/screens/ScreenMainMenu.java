package de.games.factory.ui.screens;

import java.util.ArrayList;
import java.util.List;

import de.skyengine.core.Input;
import de.skyengine.core.SkyEngine;
import de.skyengine.graphics.ImmediateRenderer;
import de.skyengine.graphics.internal.Color;
import de.skyengine.graphics.screen.ScreenAdapter;
import de.skyengine.resources.Resources;

public class ScreenMainMenu extends ScreenAdapter {

	private ImmediateRenderer renderer;
	private int width;
	private int height;
	
	private Title titleTop;
	private Title titleBottom;
	
	public ScreenMainMenu(ImmediateRenderer renderer) {
		this.renderer = renderer;
	}
	
	@Override
	public void init() {
		this.titleTop = new Title("SKY", new Color(0xFF3A393A));
		this.titleTop.resetAll();
		this.titleBottom = new Title("FACTORY", new Color(0xFF3A393A));
		this.titleBottom.resetAll();
	}
	
	@Override
	public void input(Input input) {
		if(input.isKeyPressed(Input.Keys.SPACE)) {
			SkyEngine.getInstance().getContainer().setScreen(null);
			System.out.println("Jump");
		}
	}
	
	@Override
	public void update() {
		this.titleTop.update();
		if(this.titleTop.getLastAlpha() > 0.1F) {
			this.titleBottom.update();	
		}
	}
	
	@Override
	public void render(float partialTicks) {
		this.renderer.texture(Resources.textures().backgroundMainMenu, 0, 0, this.width, this.height, 0xFFFFFFFF);
		
		this.titleTop.render(this.renderer, SkyEngine.getWidth() / 2, 120, partialTicks);
		this.titleBottom.render(this.renderer, SkyEngine.getWidth() / 2, 190, partialTicks);
	}
	
	@Override
	public void resize(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	private class Title {
		private String text;
		
		private List<TextObject> objects;
		private boolean updated;
		
		private float spaceWidth;
		
		public Title(String text, Color color) {
			this.text = text;
			this.objects = new ArrayList<TextObject>();
			
			for(String s : text.split("")) {
				if(s != null && s != " ") {
					this.objects.add(new TextObject(s, 0.0F, color));
				}
			}
			
			this.updated = false;
			this.spaceWidth = Resources.fonts().mainMenuTitle.getWidth(" ");
		}
		
		public void update() {
			if(!this.updated) {
				float addRate = 0.01F;
				
				for(TextObject object : this.objects) {
					int oIndex = this.objects.indexOf(object);
					
					if(oIndex > 0) {
						TextObject last = this.objects.get(oIndex - 1);
						if(last != null && last.getAlpha() > 0.1F) {
							object.setAlpha(object.getAlpha() + addRate);		
						}
					} else {
						object.setAlpha(object.getAlpha() + addRate);	
					}
				}
				
				if(this.objects.get(this.objects.size() - 1).getAlpha() >= 1.0F) {
					this.updated = true;
				}
			}
		}
		
		public void render(ImmediateRenderer renderer, float startX, float startY, float partialTicks) {
			float drawX = startX - (renderer.getWidth(Resources.fonts().mainMenuTitle, this.text) / 2);
			drawX -= ((this.text.length() - 1) * (this.spaceWidth + this.spaceWidth)) / 2.0F;
			
			for(TextObject object : this.objects) {
				renderer.drawString(Resources.fonts().mainMenuTitle, object.getString(), drawX, startY, object.getColor().toHex());
				drawX += renderer.getWidth(Resources.fonts().mainMenuTitle, object.getString()) + (this.spaceWidth + this.spaceWidth);
			}
		}
		
		public void resetAll() {
			this.updated = false;
			for(TextObject object : this.objects) {
				object.setAlpha(0.0F);
			}
		}
		
		public float getLastAlpha() {
			return this.objects.get(this.objects.size() - 1).getAlpha();
		}
	}
	
	private class TextObject {
		private String s;
		private float alpha;
		private Color color;
		
		public TextObject(String s, float alpha, Color color) {
			this.s = s;
			this.alpha = alpha;
			this.color = new Color(color);
		}
		
		public String getString() {
			return s;
		}
		
		public float getAlpha() {
			return alpha;
		}

		public void setAlpha(float alpha) {
			this.alpha = alpha;
			this.color.setAlpha(alpha);
		}
		
		public Color getColor() {
			return color;
		}
	}
}

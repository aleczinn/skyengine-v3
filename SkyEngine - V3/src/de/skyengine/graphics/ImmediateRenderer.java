package de.skyengine.graphics;

import org.lwjgl.opengl.GL11;

import de.games.physicsengine.objects.PolygonObject;
import de.skyengine.graphics.font.TextAlignment;
import de.skyengine.graphics.font.TrueTypeFont;
import de.skyengine.graphics.internal.Texture;
import de.skyengine.utils.MathUtils;
import de.skyengine.utils.math.Vector2d;

public class ImmediateRenderer {

	public void point(float x, float y, float pointSize, int color) {
		GlStateManager.disableTexture2D();

		GlStateManager.pointSize(pointSize);
		
		GlStateManager.glBegin(GL11.GL_POINTS);
		GlStateManager.color(color);
		GlStateManager.vertex2f(x, y);
		GlStateManager.glEnd();
		
		GlStateManager.enableTexture2D();
	}
	
	public void line(float x, float y, float x2, float y2, int color) {
		this.line(x, y, x2, y2, 1.0F, color, color);
	}
	
	public void line(float x, float y, float x2, float y2, float lineWidth, int color) {
		this.line(x, y, x2, y2, lineWidth, color, color);
	}
	
	public void line(float x, float y, float x2, float y2, float lineWidth, int color1, int color2) {
		GlStateManager.disableTexture2D();
		
		GlStateManager.lineWidth(lineWidth);
		GlStateManager.glBegin(GL11.GL_LINES);
		
		GlStateManager.color(color1);
		GlStateManager.vertex2f(x, y);

		GlStateManager.color(color2);
		GlStateManager.vertex2f(x2, y2);
		
		GlStateManager.glEnd();
		
		GlStateManager.enableTexture2D();
	}
	
	public void triangle(float x1, float y1, float x2, float y2, float x3, float y3, int color) {
		this.triangle(ShapeType.FILLED, x1, y1, x2, y2, x3, y3, color, color, color);
	}
	
	public void triangle(ShapeType type, float x1, float y1, float x2, float y2, float x3, float y3, int color) {
		this.triangle(type, x1, y1, x2, y2, x3, y3, color, color, color);
	}
	
	public void triangle(ShapeType type, float x1, float y1, float x2, float y2, float x3, float y3, int color1, int color2, int color3) {
		GlStateManager.disableTexture2D();
		
		GlStateManager.glBegin(type.getGlType());
		
		if(type.equals(ShapeType.LINE)) {
			GlStateManager.color(color1);
			GlStateManager.vertex2f(x1, y1);
			
			GlStateManager.color(color2);
			GlStateManager.vertex2f(x2, y2);

			GlStateManager.color(color2);
			GlStateManager.vertex2f(x2, y2);
			
			GlStateManager.color(color3);
			GlStateManager.vertex2f(x3, y3);

			GlStateManager.color(color3);
			GlStateManager.vertex2f(x3, y3);
			
			GlStateManager.color(color1);
			GlStateManager.vertex2f(x1, y1);
		} else if(type.equals(ShapeType.FILLED)) {
			GlStateManager.color(color1);
			GlStateManager.vertex2f(x1, y1);
			
			GlStateManager.color(color3);
			GlStateManager.vertex2f(x3, y3);
			
			GlStateManager.color(color2);
			GlStateManager.vertex2f(x2, y2);
		}
		
		GlStateManager.glEnd();
		GlStateManager.enableTexture2D();
	}
	
	public void textureNoBind(float x, float y, float width, float height, int color) {
		GlStateManager.enableTexture2D();
		GlStateManager.color(color);
		
		GlStateManager.glBegin(GL11.GL_TRIANGLES);
		
		GlStateManager.texCoord2f(0.0F, 0.0F);
		GlStateManager.vertex2f(x, y);
		
		GlStateManager.texCoord2f(1.0F, 1.0F);
		GlStateManager.vertex2f(x + width, y + height);
		
		GlStateManager.texCoord2f(1.0F, 0.0F);
		GlStateManager.vertex2f(x + width, y);
		
		GlStateManager.texCoord2f(0.0F, 1.0F);
		GlStateManager.vertex2f(x, y + height);
		
		GlStateManager.texCoord2f(1.0F, 1.0F);
		GlStateManager.vertex2f(x + width,  y + height);
		
		GlStateManager.texCoord2f(0.0F, 0.0F);
		GlStateManager.vertex2f(x, y);
		
		GlStateManager.glEnd();
		GlStateManager.disableTexture2D();
	}
	
	public void texture(Texture texture, float x, float y, float width, float height, int color) {
		texture.bind();

		this.textureNoBind(x, y, width, height, color);
	}
	
	public void texture(Texture texture, float x, float y, float width, float height, int color, float alpha) {
		texture.bind();

		GlStateManager.enableTexture2D();
		GlStateManager.color(color, alpha);
		
		GlStateManager.glBegin(GL11.GL_TRIANGLES);
		
		GlStateManager.texCoord2f(0.0F, 0.0F);
		GlStateManager.vertex2f(x, y);
		
		GlStateManager.texCoord2f(1.0F, 1.0F);
		GlStateManager.vertex2f(x + width, y + height);
		
		GlStateManager.texCoord2f(1.0F, 0.0F);
		GlStateManager.vertex2f(x + width, y);
		
		GlStateManager.texCoord2f(0.0F, 1.0F);
		GlStateManager.vertex2f(x, y + height);
		
		GlStateManager.texCoord2f(1.0F, 1.0F);
		GlStateManager.vertex2f(x + width,  y + height);
		
		GlStateManager.texCoord2f(0.0F, 0.0F);
		GlStateManager.vertex2f(x, y);
		
		GlStateManager.glEnd();
		
		GlStateManager.disableTexture2D();
	}
	
	/**
	 * Draws a texture region with the currently bound texture on specified
     * coordinates.
	 * 
     * @param texture   	Used for getting width and height of the texture
     * @param x        		X position of the texture
     * @param y         		Y position of the texture
     * @param regX      		X position of the texture region
     * @param regY      		Y position of the texture region
     * @param regWidth  	Width of the texture region
     * @param regHeight 	Height of the texture region
     * @param color       	The color to use
     */
	public void textureRegion(Texture texture, float x, float y, float regX, float regY, float regWidth, float regHeight, int color) {
	     /* Vertex positions */
        float x1 = x;
        float y1 = y;
        float x2 = x + regWidth;
        float y2 = y + regHeight;

        /* Texture coordinates */
        float s1 = regX / texture.getWidth();
        float t1 = regY / texture.getHeight();
        float s2 = (regX + regWidth) / texture.getWidth();
        float t2 = (regY + regHeight) / texture.getHeight();
        
        texture.bind();
        this.textureRegion(x1, y1, x2, y2, s1, t1, s2, t2, color);
	}
	
	  /**
     * Draws a texture region with the currently bound texture on specified
     * coordinates.
     *
     * @param x1 Bottom left x position
     * @param y1 Bottom left y position
     * @param x2 Top right x position
     * @param y2 Top right y position
     * @param s1 Bottom left s coordinate
     * @param t1 Bottom left t coordinate
     * @param s2 Top right s coordinate
     * @param t2 Top right t coordinate
     * @param c  The color to use
     */
	private void textureRegion(float x1, float y1, float x2, float y2, float s1, float t1, float s2, float t2, int color) {
		GlStateManager.enableTexture2D();
		
	    GlStateManager.glBegin(GL11.GL_TRIANGLES);
        GlStateManager.color(color);
		
//        GlStateManager.texCoord2f(s1, t2);
//		GlStateManager.vertex2f(x1, y1);
//		GlStateManager.texCoord2f(s2, t2);
//		GlStateManager.vertex2f(x2, y1);
//		GlStateManager.texCoord2f(s2, t1);
//		GlStateManager.vertex2f(x2, y2);
//		
//		GlStateManager.texCoord2f(s2, t1);
//		GlStateManager.vertex2f(x2, y2);
//		GlStateManager.texCoord2f(s1, t1);
//		GlStateManager.vertex2f(x1, y2);
//		GlStateManager.texCoord2f(s1, t2);
//		GlStateManager.vertex2f(x1, y1);
        
		GlStateManager.texCoord2f(s1, t1);
		GlStateManager.vertex2f(x1, y1);
		
		GlStateManager.texCoord2f(s2, t2);
		GlStateManager.vertex2f(x2, y2);
		
		GlStateManager.texCoord2f(s2, t1);
		GlStateManager.vertex2f(x2, y1);
		
		GlStateManager.texCoord2f(s1, t2);
		GlStateManager.vertex2f(x1, y2);
		
		GlStateManager.texCoord2f(s2, t2);
		GlStateManager.vertex2f(x2,  y2);
		
		GlStateManager.texCoord2f(s1, t1);
		GlStateManager.vertex2f(x1, y1);
		
		GlStateManager.glEnd();
        
        GlStateManager.disableTexture2D();
	}
	
	public void rect(float x, float y, float x2, float y2, int color) {
		this.rect(ShapeType.FILLED, x, y, x2, y2, color, color, color, color);
	}
	
	public void rect(ShapeType type, float x, float y, float x2, float y2, int color) {
		this.rect(type, x, y, x2, y2, color, color, color, color);
	}
	
	public void rect(ShapeType type, float x, float y, float x2, float y2, int color1, int color2, int color3, int color4) {
		GlStateManager.disableTexture2D();
		
		if(type.equals(ShapeType.FILLED)) {
			GlStateManager.glBegin(type.getGlType());
			
			GlStateManager.color(color1);
			GlStateManager.vertex2f(x, y);
			
			GlStateManager.color(color2);
			GlStateManager.vertex2f(x2, y2);
			
			GlStateManager.color(color3);
			GlStateManager.vertex2f(x2, y);
			
			GlStateManager.color(color3);
			GlStateManager.vertex2f(x, y2);
			
			GlStateManager.color(color4);
			GlStateManager.vertex2f(x2, y2);
			
			GlStateManager.color(color1);
			GlStateManager.vertex2f(x, y);
			
			GlStateManager.glEnd();
		} else if(type.equals(ShapeType.LINE)) {
			float l = GlStateManager.lineWidth / 2.0F;
			
			GlStateManager.glBegin(type.getGlType());
			
			GlStateManager.color(color1);
			GL11.glVertex2f(x - l, y);
			
			GlStateManager.color(color2);
			GL11.glVertex2f(x2 + l, y);
			
			GlStateManager.color(color2);
			GL11.glVertex2f(x2, y);
			
			GlStateManager.color(color3);
			GL11.glVertex2f(x2, y2);
			
			GlStateManager.color(color3);
			GL11.glVertex2f(x2 + l, y2);
			
			GlStateManager.color(color4);
			GL11.glVertex2f(x - l, y2);
			
			GlStateManager.color(color4);
			GL11.glVertex2f(x, y2);
			
			GlStateManager.color(color1);
			GL11.glVertex2f(x, y);
			
			//////////////////////////
			
//			GlStateManager.color(color1);
//			GL11.glVertex2f(x, y);
//			
//			GlStateManager.color(color2);
//			GL11.glVertex2f(x2, y);
//			
//			GlStateManager.color(color2);
//			GL11.glVertex2f(x2, y);
//			
//			GlStateManager.color(color3);
//			GL11.glVertex2f(x2, y2);
//			
//			GlStateManager.color(color3);
//			GL11.glVertex2f(x2, y2);
//			
//			GlStateManager.color(color4);
//			GL11.glVertex2f(x, y2);
//			
//			GlStateManager.color(color4);
//			GL11.glVertex2f(x, y2);
//			
//			GlStateManager.color(color1);
//			GL11.glVertex2f(x, y);
			
			GlStateManager.glEnd();
		}
		
		GlStateManager.enableTexture2D();
	}
	
	public void circle(float x, float y, float radius, int color) {
		this.circle(ShapeType.FILLED, x, y, radius, Math.max(1, (int) (12.0F * (float) Math.cbrt(radius))), color);
	}
	
	public void circle(ShapeType type, float x, float y, float radius, int color) {
		this.circle(type, x, y, radius, Math.max(1, (int) (12.0F * (float) Math.cbrt(radius))), color);
	}
	
	public void circle(ShapeType type, float x, float y, float radius, int segments, int color) {
		if(segments < 1) throw new IllegalArgumentException("Segments must be > 0");
		
		GlStateManager.disableTexture2D();
		
		float angle = 2 * MathUtils.PI / segments;
		float cos = MathUtils.cos(angle);
		float sin = MathUtils.sin(angle);
		float cx = radius, cy = 0;
		
		GlStateManager.color(color);
		GlStateManager.glBegin(type.getGlType());
		
		if (type.equals(ShapeType.LINE)) {
			for (int i = 0; i < segments; i++) {
				GlStateManager.vertex2f(x + cx, y + cy);
				float temp = cx;
				cx = cos * cx - sin * cy;
				cy = sin * temp + cos * cy;
				GlStateManager.vertex2f(x + cx, y + cy);
			}
			// Ensure the last segment is identical to the first.
			GlStateManager.vertex2f(x + cx, y + cy);
		} else if(type.equals(ShapeType.FILLED)) {
			segments--;
			for (int i = 0; i < segments; i++) {
				GlStateManager.vertex2f(x + cx, y + cy);
				GlStateManager.vertex2f(x, y);
				
				float temp = cx;
				cx = cos * cx - sin * cy;
				cy = sin * temp + cos * cy;
				GlStateManager.vertex2f(x + cx, y + cy);
			}
			// Ensure the last segment is identical to the first.
			GlStateManager.vertex2f(x + cx, y + cy);
			GlStateManager.vertex2f(x, y);
		}

		cx = radius;
		cy = 0;
		GlStateManager.vertex2f(x + cx, y + cy);
		GlStateManager.glEnd();
		
		GlStateManager.enableTexture2D();
	}

	/*
	 * Draw's a circle but you can change from angle to angle.
	 */
	public void arc(float x, float y, float radius, float start, float degrees, int color) {
		this.arc(ShapeType.FILLED, x, y, radius, start, degrees, Math.max(1, (int) (9 * (float) Math.cbrt(radius) * (degrees / 360.0F))), color);
	}
	
	public void arc(ShapeType type, float x, float y, float radius, float start, float degrees, int color) {
		this.arc(type, x, y, radius, start, degrees, Math.max(1, (int) (9 * (float) Math.cbrt(radius) * (degrees / 360.0F))), color);
	}
	
	public void arc(ShapeType type, float x, float y, float radius, float start, float degrees, int segments, int color) {
		if(segments < 1) throw new IllegalArgumentException("segments must be > 0.");
		
		float theta = (2 * MathUtils.PI * (degrees / 360.0f)) / segments;
		float cos = MathUtils.cos(theta);
		float sin = MathUtils.sin(theta);
		float cx = radius * MathUtils.cos(start * MathUtils.degreesToRadians);
		float cy = radius * MathUtils.sin(start * MathUtils.degreesToRadians);
		
		GlStateManager.disableTexture2D();
		
		GlStateManager.color(color);
		GlStateManager.glBegin(type.getGlType());
		
		if(type.equals(ShapeType.FILLED)) {
			for (int i = 0; i < segments; i++) {
				GlStateManager.vertex2f(x + cx, y + cy);
				GlStateManager.vertex2f(x, y);
				
				float temp = cx;
				cx = cos * cx - sin * cy;
				cy = sin * temp + cos * cy;
				GlStateManager.vertex2f(x + cx, y + cy);
			}
			GlStateManager.vertex2f(x, y);
			GlStateManager.vertex2f(x + cx, y + cy);
		} else if(type.equals(ShapeType.LINE)) {
			GlStateManager.vertex2f(x, y);
			GlStateManager.vertex2f(x + cx, y + cy);
			
			for (int i = 0; i < segments; i++) {
				GlStateManager.vertex2f(x + cx, y + cy);
				float temp = cx;
				cx = cos * cx - sin * cy;
				cy = sin * temp + cos * cy;
				GlStateManager.vertex2f(x + cx, y + cy);
			}
			GlStateManager.vertex2f(x + cx, y + cy);
		}
		
		cx = 0;
		cy = 0;
		GlStateManager.vertex2f(x + cx, y + cy);
		GlStateManager.glEnd();
		
		GlStateManager.enableTexture2D();
	}
	
	public void ellipse(float x, float y, float width, float height, int color) {
		this.ellipse(ShapeType.FILLED, x, y, width, height, Math.max(1, (int) (12 * (float) Math.cbrt(Math.max(width * 0.5F, height * 0.5F)))), color);
	}
	
	public void ellipse(ShapeType type, float x, float y, float width, float height, int color) {
		this.ellipse(type, x, y, width, height, Math.max(1, (int) (12 * (float) Math.cbrt(Math.max(width * 0.5F, height * 0.5F)))), color);
	}
	
	public void ellipse(ShapeType type, float x, float y, float width, float height, int segments, int color) {
		if (segments <= 0) throw new IllegalArgumentException("segments must be > 0.");
		
		GlStateManager.disableTexture2D();
		
		float angle = MathUtils.PI2 / segments;

		GlStateManager.color(color);
		GlStateManager.glBegin(type.getGlType());
		
		float cx = x + width / 2, cy = y + height / 2;
		if(type.equals(ShapeType.LINE)) {
			for (int i = 0; i < segments; i++) {
				GlStateManager.vertex2f(cx + (width * 0.5F * MathUtils.cos(i * angle)), cy + (height * 0.5F * MathUtils.sin(i * angle)));
				GlStateManager.vertex2f(cx + (width * 0.5F * MathUtils.cos((i + 1) * angle)), cy + (height * 0.5F * MathUtils.sin((i + 1) * angle)));				
			}
		} else if(type.equals(ShapeType.FILLED)) {
			for (int i = 0; i < segments; i++) {
				GlStateManager.vertex2f(cx + (width * 0.5F * MathUtils.cos((i + 1) * angle)), cy + (height * 0.5F * MathUtils.sin((i + 1) * angle)));
				GlStateManager.vertex2f(cx + (width * 0.5F * MathUtils.cos(i * angle)), cy + (height * 0.5F * MathUtils.sin(i * angle)));
				GlStateManager.vertex2f(cx, cy);
			}
		}
		
		GlStateManager.glEnd();
		
		GlStateManager.enableTexture2D();
	}
	
	public void polygon(PolygonObject polygonObject, int color) {
		GlStateManager.disableTexture2D();
		
		GlStateManager.color(color);
		GlStateManager.glBegin(GL11.GL_LINES);
		
		Vector2d start = new Vector2d(polygonObject.vertices[0]);
		start.addIntern(polygonObject.position);
		
		for(int i = 0; i < polygonObject.vertexCount; i++) {
			Vector2d v = new Vector2d(polygonObject.vertices[i]);
			polygonObject.u.multiplyIntern(v);
			v.addIntern(polygonObject.position);
			
			if(i == polygonObject.vertexCount - 1) {
				GlStateManager.vertex2f(v.xAsFloat(), v.yAsFloat());	
				GlStateManager.vertex2f(start.xAsFloat(), start.yAsFloat());
			} else {
				Vector2d next = new Vector2d(polygonObject.vertices[i + 1]);
				next.addIntern(polygonObject.position);
				
				GlStateManager.vertex2f(v.xAsFloat(), v.yAsFloat());	
				GlStateManager.vertex2f(next.xAsFloat(), next.yAsFloat());	
			}
		}
		
		GlStateManager.glEnd();
		
		GlStateManager.enableTexture2D();
	}
	
	public float drawString(TrueTypeFont font, String text, float x, float y, int color) {
		return this.drawString(font, text, x, y, TextAlignment.LEFT, TextAlignment.TOP, color);
	}
	
	public float drawCenteredString(TrueTypeFont font, String text, float x, float y, int color) {
		return this.drawString(font, text, x, y, TextAlignment.CENTER, TextAlignment.TOP, color);
	}
	
	public float drawString(TrueTypeFont font, String text, float x, float y, TextAlignment hAlignment, TextAlignment vAlignment, int color) {
		if(text == null || text.isEmpty()) return -1;
		
		float offsetX = 0.0F;
		float offsetY = 0.0F;
		
		switch (hAlignment) {
		case CENTER:
			offsetX = -font.getWidth(text) / 2;
			break;
		case RIGHT:
			offsetX = font.getWidth(text);
			break;
		default:
			break;
		}
		
		switch (vAlignment) {
		case CENTER:
			offsetY = - font.getHeight(text) / 2;
			break;
		case BOTTOM:
			offsetY = - font.getHeight(text);
			break;
		default:
			break;
		}
		
		return font.drawString(this, text, x + offsetX, y + offsetY, color);
	}
	
	public float getWidth(TrueTypeFont font, String text) {
		return font.getWidth(text);
	}
	
	public float getHeight(TrueTypeFont font, String text) {
		return font.getHeight(text);
	}
	
	public void lineWidth(float lineWidth) {
		GlStateManager.lineWidth(lineWidth);
	}
	
	public void startRotate(float angle, float rotatePointX, float rotatePointY) {
		GlStateManager.pushMatrix();
		
		GlStateManager.translate(rotatePointX, rotatePointY, 0);
		GlStateManager.rotate(angle, 0, 0, 1);
		GlStateManager.translate(-rotatePointX, -rotatePointY, 0);
	}
	
	public void stopRotate() {
		GlStateManager.popMatrix();
	}
	
	public enum ShapeType {
		LINE(GL11.GL_LINES), FILLED(GL11.GL_TRIANGLES);

		private final int glType;

		ShapeType(int glType) {
			this.glType = glType;
		}

		public int getGlType () {
			return glType;
		}
	}
}

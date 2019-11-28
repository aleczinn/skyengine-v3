package de.skyengine.graphics;

import org.lwjgl.opengl.GL11;

import de.skyengine.core.internal.IDisposable;
import de.skyengine.utils.MathUtils;
import de.skyengine.utils.math.Vector2f;

public class ShapeRenderer implements IDisposable {
	
	private VBO vbo;
	private RenderType renderType;
	
	private float[] vertices;
	private int vertexIndex;
	private int vertexCount;
	
	private float[] colors;
	private int colorIndex;
	private int colorCount;
	
	private int maxBufferSize;
	private float defaultRectLineWidth = 0.75F;
	
	public ShapeRenderer() {
		this(true, 5000);
	}
	
	/**
	 * @param isStatic | true = If you add a new draw call it is possible that errors may occur. [Draw a lot = static] | false
	 */
	public ShapeRenderer(boolean isStatic) {
		this(isStatic, 5000);
	}
	
	/**
	 * @param isStatic				| true = If you add a new draw call it is possible that errors may occur. [Draw a lot = static] | false
	 * @param maxBufferSize	| How many vertices can the vbo store? [example: 5.000 = 5.000 * 6 = 30.000] -> 30K from {x, y, r, g, b, a}
	 */
	public ShapeRenderer(boolean isStatic, int maxBufferSize) {
		this.maxBufferSize = maxBufferSize * 6; // X, Y, R, G, B, A
		
		this.vbo = new VBO(isStatic, this.maxBufferSize);
		
		this.vertices = new float[this.maxBufferSize];
		this.vertexIndex = 0;
		this.vertexCount = 0;
		
		this.colors = new float[this.maxBufferSize];
		this.colorIndex = 0;
		this.colorCount = 0;
	}
	
	public void begin() {
		this.begin(RenderType.FILLED);
	}
	
	public void begin(RenderType type) {
		if(this.renderType != null) throw new IllegalStateException("Call end() before beginning a new shape batch.");
		
		this.renderType = type;
		
		GlStateManager.disableTexture2D();
	}
	
	/** Finishes the batch of shapes and ensures they get rendered. */
	public void end () {
		if(this.renderType == null) throw new IllegalStateException("Call begin() before end a shape batch.");
		
		this.flush();
		this.renderType = null;
		
		GlStateManager.enableTexture2D();
	}
	
	public void flush() {
		RenderType type = this.renderType;
		if(type == null) return;

		this.vbo.setVertices(this.vertices, this.colors, this.vertexCount, this.colorCount);
		this.vbo.render(this.renderType.getGlType());
		
		this.renderType = null;
		
		this.vertexIndex = 0;
		this.vertexCount = 0;
		
		this.colorIndex = 0;
		this.colorCount = 0;
	}
	
	public void point(float x, float y, float pointSize, int color) {
		if (this.renderType.equals(RenderType.LINE)) {
			float size = this.defaultRectLineWidth * 0.5F;
			this.line(x - size, y - size, x + size, y + size, color);
			return;
		} else if (this.renderType.equals(RenderType.FILLED)) {
			return;
		}
		
		GlStateManager.pointSize(pointSize);
		
		this.check(RenderType.POINT, null, 1);
		this.color(color);
		this.vertex(x, y);
	}
	
	public void line(float x, float y, float x2, float y2, int color) {
		this.line(x, y, x2, y2, 1.0F, color, color);
	}
	
	public void line(float x, float y, float x2, float y2, float lineWidth, int color) {
		this.line(x, y, x2, y2, lineWidth, color, color);
	}
	
	public void line(float x, float y, float x2, float y2, float lineWidth, int color1, int color2) {
		if (this.renderType.equals(RenderType.FILLED)) {
			this.rectLine(x, y, x2, y2, this.defaultRectLineWidth, color1);
			return;
		}
		
		this.check(RenderType.LINE, null, 2);

		GlStateManager.lineWidth(lineWidth);
		
		this.color(color1);
		this.vertex(x, y);
		
		this.color(color2);
		this.vertex(x2, y2);
	}
	
	public void triangle(float x1, float y1, float x2, float y2, float x3, float y3, int color) {
		this.triangle(x1, y1, x2, y2, x3, y3, color, color, color);
	}
	
	public void triangle(float x1, float y1, float x2, float y2, float x3, float y3, int color1, int color2, int color3) {
		this.check(RenderType.LINE, RenderType.FILLED, 6);
		
		if(this.renderType.equals(RenderType.LINE)) {
			this.color(color1);
			this.vertex(x1, y1);
			
			this.color(color2);
			this.vertex(x2, y2);

			this.color(color2);
			this.vertex(x2, y2);
			
			this.color(color3);
			this.vertex(x3, y3);

			this.color(color3);
			this.vertex(x3, y3);
			
			this.color(color1);
			this.vertex(x1, y1);
		} else if(this.renderType.equals(RenderType.FILLED)) {
			this.color(color1);
			this.vertex(x1, y1);
			
			this.color(color3);
			this.vertex(x3, y3);
			
			this.color(color2);
			this.vertex(x2, y2);
		}
	}
	
	public void rect(float x, float y, float x2, float y2, int color) {
		this.rect(x, y, x2, y2, color, color, color, color);
	}
	
	public void rect(float x, float y, float x2, float y2, int color1, int color2, int color3, int color4) {
		this.check(RenderType.LINE, RenderType.FILLED, 8);
		
		if(this.renderType.equals(RenderType.FILLED)) {
			this.color(color1);
			this.vertex(x, y);
			
			this.color(color2);
			this.vertex(x2, y2);
			
			this.color(color3);
			this.vertex(x2, y);
			
			this.color(color3);
			this.vertex(x, y2);
			
			this.color(color4);
			this.vertex(x2, y2);
			
			this.color(color1);
			this.vertex(x, y);
		} else if(this.renderType.equals(RenderType.LINE)) {
			float l = GlStateManager.lineWidth / 2.0F;
			
			this.color(color1);
			this.vertex(x - l, y);
			
			this.color(color2);
			this.vertex(x2 + l, y);
			
			this.color(color2);
			this.vertex(x2, y);
			
			this.color(color3);
			this.vertex(x2, y2);
			
			this.color(color3);
			this.vertex(x2 + l, y2);
			
			this.color(color4);
			this.vertex(x - l, y2);
			
			this.color(color4);
			this.vertex(x, y2);
			
			this.color(color1);
			this.vertex(x, y);
		}
	}
	
	public void rectLine (float x1, float y1, float x2, float y2, float width, int color) {
		this.check(RenderType.LINE, RenderType.FILLED, 8);
		
		Vector2f t = new Vector2f(y2 - y1, x1 - x2);
		t.normalize();
		
		width *= 0.5F;
		
		float tx = t.x * width;
		float ty = t.y * width;
		
		if (this.renderType.equals(RenderType.LINE)) {
			this.color(color);
			this.vertex(x1 + tx, y1 + ty);
			this.color(color);
			this.vertex(x1 - tx, y1 - ty);

			this.color(color);
			this.vertex(x2 + tx, y2 + ty);
			this.color(color);
			this.vertex(x2 - tx, y2 - ty);

			this.color(color);
			this.vertex(x2 + tx, y2 + ty);
			this.color(color);
			this.vertex(x1 + tx, y1 + ty);

			this.color(color);
			this.vertex(x2 - tx, y2 - ty);
			this.color(color);
			this.vertex(x1 - tx, y1 - ty);
		} else {
			this.color(color);
			this.vertex(x1 + tx, y1 + ty);
			this.color(color);
			this.vertex(x1 - tx, y1 - ty);
			this.color(color);
			this.vertex(x2 + tx, y2 + ty);

			this.color(color);
			this.vertex(x2 - tx, y2 - ty);
			this.color(color);
			this.vertex(x2 + tx, y2 + ty);
			this.color(color);
			this.vertex(x1 - tx, y1 - ty);
		}
	}
	
	public void circle(float x, float y, float radius, int color) {
		this.circle(x, y, radius, Math.max(1, (int) (12.0F * (float) Math.cbrt(radius))), color);
	}
	
	public void circle(float x, float y, float radius, int segments, int color) {
		if(segments < 1) throw new IllegalArgumentException("Segments must be > 0");
		
		float angle = 2 * MathUtils.PI / segments;
		float cos = MathUtils.cos(angle);
		float sin = MathUtils.sin(angle);
		float cx = radius, cy = 0;
		
		if(this.renderType.equals(RenderType.LINE)) {
			this.check(RenderType.LINE, RenderType.FILLED, segments * 2 + 2);
			
			for (int i = 0; i < segments; i++) {
				this.color(color);
				this.vertex(x + cx, y + cy);
				
				float temp = cx;
				cx = cos * cx - sin * cy;
				cy = sin * temp + cos * cy;
				
				this.color(color);
				this.vertex(x + cx, y + cy);
			}
			
			this.color(color);
			this.vertex(x + cx, y + cy);
		} else if(this.renderType.equals(RenderType.FILLED)) {
			segments--;
			
			for (int i = 0; i < segments; i++) {
				this.color(color);
				this.vertex(x + cx, y + cy);
				
				this.color(color);
				this.vertex(x, y);
				
				float temp = cx;
				cx = cos * cx - sin * cy;
				cy = sin * temp + cos * cy;
				
				this.color(color);
				this.vertex(x + cx, y + cy);
			}
			
			// Ensure the last segment is identical to the first.
			this.color(color);
			this.vertex(x + cx, y + cy);
			
			this.color(color);
			this.vertex(x, y);
		}
		
		cx = radius;
		cy = 0;
		this.color(color);
		this.vertex(x + cx, y + cy);
	}
	
	/*
	 * Draw's a circle but you can change from angle to angle.
	 */
	public void arc(float x, float y, float radius, float start, float degrees, int color) {
		this.arc(x, y, radius, start, degrees, Math.max(1, (int) (9 * (float) Math.cbrt(radius) * (degrees / 360.0F))), color);
	}
	
	public void arc(float x, float y, float radius, float start, float degrees, int segments, int color) {
		if(segments < 1) throw new IllegalArgumentException("segments must be > 0.");
		
		float theta = (2 * MathUtils.PI * (degrees / 360.0f)) / segments;
		float cos = MathUtils.cos(theta);
		float sin = MathUtils.sin(theta);
		float cx = radius * MathUtils.cos(start * MathUtils.degreesToRadians);
		float cy = radius * MathUtils.sin(start * MathUtils.degreesToRadians);
		
		if(this.renderType.equals(RenderType.FILLED)) {
			this.check(RenderType.LINE, RenderType.FILLED, segments * 3 + 3);
			
			for (int i = 0; i < segments; i++) {
				this.color(color);
				this.vertex(x + cx, y + cy);
				
				this.color(color);
				this.vertex(x, y);
				
				float temp = cx;
				cx = cos * cx - sin * cy;
				cy = sin * temp + cos * cy;
				
				this.color(color);
				this.vertex(x + cx, y + cy);
			}
			
			this.color(color);
			this.vertex(x, y);
			
			this.color(color);
			this.vertex(x + cx, y + cy);
		} else if(this.renderType.equals(RenderType.LINE)) {
			this.check(RenderType.LINE, RenderType.FILLED, segments * 2 + 2);
			
			this.color(color);
			this.vertex(x, y);
			
			this.color(color);
			this.vertex(x + cx, y + cy);
			
			for (int i = 0; i < segments; i++) {
				this.color(color);
				this.vertex(x + cx, y + cy);
				
				float temp = cx;
				cx = cos * cx - sin * cy;
				cy = sin * temp + cos * cy;
				
				this.color(color);
				this.vertex(x + cx, y + cy);
			}
			
			this.color(color);
			this.vertex(x + cx, y + cy);
		}
		
		cx = 0;
		cy = 0;
		
		this.color(color);
		this.vertex(x + cx, y + cy);
	}
	
	public void ellipse(float x, float y, float width, float height, int color) {
		this.ellipse(x, y, width, height, Math.max(1, (int) (12 * (float) Math.cbrt(Math.max(width * 0.5F, height * 0.5F)))), color);
	}
	
	public void ellipse(float x, float y, float width, float height, int segments, int color) {
		if(segments <= 0) throw new IllegalArgumentException("segments must be > 0.");
		
		this.check(RenderType.LINE, RenderType.FILLED, segments * 3);
		
		float angle = MathUtils.PI2 / segments;
		float cx = x + width / 2, cy = y + height / 2;
		
		if((this.renderType.equals(RenderType.LINE))) {
			for (int i = 0; i < segments; i++) {
				this.color(color);
				this.vertex(cx + (width * 0.5F * MathUtils.cos(i * angle)), cy + (height * 0.5F * MathUtils.sin(i * angle)));
				
				this.color(color);
				this.vertex(cx + (width * 0.5F * MathUtils.cos((i + 1) * angle)), cy + (height * 0.5F * MathUtils.sin((i + 1) * angle)));				
			}
		} else if(this.renderType.equals(RenderType.FILLED)) {
			for (int i = 0; i < segments; i++) {
				this.color(color);
				this.vertex(cx + (width * 0.5F * MathUtils.cos((i + 1) * angle)), cy + (height * 0.5F * MathUtils.sin((i + 1) * angle)));
				
				this.color(color);
				this.vertex(cx + (width * 0.5F * MathUtils.cos(i * angle)), cy + (height * 0.5F * MathUtils.sin(i * angle)));
				
				this.color(color);
				this.vertex(cx, cy);
			}
		}
	}
	
	private void vertex(float x, float y) {
		int index = this.vertexIndex;
		
		this.vertices[index] = x;
		this.vertices[index + 1] = y;
		
		this.vertexIndex += 2;
		this.vertexCount++;
	}
	
	private void color(float red, float green, float blue, float alpha) {
		int index = this.colorIndex;
		
		this.colors[index] = red;
		this.colors[index + 1] = green;
		this.colors[index + 2] = blue;
		this.colors[index + 3] = alpha;
		
		this.colorIndex += 4;
		this.colorCount++;
	}
	
	private void color(int color) {
		float alpha = ((color & 0xff000000) >>> 24) / 255F;
		float red = ((color & 0x00ff0000) >>> 16) / 255F;
		float green = ((color & 0x0000ff00) >>> 8) / 255F;
		float blue = ((color & 0x000000ff)) / 255F;
		
		this.color(red, green, blue, alpha);
	}
	
	private void check(RenderType preferred, RenderType other, int newVertices) {
		if(this.renderType == null) throw new IllegalArgumentException("Call begin() before you use a draw function!");
		
		if(this.renderType != preferred && this.renderType != other) {
			// Rendertype is not valid!
			if(other == null) {
				throw new IllegalStateException("Must call begin(ShapeType." + preferred + ").");
			}
			
			this.end();
			this.begin(preferred);
		} else if(this.vertexIndex + newVertices > this.maxBufferSize) {
			// Not enough space [Buffer].
			RenderType type = this.renderType;
			this.end();
			this.begin(type);
		} else if(this.colorIndex + newVertices > this.maxBufferSize) {
			// Not enough space [Buffer].
			RenderType type = this.renderType;
			this.end();
			this.begin(type);
		}
	}
	
	public boolean isDrawing() {
		return this.renderType != null;
	}
	
	@Override
	public void dispose() {
		this.vbo.dispose();
	}
	
	public enum RenderType {
		POINT(GL11.GL_POINTS), LINE(GL11.GL_LINES), FILLED(GL11.GL_TRIANGLES);
		
		private int glType;
		
		private RenderType(int glType) {
			this.glType = glType;
		}
		
		public int getGlType() {
			return glType;
		}
	}
}

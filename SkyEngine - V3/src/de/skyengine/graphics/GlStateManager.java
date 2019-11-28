package de.skyengine.graphics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

public class GlStateManager {

	private static final GlStateManager.BlendState blendState;
	private static final GlStateManager.DepthState depthState;
	private static final GlStateManager.TextureState textureState;
	private static final GlStateManager.Color colorState;

	public static float lineWidth = 1.0F;
	
	static {
		blendState = new GlStateManager.BlendState();
		depthState = new GlStateManager.DepthState();
		textureState = new GlStateManager.TextureState();
		
		colorState = new GlStateManager.Color();
	}

	public static void pushMatrix() {
		GL11.glPushMatrix();
	}
	
	public static void popMatrix() {
		GL11.glPopMatrix();
	}
	
	public static void glBegin(int glMode) {
		GL11.glBegin(glMode);
	}
	
	public static void glEnd() {
		GL11.glEnd();
	}
	
	public static void vertex2f(float x, float y) {
		GL11.glVertex2f(x, y);
	}
	
	public static void vertex3f(float x, float y, float z) {
		GL11.glVertex3f(x, y, z);
	}
	
	public static void texCoord2f(float u, float v) {
		GL11.glTexCoord2f(u, v);
	}
	
	public static void rotate(float angle, float x, float y, float z) {
		GL11.glRotatef(angle, x, y, z);
	}

	public static void scale(float x, float y, float z) {
		GL11.glScalef(x, y, z);
	}

	public static void scale(double x, double y, double z) {
		GL11.glScaled(x, y, z);
	}

	public static void translate(float x, float y, float z) {
		GL11.glTranslatef(x, y, z);
	}

	public static void translate(double x, double y, double z) {
		GL11.glTranslated(x, y, z);
	}
	
	public static void lineWidth(float lineWidth) {
		GlStateManager.lineWidth = lineWidth;
		GL11.glLineWidth(lineWidth);
	}
	
	public static void pointSize(float pointSize) {
		GL11.glPointSize(pointSize);
	}
	
	public static void color(int color) {
		float alpha = (float) (color >> 24 & 255) / 255.0F;
		float red = (float) (color >> 16 & 255) / 255.0F;
		float green = (float) (color >> 8 & 255) / 255.0F;
		float blue = (float) (color & 255) / 255.0F;

		GlStateManager.color(red, green, blue, alpha);
	}
	
	public static void color(int color, float alpha) {
		float red = (float) (color >> 16 & 255) / 255.0F;
		float green = (float) (color >> 8 & 255) / 255.0F;
		float blue = (float) (color & 255) / 255.0F;
		
		GlStateManager.color(red, green, blue, alpha);
	}
	
	public static void color(float red, float green, float blue, float alpha) {
		if (red != colorState.red || green != colorState.green || blue != colorState.blue || alpha != colorState.alpha) {
			colorState.red = red;
			colorState.green = green;
			colorState.blue = blue;
			colorState.alpha = alpha;
			
			GL11.glColor4f(red, green, blue, alpha);
		}
	}

	public static void enableBlend() {
		blendState.blend.setEnabled();
	}

	public static void disableBlend() {
		blendState.blend.setDisabled();
	}

	public static void enableDepth() {
		depthState.depthTest.setEnabled();
	}

	public static void disableDepth() {
		depthState.depthTest.setDisabled();
	}
	
	public static void enableTexture2D() {
		textureState.texture2DState.setEnabled();
	}

	public static void disableTexture2D() {
		textureState.texture2DState.setDisabled();
	}
	
	public static void bindTexture(int textureID) {
		if(textureID != textureState.textureID) {
			textureState.textureID = textureID;
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		}
	}
	
	public static void tryBlendFuncSeparate(GlStateManager.SourceFactor srcFactor, GlStateManager.DestFactor dstFactor, GlStateManager.SourceFactor srcFactorAlpha, GlStateManager.DestFactor dstFactorAlpha) {
		tryBlendFuncSeparate(srcFactor.factor, dstFactor.factor, srcFactorAlpha.factor, dstFactorAlpha.factor);
	}
	
	public static void tryBlendFuncSeparate(int srcFactor, int dstFactor, int srcFactorAlpha, int dstFactorAlpha) {
		if (srcFactor != blendState.srcFactor || dstFactor != blendState.dstFactor || srcFactorAlpha != blendState.srcFactorAlpha || dstFactorAlpha != blendState.dstFactorAlpha) {
			blendState.srcFactor = srcFactor;
			blendState.dstFactor = dstFactor;
			blendState.srcFactorAlpha = srcFactorAlpha;
			blendState.dstFactorAlpha = dstFactorAlpha;
			
			GL14.glBlendFuncSeparate(srcFactor, dstFactor, srcFactorAlpha, dstFactorAlpha);
		}
	}
	
	public static class Color {
		public float red;
		public float green;
		public float blue;
		public float alpha;

		public Color() {
			this(1.0F, 1.0F, 1.0F, 1.0F);
		}

		public Color(float redIn, float greenIn, float blueIn, float alphaIn) {
			this.red = 1.0F;
			this.green = 1.0F;
			this.blue = 1.0F;
			this.alpha = 1.0F;
			this.red = redIn;
			this.green = greenIn;
			this.blue = blueIn;
			this.alpha = alphaIn;
		}
	}
	
	public static class BlendState {
		public GlStateManager.BooleanState blend;
		public int srcFactor;
		public int dstFactor;
		public int srcFactorAlpha;
		public int dstFactorAlpha;

		private BlendState() {
			this.blend = new GlStateManager.BooleanState(GL11.GL_BLEND);
			this.srcFactor = 1;
			this.dstFactor = 0;
			this.srcFactorAlpha = 1;
			this.dstFactorAlpha = 0;
		}
	}
	
	public static class DepthState {
		public GlStateManager.BooleanState depthTest;
		public boolean maskEnabled;
		public int depthFunc;

		private DepthState() {
			this.depthTest = new GlStateManager.BooleanState(GL11.GL_DEPTH_TEST);
			this.maskEnabled = true;
			this.depthFunc = GL11.GL_LESS;
		}
	}
	
	public static class TextureState {
		public GlStateManager.BooleanState texture2DState;
		public int textureID;

		private TextureState() {
			this.texture2DState = new GlStateManager.BooleanState(GL11.GL_TEXTURE_2D);
		}
	}
	
	public static enum SourceFactor {
		CONSTANT_ALPHA(32771), CONSTANT_COLOR(32769), DST_ALPHA(772), DST_COLOR(774), ONE(1),
		ONE_MINUS_CONSTANT_ALPHA(32772), ONE_MINUS_CONSTANT_COLOR(32770), ONE_MINUS_DST_ALPHA(773),
		ONE_MINUS_DST_COLOR(775), ONE_MINUS_SRC_ALPHA(771), ONE_MINUS_SRC_COLOR(769), SRC_ALPHA(770),
		SRC_ALPHA_SATURATE(776), SRC_COLOR(768), ZERO(0);

		public final int factor;

		private SourceFactor(int factorIn) {
			this.factor = factorIn;
		}
	}
	
	public static enum DestFactor {
		CONSTANT_ALPHA(32771), CONSTANT_COLOR(32769), DST_ALPHA(772), DST_COLOR(774), ONE(1),
		ONE_MINUS_CONSTANT_ALPHA(32772), ONE_MINUS_CONSTANT_COLOR(32770), ONE_MINUS_DST_ALPHA(773),
		ONE_MINUS_DST_COLOR(775), ONE_MINUS_SRC_ALPHA(771), ONE_MINUS_SRC_COLOR(769), SRC_ALPHA(770), SRC_COLOR(768),
		ZERO(0);

		public final int factor;

		private DestFactor(int factorIn) {
			this.factor = factorIn;
		}
	}
	
	public static class BooleanState {
		private final int capability;
		private boolean currentState;

		public BooleanState(int capabilityIn) {
			this.capability = capabilityIn;
		}

		public void setDisabled() {
			this.setState(false);
		}

		public void setEnabled() {
			this.setState(true);
		}

		public void setState(boolean state) {
			if(state != this.currentState) {
				this.currentState = state;

				if(state) {
					GL11.glEnable(this.capability);
				} else {
					GL11.glDisable(this.capability);
				}
			}
		}
	}
}

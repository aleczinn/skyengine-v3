package de.skyengine.graphics.internal;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import de.skyengine.core.internal.IDisposable;
import de.skyengine.graphics.GlStateManager;

public class Texture implements IDisposable {

	private int textureID;
	private int width, height;
	
	public Texture() {
		this.textureID = GL11.glGenTextures();
	}
	
	public void bind() {
		GlStateManager.bindTexture(this.textureID);
	}
	
	public void unbind() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
	
	/**
	 * Creates a texture with specified width, height and data.
	 *
	 * @param width  Width of the texture
	 * @param height Height of the texture
	 * @param data   Picture Data in RGBA format
	 *
	 * @return Texture from the specified data
	 */
	public static Texture createTexture(int width, int height, ByteBuffer data) {
		Texture texture = new Texture();
		texture.setWidth(width);
		texture.setHeight(height);

		texture.bind();

//		texture.setParameter(GL11.GL_TEXTURE_WRAP_S, GL13.GL_CLAMP_TO_BORDER);
//		texture.setParameter(GL11.GL_TEXTURE_WRAP_T, GL13.GL_CLAMP_TO_BORDER);
		
		texture.setParameter(GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		texture.setParameter(GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

		texture.uploadData(GL11.GL_RGBA8, width, height, GL11.GL_RGBA, data);
		return texture;
	}
	
	/**
	 * Sets a parameter of the texture.
	 *
	 * @param name  Name of the parameter
	 * @param value Value to set
	 */
	public void setParameter(int name, int value) {
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, name, value);
	}

	/**
	 * Uploads image data with specified width and height.
	 *
	 * @param width  Width of the image
	 * @param height Height of the image
	 * @param data   Pixel data of the image
	 */
	public void uploadData(int width, int height, ByteBuffer data) {
		this.uploadData(GL11.GL_RGBA8, width, height, GL11.GL_RGBA, data);
	}

	/**
	 * Uploads image data with specified internal format, width, height and image
	 * format.
	 *
	 * @param internalFormat Internal format of the image data
	 * @param width          Width of the image
	 * @param height         Height of the image
	 * @param format         Format of the image data
	 * @param data           Pixel data of the image
	 */
	public void uploadData(int internalFormat, int width, int height, int format, ByteBuffer data) {
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, internalFormat, width, height, 0, format, GL11.GL_UNSIGNED_BYTE, data);
	}

	/**
	 * Load texture from file.
	 *
	 * @param path File path of the texture
	 *
	 * @return Texture from specified file
	 */
	public static Texture loadTexture(String path) {
		ByteBuffer data;
		int width, height;

		try(MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer comp = stack.mallocInt(1);

			STBImage.stbi_set_flip_vertically_on_load(false);
			data = STBImage.stbi_load(path, w, h, comp, 4);
			if(data == null) throw new RuntimeException("Failed to load a texture file!" + System.lineSeparator() + STBImage.stbi_failure_reason());

			width = w.get();
			height = h.get();
		}
		return createTexture(width, height, data);
	}
	
	public int getID() {
		return textureID;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	@Override
	public void dispose() {
		GL11.glDeleteTextures(this.textureID);
	}
}

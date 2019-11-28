package de.skyengine.graphics;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL15;
import org.lwjgl.system.MemoryUtil;

import de.skyengine.core.internal.IDisposable;

public class VBO implements IDisposable {

	private int vboID;
	private int colorID;
	
	private FloatBuffer vertexData;
	private FloatBuffer colorData;
	
	private int vertexCount;
	
	public final int VERTEX_SIZE = 2;	// X, Y
	public final int UV_SIZE = 2; 			// U, V
	public final int COLOR_SIZE = 4;		// R, G, B, A
	
	private boolean created = false;
	private boolean isStatic;
	private final int usage;
	
	/**
	 * 
	 * @param isStatic				| true = If you add a new draw call it is possible that errors may occur. [Draw a lot = static] | false
	 * @param maxBufferSize	| 
	 */
	public VBO(boolean isStatic, int maxBufferSize) {
		this(isStatic, isStatic ? GL15.GL_STATIC_DRAW : GL15.GL_DYNAMIC_DRAW, maxBufferSize);
	}
	
	public VBO(boolean isStatic, int usage, int maxBufferSize) {
		this.vboID = GL15.glGenBuffers();
		this.colorID = GL15.glGenBuffers();
		
		this.isStatic = isStatic;
		this.usage = usage;
		
		this.vertexData = MemoryUtil.memAllocFloat(maxBufferSize);
		this.colorData = MemoryUtil.memAllocFloat(maxBufferSize);
	}
	
	public void setVertices(float[] vertices, float[] colors, int vertexCount, int colorCount) {
		this.vertexCount = vertexCount;
		
		int vSize = vertexCount * this.VERTEX_SIZE;
		int cSize = colorCount * this.COLOR_SIZE;
		
		this.vertexData.position(0);
		this.vertexData.limit(vSize);
		
		this.colorData.position(0);
		this.colorData.limit(cSize);

		for(int v = 0; v < vSize; v += this.VERTEX_SIZE) {
			this.vertexData.put(vertices[v]);
			this.vertexData.put(vertices[v + 1]);
		}
		
		for(int c = 0; c < cSize; c += this.COLOR_SIZE) {
			this.colorData.put(colors[c]);
			this.colorData.put(colors[c + 1]);
			this.colorData.put(colors[c + 2]);
			this.colorData.put(colors[c + 3]);
		}
		
		this.vertexData.flip();
		this.colorData.flip();
		
		if(this.isStatic) {
			if(!this.created) {
				GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vboID);
				GL15.glBufferData(GL15.GL_ARRAY_BUFFER, this.vertexData, this.usage);
				GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
				
				GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.colorID);
				GL15.glBufferData(GL15.GL_ARRAY_BUFFER, this.colorData, this.usage);
				GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
				this.created = true;
			} else {
				GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vboID);
				GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, this.vertexData);
				GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
				
				GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.colorID);
				GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, this.colorData);
				GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
			}
		} else {
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vboID);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, this.vertexData, this.usage);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
			
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.colorID);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, this.colorData, this.usage);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		}
	}
	
	public void render(int glType) {
		if(this.vertexCount == 0) return;
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vboID);
		GL15.glVertexPointer(this.VERTEX_SIZE, GL15.GL_FLOAT, 0, 0);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.colorID);
		GL15.glColorPointer(this.COLOR_SIZE, GL15.GL_FLOAT, 0, 0);
		
		GL15.glEnableClientState(GL15.GL_VERTEX_ARRAY);
		GL15.glEnableClientState(GL15.GL_COLOR_ARRAY);
		
		GL15.glDrawArrays(glType, 0, this.vertexCount);
		
		GL15.glDisableClientState(GL15.GL_COLOR_ARRAY);
		GL15.glDisableClientState(GL15.GL_VERTEX_ARRAY);
	}
	
	@Override
	public void dispose() {
		MemoryUtil.memFree(this.vertexData);
		MemoryUtil.memFree(this.colorData);	
		
		GL15.glDeleteBuffers(this.vboID);
		GL15.glDeleteBuffers(this.colorID);	
	}
	
	public int getVboID() {
		return vboID;
	}
	
	public boolean isStatic() {
		return isStatic;
	}
	
	public boolean isCreated() {
		return created;
	}
}

package de.skyengine.graphics.internal;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import de.skyengine.core.SkyEngine;
import de.skyengine.core.internal.IDisposable;
import de.skyengine.utils.math.Matrix4f;
import de.skyengine.utils.math.Vector2d;
import de.skyengine.utils.math.Vector3f;
import de.skyengine.utils.math.Vector4f;

public class Shader implements IDisposable {

	private int programID;
	private int vertexID;
	private int fragmentID;
	
	private String vertex;
	private String fragment;
	private String sourceVertex;
	private String sourceFragment;
	
	private FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	
	public static final String PROJECTION = "projectionMatrix";
	public static final String VERTICES = "vertices";
	public static final String COLOR = "color";
	
	public Shader(String vertexPath, String fragmentPath) {
		this.sourceVertex = vertexPath;
		this.sourceFragment = fragmentPath;

		this.vertex = SkyEngine.files().loadAsString(vertexPath);
		this.fragment = SkyEngine.files().loadAsString(fragmentPath);
		
		if (this.vertex == null)
			throw new IllegalArgumentException("Vertex Shader must not be null!");
		if (this.fragment == null)
			throw new IllegalArgumentException("Fragment Shader must not be null!");

		this.createShader(this.vertex, this.fragment);
	}
	
	private void createShader(String vertexShader, String fragmentShader) {
		this.programID = GL20.glCreateProgram();
		this.vertexID = this.loadShader(GL20.GL_VERTEX_SHADER, vertexShader);
		this.fragmentID = this.loadShader(GL20.GL_FRAGMENT_SHADER, fragmentShader);

		GL20.glAttachShader(this.programID, this.vertexID);
		GL20.glAttachShader(this.programID, this.fragmentID);

		// Bind Attributes here
		this.bindAttributes();

		GL20.glLinkProgram(this.programID);
		if (GL20.glGetProgrami(this.programID, GL20.GL_LINK_STATUS) == GL20.GL_FALSE) {
			throw new IllegalArgumentException(GL20.glGetProgramInfoLog(this.programID));
		}

		GL20.glValidateProgram(this.programID);
		if (GL20.glGetProgrami(this.programID, GL20.GL_VALIDATE_STATUS) == GL20.GL_FALSE) {
			throw new IllegalArgumentException(GL20.glGetProgramInfoLog(this.programID));
		}
	}
	
	private void bindAttributes() {
		GL20.glBindAttribLocation(this.programID, 0, "vertices");
		GL20.glBindAttribLocation(this.programID, 1, "color");
	}
	
	private int loadShader(int type, String shader) {
		int shaderID = GL20.glCreateShader(type);
		
		GL20.glShaderSource(shaderID, shader);
		GL20.glCompileShader(shaderID);
		
		if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL20.GL_FALSE) {
			if (type == GL20.GL_VERTEX_SHADER)
				System.err.println("VertexShaderPath: " + this.sourceVertex);
			if (type == GL20.GL_FRAGMENT_SHADER)
				System.err.println("FragmentShaderPath: " + this.sourceFragment);
			throw new IllegalArgumentException(GL20.glGetShaderInfoLog(shaderID));
		}
		return shaderID;
	}

	public void bind() {
		GL20.glUseProgram(this.programID);
	}

	public void unbind() {
		GL20.glUseProgram(0);
	}
	
	protected int getUniformLocation(String uniformName) {
		return GL20.glGetUniformLocation(this.programID, uniformName);
	}

	protected void bindAttribute(int attribute, String variableName) {
		GL20.glBindAttribLocation(this.programID, attribute, variableName);
	}

	public void uniformFloat(String location, float value) {
		GL20.glUniform1f(this.getUniformLocation(location), value);
	}

	public void uniformInt(String location, int value) {
		GL20.glUniform1i(this.getUniformLocation(location), value);
	}

	public void uniformVector3f(String location, Vector3f vector) {
		GL20.glUniform3f(this.getUniformLocation(location), vector.x, vector.y, vector.z);
	}

	public void uniformVector4f(String location, Vector4f vector) {
		GL20.glUniform4f(this.getUniformLocation(location), vector.x, vector.y, vector.z, vector.w);
	}

	public void uniformVector2d(String location, Vector2d vector) {
		GL20.glUniform2f(this.getUniformLocation(location), vector.xAsFloat(), vector.yAsFloat());
	}
	
	public void uniformMatrix4f(String location, Matrix4f matrix) {
		matrix.toBuffer(this.matrixBuffer);
		
		GL20.glUniformMatrix4fv(this.getUniformLocation(location), false, this.matrixBuffer);
	}
	
	protected void uniformBoolean(int location, boolean value) {
		float toLoad = 0;
		if (value) {
			toLoad = 1;
		}
		GL20.glUniform1f(location, toLoad);
	}
	
	public void setUniform(String name, int value) {
		int location = GL20.glGetUniformLocation(this.programID, name);
		if (location != -1) {
			GL20.glUniform1i(location, value);
		}
	}
	
	@Override
	public void dispose() {
		this.unbind();
		GL20.glDetachShader(this.programID, this.vertexID);
		GL20.glDetachShader(this.programID, this.fragmentID);

		GL20.glDeleteShader(this.vertexID);
		GL20.glDeleteShader(this.fragmentID);

		GL20.glDeleteProgram(this.programID);
	}
}

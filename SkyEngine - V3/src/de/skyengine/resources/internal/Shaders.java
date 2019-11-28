package de.skyengine.resources.internal;

import java.util.ArrayList;
import java.util.List;

import de.skyengine.core.internal.IDisposable;
import de.skyengine.graphics.internal.Shader;
import de.skyengine.utils.logging.Logger;

public class Shaders implements IDisposable {

	private final Logger logger = Logger.getLogger(Sounds.class.getName());
	private List<Shader> shaders;
	
	public Shaders() {
		this.shaders = new ArrayList<Shader>();
		
		this.logger.info("Shader initialized.");	
	}
	
	@SuppressWarnings("unused")
	private void loadShader(Shader shader) {
		this.shaders.add(shader);
	}
	
	@Override
	public void dispose() {
		for(Shader shader : this.shaders) {
			shader.dispose();
		}
	}
}

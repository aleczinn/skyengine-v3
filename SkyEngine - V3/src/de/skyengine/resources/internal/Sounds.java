package de.skyengine.resources.internal;

import de.skyengine.core.internal.IDisposable;
import de.skyengine.utils.logging.Logger;

public class Sounds implements IDisposable {

	private final Logger logger = Logger.getLogger(Sounds.class.getName());
	
//	public Audio jump;
	
	public Sounds() {
		try {
//			this.jump = new Audio("Jump", "assets/sounds/jump.wav");
			
			this.logger.info("Sounds initialized.");
		} catch (Exception e) {
			this.logger.fatal("Sounds -  Could not be initialized!", e);	
			e.printStackTrace();
		}
	}

	@Override
	public void dispose() {
		
	}
}

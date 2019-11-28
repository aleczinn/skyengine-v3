package de.skyengine.utils.logging;

import java.util.ArrayList;
import java.util.List;

public class LogManager {

	private static LogManager instance;
	
	private List<Logger> logger = new ArrayList<Logger>();
	private List<Log> logs = new ArrayList<Log>();
	
	public void register(Logger logger) {
		this.logger.add(logger);
	}
	
	public List<Logger> getLogger() {
		return logger;
	}
	
	public List<Log> getLogs() {
		return logs;
	}
	
	public static LogManager getLogManager() {
		if(instance == null) {
			instance = new LogManager();
		}
		return instance;
	}
}

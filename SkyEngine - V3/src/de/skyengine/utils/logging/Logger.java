package de.skyengine.utils.logging;

import java.time.LocalDateTime;

import de.skyengine.utils.TimeUtils;

public class Logger {

	private String name;
	
	private Logger(String name) {
		this.name = name;
	}
	
	public void info(String message) {
		this.message(message, LogLevel.INFO, null);
	}
	
	public void debug(String message) {
		this.message(message, LogLevel.DEBUG, null);
	}
	
	public void error(String message) {
		this.message(message, LogLevel.ERROR, null);
	}
	
	public void fatal(String message) {
		this.message(message, LogLevel.FATAL, null);
	}
	
	public void info(String message, Throwable throwable) {
		this.message(message, LogLevel.INFO, throwable);
	}
	
	public void debug(String message, Throwable throwable) {
		this.message(message, LogLevel.DEBUG, throwable);
	}
	
	public void error(String message, Throwable throwable) {
		this.message(message, LogLevel.ERROR, throwable);
	}
	
	public void fatal(String message, Throwable throwable) {
		this.message(message, LogLevel.FATAL, throwable);
	}
	
	private void message(String message, LogLevel level, Throwable throwable) {
		LocalDateTime ldt = LocalDateTime.now();
		
		String time = TimeUtils.timeFormatter.format(ldt);
		String date = TimeUtils.dateFormatter.format(ldt);
		
		String threadName = Thread.currentThread().getName();
		String method = new Throwable().getStackTrace()[2].getMethodName();
		
		switch (level) {
		case INFO:
		case DEBUG:
			System.out.println("[" + time + "] " + "[" + threadName + "/" + level.toString() + "] " + this.name + " " + method + " " + message);	
			break;
		case ERROR:
		case FATAL:
			System.err.println("[" + time + "] " + "[" + threadName + "/" + level.toString() + "] " + this.name + " " + method + " " + message);
			break;
		default:
			break;
		}
		
		LogManager.getLogManager().getLogs().add(new Log(message, time, date, level, throwable));
	}
	
	public static Logger getLogger(String name) {
		LogManager manager = LogManager.getLogManager();
		Logger logger = new Logger(name);
		manager.register(logger);
		return logger;
	}
}

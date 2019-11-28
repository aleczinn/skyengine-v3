package de.skyengine.utils.logging;

public class Log {

	private String message;
	private String time;
	private String date;
	private LogLevel level;
	
	private Throwable throwable;
	
	public Log(String message, String time, String date, LogLevel level) {
		this(message, time, date, level, null);
	}
	
	public Log(String message, String time, String date, LogLevel level, Throwable throwable) {
		this.message = message;
		this.time = time;
		this.date = date;
		this.level = level;
		this.throwable = throwable;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getTime() {
		return time;
	}
	
	public String getDate() {
		return date;
	}
	
	public LogLevel getLevel() {
		return level;
	}
	
	public Throwable getThrowable() {
		return throwable;
	}
}

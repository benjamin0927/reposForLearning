package org.benjamin.log.appender;

import org.benjamin.log.LogLevel;

public abstract class LogAppender {
	
	public abstract void log(LogLevel logLevel, String message);
}

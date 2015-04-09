package org.benjamin.log.appender;

import java.io.PrintStream;

import org.benjamin.log.LogLevel;

public class LogAppenderConsole extends LogAppender {
	PrintStream writer = System.out;
	@Override
	public void log(LogLevel logLevel, String message) {
		writer.println(logLevel + " - " + message);
	}

}

package org.benjamin.log.appender;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.benjamin.log.LogLevel;

public class LogAppenderFile extends LogAppender {
	private static final String LOG_FILE_PATH = "log.file.path";
	private static String logFilePath = "";
	private static FileWriter fileWriter;

	public LogAppenderFile(){
		logFilePath = System.getProperty(LOG_FILE_PATH);
		try {
			fileWriter = this.getFileWriter(logFilePath);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
	@Override
	public void log(LogLevel logLevel, String message) {
		try {
			fileWriter.write(logLevel + " - " + message + "\n");
			fileWriter.flush();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}

	private FileWriter getFileWriter(String filePath) throws IOException{
		File file = new File(filePath);
		if (!file.exists()) {
			file.createNewFile();
		}
		return new FileWriter(file.getAbsoluteFile(),true);
	}
}

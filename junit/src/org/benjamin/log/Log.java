package org.benjamin.log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Properties;
import java.util.Map.Entry;

import org.benjamin.log.appender.LogAppender;

public class Log {
	private static final String LOG_LEVEL = "log.level";
	private static final String IS_SPECIFIED_LOG_LEVEL = "is.specified.log.level";
	private static final String LOG_APPENDER = "log.appender";
	private static LogLevel logLevel = LogLevel.INFO;
	private static boolean isSpecifiedLogLevel = false;
	PrintStream writer = System.out;
	LogAppender logAppender;
	
	public Log(){
		try {
			loadLogProperties();
			logLevel = LogLevel.valueOf(System.getProperty(LOG_LEVEL));
			isSpecifiedLogLevel = Boolean.valueOf(System.getProperty(IS_SPECIFIED_LOG_LEVEL));
			logAppender = (LogAppender) Class.forName(System.getProperty(LOG_APPENDER)).newInstance();
		} catch (Exception e) {
			writer.println("Using the default Log Level - " + logLevel);
		}
	}
	
	public void debug(String message){
		this.log(LogLevel.DEBUG, message);
	}
	
	public void info(String message){
		this.log(LogLevel.INFO, message);
	}
	
	public void warn(String message){
		this.log(LogLevel.WARN, message);
	}
	
	public void error(String message){
		this.log(LogLevel.ERROR, message);
	}
	
	private void log(LogLevel logLevel, String message) {
		if(this.compareLevel(logLevel)) {
			logAppender.log(logLevel, message);
		}
	}
	
	private boolean compareLevel(LogLevel logLevel) {
		boolean comparedLevel = false;
		if(isSpecifiedLogLevel) {
			comparedLevel = this.logLevel.getLevel() == logLevel.getLevel();
		} else{
			comparedLevel = this.logLevel.getLevel() >= logLevel.getLevel();
		}
		return comparedLevel;
	}
	
	private void loadLogProperties() throws FileNotFoundException, IOException{
		Properties properties = new Properties();
		properties.load(new FileInputStream(new File(Log.class.getClassLoader().getResource("log.properties").getFile())));
		Iterator<Entry<Object, Object>> iterator = properties.entrySet().iterator(); 
        while (iterator.hasNext()) {  
            Entry<Object, Object> entry = iterator.next();  
            Object key = entry.getKey();  
            Object value = entry.getValue();  
            // writer.println("key   :" + key);  
            // writer.println("value :" + value);  
            System.setProperty((String)key, (String)value);
        } 
	}
}

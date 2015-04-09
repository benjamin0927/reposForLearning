package org.benjamin.log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Properties;
import java.util.Map.Entry;

public class Log {
	public static final String logLevelProperty = "log.level";
	private static LogLevel logLevel = LogLevel.INFO;
	PrintStream writer = System.out;
	
	public Log(){
		try {
			loadLogProperties();
			logLevel = LogLevel.valueOf(System.getProperty("log.level"));
		} catch (Exception e) {
			writer.println("Using the default Log Level - " + logLevel);
		}
	}
	
	public void debug(String message){
		if(this.compareLevel(LogLevel.DEBUG)) {
			writer.println(message);
		}
	}
	
	public void info(String message){
		if(this.compareLevel(LogLevel.INFO)) {
			writer.println(message);
		}
	}
	
	public void warn(String message){
		if(this.compareLevel(LogLevel.WARN)) {
			writer.println(message);
		}
	}
	
	public void error(String message){
		if(this.compareLevel(LogLevel.ERROR)) {
			writer.println(message);
		}
	}
	
	private boolean compareLevel(LogLevel logLevel) {
		return this.logLevel.getLevel() >= logLevel.getLevel();
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

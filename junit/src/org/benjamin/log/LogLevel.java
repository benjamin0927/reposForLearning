package org.benjamin.log;

public enum LogLevel {
	NONE(0), DEBUG(1),INFO(2),WARN(3),ERROR(4),FATAL(5);
	
	private int level = 2;
	
	private LogLevel(int value){
		this.level = value;
	}
	
    public int getLevel() {
        return this.level;
    }
}

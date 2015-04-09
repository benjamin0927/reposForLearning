package org.benjamin.log;

public enum LogLevel {
	DEBUG(0),INFO(1),WARN(2),ERROR(3);
	
	private int level = 0;
	
	private LogLevel(int value){
		this.level = value;
	}
	
    public int getLevel() {
        return this.level;
    }
}

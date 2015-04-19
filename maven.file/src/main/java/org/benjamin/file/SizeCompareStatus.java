package org.benjamin.file;

public enum SizeCompareStatus {
	BIGGER(1),EQUAL(0),SMALLER(-1);
	
	private int compareStatus;
	private SizeCompareStatus(int compareStatus){
		this.compareStatus = compareStatus;
	}
	
	public static SizeCompareStatus getSizeCompareStatus(int compareStatus) {
		SizeCompareStatus sizeCompareStatus = SizeCompareStatus.EQUAL;
		if(compareStatus == 1) {
			return SizeCompareStatus.BIGGER;
		} else if(compareStatus == -1){
			return SizeCompareStatus.SMALLER;
		}
		
		return sizeCompareStatus;
	}
}

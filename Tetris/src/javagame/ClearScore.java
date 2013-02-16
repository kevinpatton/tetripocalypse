package javagame;

import java.io.Serializable;

public class ClearScore implements Serializable, Comparable<ClearScore> {
	
	public static final long serialVersionUID = 1111L;
	public static final int LENGTH = 50;
		
	public String name;
	
	private long elapsedTime;
	
	public ClearScore(String name, long elapsedTime) {
		this.name = name;
		this.elapsedTime = elapsedTime;
	}
	
	public long getElapsedTime() {
		return elapsedTime;
	}
	
	@Override
	public String toString() {
		double seconds = elapsedTime / 1000;
		return new Double(seconds).toString() + " seconds";
	}
	
	public int compareTo(ClearScore that) {
		final int BEFORE = -1;
	    final int EQUAL = 0;
	    final int AFTER = 1;
	    
	    if (elapsedTime == that.getElapsedTime())
	    	return EQUAL;
	    else if (elapsedTime < that.getElapsedTime())
	    	return BEFORE;
	    else
	    	return AFTER;
	}
}
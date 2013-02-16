package javagame;

import java.io.Serializable;
import java.text.DecimalFormat;

public class ClearScore implements Serializable, Comparable<ClearScore> {
	
	public static final long serialVersionUID = 1111L;
	public static final int LENGTH = 50;
		
	public String name;
	
	private long elapsedTime;
	private static DecimalFormat format;
	
	public ClearScore(String name, long elapsedTime) {
		this.name = name;
		this.elapsedTime = elapsedTime;
		format = new DecimalFormat("#.00");
	}
	
	public long getElapsedTime() {
		return elapsedTime;
	}
	
	@Override
	//returns this object as a String representation
	public String toString() {
		Double d = new Double(elapsedTime);
		int z = name.length() + 3 + d.toString().length();
		int numberOfSpaces = LENGTH - z;
		String spaces = "";
		for (int i = 0; i < numberOfSpaces; i++)
			spaces += " ";
		double seconds = (double)elapsedTime / 1000.00D;
		return name + spaces + format.format(seconds) + " seconds";
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
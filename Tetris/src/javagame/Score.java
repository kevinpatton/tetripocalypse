package javagame;

import java.io.Serializable;

public class Score implements Serializable, Comparable<Score> {
	
	public static final long serialVersionUID = 111L;
	public static final int LENGTH = 50;
	
	public String name;
	public int score;
	
	public Score(String name, int score) {
		this.name=name;
		this.score=score;
	}
	
	@Override
	public String toString() {
		Integer s = new Integer(score);
		int z = name.length() + 3 + s.toString().length();
		int numberOfSpaces = LENGTH - z;
		String spaces = "";
		for (int i = 0; i < numberOfSpaces; i++)
			spaces += " ";
		return name + spaces + score + " pts";
	}
	
	public int compareTo(Score that) {
		final int BEFORE = -1;
	    final int EQUAL = 0;
	    final int AFTER = 1;
	    
	    //not done.. obviously..
	    if (this.score == that.score)
	    	return EQUAL;
	    else if (this.score < that.score)
	    	return BEFORE;
	    else
	    	return AFTER;
	}
}

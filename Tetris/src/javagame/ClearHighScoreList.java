package javagame;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class ClearHighScoreList implements Serializable {

	public static final long serialVersionUID = 43L;
	public static final int MAX_SIZE = 20;
	private ArrayList<ClearScore> highScores;
	
	public ClearHighScoreList() {
		highScores = generateDefaultScores();
	}
	
	/**
	 * @param s the score to compare to the high score list and add if necessary
	 * @return whether the player has made a high score
	 */
	public boolean add(ClearScore s) {
		Collections.sort(highScores);
		Collections.reverse(highScores);
		long lowestScore = highScores.get(0).getElapsedTime();
		boolean madeIt = false;
		//and then we compare lowest score to s and see
		//if s needs to be added to the list
		if (s.getElapsedTime() < lowestScore) {
			if (highScores.size() >= MAX_SIZE)
				highScores.remove(0);
			highScores.add(s);
			madeIt = true;
		}
		Collections.sort(highScores);
		//Collections.reverse(highScores);
		return madeIt;
	}
	
	public boolean checkForHighScore(long time) {
		Collections.sort(highScores);
		Collections.reverse(highScores);
		long lowestScore = highScores.get(0).getElapsedTime();
		return time < lowestScore;
	}

	// Returns this HighScoreList in a String representation.
	public String toString() {
		String estr = "";
		for (ClearScore s : highScores) {
			estr += s + "\n";
		}
		return estr;
	}
	
	/** Populates this HighScoreList with default scores. You know,
	 *  so the player can compete against zany names.
	 *  @return the initialized list of Score objects */
	private ArrayList<ClearScore> generateDefaultScores() {
		ArrayList<ClearScore> clearScores = new ArrayList<ClearScore>();
		String[] names = {"Poly Tsarn", "Brennan Jr.", "Bob Face", "Cacarapak",
				"Nin-ten-ele", "Hillder", "Maranyne", "Fluffy Bunny Old Slopes", "K-man",
				"Ashara"};
		for (int i = 0; i < 10; i++) {
			ClearScore s = new ClearScore(names[i], 52043L * i + 51211L);
			clearScores.add(s);
		}
		return clearScores;
	}
}

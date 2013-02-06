package javagame;

import java.util.Random;
import java.util.ArrayList;

/**
 * A special RNG that returns integers in accordance with
 * Tetris rules. Contrary to popular belief, the order of
 * tetromino pieces given to the player is not random.
 * 
 * Imagine sticking one of each type of tetromino into a
 * bag. Then keep drawing pieces until the bag is empty.
 * Then we put the pieces back in the bag and start the
 * process over again. That is how Tetris avoids the
 * problem of going too long without giving the player a
 * certain piece.
 * 
 * @author Kevin Patton
 */
public class SevenBag {

	/**
	 * The number of tetronimos in the 'bag.'
	 */
	public static final int NUM_ELEMENTS = 7;
	private Random RNG;
	private ArrayList<Integer> valuesRemaining;
	
	/**
	 * Class constructor.
	 */
	public SevenBag() {
		RNG = new Random();
		valuesRemaining = new ArrayList<Integer>();
		populateList();
	}
	
	/**
	 * Returns the next Tetrad ID integer. Think of it
	 * as the method that draws a Tetrad from the bag
	 * and returns it.
	 * @return the integer ID of the next Tetrad
	 */
	public int nextInt() {
		int size = valuesRemaining.size();
		if (size == 0) {
			populateList();
			size = NUM_ELEMENTS;
		}
		return valuesRemaining.remove(RNG.nextInt(size));
	}
	
	/**
	 * Initializes and re-populates the list of remaining
	 * Tetrad IDs. Think of it as the method that fills
	 * the bag up again when it is empty.
	 */
	private void populateList() {
		for (int i = 1; i < NUM_ELEMENTS + 1; i++)
			valuesRemaining.add(i);
	}
}

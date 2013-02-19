package javagame;

import java.util.ArrayList;
import org.newdawn.slick.*;

/**
 * @author Kevin Patton
 * A menu with various choices. Menu items can be added and removed.
 * Control schemes can be refined, and the menu selection can be captured
 * and returned. Can also launch various game states.
 */
public class MenuStrip {
	
	public static final int DEFAULT_SELECTION = 0;
	public static final int DEFAULT_NEXT = Input.KEY_DOWN;
	public static final int DEFAULT_PREVIOUS = Input.KEY_UP;
	public static final int DEFAULT_SELECT = Input.KEY_ENTER;
	public static final float SELECT_VOLUME = 0.5f;
	public static final float DEFAULT_PITCH = 1.0f;
	
	private ArrayList<Interactable> menuItems;
	private int[] menuSong;
	private int menuSongIndex = 0;
	
	private Animation cursorAnimation;
	/** The sound the plays when the user changes the menu selection. */
	private Sound selectSound;
	/**
	 * The menu item that currently has 'focus.' In other words,
	 * if the user presses enter while selection is 2, then the
	 * second menu item's code will launch.
	 */
	private int selection = DEFAULT_SELECTION;
	/** The key to press in order to go to the next menu item. */
	private int nextKey = DEFAULT_NEXT;
	/** The key to press in order to go to the previous menu item. */
	private int previousKey = DEFAULT_PREVIOUS;
	/** The key to press in order to select the current menu item. */
	private int selectKey = DEFAULT_SELECT;
	private int size = 0;
	private int x = 0;
	private int y = 0;
	private int lineHeight = 0;
	//private float selectPitch = DEFAULT_PITCH;
	//private int semitone = -1;
	
	/**
	 * Constructor that allows you to initialize the menu items.
	 * @param items the menu items to be displayed and chooseable
	 */
	public MenuStrip(Interactable...items) {
		menuItems = new ArrayList<Interactable>();
		Image cursorSheet = null;
		try {
			selectSound = new Sound("res/rotate.wav");
			cursorSheet = new Image("res/cursor.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
		cursorAnimation = new Animation(new SpriteSheet(cursorSheet, 32, 32), 100);
		for (Interactable item : items) {
			menuItems.add(item);
			size++;
		}
		if (size > 0)
			lineHeight = menuItems.get(0).getHeight();
		menuSong = initMenuSong();
	}
	
	/**
	 * Default constructor.
	 */
	public MenuStrip() {
		menuItems = new ArrayList<Interactable>();
		size = 0;
	}
	
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void gameActive() {
		menuItems.get(0).changeItem("Resume Game");
	}
	
	public void gameDone() {
		menuItems.get(0).changeItem("New Game");
	}
	
	public int getSelection() {
		return selection;
	}
	
	public void playMenuSongNote() {
		selectSound.play(z(menuSong[menuSongIndex]), SELECT_VOLUME);
		if (menuSongIndex >= menuSong.length - 1)
			menuSongIndex = 0;
		else
			menuSongIndex++;
	}
	
	public void render(Graphics g) {
		cursorAnimation.draw(x - 40, y + selection * lineHeight);
		for (Interactable i : menuItems) {
			i.render(x, y);
		}
	}
	
	/**
	 * Obtains input from the parent class and performs
	 * the necessary updates to this MenuStrip as a result.
	 * @param input an object containing the user's input information
	 * @return the menu selection chosen by the player
	 */
	public int acceptInput(Input input) {
		if (input.isKeyPressed(nextKey)) {
			advanceSelection();
			//selectSound.play(z(semitone++), SELECT_VOLUME);
			playMenuSongNote();
		}
		if (input.isKeyPressed(previousKey)) {
			regressSelection();
			//selectSound.play(z(semitone++), SELECT_VOLUME);
			playMenuSongNote();
		}
		if (input.isKeyPressed(selectKey)) {
			System.out.println("Selected " + selection + ".");
		//	selectPitch = DEFAULT_PITCH;
			return selection;
		}
		return -1;
	}
	
	//c, c#, d, ef, e, f, f#, g, af, a, bf, b -> c
	//-1, 0, 1, 2 , 3, 4, 5 , 6, 7 , 8, 9, 10
	private float z(int n) {
		return (float)Math.pow(2.0, n*1.0/12.0);
	}
	
	//private ArrayList<Integer> coi (String...notes) {
		//ArrayList<Integer> noteValues = new ArrayList<Integer>();
		//for (String note: notes) {
			//noteValues.add(convertStV(note));
		//}
		//return noteValues;
	//}
	
	private int[] initMenuSong() {
		//song = coi("d","a","b&","c","b&","a","g","g","b&","d","c","b&");
		int[] song = {1,-4,-3,-1,-3,-4,-6,-6,-3,1,-1,-3,-4,-3,-1,
				1,-3,-6,-6,-1,2,6,4,2,1,-3,1,-1,-3,-4,-4,-3,-1,1,-3,-6,-6,
				1,-3,-1,-4,-3,-6,-7,-4,1,-3,-1,-4,-3,1,6,5};
		return song;
	}
	
	/*private int convertStV(String note) {
		switch (note) {
		case"c":return -1;
		case"c#":return 0;
		case"d":return 1;
		case"e&":return 2;
		case"e":return 3;
		case"f":return 4;
		case"f#":return 5;
		case"g":return 6;
		case"a&":return 7;
		case"a":return 8;
		case"b&":return 9;
		case"b":return 10;
		default:return -1;
		}
	}*/
	
	/**
	 * Changes the menu selection to be one before whatever
	 * it is currently.
	 */
	private void regressSelection() {
		if (selection > 0)
			selection--;
		else if (selection == 0)
			selection = size - 1;
	}
	
	/**
	 * Changes the menu selection to be one after whatever
	 * it is currently.
	 */
	private void advanceSelection() {
		if (selection < size - 1)
			selection++;
		else if (selection == size - 1)
			selection = 0;
	}
	
	/**
	 * Adds a menu item to this MenuStrip.
	 * @param item the String to be added to the menu items
	 */
	public void addMenuItem(Interactable item) {
		menuItems.add(item);
		size = menuItems.size();
	}
	
	// Returns this object in a String representation.
	public String toString() {
		String estr = "";
		for (Interactable item : menuItems)
			estr += item + "\n";
		return estr;
	}
}

package javagame;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.FontUtils;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.Input;

public class UserInput extends BasicGameState {
	
	public static final int FONT_SIZE = 30;
	public static final int BLINK_DELAY = 400;
	public static final int MAX_INPUT_SIZE = 15;
	public static final int CURSOR_LENGTH = 25;
	public static final int CURSOR_HEIGHT = 2;
	public static final int CURSOR_OFFSET = 25;
	public static final int INPUT_OFFSET = 150;
	public static final int INPUT_Y = 280;
	
	private int ID;
	private UnicodeFont font;
	private int timeSinceBlink = 0;
	private int cursorX = INPUT_OFFSET;
	private String userInput = "";
	private int score = 0;
	
	private boolean blink = true;
	private Play play;
	
	public UserInput(int ID) {
		this.ID = ID;
	}
	
	public void setPlay(Play p) {
		play = p;
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		font = Utility.getFont(FONT_SIZE);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		FontUtils.drawCenter(font, "New high score!", 0, 15, 1024, Color.orange);
		FontUtils.drawCenter(font, "Enter your name:", 0, 65, 1024, Color.white);
		if (blink)
			g.fillRect(cursorX, INPUT_Y + CURSOR_OFFSET, CURSOR_LENGTH, CURSOR_HEIGHT);
		font.drawString(INPUT_OFFSET, INPUT_Y, userInput);
		
	}
	
	public void keyPressed(int z, char c) {
		int len = userInput.length();
		if (c == 8 && len > 0) // backspace
			userInput = userInput.substring(0, userInput.length() - 1);
		if ( len <= MAX_INPUT_SIZE && ((c > 64 && c < 123) || c == 32))
			userInput += "" + c;
		cursorX = 150 + font.getWidth(userInput) + 5;
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		timeSinceBlink += delta;
		
		if (timeSinceBlink >= BLINK_DELAY) {
			blink = !blink;
			timeSinceBlink = 0;
		}
		
		
		
		Input input = gc.getInput();
		if (input.isKeyPressed(Input.KEY_ENTER)) {
			int gameMode = play.getGameMode();
			if (gameMode == 0) {
				play.highScoreList.add(new Score(userInput, score));
				play.saveHighScores();
			}
			else if (gameMode == 1) {
				play.clearHighScoreList.add(new ClearScore(userInput, play.getElapsedTime()));
				play.saveClearHighScores();
			}
			userInput = "";
			score = 0;
			input.clearKeyPressedRecord();
			sbg.enterState(2); //gameover state
		}
	}
	
	public void receiveScore(int score) {
		this.score = score;
	}

	@Override
	public int getID() {
		return ID;
	}
}
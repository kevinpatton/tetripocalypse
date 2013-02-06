package javagame;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.*;
import org.newdawn.slick.util.*;

public class Credits extends BasicGameState {

	public static final int DELAY = 10;
	
	private int y = 700;
	private int timeSinceDelay = 0;
	private UnicodeFont font;
	private Image bg;
	
	public Credits(int state) {
		
	}
	
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {
		// TODO Auto-generated method stub
		font = Utility.getFont(30);
		bg = new Image("res/credits.png");

	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		FontUtils.drawCenter(font, "Programming", 0, y + 25, 1024, Color.orange);
		FontUtils.drawCenter(font, "Kevin Patton", 0, y + 50, 1024);
		FontUtils.drawCenter(font, "Art", 0, y + 150, 1024, Color.orange);
		FontUtils.drawCenter(font, "Estelore", 0, y + 175, 1024);
		FontUtils.drawCenter(font, "Kevin Patton", 0, y + 200, 1024);
		FontUtils.drawCenter(font, "Special Thanks", 0, y + 300, 1024, Color.orange);
		FontUtils.drawCenter(font, "to Estelore for great ideas!", 0, y + 325, 1024);
		FontUtils.drawCenter(font, "And to Alexey Pazhitnov for Tetris!", 0, y + 350, 1024);
		FontUtils.drawCenter(font, "CREDITS", 0, 40, 1024, Color.orange);
		g.drawImage(bg, 0, 0);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		timeSinceDelay += delta;
		if (timeSinceDelay >= DELAY) {
			if (y > 200)
				y--;
			timeSinceDelay = 0;
		}
		Input input = gc.getInput();
		if (input.isKeyPressed(Input.KEY_ESCAPE)) {
			y = 700;
			input.clearKeyPressedRecord();
			sbg.enterState(0); //menu
		}
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 3;
	}

}

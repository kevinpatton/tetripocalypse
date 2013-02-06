package javagame;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.Sound;

public class GameOver extends BasicGameState {
	
	private Play play;
	private Sound lose;
	
	public GameOver(int state, Play play) {
		this.play=play;
	}

	public void enter(GameContainer gc, StateBasedGame sbg) {
		lose.play();
	}
	
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		lose = new Sound("res/lose.wav");
	}

	public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2) throws SlickException {
		arg2.drawString("YOU LOSE, LOSER!", 300, 200);
		arg2.drawString("Final score: " + play.getScore(), 300, 220);
		arg2.drawString("High scores:", 300, 240);
		arg2.drawString(play.highScoreList.toString(), 150, 260);
	}

	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException {
		Input input = arg0.getInput();
		if (input.isKeyDown(Input.KEY_ENTER) || input.isKeyDown(Input.KEY_ESCAPE)) {
			play.resetGame();
			input.clearKeyPressedRecord();
			arg1.enterState(0);
		}
	}

	public int getID() {
		return 2;
	}

}
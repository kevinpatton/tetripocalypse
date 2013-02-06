package javagame;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class Game extends StateBasedGame {

	public static final String gameName = "Tetripocalypse";
	public static final int xDim = 1024;
	public static final int yDim = 764;
	//game states
	public static final int menu = 0;
	public static final int play = 1;
	public static final int gameOver = 2;
	public static final int credits = 3;
	
	public Game(String gameName) {
		super(gameName);
		Menu m = new Menu(menu);
		this.addState(m);
		Play p = new Play(play);
		p.setMenu(m);
		this.addState(p);
		this.addState(new GameOver(gameOver, p));
		this.addState(new Credits(credits));
	}
	
	public void initStatesList(GameContainer gc) throws SlickException {
		this.getState(menu).init(gc, this);
		this.getState(play).init(gc, this);
		this.getState(gameOver).init(gc, this);
		this.getState(credits).init(gc, this);
		this.enterState(menu);
	}
	
	public static void main(String[] args) {
		
		AppGameContainer gameWindow;
		try {
			gameWindow = new AppGameContainer(new Game(gameName));
			gameWindow.setDisplayMode(xDim, yDim, false);
			gameWindow.start();
		} catch(SlickException e) {
			e.printStackTrace();
		}
	}
}
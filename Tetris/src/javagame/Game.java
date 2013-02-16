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
	public static final int userInput = 4;
	public static final int gameModeSelect = 5;
	public static final int options = 6;
	
	public Game(String gameName) {
		super(gameName);
		Menu m = new Menu(menu);
		this.addState(m);
		Play p = new Play(play);
		p.setMenu(m);
		m.setPlay(p);
		GameModeSelect g = new GameModeSelect(gameModeSelect);
		g.setPlay(p);
		g.setMenu(m);
		Options o = new Options(options);
		//o.setPlay(p); TODO uncomment
		this.addState(p);
		this.addState(new GameOver(gameOver, p));
		this.addState(new Credits(credits));
		this.addState(o);
		UserInput u = new UserInput(userInput);
		u.setPlay(p);
		p.setUserInput(u);
		this.addState(u);
		this.addState(g);
	}
	
	public void initStatesList(GameContainer gc) throws SlickException {
		this.getState(menu).init(gc, this);
		this.getState(play).init(gc, this);
		this.getState(gameOver).init(gc, this);
		this.getState(credits).init(gc, this);
		this.getState(userInput).init(gc, this);
		this.getState(gameModeSelect).init(gc, this);
		this.enterState(menu);
	}
	
	public static void main(String[] args) {
		boolean showFPS = true;
		if (args.length == 1)
			showFPS = Boolean.parseBoolean(args[0]);
		AppGameContainer gameWindow;
		try {
			gameWindow = new AppGameContainer(new Game(gameName));
			gameWindow.setDisplayMode(xDim, yDim, false);
			gameWindow.setShowFPS(showFPS);
			gameWindow.start();
		} catch(SlickException e) {
			e.printStackTrace();
		}
	}
}

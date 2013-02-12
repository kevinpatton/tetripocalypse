package javagame;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.FontUtils;

public class GameModeSelect extends BasicGameState {
	
	private MenuStrip gameModeMenu;
	private Play play;
	private UnicodeFont font;
	private String[] splashText;
	private Image clearPreview;
	
	public GameModeSelect(int state) {
		
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		font = Utility.getFont(30);
		int y = 0;
		Interactable standard = new MenuItemString("Standard", 200, y);
		Interactable clear = new MenuItemString("Clear", 200, y+=30);
		gameModeMenu = new MenuStrip(standard, clear);
		gameModeMenu.setLocation(80, 120);
		splashText = new String[2];
		splashText[0] = "Standard Tetris with some twists.";
		splashText[1] = "Start with random blocks inhabiting the board\n"
				+ "and clear your way to the bottom!";
		clearPreview = new Image("res/clearpreview.png");
	}

	@Override
	//TODO: preview picture!
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) 
			throws SlickException {
		if (gameModeMenu.getSelection() == 1)
			g.drawImage(clearPreview, 570, 100);
		FontUtils.drawCenter(font, "Select the game mode", 0, 10, 1024, Color.orange);
		gameModeMenu.render(g);
		g.drawString(splashText[gameModeMenu.getSelection()], 100, 400);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		Input input = gc.getInput();
		int selection = -1;
		selection = gameModeMenu.acceptInput(input);
		if (selection != -1) {
			play.setGameMode(selection);
			input.clearKeyPressedRecord();
			sbg.enterState(1);
		}
	}
	
	public void setPlay(Play p) {
		play = p;
	}

	@Override
	public int getID() {
		return 5;
	}
}
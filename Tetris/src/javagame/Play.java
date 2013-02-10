package javagame;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;
import java.util.ArrayList;
import java.util.Collections;
import java.io.*;

/**
 * @author Kevin
 * The bulk of the game resides here. Contains the render and update
 * methods for the entire game. NOTE: I use the terms tetrad and
 * tetromino interchangeably. They both just mean tetris pieces,
 * really.
 */
public class Play extends BasicGameState {
		
	/**
	 * This rate determines the decrease in time between
	 * when Tetrads fall. It is multiplied by this time
	 * each time the level advances by one.
	 */
	public static final double timerDecayRate = 0.86; //0.84
	public static final int playAreaOffsetX = 200;
	public static final int playAreaOffsetY = 20;
	public static final int blockSize = 32;
	public static final int boardWidth = 12;
	public static final int boardHeight = 22;
	public static final long tetradMoveDelay = 165;
	public static final int HD_SCORE_MODIFIER = 2;
	public static final int softDropDelay = 60;
	public static final int ghostFrameDelay = 50;
	
	/** The delay, in milliseconds, between which tetrads
		fall. */
	private long blockFallDelay = 800;
	private long tempBlockFallDelay = 0;
	private int timeSinceFall = 0;
	private int timeSinceMove = 0;
	private int lineTargetForNextLevel = 10;
	private int level = 1;
	private int lines = 0;
	private int score = 0;
	
	private SevenBag sevenBag;
	private Image[] tetradImages;
	private Tetrad tetradInPlay;
	private Animation ghost;
	
	private Tetrad next;
	private int[][] board;
	boolean gameOver = false;
	boolean inGame = false;
	boolean tetrisAchieved = false;
	
	private Music theme;
	private Sound thud;
	private Sound levelup;
	private Sound tetris;
	private Sound rotate;
	
	//input stuff
	private boolean leftBeingHeldDown = false;
	private boolean rightBeingHeldDown = false;
	private boolean softBeingHeldDown = false;
	
	private boolean ghostingOn = false;
	
	public HighScoreList highScoreList;
	
	private Menu menu;
	
	public Play(int state) {
	}
	
	// This method is called once when the game first loads.
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		// initialize the RNG
		sevenBag = new SevenBag();
		
		// load images
		tetradImages = new Image[8];
		for (int i = 1; i < 9; i++) {
			Image image = new Image("res/" + i + ".png");
			tetradImages[i - 1] = image;
		}
		Image[] ghostImages = new Image[5];
		for (int i = 0; i < 5; i++)
			ghostImages[i] = new Image("res/ghost" + (i + 1) + ".png" );
		ghost = new Animation(ghostImages, ghostFrameDelay);
		// board has a border around the play area for collision
		// detection purposes. The actual play area is only 10x20.
		// Here we set up these boundaries in the board array.
		board = new int[boardWidth][boardHeight];
		for (int i = 0; i < boardWidth; i++) {
			board[i][boardHeight - 1] = 8;
			board[i][0] = 8;
		}
		for (int i = 0; i < boardHeight; i++) {
			board[0][i] = 8;
			board[boardWidth - 1][i] = 8;
		}
		
		// initialize tetromino pieces
		int type = sevenBag.nextInt();
		tetradInPlay = new Tetrad(type, tetradImages[type - 1], board);
		type = sevenBag.nextInt();
		next = new Tetrad(type, tetradImages[type - 1], board);
		next.setNext();
		
		// load sounds
		theme = new Music("res/theme.ogg");
		thud = new Sound("res/thud.wav");
		levelup = new Sound("res/levelup.wav");
		tetris = new Sound("res/tetris.wav");
		rotate = new Sound("res/cursor.wav");
		
		// load high scores, or create default ones
		File highScores = new File("data/highscores.ser");
		if (highScores.exists())
			highScoreList = loadHighScoreList();
		else
			highScoreList = new HighScoreList();
	}
	
	public void enter(GameContainer container, StateBasedGame game) {
		if (!inGame) {
			theme.loop();
			theme.setVolume(0.50f);
		}
		else
			theme.resume();
		inGame = true;
	}
	
	//graphics
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.drawString("Level: " + level, 900, 550);
		g.drawString("Score: " + score, 900, 300);
		g.drawString("Lines: " + lines, 900, 400);
		g.drawString("Next:", 725, 100);
		drawBoard(g);
		drawTetrad(tetradInPlay, g);
		drawNextBox(18, 0, g);
		drawTetrad(next, g);
		if (ghostingOn)
			drawGhostTetrad(g);
	}
	
	/**
	 * 'Sticks' a Tetrad piece to the game board. Tetrads are free-floating
	 * and not recorded onto the board until they are stuck to it by this
	 * method.
	 * @param t the Tetrad to stick to the game board.
	 */
	private void stick(Tetrad t) {
		int[] location = t.getLocation();
		int type = t.getType();
		for (int i = 0; i < 8; i += 2) {
			board[location[i]][location[i+1]] = type;
		}
	}
	
	//game logic
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		timeSinceFall += delta;
		timeSinceMove += delta;
		
		Input input = gc.getInput();
		
		if (timeSinceFall >= blockFallDelay) {
			if(!tetradInPlay.fall()) {
				stick(tetradInPlay);
				thud.play();
				int type = sevenBag.nextInt();
				tetradInPlay = next;
				tetradInPlay.setReal();
				
				// *******************
				// |  YOU LOSE!!!!!  |
				// *******************
				
				next = new Tetrad(type, tetradImages[type - 1], board);
				next.setNext();
				
				checkForGameOver(input, sbg);
			}
			timeSinceFall = 0;
			if (softBeingHeldDown) 
				score += 1;
		}
		
		checkForCompletedRows();
		
		if (!input.isKeyDown(Input.KEY_LEFT))
			leftBeingHeldDown = false;
		if (!input.isKeyDown(Input.KEY_RIGHT))
			rightBeingHeldDown = false;
		
		if (input.isKeyDown(Input.KEY_LEFT) && !leftBeingHeldDown) {
			tetradInPlay.moveLeft();
			timeSinceMove = 0;
			leftBeingHeldDown = true;
		}
		if (input.isKeyDown(Input.KEY_RIGHT) && !rightBeingHeldDown) {
			tetradInPlay.moveRight();
			timeSinceMove = 0;
			rightBeingHeldDown = true;
		}
		else if (timeSinceMove >= tetradMoveDelay) {
			
			if (input.isKeyDown(Input.KEY_LEFT)) {
				tetradInPlay.moveLeft();
				leftBeingHeldDown = true;
			}
			if (input.isKeyDown(Input.KEY_RIGHT)) {			
				tetradInPlay.moveRight();
				rightBeingHeldDown = true;
			}
			timeSinceMove = 0;
		}
		
		if (!input.isKeyDown(Input.KEY_D) && softBeingHeldDown) {
			softBeingHeldDown = false;
			blockFallDelay = tempBlockFallDelay;
			tempBlockFallDelay = 0;
		}
		
		if (input.isKeyDown(Input.KEY_D) && !softBeingHeldDown) {
			tempBlockFallDelay = blockFallDelay;
			blockFallDelay = softDropDelay;
			softBeingHeldDown = true;
		}
		
		if (input.isKeyPressed(Input.KEY_F))
			ghostingOn = !ghostingOn;
		
		if (input.isKeyPressed(Input.KEY_S))
			score += 1000; //TODO: take this out of the final release
		
		if (input.isKeyPressed(Input.KEY_UP)) {
			tetradInPlay.rotate();
			rotate.play();
		}
		if (input.isKeyPressed(Input.KEY_DOWN)) { //hard drop
			
			int rowsDropped = 0;
			//cause hard drop and tally number of rows dropped
			//so we can use it to calculate a score
			while (tetradInPlay.fall()) {
				rowsDropped++;
			}
			score += HD_SCORE_MODIFIER * rowsDropped;
			
			//continue
			stick(tetradInPlay);
			thud.play();
			int type = sevenBag.nextInt();
			tetradInPlay = next;
			tetradInPlay.setReal();
			next = new Tetrad(type, tetradImages[type - 1], board);
			next.setNext();
			checkForGameOver(input, sbg);
			
		}
		if (input.isKeyPressed(Input.KEY_SPACE)) {
			int type = sevenBag.nextInt();
			tetradInPlay = next;
			tetradInPlay.setReal();
			next = new Tetrad(type, tetradImages[type - 1], board);
			next.setNext();
		}
		if (input.isKeyPressed(Input.KEY_ESCAPE)) {
			theme.pause();
			input.clearKeyPressedRecord();
			sbg.enterState(0);
		}
	}
	
	private void checkForGameOver(Input i, StateBasedGame s) {
		if (tetradInPlay.gameOver()) {
			gameOver = true;
			boolean madeIt = highScoreList.add(new Score("Player", score));
			saveHighScores();
			theme.stop();
			inGame = false;
			menu.menu.gameDone();
			i.clearKeyPressedRecord();
			if (madeIt)
				s.enterState(4); //Input state. enter name for high score
			else
				s.enterState(2); //game over state
		}
	}
	
	/**
	 * Renders a Tetrad object to the screen.
	 * @param t the tetrad to render 
	 * @param g the render object
	 */
	private void drawTetrad(Tetrad t, Graphics g) {
		int i = 0;
		int[] location = t.getLocation();
		while (i <= 7) {
			drawBlock(location[i], location[i+1], t.getImage(), g);
			i += 2;
		}
	}
	
	//TODO: finish this function.
	/**
	 * Renders a 'ghost' Tetrad signifying the location that the
	 * tetrad in play would end up should the player perform a hard
	 * drop.
	 * @param g the render object
	 */
	private void drawGhostTetrad(Graphics g) {
		//we have a ghost tetrad object..
		int[] locationToDrawGhostTetrad = new int[8];
		int[] playTetradLocation = tetradInPlay.getLocation();
		for (int i = 0; i < 8; i++)
			locationToDrawGhostTetrad[i] = playTetradLocation[i];
		
		while (!detectCollision(locationToDrawGhostTetrad)) {
			locationToDrawGhostTetrad = drop(locationToDrawGhostTetrad);
		}
		locationToDrawGhostTetrad = raise(locationToDrawGhostTetrad);
		//render
		for (int i = 0; i < 8; i += 2)
			drawGhostBlock(locationToDrawGhostTetrad[i], locationToDrawGhostTetrad[i + 1], g);
	}
	
	private int[] drop(int[] location) {
		for (int i = 1; i < 8; i += 2)
			location[i] += 1;
		return location;
	}
	
	private int[] raise(int[] location) {
		for (int i = 1; i < 8; i += 2)
			location[i] -= 1;
		return location;
	}
	
	private boolean detectCollision(int[] location) {
		for (int i = 0; i < 8; i += 2)
			if (board[location[i]][location[i + 1]] != 0)
				return true;
		return false;
	}
	
	/**
	 * Loads the game's high score list from a HighScoreList serialized
	 * object file and returns it. This method is only called if the
	 * file already exists. Otherwise, a new HighScoreList is created
	 * using that class' default constructor.
	 * @return the HighScoreList
	 */
	private HighScoreList loadHighScoreList() {
		HighScoreList h = null;
		try {
			FileInputStream fileIn = new FileInputStream("data/highscores.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			h = (HighScoreList) in.readObject();
			in.close();
			fileIn.close();
		} catch (IOException i) {
			i.printStackTrace();
		} catch (ClassNotFoundException c) {
			c.printStackTrace();
		}
		return h;
	}
	
	private void drawBoard(Graphics g) {
		for (int i = 0; i < 22; i++) {
			for (int j = 0; j < 12; j++) {
				if (board[j][i] != 0) {
					g.drawImage(tetradImages[board[j][i]-1], j*blockSize+playAreaOffsetX, i*blockSize+playAreaOffsetY);
				}
			}
		}
	}
	
	public int getScore() {
		return score;
	}
	
	private void advanceLevelIfNecessary() {
		if (lines >= lineTargetForNextLevel) {
			lineTargetForNextLevel += 10;
			level++;
			blockFallDelay *= timerDecayRate;
			tempBlockFallDelay *= timerDecayRate;
			if (tetrisAchieved) {
				tetrisAchieved = false;
			}
			else
				levelup.play();
		}
	}
	
	public void setMenu(Menu menu) {
		this.menu = menu;
	}
	
	//copies row r-1 to row r.
	private void setToRowAbove(int r) {
		if (r > 1) {
			for (int i = 1; i < 11; i++) {
				board[i][r] = board[i][r - 1];
			}
			removeRow(r-1);
		}
	}
	
	//make floating lines fall after lines are cleared
	private void boardFall(ArrayList<Integer> rowsKilled, int rowsToDrop) {
		Collections.sort(rowsKilled);
		for (int i: rowsKilled) {
			int r = i;
			while (r > 1) {
				setToRowAbove(r);
				r--;
			}
		}
		//advanceLevelIfNecessary();
		
		//scoring
		
		switch(rowsKilled.size()) {
		case 1:
			score += 100 * level;
			break;
		case 2:
			score += 300 * level;
			break;
		case 3:
			score += 500 * level;
			break;
		case 4:
			score += 800 * level;
			tetrisAchieved = true;
			tetris.play();
			break;
		}
		advanceLevelIfNecessary();
	}
	
	//determines whether the player has completed a row.
	private boolean rowFull(int[] row) {
		for (int i = 1; i < 11; i++) {
			if (row[i] == 0)
				return false;
		}
		return true;
	}
	
	private void removeRow(int n) {
		for (int i = 1; i < 11; i++)
			board[i][n] = 0;
	}
	
	public void resetBoard() {
		for (int i = 1; i < 21; i++)
			removeRow(i);
	}
	
	// Resets the game so it appears as a new one. Called when
	// the player loses.
	public void resetGame() {
		resetBoard();
		score = 0;
		lines = 0;
		level = 1;
		lineTargetForNextLevel = 10;
		blockFallDelay = 800;
	}
	
	private void saveHighScores() {
		try {
			FileOutputStream fileOut = new FileOutputStream("data/highscores.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(highScoreList);
			out.close();
			fileOut.close();
		} catch(IOException i) {
			i.printStackTrace();
		}
	}
	
	/**
	 * Returns
	 * @param n the row of the gameBoard to get
	 * @return the nth row of gameBoard
	 */
	private int[] getRow(int n) {
		int[] row = new int[12];
		for (int i = 0; i < 12; i++)
			row[i] = board[i][n];
		return row;
	}
	
	/**
	 * Checks the game board to see if there are any full rows. (In
	 * other words, checks to see if the player has cleared a line.)
	 * @return whether or not there are any completed rows
	 */
	private boolean checkForCompletedRows() {
		ArrayList<Integer> completedRows = new ArrayList<Integer>();
		for (int i = 1; i < 21; i++) {
			int[] row = getRow(i);
			if (rowFull(row))
				completedRows.add(i);
		}
		
		//completedRows now contains the completed rows
		if (!completedRows.isEmpty()) {
			int rowsToDrop = 0;
			for (int i: completedRows) {
				removeRow(i);
				rowsToDrop++;
				lines++;
			}
			boardFall(completedRows, rowsToDrop);			
		}
		return !completedRows.isEmpty();
	}
	
	/**
	 * Renders a box around the part of the UI that shows
	 * which Tetrad is going to be dropped next.
	 * @param x the x location in pixels to render the box
	 * @param y the y location in pixels to render the box
	 * @param g the render object
	 */
	private void drawNextBox(int x, int y, Graphics g) {
		int width = 5;
		int height = 6;
		Image block = tetradImages[7];
		//draw horizontal blocks
		for (int i = x; i < x+width; i++) {
			g.drawImage(block, playAreaOffsetX+blockSize*i, playAreaOffsetY+blockSize*y);
			g.drawImage(block, playAreaOffsetX+blockSize*i, playAreaOffsetY+blockSize*(y+5));
		}
		//draw vertical blocks
		for (int i=y; i<y+height; i++) {
			g.drawImage(block, playAreaOffsetX+blockSize*x, playAreaOffsetY+blockSize*i);
			g.drawImage(block, playAreaOffsetX+blockSize*(x+4), playAreaOffsetY+blockSize*i);
		}
	}
	
	/**
	 * Renders Image block to the screen at x, y. Must be called from the
	 * render method.
	 * @param x the x location of the image to be rendered
	 * @param y the y location of the image to be rendered
	 * @param block the image to be rendered
	 * @param g the render object
	 */
	private void drawBlock(int x, int y, Image block, Graphics g) {
		g.drawImage(block, playAreaOffsetX + blockSize*x, playAreaOffsetY+blockSize*y);
	}
	
	private void drawGhostBlock(int x, int y, Graphics g) {
		ghost.draw(playAreaOffsetX + blockSize*x, playAreaOffsetY+blockSize*y);
	}
	
	public int getID() {
		return 1;
	}
}
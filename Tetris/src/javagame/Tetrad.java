package javagame;

import org.newdawn.slick.*;

public class Tetrad {

	//1 = i, 2 = square, 3 = z, 4 = s, 5 = t, 6 = L, 7 = J
	private int type;
	private int rotationStatus = 1;
	private Image image;
	private int[] location;
	private int[][] board;
	
	public Tetrad(int type, Image image, int[][] board) {
		this.type = type;
		this.image = image;
		this.board = board;
		
		//set initial locations of the tetrad blocks
		location = determineInitialLocations();
	}
	
	//determines whether the game has been lost.
	public boolean gameOver() {
		for (int i = 0; i < 8; i+=2) {
			int x = location[i];
			int y = location[i+1];
			if (board[x][y] != 0)
				return true;
		}
		return false;
	}
	
	/*private boolean collision(int[] tentativeLocation) {
		for (int i = 0; i < 8; i += 2) {
			if (board[tentativeLocation[i]][tentativeLocation[i+1]] != 0) {
				//collision!
				return false;
			}
		}
		return true;
	}*/

	//sets this tetrad to be in play
	public void setReal() {
		for (int i = 0; i < 8; i += 2)
			location[i] = location[i] - 15;
	}
	
	//sets this tetrad to be the 'next' one about to be in play
	public void setNext() {
		for (int i = 0; i < 8; i += 2)
			location[i] = location[i] + 15;
	}
	
	public Image getImage() {
		return image;
	}
	
	public int getType() {
		return type;
	}
	
	public int[] getLocation() {
		return location;
	}
	
	public boolean fall() {
		//we only increment y values which is why
		//i starts at 1 and increments by 2
		boolean collided = false;
		for (int i = 1; i < 9; i += 2) {
			int test = location[i]+1;
			if (board[location[i-1]][test] != 0) {
				//collision!
				collided = true;
				break;
			}
		}
		if (!collided)
			for (int i = 1; i < 9; i += 2)
				location[i] = location[i] + 1;
		return !collided;
	}
	
	public void moveLeft() {
		
		boolean collision = false;
		for (int i = 0; i < 8; i+=2) {
			int x = location[i];
			int y = location[i+1];
			if(board[x-1][y] != 0)
				collision = true;
		}
		if (!collision) {
		
			for (int i = 0; i < 8; i+= 2) {
				location[i] = location[i] - 1;
			}
		}
		
	}
	
	public void moveRight() {
		boolean collision = false;
		for (int i = 0; i < 8; i+=2) {
			int x = location[i];
			int y = location[i+1];
			if(board[x+1][y] != 0)
				collision = true;
		}
		if (!collision) {
		
			for (int i = 0; i < 8; i+= 2) {
				location[i] = location[i] + 1;
			}
		}
	}
	
	private void shift(int[] location) {
		boolean collision = false;
		
		for (int i = 0; i < 8; i+=2) {
			int x = this.location[i] + location[i];
			int y = this.location[i+1] + location[i+1];
			if (x > -1 && x < 12 && y > -1 && y < 22) {
				if(board[x][y] != 0)
					collision = true;
			}
			//else
				//collision = true;
		}
		
		if (!collision) {
			for (int i = 0; i < 8; i++)
				this.location[i] = this.location[i] + location[i];
			if (rotationStatus < 4)
				rotationStatus++;
			else
				rotationStatus = 1;
		}
	}
	
	public void rotate() {
		switch(type) {
		//let's start with an easy one...
		case 2: break;
		case 1:
			switch(rotationStatus) {
			case 1:
				//rotationStatus++;
				shift(new int[] {-2,+2,-1,+1,0,0,1,-1});
				break;
			case 2:
				//rotationStatus++;
				shift(new int[] {1,-2,0,-1,-1,0,-2,1});				
				break;
			case 3:
				//rotationStatus++;
				shift(new int[] {-1,1,0,0,1,-1,2,-2});
				break;
			case 4:
				//rotationStatus = 1;
				shift(new int[] {2,-1,1,0,0,1,-1,2});
				break;
			}
		break;
		case 3:
			switch(rotationStatus) {
			case 1:
				//rotationStatus++;
				shift(new int[] {2,0,1,1,0,0,-1,1});
				break;
			case 2:
				//rotationStatus++;
				shift(new int[] {0,2,-1,1,0,0,-1,-1});
				break;
			case 3:
				//rotationStatus++;
				shift(new int[] {-2,0,-1,-1,0,0,1,-1});
				break;
			case 4:
				//rotationStatus = 1;
				shift(new int[] {0,-2,1,-1,0,0,1,1});
				break;
			}
		break;
		case 4://s
			switch(rotationStatus) {
			case 1:
				//rotationStatus++;
				shift(new int[] {0,2,1,1,0,0,1,-1});
				break;
			case 2:
				//rotationStatus++;
				shift(new int[] {-2,0,-1,1,0,0,1,1});
				break;
			case 3:
				//rotationStatus++;
				shift(new int[] {0,-2,-1,-1,0,0,-1,1});
				break;
			case 4:
				//rotationStatus = 1;
				shift(new int[] {2,0,1,-1,0,0,-1,-1});
				break;
			}
		break;
		case 5:
			switch(rotationStatus) {
			case 1:
				//rotationStatus++;
				shift(new int[] {1,1,0,0,1,-1,-1,1});
				break;
			case 2:
				//rotationStatus++;
				shift(new int[] {-1,1,0,0,1,1,-1,-1});
				break;
			case 3:
				//rotationStatus++;
				shift(new int[] {-1,-1,0,0,-1,1,1,-1});
				break;
			case 4:
				//rotationStatus = 1;
				shift(new int[] {1,-1,0,0,-1,-1,1,1});
				break;
			}
		break;
		case 6:
			switch(rotationStatus) {
			case 1:
				//rotationStatus++;
				shift(new int[] {1,1,0,0,-1,-1,-2,0});
				break;
			case 2:
				//rotationStatus++;
				shift(new int[] {-1,1,0,0,1,-1,0,-2});
				break;
			case 3:
				//rotationStatus++;
				shift(new int[] {-1,-1,0,0,1,1,2,0});				
				break;
			case 4:
				//rotationStatus = 1;
				shift(new int[] {1,-1,0,0,-1,1,0,2});
				break;
			}
		break;
		case 7:
			switch(rotationStatus) {
			case 1:
				//rotationStatus++;
				shift(new int[] {1,1,0,0,-1,-1,0,-2});
				break;
			case 2:
				//rotationStatus++;
				shift(new int[] {-1,1,0,0,1,-1,2,0});
				break;
			case 3:
				//rotationStatus++;
				shift(new int[] {-1,-1,0,0,1,1,0,2});
				break;
			case 4:
				//rotationStatus = 1;
				shift(new int[] {1,-1,0,0,-1,1,-2,0});
				break;
			}
		break;
		}
	}
	
	private int[] determineInitialLocations() {
		//tetrads always have 4 blocks, so we create an
		//array that can hold 4 x and y coordinates
		switch(type) {
		case 1: return new int[] {5, 1, 5, 2, 5, 3, 5, 4}; //I
		case 2: return new int[] {5, 1, 6, 1, 5, 2, 6, 2}; //square
		case 3: return new int[] {4, 1, 5, 1, 5, 2, 6, 2}; //Z
		case 4: return new int[] {6, 1, 5, 1, 5, 2, 4, 2}; //S
		case 5: return new int[] {5, 1, 5, 2, 4, 2, 6, 2}; //T
		case 6: return new int[] {5, 1, 5, 2, 5, 3, 6, 3}; //L
		case 7: return new int[] {5, 1, 5, 2, 5, 3, 4, 3}; //J
		default: return new int[] {0, 0, 0, 0, 0, 0, 0, 0};
		}
	}
}

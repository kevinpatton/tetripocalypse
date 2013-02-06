package javagame;

/**
 * @author Kevin
 * A type that is allowed to be a menu element. Planning to
 * implement both text and image.
 */
public interface Interactable {
	
	/** @return the height in pixels of this menu element */
	public int getHeight();
	
	/** @return this Interactable in a String representation */
	public String toString();
	
	public void render(int x, int y);
	
	public void changeItem(String newItem);
	
}

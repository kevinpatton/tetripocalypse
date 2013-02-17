package javagame;

import org.newdawn.slick.*;
import java.util.Random;

public class BasicParticle {
	
	public static final int MAX_LIFESPAN = 5000;
	public static final int MAX_FLATTEN_COEFFICIENT = 25;
	
	Image particleImage;
	final static Random random = new Random();
	
	int lifespan;
	int elapsedTime = 0;
	float x;
	float step = 0.1f;
	float flattenCoefficient;
	float startingX;
	float startingY;
	boolean goingRight;
	
	public BasicParticle(Image image, float startingX, float startingY) {
		particleImage = image;
		lifespan = random.nextInt(MAX_LIFESPAN);
		flattenCoefficient = 1.0f;
		x = 0.0f;
		this.startingX = startingX;
		this.startingY = startingY;
		goingRight = random.nextDouble() < 0.5d;
	}
	
	public void render(Graphics g) {
		g.drawImage(particleImage, startingX+x, startingY+getPathY(x));
	}
	
	//@return is this particle dead?
	public boolean update(int delta) {
		elapsedTime += delta;
		if (goingRight)
			x += step;
		else
			x -= step;
		if (elapsedTime >= lifespan)
			return true;
		return false;
	}
	
	private float getPathY(float x) {
		return flattenCoefficient * (float)Math.pow(x, 2); 
	}
}

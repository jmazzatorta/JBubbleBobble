package view.visualFx;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public abstract class VisualFx {
	
	protected int arrayPosition;
	
	protected int x;
	protected int y;
	
	protected boolean alive = true;
	
	public int frameCounter;
	
	protected BufferedImage currentSprite;

	public VisualFx(int arrayPosition, int x, int y) {
		this.arrayPosition=arrayPosition;
		this.x=x;
		this.y=y;
	}
	
	public abstract void update(); 
	
	public void draw(Graphics2D g2) {
		g2.drawImage(currentSprite, x, y, null);
	};
	
	public int getArrayPosition() {
		return arrayPosition;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public boolean isAlive() {
		return alive;
	}

}

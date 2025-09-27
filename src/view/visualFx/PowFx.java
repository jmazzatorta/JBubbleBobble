package view.visualFx;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class PowFx extends VisualFx{
	
	ArrayList<BufferedImage> images = new ArrayList<>();
	
	public PowFx(int arrayPosition, int x, int y, ArrayList<BufferedImage> images) {
		super(arrayPosition, x, y);
		this.images= images;
	}
	
	public void update() {
		
		frameCounter++;
		
		if (frameCounter>40) alive = false;
		
		if (frameCounter%20 <= 10) currentSprite= images.get(0);
		else currentSprite= images.get(1);
	}

}

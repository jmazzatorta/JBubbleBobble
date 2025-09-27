package view.utils;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class ImageFlipper {
	
	public static BufferedImage createFlipped(BufferedImage image) {
		AffineTransform at= new AffineTransform(); 
		at.concatenate(AffineTransform.getScaleInstance(-1, 1));
		at.concatenate(AffineTransform.getTranslateInstance(-image.getWidth(), 0));
		return createTransformed(image, at);
	}

	private static BufferedImage createTransformed(BufferedImage image, AffineTransform at) {
		BufferedImage newImage= new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2= newImage.createGraphics();
		g2.transform(at);
		g2.drawImage(image, 0, 0, null);
		g2.dispose();
		return newImage;
	}

}

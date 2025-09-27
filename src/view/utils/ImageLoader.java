package view.utils;

import static constants.Constants.TILESIZE;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageLoader {
	private static String path;
	
	private static int scalingX=2;
	private static int scalingY=2;
	
	public static void setScaling(int amt) {
		scalingX=amt;
		scalingY=amt;
	}
	
	public static void setScalingX(int amt) {
		scalingX=amt;
	}
	
	public static void setScalingY(int amt) {
		scalingY=amt;
	}
	
	public static void setPath(String classPath) {
		path= classPath;
	}
	
	public static BufferedImage load(String imageName) {
		BufferedImage img= null;
	
		try {
			img= ImageIO.read(ImageLoader.class.getResourceAsStream(path + imageName + ".png"));
			img= scaleImage(img, TILESIZE*scalingX, TILESIZE*scalingY);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}
	
	
	private static BufferedImage scaleImage(BufferedImage original, int width, int height) {
		BufferedImage scaledImage = new BufferedImage(width, height, original.getType());
		Graphics2D g2= scaledImage.createGraphics();
		g2.drawImage(original, 0, 0, width, height, null);
		g2.dispose();
		
		return scaledImage;
	}
}

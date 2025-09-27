package view.utils;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

public class FontTool {
	
	public static Font loadFont(String fontName, float size) {
		Font font= null;
		InputStream is = FontTool.class.getResourceAsStream("/font/" + fontName);

		try {
			font = Font.createFont(Font.PLAIN, is).deriveFont(size);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
		return font;
	}

}

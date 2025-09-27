package view.objectsview;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import view.interfaces.DrawableMap;
import view.utils.ImageLoader;
import static constants.Constants.*;

public class TilesView {

    private final ArrayList<BufferedImage> tilesImages;
    private final ArrayList<BufferedImage> borderImages;
    
    private final Color[] sideColors;
    
    private final int[] polyXPoints = new int[5];
    private final int[] polyYPoints = new int[5];


    private final DrawableMap mapProvider;
    private final int[][] map;
    
    private static final int SHADOW_OFFSET = TILESIZE/2;

    public TilesView(DrawableMap mapProvider) {
        this.mapProvider = mapProvider; 
        this.map = mapProvider.getMap();
        this.tilesImages = new ArrayList<>();
        this.borderImages = new ArrayList<>();
        this.sideColors = new Color[9];
        loadImages();
        loadSideColors();
    }
    
    private void loadSideColors() {
        sideColors[0] = Color.BLACK; 
        sideColors[1] = new Color(205, 123, 205);
        sideColors[2] = new Color(99, 0, 0);
        sideColors[3] = sideColors[2]; 
        sideColors[4] = new Color(60, 60, 215);
        sideColors[5] = new Color(115, 99, 0);
        sideColors[6] = sideColors[2];
        sideColors[7] = new Color(99, 99, 173);
        sideColors[8] = new Color(130, 130, 130);
    }

    public void draw(Graphics2D g2) {
        if (map == null) return;
        
        int x,y,currentYOffset;
        int side;
        BufferedImage sprite;

        for (int row = 0; row < MAXSCREENROW; row++) {
            for (int col = 0; col < MAXSCREENCOL; col++) {
            	
            	if (col == 1 || col == MAXSCREENCOL - 1) continue;
  	
                int type = map[row][col];
                if (type == 0) continue;
                
                if (col == 0 || col == MAXSCREENCOL - 2) {
                    if (row % 2 != mapProvider.getSlidedRows() % 2) continue;
                    sprite = borderImages.get(type - 1);
                    currentYOffset = mapProvider.getOffset();
                } else {
                    sprite = tilesImages.get(type - 1);
                    currentYOffset = mapProvider.getOffset();
                }
                
                x = col * TILESIZE;
                y = row * TILESIZE + currentYOffset;
                side = (col == 0) ? TILESIZE * 2 : TILESIZE;
                
                polyXPoints[0] = x; polyXPoints[1] = x + SHADOW_OFFSET; polyXPoints[2] = x + side + SHADOW_OFFSET; polyXPoints[3] = x + side + SHADOW_OFFSET; polyXPoints[4] = x + side;
                polyYPoints[0] = y + side; polyYPoints[1] = y + side + SHADOW_OFFSET; polyYPoints[2] = y + side + SHADOW_OFFSET; polyYPoints[3] = y + SHADOW_OFFSET; polyYPoints[4] = y;
                
                g2.setColor(getSideColor(type));
                g2.fillPolygon(polyXPoints, polyYPoints, 5);

                g2.drawImage(sprite, x, y, null);
            }
        }
    }

    private void loadImages() {
        ArrayList<String> imageNames = new ArrayList<>();
        ImageLoader.setPath("/tiles/");
        ImageLoader.setScaling(1);
        Collections.addAll(imageNames, "1", "2", "3", "4", "5", "6", "7", "8");
        imageNames.stream().map(ImageLoader::load).forEachOrdered(tilesImages::add);
        imageNames.clear();

        ImageLoader.setPath("/tiles/border");
        ImageLoader.setScaling(2);
        Collections.addAll(imageNames, "1", "2", "3", "4", "5", "6", "7", "8");
        imageNames.stream().map(ImageLoader::load).forEachOrdered(borderImages::add);
        imageNames.clear();
    }

    private Color getSideColor(int type) {
        if (type < 0 || type >= sideColors.length) {
            return sideColors[0]; 
        }
        return sideColors[type];
    }
}
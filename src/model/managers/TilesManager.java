package model.managers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Stack;
import view.interfaces.DrawableMap;
import static constants.Constants.*;


public class TilesManager implements DrawableMap {

    private final int[][] map = new int[MAXSCREENROW][MAXSCREENCOL];
    private final Stack<int[]> nextLevelRows = new Stack<>();
    
    private boolean isSliding = false;
    private int slidedRows = 0;
    private int yOffset = 0;		
    private int offsetMul = 0;
	
	private final int SLIDE_SPEED = TILESIZE / 6;

    public void loadLevel(int levelNumber) {
        String path = "/maps/level" + levelNumber + ".txt";

        nextLevelRows.clear();

        InputStream is = getClass().getResourceAsStream(path);
        if (is == null) {
            System.err.println("File della mappa non trovato: " + path);
            return;
        }        

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
        	
            String line;
            int row = 0;

            while ((line = br.readLine()) != null && row < MAXSCREENROW) {
                String[] numbers = line.trim().split("\\s+"); 
                int[] newRowData = new int[MAXSCREENCOL];

                for (int col = 0; col < MAXSCREENCOL && col < numbers.length; col++) {
                    int tileType = Integer.parseInt(numbers[col]);
                    if (levelNumber == 1) map[row][col] = tileType;
                    else newRowData[col] = tileType;
                }

                if (levelNumber != 1) nextLevelRows.push(newRowData);
                row++;
            }

        } catch (Exception e) {
            System.err.println("Errore durante il caricamento della mappa: " + path);
            e.printStackTrace();
        }
    }
    
    
    public void update() {
        if (!isSliding) return;

        yOffset += SLIDE_SPEED;

        if (yOffset >= TILESIZE) {
        	
        	offsetMul ^= 1;
            yOffset = 0;

            for (int i = MAXSCREENROW - 1; i > 0; i--) {
                map[i] = map[i - 1];
            }
            
            if (nextLevelRows != null && !nextLevelRows.isEmpty()) {
                map[0] = nextLevelRows.pop();
            } else {

                map[0] = new int[MAXSCREENCOL];
            }

            slidedRows++;
        }

        if (slidedRows >= MAXSCREENROW) {
            isSliding = false;
            slidedRows = 0;
            yOffset = 0;
        }
    }
    
    public boolean isSlidingDone() {
    	return !isSliding;
    }
    
    public void slideToNextLevel() {
    	isSliding = true;
    }

    @Override
    public int[][] getMap() {
        return map;
    }
    
    @Override
    public int getOffset() {
    	return yOffset;
    }
    
    @Override
    public int getOffsetX2() {
    	return yOffset * (1 + offsetMul);
    }
    
    @Override
    public int getSlidedRows() {
    	return slidedRows;
    }

}

package model.entities.enemies;

import static constants.Constants.*;
import java.awt.Rectangle;

import model.managers.AudioManager;
import model.managers.ItemsManager;
import model.managers.TilesManager;
import model.utils.DirectionX;
import model.utils.DirectionY;

public class Monsta extends Enemy {

    private boolean collisionY;
    private DirectionY yDirection = DirectionY.UP;

    public Monsta(int x, int y, TilesManager tilesManager, AudioManager audioManager, ItemsManager itemsManager) {
        super(x, y, tilesManager, audioManager, itemsManager);
        this.hitbox = new Rectangle(5, 10, TILESIZE * 2 - 10, TILESIZE * 2 - 11);
        setDefaultValues();
    }

    @Override
    protected void setDefaultValues() {
    	super.setDefaultValues();
        speed = 2;
        scoreValue = 200;
    }

    @Override
    protected void handleAI() {
        // Monsta non ha IA
    }
    
    @Override
    protected void playingUpdate() {
		collisionX= false;
		collisionY= false;
		
		//Controllo collisioni
		updateCoordinates();
		checkTilesCollisions();
		checkPlayerCollisions();
		
		if (collisionX) changeXDirection();
		if (collisionY) changeYDirection();
		
			
		switch (yDirection) {
		case UP : y-= speed; break;
		case DOWN : y+= speed; break;
		}
		
		switch (xDirection) {
		case LEFT : x-= speed; break;
		case RIGHT : x+= speed; break;
		}
	}

    @Override
    protected void checkTilesCollisions() {
        // Monsta ha una logica di collisione personalizzata per il suo movimento a "rimbalzo".
        int[][] currentMap = tilesManager.getMap();

        // --- Controllo Orizzontale (Muri) ---
        double nextCol;
        if (xDirection == DirectionX.RIGHT) {
            nextCol = (entityRight + speed) / (double) TILESIZE;
        } else { // LEFT
            nextCol = (entityLeft - speed) / (double) TILESIZE;
        }

        if (nextCol < 0 || nextCol >= MAXSCREENCOL) {
            collisionX = true; // Colpito bordo schermo
        } else {
            int topRow = entityTop / TILESIZE;
            int bottomRow = (entityBottom - 1) / TILESIZE;
            if (currentMap[topRow][(int) nextCol] != 0 || 
                currentMap[bottomRow][(int) nextCol] != 0) {
                collisionX = true;
            }
        }

        // --- Controllo Verticale (Pavimento e Soffitto) ---
        double nextRow;
        if (yDirection == DirectionY.DOWN) {
            nextRow = (entityBottom + speed) / (double) TILESIZE;
        } else { // UP
            nextRow = (entityTop - speed) / (double) TILESIZE;
        }

        if (nextRow < 0 || nextRow >= MAXSCREENROW) {
            collisionY = true; // Colpito bordo schermo
        } else {
            int leftCol = entityLeft / TILESIZE;
            int rightCol = (entityRight - 1) / TILESIZE;
            if (currentMap[(int) nextRow][leftCol] != 0 || 
                currentMap[(int) nextRow][rightCol] != 0) {
                collisionY = true;
            }
        }
    }

    protected void changeYDirection() {
        yDirection = (yDirection == DirectionY.UP) ? DirectionY.DOWN : DirectionY.UP;
        collisionY = false;
    }

    @Override
    public String getName() { return "MONSTA"; }
    @Override
    public String getTypeData() { return "MONSTA"; }
    @Override
    public boolean isFlashing() { return false; }
}

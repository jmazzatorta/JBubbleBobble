package model.items;

import static constants.Constants.*;
import java.awt.Rectangle;
import model.GameObject;
import model.entities.players.Player;
import model.interfaces.Collidable;
import model.interfaces.Updatable;
import model.managers.TilesManager;
import view.interfaces.Drawable;

public abstract class Item extends GameObject implements Drawable, Collidable, Updatable {

    protected int x, y;
    protected Rectangle hitbox;
    protected int entityLeft, entityRight, entityBottom;
    protected double entityLeftCol, entityRightCol, entityBottomRow;
    protected boolean floorCollision;
    protected int fallSpeed = 2;
    protected boolean readyToRemove = false;
    
    

    protected final int[][] tileMap;

    public Item(int x, int y, int side, TilesManager tilesManager) {
    	this.x = x;
        this.y = y;
        this.hitbox = new Rectangle(10, 0, side - 20, side);
        this.tileMap = tilesManager.getMap();
    }

    public abstract void collect(Player collector);

    @Override
    public void update() {
        if (floorCollision) {
            return;
        }

        updateCoordinates();
        checkTilesCollisions(); // Questo metodo ora gestisce lo snap e la rimozione.
        
        if (!floorCollision) {
            y += fallSpeed;
        }
    }

    private void updateCoordinates() {
        entityLeft = x + hitbox.x;
        entityRight = x + hitbox.x + hitbox.width;
        entityBottom = y + hitbox.y + hitbox.height;

        entityLeftCol = entityLeft / TILESIZE;
        entityRightCol = entityRight / TILESIZE;
        entityBottomRow = (entityBottom + fallSpeed) / TILESIZE;
    }

    private void checkTilesCollisions() {

        if (entityBottomRow >= MAXSCREENROW) {
            readyToRemove = true; 
            return;
        }

        // Eseguiamo il controllo solo se siamo dentro i limiti della mappa
        if (entityBottomRow >= 0 && entityLeftCol >= 0 && entityRightCol < MAXSCREENCOL) {
            int tile1 = tileMap[(int)entityBottomRow][(int)entityLeftCol];
            int tile2 = tileMap[(int)entityBottomRow][(int)entityRightCol];
            
            if (tile1 != 0 || tile2 != 0) {
                floorCollision = true;
                
                y = (int)(entityBottomRow * TILESIZE) - (hitbox.y + hitbox.height);
            }
        }
    }

    // --- Getters ---
    public Rectangle getHitbox() { return hitbox; }
    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isReadyToRemove() { return readyToRemove; }

    // --- Drawable ---
    
    
    @Override
    public String getDirectionData() { return null; }
    
    @Override
    public String getActionData() { return null; }
    
    @Override
    public boolean isFlashing() { return false; }
    
}

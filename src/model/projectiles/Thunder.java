package model.projectiles;

import java.awt.Rectangle;
import model.utils.DirectionX;
import static constants.Constants.*;

public class Thunder extends Projectile {

    public Thunder(int x, int y, int[][] tileMap) {
        super(x, y, x < SCREENWIDTH / 2 ? DirectionX.RIGHT : DirectionX.LEFT, tileMap);
        this.speed = 6; 
        this.hitbox = new Rectangle(0, 0, TILESIZE / 2, TILESIZE); 
    }

    @Override
    public void update() {
        if (readyToRemove) return;

        collisionX = false;
        updateCoordinates();
        checkTilesCollisions();

        if (collisionX || x < 0 || x > SCREENWIDTH) {
            readyToRemove = true;
            return;
        }

        switch (direction) {
            case LEFT -> x -= speed;
            case RIGHT -> x += speed;
        }
    }

    @Override
    protected void checkTilesCollisions() {
        int rowTop = (int) projectileTopRow;
        int rowBottom = (int) projectileBottomRow;
        int colLeft = (int) projectileLeftCol;
        int colRight = (int) projectileRightCol;

        if (rowTop >= 0 && rowBottom < MAXSCREENROW && colLeft >= 0 && colRight < MAXSCREENCOL) {
            int tileTopLeft = tileMap[rowTop][colLeft];
            int tileTopRight = tileMap[rowTop][colRight];
            int tileBottomLeft = tileMap[rowBottom][colLeft];
            int tileBottomRight = tileMap[rowBottom][colRight];

            if (tileTopLeft != 0 || tileTopRight != 0 || tileBottomLeft != 0 || tileBottomRight != 0) {
                collisionX = true;
            }
        }
    }

    // --- Drawable methods ---

    public String getTypeData() { return "THUNDER"; }
    
    public String getActionData() { return "MOVING"; }
    
    public String getDirectionData() { return null; }
    
    public boolean isFlashing() { return false; }
}

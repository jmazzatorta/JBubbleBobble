package model.projectiles;

import java.awt.Rectangle;
import model.GameObject;
import model.interfaces.Collidable;
import model.interfaces.Updatable;
import model.utils.DirectionX;
import view.interfaces.Drawable;

import static constants.Constants.*;

public abstract class Projectile extends GameObject implements Collidable, Updatable, Drawable {

    protected int x, y;
    protected Rectangle hitbox;
    protected int speed;
    protected DirectionX direction;

    protected int projectileLeft, projectileRight, projectileTop, projectileBottom;
    protected double projectileLeftCol, projectileRightCol, projectileTopRow, projectileBottomRow;

    protected boolean moving = true;
    protected boolean collisionX;
    protected boolean readyToRemove = false;

    protected boolean exploding;
    protected int explodingTC;
    
    protected final int[][] tileMap;

    public Projectile(int x, int y, DirectionX direction, int[][] tileMap) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.tileMap = tileMap;
        this.hitbox = new Rectangle(0, 0, 10, 10); // default, sovrascrivere nelle sottoclassi
    }

    public abstract void update();
       

    protected void updateCoordinates() {
        projectileLeft = x + hitbox.x;
        projectileRight = projectileLeft + hitbox.width;
        projectileTop = y + hitbox.y;
        projectileBottom = projectileTop + hitbox.height;

        projectileLeftCol = projectileLeft / (double) TILESIZE;   
        projectileRightCol = projectileRight / (double) TILESIZE;
        projectileTopRow = projectileTop / (double) TILESIZE;   
        projectileBottomRow = projectileBottom / (double) TILESIZE; 
    }

    protected abstract void checkTilesCollisions();

    public void explode() {
        exploding = true;
        explodingTC = 0;
    }

    public boolean isExploding() {
        return exploding;
    }

    public boolean isReadyToRemove() {
        return readyToRemove;
    }

    @Override
    public int getX() { return x; }

    @Override
    public int getY() { return y; }

    @Override
    public Rectangle getHitbox() {
        return hitbox;
    }
    	
}

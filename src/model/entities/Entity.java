package model.entities;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;
import model.GameObject;
import model.interfaces.Collidable;
import model.managers.AudioManager;
import model.managers.ItemsManager;
import model.managers.TilesManager;
import model.utils.DirectionX;
import view.interfaces.Drawable;
import static constants.Constants.*;


public abstract class Entity extends GameObject implements Drawable, Collidable {

    protected int x, y;
    protected Rectangle hitbox;
    protected Point spawnPoint;
    protected Random random = new Random();

    protected int speed;
    protected DirectionX xDirection = DirectionX.RIGHT;
    protected boolean moving;
    
    protected boolean jumping, falling, collisionFloor;
    protected float jumpSpeed, defaultJumpSpeed, fallSpeed;
    
    protected boolean canDie;
    protected boolean attacking, positioning, dying;
    protected int attackTC;
    protected int positionTC;
    
    protected int entityLeft, entityRight, entityTop, entityBottom;
    protected double entityLeftCol, entityRightCol, entityTopRow, entityBottomRow;
    protected boolean collisionX;
    
    protected final TilesManager tilesManager;
    protected final AudioManager audioManager;
    protected final ItemsManager itemsManager;
    

    public Entity(TilesManager tilesManager, AudioManager audioManager, ItemsManager itemsManager) {
        this.tilesManager = tilesManager;
        this.audioManager = audioManager;
        this.itemsManager = itemsManager;
    }
    
    public abstract void update();

    protected abstract void positioningUpdate();

    
    protected void moveToSpawnPoint() {
    	if (spawnPoint == null) return;
    	
    	int posSpeed = (int)(speed/3);
     
    	if (x < spawnPoint.x) x = Math.min(x + posSpeed, spawnPoint.x);
    	else if (x > spawnPoint.x) x = Math.max(x - posSpeed, spawnPoint.x);
     	if (y < spawnPoint.y) y = Math.min(y + posSpeed, spawnPoint.y);
     	else if (y > spawnPoint.y) y = Math.max(y - posSpeed, spawnPoint.y);
 }
 

    public boolean inSpawnPosition() {
        if (spawnPoint == null) return false;
        return (Math.abs(x - spawnPoint.x) < speed && Math.abs(y - spawnPoint.y) < speed);
    }

    
    public void updateCoordinates() {
		
		entityLeft= x + hitbox.x;
		entityRight= x + hitbox.x + hitbox.width;
		entityTop= y + hitbox.y;
		entityBottom= y + hitbox.y + hitbox.height;		
		
		if (moving) {
			switch (xDirection) {				
			case LEFT : entityLeft-= speed;
			case RIGHT : entityRight+= speed;
			}
		}
		
		if (jumping) entityTop+= jumpSpeed;
		
		entityLeftCol= entityLeft/ TILESIZE;
		entityRightCol= entityRight/ TILESIZE;
		entityTopRow= entityTop/ TILESIZE;
		entityBottomRow= entityBottom/ TILESIZE;
		
	}

    
    protected void checkTilesCollisions() {

    	if (entityTopRow < TOPBORDER) return;
    	
        if (hitbox == null) return;

        int[][] map = tilesManager.getMap();
        
        int tileType1, tileType2;
		
		switch (xDirection) {
		case LEFT : 
			if (entityLeftCol <= LEFTBORDER) collisionX=true;
			else if (moving && !jumping) {
				tileType1= map[(int)entityTopRow][(int)entityLeftCol];
				tileType2= map[(int)entityBottomRow][(int)entityLeftCol];
				if (tileType1!= 0 || tileType2!= 0) collisionX=true;
			}
			else collisionX=false;
			break;
		case RIGHT :
			if (entityRightCol >= RIGHTBORDER) collisionX=true;
			else if (moving && !jumping) {
				tileType1= map[(int)entityTopRow][(int)entityRightCol];
				tileType2= map[(int)entityBottomRow][(int)entityRightCol];
				if (tileType1!= 0 || tileType2!= 0) collisionX=true;		 
			}
			else collisionX=false;
			break;
		}
		
		entityLeftCol= (entityLeft+speed+1)/ TILESIZE;
		entityRightCol= (entityRight-speed-1)/ TILESIZE;

        // --- CONTROLLO VERTICALE (PAVIMENTO) - CORRETTO ---
		if (!jumping) {
			
			double fallingShift= fallSpeed + GRAVITY;
			entityBottom += (fallingShift < 1)? 1 : fallingShift;
			entityBottomRow = entityBottom/TILESIZE;
			
			if (entityBottomRow >= MAXSCREENROW) { y = 0; return; }
			
			tileType1= map[(int)entityBottomRow][(int)entityRightCol];
			tileType2= map[(int)entityBottomRow][(int)entityLeftCol];
			
			if (tileType1!= 0 || tileType2!= 0) {
				collisionFloor=true;
				falling=false;
				fallSpeed=0;
				
				int lowerEdge= TILESIZE - (hitbox.height + hitbox.y);
				if (fallingShift >1) y=((int)(entityBottomRow-1)*TILESIZE)+ lowerEdge-1;
			} 
			
			else {
				collisionFloor=false;
				falling= true;				
			}			
		}
		
	}
    
    
    public abstract String getName();
    public abstract void jump();
    public abstract void attack();
    public abstract void die();
    

    public void move(DirectionX dir) {
        if (!moving) this.moving = true;
        this.xDirection = dir;
    }

    public void stopMoving() {
        this.moving = false;
    }
    
    public void stopAttacking() {
        this.attacking = false;
    }
    
    protected void fall() {
        if (!this.jumping) {
            this.falling = true;
        }
    }

    public int getBottomY() {
        if (hitbox == null) return y;
        return this.y + this.hitbox.y + this.hitbox.height;
    }

    public int getCenterX() {
        if (hitbox == null) return x;
        return this.x + this.hitbox.x + (this.hitbox.width / 2);
    }
    
    public int getX() { return x; }
    public int getY() { return y; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public Rectangle getHitbox() { return hitbox; }
    public DirectionX getDirection() { return xDirection; }
    public boolean isJumping() { return jumping; }
    public boolean isFalling() { return falling; }
    public boolean isDrying() { return dying; }
    public boolean isPositioning() { return positioning; }
    
    public void takePosition() { this.positioning = true; }
    
    @Override
    public String getDirectionData() { return xDirection.toString(); }
    
    @Override
    public String getActionData() {
        if (positioning) return "INSIDE BUBBLE";
        if (dying) return "DYING";
        if (attacking) return "ATTACKING";
        if (jumping) return "JUMPING";
        if (falling) return "FALLING";
        if (moving) return "WALKING";
        return "IDLING";
    }
    
}
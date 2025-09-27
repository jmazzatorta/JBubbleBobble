package model.bubbles;

import static constants.Constants.*;
import java.awt.Rectangle;
import java.util.List;

import model.GameObject;
import model.entities.players.Player;
import model.interfaces.Collidable;
import model.managers.BubblesManager;
import model.managers.MinorEventsManager;
import model.managers.TilesManager;
import model.utils.DirectionX;
import view.interfaces.Drawable;

public abstract class Bubble extends GameObject implements Drawable, Collidable {
    
    protected int x, y;
    protected Rectangle hitbox = new Rectangle(4, 2, TILESIZE * 2 - 8, TILESIZE * 2 - 4);
    protected int speed = 5;
    
    protected boolean projectile;
    protected boolean readyToRemove = false;
    protected boolean blocked;
    protected boolean jumped;
    protected int jumpedTC;
    
    protected DirectionX direction = DirectionX.RIGHT;

    protected int bubbleLeft, bubbleRight, bubbleTop, bubbleBottom;
    protected double bubbleLeftCol, bubbleRightCol, bubbleTopRow, bubbleBottomRow;
    protected boolean collisionLeft, collisionRight, collisionTop;
    protected int[][] tileMap;
    
    protected final MinorEventsManager minorEventsManager;
    protected final BubblesManager bubblesManager;

    public static final int EXPLOSION_RADIUS = 32;

    public Bubble(int x, int y, TilesManager tilesManager, MinorEventsManager minorEventsManager, BubblesManager bubblesManager) {
        this.x = x;
        this.y = y;
        this.tileMap = tilesManager.getMap();
        this.minorEventsManager = minorEventsManager;
        this.bubblesManager = bubblesManager;
    }

    public abstract void update();

    public void pop(Player popOwner) {
        if (readyToRemove) return;

        readyToRemove = true;
        minorEventsManager.createEvent(x, y, "POW");
        triggerChainReaction();
    }

    public void pop() { pop(null); }

    public Player getOwner() { return null; }

    public void updateCoordinates() {
        bubbleLeft = x + hitbox.x;
        bubbleRight = x + hitbox.x + hitbox.width;
        bubbleTop = y + hitbox.y;
        bubbleBottom = y + hitbox.y + hitbox.height;

        bubbleLeftCol = bubbleLeft / (double)TILESIZE;
        bubbleRightCol = bubbleRight / (double)TILESIZE;
        bubbleTopRow = bubbleTop / (double)TILESIZE;
        bubbleBottomRow = bubbleBottom / (double)TILESIZE;
    }

    public void checkTilesCollisions() {
        // Calcoliamo le coordinate base
        int topRow = (int) bubbleTopRow;
        int bottomRow = (int) bubbleBottomRow;
        int leftCol = (int) bubbleLeftCol;
        int rightCol = (int) bubbleRightCol;


        if (topRow < 0 || bottomRow >= MAXSCREENROW || leftCol < 0 || rightCol >= MAXSCREENCOL) {
            readyToRemove = true; // La marchiamo per la rimozione
            return;
        }

        // --- CONTROLLI DI COLLISIONE (ORA SICURI) ---
        switch (direction) {
		
		case LEFT :
		    int nextLeftCol = Math.max((bubbleLeft - speed) / TILESIZE, 0);
		    if (tileMap[topRow][nextLeftCol] != 0 || tileMap[bottomRow][nextLeftCol] != 0) {
		        collisionLeft = true;
		    }
		    break;
		
		case RIGHT :
		    int nextRightCol = Math.min((bubbleRight + speed) / TILESIZE, MAXSCREENCOL - 1);
		    if (tileMap[topRow][nextRightCol] != 0 || tileMap[bottomRow][nextRightCol] != 0) {
		        collisionRight = true;
		    }
		    break;
        }
        
        
        bubbleTopRow= (bubbleTop-speed)/ TILESIZE;
		if (bubbleTopRow==0) y=26*TILESIZE;
        int nextTopRow = Math.max((bubbleTop - speed) / TILESIZE, 0);
        if (tileMap[nextTopRow][leftCol] != 0 || tileMap[nextTopRow][rightCol] != 0) {
            collisionTop = true;
        }
    }
    
    protected void checkBubblesCollisions() { 

    	int left= bubbleLeft;
		int right= bubbleRight;
		int midX= (left+right)/2;
		int top= bubbleTop;
		int bottom= bubbleBottom;
		int midY= (top+bottom)/2;
		
		if (!collisionTop) top-=speed;
		switch (direction) {					
		case LEFT : left-= speed;
		case RIGHT : right+= speed;
		}
		
		List<Bubble> bubbles= bubblesManager.getBubbles();
        	
        for (Bubble b2 : bubbles) {
            if (isProjectile() && !b2.isProjectile() && !b2.equals(this)) {
				int left2= b2.getX() + b2.getHitbox().x;
				int right2= left2 + b2.getHitbox().width;
				int top2= b2.getY() + b2.getHitbox().y;
				int bottom2= top2 + b2.getHitbox().height;
				
				boolean xCollision= ((left <= right2 && right2 <= right) || (left <= left2 && left2 <= right));
				boolean yCollision= ((top <= top2 && top2 <= bottom) || (top <= bottom2 && bottom2 <= bottom));
				
				boolean topCollision= (top <= bottom2  && bottom2 <= midY);
				boolean leftCollision= (left <= right2 && right2 <= midX);
				boolean rightCollision= (midX <= left2 && left2 <= right);
				
				if (yCollision) {
					if (leftCollision) collisionLeft=true;
					if (rightCollision) collisionRight=true;
				}
				if (xCollision) {
					if (topCollision) collisionTop=true;
				}
                
            }
        }
    }
    
	public void triggerChainReaction() {
		
		int cLeft= bubbleLeft - EXPLOSION_RADIUS;
		int cRight= bubbleRight + EXPLOSION_RADIUS;
		int cTop= bubbleTop - EXPLOSION_RADIUS;
		int cBottom= bubbleBottom + EXPLOSION_RADIUS;
		
		List<Bubble> bubbles= bubblesManager.getBubbles();
		
		for (Bubble b2 : bubbles) {
			if (!b2.isProjectile()) {			
				int x2= b2.getX()+ (b2.getHitbox().width)/2;
				int y2= b2.getY() + (b2.getHitbox().height)/2;
				
				if ((cLeft <= x2 && x2 <= cRight) && (cTop <= y2 && y2 <= cBottom)) b2.pop();
			}
		}
	}

    protected void changeDirection() {
        if (collisionRight) direction = DirectionX.LEFT;
        else if (collisionLeft) direction = DirectionX.RIGHT;
    }

    public void setDirection() {
        if (collisionRight && collisionLeft) blocked = true;
        else if (collisionRight ^ collisionLeft) changeDirection();
    }

    public void setJumped() {
        jumped = true;
        jumpedTC = 60;
    }
    
    public void setLeftCollision() {
    	collisionLeft = true;
    }
    
    public void setRightCollision() {
    	collisionRight = true;
    }
    
    public void setTopCollision() {
    	collisionTop = true;
    }

    public void resetCollisions() {
        collisionLeft = false;
        collisionRight = false;
        collisionTop = false;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public Rectangle getHitbox() { return hitbox; }
    public int getCenterX() { return x + hitbox.x + hitbox.width / 2; }
    public int getCenterY() { return y + hitbox.y + hitbox.height / 2; }
    public DirectionX getDirection() { return direction; }
    public boolean isProjectile() { return projectile; }
    public boolean isReadyToRemove() { return readyToRemove; }
    
    // Drawable methods
    public String getDirectionData() { return null; }
    public boolean isFlashing() { return false; }

}

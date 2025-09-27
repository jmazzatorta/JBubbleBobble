package model.entities.enemies;

import static constants.Constants.*;

import java.awt.Point;

import model.entities.Entity;
import model.entities.players.Player;
import model.managers.AudioManager;
import model.managers.ItemsManager;
import model.managers.TilesManager;
import model.SoundEffects;
import model.utils.DirectionX;

public abstract class Enemy extends Entity {

    protected boolean canMove = true;
    protected boolean freezedInputs;
    protected boolean trapped;
    protected boolean angry;
    protected boolean readyToRemove;
    protected int scoreValue;
    
    protected int dyingTC = 0;
    protected int  dyingTime = FPS * 2;

    protected Player closerPlayer;
    protected int attackControlTC;
    protected int xControlTC = 50;
    protected int yControlTC;
    protected int chosePlayerTC = 300;
    

    public Enemy(int x, int y, TilesManager tilesManager, AudioManager audioManager, ItemsManager itemsManager) {
        super(tilesManager, audioManager, itemsManager);
        this.spawnPoint = new Point(x*TILESIZE, y*TILESIZE);
        this.x = x * TILESIZE;
        this.y = 0;
        this.positioning = true;
    }
    
    protected void setDefaultValues() {
        fallSpeed = 0;
        canDie = false;
    }

    
    @Override
    public void update() {
        if (positioning) { positioningUpdate(); return; }
        if (!canMove) return;
        if (dying) { dyingUpdate(); return; }

        playingUpdate();
    }


    protected abstract void handleAI();

    protected void playingUpdate() {
    	handleAI();
    	
    	collisionX = false;
		updateCoordinates();
		checkTilesCollisions();
		checkPlayerCollisions();
		
		if (collisionX) changeXDirection();
		
		if (jumping) {
			jumpSpeed-= GRAVITY;
			y-= jumpSpeed;
			if (jumpSpeed <= 0) {
				jumping= false; 
				falling= true;
				jumpSpeed = defaultJumpSpeed;
			}
		}	
		
		if (falling) {
			fallSpeed+= GRAVITY;
			y+= fallSpeed;
		}	
		
		switch (xDirection) {
		case LEFT : x-= speed; break;
		case RIGHT : x+= speed; break;
		}        
    }
    
    @Override
    public void jump() {
        if (collisionFloor && !falling) {
            this.jumping = true;
        }
    }

    @Override
    public void attack() {
        this.attacking = true;
    }

    @Override
    public void die() {
    	if (!dying && canDie) {
    		dying = true;
    		jump();
        	audioManager.playSound(SoundEffects.ENEMY_DEATH);
    	}
    }

    @Override
    protected void positioningUpdate() {
        if (spawnPoint == null) {
            this.positioning = false;
            return;
        }
        
        moveToSpawnPoint();
        if (inSpawnPosition()) {
        	positioning = false;
        	canDie = true;
        }
        
    }

    @Override
    protected void moveToSpawnPoint() { 
    	if (y < spawnPoint.y) y = Math.min(y + speed, spawnPoint.y);
     	else if (y > spawnPoint.y) y = Math.max(y - speed, spawnPoint.y);
    }

    
    protected void dyingUpdate() {
        collisionX = false;

        updateCoordinates();
        checkTilesCollisions();
        
        dyingTC++;
        if (dyingTC > dyingTime) {
            readyToRemove = true;
            itemsManager.foodFactory(x/TILESIZE,y/TILESIZE);
            return;
        }

        if (collisionX) changeXDirection();
        if (!jumping && !falling && !collisionFloor) fall();

        if (jumping) {
            jumpSpeed -= GRAVITY;
            y -= jumpSpeed;
            if (jumpSpeed <= 0) {
                jumping = false;
                falling = true;
                jumpSpeed = defaultJumpSpeed;
            }
        } else if (falling) {
            fallSpeed += GRAVITY;
            y += fallSpeed;
        }
    }


    protected void changeXDirection() {
        xDirection = (xDirection == DirectionX.RIGHT) ? DirectionX.LEFT : DirectionX.RIGHT;
        collisionX = false;
    }
    
    /*
    public void checkDyingCollisions() {
    	
    	if (entityTopRow < TOPBORDER) return;

        int[][] map = tilesManager.getMap();
        
        int tileType1, tileType2;
		
        if (jumping || falling) {
        	
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
        }        
		entityLeftCol= (entityLeft+speed+1)/ TILESIZE;
		entityRightCol= (entityRight-speed-1)/ TILESIZE;

		if (!jumping) {
			
			double fallingShift= fallSpeed + GRAVITY;
			entityBottom+= (fallingShift < 1)? 1 : fallingShift;
			entityBottomRow= entityBottom/TILESIZE;
			
			tileType1= map[(int)entityBottomRow][(int)entityRightCol];
			tileType2= map[(int)entityBottomRow][(int)entityLeftCol];
			
			if (tileType1!= 0 || tileType2!= 0) {
				collisionFloor=true;
				falling=false;
				fallSpeed=0;
				
				//Aggiustamento posizione in fase di caduta
				int lowerEdge= TILESIZE - (hitbox.height + hitbox.y);
				if (fallingShift >1) y=((int)(entityBottomRow-1)*TILESIZE)+ lowerEdge-1;
			} 
			
			else {
				collisionFloor=false;
				falling= true;				
			}			
		}
    }
    */
    
    public void checkPlayerCollisions() {
		
		int pLeft = closerPlayer.getX()+ closerPlayer.getHitbox().x;
		int pRight = pLeft + closerPlayer.getHitbox().width;
		int pTop = closerPlayer.getY()+ closerPlayer.getHitbox().y;
		int pBottom = pTop + closerPlayer.getHitbox().width;
		
		if (((pLeft <= entityLeft && entityLeft <= pRight) || (pRight <= entityRight && entityRight <= pRight))
				&& ((pTop <= entityTop && entityTop <= pBottom) || (pBottom <= entityBottom && entityBottom <= pBottom))) {
			closerPlayer.die();
		}
	}

    
    public void freezeInputs() {
        this.freezedInputs = true;
    }

    public void unfreezeInputs() {
        this.freezedInputs = false;
    }

    public boolean isAttackable() { return !(trapped || dying); }
    public boolean isReadyToRemove() { return readyToRemove; }
    public void canMove(boolean b) { this.canMove = b; }
    public boolean canDie() { return canDie; }
    public void getMad() { angry = true; speed += 3; }
    public void trapped() { this.trapped = true; }
    public int getScore() { return this.scoreValue; }

    
    @Override
    public String getActionData() {
        if (dying) return "DYING";
        if (jumping || falling) return angry ? "ANGRY JUMPING" : "JUMPING";
        if (moving) return angry ? "ANGRY MOVING" : "MOVING";
        return "IDLING";
    }
}
package model.bubbles;

import model.utils.DirectionX;

import java.util.List;

import model.entities.enemies.Enemy;
import model.entities.players.Player;
import model.managers.BubblesManager;
import model.managers.EnemiesManager;
import model.managers.MinorEventsManager;
import model.managers.TilesManager;

public class PlayerBubble extends Bubble {

    private final Player owner;
    private final EnemiesManager enemiesManager;

    private int projectileTC = 30;
    private boolean trapping;
    private Enemy trappedEnemy;
    private int lifeTC = 600;
    private boolean lifeTimerActive = false;

    public PlayerBubble(Player owner, int x, int y, DirectionX direction, TilesManager tiles, 
                        MinorEventsManager minorEvents, BubblesManager bubbles, EnemiesManager enemies) {
        super(x, y, tiles, minorEvents, bubbles);
        this.owner = owner;
        this.enemiesManager = enemies;

        this.direction = direction;
        this.projectile = true;

        checkPowerUps();
    }

    private void checkPowerUps() {
        if (owner.hasPinkCandy()) projectileTC = 40;
        if (owner.hasBlueCandy()) speed = 17;
    }

    @Override
    public void update() {    	
        resetCollisions();
        updateCoordinates();
        checkTilesCollisions();

        if (projectile) {
        	
			if (collisionLeft || collisionRight) {
				pop();	
				return;
			}
			
			if (projectileTC<0) setBubble();
			else {
				checkEnemiesCollisions();
				projectileTC--;
			}		
		} 
        
        else {
			checkBubblesCollisions();
			setDirection();
			
			if (blocked) {
				y+= speed*2;
				blocked=false;
			}
		} 
		
		if (jumped) {
			jumpedTC--;
			if (jumpedTC <= 0) jumped= false;
		}
            
		if (!collisionTop && !projectile) y-= speed;
		else {
			switch (direction) {
			case LEFT : 
				x-= speed; 
				break;	
			case RIGHT : 
				x+= speed; 
				break;	
			}
		} 
            
        if (trapping && lifeTimerActive) {
            lifeTC--; 
            if (lifeTC <= 0) {
                popByTimeout(); 
                return; 
            }
        }
    }
    
    public void checkEnemiesCollisions() {		
		int x=0;
		switch (direction) {
			case LEFT : x= bubbleLeft-speed; break;
			case RIGHT : x= bubbleRight+speed; break;
		}
		
		List<Enemy> enemies = enemiesManager.getActiveEnemies();
		for (Enemy e : enemies) {
		
			if (!e.canDie()) continue;

			int eLeft= e.getX() + e.getHitbox().x;
			int eRight= eLeft + e.getHitbox().width;
			int eTop= e.getY() + e.getHitbox().y;
			int eBottom= eTop + e.getHitbox().height;
			
			if ((eLeft <= x && x <= eRight) && ((eTop <= bubbleTop && bubbleTop <= eBottom) || (eTop <= bubbleBottom && bubbleBottom <= eBottom))){
				trap(e);
				return;
			}
		}
	}

    @Override
    public void pop(Player popOwner) {
        if (readyToRemove) return;

        if (trapping && trappedEnemy != null) {
            Player scorer = (popOwner != null) ? popOwner : this.owner;
            scorer.addScore(trappedEnemy.getScore());
            enemiesManager.releaseTrappedEnemy(trappedEnemy, x, y, true);
        }

        super.pop(popOwner);
    }

    @Override
    public Player getOwner() { return owner; }

    private void setBubble() {
        projectile = false;
        speed = 1;
    }

    public void trap(Enemy enemy) {
        setBubble();
        trapping = true;
        trappedEnemy = enemy;
        enemiesManager.trapEnemy(enemy);
        lifeTimerActive = true;
    }
    
    public void popByTimeout() {
        if (readyToRemove) return; 

        if (trapping && trappedEnemy != null) {
        	trappedEnemy.setX(x);
        	trappedEnemy.setY(y);
            enemiesManager.releaseTrappedEnemy(trappedEnemy, x, y, false);
        }

        super.pop(null);
    }

    // --- View ---
    public String getTypeData() { return owner.getName(); }
    public String getActionData() { 
    	if (projectile) return "PROJECTILE";
    	else if (trapping) return "TRAPPING " + trappedEnemy.getTypeData();
    	else if (jumped) return "JUMPED";
    	return "MOVING"; 
    }

}

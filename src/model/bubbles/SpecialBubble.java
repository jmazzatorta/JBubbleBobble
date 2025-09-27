package model.bubbles;

import static constants.Constants.*;
import model.managers.MinorEventsManager;
import model.managers.ProjectilesManager;
import model.entities.players.Player;
import model.managers.BubblesManager;
import model.managers.TilesManager;

public class SpecialBubble extends Bubble {

    private final SpecialBubbleType bubbleType;
    private final ProjectilesManager projectilesManager;

    public SpecialBubble(int x, SpecialBubbleType bubbleType, TilesManager tiles, 
                         MinorEventsManager minorEvents, BubblesManager bubblesManager, ProjectilesManager projectiles) {
        super(x, SCREENHEIGHT - TILESIZE * 4, tiles, minorEvents, bubblesManager);
        this.bubbleType = bubbleType;
        this.projectilesManager = projectiles;
        this.speed = 1;
    }

    @Override
    public void update() {   	
        resetCollisions();
        
        updateCoordinates();
        checkTilesCollisions();
        checkBubblesCollisions();
        
        setDirection();
		
		if (blocked) {
			y+= speed*2;
			blocked=false;
		}
		
		if (jumped) {
			jumpedTC--;
			if (jumpedTC <= 0) jumped= false;
		}

        if (!collisionTop) y-= speed;
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
    }

    @Override
    public void pop(Player popOwner) {
        if (readyToRemove) return;

        projectilesManager.spawnProjectileFromBubble(this);
        super.pop(popOwner);
    }

    public SpecialBubbleType getType() { return bubbleType; }

    public String getTypeData() { return bubbleType.toString(); }
    public String getActionData() { 
    	if (jumped) return "JUMPED";
    	return "MOVING"; 
    }
    public String getDirectionData() { return null; }
    public boolean isFlashing() { return false; }
}

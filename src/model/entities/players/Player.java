package model.entities.players;

import static constants.Constants.*;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.SoundEffects;
import model.bubbles.Bubble;
import model.entities.Entity;
import model.entities.PowerUp;
import model.entities.PowerUpType;
import model.items.Item;
import model.managers.AudioManager;
import model.managers.BubblesManager;
import model.managers.ItemsManager;
import model.managers.LevelManager;
import model.managers.MinorEventsManager;
import model.managers.ScoreManager;
import model.managers.TilesManager;
import model.utils.DirectionX;

public class Player extends Entity {
	

    private final ScoreManager score;
    private final String user;
    private int lives = 3;

    private final BubblesManager bubblesManager;
    private final MinorEventsManager minorEventsManager;
    private final LevelManager levelManager;
    
    private boolean poppinBubble = false;
    private int poppinTC = 0;
    
    private boolean collisionOnBubble;
    private int immortalityTC, dyingTC;
    
    private int fireRate=15;
    private boolean stopAttacking;
    
    // Contatori per la logica di gioco
    private int bubblesCounter = 0;
    private int specialBubblesCounter = 0;
    private long stepsCounter = 0;
    private int blueCandyCounter = 0, yellowCandyCounter = 0, pinkCandyCounter = 0;

    // Power-ups attivi
    private final HashMap<PowerUpType, PowerUp> activePowerUps;

    
    public Player(String user, BubblesManager bubblesManager, MinorEventsManager minorEventsManager,
                  LevelManager levelManager, TilesManager tilesManager, AudioManager audioManager, ItemsManager itemsManager) {
        super(tilesManager, audioManager, itemsManager);
        this.user = user;
        this.score = new ScoreManager();
        this.activePowerUps = new HashMap<>();
        this.bubblesManager = bubblesManager;
        this.minorEventsManager = minorEventsManager;
        this.levelManager = levelManager;
        setDefaultValues();
    }

    private void setDefaultValues() {
        switch (user) {
            case "bub" -> { spawnPoint = new Point(6 * TILESIZE, 18 * TILESIZE); xDirection = DirectionX.RIGHT; }
            case "bob" -> { spawnPoint = new Point(26 * TILESIZE, 18 * TILESIZE); xDirection = DirectionX.LEFT; }
        }
        x = spawnPoint.x;
        y = spawnPoint.y;
        hitbox = new Rectangle(7, 10, TILESIZE * 2 - 14, TILESIZE * 2 - 10);
        speed = 10;
        defaultJumpSpeed = 18;
        jumpSpeed = 18;
        fallSpeed = 0;
        canDie = true;
    }

    @Override
    public void update() {    	
    	if (!hasLives()) return; 

        if (positioning) { positioningUpdate(); return; }
        if (dying) { dyingUpdate(); return; }
        playingUpdate();
    }

    private void playingUpdate() {
        if (!canDie) {
            immortalityTC++;
            if (immortalityTC > 120) { canDie = true; immortalityTC = 0; }
        }
        
        collisionX= false;
		updateCoordinates();
        checkTilesCollisions();
		checkItemsCollisions();
		checkBubblesCollisions();
		
		
		if (!jumping && !falling && !collisionFloor) fall();
		if (jumping) {
			jumpSpeed-= GRAVITY;
			y-= jumpSpeed;
				if (jumpSpeed <= 0) {
					jumping= false; 
					falling= true;
					jumpSpeed= defaultJumpSpeed;
				}
		}	else if (falling) {
			fallSpeed+= GRAVITY;
			y+= fallSpeed;
		}	
		
		if (moving && !collisionX) {
			switch (xDirection) {
			case LEFT : x-= speed; break;
			case RIGHT : x+= speed; break;
			}
			
			stepsCounter+= 0.25;
			if (hasCrystalRing() && stepsCounter%1==0) addScore(10);
		}
        
		if (attacking) {
			if (attackTC>fireRate) {
				bubblesManager.createPlayerBubble(this);
				attackTC=0;
				
				bubblesCounter++;
				if (hasRubyRing()) addScore(100);
			}
		}
		attackTC++;
		
		if (stopAttacking) {
			attacking= false;
			stopAttacking=false;
		}
        
        updatePowerUps();
    }
    
    @Override
    protected void positioningUpdate() {
        moveToSpawnPoint();

        if (poppinBubble) {
        	if (poppinTC > FPS - 5 ) {
        		positioning = false;
        		poppinBubble = false;
        		poppinTC = 0;
        	}
        	poppinTC++;
        }
        
        else if (inSpawnPosition()) {
        	poppinBubble = true;
        }
    }
    
    public void checkBubblesCollisions() {
		
		int midY= (entityTop+entityBottom)/2;
		
		List<Bubble> bubbles = bubblesManager.getBubbles();
		
		for (Bubble b2 :  bubbles) {
			if (!b2.isProjectile()){

				int bLeft= b2.getX() + b2.getHitbox().x +10;
				int bRight= bLeft + b2.getHitbox().width -10;
				int bTop= b2.getY() + b2.getHitbox().y;
				int bBottom= bTop + b2.getHitbox().height;
				
				boolean xCollision= ((entityLeft <= bRight && bRight <= entityRight) || (entityLeft <= bLeft && bLeft <= entityRight));
				
				boolean topCollision= (entityTop <= bBottom  && bBottom <= midY);
				boolean bottomCollision= (entityTop <= bTop && bTop <= entityBottom+20);
				
				if (xCollision) {
					
					if (jumping && topCollision) {
						b2.pop();
						return;
					}
					
					if (falling && bottomCollision) {
						collisionOnBubble= true;
						b2.setJumped();
					} 
					else collisionOnBubble= false;
				}
			}
		}
	}
    
	public void checkItemsCollisions() {
		
		List<Item> items = itemsManager.getItems();
		
		for (Item it :  items) {

			int itLeft= it.getX() + it.getHitbox().x;
			int itRight= itLeft + it.getHitbox().width;
			int itTop= it.getY() + it.getHitbox().y;
			int itBottom= itTop + it.getHitbox().height;
			
			boolean xCollision= ((entityLeft <= itRight && itRight <= entityRight) || (entityLeft <= itLeft && itLeft <= entityRight));
			boolean yCollision= ((entityTop <= itBottom  && itBottom <= entityBottom) || (entityTop <= itTop  && itTop <= entityBottom));
			
			if (xCollision && yCollision) {
				it.collect(this);
			}
		}
	}

    private void updatePowerUps() {
        activePowerUps.values().forEach(PowerUp::tick);
        activePowerUps.entrySet().removeIf(entry -> {
            if (entry.getValue().isExpired()) {
                endPowerUp(entry.getKey());
                return true;
            }
            return false;
        });
    }
    
    @Override
    public void takePosition() {
        this.positioning = true;
    }

    
    @Override
	public void jump() {
    	if (jumping) return;
		if ((!falling && collisionFloor) || (falling && collisionOnBubble)) {
			jumping=true;
            audioManager.playSound(SoundEffects.JUMP);
            if (hasAmethystRing()) addScore(500);
        }
    }

    @Override
    public void attack() {
		attacking = true;
		audioManager.playSound(SoundEffects.BUBBLE);
    }
    
    @Override
    public void stopAttacking() {
        this.attacking = false;
    }

    @Override
    public void die() {
        if (canDie && !dying) {
            dying = true;
            audioManager.playSound(SoundEffects.DEATH);
        }
    }

    public void bounceOnBubble() {
        this.fallSpeed = 0;
        this.falling = false;
        jump();
    }

    public void setCollisionOnBubble(boolean onBubble) {
        this.collisionOnBubble = onBubble;
    }

    public void addScore(int value) {
        score.addScore(value);
        minorEventsManager.createScoreEvent(x, y, "SCORE", user, value);
    }

    public void activatePowerUp(PowerUpType type) {
        // Questo primo switch è corretto
        switch (type) {
            case CLOCK -> { levelManager.setClock(); audioManager.playSound(SoundEffects.ITEM); return; }
            case DYNAMITE -> { levelManager.setDynamite(); audioManager.playSound(SoundEffects.ITEM); return; }
            default -> {}
        }

        // Anche questo è corretto
        int duration = switch (type) {
            case PINKCANDY, BLUECANDY, YELLOWCANDY -> 15;
            case SHOES, CRYSTALRING, AMETHYSTRING, RUBYRING -> 10;
            default -> 0;
        };
        if (duration == 0) return;

        switch (type) {
            case PINKCANDY -> pinkCandyCounter++;
            case BLUECANDY -> blueCandyCounter++;
            case YELLOWCANDY -> {
                yellowCandyCounter++;
            }
            case SHOES -> speed = 6;
            default -> {} 
        }

        if (activePowerUps.containsKey(type)) {
            activePowerUps.get(type).reset();
        } else {
            activePowerUps.put(type, new PowerUp(duration * FPS));
        }
        audioManager.playSound(SoundEffects.ITEM);
    }

    private void dyingUpdate() {
        dyingTC++;
        if (dyingTC > 120) {
            lives--;
            dying = false;
            canDie = false;
            dyingTC = 0;
            if (lives >= 0) takePosition();
        }
    }

    public void resetPowerUps() {
        new ArrayList<>(activePowerUps.keySet()).forEach(this::endPowerUp);
        activePowerUps.clear();
    }

    public void endPowerUp(PowerUpType type) { 
        switch (type) {
           // case YELLOW_CANDY -> rechargeTime = defaultRechargeTime;
            case SHOES -> speed = 4;
            default -> {}
        }
    }

    public void resetCounters() {
        bubblesCounter = 0;
        specialBubblesCounter = 0;
        stepsCounter = 0;
        blueCandyCounter = 0;
        yellowCandyCounter = 0;
        pinkCandyCounter = 0;
    }

    public void resetBubblesCounter() { bubblesCounter = 0; }
    public void resetSpecialBubblesCounter() { specialBubblesCounter = 0; }
    public void resetStepCounter() { stepsCounter = 0; }
    public void resetCandyCounter(PowerUpType color) {
        switch(color) {
            case BLUECANDY : blueCandyCounter = 0;
            case YELLOWCANDY : yellowCandyCounter = 0;
            case PINKCANDY : pinkCandyCounter = 0;
		default:
			break;
        }
    }

    public int getScore() { return score.getScore(); }
    public int getLives() { return lives; }
    public boolean hasLives() { return lives > 0; }
    public boolean canDie() { return canDie; }
    public int getBubblesCounter() { return bubblesCounter; }
    public int getSpecialBubblesCounter() { return specialBubblesCounter; }
    public long getStepCounter() { return stepsCounter; }
    public int getCandyCounter(PowerUpType candyType) {
        return switch(candyType) {
            case BLUECANDY -> blueCandyCounter;
            case YELLOWCANDY -> yellowCandyCounter;
            case PINKCANDY -> pinkCandyCounter;
            default -> 0; 
        };
    }
    
    public boolean hasPinkCandy() { 
        return activePowerUps.containsKey(PowerUpType.PINKCANDY); 
    }
        
    public boolean hasBlueCandy() { 
        return activePowerUps.containsKey(PowerUpType.BLUECANDY); 
    }
        
    public boolean hasYellowCandy() { 
        return activePowerUps.containsKey(PowerUpType.YELLOWCANDY); 
    }
        
    public boolean hasCrystalRing() { 
        return activePowerUps.containsKey(PowerUpType.CRYSTALRING); 
    }
        
    public boolean hasAmethystRing() { 
        return activePowerUps.containsKey(PowerUpType.AMETHYSTRING); 
    }
        
    public boolean hasRubyRing() { 
        return activePowerUps.containsKey(PowerUpType.RUBYRING); 
    }
    
    public boolean moving() {
        return this.moving;
    }
    
    public boolean isDying() {
    	return this.dying;
    }

    @Override
    public String getName() { return user; }
    @Override
    public String getTypeData() { return user; }
    @Override
    public String getActionData() {
    	if (poppinBubble) return "POPPING BUBBLE";
        if (positioning) return "INSIDE BUBBLE";
        if (dying) return "DYING";
        if (attacking) return "ATTACKING";
        if (jumping) return "JUMPING";
        if (falling) return "FALLING";
        if (moving) return "WALKING";
        return "IDLING";
    }
    @Override
    public boolean isFlashing() {
        return !canDie && (immortalityTC % 5 > 2);
    }
    @Override
    public String getDirectionData() {
        return xDirection.toString();
    }    
    
}
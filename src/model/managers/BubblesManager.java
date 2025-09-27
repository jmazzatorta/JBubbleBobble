package model.managers;

import model.GameModel;
import model.bubbles.Bubble;
import model.bubbles.PlayerBubble;
import model.bubbles.SpecialBubble;
import model.bubbles.SpecialBubbleType;
import model.entities.players.Player;
import model.utils.DirectionX;

import java.util.List;
import java.util.Random;

import static constants.Constants.*;

public class BubblesManager extends BaseManager<Bubble> {

    private final TilesManager tilesManager;
    private final MinorEventsManager minorEventsManager;
    private final EnemiesManager enemiesManager;
    private final ProjectilesManager projectilesManager;

    private final Random random = new Random();
    private boolean factoryBlocked;
    private int specialBubblesSpawnTC = 10 * FPS;
    private SpecialBubbleType nextSpecialBubbleType;

    public BubblesManager(GameModel gameModel,
                          TilesManager tilesManager,
                          MinorEventsManager minorEventsManager,                        
                          EnemiesManager enemiesManager,
                          ProjectilesManager projectilesManager) {
        super(gameModel);
        this.tilesManager = tilesManager;
        this.minorEventsManager = minorEventsManager;
        this.enemiesManager = enemiesManager;
        this.projectilesManager = projectilesManager;
    }

    public void createPlayerBubble(Player player) {
        DirectionX direction = player.getDirection();
        int shift = (direction == DirectionX.LEFT) ? -5 : 5;

        PlayerBubble bubble = new PlayerBubble(
                player,
                player.getX() + shift,
                player.getY() - 5,
                direction,
                tilesManager,
                minorEventsManager,
                this,
                enemiesManager
        );

        add(bubble); 
    }

    public void createSpecialBubble() {    	
    	
        int col = random.nextInt(2, MAXSCREENCOL - 2);
        SpecialBubble bubble = new SpecialBubble(
                col * TILESIZE,
                nextSpecialBubbleType,
                tilesManager,
                minorEventsManager,
                this,
                projectilesManager
        );

        add(bubble); 
    }

    @Override
    public void update() {
        super.update();

        if (!factoryBlocked && nextSpecialBubbleType != SpecialBubbleType.NONE) {
            specialBubblesSpawnTC--;
            if (specialBubblesSpawnTC <= 0) {
                createSpecialBubble();
                specialBubblesSpawnTC = random.nextInt(10 * FPS, 20 * FPS);
            }
        }
        
        for (Bubble bubble : entities) {
            if (bubble.isReadyToRemove()) {
                remove(bubble); 
            }
        }
    }
    
	public void popAll() {
		for (Bubble bubble : entities) {
			bubble.pop();
		}
	}

    @Override
    public void reset() {
        super.reset(); // 
    }

    public void blockFactory() { factoryBlocked = true; }
    public void unlockFactory() { factoryBlocked = false; }
    public void setNextSpecialBubble(SpecialBubbleType type) { this.nextSpecialBubbleType = type; }

    public List<Bubble> getBubbles() { return getEntities(); }


}

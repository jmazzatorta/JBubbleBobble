package model.entities.enemies;

import static constants.Constants.*;
import java.awt.Rectangle;
import model.ai.EnemyAIService;
import model.managers.AudioManager;
import model.managers.ItemsManager;
import model.managers.PlayersManager;
import model.managers.TilesManager;

public class ZenChan extends Enemy {

    private final PlayersManager playersManager;
    private final EnemyAIService aiService;

    public ZenChan(int x, int y, TilesManager tilesManager, PlayersManager playersManager, EnemyAIService aiService, 
    		AudioManager audioManager, ItemsManager itemsManager) {
        super(x, y, tilesManager, audioManager, itemsManager);
        this.playersManager = playersManager;
        this.aiService = aiService;
        this.hitbox = new Rectangle(5, 10, TILESIZE * 2 - 10, TILESIZE * 2 - 11);        
        setDefaultValues();
    }

    @Override
    protected void setDefaultValues() {
    	super.setDefaultValues();
        speed = 1;
        defaultJumpSpeed = 18;
        jumpSpeed = 18;
        yControlTC = random.nextInt(0, 180);
        closerPlayer = playersManager.getP1();
        scoreValue = 100;
    }

    @Override
    protected void handleAI() {
        chosePlayerTC++;
        xControlTC++;
        yControlTC++;

        if (chosePlayerTC > 300) {
            closerPlayer = aiService.getCloserPlayer(this);
            chosePlayerTC = 0;
        }

        if (xControlTC > 40 && !freezedInputs) {
            aiService.moveEnemyX(this, closerPlayer);
            xControlTC = 0;
        }

        if (yControlTC > 120) {
            aiService.moveEnemyY(this, closerPlayer);
            yControlTC = 0;
        }
        
    }

    @Override
    public String getName() { return "ZENCHAN"; }
    @Override
    public String getTypeData() { return "ZENCHAN"; }
	@Override
	public boolean isFlashing() { return false;	}
	
	@Override
    public String getActionData() {
        if (dying) return "DYING";
        if (moving) return angry ? "ANGRY MOVING" : "MOVING";
        return "IDLING";
    }

}

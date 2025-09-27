package model.entities.enemies;

import static constants.Constants.*;
import java.awt.Rectangle;
import model.ai.EnemyAIService;
import model.managers.AudioManager;
import model.managers.ItemsManager;
import model.managers.PlayersManager;
import model.managers.ProjectilesManager;
import model.managers.TilesManager;

public class Maita extends Enemy {

    private final PlayersManager playersManager;
    private final ProjectilesManager projectilesManager;
    private final EnemyAIService aiService;

    public Maita(int x, int y, TilesManager tilesManager, PlayersManager playersManager,
                 ProjectilesManager projectilesManager, EnemyAIService aiService, AudioManager audioManager, ItemsManager itemsManager) {
        super(x, y, tilesManager, audioManager, itemsManager);
        this.playersManager = playersManager;
        this.projectilesManager = projectilesManager;
        this.aiService = aiService;
        this.hitbox = new Rectangle(10, 3, TILESIZE * 2 - 20, TILESIZE * 2 - 6);
        setDefaultValues();
    }

    @Override
    protected void setDefaultValues() {
    	super.setDefaultValues();
        speed = 2;
        defaultJumpSpeed = 18;
        jumpSpeed = 18;
        xControlTC = random.nextInt(0, 180);
        closerPlayer = playersManager.getP1();
        scoreValue = 300;
    }
    
    protected void playingUpdate() {
    	if (attacking) {
			if (attackTC>20) {
				stopAttacking();
				projectilesManager.spawnProjectileFromEnemy(this);
			}
			attackTC++;
		}
    	
    	super.playingUpdate();
    }

    @Override
    protected void handleAI() {
        attackControlTC--;
        chosePlayerTC++;
        xControlTC++;
        yControlTC++;

        if (attackControlTC <= 0) {
            boolean attacked = aiService.manageAttack(this, closerPlayer);
            attackControlTC = attacked ? 100 : 10;
        }

        if (chosePlayerTC > 300) {
            closerPlayer = aiService.getCloserPlayer(this);
            chosePlayerTC = 0;
        }

        if (xControlTC > 30 && !freezedInputs) {
            aiService.moveEnemyX(this, closerPlayer);
            xControlTC = 0;
        }

        if (yControlTC > 20) {
            aiService.moveEnemyY(this, closerPlayer);
            yControlTC = 0;
        }
    }
    
    // Drawable methods

    @Override
    public String getName() { return "MAITA"; }
    @Override
    public String getTypeData() { return "MAITA"; }
	@Override
	public boolean isFlashing() { return false; }

}

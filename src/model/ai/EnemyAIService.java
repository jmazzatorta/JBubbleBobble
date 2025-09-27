package model.ai; 

import model.entities.enemies.Enemy;
import model.entities.players.Player;
import model.managers.PlayersManager;
import model.utils.DirectionX;
import static constants.Constants.*;

import java.util.List;

public class EnemyAIService {
    
    private final PlayersManager playersManager;

    public EnemyAIService(PlayersManager playersManager) {
        this.playersManager = playersManager;
    }

    public void moveEnemyX(Enemy enemy, Player targetPlayer) {
        if (targetPlayer == null) return;

        if (enemy.getCenterX() < targetPlayer.getCenterX()) {
            enemy.move(DirectionX.RIGHT);
        } else {
            enemy.move(DirectionX.LEFT);
        }
    }

    public void moveEnemyY(Enemy enemy, Player targetPlayer) {
        if (targetPlayer == null) {
            return;
        }

        if (targetPlayer.getBottomY() < enemy.getY()) {
            enemy.jump();
        }

        else if (enemy.getBottomY() < targetPlayer.getY()) {
            enemy.freezeInputs();
        }

        else {
            enemy.unfreezeInputs();
        }
    }

    public Player getCloserPlayer(Enemy enemy) {
        List<Player> activePlayers = playersManager.getActivePlayers();

        if (activePlayers.isEmpty()) {
            return null; 
        }
        if (activePlayers.size() == 1) {
            return activePlayers.get(0); 
        }

        Player p1 = activePlayers.get(0);
        Player p2 = activePlayers.get(1);
        
        int dist1 = Math.abs(enemy.getX() - p1.getX()) + Math.abs(enemy.getY() - p1.getY());
        int dist2 = Math.abs(enemy.getX() - p2.getX()) + Math.abs(enemy.getY() - p2.getY());
        
        return (dist1 < dist2) ? p1 : p2;
    }
    
    public boolean manageAttack(Enemy enemy, Player targetPlayer) {
        if (targetPlayer == null) return false;
        
        int targetMidY = targetPlayer.getY() + (targetPlayer.getHitbox().height / 2);
        
        if (enemy.getY() - TILESIZE < targetMidY && targetMidY < enemy.getY() + TILESIZE * 2) {
            if (enemy.getDirection() == DirectionX.LEFT) {
                if (targetPlayer.getX() < enemy.getX()) {
                    enemy.attack();
                    return true;
                }
            } else { 
                if (targetPlayer.getX() > enemy.getX()) {
                    enemy.attack();
                    return true;
                }
            }
        }
        return false;
    }
}
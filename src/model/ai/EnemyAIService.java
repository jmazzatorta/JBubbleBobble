package model.ai; 

import model.entities.enemies.Enemy;
import model.entities.players.Player;
import model.managers.PlayersManager;
import model.utils.DirectionX;
import static constants.Constants.*;

import java.util.List;

public class EnemyAIService {
    
    private final PlayersManager playersManager;

    /**
     * Il servizio riceve le dipendenze di cui ha bisogno per operare.
     * @param playersManager Il manager da cui ottenere informazioni sui giocatori.
     */
    public EnemyAIService(PlayersManager playersManager) {
        this.playersManager = playersManager;
    }
    
    /**
     * Decide la direzione orizzontale del nemico per avvicinarsi al giocatore.
     */
    public void moveEnemyX(Enemy enemy, Player targetPlayer) {
        if (targetPlayer == null) return;
        
        // Confronta i centri delle hitbox per un movimento più preciso
        if (enemy.getCenterX() < targetPlayer.getCenterX()) {
            enemy.move(DirectionX.RIGHT);
        } else {
            enemy.move(DirectionX.LEFT);
        }
    }
    
    /**
     * Decide se il nemico deve saltare per raggiungere il giocatore.
     */
    public void moveEnemyY(Enemy enemy, Player targetPlayer) {
        if (targetPlayer == null) {
            return;
        }

        if (targetPlayer.getBottomY() < enemy.getY()) {
            enemy.jump();
        }
        // Caso 2: Il nemico è sopra il giocatore. Il nemico si ferma (o pattuglia).
        // Condizione: la base del nemico è più in alto della testa del giocatore.
        else if (enemy.getBottomY() < targetPlayer.getY()) {
            // Qui il nemico è in vantaggio, non dovrebbe muoversi o dovrebbe preparare un attacco dall'alto.
            enemy.freezeInputs();
        }
        // Caso 3: Il nemico e il giocatore sono circa alla stessa altezza.
        // Il nemico deve essere libero di muoversi orizzontalmente per inseguire il giocatore.
        else {
            enemy.unfreezeInputs();
        }
    }
    
    /**
     * MODIFICA: Metodo robusto che trova dinamicamente il giocatore più vicino.
     * Non si basa più su campi statici.
     */
    public Player getCloserPlayer(Enemy enemy) {
        List<Player> activePlayers = playersManager.getActivePlayers();

        if (activePlayers.isEmpty()) {
            return null; // Nessun giocatore attivo
        }
        if (activePlayers.size() == 1) {
            return activePlayers.get(0); // Solo un giocatore
        }
        
        // Se ci sono due giocatori, calcola la distanza e restituisci il più vicino
        Player p1 = activePlayers.get(0);
        Player p2 = activePlayers.get(1);
        
        int dist1 = Math.abs(enemy.getX() - p1.getX()) + Math.abs(enemy.getY() - p1.getY());
        int dist2 = Math.abs(enemy.getX() - p2.getX()) + Math.abs(enemy.getY() - p2.getY());
        
        return (dist1 < dist2) ? p1 : p2;
    }
    
    /**
     * Controlla se il nemico è allineato con il giocatore e in caso affermativo attacca.
     */
    public boolean manageAttack(Enemy enemy, Player targetPlayer) {
        if (targetPlayer == null) return false;
        
        int targetMidY = targetPlayer.getY() + (targetPlayer.getHitbox().height / 2);
        
        // Controlla se il giocatore è all'incirca alla stessa altezza
        if (enemy.getY() - TILESIZE < targetMidY && targetMidY < enemy.getY() + TILESIZE * 2) {
            if (enemy.getDirection() == DirectionX.LEFT) {
                if (targetPlayer.getX() < enemy.getX()) {
                    enemy.attack();
                    return true;
                }
            } else { // Direzione a destra
                if (targetPlayer.getX() > enemy.getX()) {
                    enemy.attack();
                    return true;
                }
            }
        }
        return false;
    }
}